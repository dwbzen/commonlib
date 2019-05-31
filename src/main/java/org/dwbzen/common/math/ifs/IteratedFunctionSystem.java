package org.dwbzen.common.math.ifs;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dwbzen.common.math.Matrix;
import org.dwbzen.common.math.Point2D;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

/**
 * Represents a general 2-dimensional IFS
 * This is a collection of n functions Fi(x,y)
 * Fi(x,y) = (ax + by + c, dx + ey + f)
 * Each function F can be represented as a 2-row by 3-column matrix T(2,3):
 * [ [ a, b, c ], [ d, e, f ] ]
 * 
 * so F <==> T * [x,y,1]  (matrix multiplication)
 * Each F has a weight: 0 < weight < 1
 * that is interpreted as a relative probability of that transform being
 * selected when the ChaosGame is run.
 * 
 * @author don_bacon
 *
 */
public class IteratedFunctionSystem extends AbstractIteratedFunctionSystem {

	private static final long serialVersionUID = 1L;

	protected static final Logger log = LogManager.getLogger(IteratedFunctionSystem.class);
	
	@JsonProperty	private double range = 2.0;
	@JsonProperty	private double low = -1.0;	// [ LOW, LOW + RANGE ]

	protected IteratedFunctionSystem() {
		super();
	}
	
	public IteratedFunctionSystem(String ifsName) {
		super();
		this.name = ifsName;
	}
	
	/**
	 * Create an IteratedFunctionSystem from transform coeficients in
	 * an Apophysis flame (XML) file.
	 * Overall structure is:
	 * <flames name="name">
	 *   <flame name="fname">
	 *   	<xform .... />    repeat as needed
	 *      <palette count="256" format="RGB">
	 *      	data
	 *      </palette>
	 *    </flame>
	 * </flames>
	 * Transforms look like this: <xform weight="0.333" color="0" linear="1" coefs="0.5 0 0 0.5 0.5 -0.5" opacity="1" />
	 * Variations specified per xform as an attribute. The values for a given transform sum to 1. Here are some examples:</p>
	 * linear="0.119537904160097" 	hyperbolic="0.880462095839903" </p>
	 * flatten="0.217206117464229" 	disc="0.78279388253577" </p>
	 * diamond="0.0912946739699692" eyefish="0.908705326030031" </p>
	 * linear="0.535267039667815" 	spiral="0.464732960332185" </p>
	 * swirl="0.5" 	horseshoe="0.5"; swirl="0.5" cylinder="0.5"; sinusoidal="0.5" swirl="0.5" </p>
	 * 
	 * @param flameFile
	 * @param flameName
	 */
	public IteratedFunctionSystem(String flameFile, String flameName) throws Exception {
		this(flameName);
		File xmlFile = new File(flameFile);
		parseXMLFlameFile(xmlFile);
	}
	
	public static IteratedFunctionSystem fromJson(String jsonstr) {
		IteratedFunctionSystem ifs = null;
		try {
			ifs = mapper.readValue(jsonstr, IteratedFunctionSystem.class);
		} catch (JsonParseException e) {
			log.error("JsonParseException (Point2D): " + jsonstr);
			e.printStackTrace();
		} catch (JsonMappingException e) {
			log.error("JsonMappingException (Point2D): " + jsonstr);
			e.printStackTrace();
		} catch (IOException e) {
			log.error("IOException: " + jsonstr);
			e.printStackTrace();
		}
		return ifs;
	}
	
