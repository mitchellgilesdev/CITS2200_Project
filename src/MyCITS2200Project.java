import java.util.*;

public class MyCITS2200Project implements CITS2200Project {

    private HashMap<Integer, String> vertMap;
    private ArrayList<LinkedList<Integer>> adjList; //storing the graph
    private ArrayList<LinkedList<Integer>> transposeGraph;
    private HashMap<String, Integer> reverseMap;
    private int vertMapIndex; // the next index of the vertMap
    private static int Infinity = Integer.MAX_VALUE / 2;

    public MyCITS2200Project() {
        adjList = new ArrayList<>();
        transposeGraph = new ArrayList<>();
        vertMap = new HashMap<>();
        reverseMap = new HashMap<>();
        vertMapIndex = 0;
    }

    /**
     * Check if vert exists, if not adds it to vertMap,
     * returning the ID in the vertMap.
     *
     * @param url The URL to be added to the graph
     * @return the ID the URL is mapped to from vertMap
     */
    private int addVert(String url) {
        if (!vertMap.containsValue(url)) {
            vertMap.put(vertMapIndex, url);
            reverseMap.put(url, vertMapIndex);

            adjList.add(vertMapIndex, new LinkedList<>());
            transposeGraph.add(vertMapIndex, new LinkedList<>());
            return vertMapIndex++;
        }
        return reverseMap.get(url);
    }

    /**
     * Add's an edge from a vertex to another, in both the graph
     * and the transpose graph.
     *
     * @param urlFrom the URL which has a link to urlTo.
     * @param urlTo   the URL which urlFrom has a link to.
     */
    @Override
    public void addEdge(String urlFrom, String urlTo) {
        int urlFromID = addVert(urlFrom);
        int urlToID = addVert(urlTo);

        adjList.get(urlFromID).add(urlToID);
        transposeGraph.get(urlToID).add(urlFromID);
    }

    /**
     * Returns the shortest number of edges required to get from one vertex to another
     * If there is no path, returns -1.
     *
     * @param urlFrom the URL/vertex where path will begin.
     * @param urlTo   the URL/vertex where the path will end.
     * @return the length of the shortest path to get from one page to another
     */
    @Override
    public int getShortestPath(String urlFrom, String urlTo) {

        //Return -1 or 0 if the vertices don't exist or are equal respectively.
        if (!reverseMap.containsKey(urlFrom) || !reverseMap.containsKey(urlTo)) {
            return -1;
        }
        if (urlFrom.equals(urlTo)) {
            return 0;
        }

        int numVert = reverseMap.size();
        boolean[] visited = new boolean[numVert];
        Arrays.fill(visited, false);
        int[] distance = new int[numVert];
        Arrays.fill(distance, -1);
        LinkedList<Integer> queue = new LinkedList<>();
        int start = reverseMap.get(urlFrom);
        int end = reverseMap.get(urlTo);

        //Add the starting vertex to the queue and initialise the root vertex distance to be 0
        visited[start] = true;
        distance[start] = 0;
        queue.add(start);

        //Iterate over each vertex in the LinkedList
        while (!queue.isEmpty()) {
            int current = queue.poll();
            for (int adjVert : adjList.get(current)) {
                if (!visited[adjVert]) {
                    visited[adjVert] = true;
                    distance[adjVert] = distance[current] + 1;

                    if (adjVert == end) {
                        return distance[adjVert];
                    }
                    queue.add(adjVert);
                }
            }
        }
        //If no path to the urlTo is found, return -1.
        return -1;
    }

    /**
     * For disconnected graphs ignore disconnected vertices when calculating BFSDistances.
     * Finds all the centers of the page graph.
     *
     * @return an array containing all the URLs that correspond to pages that are centers.
     */
    @Override
    public String[] getCenters() {
        int currentMinEcc = Integer.MAX_VALUE;
        ArrayList<Integer> minEccentricities = new ArrayList<>();

        //BFS on each vertex in the graph
        for (int i = 0; i < adjList.size(); i++) {
            int maxDistance = BFSDistances(i);
            if (maxDistance == currentMinEcc) {
                minEccentricities.add(i);
            } else if (maxDistance < currentMinEcc) {
                minEccentricities.clear();
                minEccentricities.add(i);
                currentMinEcc = maxDistance;
            }
        }
        //Convert the result into "/wiki" strings to output
        String[] centers = new String[minEccentricities.size()];
        for (int i = 0; i < minEccentricities.size(); i++) {
            centers[i] = vertMap.get(minEccentricities.get(i));
        }
        return centers;
    }

    /**
     * @param vertex perform a BFS from this vertex
     * @return the maximum shortest path from the given vertex
     */
    private int BFSDistances(int vertex) {
        int maxDistance = -1;
        int numVert = adjList.size();
        boolean[] visited = new boolean[numVert];
        int[] distance = new int[numVert];
        LinkedList<Integer> queue = new LinkedList<>();

        //Mark the input vertex as visited and add to the queue
        visited[vertex] = true;
        queue.add(vertex);
        distance[vertex] = 0;

        while (!queue.isEmpty()) {
            int current = queue.poll();
            for (int n : adjList.get(current)) {
                if (!visited[n]) {
                    visited[n] = true;
                    distance[n] = distance[current] + 1;
                    queue.add(n);
                }
                if (distance[n] > maxDistance) {
                    maxDistance = distance[n];
                }
            }
        }
        return maxDistance;
    }


