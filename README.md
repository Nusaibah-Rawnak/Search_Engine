# Search_Engine

## Introduction
This project was developed as part of COMP 250 Winter 2024. It focuses on implementing a simplified search engine that performs web crawling, indexing, ranking, and searching. The project integrates custom graph representation, hash map data retrieval, and efficient sorting algorithms.

## Features

### 1. Crawling and Indexing
This feature explores a portion of the web (simulated using an XML database) starting from a given URL. It constructs a web graph where each node represents a webpage, and edges represent hyperlinks between them. The word index is built by mapping each word to the list of URLs containing that word.

**Example Output:**

- Web graph constructed with 10 vertices and 25 edges.
- Word index created with 500 unique words.

### 2. PageRank Calculation
This feature assigns a rank to each webpage using the PageRank algorithm. The rank of a webpage is determined by the ranks of the pages linking to it and their out-degrees.

**Example Output:**

- PageRank values for all pages computed after 5 iterations.
- Convergence reached with epsilon = 0.01.

### 3. Searching
This feature allows users to search for a specific word. It returns a list of URLs containing the query, sorted by their PageRank.

### 4. Sorting Algorithms
The project includes two sorting methods for ranking search results:

- **`slowSort`**: A bubble sort implementation with O(n^2) time complexity.
- **`fastSort`**: A merge sort implementation with O(n log n) time complexity.

**Example Output:**

- Sorted URLs by PageRank using fastSort.

## Technologies Used

- **Programming Language**: Java
- **IDE**: IntelliJ IDEA
- **Data Structures**: Graph (MyWebGraph), Hash Map, Sorting Algorithms (slowSort, fastSort)
- **Dataset**: XML files simulating webpages and hyperlinks

## How to Run the Application

### Prerequisites

- **Java Development Kit (JDK)**: Ensure that JDK 8 or higher is installed on your system.

### Steps to Run

1. Clone the Repository:
   ```bash
   git clone https://github.com/yourusername/comp250-search-engine.git
   cd comp250-search-engine
   ```
2. Open the Project in IntelliJ IDEA:
   - Set the JDK as your project SDK.
   - Ensure that the `finalproject` package structure is correctly maintained.
3. Build and Run:
   - Build the project by selecting `Build > Build Project`.
   - Run the `Main` class to launch the application.
4. Use the console-based interface to perform different operations (crawling, ranking, and searching).

## Implementation Details

### MyWebGraph
This class represents a directed graph where each vertex corresponds to a webpage, and edges represent hyperlinks.

- **Data Structure**: HashMap for storing vertices and edges
- **Public Methods**:
  - `addVertex(String s)`: Adds a new vertex representing a webpage.
  - `addEdge(String s, String t)`: Adds a directed edge between two webpages.
  - `getVertices()`: Returns a list of all vertices in the graph.
  - `getEdgesInto(String v)`: Returns a list of pages linking to a given webpage.

### SearchEngine
This class handles web crawling, indexing, PageRank calculation, and searching.

- **Public Methods**:
  - `crawlAndIndex(String url)`: Builds the web graph and word index.
  - `assignPageRanks(double epsilon)`: Computes the PageRank for each webpage using an iterative approach.
  - `getResults(String query)`: Returns a list of URLs containing the query, sorted by PageRank.

### Sorting
This utility class provides sorting methods for ranking search results.

- **slowSort**: Bubble sort with O(n^2) complexity.
- **fastSort**: Merge sort with O(n log n) complexity.

## Success Criteria
- The web graph and word index are correctly constructed.
- PageRank values converge within the specified threshold.
- The search function returns correctly ranked results.
- The project adheres to the course guidelines and constraints.

## Future Enhancements
- Improve the crawling algorithm to handle larger datasets.
- Add support for multi-word queries.
- Implement additional ranking metrics.
- Provide a graphical user interface (GUI) for easier interaction.

## Contact
For any inquiries or feedback, please contact Nusaibah Binte Rawnak at nusaibah.rawnak@mail.mcgill.ca.

## License
This project is licensed under the MIT License.
