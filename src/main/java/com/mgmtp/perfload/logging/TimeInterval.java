/*
 *  Copyright (c) mgm technology partners GmbH, Munich.
 *
 *  See the copyright.txt file distributed with this work for additional
 *  information regarding copyright ownership and intellectual property rights.
 */
package com.mgmtp.perfload.logging;

import java.util.concurrent.TimeUnit;

/**
 * Utility class for measuring time intervals.
 * 
 * @author rnaeegele
 */
public class TimeInterval {
	private static final long SECOND = 1000L;
	private static final long MINUTE = 60000L;
	private static final long HOUR = 3600000L;

	private long start;
	private long end;

	/**
	 * Starts measuring (i. e. sets the start time).
	 */
	public void start() {
		start = end = System.nanoTime();
	}

	/**
	 * Stops measuring (i. e. sets the end time).
	 */
	public void stop() {
		end = System.nanoTime();
	}

	/**
	 * @return The interval in milliseconds.
	 */
	public long length() {
		if (end == 0) {
			if (start != 0) {
				stop();
			} else {
				return -1L;
			}
		}
		return TimeUnit.NANOSECONDS.toMillis(end - start);
	}

	/**
	 * Returns the interval with the format H:mm:ss:SSS using <br />
	 * {@code String.format("%d:%02d:%02d,%03d", hours, minutes, seconds, millis)}.
	 * 
	 * @return the formatted interval
	 */
	public String format() {
		long duration = length();

		int hours = (int) (duration / HOUR);
		duration = duration - hours * HOUR;

		int minutes = (int) (duration / MINUTE);
		duration = duration - minutes * MINUTE;

		int seconds = (int) (duration / SECOND);
		duration = duration - seconds * SECOND;

		int millis = (int) duration;

		return String.format("%d:%02d:%02d,%03d", hours, minutes, seconds, millis);
	}

	@Override
	public String toString() {
		return "TimeInterval[" + format() + "]";
	}
}