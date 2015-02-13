package com.nzv.gwt.dsocatalog.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Formula;

@SuppressWarnings("serial")
@Entity
@Table(name = "deep_sky_object")
public class DeepSkyObject extends AstroObject implements Serializable {

	@Id
	@GeneratedValue
	@Column(name = "id")
	private Integer id;

	@Column(name = "name")
	private String name;

	@Column(name = "other_name")
	private String otherName;

	@Column(name = "object_type")
	@Enumerated(EnumType.STRING)
	private DsoType type;

	@ManyToOne
	@JoinColumn(name="constellation")
	private Constellation constellation;

	@Column(name = "RAh")
	private Integer rightAscensionHour;

	@Column(name = "RAm")
	private Double rightAscensionMinute;
	
	@Formula("(RAh * 15) + (RAm * 15 / 60)")
	private Double rightAscension;

	@Column(name = "DEd")
	private Integer declinaisonDegree;

	@Column(name = "DEm")
	private Double declinaisonMinute;
	
	@Formula("IF(DEd<0 , (DEd - (DEm / 60)), (DEd + (DEm / 60)))")
	private Double declinaison;

	@Column(name = "magnitude")
	private Double magnitude;
	
	@Column(name = "surface_brigthness")
	private Double surfaceBrightness;

	@Column(name = "u2k_chart")
	private Integer u2kChartNumber;

	@Column(name = "ti_chart")
	private Integer tiChartNumber;

	@Column(name = "size_max")
	private Double maxSize;

	@Column(name = "size_max_unit", columnDefinition="enum")
	@Enumerated(EnumType.STRING)
	private SizeUnit maxSizeUnit;

	@Column(name = "size_min")
	private Double minSize;

	@Column(name = "size_min_unit", columnDefinition="enum")
	@Enumerated(EnumType.STRING)
	private SizeUnit minSizeUnit;

	@Column(name = "position_angle")
	private Double positionAngle;

	@Column(name = "class")
	private String classtype;

	@Column(name = "stars_count")
	private Integer starsCount;

	@Column(name = "brightest_star_mag")
	private Double brightestStarMagnitude;

	@Column(name = "in_bestngc_catalog", columnDefinition = "tinyint(1)")
	private boolean inBestNgcCatalog;
	
	@Column(name = "in_caldwell_catalog", columnDefinition = "tinyint(1)")
	private boolean inCaldwellCalatalog;
	
	@Column(name = "in_herschel_catalog", columnDefinition = "tinyint(1)")
	private boolean inHerschelCatalog;
	
	@Column(name = "in_messier_catalog", columnDefinition = "tinyint(1)")
	private boolean inMessierCatalog;
	
	/**
	 * Returns a mask with initial of the catalogs in which this object is present.
	 * The catalogs come in this order : "(M) Messier, (N) Best NGC, (C) Caldwell, (H) Herschel".
	 * If the object is in the considered catalog, the returned string will contains it initial, otherwise, it will have an underscore.
	 * 
	 * For instance, an object which is present in the Best NGC and the Caldwell will return the mask "_NC_"
	 * An object which is present in the messier, the caldwell and the herschel will return "M_CH"
	 * An object which is present in the four catalogs will return "MNCH"
	 * An object which is only present in the Herschel will return "___H"
	 * An object which is present in none of theses catalogs will return "____"
	 */
	@Formula("CONCAT(IF(in_messier_catalog>0, 'M', '_'), IF(in_bestngc_catalog>0, 'N', '_'), IF(in_caldwell_catalog>0, 'C', '_'), IF(in_herschel_catalog>0, 'H', '_'))")
	private String inCatalogs;

	@Column(name = "ngc_desc")
	private String ngcDescription;

	@Column(name = "notes")
	private String notes;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getOtherName() {
		return otherName;
	}

	public void setOtherName(String otherName) {
		this.otherName = otherName;
	}

	public DsoType getType() {
		return type;
	}

	public void setType(DsoType type) {
		this.type = type;
	}

	public Constellation getConstellation() {
		return constellation;
	}

	public void setConstellation(Constellation constellation) {
		this.constellation = constellation;
	}

	public Integer getRightAscensionHour() {
		return rightAscensionHour;
	}

	public void setRightAscensionHour(Integer rightAscensionHour) {
		this.rightAscensionHour = rightAscensionHour;
	}

	public Double getRightAscensionMinute() {
		return rightAscensionMinute;
	}

	public void setRightAscensionMinute(Double rightAscensionMinute) {
		this.rightAscensionMinute = rightAscensionMinute;
	}

	public Integer getDeclinaisonDegree() {
		return declinaisonDegree;
	}

	public void setDeclinaisonDegree(Integer declinaisonDegree) {
		this.declinaisonDegree = declinaisonDegree;
	}

	public Double getDeclinaisonMinute() {
		return declinaisonMinute;
	}

	public void setDeclinaisonMinute(Double declinaisonMinute) {
		this.declinaisonMinute = declinaisonMinute;
	}

	public Double getMagnitude() {
		return magnitude;
	}

	public double getRightAscension() {
		return rightAscension;
	}

	public double getDeclinaison() {
		return declinaison;
	}

	public void setMagnitude(Double magnitude) {
		this.magnitude = magnitude;
	}

	public Double getSurfaceBrightness() {
		return surfaceBrightness;
	}

	public void setSurfaceBrightness(Double surfaceBrightness) {
		this.surfaceBrightness = surfaceBrightness;
	}

	public Integer getU2kChartNumber() {
		return u2kChartNumber;
	}

