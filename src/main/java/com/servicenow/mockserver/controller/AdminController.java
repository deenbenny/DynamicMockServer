package com.servicenow.mockserver.controller;

import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.servicenow.mockserver.model.CustomMsg;
import com.servicenow.mockserver.model.MockAPI;
import com.servicenow.mockserver.service.AdminMockService;
import com.servicenow.mockserver.service.MockService;
import com.servicenow.mockserver.utils.JSONUtils;

@Controller
public class AdminController {

	static final Logger logger = LoggerFactory.getLogger(AdminController.class);

	@Autowired
	private MockService mockService;

	@Autowired
	private AdminMockService adminMockService;

	@Autowired
	private Gson gson;

	@RequestMapping("/")
	public String welcome(Map<String, Object> model) {
		return "index";
	}

	@ResponseBody
	@RequestMapping(value = "/__admin", method = GET, produces = "application/json")
	public ResponseEntity<?> getAll() {
		logger.info("Get all the mocked api");

		List<MockAPI> mockAPIList = mockService.getAllMockedAPI();

		if (mockAPIList.isEmpty()) {
			return new ResponseEntity(HttpStatus.NO_CONTENT);
		}

		JSONObject jsonObject = new JSONObject();
		jsonObject.put("mappings", mockAPIList);
		String payload = jsonObject.toString();

		logger.info("Response from get all api :: " + payload);
		return new ResponseEntity<>(payload, HttpStatus.OK);
	}

	@ResponseBody
	@RequestMapping(value = "/__admin/mappings", method = POST, produces = "application/json")
	public ResponseEntity<?> add(@RequestBody String payload) {

		logger.info("Registering an api in the mock service ");

		MockAPI mockAPI = gson.fromJson(payload, MockAPI.class);

		logger.info("The payload is :: " + mockAPI);

		if (!JSONUtils.isJSONValid(mockAPI.getResponse().getBody())) {
			logger.error("Invalid mock response json payload");
			return new ResponseEntity(gson.toJson(new CustomMsg("Invalid mock response json payload")), HttpStatus.BAD_REQUEST);
		}

		MockAPI mockApi = mockService.checkAPI(mockAPI.getApiName(), false);

		if (mockApi != null) {
			logger.error("Unable to register this api. A api with name {} already exist", mockAPI.getApiName());
			return new ResponseEntity(gson.toJson(new CustomMsg("Unable to register. A api with name " +
					mockAPI.getApiName() + " already exist.")), HttpStatus.CONFLICT);
		}

		mockApi = mockService.save(mockAPI);

		if (mockApi == null) {
			logger.error("Unable to register this api. A api with name {} already exist", mockAPI.getApiName());
			return new ResponseEntity(gson.toJson(new CustomMsg("Unable to register")), HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return new ResponseEntity(gson.toJson(new CustomMsg("Mocked the api : " + mockAPI.getApiName())) + " successfully", HttpStatus.CREATED);
	}

	@ResponseBody
	@RequestMapping(value = "/__admin/mappings/{id}", method = PUT, produces = "application/json")
	public ResponseEntity<?> update(@PathVariable("id") String id, @RequestBody String payload) {
		logger.info("Updating the mock api id :: " + id);
		MockAPI mockAPI = gson.fromJson(payload, MockAPI.class);
		logger.info("The moc :: " + mockAPI.toString());

		MockAPI mockApi = mockService.update(mockAPI.getApiName(), mockAPI);

		if (mockApi == null) {
			return new ResponseEntity(gson.toJson(new CustomMsg("Unable to update the api. " + mockAPI.getApiName() + " not found.")),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return new ResponseEntity(gson.toJson(new CustomMsg("Updated the api " + mockAPI.getApiName() + " successfully")), HttpStatus.OK);

	}

	@ResponseBody
	@RequestMapping(value = "/__admin/mappings/{id}", method = DELETE, produces = "application/json")
	public ResponseEntity<?> delete(@PathVariable("id") String id) {
		logger.info("Delete the mock api for the id :: " + id);

		boolean status = adminMockService.delete(id);

		if (status) {
			return new ResponseEntity(gson.toJson(new CustomMsg("Unable to delete the api. ")),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return new ResponseEntity(gson.toJson(new CustomMsg("Deleted the api successfully")), HttpStatus.OK);
	}

}