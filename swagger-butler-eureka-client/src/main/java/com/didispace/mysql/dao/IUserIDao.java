package com.didispace.mysql.dao;

import java.util.List;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.didispace.Entity.FileE;
import com.didispace.Entity.User;

@CacheConfig(cacheNames = "user")
public interface IUserIDao extends JpaRepository<User, Integer> {
	@Cacheable(value = "findAll",key="#user.findAll")
	List<User> findAll();
}
