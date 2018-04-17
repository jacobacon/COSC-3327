package dartboard;

import combinatorics.Permutation;
import combinatorics.PermutationImpl_Beneski;

import java.util.*;

public class DartboardUtils_Beneski
{
	public static Permutation<Integer> getPermutation(Dartboard dartboard){

		//TODO
		int value = dartboard.getTopmostNumber();

		Set<List<Integer>> setList = new HashSet<List<Integer>>();

		List<Integer> cycle = new ArrayList<>();

		for(int i = 0; i < dartboard.getNumbers().size(); i++){
			cycle.add(dartboard.getClockwiseAdjacentNumber(value));
			value = dartboard.getClockwiseAdjacentNumber(value);
		}

		setList.add(cycle);

		Permutation<Integer> p = new PermutationImpl_Beneski<Integer>(setList, dartboard.getNumbers());

		return p;
	}
	public static int getIntegerAtPosition(Dartboard dartboard, int position){

		int checkValue = dartboard.getTopmostNumber();

		for(int i = 1; i < position; i ++){
			checkValue = dartboard.getClockwiseAdjacentNumber(checkValue);
		}

		return checkValue;
	}
}
