import junit.framework.TestCase;

import java.util.*;

public class AppearancesTest extends TestCase {
	// utility -- converts a string to a list with one
	// elem for each char.
	private List<String> stringToList(String s) {
		List<String> list = new ArrayList<String>();
		for (int i=0; i<s.length(); i++) {
			list.add(String.valueOf(s.charAt(i)));
			// note: String.valueOf() converts lots of things to string form
		}
		return list;
	}
	
	public void testSameCount1() {
		List<String> a = stringToList("abbccc");
		List<String> b = stringToList("cccbba");
		assertEquals(3, Appearances.sameCount(a, b));
	}
	
	public void testSameCount2() {
		// basic List<Integer> cases
		List<Integer> a = Arrays.asList(1, 2, 3, 1, 2, 3, 5);
		assertEquals(1, Appearances.sameCount(a, Arrays.asList(1, 9, 9, 1)));
		assertEquals(2, Appearances.sameCount(a, Arrays.asList(1, 3, 3, 1)));
		assertEquals(1, Appearances.sameCount(a, Arrays.asList(1, 3, 3, 1, 1)));
	}

	public void testSameCount3() {
		// basic List<Integer> cases
		List<Integer> a = Arrays.asList(0, 0, 0, 0, 0, 0);
		assertEquals(1, Appearances.sameCount(a, Arrays.asList(0, 0, 0, 0, 0, 0)));
		assertEquals(0, Appearances.sameCount(a, Arrays.asList(1, 1, 1, 1)));
		assertEquals(0, Appearances.sameCount(a, Arrays.asList(1, 0, 0, 0, 0)));
	}
	public void testSameCount4() {
		// basic List<Integer> cases
		List<String> a = stringToList("dabbcccd");
		List<String> b = stringToList("dwqqdasccccceeffbba");
		assertEquals(2, Appearances.sameCount(a, b));
	}

	public void testSameCount5() {
		// basic List<Integer> cases
		List<String> a = stringToList("");
		List<String> b = stringToList("dwqqdasccccceeffbba");
		assertEquals(0, Appearances.sameCount(a, b));
	}

	public void testSameCount6() {
		// basic List<Integer> cases
		List<Integer> a = Arrays.asList(1, 2, 3, 1, 2, 3, 5, 0 , 0);
		assertEquals(2, Appearances.sameCount(a, Arrays.asList(1, 9, 9, 1, 0, 0,9)));
		assertEquals(3, Appearances.sameCount(a, Arrays.asList(1, 3, 3, 1, 0, 0)));
		assertEquals(2, Appearances.sameCount(a, Arrays.asList(1, 3, 3, 1, 1, 5, 1)));
	}

}
