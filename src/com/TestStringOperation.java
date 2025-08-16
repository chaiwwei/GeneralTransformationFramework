package com;

public class TestStringOperation {
	public static void main(String[] args) {
		testFunction(testFrom("ins(11,abc)", StringOperation.createInsOperation(11,"abc")), "testFrom('ins(11,abc)')");
		testFunction(testFrom("del(1,13)", StringOperation.createDelOperation(1,13)), "testFrom('del(1,13)')");
		testFunction(testFrom("del(11,13)", StringOperation.createDelOperation(11,13)), "testFrom('del(11,13)')");
		testFunction(testFrom("del(111,13)", StringOperation.createDelOperation(111,13)), "testFrom('del(111,13)')");

		
		testFunction(testToString(StringOperation.createDelOperation(111,13),"del(111,13)"), "toString()");
		testFunction(testToString(StringOperation.createDelOperation(11,13),"del(11,13)"), "toString()");
		testFunction(testToString(StringOperation.createDelOperation(1,13),"del(1,13)"), "toString()");
		testFunction(testToString(StringOperation.createInsOperation(11,"abc"),"ins(11,abc)"), "toString()");

	}

	private static void testFunction(boolean flag, String str) {
		if (flag) {
			System.out.println("Testing "+str+" successes!");
		} else {
			System.out.println("Testing "+str+" fails!");
		}
	}

	private static boolean testFrom(String opStr, StringOperation sop) {
		boolean result = true;

		StringOperation sopFrom = StringOperation.from(opStr);

		if (sopFrom.type == sop.type) {
			if (sopFrom.type == OpType.ins) {
				if (sopFrom.len != sop.len || !sopFrom.str.equals(sop.str)) {
					result = false;
				}
			}
		} else {
			result = false;
		}

		return result;
	}

	private static boolean testToString(StringOperation sop, String opStr) {
		boolean result = true;

		String sopStr = sop.toString();
		if (!sopStr.equals(opStr)) {
			result = false;
		}

		return result;
	}
}
