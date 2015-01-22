package com.nzv.gwt.dsocatalog.client;

import java.util.List;
import java.util.Set;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.nzv.gwt.dsocatalog.model.AstroObject;
import com.nzv.gwt.dsocatalog.model.Constellation;
import com.nzv.gwt.dsocatalog.model.DeepSkyObject;
import com.nzv.gwt.dsocatalog.model.Star;

@RemoteServiceRelativePath("springGwtServices/catalogService")
public interface PublicCatalogService extends RemoteService {

	public Set<AstroObject> findObjectBrighterThan(CatalogSearchOptions options);

	public Constellation findConstellationByCode(String constellationCode);
	
	public Constellation findConstellationByCodeFetchingBoundary(String constellationCode);

	public List<Constellation> findAllConstellations();

	public Star findStarByHrNumber(Integer hrNumber);

	public Star findStarByName(String name);

	public Star findStarByHdNumber(Integer hdNumber);

	public Star findStarBySaoNumber(Integer saoNumber);

	public Star findStarByFk5Number(Integer fk5Number);

	public DeepSkyObject findObjectByName(String name);
	
	public DeepSkyObject findObjectById(Integer id);

}
