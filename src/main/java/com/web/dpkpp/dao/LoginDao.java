package com.web.dpkpp.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.web.dpkpp.model.User;

@Repository
public class LoginDao extends BaseDao {

	@SuppressWarnings("unchecked")
	public <T> Object getUserById(String username){
		List<User> listUser = em.createQuery("FROM User where username = :username")
				.setParameter("username", username)
				.getResultList();
		
		return !listUser.isEmpty() ? listUser.get(0) : null;
	}	

}
