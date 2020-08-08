package com.web.dpkpp.controller;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.web.dpkpp.model.Login;
import com.web.dpkpp.model.Person;
import com.web.dpkpp.service.PersonService;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/person")
public class PersonController {

	@Autowired
	private PersonService personService;
	
	@PutMapping("/edit")
	@Transactional
	public ResponseEntity<?> editPerson(MultipartFile file,String person) throws Exception {
		try {
			personService.edit(file,person);
			return new ResponseEntity<>("Success", HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	
}
