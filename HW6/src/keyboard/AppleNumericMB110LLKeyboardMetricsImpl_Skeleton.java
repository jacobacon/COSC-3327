package keyboard;

import static keyboard.Key.*;
import static keyboard.KeyLayout.COLEMAK;
import static keyboard.KeyLayout.DVORAK;
import static keyboard.KeyLayout.QWERTY;
import static keyboard.KeyLayout.ROTATION_13;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import combinatorics.Permutation;
import combinatorics.PermutationImpl_Beneski;
import org.omg.Messaging.SYNC_WITH_TRANSPORT;

/**
 * @author skeleton
 *
 */
public class AppleNumericMB110LLKeyboardMetricsImpl_Skeleton implements KeyboardMetrics {
	private List<Key> vertexLabels;
	private int[][] adjacencyMatrix;
	private int[][] distanceMatrix;
	private Key homeKey;
	
	private static Map<KeyLayout, Key> keyLayoutToHomeKeyMap;
	private static Map<KeyLayout, Map<Key, Set<Key>>> keyLayoutToKeyToNeighborMapMap;
	
	static
	{
		keyLayoutToHomeKeyMap = new HashMap<KeyLayout, Key>();
		keyLayoutToHomeKeyMap.put(QWERTY, J);
		keyLayoutToHomeKeyMap.put(DVORAK, H);
		keyLayoutToHomeKeyMap.put(COLEMAK, N);
		keyLayoutToHomeKeyMap.put(ROTATION_13, W);
		
		keyLayoutToKeyToNeighborMapMap = new HashMap<KeyLayout, Map<Key, Set<Key>>>();
		Map<Key, Set<Key>> keyToNeighborMap_QWERTY = getKeyToNeighborMap_QWERTY();
		Map<Key, Set<Key>> keyToNeighborMap_DVORAK = applyPermutationToMap(keyToNeighborMap_QWERTY, getQWERTYToDvorakPermutation());
		Map<Key, Set<Key>> keyToNeighborMap_COLEMAK = applyPermutationToMap(keyToNeighborMap_QWERTY, getQWERTYToColemakPermutation());
		Map<Key, Set<Key>> keyToNeighborMap_ROT_13 = applyPermutationToMap(keyToNeighborMap_QWERTY, getQWERTYToRotation13Permutation());
		keyLayoutToKeyToNeighborMapMap.put(QWERTY, keyToNeighborMap_QWERTY);
		keyLayoutToKeyToNeighborMapMap.put(DVORAK, keyToNeighborMap_DVORAK);
		keyLayoutToKeyToNeighborMapMap.put(COLEMAK, keyToNeighborMap_COLEMAK);
		keyLayoutToKeyToNeighborMapMap.put(ROTATION_13, keyToNeighborMap_ROT_13);
	}
	
	public AppleNumericMB110LLKeyboardMetricsImpl_Skeleton(KeyLayout keyLayout)
	{
		this.homeKey = keyLayoutToHomeKeyMap.get(keyLayout);
		Map<Key, Set<Key>> keyToNeighborsMap = keyLayoutToKeyToNeighborMapMap.get(keyLayout);

		System.out.println(keyToNeighborsMap);
		init(keyToNeighborsMap, new ArrayList<Key>(keyToNeighborsMap.keySet()));
	}
	
	public void init(Map<Key, Set<Key>> physicalKeyToNeighborsMap, List<Key> vertexLabels)
	{
		this.vertexLabels = vertexLabels;
		this.adjacencyMatrix = getAdjacencyMatrix(physicalKeyToNeighborsMap, vertexLabels);
		//this.distanceMatrix = getDistanceMatrix(adjacencyMatrix);
	}
	
	private static int[][] getAdjacencyMatrix(Map<Key, Set<Key>> physicalKeyToNeighborsMap, List<Key> vertexLabels)
	{
		assert physicalKeyToNeighborsMap.keySet().equals(new HashSet<Key>(vertexLabels)) : "vertexLabels inconsistent with physicalKeyToNeighborsMap! : vertexLabels = " + vertexLabels + " physicalKeyToNeighborsMap.keySet() = " + physicalKeyToNeighborsMap.keySet();
		final int SIZE = physicalKeyToNeighborsMap.keySet().size();
		int[][] adjacencyMatrix = new int[SIZE][SIZE];

		int numCharacters = vertexLabels.size();

		System.out.println(vertexLabels + " " + numCharacters);

		for(int i = 0; i < numCharacters; i++){
			for(int j = 0; j < numCharacters; j++){
				Set<Key> neighborSet = physicalKeyToNeighborsMap.get(vertexLabels.get(i));

				if(neighborSet.contains(vertexLabels.get(j))){
					adjacencyMatrix[i][j] = 1;
				} else {
					adjacencyMatrix[i][j] = 0;
				}
			}
		}
		
		//build adjacencyMatrix here...

		/*
		for(int i = 0; i < numCharacters; i++){
			for(int j = 0; j < numCharacters; j++){
				System.out.print(adjacencyMatrix[i][j]);
			}
			System.out.println("\n");
		}
		*/
		
		return adjacencyMatrix;
	}
	
