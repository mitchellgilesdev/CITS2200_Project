import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Arrays;

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
                System.out.println("Adding edge from " + from + " to " + to);
                project.addEdge(from, to);
            }
        } catch (Exception e) {
            System.out.println("There was a problem:");
            System.out.println(e.toString());
        }
    }


    public static void main(String[] args) {
        // Change this to be the path to the graph file.
        String pathToGraphFile = "exampleGraphs/small_graph";
        // Create an instance of your implementation.
        CITS2200Project proj = new MyCITS2200Project();
        // Load the graph into the project.
        loadGraph(proj, pathToGraphFile);

/*
        String urlFrom = "/wiki/Minimum-cost_flow_problem";
        String urlTo = "/wiki/Flow_network";
        System.out.println("THe Shortest path is: " + proj.getShortestPath(urlFrom,urlTo));
        proj.getStronglyConnectedComponents();
        System.out.println("Set of vertices the center is made up of: \n" + Arrays.toString(proj.getCenters()));
        System.out.println("from one to itself: " + proj.getShortestPath("3","3"));
        System.out.println("from one to itself: " + proj.getShortestPath("1234","1043"));
*/

        System.out.println("\nHamiltonian Path: " + Arrays.toString(proj.getHamiltonianPath()));
        System.out.println( "get Centers: " + Arrays.toString(proj.getCenters()));
        System.out.println( "SCC: " +  Arrays.deepToString(proj.getStronglyConnectedComponents()));
        // Write your own tests!
    }

}