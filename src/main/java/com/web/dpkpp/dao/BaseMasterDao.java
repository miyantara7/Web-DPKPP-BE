package com.web.dpkpp.dao;

import com.web.dpkpp.model.User;

public interface BaseMasterDao {

	abstract <T> void save(T entity);
	abstract <T> void edit(T entity);
	abstract <T> void delete(T entity);
}
