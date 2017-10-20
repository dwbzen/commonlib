package mathlib;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Probability density function for Normal Distribution:
 * E^(-((x - \[Mu])^2/(2 \[Sigma]^2)))/(Sqrt[2 \[Pi]] \[Sigma])
 * where Mu is the mean (also median and mode)
 * and Sigma is the standard deviation
 * 
 * @author don_bacon
 *
 */
public class ProbabilityDensityFunction {
	private double[] range = {-5.0, 5.0};
	private double mu = 1.0;
	private double sigma = 0;
	private double factor;	// 1/sqrt(2 * Pi * mu^2)
	private ThreadLocalRandom random = ThreadLocalRandom.current();
	
	public ProbabilityDensityFunction(double mu, double sigma) {
		this.mu = mu;
		this.sigma = sigma;
		factor = 1/ Math.pow((2 * Math.PI * Math.pow(mu, 2)), .5);
	}
	
	/**
	 * Computes the probability density function value for a given x value.
	 * @param x double, presumably in the range
	 * @return the probability density function value
	 */
	public double pdf(double x) {
		double exp = Math.pow(x - mu, 2) / (2 * sigma* sigma);
		double result = factor * Math.pow(Math.E, exp);
		
		return result;
	}
	
	/**
	 * Returns the value of the probability density function for a
	 * random x value in the range.
	 * 
	 * @return
	 */
	public double randomPDF() {
		return random.nextDouble(range[0], range[1]);
	}

	public double[] getRange() {
		return range;
	}

	public void setRange(double[] range) {
		this.range = range;
	}

	public double getMu() {
		return mu;
	}

	public double getSigma() {
		return sigma;
	}

}
