import java.util.HashSet;
import java.util.Set;

// CS108 HW1 -- String static methods

public class StringCode {

	/**
	 * Given a string, returns the length of the largest run.
	 * A a run is a series of adajcent chars that are the same.
	 * @param str
	 * @return max run length
	 */
	//aaa
	public static int maxRun(String str) {
		int result = 0;
		for(int i = 0; i < str.length(); i++){
			char ch = str.charAt(i);
			int lastIndex = i + 1;
			while(lastIndex < str.length() && ch == str.charAt(lastIndex)){
				lastIndex++;
			}
			result = Integer.max(result , lastIndex - i);
			i = lastIndex - 1;
		}
		return result;
	}

	
	/**
	 * Given a string, for each digit in the original string,
	 * replaces the digit with that many occurrences of the character
	 * following. So the string "a3tx2z" yields "attttxzzz".
	 * @param str
	 * @return blown up string
	 */

	// xx3abb
	public static String blowup(String str) {
		StringBuilder result = new StringBuilder();
		for(int i = 0; i < str.length(); i++) {
			if (Character.isDigit(str.charAt(i))) {
				for(int j = 0; i != str.length() - 1 && j < str.charAt(i) - '0'; j++){
					result.append(str.charAt(i + 1));
				}
			} else result.append(str.charAt(i));
		}
		return result.toString();
	}
	
	/**
	 * Given 2 strings, consider all the substrings within them
	 * of length len. Returns true if there are any such substrings
	 * which appear in both strings.
	 * Compute this in linear time using a HashSet. Len will be 1 or more.
	 */

	// qpn nqmpn
	public static boolean stringIntersect(String a, String b, int len) {
		HashSet<String> st = new HashSet<>();
		for(int i = 0; i <= a.length() - len; i++){
			st.add(a.substring(i , i + len));
		}
		for(int i = 0; i <= b.length() - len; i++){
			if(st.contains(b.substring(i , i + len))) return true;
		}
		return false;
	}
}
