package com.kxindot.goblin.io;

import java.io.Closeable;

/**
 * @author ZhaoQingJiang
 */
abstract class AbstractIO<I extends Closeable, O extends Closeable, Self extends IO> implements IO {

	protected I in;
	
	protected O out;
	
	protected int size = 256;
	
	
	@SuppressWarnings("unchecked")
	public Self buf(int size) {
		if (size > 0) {
			this.size = size;
		}
		return (Self) this;
	}
	
	
	@Override
	public void close() {
		IO.close(in);
		IO.close(out);
	}
	
}
