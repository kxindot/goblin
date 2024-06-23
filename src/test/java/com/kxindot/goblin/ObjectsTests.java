package com.kxindot.goblin;

import static com.kxindot.goblin.Objects.capitalize;
import static com.kxindot.goblin.Objects.decapitalize;

import org.junit.jupiter.api.Test;

import com.kxindot.goblin.test.JunitTests;

/**
 * @author ZhaoQingJiang
 */
class ObjectsTests extends JunitTests {

	@Test
	void testIsEqual() {
		
	}

	@Test
	void testIsNull() {

	}

	@Test
	void testIsAnyNull() {
		
	}

	@Test
	void testIsAllNull() {
		
	}

	@Test
	void testIsNotNull() {
		
	}

	@Test
	void testIsAnyNotNull() {
		
	}

	@Test
	void testIsAllNotNull() {
		
	}

	@Test
	void testDefaultIfNull() {
		
	}

	@Test
	void testConvert() {
		
	}

	@Test
	void testIsEmptyCharSequence() {
		
	}

	@Test
	void testIsBlank() {
		
	}

	@Test
	void testIsAnyBlank() {
		
	}

	@Test
	void testIsAllBlank() {
		
	}

	@Test
	void testIsNotBlank() {
		
	}

	@Test
	void testIsAnyNotBlank() {
		
	}

	@Test
	void testIsAllNotBlank() {
		
	}

	@Test
	void testIsNumeric() {
		
	}

	@Test
	void testDefaultIfEmptyTT() {
		
	}

	@Test
	void testDefaultIfBlank() {
		
	}

	@Test
	void testCapitalize() {
		caseBegin("capitalize(null)");
		println("结果: {}", capitalize(null));
		caseEnd();
		caseBegin("capitalize(\"\")");
		println("结果: {}", capitalize(""));
		caseEnd();
		caseBegin("capitalize(\"   \")");
		println("结果: {}", capitalize("   "));
		caseEnd();
		caseBegin("capitalize(\"fooBar\")");
		println("结果: {}", capitalize("fooBar"));
		caseEnd();
		caseBegin("capitalize(\"FooBar\")");
		println("结果: {}", capitalize("FooBar"));
		caseEnd();
		caseBegin("capitalize(\"X\")");
		println("结果: {}", capitalize("X"));
		caseEnd();
		caseBegin("capitalize(\"x\")");
		println("结果: {}", capitalize("x"));
		caseEnd();
		caseBegin("capitalize(\"URl\")");
		println("结果: {}", capitalize("URl"));
		caseEnd();
		caseBegin("capitalize(\"URLParser\")");
		println("结果: {}", capitalize("URlParser"));
		caseEnd();
	}

	@Test
	void testDecapitalize() {
		caseBegin("decapitalize(null)");
		println("结果: {}", decapitalize(null));
		caseEnd();
		caseBegin("decapitalize(\"\")");
		println("结果: {}", decapitalize(""));
		caseEnd();
		caseBegin("decapitalize(\"   \")");
		println("结果: {}", decapitalize("   "));
		caseEnd();
		caseBegin("decapitalize(\"fooBar\")");
		println("结果: {}", decapitalize("fooBar"));
		caseEnd();
		caseBegin("decapitalize(\"FooBar\")");
		println("结果: {}", decapitalize("FooBar"));
		caseEnd();
		caseBegin("decapitalize(\"X\")");
		println("结果: {}", decapitalize("X"));
		caseEnd();
		caseBegin("decapitalize(\"x\")");
		println("结果: {}", decapitalize("x"));
		caseEnd();
		caseBegin("decapitalize(\"URL\")");
		println("结果: {}", decapitalize("URL"));
		caseEnd();
		caseBegin("decapitalize(\"URLParser\")");
		println("结果: {}", decapitalize("URLParser"));
		caseEnd();
	}

	@Test
	void testCountMatch() {
		
	}

	@Test
	void testStringFormat() {
		
	}

	@Test
	void testStringJoinCharSequenceArray() {
		
	}

	@Test
	void testStringJoinObjectArray() {
		
	}

	@Test
	void testStringJoinIterableOfQ() {
		
	}

	@Test
	void testStringJoinWithCharSequenceCharSequenceArray() {
		
	}

