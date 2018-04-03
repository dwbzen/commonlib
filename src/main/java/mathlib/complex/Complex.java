package mathlib.complex;

public class Complex extends Number implements Cloneable, Comparable<Number> {

	private static final long serialVersionUID = 2081526108466484928L;
	public static final double SQRT3 = Math.sqrt(3.0);
	protected static String IMAGINARY_SYMBOL = "i";
	protected static String OMEGA_SYMBOL = "w";
	
    public double x = 0.0;	// the real part
    public double y = 0.0;	// the imaginary part
    protected String imaginarySymbol = IMAGINARY_SYMBOL;
    
	public static void main(String args[]) {
    	for(int i=0; i<args.length; i++) {
    		Complex c = Complex.parseComplex(args[i]);
    		System.out.println("c: " + c);
    		
    		System.out.println("as Eisenstein: " + c.asEisenstein());
    		Complex e0=Complex.eisentstein(0,1);
    		Complex e1=Complex.eisentstein(1,1);
    		Complex e2=Complex.eisentstein(2, 1);
    		Complex e3=Complex.eisentstein(2, 2);
    		System.out.println("e0= " + e0);
    		System.out.println("e1= " + e1);
       		System.out.println("e2= " + e2);
       		System.out.println("e3= " + e3);
    		Complex cent = Complex.centroid(e1, e2, e3);
    		System.out.println("centroid: " + cent);
    		
    		Complex c2 = Complex.parseComplex(args[i]);
    		
    		ComplexSet list = new ComplexSet();
    		boolean add = list.add(c);
    		System.out.println("add: " + c + ":  " + add);
    		add = list.add(c2);
    		System.out.println("add: " + c2 + ":  " + add);

    	}
    }
    
    public Complex() {
    	this(0d, 0d);
    }
    public Complex(String s) {
    	this(Complex.parseComplex(s));
    }
    
    /**
        Constructs the complex number z = a + bi
        @param a Real part
        @param b Imaginary part
    */
    public Complex(double a,double b) {
        x=a;
        y=b;
    }
    
    public Complex(String reStr, String imStr) {
    	x=Double.parseDouble(reStr);
    	y=Double.parseDouble(imStr);
    }
    
    public Complex(Complex w) {
    	x = w.real();
    	y = w.imag();
    }
    
    @Override
    public Complex clone() {
    	return new Complex(x, y);
    }
    
    public void assign(Complex other) {
    	this.x = other.x;
    	this.y = other.y;
    }
    
    public boolean equals(Complex other) {
    	return other.x == x && other.y == y;
    }
    
    public boolean equals(Number o) {
    	if(o instanceof Complex) {
    		return equals((Complex)o);
    	}
    	else {
    		return o.doubleValue() == x && y == 0.0;
    	}
    }
    
	public int compareTo(Number o) {
		int result = 0;
		if(o instanceof Complex) {
			Complex other = (Complex)o;
			result = (other.x == x && other.y == y) ? 0 : (int)Math.signum( mod() - other.mod());
		}
		else {
			result = (o.doubleValue() == x) ? 0 : (int)Math.signum( mod() - o.doubleValue());
		}
		return result;
	}
    /**
        Real part of this Complex number 
        (the x-coordinate in rectangular coordinates).
        @return Re[z] where z is this Complex number.
    */
    public double real() {
        return x;
    }
    
    /**
        Imaginary part of this Complex number 
        (the y-coordinate in rectangular coordinates).
        @return Im[z] where z is this Complex number.
    */
    public double imag() {
        return y;
    }
    
    /**
        Modulus of this Complex number
        (the distance from the origin in polar coordinates).
        @return |z| where z is this Complex number.
    */
    public double mod() {
        if (x!=0 || y!=0) {
            return  Math.sqrt(x*x + y*y);
        } else {
            return 0d;
        }
    }
    /**
     * cabs(a + bi) = sqrt(a*a + b*b)
     * Same as modulus
     * @return  |z| where z is this Complex number.
     */
    public double cabs() {
    	return mod();
    }
    
