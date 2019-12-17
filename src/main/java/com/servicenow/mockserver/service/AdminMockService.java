package com.servicenow.mockserver.service;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.servicenow.mockserver.db.MongoMockRepository;
import com.servicenow.mockserver.model.MockAPI;
import com.servicenow.mockserver.redis.RedisRepository;

@Service
public class AdminMockService {

	static final Logger logger = LoggerFactory.getLogger(AdminMockService.class);

	@Autowired
	private RedisRepository redisRepository;


	@Autowired
	private MongoMockRepository repository;


	/**
	 * To save the mock api details in the Mongo DB
	 *
	 * @param mockAPI
	 * @return
	 */
	public MockAPI save(final MockAPI mockAPI) {
		try {

			logger.info("The mock request api is :: " + mockAPI);

			/*To Generate the UUID*/
			String uuid = UUID.randomUUID().toString();
			logger.info("The generated uuid is :: " + uuid);

			mockAPI.setId(uuid);

			/*To store in the DB*/
			repository.save(mockAPI);

			/*To save in the redis*/
			redisRepository.addEntry(mockAPI);

			return mockAPI;

		} catch (Exception error) {
			logger.error("The exception in save method :: " + error);
			error.printStackTrace();
		}
		return null;
	}

	/**
	 * To update the mock api details in the Mongo DB
	 *
	 * @param mockAPI
	 * @return
	 */
	public MockAPI update(final MockAPI mockAPI) {
		try {
			logger.info("The mock request api is :: " + mockAPI);
			repository.delete(mockAPI);
			redisRepository.delete(mockAPI.getId());
			repository.save(mockAPI);
			redisRepository.add(mockAPI);

		} catch (Exception error) {
			logger.error("The exception in update method :: " + error);
			error.printStackTrace();
		}
		return null;
	}

	/**
	 * @param id
	 * @return
	 */
	public boolean delete(final String id) {
		try {
			MockAPI mockAPI = checkAPI(id);
			repository.deleteById(id);
			redisRepository.delete(mockAPI.getApiName());
			return true;
		} catch (Exception error) {
			logger.error("The exception in delete method :: " + error);
			error.printStackTrace();
		}
		return false;
	}


	/**
	 * To check whether the api is there in mocked service
	 *
	 * @param apiPath
	 * @return
	 */
	public MockAPI checkAPI(final String id) {
		try {

			MockAPI mockApi = redisRepository.get(id);

			if (mockApi == null) {
				mockApi = repository.findFirstById(id);
			}

			if (mockApi != null && mockApi.getId() != null) {
				return mockApi;
			}
		} catch (Exception error) {
			error.printStackTrace();
		}
		return null;
	}

}
