package org.dwbzen.common.math.ifs;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.dwbzen.common.math.CommandMessage;
import org.dwbzen.common.math.IPointProducer;
import org.dwbzen.common.math.OrderedPair;
import org.dwbzen.common.math.Point2D;
import org.dwbzen.common.math.PointSet;

/**
 * Implementation of the stocastic chaos game for IFS approximation
 * @author don_bacon
 *
 */
public class ChaosGame implements IPointProducer {

	protected static final Logger log = LogManager.getLogger(ChaosGame.class);
	
	private IteratedFunctionSystem ifs;
	private int maxIterations = 100000;
	private int functionIterations = 21;
	private int count;
	private int repeats = 1;
	PointSet<Double> points = new PointSet<Double>();
	private OrderedPair<Integer, Integer> scaleDimensions = null;
	private Map<Function<Point2D<Double>, Point2D<Double>>, Integer> linearFunctionCounts = new HashMap<>();
	// a count of #times a point is the result of a linear function.
	private Map<Point2D<Double>, Integer> pointHistogram = new HashMap<>();
	
	protected ChaosGame() {
	}
	
	public ChaosGame(IteratedFunctionSystem ifs) {
		this.ifs = ifs;
	}
	public ChaosGame(IteratedFunctionSystem ifs,int maxit) {
		this(ifs, maxit, 1);
	}
	public ChaosGame(IteratedFunctionSystem ifs, int maxit, int reps) {
		this.ifs = ifs;
		maxIterations = maxit;
		repeats = reps;
	}
	public ChaosGame(int maxit) {
		this.maxIterations = maxit;
	}
	
	public void start() {
		count = 0;
		ifs.getFunctions().forEach(f -> linearFunctionCounts.put(f, 0));
	}
	
	public boolean isComplete() {
		return count >= maxIterations;
	}
	/**
	 * Pick a random point in (x,y) in [-1, +1]
	 * Pick a LinearFunction (random - based on weight)
	 * Iteratively evaluate for functionIterations (20) times, starting at point
	 * Return the result at the end
	 * 
	 * @return
	 */
	public Point2D<Double> next(Point2D<Double> point) {
		LinearFunction f = ifs.pickFunction();
		log.debug("picked " + f.getName() + " " + point.toString()); 
		point = f.apply(point);
		log.debug("next point: " + point.toString());
		count++;
		int functionCount = getFunctionCount(f) + 1;
		linearFunctionCounts.put(f, functionCount);
		return new Point2D<Double>(point.getX(), point.getY());
	}
	/**
	 * (x, y)= a random point in the bi-unit square
	 * iterate { i = a random integer from 0 to n to 1 inclusive
	 *		(x, y) = Fi(x, y)
	 *		plot(x, y) except during the first 20 iterations
	 * }
	 * @return
	 */
	public PointSet<Double>  run() {
		for(int i=0; i<repeats; i++) {
			Point2D<Double> point = ifs.getRandomPoint();
			log.debug("start: " + point);
			start();
			while(!isComplete()) {
				 point = next(point);
				 if(count >= functionIterations) {		// don't plot the first 20 points
					 if(pointHistogram.containsKey(point)) {
						 int count = pointHistogram.get(point).intValue() + 1;
						 points.getPoints().remove(point);
						 point.setCount(count);
						 pointHistogram.put(point, count);
						 points.getPoints().add(point);
					 }
					 else {
						 points.add(point);
						 pointHistogram.put(point, 1);
					 }
				 }
			}
		}
		return points;
	}
	
	public IteratedFunctionSystem getIfs() {
		return ifs;
	}
	public void setIfs(IteratedFunctionSystem ifs) {
		this.ifs = ifs;
	}
	public int getMaxIterations() {
		return maxIterations;
	}
	public void setMaxIterations(int maxIterations) {
		this.maxIterations = maxIterations;
	}
	public int getFunctionIterations() {
		return functionIterations;
	}
	public void setFunctionIterations(int functionIterations) {
		this.functionIterations = functionIterations;
	}
	
