package finalproject;

import java.util.HashMap;
import java.util.ArrayList;

public class SearchEngine {
	public HashMap<String, ArrayList<String>> wordIndex;   // this will contain a set of pairs (String, ArrayList of Strings)
	public MyWebGraph internet;
	public XmlParser parser;

	public SearchEngine(String filename) throws Exception {
		this.wordIndex = new HashMap<String, ArrayList<String>>();
		this.internet = new MyWebGraph();
		this.parser = new XmlParser(filename);
	}

	/*
	 * This does an exploration of the web, starting at the given url.
	 * For each new page seen, it updates the wordIndex, the web graph,
	 * and the set of visited vertices.
	 *
	 * 	This method will fit in about 30-50 lines (or less)
	 */
	public void crawlAndIndex(String url) throws Exception {
		// TODO : Add code here
		// we initialise two new arraylists, one more the visited urls, and one for the queue  where urls are processed
		ArrayList<String> visited = new ArrayList<>();
		ArrayList<String> queue = new ArrayList<>();

		// we add urls to both the arraylists
		queue.add(url);
		visited.add(url);

		// we initialise the currentIndex as 0
		int currentIndex = 0;
		// we use a while loop to track current url in the queue
		while (currentIndex < queue.size()) {
			String currentUrl = queue.get(currentIndex);
			currentIndex++;
			processUrl(currentUrl, visited, queue);
		}
	}

	// we add a private helper method called processUrl
	private void processUrl(String url, ArrayList<String> visited, ArrayList<String> queue) throws Exception {
		// we get the content and the hyperlinks from the current url
		ArrayList<String> words = parser.getContent(url);
		ArrayList<String> links = parser.getLinks(url);
		// we call the other private helper methods
		updateGraph(url, links);
		updateWordIndex(url, words);
		updateQueueAndVisited(url, links, visited, queue);
	}

	// we add a private helper method called updateGraph
	private void updateGraph(String url, ArrayList<String> links) {
		// we check if the vertex for the current url exists
		if (!internet.vertexList.containsKey(url)) {
			// we add vertex if it does not exist
			internet.addVertex(url);
		}
		// we set the index as 0, this acts as a counter
		int index = 0;
		// we process all the links in the current url using a while loop
		while (index < links.size()) {
			String link = links.get(index);

			// we check if the vertex for the current link exists
			if (!internet.vertexList.containsKey(link)) {
				// we add vertex if it does not exist
				internet.addVertex(link);
			}

			// we add an edge from the current url to the link
			internet.addEdge(url, link);
			// we update the counter and start processing the next link
			index++;
		}
	}

	// we add a private helper method called updateWordIndex
	private void updateWordIndex(String url, ArrayList<String> words) {
		// we loop through each pf the words in the current url
		for (String word : words) {
			// we convert all the words to uppercase letters to make it case-insensitive
			word = word.toLowerCase();
			// we check if the word is already indexed
			if (!wordIndex.containsKey(word)) {
				// if it is not indexed we create a new arraylist
				wordIndex.put(word, new ArrayList<>());
			}
			ArrayList<String> urls = wordIndex.get(word);
			// we check if the word already contains the url
			if (!urls.contains(url)) {
				// if it is not present, we add the current url to the list
				urls.add(url);
			}
		}
	}

	// we add a private helper method called updateQueueAndVisited
	private void updateQueueAndVisited(String url, ArrayList<String> links, ArrayList<String> visited, ArrayList<String> queue) {
		// we iterate through all the links in the current url
		for (String link : links) {
			// we check if the link has already been visited
			if (!visited.contains(link)) {
				// if it has not been visited, we add the link
				visited.add(link);
				queue.add(link);
			}
		}
	}

