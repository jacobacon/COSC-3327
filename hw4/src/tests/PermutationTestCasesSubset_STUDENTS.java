package tests;

import combinatorics.Permutation;
import combinatorics.PermutationImpl;
import combinatorics.PermutationUtils;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;


public class PermutationTestCasesSubset_STUDENTS
{
	@Test(expected=AssertionError.class)
	public void assertionsEnabledTest()
	{
		//TEST_GOAL_MESSAGE = "Check whether assertions are enabled";
		assert false;
		throw new RuntimeException("ENABLE ASSERTIONS IN RUN CONFIGURATIONS!");
	}
	
	@Test
	public void domain()
	{
		Set<List<Integer>> cycles = new HashSet<List<Integer>>();
		List<Integer> cycle = Arrays.asList(new Integer[]{1,3,5});
		cycles.add(cycle);
		
		Set<Integer> allCycleElements = getAllElements(cycles);
		Set<Integer> domain = allCycleElements;
		domain.addAll(Arrays.asList(new Integer[]{2, 4}));
		
		Permutation<Integer> permutation = getPermutation(cycles, domain);
		Set<Integer> desiredDomain = new HashSet<Integer>(Arrays.asList(new Integer[]{1,2,3,4,5}));
		assertEquals(desiredDomain, permutation.getDomain());
	}
	
	@Test
	public void straightforwardPermutation()
	{
		Set<List<Integer>> cycles = new HashSet<List<Integer>>();
		List<Integer> soleCycle = new ArrayList<Integer>();
		final int MAX = 5;
		for(int i = 1; i <= MAX; i++)
		{
			soleCycle.add(i);
		}
		cycles.add(soleCycle);
		
		Set<Integer> domain = getAllElements(cycles);
		//System.out.println(cycles);
		//System.out.println(domain);
		Permutation<Integer> permutation = getPermutation(cycles, domain);
		
		for(int i = 1; i <= MAX; i++)
		{
			//System.out.println(new Integer(i % MAX + 1) + " " + permutation.getImage(i));
			assertEquals(new Integer(i % MAX + 1), permutation.getImage(i));
		}
	}
	
	@Test
	public void inversePermutation()
	{
		Set<List<Integer>> cycles = new HashSet<List<Integer>>();
		List<Integer> cycle = Arrays.asList(new Integer[]{3,5,7,9});
		cycles.add(cycle);
		cycle = Arrays.asList(new Integer[]{1,2,4,8});
		cycles.add(cycle);
		cycle = Arrays.asList(new Integer[]{6});
		cycles.add(cycle);

		Set<Integer> domain = getAllElements(cycles);
		Permutation<Integer> permutation = getPermutation(cycles, domain);
		
		Set<Integer> desiredDomain = new HashSet<Integer>(Arrays.asList(new Integer[]{1,2,3,4,5,6,7,8,9}));
		
		Permutation<Integer> inversePermutation = PermutationUtils.getInverse(permutation);
		
		assertEquals(new Integer(5), permutation.getImage(3));
		assertEquals(new Integer(7), permutation.getImage(5));
		assertEquals(new Integer(9), permutation.getImage(7));
		assertEquals(new Integer(3), permutation.getImage(9));
		assertEquals(new Integer(2), permutation.getImage(1));
		assertEquals(new Integer(4), permutation.getImage(2));
		assertEquals(new Integer(8), permutation.getImage(4));
		assertEquals(new Integer(1), permutation.getImage(8));
		assertEquals(new Integer(6), permutation.getImage(6));
		
		assertEquals(desiredDomain, inversePermutation.getDomain());
		
		assertEquals(new Integer(9), inversePermutation.getImage(3));
		assertEquals(new Integer(3), inversePermutation.getImage(5));
		assertEquals(new Integer(5), inversePermutation.getImage(7));
		assertEquals(new Integer(7), inversePermutation.getImage(9));
		assertEquals(new Integer(8), inversePermutation.getImage(1));
		assertEquals(new Integer(1), inversePermutation.getImage(2));
		assertEquals(new Integer(2), inversePermutation.getImage(4));
		assertEquals(new Integer(4), inversePermutation.getImage(8));
		assertEquals(new Integer(6), inversePermutation.getImage(6));

		for(Integer e : inversePermutation.getDomain())
		{
			assertEquals(e, inversePermutation.getImage(permutation.getImage(e)));
			assertEquals(e, permutation.getImage(inversePermutation.getImage(e)));
		}

	}


	@Test
	public void testGetCycle(){
		Set<List<Integer>> cycles = new HashSet<List<Integer>>();
		List<Integer> cycle = Arrays.asList(new Integer[]{3,5,7,9});
		cycles.add(cycle);
		cycle = Arrays.asList(new Integer[]{1,2,4,8});
		cycles.add(cycle);
		cycle = Arrays.asList(new Integer[]{6});
		cycles.add(cycle);

		Set<Integer> domain = getAllElements(cycles);
		Permutation<Integer> permutation = getPermutation(cycles, domain);

		List<Integer> desiredCycle = Arrays.asList(new Integer[]{3,5,7,9});

		assertTrue(PermutationUtils.getCycle(permutation, 3).containsAll(desiredCycle));

		desiredCycle = Arrays.asList(new Integer[]{6});

		assertTrue(PermutationUtils.getCycle(permutation, 6).containsAll(desiredCycle));


	}

	@Test
	public void testPreImage(){
		Set<List<Integer>> cycles = new HashSet<List<Integer>>();
		List<Integer> cycle = Arrays.asList(new Integer[]{3,5,7,9});
		cycles.add(cycle);
		cycle = Arrays.asList(new Integer[]{1,2,4,8});
		cycles.add(cycle);
		cycle = Arrays.asList(new Integer[]{6});
		cycles.add(cycle);

		Set<Integer> domain = getAllElements(cycles);
		Permutation<Integer> permutation = getPermutation(cycles, domain);

		assertTrue(permutation.getPreImage(5) == 3);

	}
	
	private static <E> Permutation<E> getPermutation(Set<List<E>> cycles, Set<E> domain)
	{
		return new PermutationImpl<>(cycles, domain);
	}
	
	private static <E> Set<E> getAllElements(Set<List<E>> setOfLists)
	{
		Set<E> allElements = new HashSet<E>();
		Iterator<List<E>> setOfListsIterator = setOfLists.iterator();
		while(setOfListsIterator.hasNext())
		{
			List<E> list = setOfListsIterator.next();
			allElements.addAll(list);
		}
		return allElements;
	}
}