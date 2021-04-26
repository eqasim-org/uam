package org.eqasim.sao_paulo.run;

import java.util.Random;

import org.eqasim.core.components.config.EqasimConfigGroup;
import org.eqasim.core.simulation.EqasimConfigurator;
import org.eqasim.core.simulation.mode_choice.EqasimModeChoiceModule;
import org.eqasim.sao_paulo.UAMEqasimModule;
import org.eqasim.sao_paulo.UAMModeChoiceModule;
import org.eqasim.sao_paulo.mode_choice.SaoPauloModeChoiceModule;
import org.matsim.api.core.v01.Scenario;
import org.matsim.api.core.v01.population.Leg;
import org.matsim.api.core.v01.population.Person;
import org.matsim.api.core.v01.population.Plan;
import org.matsim.contrib.dvrp.run.DvrpConfigGroup;
import org.matsim.contrib.dvrp.run.DvrpModule;
import org.matsim.contribs.discrete_mode_choice.modules.config.DiscreteModeChoiceConfigGroup;
import org.matsim.core.config.CommandLine;
import org.matsim.core.config.CommandLine.ConfigurationException;
import org.matsim.core.config.Config;
import org.matsim.core.config.ConfigUtils;
import org.matsim.core.config.groups.PlanCalcScoreConfigGroup.ActivityParams;
import org.matsim.core.config.groups.PlanCalcScoreConfigGroup.ModeParams;
import org.matsim.core.config.groups.QSimConfigGroup.StarttimeInterpretation;
import org.matsim.core.controler.Controler;
import org.matsim.core.router.TripStructureUtils;
import org.matsim.core.router.TripStructureUtils.Trip;
import org.matsim.core.scenario.ScenarioUtils;

import net.bhl.matsim.uam.config.UAMConfigGroup;
import net.bhl.matsim.uam.qsim.UAMQSimModule;
import net.bhl.matsim.uam.qsim.UAMSpeedModule;
import net.bhl.matsim.uam.run.UAMModule;

public class RunSimulation {

	private static UAMConfigGroup uamConfigGroup;
	private static CommandLine cmd;
	private static String path;
	private static Config config;
	private static Controler controler;
	private static Scenario scenario;

	public static void main(String[] args) {
		parseArguments(args);
		setConfig(path);
		createScenario();
		createControler().run();
	}

	public static void parseArguments(String[] args) {
		try {
			cmd = new CommandLine.Builder(args).allowOptions("config-path").build();

			if (cmd.hasOption("config-path"))
				path = cmd.getOption("config-path").get();
			else
				path = args[0];
		} catch (ConfigurationException e) {
			e.printStackTrace();
		}
		uamConfigGroup = new UAMConfigGroup();
	}

	public static Config setConfig(String path) {

		config = ConfigUtils.loadConfig(path, EqasimConfigurator.getConfigGroups());
		config.addModule(uamConfigGroup);
		config.addModule(new DvrpConfigGroup());
		return config;

	}

	public static Scenario createScenario() {
		scenario = ScenarioUtils.createScenario(config);
		EqasimConfigurator.configureScenario(scenario);
		ScenarioUtils.loadScenario(scenario);
		EqasimConfigurator.adjustScenario(scenario);
		return scenario;
	}

	public static Scenario setScenario(Scenario scenario) {
		return RunSimulation.scenario = scenario;
	}

	public static Controler createControler() {
		try {
			cmd.applyConfiguration(config);
		} catch (ConfigurationException e) {
			e.printStackTrace();
		}
		EqasimConfigGroup eqasimConfig = (EqasimConfigGroup) config.getModules().get(EqasimConfigGroup.GROUP_NAME);
		eqasimConfig.setEstimator("uam", "UAMEstimator");
		eqasimConfig.setCostModel("uam", UAMModeChoiceModule.UAM_COST_MODEL_NAME);
		eqasimConfig.setEstimator("walk", "spWalkEstimator");
		eqasimConfig.setEstimator("pt", "spPTEstimator");
		eqasimConfig.setEstimator("car", "spCarEstimator");
		eqasimConfig.setEstimator("taxi", "spTaxiEstimator");

		DiscreteModeChoiceConfigGroup dmcConfig = (DiscreteModeChoiceConfigGroup) config.getModules()
				.get(DiscreteModeChoiceConfigGroup.GROUP_NAME);
		dmcConfig.setModeAvailability(UAMEqasimModule.UAM_MODE_AVAILABILITY_NAME);
		dmcConfig.getTripConstraints().add("UAMConstraint");
		controler = new Controler(scenario);

		EqasimConfigurator.configureController(controler);

		Random random = new Random(0);

		for (Person person : scenario.getPopulation().getPersons().values()) {
			for (Plan plan : person.getPlans()) {
				for (Trip trip : TripStructureUtils.getTrips(plan)) {
					if (trip.getTripElements().size() == 1) {
						Leg leg = (Leg) trip.getTripElements().get(0);
						if (leg.getMode().equals("car")) {
							if (random.nextDouble() < 0.5) {
								leg.setMode("walk");
								leg.setRoute(null);
							}
						}
					}
				}
			}
		}

		controler.addOverridingModule(new EqasimModeChoiceModule());
		controler.addOverridingModule(new SaoPauloModeChoiceModule(cmd));
		controler.addOverridingModule(new UAMModeChoiceModule());
		controler.addOverridingModule(new UAMEqasimModule());
		controler.addOverridingModule(new DvrpModule());

		controler.addOverridingModule(new UAMModule());
		controler.addOverridingModule(new UAMSpeedModule());

		controler.configureQSimComponents(configurator -> {
			UAMQSimModule.activateModes().configure(configurator);
		});

		controler.getConfig().qsim().setSimStarttimeInterpretation(StarttimeInterpretation.onlyUseStarttime);
		controler.getConfig().qsim().setStartTime(0.0);

		DvrpConfigGroup.get(config).setNetworkModesAsString("uam");

		config.planCalcScore().addModeParams(new ModeParams("access_uam_car"));
		config.planCalcScore().addModeParams(new ModeParams("egress_uam_car"));
		config.planCalcScore().addModeParams(new ModeParams("access_uam_walk"));
		config.planCalcScore().addModeParams(new ModeParams("egress_uam_walk"));
		config.planCalcScore().addModeParams(new ModeParams("uam"));
		config.planCalcScore()
				.addActivityParams(new ActivityParams("uam_interaction").setScoringThisActivityAtAll(false));

		config.controler().setWriteEventsInterval(1);

		return controler;
	}

}
