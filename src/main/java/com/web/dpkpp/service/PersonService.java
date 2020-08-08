package com.web.dpkpp.service;

import java.io.File;

import javax.transaction.Transactional;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.web.dpkpp.dao.PersonDao;
import com.web.dpkpp.model.Person;
import com.web.dpkpp.model.User;
import java.util.Base64;

@Service
@Transactional
public class PersonService extends BaseService  {

	@Autowired
	private PersonDao personDao;
	
	public void save(Person person) throws Exception {
		try {
			if(personDao.getPersonByNip(person.getNip())!=null) {	
				throw new Exception("Person is exist !") ;
			}else {
				personDao.save(person);
			}
		} catch (Exception e) {
			throw e;	
		}	
	}
	
	public void edit(MultipartFile file,String persons) throws Exception{
		Person person = new Person();
//		byte[] fileContent = FileUtils.readFileToByteArray(new File(file));
//		String encodedString = Base64.getEncoder().encodeToString(fileContent);
		person = super.readValue(persons, Person.class);
		byte[] bytes = Base64.getEncoder().encode(file.getBytes());
		String photo = Base64.getEncoder().encodeToString(bytes);
		person.setPhoto(photo);
		person.setTypeFile(file.getContentType());
		String[] parts = file.getOriginalFilename().split(".");
		person.setFileName(file.getOriginalFilename());
		try {
			Person tempPerson = personDao.getPersonById(person.getId());
			if(tempPerson!=null) {
				tempPerson.setName(person.getName());
				tempPerson.setPhoto(person.getPhoto());
				tempPerson.setPosition(person.getPosition());
				tempPerson.setTypeFile(person.getTypeFile());
				tempPerson.setGender(person.getGender());
				tempPerson.setFileName(person.getFileName());
				personDao.edit(tempPerson);
			}
		} catch (Exception e) {
			throw e;
		}
	}
}
