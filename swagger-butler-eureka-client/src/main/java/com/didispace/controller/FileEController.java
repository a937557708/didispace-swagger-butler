package com.didispace.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.didispace.Entity.FileE;
import com.didispace.Entity.Topic;
import com.didispace.criteria.Criteria;
import com.didispace.criteria.Restrictions;
import com.didispace.mysql.dao.IFileDao;
import com.didispace.mysql.dao.ITopicDao;
import com.didispace.service.impl.TestServiceImpl;

@RestController
@RequestMapping(value = "/fileE")
public class FileEController {
	@Autowired
	private IFileDao iFileDao;
	@Autowired
	private ITopicDao iTopicDao;
	@Autowired
	private TestServiceImpl iTestServiceImpl;

	@RequestMapping(value = "/hello", method = RequestMethod.GET)
	public List<FileE> SayHello() {
		Criteria<FileE> criteria = new Criteria<>();
		criteria.add(Restrictions.and(Restrictions.isNull("title", true)));
		List<FileE> users = iFileDao.findAll(criteria);

		return users;

	}

	@RequestMapping(value = "/hello1", method = RequestMethod.GET)
	public List<Topic> SayHello1() {

		List<Topic> topics = iTopicDao.findAll();

		return topics;

	}

	@RequestMapping(value = "/hello2", method = RequestMethod.GET)
	public List SayHello2() {
		Specification<Topic> specification = iTestServiceImpl.listAdvanceSpec();
		List<Topic> topics = iTopicDao.findAll(specification);
		return topics;

	}
}
