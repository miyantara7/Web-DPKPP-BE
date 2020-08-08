package com.web.dpkpp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.web.dpkpp.service.LoginService;

@RestController
public class HelloWorldController {
	
	@Autowired
	private LoginService userDetailService;

	@RequestMapping({ "/hello" })
	public ResponseEntity<?> firstPage() {
		try {
			return new ResponseEntity<>("Berhasil ", HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}

	}

}