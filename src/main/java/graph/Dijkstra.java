package graph;

/** Class Dijkstra. Implementation of Dijkstra's algorithm for finding the shortest path
 * between the source vertex and other vertices in the graph.
 *  Fill in code. It is ok to add additional helper methods / classes.
 *  To get full credit, must add a class representing a Priority queue.
 *  You can still get 90% if you do not use a priority queue.
 */

import java.awt.*;
import java.sql.Array;
import java.util.*;
import java.util.List;

public class Dijkstra {
    private Graph graph; // stores the graph of CityNode-s and edges connecting them
    private List<Integer> shortestPath = null; // nodes that are part of the shortest path
    private Node[] table;
    public class Node{
        private int path;
        private int distance;
        //private boolean known;
        public Node(){
            this.path=-1;
            this.distance=Integer.MAX_VALUE;
           // this.known=false;
        }
        public Node(int distance, int path) {
            this.path = path;
            this.distance = distance;
        }
        public int getDistance() {
            return distance;
        }

        public void setDistance(int distance) {
            this.distance = distance;
        }

        public void setPath(int path) {
            this.path = path;
        }
    }
    /** Constructor
     *
     * @param filename name of the file that contains info about nodes and edges
     * @param graph graph
     */
    public Dijkstra(String filename, Graph graph) {
        this.graph = graph;
        graph.loadGraph(filename);
        this.table=new Node[graph.numNodes()];
    }

    /**
     * Returns the shortest path between the origin vertex and the destination vertex.
     * The result is stored in shortestPathEdges.
     * This function is called from GUIApp, when the user clicks on two cities.
     * @param origin source node
     * @param destination destination node
     * @return the ArrayList of nodeIds (of nodes on the shortest path)
     */
    public List<Integer> computeShortestPath(CityNode origin, CityNode destination) {
        // FILL IN CODE
        // Create and initialize Dijkstra's table
        // Initialize the table and the heap
        int totalCost=0;
        MinHeap heap=new MinHeap(graph.numNodes()+1);
        int originId=graph.getId(origin);
        int destinationId=graph.getId(destination);
        //source vertex
        table[originId]=new Node(0,-1);
        heap.insert(originId,0);
        for (int i=0 ; i<table.length;i++){
            if(i!=originId){
                table[i]=new Node();
                heap.insert(i,Integer.MAX_VALUE);
            }
        }
        // Run Dijkstra
        while (!heap.empty()) {
            int smallestNodeId = heap.removeMin();
            Edge edge = graph.getfirstEdge(smallestNodeId);
            while (edge != null) {
                int neighborId = edge.getNeighbor();
                if (heap.getPositions()[neighborId] <= heap.getSize()) {//finalized?
                    int newDistance = table[smallestNodeId].getDistance() + edge.getCost();
                    if (newDistance < table[neighborId].getDistance()) {
                        //update the table
                        table[neighborId].setDistance(newDistance);
                        //update the path
                        table[neighborId].setPath(smallestNodeId);
                        //updating the min heap
                        heap.reduceKey(neighborId, newDistance);
                    }
                }
                edge = edge.getNext();
            }
        }
        // Compute the nodes on the shortest path by "backtracking" using the table
        // The result should be in an instance variable called "shortestPath" and
        // should also be returned by the method
        shortestPath = new ArrayList<>();
        int currentNodeId = destinationId;
        while (currentNodeId != -1 && currentNodeId != originId) {
            shortestPath.add(0, currentNodeId);
            int previousNodeId = table[currentNodeId].path;
            if (previousNodeId != -1) {
                Edge edge = graph.getfirstEdge(previousNodeId);
                while (edge != null) {
                    if (edge.getNeighbor() == currentNodeId) {
                        totalCost += edge.getCost(); // Accumulate edge cost
                        break;
                    }
                    edge = edge.getNext();
                }
            }
            currentNodeId = previousNodeId;
        }
        if (currentNodeId == originId) {
            shortestPath.add(0, originId);
        }
        // Print the cost of the shortest path
        System.out.println("the cost for the shortest path is "+totalCost);
        return shortestPath;
    }

    /**
     * Return the shortest path as a 2D array of Points.
     * Each element in the array is another array that has 2 Points:
     * these two points define the beginning and end of a line segment.
     * @return 2D array of points
     */

    public Point[][] getPath() {
        if (shortestPath == null)
            return null;
        return graph.getPath(shortestPath); // delegating this task to the Graph class
    }

    /** Set the shortestPath to null.
     *  Called when the user presses Reset button.
     */
    public void resetPath() {
        shortestPath = null;
    }

}