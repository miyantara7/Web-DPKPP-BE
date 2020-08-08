package com.web.dpkpp.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "tb_unit")
public class Unit extends BaseModel {
	
	@Column(columnDefinition="TEXT")
	private String code;
	@Column(columnDefinition="TEXT")
	private String name;
	@OneToOne
	@JoinColumn(name = "unit_id" ,nullable = true,columnDefinition="TEXT")
	private Unit unit;
	
	public Unit getUnit() {
		return unit;
	}
	public void setUnit(Unit unit) {
		this.unit = unit;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
}
