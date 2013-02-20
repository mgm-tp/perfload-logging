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

import static org.apache.commons.lang3.StringUtils.trimToEmpty;
import static org.testng.Assert.assertEquals;

import java.io.IOException;
import java.net.InetAddress;
import java.util.UUID;

import org.apache.commons.lang3.text.StrBuilder;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.mgmtp.perfload.logging.DefaultResultLogger;
import com.mgmtp.perfload.logging.ResultLogger;
import com.mgmtp.perfload.logging.SimpleLogger;
import com.mgmtp.perfload.logging.TimeInterval;

/**
 * @author rnaegele
 */
public class DefaultResultLoggerTest {

	private static final String EXP_RESULT_TEMPLATE =
			"\"1\";\"2\";\"3\";\"%s\";\"-1\";\"-1\";\"operation\";\"target\";\"%s\";\"%s\";\"GET\";"
					+ "\"http://localhost/someUri\";\"someUriAlias\";\"%s\";\"client\";\"%s\";\"%s\"";

	private final String timestamp = DefaultResultLogger.DATE_FORMAT.format(1L);
	private InetAddress localhost;
	private ResultLogger logger;
	private TestLogger tl;
	private UUID requestId;
	private final UUID executionId = UUID.randomUUID();

	@BeforeTest
	public void init() throws IOException {
		tl = new TestLogger();
		localhost = InetAddress.getLocalHost();
		logger = new DefaultResultLogger(tl, localhost, "client", "operation", "target", 1, 2, 3);
		requestId = UUID.randomUUID();
	}

	private String createExpectedResultString(final String errorMsg, final Object... extras) {
		String success = errorMsg != null ? "ERROR" : "SUCCESS";

		StrBuilder sb = new StrBuilder(250);
		sb.setNullText("");

		String result = String.format(EXP_RESULT_TEMPLATE, timestamp, success, trimToEmpty(errorMsg), localhost, executionId,
				requestId);
		sb.append(result);
		if (extras != null) {
			for (Object extra : extras) {
				sb.append(";\"");
				sb.append(extra);
				sb.append("\"");
			}
		}
		return sb.toString();
	}

	@Test
	public void testWithoutExtras() {
		logger.logResult(1L, new TimeInterval(), new TimeInterval(), "GET", "http://localhost/someUri", "someUriAlias",
				executionId, requestId);
		String expected = createExpectedResultString(null);
		assertLogMessage(expected);
	}

	@Test
	public void testWithExtras() {
		logger.logResult(1L, new TimeInterval(), new TimeInterval(), "GET", "http://localhost/someUri", "someUriAlias",
				executionId, requestId, "extra1", "extra2", null);
		String expected = createExpectedResultString(null, "extra1", "extra2", null);
		assertLogMessage(expected);
	}

	@Test
	public void testWithLineBreaks() {
		final String unnormalized = "  foo\"\"  bar\r\nbaz\r\n\r\nblubb\n\n\n\"blah\"   blah\n42\n";
		final String normalized = "  foo\"\"\"\"  bar baz blubb \"\"blah\"\"   blah 42";
		logger.logResult(1L, new TimeInterval(), new TimeInterval(), "GET", "http://localhost/someUri", "someUriAlias",
				executionId, requestId,	unnormalized);
		String expected = createExpectedResultString(null, normalized);
		assertLogMessage(expected);
	}

	@Test
	public void testWithError() {
		final String errorMessage = "my little test error message";
		logger.logResult(errorMessage, 1L, new TimeInterval(), new TimeInterval(), "GET", "http://localhost/someUri",
				"someUriAlias", executionId, requestId);
		String expected = createExpectedResultString(errorMessage);
		assertLogMessage(expected);
	}

	private void assertLogMessage(final String expected) {
		assertEquals(tl.getLastOutput(), expected);
	}

	static class TestLogger implements SimpleLogger {

		private String lastOutput;

		@Override
		public void open() throws IOException {
			// no-op
		}

		@Override
		public void close() {
			// no-op
		}

		@Override
		public void writeln(final String output) {
			this.lastOutput = output;
		}

		/**
		 * @return the lastOutput
		 */
		public String getLastOutput() {
			return lastOutput;
		}
	}
}
