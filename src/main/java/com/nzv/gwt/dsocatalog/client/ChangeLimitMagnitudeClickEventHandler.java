package com.nzv.gwt.dsocatalog.client;

import org.realityforge.gwt.ga.UniversalGoogleAnalytics;
import org.realityforge.gwt.ga.impl.UniversalGoogleAnalyticsImpl;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.ValueBoxBase;

public class ChangeLimitMagnitudeClickEventHandler implements ClickHandler {
	
	private DsoCatalogGWT application;
	private ValueBoxBase<String> target;
	private double variation;
	private UniversalGoogleAnalytics ga = new UniversalGoogleAnalyticsImpl();
	
	public ChangeLimitMagnitudeClickEventHandler(DsoCatalogGWT app, ValueBoxBase<String> target, double variation) {
		this.application = app;
		this.target = target;
		this.variation = variation;
	}

	@Override
	public void onClick(ClickEvent event) {
		double currentValue = Double.parseDouble(target.getText());
		double newValue = currentValue + variation;
		target.setText(""+newValue);
		application.updateMap();
		ga.trackEvent("Filter", target.getTitle(), ""+newValue);
	}
}
