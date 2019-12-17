package com.servicenow.mockserver.config;

import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

public class RestConfig {

	public static HttpStatus getResponseStatus(final int code) {

		switch (code) {
			case 100:
				return HttpStatus.CONTINUE;
			case 101:
				return HttpStatus.SWITCHING_PROTOCOLS;
			case 102:
				return HttpStatus.PROCESSING;
			case 200:
				return HttpStatus.OK;
			case 201:
				return HttpStatus.CREATED;
			case 202:
				return HttpStatus.ACCEPTED;
			case 203:
				return HttpStatus.NON_AUTHORITATIVE_INFORMATION;
			case 204:
				return HttpStatus.NO_CONTENT;
			case 205:
				return HttpStatus.RESET_CONTENT;
			case 206:
				return HttpStatus.PARTIAL_CONTENT;
			case 207:
				return HttpStatus.MULTI_STATUS;
			case 208:
				return HttpStatus.ALREADY_REPORTED;
			case 226:
				return HttpStatus.IM_USED;
			case 300:
				return HttpStatus.MULTIPLE_CHOICES;
			case 301:
				return HttpStatus.MOVED_PERMANENTLY;
			case 302:
				return HttpStatus.FOUND;
			case 303:
				return HttpStatus.SEE_OTHER;
			case 304:
				return HttpStatus.NOT_MODIFIED;
			case 305:
				return HttpStatus.USE_PROXY;
			case 307:
				return HttpStatus.TEMPORARY_REDIRECT;
			case 308:
				return HttpStatus.PERMANENT_REDIRECT;
			case 400:
				return HttpStatus.BAD_REQUEST;
			case 401:
				return HttpStatus.UNAUTHORIZED;
			case 402:
				return HttpStatus.PAYMENT_REQUIRED;
			case 403:
				return HttpStatus.FORBIDDEN;
			case 404:
				return HttpStatus.NOT_FOUND;
			case 405:
				return HttpStatus.METHOD_NOT_ALLOWED;
			case 406:
				return HttpStatus.NOT_ACCEPTABLE;
			case 407:
				return HttpStatus.PROXY_AUTHENTICATION_REQUIRED;
			case 408:
				return HttpStatus.REQUEST_TIMEOUT;
			case 409:
				return HttpStatus.CONFLICT;
			case 410:
				return HttpStatus.GONE;
			case 411:
				return HttpStatus.LENGTH_REQUIRED;
			case 412:
				return HttpStatus.PRECONDITION_FAILED;
			case 413:
				return HttpStatus.PAYLOAD_TOO_LARGE;
			case 414:
				return HttpStatus.URI_TOO_LONG;
			case 415:
				return HttpStatus.UNSUPPORTED_MEDIA_TYPE;
			case 416:
				return HttpStatus.REQUESTED_RANGE_NOT_SATISFIABLE;
			case 417:
				return HttpStatus.EXPECTATION_FAILED;
			case 418:
				return HttpStatus.I_AM_A_TEAPOT;
			case 422:
				return HttpStatus.UNPROCESSABLE_ENTITY;
			case 423:
				return HttpStatus.LOCKED;
			case 424:
				return HttpStatus.FAILED_DEPENDENCY;
			case 426:
				return HttpStatus.UPGRADE_REQUIRED;
			case 428:
				return HttpStatus.PRECONDITION_REQUIRED;
			case 429:
				return HttpStatus.TOO_MANY_REQUESTS;
			case 431:
				return HttpStatus.REQUEST_HEADER_FIELDS_TOO_LARGE;
			case 451:
				return HttpStatus.UNAVAILABLE_FOR_LEGAL_REASONS;
			case 500:
				return HttpStatus.INTERNAL_SERVER_ERROR;
			case 501:
				return HttpStatus.NOT_IMPLEMENTED;
			case 502:
				return HttpStatus.BAD_GATEWAY;
			case 503:
				return HttpStatus.SERVICE_UNAVAILABLE;
			case 504:
				return HttpStatus.GATEWAY_TIMEOUT;
			case 505:
				return HttpStatus.HTTP_VERSION_NOT_SUPPORTED;
			case 506:
				return HttpStatus.VARIANT_ALSO_NEGOTIATES;
			case 507:
				return HttpStatus.INSUFFICIENT_STORAGE;
			case 508:
				return HttpStatus.LOOP_DETECTED;
			case 510:
				return HttpStatus.NOT_EXTENDED;
			case 511:
				return HttpStatus.NETWORK_AUTHENTICATION_REQUIRED;
		}
		return null;

	}


	public static HttpHeaders getHTTPHeaders(final Map<String, String> headers) {

		HttpHeaders httpHeaders = new HttpHeaders();

		for (Map.Entry<String, String> entry : headers.entrySet()) {
			httpHeaders.add(entry.getKey(), entry.getValue());
		}

		return httpHeaders;
	}

}
