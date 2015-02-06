package com.nzv.gwt.dsocatalog.client;

import org.realityforge.gwt.ga.UniversalGoogleAnalytics;
import org.realityforge.gwt.ga.impl.UniversalGoogleAnalyticsImpl;

import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;

public class DetailsPanelSelectionHandler implements SelectionHandler<Integer>{
	
	//private AstroObject ao;
	private String[] availableTabs;
	private UniversalGoogleAnalytics ga = new UniversalGoogleAnalyticsImpl();
	
	public DetailsPanelSelectionHandler(String[] availableTabs) {
		//this.ao = ao;
		this.availableTabs = availableTabs;
	}

	@Override
	public void onSelection(SelectionEvent<Integer> event) {
		String selectedTab = availableTabs[event.getSelectedItem()];
		ga.trackEvent("Object details", "Tab selection", selectedTab);
	}
}
