package com.servicenow.mockserver.utils;

import java.util.concurrent.ThreadLocalRandom;

public class RandomUtils {

	private RandomUtils() {
		//do nothing
	}

	public static Integer getRandomInteger(final int start, final int end) {
		return ThreadLocalRandom.current().nextInt(start, end);
	}

	public static Double getRandomDouble(final double start, final double end) {
		return ThreadLocalRandom.current().nextDouble(start, end);
	}

	public static Long getRandomLong(final long start, final long end) {
		return ThreadLocalRandom.current().nextLong(start, end);
	}


}