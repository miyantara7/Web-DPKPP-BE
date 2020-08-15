package com.web.dpkpp.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.web.dpkpp.model.Person;
import com.web.dpkpp.model.User;

@Repository
public class PersonDao extends BaseDao {

	@SuppressWarnings("unchecked")
	public Person getPersonByNip(String nip){
		List<Person> listUser = em.createQuery("FROM Person where nip = :nip")
				.setParameter("nip", nip)
				.getResultList();
		
		return !listUser.isEmpty() ? listUser.get(0) : null;
	}
	
	@SuppressWarnings("unchecked")
	public Person getPersonById(String id){
		List<Person> listUser = em.createQuery("FROM Person where id = :id")
				.setParameter("id", id)
				.getResultList();
		
		return !listUser.isEmpty() ? listUser.get(0) : null;
	}

}
