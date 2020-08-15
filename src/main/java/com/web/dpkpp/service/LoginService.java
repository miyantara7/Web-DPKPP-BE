package com.web.dpkpp.service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Base64;

import javax.transaction.Transactional;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.SetFactoryBean;
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
import com.web.dpkpp.model.Person;
import com.web.dpkpp.model.RegisterUser;
import com.web.dpkpp.model.User;
import com.web.dpkpp.model.Users;

@Service
public class LoginService extends BaseService implements UserDetailsService {
	
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
	
	@Value("${path.photo}")
    private String path;
	
	@Value("${photo.not.found}")
    private String photoNotFound;
	
	@Override
	public Users loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userDao.findByUsername(username);
		if (user == null) {
			throw new UsernameNotFoundException("User not found with username: " + username);
		}
		String filePath = user.getPerson().getId() + "_" + user.getPerson().getFileName();
		String photo;
		File file;	
			file = new File(path + filePath);
			if (file.exists()) {
				try {
					photo = Base64.getEncoder()
							.encodeToString(FileUtils.readFileToByteArray(file));
					Person person = user.getPerson();
					person.setPhoto(photo);
					person.setFileName(user.getPerson().getFileName());
					person.setTypeFile(user.getPerson().getTypeFile());
					user.setPerson(person);
				} catch (IOException e) {
					e.printStackTrace();
					System.out.println("masuk e1" );
				}
			}else {
				try {
					file = new File(path + photoNotFound);
					photo = Base64.getEncoder().encodeToString(FileUtils.readFileToByteArray(file));
					Person person = user.getPerson();
					person.setPhoto(photo);
					person.setFileName(file.getName());
					person.setTypeFile(FilenameUtils.getExtension(file.toString()));
					user.setPerson(person);
				} catch (IOException e) {
					e.printStackTrace();
					System.out.println("masuk e");
				}
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
		
		System.out.println(userDetails.getUser().getPerson().getFileName());
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
	
	public void save(String user,MultipartFile file) throws Exception {
		User newUser = new User();
		   
		RegisterUser regisUser = super.readValue(user, RegisterUser.class);
		newUser.setUsername(regisUser.getUsername());
		newUser.setPassword(bcryptEncoder.encode(regisUser.getPassword()));
		newUser.setPerson(regisUser.getPerson());
		File files = new File(path);
		try {
			if(userDaoHibernate.getUserByUsername(newUser.getUsername())==null) {
				
				String fileName = file.getOriginalFilename();
	            InputStream is = file.getInputStream();
	        	Person person = newUser.getPerson();
	        	person.setTypeFile(file.getContentType());
	        	person.setFileName(fileName);
	        	newUser.setPerson(person);
	        	
				personService.save(newUser.getPerson());
				loginDao.save(newUser);	
				if (!Files.exists(Paths.get(path))) {
			        if (!files.mkdirs()) {
			            System.out.println("Failed to create directory!");
			        }
				}
				
		        if(!file.isEmpty()) {
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