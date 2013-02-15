/*
 *  Copyright (c) mgm technology partners GmbH, Munich.
 *
 *  See the copyright.txt file distributed with this work for additional
 *  information regarding copyright ownership and intellectual property rights.
 */
package com.mgmtp.perfload.logging;

import java.util.UUID;

/**
 * Interface for logging test results.
 * 
 * @author rnaegele
 */
public interface ResultLogger {

	/**
	 * Logs a test result.
	 * 
	 * @param ti1
	 *            a time interval representing a time measurement
	 * @param ti2
	 *            a time interval representing a time measurement
	 * @param type
	 *            the type associated with this log message (e. g. the request type such as GET or
	 *            POST for HTTP requests)
	 * @param uri
	 *            the uri associated with this log message
	 * @param uriAlias
	 *            the uriAlias associated with this log message
	 * @param executionId
	 *            the execution id (all requests that are part of the same operation execution get
	 *            the same execution id)
	 * @param requestId
	 *            the request id (unique for each request)
	 * @param extraArgs
	 *            additional application-specific arguments to be logged
	 */
	void logResult(long timestamp, TimeInterval ti1, TimeInterval ti2, String type, String uri, String uriAlias,
			UUID executionId, UUID requestId, Object... extraArgs);

	/**
	 * Logs a test result.
	 * 
	 * @param errorMessage
	 *            the error message
	 * @param timestamp
	 *            timestamp before taking time measurements
	 * @param ti1
	 *            a time interval representing a time measurement
	 * @param ti2
	 *            a time interval representing a time measurement
	 * @param type
	 *            the type associated with this log message (e. g. the request type such as GET or
	 *            POST for HTTP requests)
	 * @param uri
	 *            the uri associated with this log message
	 * @param uriAlias
	 *            the uriAlias associated with this log message
	 * @param executionId
	 *            the execution id (all requests that are part of the same operation execution get
	 *            the same execution id)
	 * @param requestId
	 *            the request id (unique for each request)
	 * @param extraArgs
	 *            additional application-specific arguments to be logged
	 */
	void logResult(String errorMessage, long timestamp, TimeInterval ti1, TimeInterval ti2, String type, String uri,
			String uriAlias, UUID executionId, UUID requestId, Object... extraArgs);

}