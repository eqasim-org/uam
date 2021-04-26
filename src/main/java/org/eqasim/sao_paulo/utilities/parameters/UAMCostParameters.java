package org.eqasim.sao_paulo.utilities.parameters;

import org.eqasim.core.simulation.mode_choice.ParameterDefinition;

public class UAMCostParameters implements ParameterDefinition {

	public double uamCost_km;

	public double uamCost_min;

	public double uamCost_base_fare;

	public static UAMCostParameters buildDefault() {
		UAMCostParameters parameters = new UAMCostParameters();

		parameters.uamCost_km = 0.0;
		parameters.uamCost_min = 0.0;
		parameters.uamCost_base_fare = 0.0;
		return parameters;
	}
}