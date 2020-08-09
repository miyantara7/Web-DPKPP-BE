package com.web.dpkpp.dao;

import org.springframework.stereotype.Repository;

@Repository
public class LkhDao extends BaseDao implements BaseMasterDao {

	@Override
	public <T> void save(T entity) {
		em.persist(entity);	
	}

	@Override
	public <T> void edit(T entity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public <T> void delete(T entity) {
		// TODO Auto-generated method stub
		
	}

}
