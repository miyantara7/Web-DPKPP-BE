package com.web.dpkpp.helper;

import org.springframework.security.core.context.SecurityContextHolder;

import com.web.dpkpp.model.Person;
import com.web.dpkpp.model.Users;

public class SessionHelper {
	
	public static String getUserId() {
		return ((Users) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUserId();
	}
	
	public static Person getPerson() {
		return ((Users) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser().getPerson();
	}
}
