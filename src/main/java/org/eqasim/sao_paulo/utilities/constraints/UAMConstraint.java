package org.eqasim.sao_paulo.utilities.constraints;

import java.util.Collection;
import java.util.List;

import org.matsim.api.core.v01.population.Person;
import org.matsim.contribs.discrete_mode_choice.model.DiscreteModeChoiceTrip;
import org.matsim.contribs.discrete_mode_choice.model.constraints.AbstractTripConstraint;
import org.matsim.contribs.discrete_mode_choice.model.trip_based.TripConstraint;
import org.matsim.contribs.discrete_mode_choice.model.trip_based.TripConstraintFactory;
import org.matsim.core.config.Config;

import com.google.inject.Inject;

import net.bhl.matsim.uam.config.UAMConfigGroup;
import net.bhl.matsim.uam.dispatcher.UAMManager;

public class UAMConstraint extends AbstractTripConstraint {
	public static final String UAM_MODE = "uam";
	private UAMManager uamData;
	private Config config;

	public UAMConstraint(UAMManager uamData, Config config) {
		this.uamData = uamData;
		this.config = config;
	}

	@Override
	public boolean validateBeforeEstimation(DiscreteModeChoiceTrip trip, String mode, List<String> previousModes) {
		if (mode.equals(UAM_MODE)) {
			double radius = ((UAMConfigGroup) config.getModules().get(UAMConfigGroup.GROUP_NAME)).getSearchRadius();
			if (this.uamData.getStations().getUAMStationsInRadius(trip.getOriginActivity().getCoord(), radius)
					.size() == 0) {
				return false;
			}

			if (this.uamData.getStations().getUAMStationsInRadius(trip.getDestinationActivity().getCoord(), radius)
					.size() == 0) {
				return false;
			}

		}

		return true;
	}

	static public class Factory implements TripConstraintFactory {
		public UAMManager uamData;
		public Config config;

		@Inject
		public Factory(UAMManager uamData, Config config) {
			this.uamData = uamData;
			this.config = config;
		}

		@Override
		public TripConstraint createConstraint(Person person, List<DiscreteModeChoiceTrip> planTrips,
				Collection<String> availableModes) {
			return new UAMConstraint(uamData, config);
		}
	}
}
