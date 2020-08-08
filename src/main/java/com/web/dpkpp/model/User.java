package com.web.dpkpp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@Entity
@Table(name = "tb_users",uniqueConstraints = {@UniqueConstraint(name = "u_username", columnNames = {"username"})})
public class User extends BaseModel {

	@Column
	private String username;
	
	@Column
	@JsonIgnore
	private String password;

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