package com.web.dpkpp.dao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

abstract class BaseDao {
	
	@PersistenceContext
	protected EntityManager em;
	
}