	public void setU2kChartNumber(Integer u2kChartNumber) {
		this.u2kChartNumber = u2kChartNumber;
	}

	public Integer getTiChartNumber() {
		return tiChartNumber;
	}

	public void setTiChartNumber(Integer tiChartNumber) {
		this.tiChartNumber = tiChartNumber;
	}

	public Double getMaxSize() {
		return maxSize;
	}

	public void setMaxSize(Double maxSize) {
		this.maxSize = maxSize;
	}
	
	@Override
	public Double getBoundingBoxSizeInArcMinute() {
		if (this.maxSize == null) {
			return null;
		} else {
			double widthInArcmin= 60;
			if (maxSize != null) {
				switch(maxSizeUnit) {
				case d:
					widthInArcmin = 60 *  maxSize;
					break;
				case s:
					widthInArcmin = maxSize / 60;
					break;
				case m:
				default:
					widthInArcmin = maxSize;
					break;
				}
			}
			return widthInArcmin;
		}
	}

	public SizeUnit getMaxSizeUnit() {
		return maxSizeUnit;
	}

	public void setMaxSizeUnit(SizeUnit maxSizeUnit) {
		this.maxSizeUnit = maxSizeUnit;
	}

	public Double getMinSize() {
		return minSize;
	}

	public void setMinSize(Double minSize) {
		this.minSize = minSize;
	}

	public SizeUnit getMinSizeUnit() {
		return minSizeUnit;
	}

	public void setMinSizeUnit(SizeUnit minSizeUnit) {
		this.minSizeUnit = minSizeUnit;
	}
	
	public String getSizeHumanReadable() {
		StringBuffer sb = new StringBuffer();
		if (minSize != null && maxSize != null) {
			sb.append(""+maxSize+maxSizeUnit);
			sb.append(" x ");
			sb.append(""+minSize+minSizeUnit);
		} else {
			if (maxSize != null) {
				sb.append(""+maxSize+maxSizeUnit);
			}
			if (minSize != null) {
				sb.append(""+minSize+minSizeUnit);
			}
		}
		return sb.toString();
	}

	public Double getPositionAngle() {
		return positionAngle;
	}

	public void setPositionAngle(Double positionAngle) {
		this.positionAngle = positionAngle;
	}

	public String getClasstype() {
		return classtype;
	}

	public void setClasstype(String classtype) {
		this.classtype = classtype;
	}

	public Integer getStarsCount() {
		return starsCount;
	}

	public void setStarsCount(Integer starsCount) {
		this.starsCount = starsCount;
	}

	public Double getBrightestStarMagnitude() {
		return brightestStarMagnitude;
	}

	public void setBrightestStarMagnitude(Double brightestStarMagnitude) {
		this.brightestStarMagnitude = brightestStarMagnitude;
	}

	public String getInCatalogs() {
		return inCatalogs;
	}

	public boolean isInBestNgcCatalog() {
		return inBestNgcCatalog;
	}

	public void setInBestNgcCatalog(boolean inBestNgcCatalog) {
		this.inBestNgcCatalog = inBestNgcCatalog;
	}

	public boolean isInCaldwellCalatalog() {
		return inCaldwellCalatalog;
	}

	public void setInCaldwellCalatalog(boolean inCaldwellCalatalog) {
		this.inCaldwellCalatalog = inCaldwellCalatalog;
	}

	public boolean isInHerschelCatalog() {
		return inHerschelCatalog;
	}

	public void setInHerschelCatalog(boolean inHerschelCatalog) {
		this.inHerschelCatalog = inHerschelCatalog;
	}

	public boolean isInMessierCatalog() {
		return inMessierCatalog;
	}

	public void setInMessierCatalog(boolean inMessierCatalog) {
		this.inMessierCatalog = inMessierCatalog;
	}

	public String getNgcDescription() {
		return ngcDescription;
	}

	public void setNgcDescription(String ngcDescription) {
		this.ngcDescription = ngcDescription;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}
	
	public boolean isAsterism() {
		return DsoType.ASTER == type;
	}
	public boolean isGalaxy() {
		return DsoType.GALXY == type || DsoType.GALCL == type;
	}
	public boolean isGlobularCluster() {
		return DsoType.GLOCL == type || DsoType.GX_GC == type
				|| DsoType.LMCGC == type || DsoType.SMCGC == type;
	}
	public boolean isOpenCluster() {
		return DsoType.CL_NB == type || DsoType.G_C_N == type
				|| DsoType.LMCCN == type || DsoType.LMCOC == type
				|| DsoType.OPNCL == type || DsoType.SMCCN == type
				|| DsoType.SMCOC == type;
	}
	public boolean isPlanetaryNebula() {
		return DsoType.PLNNB == type;
	}
	public boolean isNebula() {
		return DsoType.BRTNB == type  || DsoType.DRKNB == type 
				|| DsoType.GX_DN == type || DsoType.LMCDN == type 
				|| DsoType.SMCDN == type;
	}
	public boolean isSupernovaRemnant() {
		return DsoType.SNREM == type;
	}
	public boolean isQuasar() {
		return DsoType.QUASR == type;
	}

	@Override
	public String toString() {
		return "DeepSkyObject [name=" + name + "]";
	}

	@Override
	public Double getVisualMagnitude() {
		return getMagnitude();
	}

	@Override
	public String getIdentifier() {
		return getName();
	}

	@Override
	public String getObjectType() {
		return getType().getComment()+ " "+getClasstype();
	}
}
