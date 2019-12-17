package com.servicenow.mockserver.controller;

import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.HandlerMapping;

import com.google.gson.Gson;
import com.servicenow.mockserver.config.RestConfig;
import com.servicenow.mockserver.model.CustomMsg;
import com.servicenow.mockserver.model.MockAPI;
import com.servicenow.mockserver.service.MockService;

@RestController
public class MockController {

	static final Logger logger = LoggerFactory.getLogger(MockController.class);

	@Autowired
	private Gson gson;

	@Autowired
	private MockService mockService;

	@ResponseBody
	@RequestMapping(value = "/**", method = GET, produces = "application/json")
	public ResponseEntity<?> mockGetCallAPI(HttpServletRequest request) {

		String restOfTheUrl = getAPIPath(request);
		MockAPI mockAPI = mockService.process(restOfTheUrl, GET);
		if (mockAPI == null) {
			return new ResponseEntity(gson.toJson(new CustomMsg("Unable to find the mock api")), HttpStatus.INTERNAL_SERVER_ERROR);
		}

		if (mockAPI.getResponse().getHeaders() == null) {
			return new ResponseEntity(mockAPI.getResponse().getBody(),
					RestConfig.getResponseStatus(Integer.parseInt(mockAPI.getResponse().getStatus())));
		} else {
			return new ResponseEntity(mockAPI.getResponse().getBody(), RestConfig.getHTTPHeaders(mockAPI.getResponse().getHeaders()),
					RestConfig.getResponseStatus(Integer.parseInt(mockAPI.getResponse().getStatus())));
		}

	}

	@ResponseBody
	@RequestMapping(value = "/**", method = POST, produces = "application/json")
	public ResponseEntity<?> mockPostCallAPI(HttpServletRequest request) {
		String restOfTheUrl = getAPIPath(request);
		logger.info("Calling the mock POST API, for the api path " + restOfTheUrl);
		MockAPI mockAPI = mockService.process(restOfTheUrl, POST);
		if (mockAPI == null) {
			return new ResponseEntity(gson.toJson(new CustomMsg("Unable to find the mock api")), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity(gson.toJson(mockAPI.getResponse().getBody()), RestConfig.getHTTPHeaders(mockAPI.getResponse().getHeaders()),
				RestConfig.getResponseStatus(Integer.parseInt(mockAPI.getResponse().getStatus())));
	}

	@ResponseBody
	@RequestMapping(value = "/**", method = PUT, produces = "application/json")
	public ResponseEntity<?> mockPutCallAPI(HttpServletRequest request) {
		String restOfTheUrl = getAPIPath(request);
		MockAPI mockAPI = mockService.process(restOfTheUrl, PUT);
		logger.info("Calling the mock PUT API, for the api path " + restOfTheUrl);
		if (mockAPI == null) {
			return new ResponseEntity(gson.toJson(new CustomMsg("Unable to find the mock api")), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity(gson.toJson(mockAPI.getResponse().getBody()), RestConfig.getHTTPHeaders(mockAPI.getResponse().getHeaders()),
				RestConfig.getResponseStatus(Integer.parseInt(mockAPI.getResponse().getStatus())));
	}

	@ResponseBody
	@RequestMapping(value = "/**", method = DELETE, produces = "application/json")
	public ResponseEntity<?> mockDeleteCallAPI(HttpServletRequest request) {
		String restOfTheUrl = getAPIPath(request);
		MockAPI mockAPI = mockService.process(restOfTheUrl, DELETE);
		logger.info("Calling the mock DELETE API, for the api path " + restOfTheUrl);
		if (mockAPI == null) {
			return new ResponseEntity(gson.toJson(new CustomMsg("Unable to find the mock api")), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity(gson.toJson(mockAPI.getResponse().getBody()), RestConfig.getHTTPHeaders(mockAPI.getResponse().getHeaders()),
				RestConfig.getResponseStatus(Integer.parseInt(mockAPI.getResponse().getStatus())));
	}

	private String getAPIPath(final HttpServletRequest request) {
		String url = (String) request.getAttribute(
				HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
		String queryString = (String) request.getQueryString();
		if(null != queryString)
		{
			url =url+"?" + queryString;
			System.out.println(queryString);
		}
		return url;
	}

}
