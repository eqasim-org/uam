package org.eqasim.sao_paulo.utilities.costs;

import java.util.List;

import org.eqasim.core.simulation.mode_choice.cost.AbstractCostModel;
import org.eqasim.sao_paulo.utilities.parameters.UAMCostParameters;
import org.matsim.api.core.v01.population.Leg;
import org.matsim.api.core.v01.population.Person;
import org.matsim.api.core.v01.population.PlanElement;
import org.matsim.contribs.discrete_mode_choice.model.DiscreteModeChoiceTrip;

import com.google.inject.Inject;

public class UAMCostModel extends AbstractCostModel {
	private final UAMCostParameters costParameters;

	@Inject
	public UAMCostModel(UAMCostParameters costParameters) {
		super("uam");

		this.costParameters = costParameters;
	}

	@Override
	public double calculateCost_MU(Person person, DiscreteModeChoiceTrip trip, List<? extends PlanElement> elements) {
		double inVehicleTime = 0.0;
		for (PlanElement pe : elements) {
			if (pe instanceof Leg) {
				if (((Leg) pe).getMode().equals("uam"))
					inVehicleTime += ((Leg) pe).getTravelTime().seconds() / 60.0;
			}
		}

		return costParameters.uamCost_base_fare + costParameters.uamCost_km * getInVehicleDistance_km(elements)
				+ costParameters.uamCost_min * inVehicleTime;
	}
}