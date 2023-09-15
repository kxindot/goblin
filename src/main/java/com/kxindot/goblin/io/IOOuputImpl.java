package com.kxindot.goblin.io;

import static com.kxindot.goblin.Objects.requireNotNull;
import static com.kxindot.goblin.Throws.silentThrex;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author ZhaoQingJiang
 */
class IOOuputImpl extends AbstractStreamIO<InputStream, OutputStream, IOOutput> implements IOOutput {
	
	IOOuputImpl(OutputStream out) {
		this.out = requireNotNull(out);
	}
	
	@Override
	public IOOutput write(CharSequence content) {
		if (content.length() == 0) {
			return this;
		}
		return write(content.toString().getBytes());
	}

	@Override
	public IOOutput write(byte[] content) {
		if (content.length < size) {
			size = content.length;
		}
		if (!(out instanceof BufferedOutputStream)) {
			out = new BufferedOutputStream(out, size * 2);
		}
		try {
			out.write(content);
		} catch (IOException e) {
			close();
			silentThrex(e);
		}
		return this;
	}

	@Override
	public IO read(InputStream in) {
		this.in = requireNotNull(in);
		if (!(out instanceof BufferedOutputStream)) {
			out = new BufferedOutputStream(out, size * 2);
		}
		tunnel();
		return this;
	}

}
