package com.didispace.controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONException;
import com.didispace.ueditor.ActionEnter;

import io.swagger.annotations.Api;

@RestController
@RequestMapping(value = "/ueditor")
@Api(value = "Ueditor", tags = "Ueditor编辑器")
public class UeditorController {

	@RequestMapping(value = "/upload", method = { RequestMethod.GET, RequestMethod.POST })
	public void config(HttpServletRequest request, HttpServletResponse response) {
		response.setHeader("Content-Type", "text/html");
		String rootPath = request.getSession().getServletContext().getRealPath("/");
		try {
			String exec = new ActionEnter(request, rootPath).exec();
			PrintWriter writer = response.getWriter();
			writer.write(exec);
			writer.flush();
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
