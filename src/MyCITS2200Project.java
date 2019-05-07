import java.util.HashMap;
import java.util.LinkedList;

public class MyCITS2200Project implements CITS2200Project {

    private HashMap<Integer, String> vertMap;
    private LinkedList<Integer> adjList[]; //storing the graph
    private int vertMapIndex;

    public class wikiGraph {

        private int numVertices;

        public wikiGraph(int numVertex) {
            numVertices = numVertex;
            adjList = new LinkedList[numVertex];

            for (int i = 0; i < numVertices; i++) {
                adjList[i] = new LinkedList<>();
            }
        }
    }

    //Constructor for the class
    public MyCITS2200Project() {
        vertMap = new HashMap<>();
        vertMapIndex = 0;

    }


    //setter method.
    private boolean addVert(String url) {

        if (!vertMap.containsValue(url)) {
            vertMap.put(vertMapIndex++, url);
        }
        return true;
    }


    private int urlToId(String url) {
        int temp = vertMapIndex;
        vertMap.put(vertMapIndex, url);
        vertMapIndex++;
        return temp;
    }

    @Override
    public void addEdge(String urlFrom, String urlTo) {

        addVert(urlFrom);
        addVert(urlTo);

        //Once we know that the vertexes exist we can add the edges to the adjList
        int urlFromId = urlToId(urlFrom);
        int urlToId = urlToId(urlTo);

        adjList[urlFromId].add(urlToId);


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