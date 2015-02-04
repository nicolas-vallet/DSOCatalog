package com.nzv.gwt.dsocatalog.visualization;

import com.google.gwt.ajaxloader.client.ArrayHelper;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArrayString;

/**
 * The explorer allows users to pan and zoom Google charts. 
 * Explorer.create() provides the default explorer behavior, 
 * enabling users to pan horizontally and vertically by dragging, 
 * and to zoom in and out by scrolling.
 * 
 * Note: The explorer only works with continuous axes (such as numbers or dates).
 * 
 * /!\ This feature is experimental and may change in future releases.
 * 
 */
public class Explorer extends JavaScriptObject {
	
	public static Explorer create() {
		return JavaScriptObject.createObject().cast();
	}
	
	/**
	 * By default, users can pan both horizontally and vertically when the explorer option is used. 
	 * If you want to users to only pan horizontally, use explorer: axis 'horizontal'.
	 * Similarly, axis 'vertical' enables vertical-only panning.
	 */
	public enum Axis {
		HORIZONTAL("horizontal"),
		VERTICAL("vertical");
		
		private String chartApiValue;
		
		private Axis(String v) {
			this.chartApiValue = v;
		}
		
		public String getChartApiValue() {
			return this.chartApiValue;
		}
	}
	
	/**
	 * The Explorer supports three actions:
	 * 
	 *  - dragToPan: Drag to pan around the chart horizontally and vertically. 
	 *  To pan only along the horizontal axis, use explorer: { axis: 'horizontal' }.
	 *  Similarly for the vertical axis.
	 *  
	 *  - dragToZoom: The explorer's default behavior is to zoom in and out when the user scrolls.
	 *  If dragToZoom action is used, dragging across a rectangular area zooms into that area.
	 *  
	 *  - rightClickToReset: Right clicking on the chart returns it to the original pan and zoom level.
	 */
	public enum Action {
		DRAG_TO_PAN("dragToPan"),
		DRAG_TO_ZOOM("dragToZoom"),
		RIGHT_CLICK_TO_RESET("rightClickToReset");
		
		private String chartApiValue;
		
		private Action(String v) {
			this.chartApiValue = v;
		}
		
		public String getChartApiValue() {
			return this.chartApiValue;
		}
	}
	
	protected Explorer() {}
	
	/**
	 * Allows to specify which actions are authorized. 
	 * By default, zoom in and out is enabled on scrolling.
	 */
	public final void setActions(Action[] actions) {
		String[] acts = new String[actions.length];
		for (int i=0 ; i<actions.length ; i++) {
			acts[i] = actions[i].getChartApiValue();
		}
		setActions(ArrayHelper.toJsArrayString(acts));
	}
	
	private final native void setActions(JsArrayString actions) /*-{
		this.actions = actions;
	}-*/;
	
	/**
	 * Allows to restrict pan direction for the user.
	 */
	public final void setAxis(Axis a) {
		setAxis(a.getChartApiValue());
	}
	
	private final native void setAxis(String a) /*-{
		this.axis = a;
	}-*/;
	
	/**
	 * By default, users can pan all around, regardless of where the data is.
	 * To ensure that users don't pan beyond the original chart, use explorer.setKeepInBounds(true).
	 */
	public final native void setKeepInBounds(boolean k) /*-{
		this.keepInBounds = k;
	}-*/;
	
	/**
	 * The maximum that the explorer can zoom in. 
	 * By default, users will be able to zoom in enough that they'll see only 25% of the original view.
	 * Setting a value of 0.5 would let users zoom in only far enough to see half of the original view.
	 */
	public final native void setMaxZoomIn(double max) /*-{
		this.maxZoomIn = max;
	}-*/;
	
	/**
	 * The maximum that the explorer can zoom out.
	 * By default, users will be able to zoom out far enough that the chart will take up only 1/4
	 * of the available space. 
	 * Setting a value of 8 would let users zoom out far enough that the chart would take up only 1/8
	 * of the available space.
	 */
	public final native void setMaxZoomOut(double max) /*-{
		this.maxZoomOut = max;
	}-*/;
	
	/**
	 * When users zoom in or out, zoomDelta determines how much they zoom by.
	 * The smaller the number, the smoother and slower the zoom.
	 */
	public final native void setZoomDelta(double delta) /*-{
		this.zoomDelta = delta;
	}-*/;
}
