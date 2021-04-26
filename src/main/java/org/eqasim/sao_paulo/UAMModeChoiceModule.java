package org.eqasim.sao_paulo;

import org.eqasim.core.simulation.mode_choice.AbstractEqasimExtension;
import org.eqasim.core.simulation.mode_choice.cost.CostModel;
import org.eqasim.sao_paulo.utilities.constraints.UAMConstraint;
import org.eqasim.sao_paulo.utilities.costs.UAMCostModel;
import org.eqasim.sao_paulo.utilities.estimator.SPUAMUtilityEstimator;
import org.eqasim.sao_paulo.utilities.parameters.UAMCostParameters;
import org.eqasim.sao_paulo.utilities.parameters.UAMModeParameters;
import org.eqasim.sao_paulo.utilities.predictor.UAMPredictor;

import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.name.Named;

public class UAMModeChoiceModule extends AbstractEqasimExtension {

	static public final String UAM_ESTIMATOR_NAME = "UAMEstimator";
	static public final String UAM_COST_MODEL_NAME = "UAMCostModel";

	@Override
	protected void installEqasimExtension() {
		bindUtilityEstimator(UAM_ESTIMATOR_NAME).to(SPUAMUtilityEstimator.class);

		bindCostModel(UAM_COST_MODEL_NAME).to(UAMCostModel.class);
		bind(UAMPredictor.class);
		bind(UAMCostParameters.class).asEagerSingleton();
		bindTripConstraintFactory("UAMConstraint").to(UAMConstraint.Factory.class);

	}

	@Provides
	@Named("uam")
	public CostModel provideUAMTCostModel(UAMCostParameters costParameters) {
		return new UAMCostModel(costParameters);
	}

	@Provides
	@Singleton
	public UAMModeParameters provideUAMModeParameters() {
		UAMModeParameters parameters = UAMModeParameters.buildDefault();

		return parameters;
	}

}
