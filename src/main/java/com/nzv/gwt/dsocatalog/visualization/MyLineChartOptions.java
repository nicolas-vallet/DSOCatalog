package com.nzv.gwt.dsocatalog.visualization;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.visualization.client.visualizations.corechart.Series;
import com.google.gwt.visualization.client.visualizations.corechart.TextStyle;

public class MyLineChartOptions extends
		com.google.gwt.visualization.client.visualizations.corechart.Options {

	protected MyLineChartOptions() {
	}
	
	public static MyLineChartOptions create() {
		return JavaScriptObject.createObject().cast();
	}

	public final native void setSeries(int index, Series seriesAtIndex) /*-{
		if (!this.series) {
	      this.series = {};
	    }
	    this.series[index] = seriesAtIndex;
	}-*/;
	
	public final native void setAnnotationTextStyle(TextStyle ts) /*-{
		if (!this.annotations) {
			this.annotations = {};
		}
		this.annotations.textStyle = ts;
	}-*/;

	public final native void setSeries(JsArray<Series> series) /*-{
		this.series = series;
	}-*/;
	
	public final void setSeriesType(Series.Type seriesType) {
		setSeriesType(seriesType.name().toLowerCase());
	}
	
	public final native void setSeriesType(String seriesType) /*-{
		this.seriesType = seriesType;
	}-*/;
	
	public final native void setExplorer(Explorer exp) /*-{
		this.explorer = exp;
	}-*/;
}
