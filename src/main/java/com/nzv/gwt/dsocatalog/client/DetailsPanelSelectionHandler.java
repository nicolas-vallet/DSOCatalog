package com.nzv.gwt.dsocatalog.client;


import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.nzv.gwt.ga.UniversalGoogleAnalytics;
import com.nzv.gwt.ga.impl.UniversalGoogleAnalyticsImpl;

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
