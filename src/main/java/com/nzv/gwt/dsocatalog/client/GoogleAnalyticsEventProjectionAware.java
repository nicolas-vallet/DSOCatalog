package com.nzv.gwt.dsocatalog.client;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.ui.ListBox;
import com.nzv.gwt.dsocatalog.model.CoordinatesSystem;
import com.nzv.gwt.ga.UniversalGoogleAnalytics;
import com.nzv.gwt.ga.impl.UniversalGoogleAnalyticsImpl;

public class GoogleAnalyticsEventProjectionAware implements ChangeHandler{
	
	private UniversalGoogleAnalytics ga = new UniversalGoogleAnalyticsImpl();
	private ListBox guiComponent;
	
	public GoogleAnalyticsEventProjectionAware(ListBox guiComponent) {
		super();
		this.guiComponent = guiComponent;
	}



	@Override
	public void onChange(ChangeEvent event) {
		CoordinatesSystem cs = CoordinatesSystem.valueOf(guiComponent.getValue(guiComponent.getSelectedIndex()));
		ga.trackEvent("Projection", "Select type", ""+cs);
	}
}