    /**
     * Distance between z (this) and complex w
     * @param w
     * @return
     */
    public double distance(Complex w) {
    	return this.minus(w).cabs();
    }
    
    /**
     * Distance between two Complex numbers, z and w
     * @param z
     * @param w
     * @return
     */
    public static double distance(Complex z, Complex w) {
    	return z.minus(w).cabs();
    }
    
    public static Complex centroid(Complex z1, Complex z2, Complex z3) {
    	Complex cent = new Complex();
    	cent.x = (z1.x + z2.x + z3.x)/3;
    	cent.y = (z1.y + z2.y + z3.y)/3;
    	return cent;
    }
    
    public Complex asEisenstein() {
 	    double b = 2*y/SQRT3;
 	    double a = x + b/2.0;
	   	Complex z = new Complex(a,b);
	   	z.setImaginarySymbol(OMEGA_SYMBOL);
	   	return z;
   }
    
	public Complex asEisenstein(double len) {
    	double b = 2*y/(len*SQRT3);
    	double a = x - (y/SQRT3);
	   	Complex z = new Complex(a,b);
	   	return z;
	}
    
    public static Complex eisentstein(double a, double b) {
    	return new Complex(a-(b/2.0), b*SQRT3/2);
    }
    /**
     * abs(a + bi) = abs(a) + abs(b)i
     * @return
     */
    public Complex abs() {
    	return new Complex(Math.abs(x), Math.abs(y));
    }
    /**
        Argument of this Complex number 
        (the angle in radians with the x-axis in polar coordinates).
        @return arg(z) where z is this Complex number.
    */
    public double arg() {
        return Math.atan2(y,x);
    }
    
    /**
        Complex conjugate of this Complex number
        (the conjugate of x+i*y is x-i*y).
        @return z-bar where z is this Complex number.
    */
    public Complex conj() {
        return new Complex(x,-y);
    }
    
    /**
        Addition of Complex numbers (doesn't change this Complex number).
        <br>(x+i*y) + (s+i*t) = (x+s)+i*(y+t).
        @param w is the number to add.
        @return z+w where z is this Complex number.
    */
    public Complex plus(Complex w) {
        return new Complex(x+w.real(),y+w.imag());
    }
    /**
     * Adds a double to this Complex (doesn't change this Complex number).
     * Returns the sum as a new Complex()
     * @param a
     * @return the sum as a new Complex()
     */
    public Complex plus(double a) {
    	return new Complex(x + a, y);
    }
    
    public void add(double realAmount, double complexAmount) {
    	x+=realAmount;
    	y+=complexAmount;
    }
    
    public void add(Complex w) {
    	x+=w.real();
    	y+=w.imag();
    }
  
    
    /**
        Subtraction of Complex numbers (doesn't change this Complex number).
        <br>(x+i*y) - (s+i*t) = (x-s)+i*(y-t).
        @param w is the number to subtract.
        @return z-w where z is this Complex number.
    */
    public Complex minus(Complex w) {
        return new Complex(x-w.real(),y-w.imag());
    }
    
    public void subtract(double realAmount, double complexAmount) {
    	x-=realAmount;
    	y-=complexAmount;
    }
    public void subtract(Complex w) {
    	x-=w.real();
    	y-=w.imag();
    }
   
    /**
        Complex multiplication (doesn't change this Complex number).
        @param w is the number to multiply by.
        @return z*w where z is this Complex number.
    */
    public Complex times(Complex w) {
        return new Complex(x*w.real()-y*w.imag(),x*w.imag()+y*w.real());
    }
    public Complex times(double d) {
        return new Complex(x*d, y*d);
    }
    
    public void  mult(Complex w) {
    	double ox = x;
    	double oy = y;
    	x = ox*w.real() - oy*w.imag();
    	y = ox*w.imag() + oy*w.real();
    }
    public void mult(double d) {
    	x *= d;
    	y *= d;
    }
    
    /**
        Division of Complex numbers (doesn't change this Complex number).
        <br>(x+i*y)/(s+i*t) = ((x*s+y*t) + i*(y*s-y*t)) / (s^2+t^2)
        @param w is the number to divide by
        @return new Complex number z/w where z is this Complex number  
    */
    public Complex div(Complex w) {
        double den=Math.pow(w.mod(),2);
        return new Complex((x*w.real()+y*w.imag())/den,(y*w.real()-x*w.imag())/den);
    }
    
