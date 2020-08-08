package com.web.dpkpp.helper;

import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import com.web.dpkpp.model.Person;
import com.web.dpkpp.model.User;
import com.web.dpkpp.model.Users;

public class SessionHelper {

	SecurityContext securityContext = SecurityContextHolder.getContext();
	
	public String getUserId() {
		return ((Users) securityContext.getAuthentication().getPrincipal()).getUserId();
	}
	
	public Person getPerson() {
		User users = ((Users) securityContext.getAuthentication().getPrincipal()).getUser();
		return users.getPerson();
	}
}
