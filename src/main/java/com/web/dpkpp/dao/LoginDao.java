package com.web.dpkpp.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.web.dpkpp.model.Login;
import com.web.dpkpp.model.User;

@Repository
public class LoginDao extends BaseDao implements BaseMasterDao {

	@Override
	public <T> void save(T entity) {
		em.persist(entity);	
	}

	@Override
	public <T> void edit(T entity) {
		em.merge(entity);
	}

	@Override
	public <T> void delete(T entity) {
		// TODO Auto-generated method stub
		
	}

	

}