	@Test
	void testStringJoinWithCharSequenceObjectArray() {
		
	}

	@Test
	void testStringJoinWithCharSequenceIterableOfQ() {
		
	}

	@Test
	void testStringReplace() {
		
	}

	@Test
	void testStringReplaceFirstStringStringString() {
		
	}

	@Test
	void testStringReplaceFirstStringStringStringInt() {
		
	}

	@Test
	void testStringRemove() {
		
	}

	@Test
	void testStringRepeat() {
		
	}

	@Test
	void testSubstringCharSequenceInt() {
		
	}

	@Test
	void testSubstringCharSequenceIntInt() {
		
	}

	@Test
	void testSubstringLeft() {
		
	}

	@Test
	void testSubstringRight() {
		
	}

	@Test
	void testSubstringBefore() {
		
	}

	@Test
	void testSubstringAfter() {
		
	}

	@Test
	void testSubstringBeforeLast() {
		
	}

	@Test
	void testSubstringAfterLast() {
		
	}

	@Test
	void testSubstringBetween() {
		
	}

	@Test
	void testToStringThrowable() {
		
	}

	@Test
	void testAssertsT() {
		
	}

	@Test
	void testAssertsT1() {
		
	}

	@Test
	void testAssertsT2() {
		
	}

	@Test
	void testAssertsT3() {
		
	}

	@Test
	void testRequireNullT() {
		
	}

	@Test
	void testRequireNullTSupplierOfE() {
		
	}

	@Test
	void testRequireNullTString() {
		
	}

	@Test
	void testRequireNullTStringObjectArray() {
		
	}

	@Test
	void testRequireNullTFunctionOfStringEString() {
		
	}

	@Test
	void testRequireNullTFunctionOfStringEStringObjectArray() {
		
	}

	@Test
	void testRequireNotNullT() {
		
	}

	@Test
	void testRequireNotNullTSupplierOfE() {
		
	}

	@Test
	void testRequireNotNullTString() {
		
	}

	@Test
	void testRequireNotNullTStringObjectArray() {
		
	}

	@Test
	void testRequireNotNullTFunctionOfStringEString() {
		
	}

	@Test
	void testRequireNotNullTFunctionOfStringEStringObjectArray() {
		
	}

	@Test
	void testRequireNotEmptyT() {
		
	}

	@Test
	void testRequireNotEmptyTSupplierOfE() {
		
	}

	@Test
	void testRequireNotEmptyTString() {
		
	}

	@Test
	void testRequireNotEmptyTStringObjectArray() {
		
	}

	@Test
	void testRequireNotEmptyTFunctionOfStringEString() {
		
	}

	@Test
	void testRequireNotEmptyTFunctionOfStringEStringObjectArray() {
		
	}

	@Test
	void testRequireNotBlankT() {
		
	}

	@Test
	void testRequireNotBlankTSupplierOfE() {
		
	}

	@Test
	void testRequireNotBlankTString() {
		
	}

	@Test
	void testRequireNotBlankTStringObjectArray() {
		
	}

	@Test
	void testRequireNotBlankTFunctionOfStringEString() {
		
	}

	@Test
	void testRequireNotBlankTFunctionOfStringEStringObjectArray() {
		
	}

	@Test
	void testRequireNotEmptyTArray() {
		
	}

	@Test
	void testRequireNotEmptyTArraySupplierOfE() {
		
	}

	@Test
	void testRequireNotEmptyTArrayString() {
		
	}

	@Test
	void testRequireNotEmptyTArrayStringObjectArray() {
		
	}

	@Test
	void testRequireNotEmptyTArrayFunctionOfStringEString() {
		
	}

	@Test
	void testRequireNotEmptyTArrayFunctionOfStringEStringObjectArray() {
		
	}

	@Test
	void testRequireNotEmptyC() {
		
	}

	@Test
	void testRequireNotEmptyCSupplierOfE() {
		
	}

	@Test
	void testRequireNotEmptyCString() {
		
	}

	@Test
	void testRequireNotEmptyCStringObjectArray() {
		
	}

	@Test
	void testRequireNotEmptyCFunctionOfStringEString() {
		
	}

	@Test
	void testRequireNotEmptyCFunctionOfStringEStringObjectArray() {
		
	}

