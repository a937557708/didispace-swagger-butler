package com.didispace.mysql.dao;

import java.util.List;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.didispace.Entity.FileE;

@CacheConfig(cacheNames = "fileE")
public interface IFileDao extends BaseRepository<FileE, Integer> {
	@Cacheable
	List<FileE> findAll();
}