	//Matrix multiplication
	private static int[][] multiply(int[][] A, int[][] B)
	{
		int rowCount_A = A.length;
		assert rowCount_A > 0 : "rowCount_A = 0!";
		int columnCount_A = A[0].length;
		int rowCount_B = B.length;
		assert rowCount_B > 0 : "rowCount_B = 0!";
		int columnCount_B = B[0].length;
		assert columnCount_A == rowCount_B : "columnCount_A = " + columnCount_A + " <> " + rowCount_B + " = rowCount_B!";
		
		int[][] C = new int[rowCount_A][columnCount_B];
        for (int i = 0; i < rowCount_A; i++)
            for (int j = 0; j < columnCount_B; j++)
                for (int k = 0; k < columnCount_A; k++)
                    C[i][j] += A[i][k] * B[k][j];
        
        return C;
	}
	
	private static int[][] getDistanceMatrix(int[][] adjacencyMatrix)
	{
		int vertexCount = adjacencyMatrix.length;
		assert vertexCount > 0 : "rowCount = 0!";
		
		int[][] distanceMatrix = new int[vertexCount][vertexCount];
		
		//Figure out distanceMatrix here...
		
		return distanceMatrix;
	}
	
	/* (non-Javadoc)
	 * @see keyboard.KeyboardMeasurements#getDistance(keyboard.PhysicalKey, keyboard.PhysicalKey)
	 */
	@Override
	public double getDistance(Key key1, Key key2) {
		int index1 = getIndex(vertexLabels, key1);
		int index2 = getIndex(vertexLabels, key2);
		return distanceMatrix[index1][index2];
	}

	private static <E> int getIndex(List<E> list, E element)
	{
		boolean foundIndex = false;
		int i = 0;
		while(!foundIndex && i < list.size())
		{
			foundIndex = (list.get(i) == element);
			if(!foundIndex) i++;
		}
		int rv = -1;
		if(foundIndex) rv = i;
		return rv;
	}

	@Override
	public double getDistance(String str) {
		double distance = 0;
		Key currentKey = homeKey;
		
		//Calculate distance here
		
		return distance;
	}
	
	private Key getClosestKey(Set<Key> keySet, Key key)
	{
		double minDistance = 0.0;
		List<Key> keyList = new ArrayList<Key>(keySet);
		Key minDistanceKey = null;
		
		//DO SOMETHING HERE...
		//getDistance() is involved...
		
		return minDistanceKey;
	}

	private static Set<Key> getKeySet(char character)
	{
		List<Key> keyList = Arrays.asList(Key.values());
		Set<Key> characterProducingKeysSet = new HashSet<Key>();
		for(int i = 0; i < keyList.size(); i++)
		{
			Key key = keyList.get(i);
			assert key != null : "key is null!";
			boolean keyProducesCharacter = (key.getNormalCharacter() != null && key.getNormalCharacter() == character) || (key.getShiftModifiedCharacter() != null && key.getShiftModifiedCharacter() == character);
			if(keyProducesCharacter) characterProducingKeysSet.add(key);
		}
		return characterProducingKeysSet;
	}
	
