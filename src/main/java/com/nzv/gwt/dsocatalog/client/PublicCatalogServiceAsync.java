package com.nzv.gwt.dsocatalog.client;

import java.util.List;
import java.util.Set;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.nzv.gwt.dsocatalog.model.AstroObject;
import com.nzv.gwt.dsocatalog.model.Constellation;
import com.nzv.gwt.dsocatalog.model.DeepSkyObject;
import com.nzv.gwt.dsocatalog.model.Star;

public interface PublicCatalogServiceAsync {

	public void findObjectBrighterThan(CatalogSearchOptions options,
			AsyncCallback<Set<AstroObject>> callback);

	public void findConstellationByCode(String constellationCode,
			AsyncCallback<Constellation> callback);
	
	public void findConstellationByCodeFetchingBoundary(String constellationCode,
			AsyncCallback<Constellation> callback);

	public void findAllConstellations(AsyncCallback<List<Constellation>> callback);

	public void findStarByHrNumber(Integer hrNumber, AsyncCallback<Star> callback);

	public void findStarByName(String name, AsyncCallback<Star> callback);

	public void findStarByHdNumber(Integer hdNumber, AsyncCallback<Star> callback);

	public void findStarBySaoNumber(Integer saoNumber, AsyncCallback<Star> callback);

	public void findStarByFk5Number(Integer fk5Number, AsyncCallback<Star> callback);
	
	public void findObjectByName(String name, AsyncCallback<DeepSkyObject> callback);

}
