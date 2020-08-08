package com.web.dpkpp.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.web.dpkpp.model.User;

@Repository
public interface UserDao extends CrudRepository<User, String> {
	
	User findByUsername(String username);
	
}