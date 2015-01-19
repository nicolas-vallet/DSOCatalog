package com.nzv.gwt.dsocatalog.visualization;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.visualization.client.DataTable;

public class MyDataTable extends DataTable {
	
	public static native MyDataTable create() /*-{
		return new $wnd.google.visualization.DataTable();
	}-*/;
	
	public static native MyDataTable create(JavaScriptObject jso, double version) /*-{
	    return new $wnd.google.visualization.DataTable(jso, version);
	}-*/;
	
	public static native MyDataTable create(JavaScriptObject jso) /*-{
		return new $wnd.google.visualization.DataTable(jso);
	}-*/;
	
	protected MyDataTable() {}
	
	public final native int addTooltipColumn(DataTable data) /*-{
		return data.addColumn({type:'string', role:'tooltip'});
	}-*/;
	
	public final native int addStyleColumn(DataTable data) /*-{
		return data.addColumn({type: 'string', role: 'style'});
	}-*/;

}
