package com.nzv.gwt.dsocatalog.client;

import org.realityforge.gwt.ga.UniversalGoogleAnalytics;
import org.realityforge.gwt.ga.impl.UniversalGoogleAnalyticsImpl;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;

public class GoogleAnalyticsEventFilterAware implements ValueChangeHandler<Boolean> {
	
	private String guiComponent;
	private UniversalGoogleAnalytics ga = new UniversalGoogleAnalyticsImpl();
	
	public GoogleAnalyticsEventFilterAware(String guiComponent) {
		super();
		this.guiComponent = guiComponent;
	}


	@Override
	public void onValueChange(ValueChangeEvent<Boolean> event) {
		if (event.getValue()) {
			ga.trackEvent("Filter", guiComponent, "ENABLE");
		} else {
			ga.trackEvent("Filter", guiComponent, "DISABLE");
		}
	}
}
