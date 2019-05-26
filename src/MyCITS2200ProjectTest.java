import org.junit.jupiter.api.io.TempDir;

import java.lang.reflect.Array;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MyCITS2200ProjectTest {

    MyCITS2200Project[] proj;
    private final int iterator = 15;
    ArrayList<AbstractMap.SimpleEntry<Integer, Integer>> edges[]; //key = from //value = to

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

    @Test
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

    @Test
    void getShortestPath() {


    }

    @Test
    void getCenters() {
    }

    @Test
    void getStronglyConnectedComponents() {
    }

    @Test
    void fillStack() {
    }

    @Test
    void DFS() {
    }

    @Test
    void getHamiltonianPath() {
    }

    @Test
    void main() {
    }
}