    /**
	    Division of Complex numbers (doesn't change this Complex number).
	    <br>(x+i*y)/s = ((x*s) + i*(y*s)) / (s^2)
	    @param w is the number to divide by
	    @return new Complex number z/d where z is this Complex number  
    */
    public Complex div(double d) {
        double den=d*d;
        return new Complex(x*d/den,y*d/den);    	
    }
    
    public void divideBy(Complex w) {
    	assign(div(w));
    }
    
    /**
        Complex exponential (doesn't change this Complex number).
        @return exp(z) where z is this Complex number.
    */
    public Complex exp() {
        return new Complex(Math.exp(x)*Math.cos(y),Math.exp(x)*Math.sin(y));
    }
    
    /**
        Principal branch of the Complex logarithm of this Complex number.
        (doesn't change this Complex number).
        The principal branch is the branch with -pi < arg <= pi.
        @return log(z) where z is this Complex number.
    */
    public Complex log() {
        return new Complex(Math.log(this.mod()),this.arg());
    }
    
    public Complex trunc() {
    	return new Complex((long)x, (long)y);
    }
    
    /**
        Complex square root (doesn't change this complex number).
        Computes the principal branch of the square root, which 
        is the value with 0 <= arg < pi.
        @return sqrt(z) where z is this Complex number.
    */
    public Complex sqrt() {
        double r=Math.sqrt(this.mod());
        double theta=this.arg()/2;
        return new Complex(r*Math.cos(theta),r*Math.sin(theta));
    }
    
    // Real cosh function (used to compute complex trig functions)
    private double cosh(double theta) {
        return (Math.exp(theta)+Math.exp(-theta))/2;
    }
    
    // Real sinh function (used to compute complex trig functions)
    private double sinh(double theta) {
        return (Math.exp(theta)-Math.exp(-theta))/2;
    }
    
    /**
        Sine of this Complex number (doesn't change this Complex number).
        <br>sin(z) = (exp(i*z)-exp(-i*z))/(2*i).
        @return sin(z) where z is this Complex number.
    */
    public Complex sin() {
        return new Complex(cosh(y)*Math.sin(x),sinh(y)*Math.cos(x));
    }
    
    /**
        Cosine of this Complex number (doesn't change this Complex number).
        <br>cos(z) = (exp(i*z)+exp(-i*z))/ 2.
        @return cos(z) where z is this Complex number.
    */
    public Complex cos() {
        return new Complex(cosh(y)*Math.cos(x),-sinh(y)*Math.sin(x));
    }
    
    /**
        Hyperbolic sine of this Complex number 
        (doesn't change this Complex number).
        <br>sinh(z) = (exp(z)-exp(-z))/2.
        @return sinh(z) where z is this Complex number.
    */
    public Complex sinh() {
        return new Complex(sinh(x)*Math.cos(y),cosh(x)*Math.sin(y));
    }
    
    /**
        Hyperbolic cosine of this Complex number 
        (doesn't change this Complex number).
        <br>cosh(z) = (exp(z) + exp(-z)) / 2.
        @return cosh(z) where z is this Complex number.
    */
    public Complex cosh() {
        return new Complex(cosh(x)*Math.cos(y),sinh(x)*Math.sin(y));
    }
    
    /**
        Tangent of this Complex number (doesn't change this Complex number).
        <br>tan(z) = sin(z)/cos(z).
        @return tan(z) where z is this Complex number.
    */
    public Complex tan() {
        return (this.sin()).div(this.cos());
    }
    
    /**
        Negative of this complex number (chs stands for change sign). 
        This produces a new Complex number and doesn't change 
        this Complex number.
        <br>-(x+i*y) = -x-i*y.
        @return -z where z is this Complex number.
    */
    public Complex chs() {
        return new Complex(-x,-y);
    }
 
    public String getImaginarySymbol() {
		return imaginarySymbol;
	}

