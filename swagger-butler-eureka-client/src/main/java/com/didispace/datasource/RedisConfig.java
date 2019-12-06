package com.didispace.datasource;

import static java.util.Collections.singletonMap;

import java.lang.reflect.Method;
import java.time.Duration;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
@AutoConfigureAfter(RedisAutoConfiguration.class)
public class RedisConfig extends CachingConfigurerSupport {

	@Bean
	CacheManager cacheManager(RedisConnectionFactory connectionFactory) {

		/* 默认配置， 默认超时时间为30s */

		RedisCacheConfiguration defaultCacheConfig = RedisCacheConfiguration.defaultCacheConfig()
				.entryTtl(Duration.ofSeconds(60*60L)).disableCachingNullValues();
		RedisCacheManager cacheManager = RedisCacheManager
				.builder(RedisCacheWriter.lockingRedisCacheWriter(connectionFactory)).cacheDefaults(defaultCacheConfig)
				.withInitialCacheConfigurations(
						singletonMap("test", RedisCacheConfiguration.defaultCacheConfig().disableCachingNullValues()))
				.transactionAware().build();

		return cacheManager;
	}

	/* 设置RedisTemplate */
	@Bean
	public RedisTemplate<String, String> redisTemplate(RedisConnectionFactory factory) {
		StringRedisTemplate template = new StringRedisTemplate(factory);
		setSerializer(template);// 设置序列化工具
		template.afterPropertiesSet();
		return template;
	}

	/* 设置序列化工具 */
	private void setSerializer(StringRedisTemplate template) {
		@SuppressWarnings({ "rawtypes", "unchecked" })
		Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
		ObjectMapper om = new ObjectMapper();
		om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
		om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
		jackson2JsonRedisSerializer.setObjectMapper(om);
		template.setValueSerializer(jackson2JsonRedisSerializer);
	}

	@Bean
	public KeyGenerator keyGenerator() {
		return new KeyGenerator() {
			// 为给定的方法及其参数生成一个键
			// 格式为：com.frog.mvcdemo.controller.FrogTestController-show-[params]
			@Override
			public Object generate(Object target, Method method, Object... params) {
				StringBuffer sb = new StringBuffer();
				sb.append(target.getClass().getName());// 类名
				sb.append("-");
				sb.append(method.getName());// 方法名
				sb.append("-");
				for (Object param : params) {
					sb.append(param.toString());// 参数
				}
				return sb.toString();
			}
		};
	}
}
