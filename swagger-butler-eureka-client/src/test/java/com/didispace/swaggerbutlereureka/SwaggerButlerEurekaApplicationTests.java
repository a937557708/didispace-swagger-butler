package com.didispace.swaggerbutlereureka;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import com.alibaba.fastjson.JSONObject;
import com.didispace.mysql.dao.IFileDao;
import com.didispace.mysql.dao.IUserIDao;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SwaggerButlerEurekaApplicationTests {

	@Autowired
	private StringRedisTemplate stringRedisTemplate;
	@Autowired
	private RedisTemplate redisTemplate;

	@Autowired
	private IUserIDao iUserIDao;
	@Autowired
	private IFileDao iFileDao;

	@Test
	public void test() throws Exception {
		try {
			System.out.println(System.currentTimeMillis());
			System.out.println(stringRedisTemplate.getExpire("fileE_findAll"));
			
			List list = iFileDao.findAll();

			System.out.println(System.currentTimeMillis());
			System.out.println(JSONObject.toJSONString(list));
			Thread.sleep(1000);
			List list1 = iFileDao.findAll();
			System.out.println(System.currentTimeMillis());
			System.out.println(JSONObject.toJSONString(list1));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void testObj() throws Exception {
		boolean exists = stringRedisTemplate.hasKey("file");
		if (exists) {
			System.out.println("the value: " + redisTemplate.opsForValue().get("aaa"));
			System.out.println("the spring value: " + stringRedisTemplate.opsForValue().get("testkey"));
		} else {
			System.out.println("exists is false");
		}
	}

}
