package com.web.dpkpp.service;

import java.util.ArrayList;

import javax.transaction.Transactional;

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

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userDao.findByUsername(username);
		if (user == null) {
			throw new UsernameNotFoundException("User not found with username: " + username);
		}
		return new com.web.dpkpp.model.Users(user,user.getId(),user.getUsername(), user.getPassword(),new ArrayList<>());
	}
	
	public LoginResponse loginWeb(Login authenticationRequest) throws Exception{
		authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());

		final Users userDetails = (Users) loadUserByUsername(authenticationRequest.getUsername());
		
		final String token = jwtTokenUtil.generateToken(userDetails);
		
		return new LoginResponse(token,userDetails.getUser());
	}
	
	public UserDetails getUserByIdMobile(String username) throws Exception {
		User user = userDao.findByUsername(username);
		if (user == null) {
			throw new UsernameNotFoundException("User not found with username: " + username);
		}
		return new com.web.dpkpp.model.Users(user,user.getId(),user.getUsername(), user.getPassword(),new ArrayList<>());
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
	
	public User save(RegisterUser user) throws Exception {
		User newUser = new User();
		newUser.setUsername(user.getUsername());
		newUser.setPassword(bcryptEncoder.encode(user.getPassword()));
		try {
			if(loginDao.getUserById(newUser.getUsername())==null) {
				loginDao.save(newUser);
				throw new Exception("Register Success !") ;
			}else {
				throw new Exception("Username is exist !") ;
			}	
		} catch (Exception e) {
			throw e;
		}	
	}
	
}