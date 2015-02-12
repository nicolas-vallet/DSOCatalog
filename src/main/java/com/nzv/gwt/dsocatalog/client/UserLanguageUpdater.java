package com.nzv.gwt.dsocatalog.client;

import java.util.List;
import java.util.Map;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.http.client.UrlBuilder;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Hidden;
import com.google.gwt.user.client.ui.ListBox;
import com.nzv.gwt.dsocatalog.i18n.DsoCatalogMessages;
import com.nzv.gwt.ga.UniversalGoogleAnalytics;
import com.nzv.gwt.ga.impl.UniversalGoogleAnalyticsImpl;

public class UserLanguageUpdater implements ChangeHandler {
	
	private ListBox guiComponent;
	private Hidden currentLocaleHandler;
	private DsoCatalogMessages msg = GWT.create(DsoCatalogMessages.class);
	private UniversalGoogleAnalytics ga = new UniversalGoogleAnalyticsImpl();
	
	public UserLanguageUpdater(ListBox c, Hidden csh) {
		this.guiComponent = c;
		this.currentLocaleHandler = csh;
	}

	@Override
	public void onChange(ChangeEvent event) {
		Window.alert("Changement de langue vers "+guiComponent.getValue(guiComponent.getSelectedIndex()));
		if (Window.confirm(msg.tabObserverLanguageConfirmChange())) {
			String desiredLocaleCode = guiComponent.getValue(guiComponent.getSelectedIndex());
			currentLocaleHandler.setValue(desiredLocaleCode);
			UrlBuilder urlBuilder = Window.Location.createUrlBuilder();
			Map<String, List<String>> params = Window.Location.getParameterMap();
			if (params.keySet().contains("locale")) {
				urlBuilder.removeParameter("locale");
			}
			urlBuilder.setParameter("locale", new String[]{desiredLocaleCode});
			ga.trackEvent("Language", "UI language selection", desiredLocaleCode);
			Window.Location.replace(urlBuilder.buildString());
		} else {
			String currentLocale = currentLocaleHandler.getValue();
			for (int i=0 ; i<guiComponent.getItemCount() ; i++) {
				if (currentLocale.equals(guiComponent.getValue(i))) {
					guiComponent.setSelectedIndex(i);
				}
			}
		}
	}
}
