package com.web.dpkpp.dao;

import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.web.dpkpp.model.Lkh;

@Repository
public class LkhDao extends BaseDao {
	
	public List<Object> getListLkh(String personId) {
		StringBuilder sb = new StringBuilder();
		return null;
//		Query q = em.createNativeQuery("select id from "
//				+ "tb_attendances ta where ta.date = ? and ta.materipengajar_id = ? and ta.user_id = ?");
//		q.setParameter(1, date).setParameter(2, materiPengajarId).setParameter(3, user);
//		List results = q.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public Lkh getById(String id) {
		List<Lkh> list = em.createQuery("From Lkh where id = :id")
				.setParameter("id", id)
				.getResultList();
		
		return !list.isEmpty() ? list.get(0) : null;
	}

}
