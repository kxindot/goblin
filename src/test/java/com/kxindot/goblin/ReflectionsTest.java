package com.kxindot.goblin;

import java.lang.invoke.SerializedLambda;

import com.kxindot.goblin.method.function.OneArgConsumer;

/**
 * @author ZhaoQingJiang
 */
public class ReflectionsTest {

	public static void main(String[] args) {
		OneArgConsumer<ReflectionsTest, String> ref = ReflectionsTest::testMethod;
		SerializedLambda lambda = Reflections.parseMethodReference(ref);
		System.out.println(lambda.getCapturingClass());
		System.out.println(lambda.getCapturedArgCount());
		System.out.println(lambda.getFunctionalInterfaceClass());
		System.out.println(lambda.getFunctionalInterfaceMethodName());
		System.out.println(lambda.getImplClass());
		System.out.println(lambda.getImplMethodSignature());
		System.out.println(lambda.getInstantiatedMethodType());
		System.out.println(lambda.getFunctionalInterfaceMethodSignature());
	}
	
	
	
	public void testMethod(String param) {
		
	}
	
}
