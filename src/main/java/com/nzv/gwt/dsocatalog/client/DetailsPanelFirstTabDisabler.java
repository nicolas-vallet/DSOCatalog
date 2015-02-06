package com.nzv.gwt.dsocatalog.client;

import com.google.gwt.event.logical.shared.BeforeSelectionEvent;
import com.google.gwt.event.logical.shared.BeforeSelectionHandler;

public class DetailsPanelFirstTabDisabler implements BeforeSelectionHandler<Integer>{

	@Override
	public void onBeforeSelection(BeforeSelectionEvent<Integer> event) {
		if (event.getItem() == 0) {
			event.cancel();
		}
	}
	
}
