package graph;


/** A class that represents a graph where nodes are cities (CityNode).
 * The cost of each edge connecting two cities is the distance between the cities.
 * Fill in code in this class. You may add additional methods and variables.
 */

import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLOutput;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Graph {
    public final int EPS_DIST = 5;
    private int numNodes;     // total number of nodes
    private int numEdges; // total number of edges
    private CityNode[] nodes; // array of nodes of the graph
    private Edge[] adjacencyList; // adjacency list; for each vertex stores a linked list of edges
    private Map<String, Integer> labelsToIndices; // a HashMap that maps each city to the corresponding node id  // like san francisco,0 / LA, 1

    public Edge[] getAdjacencyList() {
        return adjacencyList;
    }

    /**
     * Read graph info from the given file, and create nodes and edges of
     * the graph.
     * first read the first line of the file and if it was NODES, then add nodes to the graph
     * after that we have the number of the nodes
     * and then check if the line is for ARCS
     * if so we read adges and add them to the graph
     * @param filename name of the file that has nodes and edges
     */
    public void loadGraph(String filename) {//after adding the edge from sf to LA, then add edge from LA to SF
        // FILL IN CODE
        labelsToIndices=new HashMap<>();
        try( BufferedReader bf= new BufferedReader(new FileReader(filename))){
           String line= bf.readLine();
           if(line.equals("NODES")){
               int len=Integer.parseInt(bf.readLine());
               nodes= new CityNode[len];
               for(int i=0;i<len;i++){
                    String[] info=bf.readLine().split(" ");
                    CityNode node= new CityNode(info[0],Double.parseDouble(info[1]),Double.parseDouble(info[2]));
                   labelsToIndices.put(info[0],i);
                    addNode(node);
               }
               line=bf.readLine();
           }
            adjacencyList=new Edge[numNodes];
           if(line.equals("ARCS")){
               line=bf.readLine();
               while (line!=null){
                   String[] info=line.split(" ");
                   int id = labelsToIndices.get(info[0]);
                   int neighborId=labelsToIndices.get(info[1]);
                   Edge ed= new Edge(neighborId);
                   ed.setCost(Integer.parseInt(info[2]));
                   Edge ed2= new Edge(id);
                   ed2.setCost(Integer.parseInt(info[2]));
                   addEdge(id,ed);
                   addEdge(neighborId,ed2);
                   line=bf.readLine();
               }
           }
       }
       catch (IOException e){
           System.err.println(e);
       }

    }

    /**
     * Add a node to the array of nodes.
     * Increment numNodes variable.
     * Called from loadGraph.
     * @param node a CityNode to add to the graph
     */
    public void addNode(CityNode node) {
        // FILL IN CODE
        nodes[numNodes] = node;
        numNodes++;
    }

    /**
     * Return the number of nodes in the graph
     * @return number of nodes
     */
    public int numNodes() {
        return numNodes;
    }

    /**
     * Adds the edge to the linked list for the given nodeId
     * Called from loadGraph.
     * this method adds to the beginning of the linked list
     * and increment the number of edges
     * @param nodeId id of the node
     * @param edge edge to add
     */
    public void addEdge(int nodeId, Edge edge) {
        // FILL IN CODE
        Edge head= adjacencyList[nodeId];
        adjacencyList[nodeId]=edge;
        if (head != null) {
            edge.setNext(head);
        }
        numEdges++;
    }

    /**
     * Returns an integer id of the given city node by using the labelsToIndices
     * @param city node of the graph
     * @return its integer id
     */
    public int getId(CityNode city) {
        Integer id = labelsToIndices.get(city.getCity());
        if (id != null) {
            return id;
        } else {
            return -1;
        }
    }


    /**
     * Return the edges of the graph as a 2D array of points.
     * Called from GUIApp to display the edges of the graph.
     *
     * @return a 2D array of Points.
     * For each edge, we store an array of two Points, v1 and v2.
     * v1 is the source vertex for this edge, v2 is the destination vertex.
     * This info can be obtained from the adjacency list
     * we get the location of the current city and its neighbors and store them in the 2 dimension array
     */
    public Point[][] getEdges() {
        if (adjacencyList == null || adjacencyList.length == 0) {
            System.out.println("Adjacency list is empty. Load the graph first.");
            return null;
        }
        Point[][] edges2D = new Point[numEdges][2];
        // FILL IN CODE
        Edge[] list = adjacencyList;
        int index=0;
        for(int i=0;i<adjacencyList.length;i++){
            Edge curr=list[i];
            CityNode city=getNode(i);
            while (curr!=null && index<numEdges){
                CityNode neighbor=getNode(curr.getNeighbor());
                edges2D[index][0]=city.getLocation();
                edges2D[index][1]=neighbor.getLocation();
                curr=curr.getNext();
                index++;
            }
        }
        return edges2D;
    }

    /**
     * Get the nodes of the graph as a 1D array of Points.
     * Used in GUIApp to display the nodes of the graph.
     * @return a list of Points that correspond to nodes of the graph.
     */
    public Point[] getNodes() {
        if (nodes == null) {
             System.out.println("Array of nodes is empty. Load the graph first.");
            return null;
        }
        Point[] nodes = new Point[this.nodes.length];
        for (int i = 0; i < nodes.length; i++) {
            nodes[i] = this.nodes[i].getLocation();
        }
        return nodes;
    }

    /**
     * Used in GUIApp to display the names of the airports.
     * @return the list that contains the names of cities (that correspond
     * to the nodes of the graph)
     */
    public String[] getCities() {
        if (this.nodes == null) {
            System.out.println("Graph has no nodes. Write loadGraph method first. ");
            return null;
        }
        String[] labels = new String[nodes.length];
        for (int i = 0; i < nodes.length; i++) {
            labels[i] = nodes[i].getCity();
        }
        return labels;
    }

    /** Take a list of node ids on the path and return an array where each
     * element contains two points (an edge between two consecutive nodes)
     * we iterate over city ids that are stored in the pathOfNodes and get tbe location of them
     * store them in the array and retutn it
     * @param pathOfNodes A list of node ids on the path
     * @return array where each element is an array of 2 points
     */
    public Point[][] getPath(List<Integer> pathOfNodes) {
        if(pathOfNodes==null){
            System.out.println("there are no nods on the path");
            return null;
        }
        Point[][] edges2D = new Point[pathOfNodes.size() - 1][2];
        // Each "edge" is an array of size two (one Point is origin, one Point is destination)
        // FILL IN CODE
        for (int i = 0; i < pathOfNodes.size()-1 ; i++) {
            int nodeId1 = pathOfNodes.get(i);
            int nodeId2 = pathOfNodes.get(i+1 );
            CityNode node1 = getNode(nodeId1);
            CityNode node2 = getNode(nodeId2);
            edges2D[i][0] = node1.getLocation();
            edges2D[i][1] = node2.getLocation();
        }
        return edges2D;
    }

    /**
     * Return the CityNode for the given nodeId
     * @param nodeId id of the node
     * @return CityNode
     */
    public CityNode getNode(int nodeId) {
        return nodes[nodeId];
    }
    public Edge getfirstEdge(int id){
        return adjacencyList[id];
    }

    /**
     * Take the location of the mouse click as a parameter, and return the node
     * of the graph at this location. Needed in GUIApp class. No need to modify.
     * @param loc the location of the mouse click
     * @return reference to the corresponding CityNode
     */
    public CityNode getNode(Point loc) {
        if (nodes == null) {
            System.out.println("No node at this location. ");
            return null;
        }
        for (CityNode v : nodes) {
            Point p = v.getLocation();
            if ((Math.abs(loc.x - p.x) < EPS_DIST) && (Math.abs(loc.y - p.y) < EPS_DIST))
                return v;
        }
        return null;
    }

}