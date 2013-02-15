/*
 *  Copyright (c) mgm technology partners GmbH, Munich.
 *
 *  See the copyright.txt file distributed with this work for additional
 *  information regarding copyright ownership and intellectual property rights.
 */
package com.mgmtp.perfload.logging;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.Charset;

/**
 * A simple file logger.
 * 
 * @author rnaegele
 */
public class SimpleFileLogger implements SimpleLogger {

	private final File file;
	private PrintWriter writer;

	/**
	 * @param file
	 *            the log file
	 */
	public SimpleFileLogger(final File file) {
		this.file = file;
	}

	/**
	 * Opens an auto-flushing {@link PrintWriter} to the output file using UTF-8 encoding.
	 */
	@Override
	public void open() throws IOException {
		if (writer != null) {
			throw new IllegalStateException("already open");
		}
		writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(file), Charset.forName("UTF-8"))), true);
	}

	/**
	 * Writes the output to the internal {@link PrintWriter} using
	 * {@link PrintWriter#println(String)} and {@link PrintWriter#flush()}.
	 */
	@Override
	public void writeln(final String output) {
		if (writer == null) {
			try {
				open();
			} catch (IOException ex) {
				throw new RuntimeException(ex);
			}
		}
		writer.println(output);
	}

	/**
	 * Closes the internal {@link PrintWriter}.
	 */
	@Override
	public void close() {
		if (writer != null) {
			writer.close();
		}
	}
}
