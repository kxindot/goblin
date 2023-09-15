package com.kxindot.goblin.io;

import java.io.InputStream;
import java.io.PrintStream;

/**
 * @author ZhaoQingJiang
 */
class IOPrintOutputImpl extends IOOuputImpl {

	IOPrintOutputImpl(PrintStream out) {
		super(out);
	}
	
	@Override
	public IOOutput write(CharSequence content) {
		out().print(content);
		return this;
	}
	
	
	@Override
	public IOOutput write(byte[] content) {
		out().print(content);
		return this;
	}
	
	
	@Override
	public IO read(InputStream in) {
		String txt = IO.input(in).readString();
		out().print(txt);
		return this;
	}
	
	@Override
	public void close() {
		//do nothing
	}
	
	
	PrintStream out() {
		return PrintStream.class.cast(out);
	}

}
