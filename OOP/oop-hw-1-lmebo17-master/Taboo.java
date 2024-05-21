
/*
 HW1 Taboo problem class.
 Taboo encapsulates some rules about what objects
 may not follow other objects.
 (See handout).
*/

import java.util.*;

public class Taboo<T> {

	List<T> rules;

	/**
	 * Constructs a new Taboo using the given rules (see handout.)
	 */
	public Taboo(List<T> rules) {
		this.rules = rules;
	}
	
	/**
	 * Returns the set of elements which should not follow
	 * the given element.
	 * @param elem
	 * @return elements which should not follow the given element
	 */
	public Set<T> noFollow(T elem) {
		HashSet<T> res = new HashSet<T>();
		for(int i = 0; i < rules.size() - 1; i++){
			if(rules.get(i) != null && rules.get(i).equals(elem)){
				if(rules.get(i+1) != null){
					res.add(rules.get(i + 1));
				}
			}
		}
		return res;
	}
	
	/**
	 * Removes elements from the given list that
	 * violate the rules (see handout).
	 * @param list collection to reduce
	 */

	// a a d c a d b c

	public void reduce(List<T> list) {
		for(int i = 0; i < list.size(); i++){
			Set<T> curr = noFollow(list.get(i));
			while(i < list.size() - 1 && curr.contains(list.get(i + 1))){
				list.remove(i + 1);
			}
		}
	}
}
