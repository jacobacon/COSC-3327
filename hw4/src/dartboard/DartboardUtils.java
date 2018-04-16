package dartboard;

import combinatorics.Permutation;
import combinatorics.PermutationImpl;

public class DartboardUtils
{
	public static Permutation<Integer> getPermutation(Dartboard dartboard){

		Permutation<Integer> p = new PermutationImpl<Integer>(null, dartboard.getNumbers());

		return p;
	}
	public static int getIntegerAtPosition(Dartboard dartboard, int position){

		int checkValue = dartboard.getTopmostNumber();

		for(int i = 0; i < position; i ++){
			checkValue = dartboard.getClockwiseAdjacentNumber(checkValue);
		}

		return checkValue;
	}
}
