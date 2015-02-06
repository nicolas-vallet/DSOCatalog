package com.nzv.gwt.dsocatalog.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("springGwtServices/infrastructureService")
public interface InfrastructureService extends RemoteService {
	
	public String getGoogleAnalyticsWebPropertyId();

}