	/*
	 * This computes the pageRanks for every vertex in the web graph.
	 * It will only be called after the graph has been constructed using
	 * crawlAndIndex().
	 * To implement this method, refer to the algorithm described in the
	 * assignment pdf.
	 *
	 * This method will probably fit in about 30 lines.
	 */
	public void assignPageRanks(double epsilon) {
		// we initialise and ArrayList to get the list of vertices from internet
		ArrayList<String> vertices = internet.getVertices();
		boolean hasConverged;
		// we initialize maxIterations to avoid infinite loops
		int maxIterations = 1000;
		int iterationCount = 0;
		// we initialize a variable to track number of iterations after convergence
		int postConvergenceIterations = 0;
		// we initialize this variable to confirm stability
		int additionalStabilityIterations = 5;

		// we iterate through all the vertices and set an initial page rank to each of them
		for (String vertex : vertices) {
			internet.setPageRank(vertex, 1.0);
		}

		// we go through a do-while loop
		do {
			hasConverged = true;
			// we calculate the newPageRanks
			ArrayList<Double> newRanks = computeRanks(vertices);

			// we iterate through all vertices to compare the ranks and check for convergence
			for (int i = 0; i < vertices.size(); i++) {
				String vertex = vertices.get(i);
				double oldRank = internet.getPageRank(vertex);
				double newRank = newRanks.get(i);
				// we check if the ranks have or have not converged and update the counters accordingly
				if (Math.abs(newRank - oldRank) >= epsilon) {
					hasConverged = false;
					postConvergenceIterations = 0;
				}
				internet.setPageRank(vertex, newRank);
			}
			iterationCount++;

			// we perform additional iterations to check for stability
			if (hasConverged) {
				postConvergenceIterations++;
			}

			// we exit the loop if stability is confirmed or if maxIterations is reached
			if (iterationCount >= maxIterations || postConvergenceIterations > additionalStabilityIterations) {
				break;
			}
		}
		// we go through the whole process again if the following conditions apply
		while (!hasConverged || postConvergenceIterations <= additionalStabilityIterations);
	}

	public ArrayList<Double> computeRanks(ArrayList<String> vertices) {
		// we initialize the damping factor used
		double dampingFactor = 0.5;
		// we initialize a new ArrayList which stores all the newRanks
		ArrayList<Double> newRanks = new ArrayList<>();

		// we loop through all the vertices
		for (String vertex : vertices) {
			// we initialize a variable that keeps track of the sum
			double rankSum = 0.0;
			// we initialize an ArrayList that stores all the links
			ArrayList<String> links = internet.getEdgesInto(vertex);

			// we initialize an index to iterate through while loop
			int index = 0;
			// we use a while loop to calculate sum of all ranks
			while (index < links.size()) {
				String link = links.get(index);
				rankSum += internet.getPageRank(link) / internet.getOutDegree(link);
				index++;
			}
			// we compute the newRank using the formula
			double newRank = (1 - dampingFactor) + dampingFactor * rankSum;
			// we add the computed ranks to the ArrayList
			newRanks.add(newRank);
		}
		// we return the ArrayList containing the newRanks
		return newRanks;
	}


	/*
	 * The method takes as input an ArrayList<String> representing the urls in the web graph
	 * and returns an ArrayList<double> representing the newly computed ranks for those urls.
	 * Note that the double in the output list is matched to the url in the input list using
	 * their position in the list.
	 *
	 * This method will probably fit in about 20 lines.
	 */
	/*public ArrayList<Double> computeRanks(ArrayList<String> vertices) {
		// TODO : Add code here
		double dampingFactor = 0.5;  // Damping factor
		ArrayList<Double> newRanks = new ArrayList<>();
		HashMap<String, Double> currentRanks = new HashMap<>();

		for (String vertex : vertices) {
			currentRanks.put(vertex, internet.getPageRank(vertex));
			newRanks.add(0.0);  // Initialize with zero
		}

		for (int i = 0; i < vertices.size(); i++) {
			String v = vertices.get(i);
			double sum = 0.0;
			ArrayList<String> incomingLinks = internet.getEdgesInto(v);

			for (String w : incomingLinks) {
				sum += currentRanks.get(w) / internet.getOutDegree(w);
			}

			double newRank = (1 - dampingFactor) + (dampingFactor * sum);
			newRanks.set(i, newRank);
		}

		return newRanks;
	}*/


	/* Returns a list of urls containing the query, ordered by rank
	 * Returns an empty list if no web site contains the query.
	 *
	 * This method will probably fit in about 10-15 lines.
	 */
	public ArrayList<String> getResults(String query) {
		// we make sure the input parameter is case-insensitive
		query = query.toLowerCase();

		// if query is not found in the wordIndex, we return a new empty ArrayList
		if (!wordIndex.containsKey(query)) {
			return new ArrayList<>();
		}

		//we retrieve the list of urls
		ArrayList<String> urls = wordIndex.get(query);

		return sortUrlsByRank(urls);
	}

	// we create a new private helper method called sortUrlsByRank
	private ArrayList<String> sortUrlsByRank(ArrayList<String> urls) {
		// we create a new hashmap to arrange each url with its page rank
		HashMap<String, Double> urlsWithRanks = new HashMap<>();

		// we iterate through all the urls
		for (String url : urls) {
			// we get the pageRank for each of the urls
			double pageRank = internet.getPageRank(url);
			// we store the urls along with the pageRanks in the hashmap
			urlsWithRanks.put(url, pageRank);
		}
		// we sort and return each of the urls by their pageRanks in descending order using the static method fastSort
		return Sorting.fastSort(urlsWithRanks);
	}
}



