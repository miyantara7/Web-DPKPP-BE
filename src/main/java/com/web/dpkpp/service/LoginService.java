package com.web.dpkpp.service;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Base64;

import javax.transaction.Transactional;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.web.dpkpp.config.JwtTokenUtil;
import com.web.dpkpp.dao.LoginDao;
import com.web.dpkpp.dao.PersonDao;
import com.web.dpkpp.dao.UserDao;
import com.web.dpkpp.dao.UserDaoHibernate;
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
	private UserDaoHibernate userDaoHibernate;

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
	
	@Value("${upload.path}")
    private String path;
	
	@Override
	public Users loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userDao.findByUsername(username);
		if (user == null) {
			throw new UsernameNotFoundException("User not found with username: " + username);
		}
		return new com.web.dpkpp.model.Users(user,user.getId(),user.getUsername(), user.getPassword(),new ArrayList<>(),user.getPerson());
	}
	
	public void checkUserLogin(Login login) throws Exception{
		String username = login.getUsername();
		User user = userDaoHibernate.getUserByUsername(username);
		try {
			if (user.isActive() == true) {
				throw new Exception("User has been login !");
			} 
			user.setActive(true);
			userDaoHibernate.edit(user);
		} catch (Exception e) {
			throw e;
		}
	}
	
	public LoginResponse loginWeb(Login authenticationRequest) throws Exception{
		
		final Users userDetails = (Users) loadUserByUsername(authenticationRequest.getUsername());
		
		authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());

		final String token = jwtTokenUtil.generateToken(userDetails);
		
		return new LoginResponse(token,userDetails.getUser());
	}
	
	public Object loginMobile(Login authenticationRequest) throws Exception{
		
		final Users userDetails = (Users) loadUserByUsername(authenticationRequest.getUsername());
		
		checkUserLogin(authenticationRequest);
		
		authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());

		final String token = jwtTokenUtil.generateToken(userDetails);
		
		return new LoginResponse(token,userDetails.getUser());	
	}
	
	public Object logOutMobile(Login login) throws Exception{
		String username = login.getUsername();
		User user = userDaoHibernate.getUserByUsername(username);
		try {
			if (user == null) {
				throw new UsernameNotFoundException("User not found with username: " + username);
			}

			if (user.isActive() != true) {
				throw new Exception("Your not login !");
			}
			user.setActive(false);
			userDaoHibernate.edit(user);
			throw new Exception("Logout Successfull !");
		} catch (Exception e) {
			throw e;
		}
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
	
	public void save(RegisterUser user,MultipartFile file) throws Exception {
		User newUser = new User();
		newUser.setUsername(user.getUsername());
		newUser.setPassword(bcryptEncoder.encode(user.getPassword()));
		newUser.setPerson(user.getPerson());
		String paths = path+"/photo";
		File files = new File(paths);
		try {
			if(userDaoHibernate.getUserByUsername(newUser.getUsername())==null) {
				personService.save(newUser.getPerson());
				loginDao.save(newUser);		
				if (!Files.exists(Paths.get(path))) {
			        if (!files.mkdirs()) {
			            System.out.println("Failed to create directory!");
			        }
				}
				
		        if(!file.isEmpty()) {
			        String fileName = file.getOriginalFilename();
		            InputStream is = file.getInputStream();
		            Files.copy(is, Paths.get(path + newUser.getPerson().getId()+"_"+fileName),
		                    StandardCopyOption.REPLACE_EXISTING);
		        }
			}else {
				throw new Exception("Username is exist !") ;
			}
		} catch (Exception e) {
			throw e;
		}	
	}
	
}