package com.web.dpkpp.service;

import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.web.dpkpp.dao.LkhDao;
import com.web.dpkpp.helper.SessionHelper;
import com.web.dpkpp.model.Lkh;

@Service
public class LkhService extends BaseService  {

	@Autowired
	private LkhDao lkhDao;
	
	public void add(MultipartFile file,String lkhs) throws Exception{
		Lkh lkh = new Lkh();
		lkh = super.readValue(lkhs, Lkh.class);
		byte[] bytes = Base64.getEncoder().encode(file.getBytes());
		String files = Base64.getEncoder().encodeToString(bytes);
		lkh.setFile(files);
		lkh.setTypeFile(file.getContentType());
		lkh.setFileName(file.getOriginalFilename());
		lkh.setStatus(false);
		lkh.setPerson(SessionHelper.getPerson());
		lkhDao.save(lkh);
	}
}
