package com.kxindot.goblin.proxy;

import static com.kxindot.goblin.Classes.EMPTY_CLS_ARRAY;
import static com.kxindot.goblin.Objects.isNotEmpty;

import java.lang.reflect.Method;
import java.util.List;

import net.sf.cglib.proxy.NoOp;

/**
 * @author ZhaoQingJiang
 */
public class ProxyCallbackHelper extends CallbackHelper {
	
	public ProxyCallbackHelper(Class<?> superclass, Callback... callbacks) {
		this(superclass, EMPTY_CLS_ARRAY, callbacks);
	}

	public ProxyCallbackHelper(Class<?> superclass, Class<?>[] interfaces, Callback... callbacks) {
		super(superclass, interfaces, callbacks);
	}

	@Override
	protected Object getCallback(Method method) {
		Class<?> type = getSuperClass();
		List<Callback> callbacks = list;
		if (isNotEmpty(callbacks)) {
			for (Callback callback : callbacks) {
				if (callback.accept(type, method)) {
					return callback;
				}
			}
		}
		return NoOp.INSTANCE;
	}

}
