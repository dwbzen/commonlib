package mathlib.fractal;

import java.util.ArrayList;
import java.util.Collection;

import mathlib.complex.Complex;
import mathlib.util.BroadcastEvent;
import mathlib.util.IBroadcastClient;
import mathlib.util.IBroadcastServer;

public class GenericIterationListener implements IterationListener, IBroadcastServer {

	protected int iterations = 0;
	protected Complex zMax = new Complex();	// (0,0)
	protected Complex zMin = new Complex(Double.MAX_VALUE, Double.MAX_VALUE);
	
	protected double maxDistance = 0.0;
	protected double minDistance = Double.MAX_VALUE;
	
	private Collection<IBroadcastClient> clients = new ArrayList<IBroadcastClient>();
	private int nclients = 0;
	private BroadcastEvent event = new BroadcastEvent(BroadcastEvent.EVENT_TYPE.Iteration);
	private BroadcastEvent completeEvent = new BroadcastEvent(BroadcastEvent.EVENT_TYPE.IterationComplete);
	
	public int afterIterations(Object obj) {
		completeEvent.setNumberPayload( iterations);
		completeEvent.setObjectPayload(obj);
		broadcast(completeEvent);
		return iterations;
	}

	public Complex atEachIteration(Complex z) {
		Complex z0 = atIteration(z);
		double dist = z0.cabs();
		if(dist < minDistance) {
			minDistance = dist;
			zMin.assign(z);
		}
		if(dist > maxDistance) {
			maxDistance = dist;
			zMax.assign(z);
		}
		if(nclients > 0) {
			event.setNumberPayload(z0);
			broadcast(event);
		}
		return z0;
	}

	public Complex atIteration(Complex z) {
		iterations++;
		return z;
	}

	public int getIterations() {
		return iterations;
	}

	public void setIterations(int iterations) {
		this.iterations = iterations;
	}
	
	public Complex getZMax() {
		return zMax;
	}

	public void setZMax(Complex max) {
		zMax = max;
	}

	public Complex getZMin() {
		return zMin;
	}

	public void setZMin(Complex min) {
		zMin = min;
	}

	
	public Collection<IBroadcastClient> getClients() {
		return clients;
	}

	public void setClients(Collection<IBroadcastClient> clients) {
		this.clients = clients;
		nclients = clients.size();
	}

	public void addClient(IBroadcastClient client) {
		clients.add(client);
		nclients++;
	}
	
	public void broadcast(BroadcastEvent event) {
		for(IBroadcastClient client : clients) {
			client.receive(event);
		}
	}

}
