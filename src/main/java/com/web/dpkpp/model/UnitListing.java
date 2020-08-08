package com.web.dpkpp.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "tb_unit_listing")
public class UnitListing extends BaseModel {

	@ManyToOne
	@JoinColumn(name = "unit_id" ,nullable = false,columnDefinition="TEXT")
	private Unit unit;
	@ManyToOne
	@JoinColumn(name = "sub_unit_id" ,nullable = false,columnDefinition="TEXT")
	private SubUnit subUnit;
	public Unit getUnit() {
		return unit;
	}
	public void setUnit(Unit unit) {
		this.unit = unit;
	}
	public SubUnit getSubUnit() {
		return subUnit;
	}
	public void setSubUnit(SubUnit subUnit) {
		this.subUnit = subUnit;
	}
	
}
