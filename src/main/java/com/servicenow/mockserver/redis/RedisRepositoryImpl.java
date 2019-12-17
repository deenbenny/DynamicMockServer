package com.servicenow.mockserver.redis;

import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import com.servicenow.mockserver.model.MockAPI;

@Repository
public class RedisRepositoryImpl implements RedisRepository {

	private static final String KEY = "MockApi";

	private RedisTemplate<String, Object> redisTemplate;
	private HashOperations hashOperations;

	@Autowired
	public RedisRepositoryImpl(RedisTemplate<String, Object> redisTemplate) {
		this.redisTemplate = redisTemplate;
	}

	@PostConstruct
	private void init() {
		hashOperations = redisTemplate.opsForHash();
	}

	/**
	 * To add an entry in redis
	 *
	 * @param mockAPI
	 */
	public void add(final MockAPI mockAPI) {
		hashOperations.put(KEY, mockAPI.getApiName(), mockAPI);
	}

	/**
	 * To add an entry in redis
	 *
	 * @param mockAPI
	 */
	public void addEntry(final MockAPI mockAPI) {
		hashOperations.put(KEY, mockAPI.getId(), mockAPI);
	}

	/**
	 * To remove an entry in redis
	 *
	 * @param apiName
	 */
	public void delete(final String apiName) {
		hashOperations.delete(KEY, apiName);
	}

	/**
	 * To remove an entry in redis
	 *
	 * @param apiName
	 */
	public void deleteAll() {
		hashOperations.delete(KEY);
	}

	/**
	 * To remove an entry in redis
	 *
	 * @param apiName
	 */
	public MockAPI get(final String id) {
		return (MockAPI) hashOperations.get(KEY, id);
	}


	/**
	 * To get an entry in redis
	 *
	 * @param id
	 * @return
	 */
	public MockAPI getMockAPI(final String apiName) {
		return (MockAPI) hashOperations.get(KEY, apiName);
	}

	/**
	 * To get all the entries in redis
	 *
	 * @return
	 */
	public Map<Object, Object> getAllMockAPI() {
		return hashOperations.entries(KEY);
	}


}