package com.web.dpkpp.dao;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.web.dpkpp.model.User;

@Repository
public interface UserDao extends CrudRepository<User, String> {
	
	User findByUsername(String username);
}