	/**
	 * 
	 * @param xmlFile Apophysis .flame XML file
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 * @throws java.lang.IllegalArgumentException if the named flame is not found
	 */
	public List<LinearFunction> parseXMLFlameFile(File xmlFile) throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(xmlFile);
		log.debug("Root element :" + doc.getDocumentElement().getNodeName());
		NodeList flameNList =  doc.getElementsByTagName("flame");
		Element flameElement = null;
		boolean found = false;
		String flameName = null;
		for(int i=0; i<flameNList.getLength(); i++) {
			Node node = flameNList.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				flameElement = (Element) node;
				flameName = flameElement.getAttribute("name");
				if(flameName.equalsIgnoreCase(this.name)) {
					found = true;
					break;
				}
			}
		}
		if(!found) {
			throw new IllegalArgumentException("The named flame: '" + name + "' not found in this flame file");
		}
		/*
		 * Get coefs and weights from this <flame> element
		 * example:
		 *  weight="0.333333333333333" 
		 * 	coefs="0.192318735701774 0.626289901255116 -0.85410800259596 0.698886714971427 -0.292309265118092 -0.853723430074751"
		 * xform coeficient order is x1,x2,y1,y2,o1,o2 (across rows first)
		 * and for some odd reason, x2,y1,o2 are -1*value shown in the Apophysis UI
		 * So the Apophysis UI in the above example shows x2 as -0.62629, y1 as 0.854108, and o2 as 0.853723
		 * 
		 */
		Matrix<Number> function = null;
		NodeList nList = flameElement.getElementsByTagName("xform");
		for(int i=0; i<nList.getLength(); i++) {
			Node node = nList.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				Element element = (Element) node;
				getVariations(element);
				String coefs = element.getAttribute("coefs");
				double weight = Double.parseDouble(element.getAttribute("weight"));
				log.trace("coefs: " + coefs);
				String[] coefArray = coefs.split("\\s+");
				function = new Matrix<Number>(3);	// 3 columns x 2 rows
				for(int irow=0; irow<2; irow++) {
					double[] dm = new double[3];
					for(int icol=0; icol<3; icol++) {
						// 
						// 
						int index = irow + 2*icol;
						BigDecimal bd = new BigDecimal(Double.parseDouble(coefArray[index]) );
						if(index == 1 || index == 2 || index ==5) {
							dm[icol] = -bd.round(mathContext).floatValue();
						}
						else {
							dm[icol] = bd.round(mathContext).floatValue();
						}
					}
					function.addRow(dm);
				}
				if(weight >= 0.00001)  {	// don't add if no weight
					LinearFunction lf = new LinearFunction(function);
					lf.setWeight(weight);
					lf.setName("F"+i);
					log.debug(lf.toJson());
					addFunction(lf);
				}
			}
		}
		return getFunctions();
	}
	
	/**
	 * Gets and saves known variations for a given transform (xform) Element
	 * @param element
	 */
	private void getVariations(Element element) {
		// TODO 
	}

	public static void main(String... args) throws Exception {
		String filename = null;
		String flameName = null;
		
		for(int i=0; i<args.length;i++) {
			if(args[i].equalsIgnoreCase("-file")) {
				/*
				 * Specifies the name of a Apophysis flame file
				 * This is a XML file that specifies (among other things)
				 * the transform coeficients and name of the flame fractal
				 */
				filename = args[++i];
			}
			else if(args[i].equalsIgnoreCase("-flame")) {
				flameName = args[++i];
			}
		}
		if(filename == null || flameName == null) {
			log.error("Usage: IteratedFunctionSystem -file <file_name> -flame <flame_name>");
			System.exit(1);
		}
		IteratedFunctionSystem ifs = new IteratedFunctionSystem(filename, flameName);
		for(LinearFunction lf : ifs.getFunctions()) {
			System.out.println(lf.toJson());
		}
	}

	
	/**
	 * Gets a random point in the range x,y :: [-1, +1]
	 * @return
	 */
	@JsonIgnore
	public Point2D<Double> getRandomPoint() {
		double x = (random.nextDouble()*range) + low;
		double y = (random.nextDouble()*range) + low;
		return new Point2D<Double>(x,y);
	}
	
	public double getRange() {
		return range;
	}

	public void setRange(double range) {
		this.range = range;
	}

	public double getLow() {
		return low;
	}

	public void setLow(double low) {
		this.low = low;
	}

}
