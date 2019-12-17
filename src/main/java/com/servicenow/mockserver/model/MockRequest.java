package com.servicenow.mockserver.model;

import java.io.Serializable;
import java.util.Map;

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
public class MockRequest implements Serializable {

	@JsonProperty("method_type")
	private String methodType;

	private String body;

	private Map<String, String> headers;

	private String urlPattern;

	@JsonProperty("query_param")
	private Map<String, String> queryParams;

}
