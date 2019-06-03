package org.dwbzen.common.math.ifs;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import org.dwbzen.common.math.JsonObject;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public abstract class AbstractIteratedFunctionSystem extends JsonObject implements IIteratedFunctionSystem {

	private static final long serialVersionUID = 5269240422246926577L;
	public static BigDecimal lowerLimit = new BigDecimal(1E-4);	// any number having an absolute value <= lowerLimit is set to 0.0
	public static final String objectType = "IFS";
	
	@JsonProperty	protected int	precision = 4;
	@JsonProperty	private List<LinearFunction> functions = new ArrayList<LinearFunction>();
	
	@JsonIgnore		private double totalWeight = 0.0;
	@JsonIgnore		protected MathContext mathContext = new MathContext(precision, RoundingMode.HALF_DOWN);
	@JsonIgnore		protected ThreadLocalRandom random = ThreadLocalRandom.current();

	
	public AbstractIteratedFunctionSystem() {
		this.type = objectType;		// JsonObject.type
	}
	
	public int getPrecision() {
		return precision;
	}

	public void setPrecision(int precision) {
		this.precision = precision;
		 mathContext = new MathContext(precision, RoundingMode.HALF_DOWN);
	}

	public MathContext getMathContext() {
		return mathContext;
	}

	public void setMathContext(MathContext mathContext) {
		this.mathContext = mathContext;
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
	
	public double getTotalWeight() {
		return totalWeight;
	}
}
