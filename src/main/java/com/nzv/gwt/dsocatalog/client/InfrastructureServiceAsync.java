package com.nzv.gwt.dsocatalog.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface InfrastructureServiceAsync {

	public void getGoogleAnalyticsWebPropertyId(AsyncCallback<String> callback);
	
}
