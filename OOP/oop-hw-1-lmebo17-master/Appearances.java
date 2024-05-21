import java.util.*;

public class Appearances {
	
	/**
	 * Returns the number of elements that appear the same number
	 * of times in both collections. Static method. (see handout).
	 * @return number of same-appearance elements
	 */
	public static <T> int sameCount(Collection<T> a, Collection<T> b) {
		int result = 0;
		HashMap<T , Integer> mp1 = new HashMap<>();
		HashMap<T , Integer> mp2 = new HashMap<>();
		for (T curr : a) {
			if (mp1.containsKey(curr)) {
				mp1.put(curr, mp1.get(curr) + 1);
			} else {
				mp1.put(curr, 1);
			}
		}
		for(T curr : b){
			if (mp2.containsKey(curr)) {
				mp2.put(curr, mp2.get(curr) + 1);
			} else {
				mp2.put(curr, 1);
			}
		}
		for(T curr : mp1.keySet()){
			if(Objects.equals(mp1.get(curr), mp2.get(curr))) result++;
		}
		return result;
	}
	
}