	private static Map<Key, Set<Key>> getKeyToNeighborMap_QWERTY()
	{
		Map<Key, Set<Key>> keyToNeighborSetMap = new HashMap<Key, Set<Key>>();
		
		//Produce keyToNeighborSetMap here
		//You might want to take a look at getSet()


		keyToNeighborSetMap.put(A, getSet(Q, W, S, Z, SHIFT_1));
		keyToNeighborSetMap.put(B, getSet(SPACEBAR_3,N,G,V,H));
		keyToNeighborSetMap.put(C,getSet(SPACEBAR_1, X, V, F, D));
		keyToNeighborSetMap.put(D,getSet(C, X, S,F,E,R));
		keyToNeighborSetMap.put(E,getSet(S,R,D,W,THREE,FOUR));
		keyToNeighborSetMap.put(F,getSet(V,G,R,T,D,C));
		keyToNeighborSetMap.put(G,getSet(V,H,Y,T,B,F));
		keyToNeighborSetMap.put(H,getSet(N,J,U,Y,B,G));
		keyToNeighborSetMap.put(I,getSet(J,U,EIGHT,NINE,K,O));
		keyToNeighborSetMap.put(J,getSet(M,N,K,U,I,H));
		keyToNeighborSetMap.put(K,getSet(COMMA,M,I,L,J,O));
		keyToNeighborSetMap.put(L,getSet(SEMICOLON,PERIOD,COMMA,P,O, K));
		keyToNeighborSetMap.put(M,getSet(SPACEBAR_5,N,COMMA,K,J));
		keyToNeighborSetMap.put(N,getSet(SPACEBAR_4,M,J,H,B));
		keyToNeighborSetMap.put(O,getSet(K,I,P,ZERO,NINE,L));
		keyToNeighborSetMap.put(P,getSet(O,ZERO,MINUS,LEFT_BRACKET,SEMICOLON,L));
		keyToNeighborSetMap.put(Q,getSet(W,TAB,ONE,TWO,A));
		keyToNeighborSetMap.put(R,getSet(FOUR,FIVE,T,F,E,D));
		keyToNeighborSetMap.put(S,getSet(Z,X,W,A,E,D));
		keyToNeighborSetMap.put(T,getSet(FIVE,SIX,Y,G,R,F));
		keyToNeighborSetMap.put(U,getSet(SEVEN,EIGHT,I,Y,H,J));
		keyToNeighborSetMap.put(V,getSet(SPACEBAR_2,B,G,C,F));
		keyToNeighborSetMap.put(W,getSet(TWO,THREE,E,Q,S,A));
		keyToNeighborSetMap.put(X,getSet(Z,C,S,D));
		keyToNeighborSetMap.put(Y,getSet(SIX,SEVEN,U,H,G,T));
		keyToNeighborSetMap.put(Z,getSet(SHIFT_1,X,A,S));
		keyToNeighborSetMap.put(ZERO,getSet(MINUS,NINE,O,P));
		keyToNeighborSetMap.put(ONE,getSet(TAB,BACKTICK,TWO,Q));
		keyToNeighborSetMap.put(TWO,getSet(ONE,THREE,Q,W));
		keyToNeighborSetMap.put(THREE,getSet(FOUR,TWO,W,E));
		keyToNeighborSetMap.put(FOUR,getSet(FIVE,THREE,E,R));
		keyToNeighborSetMap.put(FIVE,getSet(SIX,FOUR,R,T));
		keyToNeighborSetMap.put(SIX,getSet(SEVEN,FIVE,T,Y));
		keyToNeighborSetMap.put(SEVEN,getSet(EIGHT,SIX,Y,U));
		keyToNeighborSetMap.put(EIGHT,getSet(NINE,SEVEN,U,I));
		keyToNeighborSetMap.put(NINE,getSet(ZERO,EIGHT,I,O));
		keyToNeighborSetMap.put(BACKTICK,getSet(TAB,ONE));
		keyToNeighborSetMap.put(TAB,getSet(BACKTICK,ONE,Q));
		keyToNeighborSetMap.put(SHIFT_1,getSet(A,Z));
		keyToNeighborSetMap.put(SPACEBAR_1,getSet(C));
		keyToNeighborSetMap.put(SPACEBAR_2,getSet(V));
		keyToNeighborSetMap.put(SPACEBAR_3,getSet(B));
		keyToNeighborSetMap.put(SPACEBAR_4,getSet(N));
		keyToNeighborSetMap.put(SPACEBAR_5,getSet(M));



		
		return keyToNeighborSetMap;
	}
	
	private static Set<Key> getSet(Key... keys)
	{
		return new HashSet<Key>(Arrays.asList(keys));
	}

	private static PermutationImpl_Beneski<Key> getQWERTYToDvorakPermutation()
	{
		//Figure out what Permutation_Skeleton should be and QWERTYToDvorakPermutation if you want to use...
		//If you invent your own Permutation type, remember to name it Permutation_LastName...

		return null;
	}
	
	private static PermutationImpl_Beneski<Key> getQWERTYToColemakPermutation()
	{
		//Figure out what Permutation_Skeleton should be and QWERTYToColemakPermutation if you want to use...
		//If you invent your own Permutation type, remember to name it Permutation_LastName...
		return null;
	}
	
	private static PermutationImpl_Beneski<Key> getQWERTYToRotation13Permutation()
	{
		//Figure out what Permutation_Skeleton should be and QWERTYToRotation13Permutation if you want to use...
		//If you invent your own Permutation type, remember to name it Permuation_LastName...
		return null;
	}

	private static <E> Map<E, Set<E>> applyPermutationToMap(Map<E, Set<E>> map, PermutationImpl_Beneski<E> permutation)
	{
		//Ex: map = {A -> {B, E}, B -> {A, C, D}, C -> {B}, D -> {B}, E -> {A}}, permutation(A) = B, permutation(B) = C, permutation(C) = A
		Map<E, Set<E>> newMap = new HashMap<E, Set<E>>();

		//DO SOMETHING HERE...

		//Ex: map = {C -> {B, E}, A -> {A, C, D}, B -> {B}, D -> {B}, E -> {A}}, permutation(A) = B, permutation(B) = C, permutation(C) = A

		//DO SOMETHING ELSE HERE...

		//Ex: map = {C -> {A, E}, A -> {C, B, D}, B -> {A}, D -> {A}, E -> {C}}, permutation(A) = B, permutation(B) = C, permutation(C) = A
		return newMap;
	}

}