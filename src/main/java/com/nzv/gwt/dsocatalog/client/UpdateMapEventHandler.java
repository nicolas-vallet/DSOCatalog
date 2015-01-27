package com.nzv.gwt.dsocatalog.client;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;


public class UpdateMapEventHandler implements ClickHandler, ValueChangeHandler<Boolean>, ChangeHandler, KeyUpHandler {
	
	private DsoCatalogGWT application;
	
	@SuppressWarnings("unused")
	private UpdateMapEventHandler() {}
	
	public UpdateMapEventHandler(DsoCatalogGWT app) {
		application = app;
	}
	
	@Override
	public void onClick(ClickEvent event) {
		application.updateMap();
	}

	@Override
	public void onValueChange(ValueChangeEvent<Boolean> event) {
		application.updateMap();
	}

	@Override
	public void onChange(ChangeEvent event) {
		application.updateMap();
	}

	@Override
	public void onKeyUp(KeyUpEvent event) {
		if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
			application.updateMap();
		}
	}
}
