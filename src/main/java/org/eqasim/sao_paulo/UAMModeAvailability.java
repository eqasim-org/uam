package org.eqasim.sao_paulo;

import java.util.Collection;
import java.util.List;

import org.eqasim.sao_paulo.mode_choice.SaoPauloModeAvailability;
import org.matsim.api.core.v01.TransportMode;
import org.matsim.api.core.v01.population.Person;
import org.matsim.contribs.discrete_mode_choice.model.DiscreteModeChoiceTrip;
import org.matsim.contribs.discrete_mode_choice.model.mode_availability.ModeAvailability;

public class UAMModeAvailability implements ModeAvailability {
	private final ModeAvailability delegateSP = new SaoPauloModeAvailability();

	@Override
	public Collection<String> getAvailableModes(Person person, List<DiscreteModeChoiceTrip> trips) {
		Collection<String> modes;
		modes = delegateSP.getAvailableModes(person, trips);

		if (modes.contains(TransportMode.walk)) {
			modes.add("uam");
		}

		return modes;
	}
}