	@Test
	void testRequireNotEmptyM() {
		
	}

	@Test
	void testRequireNotEmptyMSupplierOfE() {
		
	}

	@Test
	void testRequireNotEmptyMString() {
		
	}

	@Test
	void testRequireNotEmptyMStringObjectArray() {
		
	}

	@Test
	void testRequireNotEmptyMFunctionOfStringEString() {
		
	}

	@Test
	void testRequireNotEmptyMFunctionOfStringEStringObjectArray() {
		
	}

	@Test
	void testRequireTrueBoolean() {
		
	}

	@Test
	void testRequireTrueBooleanSupplierOfE() {
		
	}

	@Test
	void testRequireTrueBooleanString() {
		
	}

	@Test
	void testRequireTrueBooleanStringObjectArray() {
		
	}

	@Test
	void testRequireTrueBooleanFunctionOfStringEString() {
		
	}

	@Test
	void testRequireTrueBooleanFunctionOfStringEStringObjectArray() {
		
	}

	@Test
	void testRequireFalse() {
		
	}

	@Test
	void testPrimitiveToWrapper() {
		
	}

	@Test
	void testWrapperToPrimitive() {
		
	}

	@Test
	void testFindClass() {
		
	}

	@Test
	void testIsEmptyByteArray() {
		
	}

	@Test
	void testIsEmptyShortArray() {
		
	}

	@Test
	void testIsEmptyIntArray() {
		
	}

	@Test
	void testIsEmptyLongArray() {
		
	}

	@Test
	void testIsEmptyFloatArray() {
		
	}

	@Test
	void testIsEmptyDoubleArray() {
		
	}

	@Test
	void testIsEmptyTArray() {
		
	}

	@Test
	void testIsNotEmptyTArray() {
		
	}

	@Test
	void testContainsCharArrayChar() {
		
	}

	@Test
	void testContainsAArrayA() {
		
	}

	@Test
	void testContainsAArrayAComparatorOfA() {
		
	}

	@Test
	void testContainsAArrayBComparaterOfAB() {
		
	}

	@Test
	void testDefaultIfEmptyByteArrayByteArray() {
		
	}

	@Test
	void testDefaultIfEmptyShortArrayShortArray() {
		
	}

	@Test
	void testDefaultIfEmptyIntArrayIntArray() {
		
	}

	@Test
	void testDefaultIfEmptyLongArrayLongArray() {
		
	}

	@Test
	void testDefaultIfEmptyFloatArrayFloatArray() {
		
	}

	@Test
	void testDefaultIfEmptyDoubleArrayDoubleArray() {
		
	}

	@Test
	void testDefaultIfEmptyTArrayTArray() {
		
	}

	@Test
	void testAsList() {
		
	}

	@Test
	void testToListByteArray() {
		
	}

	@Test
	void testToListCharArray() {
		
	}

	@Test
	void testToListShortArray() {
		
	}

	@Test
	void testToListIntArray() {
		
	}

	@Test
	void testToListLongArray() {
		
	}

	@Test
	void testToListFloatArray() {
		
	}

	@Test
	void testToListDoubleArray() {
		
	}

	@Test
	void testToListTArray() {
		
	}

	@Test
	void testToSetCharArray() {
		
	}

	@Test
	void testToSetShortArray() {
		
	}

	@Test
	void testToSetIntArray() {
		
	}

	@Test
	void testToSetLongArray() {
		
	}

	@Test
	void testToSetFloatArray() {
		
	}

	@Test
	void testToSetDoubleArray() {
		
	}

	@Test
	void testToSetTArray() {
		
	}

	@Test
	void testCopyOfAArrayClassOfQextendsBFunctionOfAB() {
		
	}

	@Test
	void testCopyOfAArrayClassOfQextendsB() {
		
	}

	@Test
	void testCopyOfAArrayClassOfAClassOfQextendsB() {
		
	}

	@Test
	void testIsEmptyCollectionOfQ() {
		
	}

	@Test
	void testIsNotEmptyCollectionOfQ() {
		
	}

	@Test
	void testNewArrayList() {
		
	}

	@Test
	void testNewArrayListInt() {
		
	}

	@Test
	void testNewArrayListTArray() {
		
	}

