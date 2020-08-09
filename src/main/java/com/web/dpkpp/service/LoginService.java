package com.web.dpkpp.service;

import java.io.File;
import java.util.ArrayList;
import java.util.Base64;

import javax.transaction.Transactional;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.web.dpkpp.config.JwtTokenUtil;
import com.web.dpkpp.dao.LoginDao;
import com.web.dpkpp.dao.PersonDao;
import com.web.dpkpp.dao.UserDao;
import com.web.dpkpp.model.Login;
import com.web.dpkpp.model.LoginResponse;
import com.web.dpkpp.model.RegisterUser;
import com.web.dpkpp.model.User;
import com.web.dpkpp.model.Users;

@Service
public class LoginService implements UserDetailsService {
	
	@Autowired
	private UserDao userDao;

	@Autowired
	private PasswordEncoder bcryptEncoder;
	
	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private LoginDao loginDao;

	@Autowired
	private PersonService personService;
	
	@Override
	public Users loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userDao.findByUsername(username);
		if (user == null) {
			throw new UsernameNotFoundException("User not found with username: " + username);
		}
		return new com.web.dpkpp.model.Users(user,user.getId(),user.getUsername(), user.getPassword(),new ArrayList<>(),user.getPerson());
	}
	
	public LoginResponse loginWeb(Login authenticationRequest) throws Exception{
		final Users userDetails = (Users) loadUserByUsername(authenticationRequest.getUsername());
		
		authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());

		final String token = jwtTokenUtil.generateToken(userDetails);
		
		return new LoginResponse(token,userDetails.getUser());
	}
	
	public UserDetails getUserByIdMobile(String username) throws Exception {
		User user = userDao.findByUsername(username);
		if (user == null) {
			throw new UsernameNotFoundException("User not found with username: " + username);
		}
		return new com.web.dpkpp.model.Users(user,user.getId(),user.getUsername(), user.getPassword(),new ArrayList<>(),user.getPerson());
	}

	private void authenticate(String username, String password) throws Exception {
		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
		} catch (DisabledException e) {
			throw new Exception("USER_DISABLED", e);
		} catch (BadCredentialsException e) {
			throw new Exception("INVALID_CREDENTIALS", e);
		}
	}
	
	public void save(RegisterUser user) throws Exception {
		User newUser = new User();
		newUser.setUsername(user.getUsername());
		newUser.setPassword(bcryptEncoder.encode(user.getPassword()));
		newUser.setPerson(user.getPerson());
		try {
			if(loginDao.getUserById(newUser.getUsername())==null) {
				personService.save(newUser.getPerson());
				loginDao.save(newUser);
			}else {
				throw new Exception("Username is exist !") ;
			}
		} catch (Exception e) {
			throw e;
		}	
	}
	
}