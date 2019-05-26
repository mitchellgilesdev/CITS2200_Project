import java.util.*;
import java.io.BufferedReader;
import java.io.FileReader;

public class MyCITS2200Project implements CITS2200Project {

    private HashMap<Integer, String> vertMap; //KEY:ID VALUE:URL
    public ArrayList<LinkedList<Integer>> adjList;//storing the graph
    private ArrayList<LinkedList<Integer>> transposeGraph;
    private HashMap<String, Integer> reverseMap;//KEY:URL VALUE:ID
    private int vertMapIndex; // the next index of the vertMap
    private static int Infinity = Integer.MAX_VALUE / 2;

    //Constructor for the class
    public MyCITS2200Project() {
        adjList = new ArrayList<>();
        transposeGraph = new ArrayList<>();
        vertMap = new HashMap<>();
        reverseMap = new HashMap<>();
        vertMapIndex = 0;
    }

    //Check if vert exists, if not adds it to vertMap
    //returning the ID in the vertMap
    private int addVert(String url) {
        //if there is no vertex existing
        if (!vertMap.containsValue(url)) {
            vertMap.put(vertMapIndex, url);
            reverseMap.put(url, vertMapIndex);
            adjList.add(vertMapIndex, new LinkedList<>()); //add the vertex to the graph
            transposeGraph.add(vertMapIndex, new LinkedList<>()); //fixing out of bounds error
            return vertMapIndex++;
        }
        //the vertex already exists, return the index
        return reverseMap.get(url);
    }


    @Override
    public void addEdge(String urlFrom, String urlTo) {
        int urlFromID = addVert(urlFrom);
        int urlToID = addVert(urlTo);

        //add connection to the graph and the transpose graph
        adjList.get(urlFromID).add(urlToID);
        transposeGraph.get(urlToID).add(urlFromID);
    }


    /*
     ******************************************************************************************************************
     *                   getShortestPath()
     ******************************************************************************************************************
     */

    /**
     * Returns the shortest number of edges required to get from one page to another
     * If there is no path, returns -1.
     *
     * @param urlFrom the URL/page where path will begin.
     * @param urlTo   the URL/page where the path will end.
     * @return the length of the shortest path to get from one page to another
     */
    @Override
    public int getShortestPath(String urlFrom, String urlTo) {

        //if one of the urls does not exist in the graph
        if (!reverseMap.containsKey(urlFrom) || !reverseMap.containsKey(urlTo)) {
            //Throw an error or return -1
            //throw new IllegalArgumentException("One of the urls does not exist!");
            //System.out.println("One of the urls does not exist");
            return -1;
        }
        //if both urls are the same
        if (urlFrom.equals(urlTo)) {
            return 0;
        }

        int numVert = reverseMap.size();
        //Array to record if the vertex has been visited
        boolean[] visited = new boolean[numVert];
        Arrays.fill(visited, false);
        //Array to hold the distances
        int[] distance = new int[numVert];
        Arrays.fill(distance, -1);
        //Get the urlFrom index and the urlTo index
        int start = reverseMap.get(urlFrom);
        int end = reverseMap.get(urlTo);
        //Initialise a LinkedList to act as a queue to explore vertices/pages
        LinkedList<Integer> queue = new LinkedList<>();
        //Add the starting vertex to the queue and initialise the root vertex distance to be 0
        visited[start] = true;
        distance[start] = 0;
        queue.add(start);

        while (!queue.isEmpty()) {
            //dequeue the front element of the queue
            int current = queue.poll();
            //Iterate over each vertex in the LinkedList at the start vertex of the adjList
            for (int adjVert : adjList.get(current)) {
                //if colour is white/if the vertex has not been visited
                if (!visited[adjVert]) {
                    visited[adjVert] = true;
                    distance[adjVert] = distance[current] + 1;

                    //Checking if it has reached the final destination/page/vertex
                    if (adjVert == end) {
                        return distance[adjVert];
                    }
                    queue.add(adjVert);
                }
            }
        }
        //If no path to the urlTo is found, return -1 to show no shortest path found from urlFrom
        return -1;
    }


    /*
     ******************************************************************************************************************
     *
     *                   getCenters()
     *                   For disconnected graphs ignore disconnected vertices when calculating BFSDistances.
     *
     ******************************************************************************************************************
     */