    /**
     * Finds all the strongly connected components of the page graph.
     * Every strongly connected component can be represented as an array
     * containing the page URLs in the component.
     *
     * @return An array of Strongly Connected Components.
     */
    @Override
    public String[][] getStronglyConnectedComponents() {
        String[][] output;
        int numVert = adjList.size();
        boolean[] visited = new boolean[numVert];
        Arrays.fill(visited, false);

        //The first "down" DFS is performed in fillStack.
        Stack<Integer> stack = fillStack(adjList, visited);
        visited = new boolean[numVert]; //reset the
        List<List<Integer>> SCC = new ArrayList<>();

        //An "Upwards" DFS is performed on the stack
        while (!stack.isEmpty()) {
            int vert = stack.pop();
            if (!visited[vert]) {
                Stack<Integer> order = new Stack<>();
                DFS(transposeGraph, vert, visited, order);
                SCC.add(order);
            }
        }

        //Converting the results back to URL's
        output = new String[SCC.size()][];
        for (int i = 0; i < SCC.size(); i++) {
            String[] components = new String[SCC.get(i).size()];
            for (int j = 0; j < SCC.get(i).size(); j++) {
                components[j] = vertMap.get(SCC.get(i).get(j));
            }
            output[i] = components;
        }
        return output;
    }

    /**
     * Helper method for the getStronglyConnectedComponent()
     * Performs the first "downwards" DFS.
     *
     * @param graph   The specific adjList it is used on.
     * @param visited Whether or not a vertex has been visited in the BFS.
     * @return A stack containing the order for the "upwards" BFS.
     */
    private Stack<Integer> fillStack(ArrayList<LinkedList<Integer>> graph, boolean[] visited) {
        int numVert = graph.size();
        Stack<Integer> order = new Stack<>();

        for (int i = 0; i < numVert; i++)
            if (!visited[i])
                DFS(graph, i, visited, order);
        return order;
    }

    /**
     * Helper method for the getStronglyConnectedComponent()
     * Implements a Depth First Search on the graph.
     *
     * @param graph   The specific adjList it is used on.
     * @param vertex  The vertex to perform the DFS from.
     * @param visited Whether or not a vertex has been visited in the BFS.
     * @param order   The order to do the upwards DFS.
     */
    private void DFS(ArrayList<LinkedList<Integer>> graph, int vertex, boolean[] visited, Stack<Integer> order) {
        visited[vertex] = true;
        for (int i = 0; i < graph.get(vertex).size(); i++) {
            if (!visited[graph.get(vertex).get(i)]) {
                DFS(graph, graph.get(vertex).get(i), visited, order);
            }
        }
        order.push(vertex);
    }

    /**
     * Finds a Hamiltonian path in the page graph. There may be many
     * possible Hamiltonian paths. This method should never be called on a
     * graph with more than 20 vertices. If there is no Hamiltonian path,
     * this method will return an empty array.
     *
     * @return a Hamiltonian path of the page graph
     */
    @Override
    public String[] getHamiltonianPath() {
        int numVert = adjList.size();
        int[][] matrix = new int[1 << numVert][numVert];

        //fill the entire matrix by MAX_INT
        for (int[] vertex : matrix) {
            Arrays.fill(vertex, Infinity);
        }
        for (int i = 0; i < numVert; i++) {
            matrix[1 << i][i] = 0;
        }

        //Iteration over everything.
        for (int mask = 0; mask < 1 << numVert; mask++) {
            for (int i = 0; i < numVert; i++) {
                if ((mask & 1 << i) != 0) {
                    for (int j = 0; j < numVert; j++) {
                        if ((mask & 1 << j) != 0) {
                            matrix[mask][i] = Math.min(matrix[mask][i], matrix[mask ^ (1 << i)][j] + getElement(j, i));
                        }
                    }
                }
            }
        }
        int last = -1;
        int cur = (1 << numVert) - 1;
        int[] order = new int[numVert];
        //Selecting the vertices backwards
        for (int i = numVert - 1; i >= 0; i--) {
            int bj = -1;
            for (int j = 0; j < numVert; j++) {
                if ((cur & 1 << j) != 0 && (bj == -1
                        || matrix[cur][bj] + (last == -1 ? 0 : getElement(bj, last)) > matrix[cur][j] + (last == -1 ? 0 : getElement(j, last)))) {
                    bj = j;
                }
            }
            order[i] = bj;
            cur ^= 1 << bj;
            last = bj;
        }

        //Formatting the answer as a String for output
        String[] result = new String[numVert];
        for (int i = 0; i < order.length; i++) {
            if (i != numVert - 1) {
                if (!(adjList.get(order[i]).contains(order[i + 1]))) {
                    return new String[0];
                }
            }
            result[i] = vertMap.get(order[i]);
        }
        return result;
    }

    /**
     * Helper method to use adjacency list to locate a certain element
     *
     * @param node    parent node of the element
     * @param element actual element to be checked
     * @return 1 if element exists, "Infinity" if it doesn't.
     */
    private int getElement(int node, int element) {
        if (!(adjList.get(node).contains(element))) {
            return Infinity;
        }
        return 1;
    }
}