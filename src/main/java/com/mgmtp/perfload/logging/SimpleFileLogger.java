/*
 * Copyright (c) 2014 mgm technology partners GmbH
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
