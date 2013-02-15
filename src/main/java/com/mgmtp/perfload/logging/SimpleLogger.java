/*
 *  Copyright (c) mgm technology partners GmbH, Munich.
 *
 *  See the copyright.txt file distributed with this work for additional
 *  information regarding copyright ownership and intellectual property rights.
 */
package com.mgmtp.perfload.logging;

import java.io.IOException;

/**
 * Interface for perfLoad's logger for measurings.
 * 
 * @author rnaegele
 */
public interface SimpleLogger {

	/**
	 * Opens the logger.
	 */
	void open() throws IOException;

	/**
	 * Writes the output to logger.
	 */
	void writeln(final String output);

	/**
	 * Closes logger.
	 */
	void close();
}