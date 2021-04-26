package org.eqasim.sao_paulo;

import org.eqasim.core.simulation.mode_choice.AbstractEqasimExtension;

public class UAMEqasimModule extends AbstractEqasimExtension {

	static public final String UAM_MODE_AVAILABILITY_NAME = "UAMModeAvailability";

	@Override
	protected void installEqasimExtension() {
		UAMModeAvailability uamModeAvailability = new UAMModeAvailability();
		bindModeAvailability(UAM_MODE_AVAILABILITY_NAME).toInstance(uamModeAvailability);
	}

}
