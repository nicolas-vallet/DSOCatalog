package com.nzv.gwt.dsocatalog.client;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.ListBox;
import com.nzv.astro.ephemeris.Sexagesimal;
import com.nzv.gwt.dsocatalog.model.AstroObject;

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
		StringBuilder url = new StringBuilder();
		// FOV
		int widthInArcmin= 60;
		if (ao.getBoundingBoxSizeInArcMinute() != null) {
			widthInArcmin = (int) Math.ceil(1.2 * ao.getBoundingBoxSizeInArcMinute());
		}
		if (choosedSurvey.startsWith("sloan")) {
			url.append("http://skyservice.pha.jhu.edu/DR12/ImgCutout/getjpeg.aspx?");
			url.append("&ra="+ra.getValueAsUnits());
			url.append("&dec="+dec.getValueAsUnits());
			url.append("&width=500"); // Width=500px
			url.append("&height=500"); // Height=500px
			url.append("&query="); // Unknown parameter
			url.append("&Label=on"); // Label
			double scale = ((double)(widthInArcmin) * 60) / 500;
			url.append("&scale="+scale);
			if (choosedSurvey.endsWith("_inverted")) {
				url.append("&opt=LI&InvertImage=on");
			} else {
				url.append("&opt=L");
			}
		} else {
			url.append("http://stdatu.stsci.edu/cgi-bin/dss_search?v=");
			url.append(choosedSurvey);
			url.append("&e=J2000&f=gif&c=none");
			// Coordinates
			url.append("&r="+ra.getUnit()+"d"+ra.getMinute()+"m"+Math.round(ra.getSecond())+"s");
			url.append("&d="+dec.getUnit()+"d"+dec.getMinute()+"m"+Math.round(dec.getSecond())+"s");
			widthInArcmin = Math.min(60, widthInArcmin);
			url.append("&w="+widthInArcmin+"&h="+widthInArcmin);
		}
		return url.toString();
	}
}
