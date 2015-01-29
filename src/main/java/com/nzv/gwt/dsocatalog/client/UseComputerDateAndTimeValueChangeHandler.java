package com.nzv.gwt.dsocatalog.client;

import java.util.Date;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;

public class UseComputerDateAndTimeValueChangeHandler implements ValueChangeHandler<Boolean> {

	private DsoCatalogGWT application;
	private ApplicationBoard appPanel;

	public UseComputerDateAndTimeValueChangeHandler(DsoCatalogGWT app, ApplicationBoard panel) {
		this.application = app;
		this.appPanel = panel;
	}

	@Override
	public void onValueChange(ValueChangeEvent<Boolean> event) {
		if (event.getValue() == true) {
			setObserverTimeFromComputerTime(appPanel);
		}
	}
	
	private void setObserverTimeFromComputerTime(ApplicationBoard board) {
		Date now = new Date();
		board.txtObserverDate.setText(ApplicationBoard.dtfDate.format(now));
		board.txtObserverLocalTime.setText(ApplicationBoard.dtfTime.format(now));
		application.updateMap();
	}
}
