package com.servicenow.mockserver.redis;

import java.util.Map;

import com.servicenow.mockserver.model.MockAPI;

public interface RedisRepository {

	/**
	 * Add key-value pair to Redis.
	 */
	void add(MockAPI mockAPI);

	void addEntry(MockAPI mockAPI);

	/**
	 * Delete a key-value pair in Redis.
	 */
	void delete(String id);

	/**
	 * find a mockAPI
	 */
	MockAPI get(String id);

	/**
	 * find a mockAPI
	 */
	MockAPI getMockAPI(String apiName);

	/**
	 * Return all mocked api
	 */
	Map<Object, Object> getAllMockAPI();


}
