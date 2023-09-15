package com.kxindot.goblin.io;

import static com.kxindot.goblin.Objects.requireNotNull;
import static com.kxindot.goblin.Throws.silentThrex;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

/**
 * @author ZhaoQingJiang
 */
class IOWriterImpl extends AbstractCharIO<Reader, Writer, IOWriter> implements IOWriter {
	
	IOWriterImpl(Writer writer) {
		this.out = requireNotNull(writer);
	}

	@Override
	public IOWriter write(CharSequence content) {
		if (content.length() == 0) {
			return this;
		} else if (content.length() < size) {
			size = content.length();
		}
		if (!BufferedWriter.class.isInstance(out)) {
			out = new BufferedWriter(out, size * 2);
		}
		try {
			out.write(content.toString());
		} catch (IOException e) {
			close();
			silentThrex(e);
		}
		return null;
	}

	@Override
	public IOWriter write(byte[] content) {
		return write(new String(content));
	}

	@Override
	public IO read(Reader reader) {
		this.in = requireNotNull(reader);
		if (!BufferedWriter.class.isInstance(out)) {
			out = new BufferedWriter(out, size * 2);
		}
		tunnel();
		return this;
	}

}
