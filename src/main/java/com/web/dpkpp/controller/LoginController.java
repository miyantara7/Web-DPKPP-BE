package com.web.dpkpp.controller;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.web.dpkpp.model.Login;
import com.web.dpkpp.model.RegisterUser;
import com.web.dpkpp.service.LoginService;

@RestController
@CrossOrigin("*")
@RequestMapping("/api")
public class LoginController {

	@Autowired
	private LoginService userDetailService;

	@PostMapping("/login")
	@Transactional
	public ResponseEntity<?> loginWeb(@RequestBody Login authenticationRequest) throws Exception {
		try {
			return ResponseEntity.ok(userDetailService.loginWeb(authenticationRequest));
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	
	@PostMapping("/login-mobile")
	@Transactional
	public ResponseEntity<?> loginMobile(@RequestBody Login login) throws Exception {
		try {
			return ResponseEntity.ok(userDetailService.loginMobile(login));
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	
	@PostMapping("/register")
	@Transactional
	public ResponseEntity<?> register(String user, MultipartFile file) throws Exception {
		try {
			userDetailService.save(user,file);
			return new ResponseEntity<>("Success", HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	
	}
	
	@PostMapping("/logout-mobile")
	@Transactional
	public ResponseEntity<?> logOutMobile(@RequestBody Login login) throws Exception {
		try {
			return ResponseEntity.ok(userDetailService.logOutMobile(login));
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}


}