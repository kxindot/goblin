package com.kxindot.goblin.io;

import static com.kxindot.goblin.Objects.requireNotNull;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author ZhaoQingJiang
 */
class IOInputImpl extends AbstractStreamIO<InputStream, OutputStream, IOInput> implements IOInput {
	
	IOInputImpl(InputStream in) {
		this.in = requireNotNull(in);
	}
	
	@Override
	public IO write(OutputStream out) {
		this.out = requireNotNull(out);
		tunnel();
		return this;
	}
	
	@Override
	public String readString() {
		return new String(readBytes());
	}

	@Override
	public byte[] readBytes() {
		int available = IO.available(in);
		if (available <= 0) {
			available = size;
		}
		ByteArrayOutputStream out = new ByteArrayOutputStream(available);
		write(out);
		try {
			return out.toByteArray();
		} finally {
			close();
		}
	}
}