	public void setImaginarySymbol(String imaginarySymbol) {
		this.imaginarySymbol = imaginarySymbol;
	}

    /**
        String representation of this Complex number.
        @return x+i*y, x-i*y, x, or i*y as appropriate.
    */
    public String toString() {
        if (x!=0 && y>0) {
            return x+" + "+y + imaginarySymbol;
        }
        if (x!=0 && y<0) {
            return x+" - "+(-y)+imaginarySymbol;
        }
        if (y==0) {
            return String.valueOf(x);
        }
        if (x==0) {
            return y+imaginarySymbol;
        }
        // shouldn't get here (unless Inf or NaN)
        return x+" + i*"+y;
        
    }
    /**
     * format is a + bi or a - bi
 	 * where a, b are floating point or integers
 	 * either the real or imaginary part can be absent
 	 * so '5.3i' is valid, and so are '200.1', '-3.1i'
     * spaces not important
     * Pair format: (re, im) where re = real part, im = imaginary part
     * @param cs
     * @return a Complex number
     */
    public static Complex parseComplex(String cstring) throws IllegalArgumentException {
    	
    	String cs = removeAllChars(cstring, ' ');
    	if(cs.charAt(0)== '(') {
    		return parseComplexPairFormat(cs);
    	}
    	else {
    		return parseComplexStandardFormat(cs);
    	}
    }
    
    /**
     * (re, im)
     * @param cs
     * @return Complex
     */
    private static Complex parseComplexPairFormat(String cs) {
       	Complex c = new Complex();
       	int ind = cs.indexOf(',');
       	int ind2 = cs.indexOf(')');
       	if(ind >= 0 && ind2 > ind) {
       		c.x = Double.parseDouble(cs.substring(1, ind));
       		c.y = Double.parseDouble(cs.substring(ind+1, ind2));
       	}
       	return c;
    }
    
    /**
     * a + bi
     * @param cs
     * @return Complex
     */
    private static Complex parseComplexStandardFormat(String cs) {
    	Complex c = new Complex();
    	int startind = (cs.charAt(0)== '-' || cs.charAt(0)== '+') ? 1 : 0;
    	
    	int ind1 = cs.indexOf('+', startind);	// could be 2 signs as in "-3.1 + 5i"
    	int ind = (ind1 < 0) ? cs.indexOf('-', startind) : ind1;
    	int iindex = cs.indexOf('i');
    	if(iindex < 0) {	// real only
    		c.x = Double.parseDouble(cs);
    	}
    	else {
	    	if(ind > 0) {	// position of + or - after optional sign
	    		if(ind > iindex) {	// the sign has to come before the 'i'
	    			throw new IllegalArgumentException(cs);
	    		}
	    		c.x = Double.parseDouble(cs.substring(0, ind));
	    		c.y = Double.parseDouble(cs.substring(ind, iindex));
	    	}
	    	else {	// imaginary only
	    		c.y = Double.parseDouble(cs.substring(0, iindex));
	    	}
    	}
    	return c;
    }
    
    protected static String removeAllChars(String s, char c) {
    	StringBuffer sb = new StringBuffer();
    	for(int i=0; i<s.length(); i++) {
    		if(s.charAt(i)!=c)
    			sb.append(s.charAt(i));
    	}
    	return sb.toString();
    }
    
	public void setReal(double x) {
		this.x = x;
	}

	public void setImag(double y) {
		this.y = y;
	}
	/**
	 * Returns the real part as a double
	 */
	@Override
	public double doubleValue() {
		return this.x;
	}
	/**
	 * Returns the real part as a float
	 */
	@Override
	public float floatValue() {
		return (float)x;
	}
	/**
	 * Returns the real part, rounded, as an int
	 */
	@Override
	public int intValue() {
		return (int)Math.round(x);
	}
	/**
	 * Returns the real part, rounded, as a long
	 */
	@Override
	public long longValue() {
		return Math.round(x);
	}
	
	public Complex floor() {
		return new Complex(Math.floor(x), Math.floor(y));
	}
	
	public Complex ceil() {
		return new Complex(Math.ceil(x), Math.ceil(y));
	}
}
