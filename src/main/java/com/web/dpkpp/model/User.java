package com.web.dpkpp.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "tb_users",uniqueConstraints = {@UniqueConstraint(name = "u_username", columnNames = {"username"})})
public class User extends BaseModel {

	@Column
	private String username;
	
	@Column
	@JsonIgnore
	private String password;
	
	@OneToOne
	@JoinColumn(name = "person_id" ,nullable = false)
	private Person person;

	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}