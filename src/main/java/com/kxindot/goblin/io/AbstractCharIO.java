package com.kxindot.goblin.io;

import static com.kxindot.goblin.Throws.silentThrex;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

/**
 * @author ZhaoQingJiang
 */
abstract class AbstractCharIO<I extends Reader, O extends Writer, Self extends IO> 
extends AbstractIO<I, O, Self> {

	private char[] buf;
	
	void tunnel() {
		int i = 0;
		buf = new char[size];
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
