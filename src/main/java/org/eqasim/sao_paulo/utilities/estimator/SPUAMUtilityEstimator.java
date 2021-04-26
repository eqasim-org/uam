package org.eqasim.sao_paulo.utilities.estimator;

import java.util.List;

import org.eqasim.core.simulation.mode_choice.utilities.UtilityEstimator;
import org.eqasim.core.simulation.mode_choice.utilities.estimators.EstimatorUtils;
import org.eqasim.sao_paulo.mode_choice.parameters.SaoPauloModeParameters;
import org.eqasim.sao_paulo.utilities.parameters.UAMModeParameters;
import org.eqasim.sao_paulo.utilities.predictor.UAMPredictor;
import org.eqasim.sao_paulo.utilities.variables.UAMVariables;
import org.matsim.api.core.v01.population.Person;
import org.matsim.api.core.v01.population.PlanElement;
import org.matsim.contribs.discrete_mode_choice.model.DiscreteModeChoiceTrip;

import com.google.inject.Inject;

public class SPUAMUtilityEstimator implements UtilityEstimator {
	private final UAMModeParameters uamParameters;
	private UAMPredictor uamPredictor;
	private final SaoPauloModeParameters modeParameters;

	@Inject
	public SPUAMUtilityEstimator(SaoPauloModeParameters modeParameters, UAMPredictor uamPredictor,
			UAMModeParameters uamParameters) {

		this.uamParameters = uamParameters;
		this.uamPredictor = uamPredictor;
		this.modeParameters = modeParameters;
	}

	private double estimateConstantUtility() {
		return uamParameters.alpha_u;
	}

	protected double estimateTravelTime(UAMVariables variables) {
		return uamParameters.betaTravelTime_u_min * variables.travelTime_min;
	}

	protected double estimateMonetaryCost(UAMVariables variables) {
		return modeParameters.betaCost_u_MU
				* EstimatorUtils.interaction(variables.euclideanDistance_km,
						modeParameters.referenceEuclideanDistance_km, modeParameters.lambdaCostEuclideanDistance)
				* variables.cost;
	}

	@Override
	public double estimateUtility(Person person, DiscreteModeChoiceTrip trip, List<? extends PlanElement> elements) {

		UAMVariables uamVariables = this.uamPredictor.predict(person, trip, elements);
		double utility = estimateConstantUtility();
		utility += estimateTravelTime(uamVariables);

		utility += estimateMonetaryCost(uamVariables);

		return utility;
	}

}
