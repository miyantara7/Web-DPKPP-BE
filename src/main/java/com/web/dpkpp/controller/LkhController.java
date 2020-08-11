package com.web.dpkpp.controller;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.web.dpkpp.model.Lkh;
import com.web.dpkpp.service.LkhService;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/lkh")
public class LkhController {

	@Autowired
	private LkhService lkhService;
	
	@PostMapping
	@Transactional
	public ResponseEntity<?> add(MultipartFile file,String lkh) throws Exception {
		try {
			lkhService.add(file,lkh);
			return new ResponseEntity<>("Success", HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	
	@GetMapping("/get-list")
	public ResponseEntity<?> getListLkh() throws Exception {
		try {			
			return new ResponseEntity<>(lkhService.getListLkh(), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	
	@PutMapping("/edit")
	@Transactional
	public ResponseEntity<?> editPerson(MultipartFile file,String lkh) throws Exception {
		try {
			lkhService.edit(file,lkh);
			return new ResponseEntity<>("Success", HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
}
