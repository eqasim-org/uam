package org.eqasim.sao_paulo.utilities.parameters;

import org.eqasim.core.simulation.mode_choice.ParameterDefinition;

public class UAMModeParameters implements ParameterDefinition {
	public double alpha_u = 0.0;
	public double betaTravelTime_u_min = 0.0;
	public double betaAccessEgressTime_min = 0.0;
	public double betaWaitingTime_u_min = 0.0;
	public double vot_min;
	public double vot_ae_min;

	static public UAMModeParameters buildDefault() {
		UAMModeParameters parameters = new UAMModeParameters();

		parameters.alpha_u = 0.0;
		parameters.betaWaitingTime_u_min = -0.0;
		parameters.betaAccessEgressTime_min = -0.0;
		parameters.betaTravelTime_u_min = -0.0;
		parameters.vot_min = 0.0;
		parameters.vot_ae_min = 0.0;
		return parameters;
	}
}