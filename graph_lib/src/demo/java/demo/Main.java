package demo.java.demo;

import main.java.graph.*;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        System.out.println("demo project\n");

        testBasicGraph();
        testDirectedGraph();
        testAlgorithms();
        testRandomGraphs();
        testErrorCases();
    }

    private static void testBasicGraph() {
        System.out.println("1. BASIC GRAPH TEST");

        UndirectedGraph<String, Integer> graph = new UndirectedGraph<>();

        // Add vertices and edges
        graph.addVertex("A");
        graph.addVertex("B");
        graph.addVertex("C");
        graph.addEdge("A", "B", 5);
        graph.addEdge("B", "C", 3);

        System.out.println("Vertices: " + graph.getVertices());
        System.out.println("Edges: " + graph.getEdges());
        System.out.println("Vertex count: " + graph.getVertexCount());
        System.out.println("Edge count: " + graph.getEdgeCount());
        System.out.println("Neighbors of A: " + graph.getNeighbors("A"));
        System.out.println("Contains vertex A: " + graph.containsVertex("A"));
        System.out.println("Contains edge A-B: " + graph.containsEdge("A", "B"));
        System.out.println("Edge weight A-B: " + graph.getEdgeWeight("A", "B"));
    }

    private static void testDirectedGraph() {
        System.out.println("\n2. DIRECTED GRAPH TEST");

        DirectedGraph<String, Double> graph = new DirectedGraph<>();

        graph.addEdge("Start", "Process", 1.5);
        graph.addEdge("Process", "End", 2.0);
        graph.addEdge("Start", "Validate", 0.5);

        System.out.println("Edges: " + graph.getEdges());
        System.out.println("Is directed: " + graph.isDirected());
        System.out.println("Neighbors of Start: " + graph.getNeighbors("Start"));
        System.out.println("Incoming neighbors of End: " + graph.getIncomingNeighbors("End"));
        System.out.println("Out degree of Start: " + graph.getOutDegree("Start"));
        System.out.println("In degree of End: " + graph.getInDegree("End"));
    }

    private static void testAlgorithms() {
        System.out.println("\n3. ALGORITHMS TEST");

        UndirectedGraph<Integer, Double> graph = new UndirectedGraph<>();

        graph.addEdge(1, 2, 1.0);
        graph.addEdge(1, 3, 1.0);
        graph.addEdge(2, 4, 1.0);
        graph.addEdge(3, 4, 1.0);
        graph.addEdge(4, 5, 1.0);

        System.out.println("Graph: " + graph.getVertices() + " " + graph.getEdges());

        // DFS and BFS
        java.util.List<Integer> dfs = GraphAlgorithms.dfs(graph, 1);
        java.util.List<Integer> bfs = GraphAlgorithms.bfs(graph, 1);

        System.out.println("DFS from 1: " + dfs);
        System.out.println("BFS from 1: " + bfs);

        // Connectivity
        boolean connected = GraphAlgorithms.isConnected(graph);
        System.out.println("Is connected: " + connected);

        // Dijkstra
        UndirectedGraph<Integer, Double> weightedGraph = new UndirectedGraph<>();
        weightedGraph.addEdge(1, 2, 5.0);
        weightedGraph.addEdge(1, 3, 3.0);
        weightedGraph.addEdge(2, 4, 2.0);
        weightedGraph.addEdge(3, 4, 7.0);

        java.util.Map<Integer, Double> distances = GraphAlgorithms.dijkstra(weightedGraph, 1);
        System.out.println("Dijkstra from 1:");
        for (java.util.Map.Entry<Integer, Double> entry : distances.entrySet()) {
            String dist = entry.getValue() == Double.MAX_VALUE ? "∞" : String.format("%.1f", entry.getValue());
            System.out.println("  to " + entry.getKey() + ": " + dist);
        }
    }

    private static void testRandomGraphs() {
        System.out.println("\n4. RANDOM GRAPHS TEST");

        GraphGenerator generator = new GraphGenerator();

        try {
            // Generate random graphs
            Graph<Integer, Integer> randomUndirected = generator.generateRandomGraph(
                    UndirectedGraph.class, 6, 0.4, 1);

            Graph<Integer, Double> randomDirected = generator.generateRandomGraph(
                    DirectedGraph.class, 5, 0.6, 1.0);

            System.out.println("Random undirected graph:");
            System.out.println("  Vertices: " + randomUndirected.getVertices());
            System.out.println("  Edges: " + randomUndirected.getEdges().size());

            System.out.println("Random directed graph:");
            System.out.println("  Vertices: " + randomDirected.getVertices());
            System.out.println("  Edges: " + randomDirected.getEdges().size());
            System.out.println("  Is directed: " + randomDirected.isDirected());

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void testErrorCases() {
        System.out.println("\n5. ERROR CASES TEST");

        // Empty graph
        UndirectedGraph<String, Integer> emptyGraph = new UndirectedGraph<>();
        System.out.println("Empty graph vertices: " + emptyGraph.getVertices());
        System.out.println("Empty graph edges: " + emptyGraph.getEdges());

        // Non-existent vertex
        UndirectedGraph<Integer, Integer> graph = new UndirectedGraph<>();
        graph.addVertex(1);
        System.out.println("Neighbors of non-existent 999: " + graph.getNeighbors(999));

        // Remove operations
        graph.addEdge(1, 2, 10);
        graph.addEdge(2, 3, 20);
        System.out.println("Before removal: " + graph.getEdges());

        graph.removeEdge(1, 2);
        System.out.println("After removing edge 1-2: " + graph.getEdges());

        graph.removeVertex(2);
        System.out.println("After removing vertex 2: " + graph.getVertices());
    }
}