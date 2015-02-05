package com.nzv.gwt.dsocatalog.client;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.ListBox;
import com.nzv.astro.ephemeris.Sexagesimal;
import com.nzv.gwt.dsocatalog.model.AstroObject;
import com.nzv.gwt.dsocatalog.model.DeepSkyObject;
import com.nzv.gwt.dsocatalog.model.Star;

public class UpdateDssImageHandler implements ChangeHandler {
	
	private ListBox liSurveys;
	private Image img;
	private Image spinner;
	private AstroObject ao;
	
	public UpdateDssImageHandler(ListBox li, Image i, Image s, AstroObject o) {
		this.liSurveys = li;
		this.img = i;
		this.spinner = s;
		this.ao = o;
	}

	@Override
	public void onChange(ChangeEvent event) {
		this.spinner.setVisible(true);
		this.img.setVisible(false);
		this.img.setUrl(getDssImageUrl(liSurveys.getValue(liSurveys.getSelectedIndex()), ao));
	}
	
	public static String getDssImageUrl(String choosedSurvey, AstroObject ao) {
		Sexagesimal ra = new Sexagesimal(ao.getRightAscension());
		Sexagesimal dec = new Sexagesimal(ao.getDeclinaison());
		StringBuilder url = new StringBuilder("http://stdatu.stsci.edu/cgi-bin/dss_search?v=");
		url.append(choosedSurvey);
		url.append("&e=J2000&f=gif&c=none");
		// Coordinates
		url.append("&r="+ra.getUnit()+"d"+ra.getMinute()+"m"+Math.round(ra.getSecond())+"s");
		url.append("&d="+dec.getUnit()+"d"+dec.getMinute()+"m"+Math.round(dec.getSecond())+"s");
		// FOV
		if (ao instanceof Star) {
			// We always use a field of 30'
			url.append("&w=30&h=30");
		} else if (ao instanceof DeepSkyObject) {
			// We adapt the FOV to the object'size if it is known, 60' otherwise.
			DeepSkyObject dso = (DeepSkyObject) ao;
			int widthInArcmin= 60;
			if (dso.getMaxSize() != null) {
				switch(dso.getMaxSizeUnit()) {
				// 60 arcmin is the maximum authorized
				case d:
					widthInArcmin = 60;
					break;
				case s:
					widthInArcmin = Math.max((int) Math.ceil(1.2 * ((1.0 / 60) * dso.getMaxSize())), 5);
					break;
				case m:
				default:
					widthInArcmin = (int) Math.min(60, Math.round(1.20 * dso.getMaxSize()));
					break;
				}
			}
			url.append("&w="+widthInArcmin+"&h="+widthInArcmin);
		}
		return url.toString();
	}
}
