import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Random;

class MyCITS2200ProjectTest {

    MyCITS2200Project[] proj;
    private final int iterator = 15;
    ArrayList<AbstractMap.SimpleEntry<Integer, Integer>>[] edges; //key = from //value = to

    //this only checks positive numbers?
    @org.junit.jupiter.api.BeforeEach
    void setUp() {
        Random rand = new Random();
        proj = new MyCITS2200Project[100];
        for (int i = 0; i < proj.length; i++) {
            proj[i] = new MyCITS2200Project();
            for (int j = 0; j < iterator; j++) {
                for (int k = 0; k < iterator; k++) {

                    float f = rand.nextFloat();
                    if (f < 0.1) {
                        proj[i].addEdge(Integer.toString(j), Integer.toString(k));
                        edges[i].add(new AbstractMap.SimpleEntry<>(j, k));
                    }
                }
            }
        }

    }

    @org.junit.jupiter.api.Test
    void addEdge() {

        /*
        for (int i = 0; i < proj.length; i++) {
            //iterator for the key
            for (int j = 0; j < proj[i].adjList.size(); j++) {
                //iterator for the value
                for (int k = 0; k < proj[i].adjList.get(j).size(); k++) {
                    assertEquals(edges[i].get(j).getKey(), proj[i].adjList.get(j).get(k));
                }
            }
        }
        */

    }

    @org.junit.jupiter.api.Test
    void getShortestPath() {
    }

    @org.junit.jupiter.api.Test
    void getCenters() {
    }

    @org.junit.jupiter.api.Test
    void getStronglyConnectedComponents() {
    }

    @org.junit.jupiter.api.Test
    void fillStack() {
    }

    @org.junit.jupiter.api.Test
    void DFS() {
    }

    @org.junit.jupiter.api.Test
    void getHamiltonianPath() {
    }

    @org.junit.jupiter.api.Test
    void main() {
    }
}