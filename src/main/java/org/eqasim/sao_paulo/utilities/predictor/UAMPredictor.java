package org.eqasim.sao_paulo.utilities.predictor;

import java.util.List;

import org.eqasim.core.simulation.mode_choice.cost.CostModel;
import org.eqasim.core.simulation.mode_choice.utilities.predictors.CachedVariablePredictor;
import org.eqasim.sao_paulo.utilities.variables.UAMVariables;
import org.matsim.api.core.v01.population.Leg;
import org.matsim.api.core.v01.population.Person;
import org.matsim.api.core.v01.population.PlanElement;
import org.matsim.contribs.discrete_mode_choice.model.DiscreteModeChoiceTrip;
import org.matsim.core.utils.geometry.CoordUtils;

import com.google.inject.Inject;
import com.google.inject.name.Named;

public class UAMPredictor extends CachedVariablePredictor<UAMVariables> {
	private CostModel costModel;

	@Inject
	public UAMPredictor(@Named("uam") CostModel costModel) {
		this.costModel = costModel;
	}

	@Override
	public UAMVariables predict(Person person, DiscreteModeChoiceTrip trip, List<? extends PlanElement> elements) {
		double travelTime_min = 0.0;
		double accessegressTime_min = 0.0;
		for (PlanElement pe : elements) {
			if (pe instanceof Leg) {
				if (((Leg) pe).getMode().equals("uam")) {
					travelTime_min += ((Leg) pe).getTravelTime().seconds() / 60.0;
				} else {
					accessegressTime_min += ((Leg) pe).getTravelTime().seconds() / 60.0;
				}
			}
		}

		double cost = costModel.calculateCost_MU(person, trip, elements);
		double euclidean_distance = CoordUtils.calcEuclideanDistance(trip.getOriginActivity().getCoord(),
				trip.getDestinationActivity().getCoord());

		return new UAMVariables(travelTime_min, accessegressTime_min, cost, euclidean_distance);
	}
}
