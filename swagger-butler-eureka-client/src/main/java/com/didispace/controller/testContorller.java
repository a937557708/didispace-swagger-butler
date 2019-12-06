package com.didispace.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.didispace.Entity.User;
import com.didispace.utils.ResponseUtils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(value = "/hello")
@Api(value = "这是一个测试接口", tags = "淡定")
public class testContorller {

	@RequestMapping(value = "/name", method = { RequestMethod.GET })
	@ApiOperation(value = "name接口", notes = "这是一个装逼神器")
	@ApiImplicitParam(name = "name", value = "姓名", required = false, dataType = "String", paramType = "query")
	public String hello(@RequestParam(required = false) String name) {
		return "扯蛋玩意" + name;
	}

	@ApiOperation(value = "这是一个王八蛋接口", notes = "冒险吕大爷")
	@ApiImplicitParams({ @ApiImplicitParam(name = "id", value = "用户ID", paramType = "path", dataType = "string"),
			@ApiImplicitParam(name = "userName", value = "用户名称", paramType = "form", dataType = "string") })
	@RequestMapping(value = "/map", method = { RequestMethod.POST })
	public Map<Object, Object> getMap(@RequestBody User user) {
		Map<Object, Object> map = new HashMap<Object, Object>();
		map.put("name", "隔壁老王");
		map.put("name1", "隔壁老王");
		return map;
	}

	// @RequestMapping("/test")
	// public String hello(Map<String, Object> map) {
	// map.put("name", "[Angel -- 守护天使]");
	// return "test";
	// }
	@RequestMapping("/index")
	public ModelAndView index(ModelAndView modelAndView) {
		modelAndView.setViewName("test");
		modelAndView.addObject("name", "守护天使");
		return modelAndView;
	}

	@RequestMapping(value = "/json", method = { RequestMethod.POST })
	public @ResponseBody ResponseUtils json(@RequestBody JSONObject paramJSON) {
		JSONObject obj = new JSONObject();
		obj.put("name", "童家荣");
		return ResponseUtils.ok(obj);
	}
}
