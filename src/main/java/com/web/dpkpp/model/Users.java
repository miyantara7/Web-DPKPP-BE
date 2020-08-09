package com.web.dpkpp.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class Users  implements UserDetails {

	private static final long serialVersionUID = 1L;
	private User user;
	private Person person;
	private String username;
	private String password;
	private String userId;
	private String token;
	private Collection<? extends GrantedAuthority> authorities;
	
	 public User getUser() {
			return user;
		}
	 
	 public Person getPerson() {
		 return person;
	 }
	 
	 
	public Users(User user,String token,Person person) {
		this.user = user;
		this.token = token;
		this.person = person;
	}

	public Users(User user, String userId, String username,String password,
              Collection<? extends GrantedAuthority> authorities,Person person) {
        this.user = user;
        this.username = username;
        this.password = password;
        this.userId = userId;
        this.authorities = authorities;
        this.person = person;
    }
	
    public static Users build(User user, String userId,String username,String password,Person person) {
    	List<GrantedAuthority> authorities = new ArrayList<>();
        return new Users(
        		user,
        		userId,
        		username,
        		password,	
                authorities,
                person
        );
    }


	public void setUser(User user) {
		this.user = user;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}


	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public void setAuthorities(Collection<? extends GrantedAuthority> authorities) {
		this.authorities = authorities;
	}

	@Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
    @Override
    public boolean isEnabled() {
        return true;
    }

	@Override
    public String getUsername() {
        return username;
    }
    @Override
    public String getPassword() {
        return password;
    }
    
    

}