	@Test
	void testNewArrayListIterableOfT() {
		
	}

	@Test
	void testNewArrayListCollectionOfT() {
		
	}

	@Test
	void testNewCopyOnWriteArrayList() {
		
	}

	@Test
	void testNewCopyOnWriteArrayListTArray() {
		
	}

	@Test
	void testNewCopyOnWriteArrayListCollectionOfT() {
		
	}

	@Test
	void testNewLinkedList() {
		
	}

	@Test
	void testNewLinkedListTArray() {
		
	}

	@Test
	void testNewLinkedListCollectionOfT() {
		
	}

	@Test
	void testNewHashSet() {
		
	}

	@Test
	void testNewHashSetInt() {
		
	}

	@Test
	void testNewHashSetTArray() {
		
	}

	@Test
	void testNewHashSetCollectionOfT() {
		
	}

	@Test
	void testNewLinkedHashSet() {
		
	}

	@Test
	void testNewLinkedHashSetInt() {
		
	}

	@Test
	void testNewLinkedHashSetTArray() {
		
	}

	@Test
	void testNewLinkedHashSetCollectionOfT() {
		
	}

	@Test
	void testNewArrayBlockingQueue() {
		
	}

	@Test
	void testNewStack() {
		
	}

	@Test
	void testUnmodifiableEmptyList() {
		
	}

	@Test
	void testUnmodifiableEmptySet() {
		
	}

	@Test
	void testNewUnmodifiableListTArray() {
		
	}

	@Test
	void testNewUnmodifiableSetTArray() {
		
	}

	@Test
	void testNewUnmodifiableListListOfT() {
		
	}

	@Test
	void testNewUnmodifiableSetSetOfT() {
		
	}

	@Test
	void testNewUnmodifiableListCollectionOfT() {
		
	}

	@Test
	void testNewUnmodifiableSetCollectionOfT() {
		
	}

	@Test
	void testToArray() {
		
	}

	@Test
	void testIsEmptyMapOfQQ() {
		
	}

	@Test
	void testIsNotEmptyMapOfQQ() {
		
	}

	@Test
	void testNewHashMap() {
		
	}

	@Test
	void testNewHashMapInt() {
		
	}

	@Test
	void testNewHashMapMapOfQextendsKQextendsV() {
		
	}

	@Test
	void testNewCaseInsensitiveHashMap() {
		
	}

	@Test
	void testNewTreeMap() {
		
	}

	@Test
	void testNewTreeMapMapOfQextendsKQextendsV() {
		
	}

	@Test
	void testNewTreeMapComparatorOfQsuperK() {
		
	}

	@Test
	void testNewLinkedHashMap() {
		
	}

	@Test
	void testNewLinkedHashMapInt() {
		
	}

	@Test
	void testNewLinkedHashMapMapOfQextendsKQextendsV() {
		
	}

	@Test
	void testNewConcurrentHashMap() {
		
	}

	@Test
	void testNewCaseInsensitiveLinkedHashMap() {
		
	}

	@Test
	void testNewCaseInsensitiveConcurrentHashMap() {
		
	}

	@Test
	void testNewUnmodifiedMap() {
		
	}

	@Test
	void testIsNullKey() {
		
	}

	@Test
	void testIsNullValue() {
		
	}

	@Test
	void testNewPair() {
		
	}

	@Test
	void testNewPairKV() {
		
	}

	@Test
	void testNewPairPairOfKV() {
		
	}

	@Test
	void testNewUnmodifiablePairKV() {
		
	}

	@Test
	void testNewUnmodifiablePairPairOfKV() {
		
	}

	@Test
	void testDoIfNotNullTConsumerOfT() {
		
	}

	@Test
	void testDoIfNotNullTFunctionOfTR() {
		
	}

	@Test
	void testDoIfNotEmptyTConsumerOfT() {
		
	}

	@Test
	void testDoIfNotEmptyTFunctionOfTR() {
		
	}

	@Test
	void testDoIfNotBlankTConsumerOfT() {
		
	}

	@Test
	void testDoIfNotBlankTFunctionOfTR() {
		
	}

	@Test
	void testConsumer() {
		
	}

	@Test
	void testFunction() {
		
	}

}
