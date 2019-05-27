import static org.junit.jupiter.api.Assertions.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.lang.reflect.Array;
import java.util.*;


public class CITS2200ProjectTester {
    public static void loadGraph(CITS2200Project project, String path) {
        // The graph is in the following format:
        // Every pair of consecutive lines represent a directed edge.
        // The edge goes from the URL in the first line to the URL in the second line.
        try {
            BufferedReader reader = new BufferedReader(new FileReader(path));
            while (reader.ready()) {
                String from = reader.readLine();
                String to = reader.readLine();
                //System.out.println("Adding edge from " + from + " to " + to);
                project.addEdge(from, to);
            }
        } catch (Exception e) {
            System.out.println("There was a problem:");
            System.out.println(e.toString());
        }
    }


    public static void main(String[] args) {
        // Change this to be the path to the graph file.
        //String pathToGraphFile = "exampleGraphs/example_graph.txt";
        //String pathToGraphFile = "exampleGraphs/medium_graph.txt";
        //String pathToGraphFile = "exampleGraphs/small_graph";
        String pathToGraphFile = "exampleGraphs/Hamil_20nodes.txt";
        // Create an instance of your implementation.
        CITS2200Project proj = new MyCITS2200Project();
        // Load the graph into the project.
        loadGraph(proj, pathToGraphFile);


        /*String urlFrom = "/wiki/Minimum-cost_flow_problem";
        String urlTo = "/wiki/Flow_network";
        System.out.println("THe Shortest path is: " + proj.getShortestPath(urlFrom,urlTo));
        proj.getStronglyConnectedComponents();
        System.out.println("Set of vertices the center is made up of: \n" + Arrays.toString(proj.getCenters()));
        System.out.println("from one to itself: " + proj.getShortestPath("3","3"));
        System.out.println("from one to itself: " + proj.getShortestPath("1234","1043"));*/
/*        File file = new File("/Users/ahbar.sakib/Desktop/sample.txt");
        try {
            BufferedReader in = new BufferedReader(new FileReader(file));
            while (in.ready()) {
                String [] line = in.readLine().split("\\s+");
                int result = proj.getShortestPath(line[3], line[5]);
                System.out.println("Shortest Path from " + line[3] + " to "+ line[5] +" = " + result);
                assertEquals(Integer.valueOf(line[line.length-1]),result);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }*/
        System.out.println("\nHamiltonian Path: " + Arrays.toString(proj.getHamiltonianPath()));
        //System.out.println("get Centers: " + Arrays.toString(proj.getCenters()));
        //System.out.println("SCC: " + Arrays.deepToString(proj.getStronglyConnectedComponents()));

        String temp = "/wiki/Flow_network,/wiki/Circulation_problem,/wiki/Multi-commodity_flow_problem,/wiki/Maximum_flow_problem,/wiki/Dinic%27s_algorithm,/wiki/Edmonds%E2%80%93Karp_algorithm,/wiki/Ford%E2%80%93Fulkerson_algorithm,/wiki/Minimum-cost_flow_problem,/wiki/Network_simplex_algorithm,/wiki/Out-of-kilter_algorithm,/wiki/Max-flow_min-cut_theorem,/wiki/Push%E2%80%93relabel_maximum_flow_algorithm,/wiki/Gomory%E2%80%93Hu_tree,/wiki/Minimum_cut,/wiki/Approximate_max-flow_min-cut_theorem ";
        String temp1 = "/wiki/Minimum_cut,/wiki/Gomory%E2%80%93Hu_tree,/wiki/Multi-commodity_flow_problem,/wiki/Circulation_problem,/wiki/Network_simplex_algorithm,/wiki/Out-of-kilter_algorithm,/wiki/Minimum-cost_flow_problem,/wiki/Maximum_flow_problem,/wiki/Push%E2%80%93relabel_maximum_flow_algorithm, /wiki/Max-flow_min-cut_theorem,/wiki/Edmonds%E2%80%93Karp_algorithm, /wiki/Dinic%27s_algorithm, /wiki/Ford%E2%80%93Fulkerson_algorithm,/wiki/Approximate_max-flow_min-cut_theorem,/wiki/Flow_network";


        String [] expSccResult = temp.split(",");
        //String [][] mySCC = proj.getStronglyConnectedComponents();

        //System.out.println(Arrays.toString(mySCC[1]));
        //System.out.println(compareArrays(expSccResult, mySCC[1]));
        //System.out.println(Arrays.equals(expSccResult, mySCC));


        //System.out.println(Arrays.deepToString(proj.getStronglyConnectedComponents()).replace("], ", "]\n").replace("[[", "[").replace("]]", "]"));
        // Write your own tests!
    }
    public static boolean compareArrays(String [] arr1, String[] arr2) {
        HashSet<String> set1 = new HashSet<>(Arrays.asList(arr1));
        HashSet<String> set2 = new HashSet<>(Arrays.asList(arr2));
        return set1.equals(set2);
    }
}

/*SCC: [[/wiki/Nowhere-zero_flow],
        [/wiki/Minimum_cut, /wiki/Gomory%E2%80%93Hu_tree, /wiki/Multi-commodity_flow_problem, /wiki/Circulation_problem,
        /wiki/Network_simplex_algorithm, /wiki/Out-of-kilter_algorithm, /wiki/Minimum-cost_flow_problem, /wiki/Maximum_flow_problem,
        /wiki/Push%E2%80%93relabel_maximum_flow_algorithm, /wiki/Max-flow_min-cut_theorem, /wiki/Edmonds%E2%80%93Karp_algorithm,
        /wiki/Dinic%27s_algorithm, /wiki/Ford%E2%80%93Fulkerson_algorithm, /wiki/Approximate_max-flow_min-cut_theorem, /wiki/Flow_network]
        [/wiki/Braess%27_paradox]]*/
