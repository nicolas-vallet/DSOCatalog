package com.nzv.gwt.dsocatalog.client;

import java.util.Date;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.CalendarUtil;

public class TimeExplorerClickHandler implements ClickHandler {
	
	private DsoCatalogGWT application;
	private ApplicationBoard ihm;
	
	public TimeExplorerClickHandler(DsoCatalogGWT engine, ApplicationBoard ihm) {
		this.application = engine;
		this.ihm = ihm;
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void onClick(ClickEvent event) {
		Widget source = (Widget) event.getSource();
		Date currentConfiguredDate = ApplicationBoard.dtfDateAndTime.parse(ihm.txtObserverDate.getText()+" "+ihm.txtObserverLocalTime.getText());
		if (ihm.btObserverSubstractOneWeek == source) {
			CalendarUtil.addDaysToDate(currentConfiguredDate, -7);
		} else if (ihm.btObserverSubstractOneDay == source) {
			CalendarUtil.addDaysToDate(currentConfiguredDate, -1);
		} else if (ihm.btObserverAddOneDay == source) {
			CalendarUtil.addDaysToDate(currentConfiguredDate, +1);
		} else if (ihm.btObserverAddOneWeek == source) {
			CalendarUtil.addDaysToDate(currentConfiguredDate, +7);
		} else if (ihm.btObserverSubstractOneHour == source) {
			currentConfiguredDate.setHours(currentConfiguredDate.getHours()-1);
		} else if (ihm.btObserverAddOneHour == source) {
			currentConfiguredDate.setHours(currentConfiguredDate.getHours()+1);
		}
		ihm.txtObserverDate.setText(ApplicationBoard.dtfDate.format(currentConfiguredDate));
		ihm.txtObserverLocalTime.setText(ApplicationBoard.dtfTime.format(currentConfiguredDate));
		application.updateMap();
	}
	
}
