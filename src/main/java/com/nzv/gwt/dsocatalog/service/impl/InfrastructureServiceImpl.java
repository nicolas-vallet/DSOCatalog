package com.nzv.gwt.dsocatalog.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.nzv.gwt.dsocatalog.client.InfrastructureService;

@Service("infrastructureService")
public class InfrastructureServiceImpl implements InfrastructureService {

	@Value("${GOOGLE_ANALYTICS_WEB_PROPERTY_ID}")
	private String googleAnalyticsWebPropertyId;

	@Override
	public String getGoogleAnalyticsWebPropertyId() {
		return this.googleAnalyticsWebPropertyId;
	}
}
