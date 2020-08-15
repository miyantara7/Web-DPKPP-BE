package com.web.dpkpp.dao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

abstract class BaseDao {
	
	@PersistenceContext
	protected EntityManager em;
	
	public <T> void save(T entity) {
		em.persist(entity);
	}
	public <T> void edit(T entity) {
		em.merge(entity);
	}
	public <T> void delete(T entity) {
		em.remove(entity);
	}
}
