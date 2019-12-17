package com.servicenow.mockserver.controller;

import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.HandlerMapping;

import com.google.gson.Gson;
import com.servicenow.mockserver.model.CustomMsg;
import com.servicenow.mockserver.model.MockAPI;
import com.servicenow.mockserver.service.MockService;
import com.servicenow.mockserver.utils.JSONUtils;

@RestController
public class APIController {

	static final Logger logger = LoggerFactory.getLogger(APIController.class);

	@Autowired
	private MockService mockService;

	@Autowired
	private Gson gson;

	/**
	 * To create an mock api
	 *
	 * @param allRequestParams
	 * @param mockAPI
	 * @return
	 */
	@ResponseBody
	@RequestMapping( value = "/mock/api/create", method = POST )
	public ResponseEntity<?> register(@RequestBody MockAPI mockAPI) {

		logger.info(" mock api :: " + mockAPI);

		if (mockAPI == null || mockAPI.getApiName() == null || mockAPI.getRequest() == null ||
				mockAPI.getRequest().getMethodType() == null || mockAPI.getResponse() == null || mockAPI.getResponse().getBody() == null ||
				Integer.parseInt(mockAPI.getResponse().getStatus()) == 0 || mockAPI.getResponse().getDelayDistribution() == null) {
			logger.error("Invalid mock request payload");
			return new ResponseEntity("Invalid mock payload", HttpStatus.BAD_REQUEST);
		}

		if (!JSONUtils.isJSONValid(mockAPI.getResponse().getBody())) {
			logger.error("Invalid mock response json payload");
			return new ResponseEntity("Invalid mock response json payload", HttpStatus.BAD_REQUEST);
		}

		MockAPI mockApi = mockService.checkAPI(mockAPI.getApiName(), false);

		if (mockApi != null) {
			logger.error("Unable to register this api. A api with name {} already exist", mockAPI.getApiName());
			return new ResponseEntity("Unable to register. A api with name " +
					mockAPI.getApiName() + " already exist.", HttpStatus.CONFLICT);
		}
		
		if(null == mockAPI.getRequest().getUrlPattern())
		{
			mockAPI.getRequest().setUrlPattern(mockAPI.getApiName());
		}

		mockApi = mockService.save(mockAPI);

		if (mockApi == null) {
			logger.error("Unable to register this api. A api with name {} already exist", mockAPI.getApiName());
			return new ResponseEntity("Unable to register", HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return new ResponseEntity("Mocked the api : " + mockAPI.getApiName() + " successfully", HttpStatus.CREATED);
	}

	/**
	 * To update an mock api
	 *
	 * @param mockAPI
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/mock/api/update/**", method = PUT, produces = "application/json")
	public ResponseEntity<?> update(HttpServletRequest request, @RequestBody MockAPI mockAPI) {

		String relApiName = (String) request.getAttribute(
				HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);

		String split[] = relApiName.split("update");
		String apiName = split[1];

		logger.info("Updating the mock api :: " + apiName);

		if (!apiName.equals(mockAPI.getApiName())) {
			logger.error("Unable to update this api. A api with name {} doesn't exist", mockAPI.getApiName());
			return new ResponseEntity(gson.toJson(new CustomMsg("Unable to update. A api with name " +
					mockAPI.getApiName() + " doesn't exist.")), HttpStatus.NOT_FOUND);
		}

		MockAPI mockApi = mockService.checkAPI(mockAPI.getApiName(), false);

		if (mockApi == null) {
			logger.error("Unable to update this api. A api with name {} doesn't exist", mockAPI.getApiName());
			return new ResponseEntity(gson.toJson(new CustomMsg("Unable to update. A api with name " +
					mockAPI.getApiName() + " doesn't exist.")), HttpStatus.NOT_FOUND);
		}
		mockAPI.setId(mockApi.getId());
		mockApi = mockService.update(apiName, mockAPI);

		if (mockApi == null) {
			return new ResponseEntity(gson.toJson(new CustomMsg("Unable to update the api. " + apiName + " not found.")),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return new ResponseEntity(gson.toJson(new CustomMsg("Updated the api " + apiName + " successfully")), HttpStatus.OK);
	}

	/**
	 * To delete an mock api
	 *
	 * @param mockAPI
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/mock/api/delete/**", method = DELETE, produces = "application/json")
	public ResponseEntity<?> delete(HttpServletRequest request) {

		String relApiName = (String) request.getAttribute(
				HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);

		String split[] = relApiName.split("delete");
		String apiName = split[1];

		logger.info("Deleting the mock api :: " + apiName);

		MockAPI mockApi = mockService.checkAPI(apiName, false);

		if (mockApi == null) {
			logger.error("Unable to delete this api. A api with name {} doesn't exist", apiName);
			return new ResponseEntity(gson.toJson(new CustomMsg("Unable to delete. A api with name " +
					apiName + " doesn't exist.")), HttpStatus.NOT_FOUND);
		}

		boolean status = mockService.delete(apiName, mockApi);

		if (status) {
			return new ResponseEntity(gson.toJson(new CustomMsg("Unable to delete the api. " + apiName + " not found.")),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return new ResponseEntity(gson.toJson(new CustomMsg("Deleted the api " + apiName + " successfully")), HttpStatus.OK);
	}

	/**
	 * To Delete all the mock apis
	 *
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/mock/api/delete/all", method = DELETE, produces = "application/json")
	public ResponseEntity<?> deleteAllMockAPI() {
		boolean status = mockService.deleteAllMockedAPI();

		if (status) {
			return new ResponseEntity(gson.toJson(new CustomMsg("Unable to delete all the apis ")),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return new ResponseEntity(gson.toJson(new CustomMsg("Deleted the all apis successfully")), HttpStatus.OK);

	}

}