	public int getRepeats() {
		return repeats;
	}
	public void setRepeats(int repeats) {
		this.repeats = repeats;
	}
	
	public int getFunctionCount(LinearFunction f) {
		return linearFunctionCounts.get(f).intValue();
	}
	
	public OrderedPair<Integer, Integer> getScaleDimensions() {
		return scaleDimensions;
	}

	public void setScaleDimensions(OrderedPair<Integer, Integer> scaleDimensions) {
		this.scaleDimensions = scaleDimensions;
	}

	public PointSet<Double> getPoints() {
		return points;
	}

	public Map<Function<Point2D<Double>, Point2D<Double>>, Integer> getLinearFunctionCounts() {
		return linearFunctionCounts;
	}

	public Map<Point2D<Double>, Integer> getPointHistogram() {
		return pointHistogram;
	}

	/**
	 * Usage: ChaosGame [-n num] [-name datasetname] [-ifs ifsname] [-start text] [-trailing text] > filename.json
	 * where num is #iterations (defaults to 10000)
	 * -start "START" -trailing "SHUTDOWN" is also default if not specified
	 * ifsname is the name of one of the builtin IteratedFunctionSystems 
	 * 
	 * Outputs JSON format suitable for importing into MongoDB
	 * Example 1:
	 * 1. java -jar ChaosGame.jar -n 10000 -name ifs1 -start START -trailing SHUTDOWN > ifs1.json
	 * 2. mongoimport --type json --collection ifs1 --db music --file ifs1.json
	 * 3. mongoimport --type json --collection ifs2 --db music --file ifs2.json
	 * 
	 * Example 2:
	 * java -jar ChaosGame.jar -n 10000 -name flame1 -start START -trailing SHUTDOWN > flame.json
	 * 
	 * Example 3:
	 * 1. java -jar ChaosGame.jar -n 10000 -name ifs2 -start START -trailing SHUTDOWN > ifs2.json
	 * 2. mongoimport --type json --collection ifs2 --db music --file ifs2.json
	 * 
	 * Example 4: read from Apophysis .flame XML file
	 * 1. java -jar ChaosGame.jar -file "C:\\Users\\DWBZe\\Documents\\fractals\\Apophysis\\Apo7X-170131-20.flame" -flame "Apo7X-170131-20" -name "Apo7X_170131" > Apo7X_170131.json
	 * 2. mongoimport --type json --collection "Apo7X_170131" --db music --file Apo7X_170131.json
	 * 
	 * Example 5: built-in IteratedFunctionSystem
	 *  1. java -jar ChaosGame.jar -n 10000 -name sierpinski3 -ifs sierpinski3 > sierpinski3.json
	 *  2. mongoimport --type json --collection sierpinski3 --db music --file sierpinski3.json
	 *   
	 * Sample query:
	 * db.points.find({name:"ifs1", type:"Point2D"})
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		int niterations = 100000;
		int nrepeats = 1;
		String dataSetName = "ifs1";
		String trailingMessage = "SHUTDOWN";
		String startMessage = "START";
		String filename = null;
		String flameName = null;
		String ifsname = null;
		IteratedFunctionSystem ifs = null;
		String[] scale = null;
		OrderedPair<Integer, Integer> scaleDimensions = null;
		
		for(int i=0; i<args.length;i++) {
			if(args[i].equalsIgnoreCase("-n")) {
				niterations = Integer.parseInt(args[++i]);
			}
			if(args[i].startsWith("-rep")) {
				nrepeats = Integer.parseInt(args[++i]);
			}			
			if(args[i].equalsIgnoreCase("-name")) {
				dataSetName = args[++i];
				ifsname = dataSetName;
			}
			if(args[i].equalsIgnoreCase("-trailing")) {
				/*
				 * Add a message string following JSON output
				 */
				trailingMessage = args[++i];
			}
			if(args[i].equalsIgnoreCase("-start")) {
				/*
				 * Add a message string preceding JSON output
				 */
				startMessage = args[++i];
			}
			else if(args[i].equalsIgnoreCase("-file")) {
				/*
				 * Specifies the name of a Apophysis flame file
				 * This is a XML file that specifies (among other things)
				 * the transform coeficients and name of the flame fractal
				 */
				filename = args[++i];
			}
			else if(args[i].equalsIgnoreCase("-ifs")) {
				/*
				 * specifies a built-in IteratedFunctionSystem name
				 */
				ifsname =  args[++i];
			}
			else if(args[i].equalsIgnoreCase("-flame")) {
				flameName = args[++i];
				ifsname = flameName;
			}
			else if(args[i].equalsIgnoreCase("-scale")) {
				scale = args[++i].split(",");
			}
		}
		
		if(filename != null) {
			try {
				ifs = new IteratedFunctionSystem(filename, flameName);
			}
			catch(Exception e) {
				log.error("Could not create IteratedFunctionSystem " + filename + " " + flameName);
				log.error("Exception: " + e.toString());
				return;
			}
		}
		else {
			if(ifsname.equalsIgnoreCase("Sierpinski2")) {
				ifs = IfsSystems.Sierpinski2();
			}
			else if(ifsname.equalsIgnoreCase("Sierpinski")) {
				ifs = IfsSystems.Sierpinski();
			}
			else if(ifsname.equalsIgnoreCase("flame1")) {
				ifs = IfsSystems.Flame1();
			}
			else if(ifsname.equalsIgnoreCase("Sierpinski3")) {
				ifs = IfsSystems.Sierpinski3();
			}
			else if(ifsname.equalsIgnoreCase("ifs2")) {
				ifs = IfsSystems.IFS2();
			}
			else if(ifsname.equalsIgnoreCase("ifs3")) {
				ifs = IfsSystems.IFS3();
			}
			else if(ifsname.equalsIgnoreCase("Sierpinski3Variations")) {
				ifs = IfsSystems.Sierpinski3Variations();
			}

		}
		ChaosGame game = new ChaosGame(ifs, niterations, nrepeats);
		if(scale != null) {
			int x = Integer.parseInt(scale[0]);
			int y = Integer.parseInt(scale[1]);
			scaleDimensions = new OrderedPair<>(x,y);
			game.setScaleDimensions(scaleDimensions);
		}
		/*
		 * Run the chaos game to generate points
		 */
		PointSet<Double> points = game.run();
		points.setName(dataSetName);
		points.setN(niterations);
		StringBuffer sb = new StringBuffer();
		List<LinearFunction> lf = ifs.getFunctions();

		for(int i=0; i<lf.size(); i++) {
			sb.append(lf.get(i).toString());
			if(i<lf.size()-1) {
				sb.append(",");
			}
		}
		points.setIteratedFunctionSystem(ifs);
		points.setName(dataSetName);
		CommandMessage cmstart = null;
		CommandMessage cmtrailing = null;
		
		if(startMessage != null) {
			cmstart = new CommandMessage(dataSetName, startMessage);
		}
		if(trailingMessage != null) {
			cmtrailing = new CommandMessage(dataSetName, trailingMessage);
		}
		if(startMessage != null) {
			System.out.println(cmstart.toJson());
		}
		System.out.println(ifs.toJson());
		System.out.println(points.getStats().toJson());
				
		for(Point2D<Double> point : points.getPoints()) {
			point.setName(ifs.getName());
			System.out.println(point.toJson());
		}
		if(trailingMessage != null) {
			System.out.println(cmtrailing.toJson());
		}

		log.trace("minX=" + points.getMinXValue() + " minY=" + points.getMinYValue());
		log.trace("maxX=" + points.getMaxXValue() + " maxY=" + points.getMaxYValue());
		log.trace("min Point: " + points.getMinPoint());
		log.trace("max Point: " + points.getMaxPoint());
	}

}
