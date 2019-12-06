package com.didispace.utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.didispace.Entity.User;

public class Debug {
	/**
	 * 格式化输出，打印信息到控制台
	 * 
	 * @param format
	 * @param args
	 * @author xxj 2017年4月26日
	 */
	public static void printFormat(String format, Object... args) {
		if (args == null) {
			System.out.println(format);
		}
		System.out.println(java.text.MessageFormat.format(format, args));
	}

	/**
	 * 格式化输出，打印信息到控制台
	 * 
	 * @param format
	 * @param args
	 * @author xxj 2017年4月26日
	 */
	public static void print(Object... msg) {
		if (msg == null) {
			return;
		}
		for (Object x : msg) {
			System.out.print(x);
			// System.out.print(' ');
		}
		System.out.println();
	}

	/**
	 * 格式化输出，打印信息到控制台
	 * 
	 * @param format
	 * @param args
	 * @author xxj 2017年4月26日
	 */
	public static void println(Object... msg) {
		if (msg == null) {
			return;
		}
		for (Object x : msg)
			System.out.println(x);
	}

	/**
	 * 打印当前线程的调用堆栈
	 * 
	 * @author xxj 2017年4月26日
	 */
	public static void printTrack() {
		StackTraceElement[] st = Thread.currentThread().getStackTrace();
		if (st == null) {
			System.out.println("无堆栈...");
			return;
		}
		StringBuffer sbf = new StringBuffer();
		sbf.append(StringExtend.format("调用堆栈[{0}]：", StringExtend.getString(new java.util.Date())));
		for (StackTraceElement e : st) {
			sbf.append(StringExtend.format(" {0}.{1}() {2} <- {3}", e.getClassName(), e.getMethodName(),
					e.getLineNumber(), StringExtend.getEnterMark()));
		}
		System.out.println(sbf.toString());
	}

	public static void main(String[] args) {
		// 字符串转换成json数据
		String str = "{'name':'zhang','age':20}";
		String str_json1 = JSON.toJSONString(str, true);
		String str_json2 = JSON.toJSONString(str, false);
		System.out.println("格式化数据" + str_json1);
		System.out.println("未格式化数据" + str_json2);

		JSONObject js1 = new JSONObject();
		js1.fluentPut("name11", "aa").fluentPut("age", 22);
		// 阿里 json转化简单的实体类
		String stu = "{\"name11\":\"lisi\",\"age\":22}";
		JSONObject j1 = JSONObject.parseObject(stu);
		User ss = JSONObject.toJavaObject(j1, User.class);
		System.out.println(ss);
		// User(id=null, userName=null, passWord=null, age=22, sex=null, name=null)

		// 输出jsonObject中的数据

		JSONObject object = JSON.parseObject(stu);
		System.out.println("获取json数据中的数据       " + object.get("name") + " " + object.get("age"));
		// 删除json中的数据
		String propertyName = "name";
		Set set = object.keySet();
		set.remove(propertyName);
		// object.remove(propertyName);
		System.out.println("删除数据之后的json格式  " + object.toString());
		// json转化list集合
		// String list = "[{'name':'zhang','age':20},{'name':'li','age':30}]";
		// 添加属性value值
		String addPropertyName = "sex";
		String addPropertyVlaue = "man";
		object.put(addPropertyName, addPropertyVlaue);
		System.out.println("输出新增后的json数据   " + object.toString());
		// 修改属性的值等同于覆盖值
		String updatePropertyName = "sex";
		String updatePropertyVlaue = "woman";
		Set set2 = object.keySet();
		if (set2.contains(updatePropertyName)) {
			// object.put(updatePropertyName, JSON.toJSONString(updatePropertyVlaue));
			object.put(updatePropertyName, updatePropertyVlaue);
		}
		System.out.println("输出修改属性值的json数据   " + object.toString());
		// 判断json是否存在属性
		System.out.println("是否存在属性值id  " + object.keySet().contains("id"));
		// 转换日期，这个还是比较重要
		Object date = new Date();
		String date_json = JSON.toJSONStringWithDateFormat(date, "yyyy-MM-dd HH:mm:ss.SSS");
		System.out.println("日期处理  " + date_json);
		// 解析Map集合对象
		Map<String, String> map = new HashMap<String, String>();
		map.put("name", "李四");
		map.put("age", "20");
		String map_json = JSON.toJSONString(map);
		System.out.println("map转换成json数据     " + map_json);

		Map<String, String> map1 = new HashMap<String, String>();
		map1.put("code", "11");
		map1.put("message", "ok");
		String json = JSON.toJSONString(map1);
		JSONObject jsonObject = JSON.parseObject(json);
		System.out.println("获取map集合中的数据   " + jsonObject.get("code") + "  " + jsonObject.get("message"));

		// 解析多个对象成list集合 使用JSONArray数组
		String array = "[{'name1':'zhang','age':20},{'name1':'wang','age':21}]";
		List<User> stu_list = new ArrayList<User>(JSONArray.parseArray(array, User.class));
		System.out.println("输出集合大小  " + stu_list.size());
		for (User u : stu_list) {
			System.out.println("User：" + u);
		}

	}

}
