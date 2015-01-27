package com.nzv.gwt.dsocatalog.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.ValueBoxBase;

public class ChangeLimitMagnitudeClickEventHandler implements ClickHandler {
	
	private DsoCatalogGWT application;
	private ValueBoxBase<String> target;
	private double variation;
	
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
	}
}
