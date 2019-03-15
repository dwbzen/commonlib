package common.math.ifs;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import common.math.JsonObject;
import common.math.Matrix;
import common.math.Point2D;

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
public class IteratedFunctionSystem extends JsonObject {

	private static final long serialVersionUID = 1L;
	protected static final Logger log = LogManager.getLogger(IteratedFunctionSystem.class);
	
	public static final MathContext mathContext = new MathContext(5, RoundingMode.HALF_DOWN);	// precision is 5 decimal places
	public static final BigDecimal lowerLimit = new BigDecimal(1E-6);	// any number having an absolute value <= lowerLimit is set to 0.0
	
	public static final String objectType = "IFS";
	
	@JsonProperty	private List<LinearFunction> functions = new ArrayList<LinearFunction>();
	@JsonProperty	private double range = 2.0;
	@JsonProperty	private double low = -1.0;	// [ LOW, LOW + RANGE ]
	
	@JsonIgnore		private double totalWeight = 0.0;
	@JsonIgnore		private Document doc;
	@JsonIgnore		private ThreadLocalRandom random = ThreadLocalRandom.current();

	protected IteratedFunctionSystem() {
		this.type = objectType;
	}
	
	public IteratedFunctionSystem(String ifsName) {
		this.type = objectType;
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
		parseXMLFile(xmlFile);
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
	private void parseXMLFile(File xmlFile) throws ParserConfigurationException, SAXException, IOException {
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
	}
	
	/**
	 * Gets and saves known variations for a given transform (xform) Element
	 * @param element
	 */
	private void getVariations(Element element) {
		
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

	public LinearFunction pickFunction() {
		double d = random.nextDouble(totalWeight);
		double sum = 0;
		LinearFunction linearFunction = null;
		for(LinearFunction f : functions) {
			sum += f.getWeight();
			linearFunction = f;
			if(d <= sum) {
				break;
			}
		}
		return linearFunction;
	}
	
	public List<LinearFunction> getFunctions() {
		return this.functions;
	}
	
	public int addFunction(LinearFunction f) {
		return addFunction(f, f.getWeight());
	}
	
	public int addFunction(LinearFunction f, double weight) {
		LinearFunction lf = new LinearFunction(f);
		lf.setWeight(weight);
		totalWeight += weight;
		functions.add(lf);
		return functions.size();
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
	
	/**
	 * Sierpinksi in upper left diagonal of square canvas. Data set sierpinski
	 * In Apophysis the .flame file transform gives the coeficients in order  { {a, c, e}, {b, d, f} } 
	 *    
	 *  <xform weight="0.5" color="0" linear="1" coefs="0.5  0  0     0.5   0.5  -0.5"  opacity="1" />
   	 *  <xform weight="0.5" color="0" linear="1" coefs="0.5  0  0     0.5   0.5   0"    opacity="1" />
     *  <xform weight="0.5" color="0" linear="1" coefs="0.5  0  0     0.5   0     0"    opacity="1" />
     * In the Apophysis editor UI:
     * <code>
     * Transform 1
     * 	X	0.5		  0
     *  Y	  0		0.5
     *  O   0.5		0.5
     *  
     *  Transform 2
     *  X	0.5		  0
     *  Y	  0		0.5
     *  O   0.5		  0
     *  
     *  Transform 3
     *  X	0.5		  0
     *  Y	  0		0.5
     *  O     0		  0
     *  </code>
	 * @return IteratedFunctionSystem
	 */
	public static IteratedFunctionSystem Sierpinski() {
		IteratedFunctionSystem ifs = new IteratedFunctionSystem("Sierpinski");
		
		double[][] dm1 = { {.5, 0, 0}, {0, .5, 0} };
		double[][] dm2 = { {.5, 0, .5}, {0, .5, 0} };
		double[][] dm3 = { {.5, 0, 0}, {0, .5, .5} };
		double weight = 0.5;
		LinearFunction f1 = new LinearFunction(dm1);
		f1.setName("f1");
		LinearFunction f2 = new LinearFunction(dm2);
		f2.setName("f2");
		LinearFunction f3 = new LinearFunction(dm3);
		f3.setName("f3");
		
		ifs.addFunction(f1, weight);
		ifs.addFunction(f2, weight);
		ifs.addFunction(f3, weight);
		
		return ifs;
	}
	
	/**
	 * In Apophysis the .flame file transform gives the coeficients in order { {a, c, e}, {b, d, f} } </p>
	 * 
	 * xform weight="0.5" color="0" linear="1" coefs="0.51 -0.1 -0.2 0.52 0.53 -0.54" </p>
	 * xform weight="0.5" color="0" linear="1" coefs="0.5 0 0 0.5 0.5 0"  </p>
	 * xform weight="0.5" color="0" linear="1" coefs="0.5 0 0 0.5 0 0" </p>
	 * 
	 * In the Apophysis editor UI: </p>
     * Transform 1, L-R, T-B reads a, b, c, d, e, f  </p>
     * 	X	0.51	0.1 </p>
     *  Y	0.2		0.52 </p>
     *  O   0.53	0.54 </p>
     *  
	 * @return IteratedFunctionSystem
	 */
	public static IteratedFunctionSystem Sierpinski1() {
		IteratedFunctionSystem ifs = new IteratedFunctionSystem("Sierpinski1");
		
		double[][] dm1 = { {0.51, -0.2, 0.53}, {-0.1, 0.52, -0.54} };
		double[][] dm2 = { {.5, 0, .5}, {0, .5, 0} };
		double[][] dm3 = { {.5, 0, 0}, {0, .5, 0} };
		double weight = 0.5;
		LinearFunction f1 = new LinearFunction(dm1);
		f1.setName("f1");
		LinearFunction f2 = new LinearFunction(dm2);
		f2.setName("f2");
		LinearFunction f3 = new LinearFunction(dm3);
		f3.setName("f3");
		
		Variation v5 = Variation.createNew("spherical", .5);
		f1.addVariation(v5);
		
		ifs.addFunction(f1, weight);
		ifs.addFunction(f2, weight);
		ifs.addFunction(f3, weight);
		
		return ifs;
	}

	/**
	 * Sierpinksi in upper right diagonal of square canvas.
	 * @return
	 */
	public static IteratedFunctionSystem Sierpinski2() {
		IteratedFunctionSystem ifs = new IteratedFunctionSystem("Sierpinski2");
		
		double[][] dm1 = { {.5, 0, .5}, {0, .5, .5} };
		double[][] dm2 = { {.5, 0, .5}, {0, .5, 0} };
		double[][] dm3 = { {.5, 0, 0}, {0, .5, 0} };
		double weight = 0.5;
		LinearFunction f1 = new LinearFunction(dm1);
		f1.setName("f1");
		LinearFunction f2 = new LinearFunction(dm2);
		f2.setName("f2");
		LinearFunction f3 = new LinearFunction(dm3);
		f3.setName("f3");
		
		ifs.addFunction(f1, weight);
		ifs.addFunction(f2, weight);
		ifs.addFunction(f3, weight);
		
		return ifs;
	}

	/**
	 * Sierpinksi gasket centered with point up
	 * Data set sierpinski
	 * @return
	 */
	public static IteratedFunctionSystem Sierpinski3() {
		IteratedFunctionSystem ifs = new IteratedFunctionSystem("Sierpinski3");

		double[][] dm1 = { {.5, 0, 0}, {0, .5, .5} };
		double[][] dm2 = { {.5, 0, .5}, {0, .5, .5} };
		double[][] dm3 = { {.5, 0, .25}, {0, .5, 0} };
		double weight = 1D/3D;
		LinearFunction f1 = new LinearFunction(dm1);
		f1.setName("f1");
		LinearFunction f2 = new LinearFunction(dm2);
		f2.setName("f2");
		LinearFunction f3 = new LinearFunction(dm3);
		f3.setName("f3");
		
		ifs.addFunction(f1, weight);
		ifs.addFunction(f2, weight);
		ifs.addFunction(f3, weight);
		
		return ifs;
	}
	
	/**
	 * Sierpinksi gasket centered with point up with Variations
	 * Data set sierpinski3
	 * @return
	 */
	public static IteratedFunctionSystem Sierpinski3Variations() {
		IteratedFunctionSystem ifs = new IteratedFunctionSystem("Sierpinski3Variations");

		double[][] dm1 = { {.5, 0, 0}, {0, .5, .5} };
		double[][] dm2 = { {.5, 0, .5}, {0, .5, .5} };
		double[][] dm3 = { {.5, 0, .25}, {0, .5, 0} };
		double weight = 1D/3D;
		LinearFunction f1 = new LinearFunction(dm1);
		f1.setName("f1");
		LinearFunction f2 = new LinearFunction(dm2);
		f2.setName("f2");
		LinearFunction f3 = new LinearFunction(dm3);
		f3.setName("f3");
		
		Variation v1 = Variation.createNew("swirl", .5);
		Variation v2 = Variation.createNew("horseshoe", .5);
		Variation v3 = Variation.createNew("cylinder", .5);
		//Variation v4 = Variation.createNew("spiral", .5);
		f1.addVariation(v1).addVariation(v2);
		f2.addVariation(v1).addVariation(v2);
		f3.addVariation(v1).addVariation(v3);

		ifs.addFunction(f1, weight);
		ifs.addFunction(f2, weight);
		ifs.addFunction(f3, weight);
		
		return ifs;
	}
	
	/**
	 * Data set ifs1.
	 * @return
	 */
	public static IteratedFunctionSystem Flame1() {
		IteratedFunctionSystem ifs = new IteratedFunctionSystem("Flame1");
		double[][] dm1 = { {.5, 0, .25}, {0, .5, .5} };
		double[][] dm2 = { {.5, 0, .5}, {0, .5, 0} };
		double[][] dm3 = { {.5, 0, 0}, {0, .5, 0} };
		double[][] dm4 = { {.5, 0, 0}, {0, .5, .5} };
		double[][] dm5 = { {.5, 0, .5}, {0, .5, .5} };
		double[][] dm6 = { {.5, 0, .25}, {0, .5, 0} };

		double weight = 1D/8D;
		LinearFunction f1 = new LinearFunction(dm1, "f1");
		LinearFunction f2 = new LinearFunction(dm2, "f2");
		LinearFunction f3 = new LinearFunction(dm3, "f3");
		LinearFunction f4 = new LinearFunction(dm4, "f4");
		LinearFunction f5 = new LinearFunction(dm5, "f5");
		LinearFunction f6 = new LinearFunction(dm6, "f6");
		ifs.addFunction(f1, weight);
		ifs.addFunction(f2, weight);
		ifs.addFunction(f3, weight);
		ifs.addFunction(f4, weight);
		ifs.addFunction(f5, weight);
		ifs.addFunction(f6, 0.0);
		return ifs;
	}
	
	/**
	 * Data set ifs2
	 * @return
	 */
	public static IteratedFunctionSystem IFS2() {
		IteratedFunctionSystem ifs = new IteratedFunctionSystem("IFS2");
		double[][] dm1 = { {0.0, 0.65, 0.85}, {0.646604, 0.009, 0.54} };
		double[][] dm2 = { {0.576, 0.0, 0.78}, {0.297797, 0.0, 0.244441} };
		double[][] dm3 = { {.5, 0, 0}, {0, .5, .1} };
		double[][] dm4 = { {.5, 0, .5}, {0, .5, .5} };
		double[][] dm5 = { {.5, 0, .25}, {0, .5, 0} };

		LinearFunction f1 = new LinearFunction(dm1);
		f1.setName("f1");
		LinearFunction f2 = new LinearFunction(dm2);
		f2.setName("f2");
		LinearFunction f3 = new LinearFunction(dm3);
		f3.setName("f3");
		LinearFunction f4 = new LinearFunction(dm4);
		f4.setName("f4");
		LinearFunction f5 = new LinearFunction(dm5);
		f5.setName("f5");

		Variation v1 = Variation.createNew("swirl", .5);
		Variation v2 = Variation.createNew("sinusoidal", .5);
		Variation v3 = Variation.createNew("cylinder", .5);
		Variation v4 = Variation.createNew("spiral", .5);

		f1.addVariation(v1).addVariation(v4);
		f2.addVariation(v2).addVariation(v3);
		f3.addVariation(v1).addVariation(v2);
		f4.addVariation(v2).addVariation(v4);
		f5.addVariation(v3).addVariation(v4);

		ifs.addFunction(f1, 0.2);
		ifs.addFunction(f2, 0.2);
		ifs.addFunction(f3, 0.2);
		ifs.addFunction(f4, 0.2);
		ifs.addFunction(f5, 0.2);
		
		return ifs;

	}

	/**
	 *
	 * xform weight="0.333333333333333" swirl="0.5" horseshoe="0.5"  coefs="-0.080798 0.525611    0.906202 -0.146845   -0.371499 0.566276"</p>
	 * xform weight="0.333333333333333" swirl="0.5" cylinder="0.5"   coefs="-0.896688 -0.123683  -0.930711  0.010323   -0.442994 -1.309826" </p>
     * xform weight="0.333333333333333" sinusoidal="0.5" swirl="0.5" coefs="0.359647 -0.393232    0.544532  0.396986    0.48317 1.394057"</p>
	 * xform coeficient order is x1, x2, y1, y2, o1, o2 (across rows first) and for some odd reason, x2,y1,o2 are -1*value.</p>
	 *
	 * @return IteratedFunctionSystem
	 */
	public static IteratedFunctionSystem IFS3() {
		IteratedFunctionSystem ifs = new IteratedFunctionSystem("IFS3");
		double[][] dm1 = { {0.0, 0.65, 0.85}, {0.646604, 0.009, 0.54} };
		double[][] dm2 = { {0.576, 0.0, 0.78}, {0.297797, 0.0, 0.244441} };
		double[][] dm3 = { {.5, 0, 0}, {0, .5, .1} };
		//double[][] dm4 = { {.5, 0, .5}, {0, .5, .5} };
		//double[][] dm5 = { {.5, 0, .25}, {0, .5, 0} };

		LinearFunction f1 = new LinearFunction(dm1);
		f1.setName("f1");
		LinearFunction f2 = new LinearFunction(dm2);
		f2.setName("f2");
		LinearFunction f3 = new LinearFunction(dm3);
		f3.setName("f3");
		
		return ifs;
	}
	
	/**
	 * Data set ifs3
	 * @return
	 */
	public static IteratedFunctionSystem Sample3() {
		IteratedFunctionSystem ifs = new IteratedFunctionSystem("Sample3");
		double[][] dm1 = { {0.25, 0.5, 0.1}, {0.1, 0.5, 0.4} };
		double[][] dm2 = { {0.0, 0.24, 0.5}, {0.5, 0.25, 0.0} };
		LinearFunction f1 = new LinearFunction(dm1);
		f1.setName("f1");
		LinearFunction f2 = new LinearFunction(dm2);
		f2.setName("f2");
		
		ifs.addFunction(f1, 0.5);
		ifs.addFunction(f2, 0.5);
		
		return ifs;
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

	public Document getDoc() {
		return doc;
	}

	public double getTotalWeight() {
		return totalWeight;
	}
	
}
