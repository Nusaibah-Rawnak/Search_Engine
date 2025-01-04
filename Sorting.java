package finalproject;

import java.util.ArrayList;
import java.util.HashMap;
//import java.util.Map.Entry; // You may (or may not) need it to implement fastSort

public class Sorting {

	/*
	 * This method takes as input an HashMap with values that are Comparable. 
	 * It returns an ArrayList containing all the keyList from the map, ordered 
	 * in descending order based on the values they mapped to. 
	 * 
	 * The time complexity for this method is O(n^2) as it uses bubble sort, where n is the number 
	 * of pairs in the map. 
	 */
    public static <K, V extends Comparable<V>> ArrayList<K> slowSort (HashMap<K, V> results) {
        ArrayList<K> sortedUrls = new ArrayList<K>();
        sortedUrls.addAll(results.keySet());	//Start with unsorted list of urls

        int N = sortedUrls.size();
        for(int i=0; i<N-1; i++){
			for(int j=0; j<N-i-1; j++){
				if(results.get(sortedUrls.get(j)).compareTo(results.get(sortedUrls.get(j+1))) < 0){
					K temp = sortedUrls.get(j);
					sortedUrls.set(j, sortedUrls.get(j+1));
					sortedUrls.set(j+1, temp);
				}
			}
        }
        return sortedUrls;
    }
    
    
	/*
	 * This method takes as input an HashMap with values that are Comparable. 
	 * It returns an ArrayList containing all the keys from the map, ordered 
	 * in descending order based on the values they mapped to. 
	 * 
	 * The time complexity for this method is O(n*log(n)), where n is the number 
	 * of pairs in the map. 
	 */
    public static <K, V extends Comparable<V>> ArrayList<K> fastSort(HashMap<K, V> results) {
		// TODO: Add code here
		// we create a new ArrayList of keys
		ArrayList<K> keyList = new ArrayList<>(results.keySet());
		// we call the mergeSort method to sort the keys
		mergeSort(keyList, 0, keyList.size() - 1, results);
		// we return the sorted list of keys
		return keyList;
	}

	// we create a new private helper method called mergeSort
	private static <K, V extends Comparable<V>> void mergeSort(ArrayList<K> keyList, int lowerIndex, int upperIndex, HashMap<K, V> results) {
		// we check if the lowerIndex is lower than the upperIndex to check if the list can be divided
		if (lowerIndex < upperIndex) {
			// we compute the middleIndex
			int middleIndex = lowerIndex + (upperIndex - lowerIndex) / 2;
			// we call mergeSort and use recursion to solve the two halves
			mergeSort(keyList, lowerIndex, middleIndex, results);
			mergeSort(keyList, middleIndex + 1, upperIndex, results);
			// we call mergeSortedSegments to merge the two halves
			mergeSortedSegments(keyList, lowerIndex, middleIndex, upperIndex, results);
		}
	}

	// we create a new private helper method called mergeSortedSegments
	private static <K, V extends Comparable<V>> void mergeSortedSegments(ArrayList<K> keyList, int lowerIndex, int middleIndex, int upperIndex, HashMap<K, V> results) {
		// we initialize a new temporary ArrayList which stores the merged results
		ArrayList<K> tmp = new ArrayList<>(upperIndex - lowerIndex + 1);
		// we initialize new variables which act as pointers to the different segments
		int i = lowerIndex;
		int j = middleIndex + 1;
		int k = 0;

		// we use a while loop to sort and merge elements from the left and right segments
		while (i <= middleIndex && j <= upperIndex) {
			// we initialize a variable named comparisonResult to store the result after comparison of the keys
			int comparisonResult = results.get(keyList.get(j)).compareTo(results.get(keyList.get(i)));
			if (comparisonResult > 0) {
				tmp.add(k++, keyList.get(j++));
			}
			else {
				tmp.add(k++, keyList.get(i++));
			}
		}

		// we copy any remaining elements from the left segment
		while (i <= middleIndex) {
			tmp.add(k++, keyList.get(i++));
		}

		// we copy any remaining elements from the right segment
		while (j <= upperIndex) {
			tmp.add(k++, keyList.get(j++));
		}

		// finally, we copy the merged elements back to the original ArrayList
		for (k = 0; k < tmp.size(); k++) {
			keyList.set(lowerIndex + k, tmp.get(k));
		}
	}

}