    /**
     * Finds all the centers of the page graph. The order of pages
     * in the output does not matter. Any order is correct as long as
     * all the centers are in the array, and no pages that aren't centers
     * are in the array.
     *
     * @return an array containing all the URLs that correspond to pages that are centers.
     */
    @Override
    public String[] getCenters() {
        int currentMinEcc = Integer.MAX_VALUE;
        ArrayList<Integer> minEccentricityVerts = new ArrayList<>();

        //for each vertex in the adjList perform a BFS on the vertex
        for (int i = 0; i < adjList.size(); i++) {
            int maxDistance = BFSDistances(i);
            if (maxDistance == currentMinEcc) {
                minEccentricityVerts.add(i);
            } else if (maxDistance < currentMinEcc) {
                minEccentricityVerts.clear();
                minEccentricityVerts.add(i);
                currentMinEcc = maxDistance;
            }
        }

        String[] centers = new String[minEccentricityVerts.size()];
        for (int i = 0; i < minEccentricityVerts.size(); i++) {
            centers[i] = vertMap.get(minEccentricityVerts.get(i));
        }
        return centers;
    }


    /**
     * @param vertex perform a BFS from the vertex that is passed in as a linked list
     * @return
     */
    private int BFSDistances(int vertex) {

        int maxDistance = -1;
        int numVert = adjList.size();
        boolean[] visited = new boolean[numVert];
        int[] distance = new int[numVert];
        LinkedList<Integer> queue = new LinkedList<>();

        //mark the input vertex as visited and add to the queue
        visited[vertex] = true;
        queue.add(vertex);
        distance[vertex] = 0; //don't need already initialised to zero

        //iterate through each vertex in the array
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

    /*
     ******************************************************************************************************************
     *                   getStronglyConnectedComponents()
     ******************************************************************************************************************
     */
    @Override
    public String[][] getStronglyConnectedComponents() {

        String[][] output;
        int V = adjList.size();
        boolean[] visited = new boolean[V];
        Arrays.fill(visited, false);
        Stack<Integer> stack = fillStack(adjList, visited);
        visited = new boolean[V];

        List<List<Integer>> SCC = new ArrayList<>();
        while (!stack.isEmpty()) {
            int vert = stack.pop();
            if (!visited[vert]) {
                Stack<Integer> order = new Stack<>();
                DFS(transposeGraph, vert, visited, order);
                SCC.add(order);
            }
        }

        //String[SCC #][Components]
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

    public Stack<Integer> fillStack(ArrayList<LinkedList<Integer>> graph, boolean[] visited) {
        int V = graph.size();
        Stack<Integer> order = new Stack<>();

        for (int i = 0; i < V; i++)
            if (!visited[i])
                DFS(graph, i, visited, order);
        return order;

    }

    public void DFS(ArrayList<LinkedList<Integer>> graph, int vertex, boolean[] visited, Stack<Integer> order) {

        visited[vertex] = true;
        for (int i = 0; i < graph.get(vertex).size(); i++) {
            if (!visited[graph.get(vertex).get(i)]) {
                DFS(graph, graph.get(vertex).get(i), visited, order);
            }
        }
        order.push(vertex);
    }


    //Helper method to use adjacency list to locate a certain element
    private int getElement(int node, int element) {
        if (!(adjList.get(node).contains(element))) {
            return Infinity;
        }
        return 1;
    }

    /*
     ******************************************************************************************************************
     *                   getHamiltonianPath()
     ******************************************************************************************************************
     */
    @Override
    public String[] getHamiltonianPath() {
        //Use Bellman-Fords algorithm?
        //Travelling Salesman problem?
        //Use bit shifting
        //2^V subsets of edges

        //Gets the number of vertices
        int n = adjList.size();
        //Use bit shifting to create a 2^n by n matrix
        //As dynamic programming tells us, find all the solutions and pick the best one
        //Use bitmasks to represent subsets
        int[][] dp = new int[1 << n][n];

        //fill the entire matrix by infinity/large value
        for (int[] d : dp) {
            Arrays.fill(d, Infinity);
        }

        for (int i = 0; i < n; i++) {
            dp[1 << i][i] = 0;
        }

        //Iteration over the 2^n lines of code
        for (int mask = 0; mask < 1 << n; mask++) {
            for (int i = 0; i < n; i++) {
                if ((mask & 1 << i) != 0) {
                    for (int j = 0; j < n; j++) {
                        if ((mask & 1 << j) != 0) {
                            dp[mask][i] = Math.min(dp[mask][i], dp[mask ^ (1 << i)][j] + getElement(j, i));
                        }
                    }
                }
            }
        }
        //  System.out.println(Arrays.deepToString(dp).replace("], ", "]\n").replace("[[", "[").replace("]]", "]"));

        // reconstruct path
        /*int last = 0;
        int cur = (1 << n) - 1;
        int[] order = new int[n];
        for (int i = n - 1; i >= 0; i--) {
            int bj = -1;
            for (int j = 0; j < n; j++) {
                if ((cur & 1 << j) != 0
                        && (bj == -1
                        || dp[cur][bj] + (last == -1 ? 0 : getElement(bj, last)) > dp[cur][j] + (last == -1 ? 0 : getElement(j, last)))) {
                    bj = j;
                }
            }
            order[i] = bj;
            cur ^= 1 << bj;
            last = bj;
        }*/

        int last = -1;
        int cur = (1 << n) - 1;
        int[] order = new int[n];
        //Selecting the vertices backwards
        for (int i = n - 1; i >= 0; i--) {
            int bj = -1;

            for (int j = 0; j < n; j++) {
                if ((cur & 1 << j) != 0 && (bj == -1
                        || dp[cur][bj] + (last == -1 ? 0 : getElement(bj, last)) > dp[cur][j] + (last == -1 ? 0 : getElement(j, last)))) {
                    bj = j;
                }
            }

            order[i] = bj;
            cur ^= 1 << bj;
            last = bj;
        }
        String[] result = new String[n];
        for (int i = 0; i < order.length; i++) {
            if (i != n - 1) {
                if (!(adjList.get(order[i]).contains(order[i + 1]))) {
                    return new String[0];
                }
            }
            result[i] = vertMap.get(order[i]);
        }
        return result;
        /*String[] ham = new String [n];

        // Loop to check if the path exists using the indices in the array, order
        for ( int i = 0; i < order.length-1; i++ )
            if( !page.getRow(order[i]).contains(order[i+1]) )
                return ham;

        for( int i = 0; i < n; i++ )
            ham[i] = page.getVertex(order[i]);

        return ham;*/

        //System.out.println(Arrays.deepToString(dp).replace("], ", "]\n").replace("[[", "[").replace("]]", "]"));
    }

    //Testing for the method shortestPath
    public static void main(String[] args) {
        MyCITS2200Project test = new MyCITS2200Project();
        String path = "exampleGraphs/example_graph.txt";
        //String path = "exampleGraphs/example2.txt";
        //String path = "exampleGraphs/small_graph";
        try {
            BufferedReader reader = new BufferedReader(new FileReader(path));
            while (reader.ready()) {
                String from = reader.readLine();
                String to = reader.readLine();
                //System.out.println("Adding edge from " + from + " to " + to);
                test.addEdge(from, to);
            }
        } catch (Exception e) {
            System.out.println("There was a problem:");
            System.out.println(e.toString());
        }
        //System.out.println(test.getShortestPath("2","0"));
        System.out.println(Arrays.toString(test.getHamiltonianPath()));
        //System.out.println(Arrays.deepToString(test.getStronglyConnectedComponents()));
        //System.out.println(Arrays.toString(test.getCenters()));

        /*test.addEdge("1", "2");
        test.addEdge("1", "3");
        test.addEdge("1", "5");
        test.addEdge("3", "4");
        test.addEdge("3", "5");
        test.addEdge("4", "5");
        test.addEdge("4", "2"); // Added edge for HAM
        test.addEdge("2", "7");
        test.addEdge("7", "6");
        test.addEdge("6", "5");
        test.addEdge("5", "8");*/

        /*
        //###############################################################################
        //Shortest Path tests
        //###############################################################################
        System.out.println("Shortest paths tests initializing...");
        Random rand = new Random();
        int nums = test.adjList.size();

        //-------------------------------------------------------------------------------
        System.out.println("\nTesting for when 1 or both urls don't exist in the graph...");
        if (test.getShortestPath("1", "10") != -1) {
            System.out.println("Value is incorrect!");
            System.exit(0);
        }
        else if (test.getShortestPath("10", "2") != -1) {
            System.out.println("Value is incorrect!");
            System.exit(0);
        }
        //-------------------------------------------------------------------------------
        System.out.println("\nTesting for both urls are the same...");
        System.out.println(test.getShortestPath("1", "1"));
        //-------------------------------------------------------------------------------

        System.out.println("\nTesting for different vertices...");
        for (int i = 0; i < nums; i++) {
            String urlTo = Integer.toString(rand.nextInt(nums)+1);
            String urlFrom = Integer.toString(rand.nextInt(nums)+1);
            System.out.println("\n"+urlFrom + " to " + urlTo);
            System.out.println(test.getShortestPath(urlFrom, urlTo));

        }*/
        //System.out.println(test.reverseMap);
    }
}