package com.servicenow.mockserver.model;

import java.io.Serializable;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Document(collection = "mock")
public class MockAPI implements Serializable {

	@Id
	private String id;

	@JsonProperty("api_name")
	@Indexed(unique = true)
	private String apiName;

	private MockRequest request;
	private MockResponse response;

}
