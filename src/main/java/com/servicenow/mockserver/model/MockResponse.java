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
public class MockResponse implements Serializable {

	private String status;
	private String body;
	private Map<String, String> headers;

	@JsonProperty("delay_distribution")
	private DelayDistribution delayDistribution;
}
