package com.servicenow.mockserver.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMethod;

import com.servicenow.mockserver.db.MongoMockRepository;
import com.servicenow.mockserver.model.MockAPI;
import com.servicenow.mockserver.model.MockResponse;
import com.servicenow.mockserver.redis.RedisRepository;

@Service
public class MockService {

	static final Logger logger = LoggerFactory.getLogger(MockService.class);

	@Autowired
	private RedisRepository redisRepository;
	
	 String Random ="RANDOM";


	@Autowired
	private MongoMockRepository repository;

	/**
	 * To process the mock server
	 *
	 * @param apiPath
	 * @return
	 */
	public MockAPI process(final String apiPath, final RequestMethod apiType) {
		try {
			MockAPI mockApi = checkAPI(apiPath, true);
		if (mockApi != null && mockApi.getRequest().getMethodType().equalsIgnoreCase(apiType.toString())) {
				if (mockApi != null) {
//					randomize anything that has a random string
//					call the count api
//					replace only the first each time
					String mockedResponse = mockApi.getResponse().getBody();
					int countOfRandom = countOccurences(mockedResponse,Random);
					MockResponse response = mockApi.getResponse();
					for (int i=0;i<countOfRandom;i++)
					{
					mockedResponse = mockApi.getResponse().getBody();	
					response.setBody(mockedResponse.replaceFirst(Random , new Random().nextInt(10000) +""));
					mockApi.setResponse(response);
					}
					TimeUnit.MILLISECONDS.sleep(Integer.parseInt(mockApi.getResponse().getDelayDistribution().getMedian()));
					return mockApi;
				}
			}

		} catch (Exception error) {
			error.printStackTrace();
		}
		return null;
	}

	/**
	 * To get all the mocked api details from DB
	 *
	 * @return
	 */
	public List getAllMockedAPI() {
		try {
			return repository.findAll();
		} catch (Exception error) {
			logger.error("The exception is getAllMockedAPI method is :: " + error);
			error.printStackTrace();
		}
		return new ArrayList();
	}

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
			redisRepository.add(mockAPI);

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
	public MockAPI update(final String apiName, final MockAPI mockAPI) {
		try {

			logger.info("The mock request api is :: " + mockAPI);
			repository.delete(mockAPI);
			redisRepository.delete(mockAPI.getApiName());
			repository.save(mockAPI);
			redisRepository.add(mockAPI);
			return checkAPI(mockAPI.getApiName(), false);

		} catch (Exception error) {
			logger.error("The exception in update method :: " + error);
			error.printStackTrace();
		}
		return null;
	}


	public boolean delete(final String apiName, final MockAPI mockApi) {
		try {

			logger.info("The mock request api is :: " + mockApi);
			repository.delete(mockApi);
			redisRepository.delete(apiName);
			MockAPI mockAPI = checkAPI(apiName, false);
			return mockAPI == null ? true : false;

		} catch (Exception error) {
			logger.error("The exception in delete method :: " + error);
			error.printStackTrace();
		}
		return false;
	}

	/**
	 * To delete all the mocked api from the mongo
	 *
	 * @return
	 */
	public boolean deleteAllMockedAPI() {
		try {
			repository.deleteAll();

			String command = "redis-cli flushall";
			String output = executeCommand(command);
			System.out.println(output);

			return true;
		} catch (Exception error) {
			logger.error("The exception is getAllMockedAPI method is :: " + error);
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
	public MockAPI checkAPI(final String apiPath, final boolean absolutePath) {
		try {

			String path = apiPath;

//			if (absolutePath) {
//				path = getAbsolutePath(apiPath);
//			}


			MockAPI mockApi = redisRepository.getMockAPI(path);

			if (mockApi == null) {
				mockApi = repository.findFirstByApiName(path);
			}

			if (mockApi != null && mockApi.getId() != null) {
				return mockApi;
			}
		} catch (Exception error) {
			error.printStackTrace();
		}
		return null;
	}


	/**
	 * To get the absolute path of an api
	 *
	 * @param apiPath
	 * @return
	 */
//	public String getAbsolutePath(final String apiPath) {
//
//	
//
//		return absoluteAPIPath.toString();
//	}

	/**
	 * @param command
	 * @return
	 */
	private String executeCommand(String command) {

		StringBuffer output = new StringBuffer();

		Process p;
		try {
			p = Runtime.getRuntime().exec(command);
			p.waitFor();
			BufferedReader reader =
					new BufferedReader(new InputStreamReader(p.getInputStream()));

			String line = "";
			while ((line = reader.readLine()) != null) {
				output.append(line + "\n");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return output.toString();

	}
	
	
	int countOccurences(String str, String word)  
	{ 
	    // split the string by spaces in a 
	    String a[] = str.split(","); 
	  
	    // search for pattern in a 
	    int count = 0; 
	    for (int i = 0; i < a.length; i++)  
	    { 
	    // if match found increase count 
	    if (a[i].contains(word)) 
	        count++; 
	    } 
	  
	    return count; 
	} 

}