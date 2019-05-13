import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;

public class MyCITS2200Project implements CITS2200Project {

    private HashMap<Integer, String> vertMap;
    private ArrayList<LinkedList<Integer>> adjList;//storing the graph
    private HashMap<String, Integer> reverseMap;
    private int vertMapIndex;

    //Constructor for the class
    public MyCITS2200Project() {
        adjList = new ArrayList<>();
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
            return vertMapIndex++;
        }
        //the vertex already exists, return the index
        return reverseMap.get(url);
    }


    @Override
    public void addEdge(String urlFrom, String urlTo) {

        int urlFromID =  addVert(urlFrom);
        int urlToID = addVert(urlTo);

        adjList.add(urlFromID, new LinkedList<>(Collections.singleton(urlToID)));

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
        return new String[0][];
    }

    @Override
    public String[] getHamiltonianPath() {
        return new String[0];
    }
}