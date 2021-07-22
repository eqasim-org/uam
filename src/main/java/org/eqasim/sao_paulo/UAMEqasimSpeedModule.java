package org.eqasim.sao_paulo;

import org.eqasim.core.components.config.EqasimConfigGroup;
import org.eqasim.core.components.traffic.EqasimLinkSpeedCalculator;
import org.matsim.api.core.v01.Scenario;
import org.matsim.core.api.experimental.events.EventsManager;
import org.matsim.core.mobsim.qsim.AbstractQSimModule;
import org.matsim.core.mobsim.qsim.qnetsimengine.ConfigurableQNetworkFactory;
import org.matsim.core.mobsim.qsim.qnetsimengine.QNetworkFactory;
import org.matsim.core.mobsim.qsim.qnetsimengine.linkspeedcalculator.DefaultLinkSpeedCalculator;

import com.google.inject.Provides;
import com.google.inject.Singleton;

import net.bhl.matsim.uam.infrastructure.readers.UAMXMLReader;
import net.bhl.matsim.uam.qsim.UAMLinkSpeedCalculator;

public class UAMEqasimSpeedModule extends AbstractQSimModule {

	@Override
	public void configureQSim() {

	}

	@Provides
	@Singleton
	public QNetworkFactory provideQNetworkFactory(EventsManager events, Scenario scenario,
												  UAMLinkSpeedCalculator linkSpeedCalculator) {
		ConfigurableQNetworkFactory networkFactory = new ConfigurableQNetworkFactory(events, scenario);
		networkFactory.setLinkSpeedCalculator(linkSpeedCalculator);
		return networkFactory;
	}

	@Provides
	@Singleton
	public UAMLinkSpeedCalculator provideUAMLinkSpeedCalculator(UAMXMLReader uamReader,EqasimConfigGroup eqasimConfig) {
		DefaultLinkSpeedCalculator defaultDelegate = new DefaultLinkSpeedCalculator();
		EqasimLinkSpeedCalculator delegate = new EqasimLinkSpeedCalculator(defaultDelegate, eqasimConfig.getCrossingPenalty());
		return new UAMLinkSpeedCalculator(uamReader.getMapVehicleVerticalSpeeds(), uamReader.getMapVehicleHorizontalSpeeds(), delegate);
	}
}