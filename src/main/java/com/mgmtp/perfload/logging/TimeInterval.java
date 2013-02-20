/*
 * Copyright (c) 2013 mgm technology partners GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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