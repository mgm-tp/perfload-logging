/*
 *  Copyright (c) mgm technology partners GmbH, Munich.
 *
 *  See the copyright.txt file distributed with this work for additional
 *  information regarding copyright ownership and intellectual property rights.
 */
package com.mgmtp.perfload.logging;

import java.net.InetAddress;
import java.util.UUID;

import org.apache.commons.lang3.time.FastDateFormat;

/**
 * <p>
 * Default {@link ResultLogger} implementation for logging test results to a log file. This
 * implementation may be used exterally when applications need to log measurements directly, e. g.
 * in special aspects for load tests.
 * </p>
 * <p>
 * Measurements are logged in the following format: <br />
 * {@code "daemonId";"processId";"threadId";"timestamp (ms)";"execution time 1 (ms)";"execution time 2 (ms)";"operation";"target";"(ERROR|SUCCESS)";"error msg or empty string";"type";"uri";"uriAlias";"localAddress";"layer";"executionId";"extraArgs[0]";"extraArgs[1]";...}
 * </p>
 * <p>
 * For perfLoad tests, the {@code reserved} parameter could be used to log {@code daemonId} and
 * {@code processId}. If present, all {@code extraArgs} are appended to the end of the log message.
 * Values are quoted and delimited by semi-colons. Quote characters are esccaped with an additional
 * quote character. For each value, line breaks are replaced with spaces. Multiple line breaks are
 * collapsed to a single space.
 * </p>
 * 
 * @author rnaegele
 */
public class DefaultResultLogger implements ResultLogger {
	private static final char CSV_QUOTE = '"';

	// ISO8601 timestamp with milliseconds and timezone
	static final FastDateFormat DATE_FORMAT = FastDateFormat.getInstance("yyyy-MM-dd'T'HH:mm:ss.SSSZZ");

	private final SimpleLogger logger;
	protected final InetAddress localAddress;
	protected final String layer;
	protected final String operation;
	protected final String target;
	protected final int daemonId;
	protected final int processId;
	protected final int threadId;

	/**
	 * @param logger
	 *            the underlying logger to use
	 * @param localAddress
	 *            the local address of the client
	 * @param layer
	 *            some identifier for the layer in which the result is logged (e. g. client, server,
	 *            ...)
	 * @param operation
	 *            the operation
	 * @param target
	 *            the target
	 */
	public DefaultResultLogger(final SimpleLogger logger, final InetAddress localAddress, final String layer,
			final String operation,	final String target, final int daemonId, final int processId, final int threadId) {
		this.logger = logger;
		this.localAddress = localAddress;
		this.layer = layer;
		this.operation = operation;
		this.target = target;
		this.daemonId = daemonId;
		this.processId = processId;
		this.threadId = threadId;
	}

	/**
	 * Delegates to
	 * {@link #logResult(String, long, TimeInterval, TimeInterval, String, String, String, UUID, UUID, Object...)}
	 * .
	 */
	@Override
	public void logResult(final long timestamp, final TimeInterval ti1, final TimeInterval ti2, final String type,
			final String uri, final String uriAlias, final UUID executionId, final UUID requestId, final Object... extraArgs) {
		logResult(null, timestamp, ti1, ti2, type, uri, uriAlias, executionId, requestId, extraArgs);
	}

	/**
	 * {@inheritDoc} See class comment above for details.
	 */
	@Override
	public void logResult(final String errorMessage, final long timestamp, final TimeInterval ti1, final TimeInterval ti2,
			final String type, final String uri, final String uriAlias, final UUID executionId, final UUID requestId,
			final Object... extraArgs) {
		StringBuilder sb = new StringBuilder(250);
		appendEscapedAndQuoted(sb, daemonId);
		appendEscapedAndQuoted(sb, processId);
		appendEscapedAndQuoted(sb, threadId);
		appendEscapedAndQuoted(sb, DATE_FORMAT.format(timestamp));
		appendEscapedAndQuoted(sb, ti1.length());
		appendEscapedAndQuoted(sb, ti2.length());
		appendEscapedAndQuoted(sb, operation);
		appendEscapedAndQuoted(sb, target);
		if (errorMessage != null) {
			appendEscapedAndQuoted(sb, "ERROR");
			appendEscapedAndQuoted(sb, errorMessage);
		} else {
			appendEscapedAndQuoted(sb, "SUCCESS");
			appendEscapedAndQuoted(sb, "");
		}
		appendEscapedAndQuoted(sb, type);
		appendEscapedAndQuoted(sb, uri);
		appendEscapedAndQuoted(sb, uriAlias);

		appendEscapedAndQuoted(sb, localAddress);
		appendEscapedAndQuoted(sb, layer);
		appendEscapedAndQuoted(sb, executionId);
		appendEscapedAndQuoted(sb, requestId);

		for (Object obj : extraArgs) {
			appendEscapedAndQuoted(sb, obj);
		}

		doLog(sb.toString());
	}

	protected void doLog(final String message) {
		logger.writeln(message);
	}

	/**
	 * Encloses the given value into double-quotes. Quote characters are escaped with an additional
	 * quote character. Line breaks are replace with a space character. Multiple line breaks are
	 * collapsed to a single space.
	 * 
	 * @param sb
	 *            the string buffer the escaped and quoted result is appended to
	 * @param value
	 *            the input value to transform
	 */
	protected void appendEscapedAndQuoted(final StringBuilder sb, final long value) {
		appendEscapedAndQuoted(sb, String.valueOf(value));
	}

	/**
	 * Encloses the given value into double-quotes. Quote characters are escaped with an additional
	 * quote character. Line breaks are replace with a space character. Multiple line breaks are
	 * collapsed to a single space.
	 * 
	 * @param sb
	 *            the string buffer the escaped and quoted result is appended to
	 * @param value
	 *            the input value to transform
	 */
	protected void appendEscapedAndQuoted(final StringBuilder sb, final Object value) {
		appendEscapedAndQuoted(sb, value == null ? null : String.valueOf(value));
	}

	/**
	 * <p>
	 * Encloses the given value into double-quotes. Quote characters are escaped with an additional
	 * quote character. Line breaks are replace with a space character. Multiple line breaks are
	 * collapsed to a single space.
	 * </p>
	 * <p>
	 * If the specified StringBuilder is non-empty, a semi-colon is appended first.
	 * </p>
	 * 
	 * @param sb
	 *            the string buffer the escaped and quoted result is appended to
	 * @param value
	 *            the input string to transform
	 */
	protected void appendEscapedAndQuoted(final StringBuilder sb, final String value) {
		boolean foundLineBreak = false;

		if (sb.length() > 0) {
			sb.append(';');
		}

		sb.append(CSV_QUOTE);
		if (value != null) {
			for (int i = 0, len = value.length(); i < len; ++i) {
				char c = value.charAt(i);
				switch (c) {
					case CSV_QUOTE:
						if (foundLineBreak) {
							foundLineBreak = false;
							sb.append(' ');
						}
						sb.append(c); // escape double quote, i. e. add quote character again
						break;
					case '\r':
					case '\n':
						foundLineBreak = true;
						continue;
					default:
						if (foundLineBreak) {
							sb.append(' ');
							foundLineBreak = false;
						}
						break;
				}
				sb.append(c);
			}
		}
		sb.append(CSV_QUOTE);
	}
}
