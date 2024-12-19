# Dijkstra's Algorithm: Shortest Path Calculator

## Overview

This project implements Dijkstra's Algorithm to compute the shortest path between nodes in a graph. The nodes represent major U.S. cities, and the edges (roads) connect these cities with specified costs. The application includes a graphical user interface (GUI) to visualize the map and interactively compute paths by clicking on cities.

### Features

* #### Graph Representation:

  * Cities and their connections are represented using a Graph object with an adjacency list.
  * A HashMap maps city names to unique integer IDs for efficient lookups.

* #### Shortest Path Calculation:

  * Implements Dijkstra’s Algorithm for pathfinding: Optimized version with a priority queue (min-heap).

* ####   GUI Integration:

  1. Displays a map of the U.S. with clickable cities.
  2. Visualizes the computed shortest path between two selected cities.
  3. Includes a reset button to clear the current selection.

### Project Files

* #### Input Data:

    * USA.txt: Contains city names, coordinates, and edge costs.
 
    * Cities are stored as nodes, and roads are stored as bidirectional edges.
* #### Graph Implementation:

    * Graph class manages nodes, adjacency lists, and data loading.
    * Includes helper methods for accessing nodes, edges, and shortest paths.

* #### Shortest Path Algorithm:

  * Dijkstra class computes shortest paths using priority queue (min-heap) for improved performance on sparse graphs.

###   Technologies

*   Programming Language: Java
*   GUI Framework: Java Swing 
* Data Structures:

    * Adjacency list for graph representation.
    * Min-heap for optimized pathfinding.

###   Credits

  Developed as part of CS245 at the University of San Francisco.