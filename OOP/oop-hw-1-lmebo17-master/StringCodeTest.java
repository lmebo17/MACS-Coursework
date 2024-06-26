// StringCodeTest
// Some test code is provided for the early HW1 problems,
// and much is left for you to add.

import junit.framework.TestCase;

public class StringCodeTest extends TestCase {
	//
	// blowup
	//
	public void testBlowup1() {
		// basic cases
	//	System.out.println(StringCode.blowup("xx3abb"));
		assertEquals("xxaaaabb", StringCode.blowup("xx3abb"));
		assertEquals("xxxZZZZ", StringCode.blowup("2x3Z"));
	}
	
	public void testBlowup2() {
		// things with digits
		
		// digit at end
		assertEquals("axxx", StringCode.blowup("a2x3"));

		// digits next to each other
		assertEquals("a33111", StringCode.blowup("a231"));

		// try a 0
		assertEquals("aabb", StringCode.blowup("aa0bb"));
	}
	
	public void testBlowup3() {
		// weird chars, empty string
		assertEquals("AB&&,- ab", StringCode.blowup("AB&&,- ab"));
		assertEquals("", StringCode.blowup(""));

		// string with only digits
		assertEquals("", StringCode.blowup("2"));
		assertEquals("33", StringCode.blowup("23"));
	}
	
	
	//
	// maxRun
	//
	public void testRun1() {
		assertEquals(2, StringCode.maxRun("hoopla"));
		assertEquals(3, StringCode.maxRun("hoopllla"));
	}
	
	public void testRun2() {
		assertEquals(3, StringCode.maxRun("abbcccddbbbxx"));
		assertEquals(0, StringCode.maxRun(""));
		assertEquals(3, StringCode.maxRun("hhhooppoo"));
	}
	
	public void testRun3() {
		// "evolve" technique -- make a series of test cases
		// where each is change from the one above.
		assertEquals(1, StringCode.maxRun("123"));
		assertEquals(2, StringCode.maxRun("1223"));
		assertEquals(2, StringCode.maxRun("112233"));
		assertEquals(3, StringCode.maxRun("1112233"));
	}



	// stringIntersect


	public void testIntersection1(){
		assertEquals(true, StringCode.stringIntersect("avadadsadaso", "dwqdqassswqdq", 1));
		assertEquals(false, StringCode.stringIntersect("ddqwdqssdsafqw", "ddqmlmlmmknlk", 4));
		assertEquals(true, StringCode.stringIntersect("ddqwdqssdsafqw", "ddqmlmlmmknlk", 3));

		assertEquals(false, StringCode.stringIntersect("dqwdmalsds", "dwqdmlqwdqmkqd", 7));
		assertEquals(false, StringCode.stringIntersect("ff", "dqwdqdsawqw", 7));

		assertEquals(true, StringCode.stringIntersect("dowqdnqondoqkaaaaassbdnqwodqwodiq", "dqwndqwndaaaaassbdddwq", 8));
		assertEquals(true, StringCode.stringIntersect("dqwdqwfswqwq", "fffs", 2));

		assertEquals(true, StringCode.stringIntersect("abcdefg", "abcdefg", 1));
		assertEquals(true, StringCode.stringIntersect("abcdefg", "abcdefg", 2));
		assertEquals(true, StringCode.stringIntersect("abcdefg", "abcdefg", 3));
		assertEquals(true, StringCode.stringIntersect("abcdefg", "abcdefg", 4));
		assertEquals(true, StringCode.stringIntersect("abcdefg", "abcdefg", 5));
		assertEquals(true, StringCode.stringIntersect("abcdefg", "abcdefg", 6));
		assertEquals(true, StringCode.stringIntersect("abcdefg", "abcdefg", 7));

		assertEquals(false, StringCode.stringIntersect("bbbbbbbbb", "cccccccccc", 1));
		assertEquals(false, StringCode.stringIntersect("bbbbbbbbb", "cccccccccc", 2));
		assertEquals(false, StringCode.stringIntersect("bbbbbbbbb", "cccccccccc", 3));
		assertEquals(false, StringCode.stringIntersect("bbbbbbbbb", "cccccccccc", 4));
		assertEquals(false, StringCode.stringIntersect("bbbbbbbbb", "cccccccccc", 5));
		assertEquals(false, StringCode.stringIntersect("bbbbbbbbb", "cccccccccc", 6));
		assertEquals(false, StringCode.stringIntersect("bbbbbbbbb", "cccccccccc", 7));
		assertEquals(false, StringCode.stringIntersect("bbbbbbbbb", "cccccccccc", 8));
		assertEquals(false, StringCode.stringIntersect("bbbbbbbbb", "cccccccccc", 9));
		assertEquals(false, StringCode.stringIntersect("bbbbbbbbb", "cccccccccc", 10));


	}

	
}
