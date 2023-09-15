package com.kxindot.goblin.io;

import static com.kxindot.goblin.Objects.requireNotNull;

import java.io.BufferedWriter;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;

/**
 * @author ZhaoQingJiang
 */
class IOReaderImpl extends AbstractCharIO<Reader, Writer, IOReader> implements IOReader {
	
	IOReaderImpl(Reader reader) {
		this.in = requireNotNull(reader);
	}

	@Override
	public IO write(Writer writer) {
		out = requireNotNull(writer);
		if (!BufferedWriter.class.isInstance(writer)) {
			out = new BufferedWriter(writer, size * 2);
		}
		tunnel();
		return this;
	}

	@Override
	public String read() {
		StringWriter writer = new StringWriter();
		write(writer);
		try {
			return writer.toString();
		} finally {
			close();
		}
	}
	
}
