package com.nzv.gwt.dsocatalog.client;

public abstract class MyRunnable<T> implements Runnable {
	
	@SuppressWarnings("unused")
	private final T queryResult;
	
	public MyRunnable(T t) {
		queryResult = t;
	}

}
