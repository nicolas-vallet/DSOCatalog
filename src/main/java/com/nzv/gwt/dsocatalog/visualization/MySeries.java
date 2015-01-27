package com.nzv.gwt.dsocatalog.visualization;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.visualization.client.visualizations.corechart.Series;
import com.google.gwt.visualization.client.visualizations.corechart.TextStyle;

public class MySeries extends Series {
	
	protected MySeries() {}
	
	public static MySeries create() {
	    return JavaScriptObject.createObject().cast();
	  } 
	
	public final native void setPointShape(Shape pointShape) /*-{
		this.pointShape = pointShape;
    }-*/;
	
	public final native void setVisibleInLegend(boolean v) /*-{
		this.visibleInLegend = v;
	}-*/;
	
	public final native void setLineDashStyle(int[] s) /*-{
		this.lineDashStyle = s;
	}-*/;
}
