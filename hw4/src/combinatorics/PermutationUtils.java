package combinatorics;

import java.util.*;

public class PermutationUtils
{
	public static <E> List<E> getCycle(Permutation<E> permutation, E e){
		assert permutation.getDomain().contains(e): "Element is not in the Domain of the Permutation";

		List<E> returnList = new ArrayList<>();

		E valueToFind = e;
		boolean complete = false;

		while(!complete){
			E value = permutation.getImage(valueToFind);
			if(!returnList.contains(value)){
				returnList.add(value);
				valueToFind = value;
			} else {
				complete = true;
			}

		}

		return returnList;
	}
	
	public static <E> Permutation<E> getInverse(Permutation<E> permutation){

		Set<E> domain = permutation.getDomain();

		Set<List<E>> cycles = new HashSet<>();

		//This is the messy way of doing it. It adds a lot of cycles to the permutation, but the internal map will take care of that.
		for (E value: domain) {
			List<E> tempCycle = getCycle(permutation, value);

			//System.out.println(tempCycle);

			Collections.reverse(tempCycle);

			//System.out.println(tempCycle);

			cycles.add(tempCycle);


		}
		Permutation<E> reversePermutation = new PermutationImpl<>(cycles, domain);

		return reversePermutation;
	}
	
	//part of pre: permutation2.getDomain().containsAll(permutation1.getDomain())
	//post: rv = permutation1*permutation2, that is, 
	//rv.getImage(e) = permutation1.getImage(permutation2.getImage(e)) for all e in permutation2.getDomain()
	public static <E> Permutation<E> compose(Permutation<E> permutation1, Permutation<E> permutation2){

		Set<E> domain = permutation2.getDomain();

		Map<E, E> map = new HashMap<>();

		for (E value : domain) {
			E perm2Image = permutation2.getImage(value);

			E item = permutation1.getImage(perm2Image);

			map.put(value, item);

		}
		//TODO
		return null;
	}
	
	//part of post: rv = "permutation.getImage(e) != e
	//				(for all e in permutation.getDomain())"
	public static <E> boolean isCyclic(Permutation<E> permutation){

		boolean cyclic = false;

		//TODO

		return cyclic;
	}
}