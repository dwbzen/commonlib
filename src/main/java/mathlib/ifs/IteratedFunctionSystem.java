package mathlib.ifs;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import mathlib.Matrix;
import mathlib.Point2D;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * Represents a general 2-dimensional IFS
 * This is a collection of n functions Fi(x,y)
 * Fi(x,y) = (ax + by + c, dx + ey + f)
 * Each function F can be represented as a 2-row by
 * 3-column matrix T(2,3):
 * [ [ a, b, c ], [ d, e, f ] ]
 * 
 * so F <==> T * [x,y,1]  (matrix multiplication)
 * Each F has a weight: 0 < weight < 1
 * such that the sum of the weights == 1
 * 
 * @author dbacon
 *
 */
public class IteratedFunctionSystem {
	protected static final Logger log = LogManager.getLogger(IteratedFunctionSystem.class);
	static MathContext context = MathContext.DECIMAL32;	// precision is 7 decimal places
	
	/**
	 * the function system each Matrix must be 2 x 3
	 */
	private List<LinearFunction> functions = new ArrayList<LinearFunction>();
	private ThreadLocalRandom random = ThreadLocalRandom.current();
	private double range = 2.0;
	private double low = -1.0;	// [ LOW, LOW + RANGE ]
	private String flame = null;
	private Document doc;

	public IteratedFunctionSystem() {
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
	 * Variations specified per xform as an attribute, for example:
	 * bubble="0.381160313030705" blur="0.618839686969295" sinusoidal="0.077552528353408" polar="0.922447471646592"
	 * eyefish="0.693151000887156" disc="0.34543321817182"
	 * @param flameFile
	 * @param flameName
	 */
	public IteratedFunctionSystem(String flameFile, String flameName) throws Exception {
		File xmlFile = new File(flameFile);
		this.flame = flameName;
		parseXMLFile(xmlFile);
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
		for(int i=0; i<flameNList.getLength(); i++) {
			Node node = flameNList.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				flameElement = (Element) node;
				String fname = flameElement.getAttribute("name");
				if(fname.equalsIgnoreCase(flame)) {
					found = true;
					break;
				}
			}
		}
		if(!found) {
			throw new IllegalArgumentException("The named flame: '" + flame + "' not found in this flame file");
		}
		/*
		 * Get coefs and weights from this <flame> element
		 * example:
		 *  weight="0.333333333333333" 
		 * 	coefs="0.192318735701774 0.626289901255116 -0.85410800259596 0.698886714971427 -0.292309265118092 -0.853723430074751"
		 * xform coeficient order is x1,x2,y1,y2,o1,o2 (across rows first)
		 * and for some odd reason, x2,y1,o2 are -1*value. So the Apophysis UI in the above example
		 * shows x2 as -0.62629, y1 as 0.854108, and o2 as 0.853723
		 * 
		 */
		Matrix<Number> function = null;
		NodeList nList = flameElement.getElementsByTagName("xform");
		for(int i=0; i<nList.getLength(); i++) {
			Node node = nList.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				Element element = (Element) node;
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
							dm[icol] = -bd.round(context).floatValue();
						}
						else {
							dm[icol] = bd.round(context).floatValue();
						}
					}
					function.addRow(dm);
				}
				if(weight >= 0.00001)  {	// don't add if no weight
					LinearFunction lf = new LinearFunction(function);
					lf.setWeight(weight);
					log.debug(lf.toJSON());
					this.addFunction(lf);
				}
			}
		}
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
			System.out.println(lf.toJSON());
		}
	}

	public LinearFunction pickFunction() {
		double d = random.nextDouble();
		LinearFunction f = null;
		double sum = 0;
		for(Iterator<LinearFunction> it=functions.iterator(); it.hasNext();) {
			if(sum > d) {
				break;
			}
			f = it.next();
			sum += f.getWeight();
		}
		return f;
	}
	
	public List<LinearFunction> getFunctions() {
		return this.functions;
	}
	
	public int addFunction(LinearFunction f) {
		functions.add(f);
		return functions.size();
	}
	public int addFunction(LinearFunction f, double weight) {
		LinearFunction lf = new LinearFunction(f);
		lf.setWeight(weight);
		functions.add(lf);
		return functions.size();
	}
	/**
	 * Normalize weights to range [0, 1]
	 * @return
	 */
	public int distributeWeights() {
		double sum = 0D;
		for(Iterator<LinearFunction> it=functions.iterator(); it.hasNext();) {
			sum += it.next().getWeight();
		}
		for(Iterator<LinearFunction> it=functions.iterator(); it.hasNext();) {
			LinearFunction f = it.next();
			f.setWeight(f.getWeight()/sum);
		}
		return functions.size();
	}
	
	/**
	 * Gets a random point in the range x,y :: [-1, +1]
	 * @return
	 */
	public Point2D<BigDecimal> getRandomPoint() {
		double x = (random.nextDouble()*range) + low;
		double y = (random.nextDouble()*range) + low;
		return new Point2D<BigDecimal>(x,y);
	}
	
	/**
	 * Sierpinksi in upper left diagonal of square canvas.
	 * Data set sierpinski
	 * @return
	 */
	public static IteratedFunctionSystem Sierpinski() {
		IteratedFunctionSystem ifs = new IteratedFunctionSystem();
		
		double[][] dm1 = { {.5, 0, 0}, {0, .5, 0} };
		double[][] dm2 = { {.5, 0, .5}, {0, .5, 0} };
		double[][] dm3 = { {.5, 0, 0}, {0, .5, .5} };
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
	 * Sierpinksi in upper right diagonal of square canvas.
	 * Data set sierpinski2
	 * @return
	 */
	public static IteratedFunctionSystem Sierpinski2() {
		IteratedFunctionSystem ifs = new IteratedFunctionSystem();
		
		double[][] dm1 = { {.5, 0, .5}, {0, .5, .5} };
		double[][] dm2 = { {.5, 0, .5}, {0, .5, 0} };
		double[][] dm3 = { {.5, 0, 0}, {0, .5, 0} };
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
	 * Sierpinksi gasket centered with point up
	 * Data set sierpinski
	 * @return
	 */
	public static IteratedFunctionSystem Sierpinski3() {
		IteratedFunctionSystem ifs = new IteratedFunctionSystem();

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
	 * Data set ifs1.
	 * @return
	 */
	public static IteratedFunctionSystem Flame1() {
		IteratedFunctionSystem ifs = new IteratedFunctionSystem();
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
		IteratedFunctionSystem ifs = new IteratedFunctionSystem();
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

		ifs.addFunction(f1, 0.2);
		ifs.addFunction(f2, 0.2);
		ifs.addFunction(f3, 0.2);
		ifs.addFunction(f4, 0.2);
		ifs.addFunction(f5, 0.2);
		
		return ifs;

	}

	/**
	 * Data set ifs3
	 * @return
	 */
	public static IteratedFunctionSystem Sample3() {
		IteratedFunctionSystem ifs = new IteratedFunctionSystem();
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

	public String getFlame() {
		return flame;
	}

	public void setFlame(String flame) {
		this.flame = flame;
	}

	public Document getDoc() {
		return doc;
	}
	
}