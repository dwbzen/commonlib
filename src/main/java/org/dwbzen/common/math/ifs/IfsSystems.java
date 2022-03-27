package org.dwbzen.common.math.ifs;

/**
 * Built-in IteratedFunctionSystem instances
 * 
 * @author don_bacon
 *
 */
public class IfsSystems {

	/**
	 * Sierpinksi in upper left diagonal of square canvas. Data set sierpinski
	 * In Apophysis the .flame file transform gives the coeficients in order  { {a, c, e}, {b, d, f} } 
	 *    
	 *  <xform weight="0.5" color="0" linear="1" coefs="0.5  0  0     0.5   0.5  -0.5"  opacity="1" />
   	 *  <xform weight="0.5" color="0" linear="1" coefs="0.5  0  0     0.5   0.5   0"    opacity="1" />
     *  <xform weight="0.5" color="0" linear="1" coefs="0.5  0  0     0.5   0     0"    opacity="1" />
     * In the Apophysis editor UI:
     * <code>
     * Transform 1<br>
     * 	X	0.5		  0<br>
     *  Y	  0		0.5<br>
     *  O   0.5		0.5
     *  </p>
     *  Transform 2<br>
     *  X	0.5		  0<br>
     *  Y	  0		0.5<br>
     *  O   0.5		  0
     *  </p>
     *  Transform 3<br>
     *  X	0.5		  0<br>
     *  Y	  0		0.5<br>
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
	
	public static IteratedFunctionSystem Barnsley() {
		IteratedFunctionSystem ifs = new IteratedFunctionSystem("Barnsley");
		
		double[][] dm1 = { {0, 0, 0}, {0.16, 0, 0} };
		double[][] dm2 = { {0.85, 0.04, 0.0}, {-0.04, 0.85, 1.6} };
		double[][] dm3 = { {0.20, -0.26, 0.0}, {0.23, 0.22, 1.60} };
		double[][] dm4 = { {-0.15, 0.28, 0.0}, {0.26, 0.24, 0.44} };
		
		LinearFunction f1 = new LinearFunction(dm1);
		f1.setName("f1");
		LinearFunction f2 = new LinearFunction(dm2);
		f2.setName("f2");
		LinearFunction f3 = new LinearFunction(dm3);
		f3.setName("f3");
		LinearFunction f4 = new LinearFunction(dm4);
		f3.setName("f4");
		
		ifs.addFunction(f1, 0.01);
		ifs.addFunction(f2, 0.85);
		ifs.addFunction(f3, 0.07);
		ifs.addFunction(f4, 0.07);
		
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
	 * xform coeficient order is x1, x2, y1, y2, o1, o2 (across rows first) 
	 * and for some odd reason the values displayed in Transforms for x2,y1,o2 are -1*value in the flame file</p>
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
		
		/*
		Variation v1 = Variation.createNew("swirl", .5);
		Variation v2 = Variation.createNew("sinusoidal", .5);
		Variation v3 = Variation.createNew("cylinder", .5);
		Variation v4 = Variation.createNew("horseshoe", .5);
		f1.addVariation(v1);
		f2.addVariation(v1);
		f3.addVariation(v1);
		*/
		
		ifs.addFunction(f1, 0.25);
		ifs.addFunction(f2, 0.45);
		ifs.addFunction(f3, 0.3);
		
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

}
