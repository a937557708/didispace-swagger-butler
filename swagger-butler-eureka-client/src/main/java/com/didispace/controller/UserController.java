package com.didispace.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.didispace.mysql.dao.IUserIDao;

import io.swagger.annotations.Api;

@RestController
@RequestMapping(value = "/user")
@Api(value = "用户信息", tags = "用户添加，修改，删除，查询")
public class UserController {

	@Autowired
	private IUserIDao iUserIDao;

	@RequestMapping(value = "/hello", method = RequestMethod.GET)
	public String SayHello() {
		return "Hello Spring Boot";

	}

}
