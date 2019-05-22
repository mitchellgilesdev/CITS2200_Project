import java.util.*;

public class MyCITS2200Project implements CITS2200Project {

    private HashMap<Integer, String> vertMap; //KEY:ID VALUE:URL
    private ArrayList<LinkedList<Integer>> adjList;//storing the graph
    private ArrayList<LinkedList<Integer>> transposeGraph;
    private HashMap<String, Integer> reverseMap;//KEY:URL VALUE:ID
    private int vertMapIndex; // the next index of the vertMap
    private boolean[] visited; //use in DFS for SCC
    private Stack<Integer> stack; //use in DFS for SCC
    private int[] eccentricity; //maintaining a list of vertices with minimum eccentricity gives us a list of graph centers
    private static int Infinity = Integer.MAX_VALUE/2;

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


    /* Need to find the set of vertices with minimum eccentricity
       Eccentricity = max length shortest path from a vertex to any other
                Lengths of shortest path to each vertex (from Q1)
                -> the max of this path from a vertex is it's eccentricity
                -> Perform a BFS search (from Q1) from that vertex and take the maximum value from the array
                of distances computed

        for each vertex in the adjList
        Do a BFS(vertex) to get max(distances[])       -----from Q1

        Doing this once for each starting vertex and maintaining a list of vertices
        with minimum eccentricity gives us a list of graph centers

        [ ] Requires one BFS per vertex giving:
            Time Complexity: O(V × (V + E)) = O( V^2 + VE) which is optimal.
         */
    /*
     ******************************************************************************************************************
     *                   getCenters()
     ******************************************************************************************************************
     */
    @Override
    public String[] getCenters() {

        int currentMinEcc = Integer.MAX_VALUE;
        ArrayList<Integer> minEccentricityVerts = new ArrayList<>();

        //for each vertex in the adjList
        for (int i = 0; i < adjList.size(); i++) {

            //perform a BFS on the vertex
            int maxDistance = BFSDistances(i);

            //if the
            if (maxDistance < currentMinEcc) {
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


    //perform a BFS from the vertex that is passed in as a linked list
    private int BFSDistances(int vertex) {

        int maxDistance = -1;
        int numVert = adjList.size();
        boolean visited[] = new boolean[numVert];
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

        int V = adjList.size();
        visited = new boolean[V];
        Arrays.fill(visited, false);
        stack = fillStack(adjList, visited); //first DFS performed in fillStack
        visited = new boolean[V];


        //must change arraylist to String[][]
        List<List<Integer>> SCC = new ArrayList<>(); //change the size initialiser
        for (int i = 0; i < stack.size(); i++) {
            int vert = stack.pop();
            if (!visited[vert]) {
                Stack<Integer> order = new Stack<>();
                DFS(transposeGraph, vert, visited, order);
                SCC.add(order);
            }
        }

        //testing
        System.out.println("SCC Array:" + SCC);
        // DFS on adjList and transpose graph
        /*
        any vertex whose subtree was explored before another in the DFS stack
        (this is called post-stack) either must
        not have a path to that other vertex, or is a descendant of it in the DFS tree
        */

        return new String[0][];
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
    private int getElement(int node, Object element) {
        if (adjList.get(node).contains(element)) {
            return 1;
        }
        return Infinity;
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
        int n = adjList.size();
        int[][] dp = new int[1 << n][n];
        for (int[] d : dp)
            Arrays.fill(d, Infinity);
        for (int i = 0; i < n; i++)
            dp[1 << i][i] = 0;
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
        int res = Integer.MAX_VALUE;
        for (int i = 0; i < n; i++) {
            res = Math.min(res, dp[(1 << n) - 1][i]);
        }

        // reconstruct path
        int last = 0;
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
        }
        String [] result = new String[n];
        for (int i = 0; i < order.length; i++) {
            result[i] = vertMap.get(order[i]);
        }
        /*String[] ham = new String [n];

        // Loop to check if the path exists using the indices in the array, order
        for ( int i = 0; i < order.length-1; i++ )
            if( !page.getRow(order[i]).contains(order[i+1]) )
                return ham;

        for( int i = 0; i < n; i++ )
            ham[i] = page.getVertex(order[i]);

        return ham;*/

        //System.out.println(Arrays.deepToString(dp).replace("], ", "]\n").replace("[[", "[").replace("]]", "]"));
        System.out.println(Arrays.toString(result));
        return result;
    }

    //Testing for the method shortestPath
    public static void main(String [] args) {
        MyCITS2200Project test = new MyCITS2200Project();
        /*String path = "exampleGraphs/example_graph.txt";
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
        }*/


        test.addEdge("1", "2");
        test.addEdge("1", "3");
        test.addEdge("1", "5");
        test.addEdge("3", "4");
        test.addEdge("3", "5");
        test.addEdge("4", "5");
        test.addEdge("4", "2"); // Added edge for HAM
        test.addEdge("2", "7");
        test.addEdge("7", "6");
        test.addEdge("6", "5");
        test.addEdge("5", "8");

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
        System.out.println(Arrays.toString(test.getHamiltonianPath()));
        System.out.println(Arrays.deepToString(test.getStronglyConnectedComponents()));
        System.out.println(Arrays.toString(test.getCenters()));
    }
}