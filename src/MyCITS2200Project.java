import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

public class MyCITS2200Project implements CITS2200Project {

    private HashMap<Integer, String> vertMap;
    private ArrayList<LinkedList<Integer>> adjList;//storing the graph
    private ArrayList<LinkedList<Integer>> transposeGraph;
    private HashMap<String, Integer> reverseMap;
    private int vertMapIndex; // the next index of the vertMap
    private int test;

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
            return vertMapIndex++;
        }
        //the vertex already exists, return the index
        return reverseMap.get(url);
    }


    @Override
    public void addEdge(String urlFrom, String urlTo) {

        int urlFromID = addVert(urlFrom);
        int urlToID = addVert(urlTo);

        adjList.get(urlFromID).add(urlToID);


    }

    @Override
    public int getShortestPath(String urlFrom, String urlTo) {
        return 0;
    }

    @Override
    public String[] getCenters() {
        return new String[0];
    }

    @Override
    public String[][] getStronglyConnectedComponents() {

        // DFS on adjList and transpose graph
        /*
        any vertex whose subtree was explored before another in the DFS order
        (this is called post-order) either must
        not have a path to that other vertex, or is a descendant of it in the DFS tree
        */

        return new String[0][];
    }

    @Override
    public String[] getHamiltonianPath() {
        return new String[0];
    }
}