package org.eqasim.sao_paulo.utilities.variables;

import org.eqasim.core.simulation.mode_choice.utilities.variables.BaseVariables;

public class UAMVariables implements BaseVariables {
	final public double travelTime_min;
	final public double accessegressTime_min;
	final public double cost;
	final public double euclideanDistance_km;

	public UAMVariables(double travelTime_min, double accessegressTime_min, double cost, double euclideanDistance_km) {
		this.travelTime_min = travelTime_min;
		this.accessegressTime_min = accessegressTime_min;
		this.cost = cost;
		this.euclideanDistance_km = euclideanDistance_km;
	}
}
