package com.kxindot.goblin.io;

import static com.kxindot.goblin.Throws.silentThrex;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author ZhaoQingJiang
 */
abstract class AbstractStreamIO<I extends InputStream, O extends OutputStream, Self extends IO>
extends AbstractIO<I, O, Self>{

	private byte[] buf;
	
	
	void tunnel() {
		int i = 0;
		buf = new byte[size];
		try {
			while ((i = in.read(buf)) != -1) {
				out.write(buf, 0, i);
			}
			IO.flush(out);
		} catch (IOException e) {
			close();
			silentThrex(e);
		}
	}
	
	@Override
	public void close() {
		super.close();
		buf = null;
	}
	
}
