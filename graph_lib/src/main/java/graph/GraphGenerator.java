package main.java.graph;
import java.util.*;

public class GraphGenerator {
    private Random random = new Random();


    @SuppressWarnings("unchecked")
    private <V, E> Graph<V, E> createGraph(Class<? extends Graph> graphClass, E defaultWeight) throws Exception {
        if (graphClass == DirectedGraph.class) {
            return (Graph<V, E>) new DirectedGraph<V, E>(defaultWeight);
        } else if (graphClass == UndirectedGraph.class) {
            return (Graph<V, E>) new UndirectedGraph<V, E>(defaultWeight);
        } else {
            throw new IllegalArgumentException("Unsupported graph class: " + graphClass);
        }
    }

    public <V, E> Graph<V, E> generateRandomGraph(
            Class<? extends Graph> graphClass,
            int vertexCount,
            double edgeProbability,
            E defaultWeight) throws Exception {

        Graph<V, E> graph = createGraph(graphClass, defaultWeight);


        for (int i = 0; i < vertexCount; i++) {
            graph.addVertex((V) Integer.valueOf(i));
        }


        List<V> vertices = new ArrayList<>(graph.getVertices());
        for (int i = 0; i < vertices.size(); i++) {
            for (int j = 0; j < vertices.size(); j++) {
                if (i != j && random.nextDouble() < edgeProbability) {
                    graph.addEdge(vertices.get(i), vertices.get(j), defaultWeight);
                }
            }
        }

        return graph;
    }

    public <V, E> Graph<V, E> generateConnectedGraph(
            Class<? extends Graph> graphClass,
            int vertexCount,
            double additionalEdgeProbability,
            E defaultWeight) throws Exception {

        Graph<V, E> graph = createGraph(graphClass, defaultWeight);

        if (vertexCount == 0) return graph;


        for (int i = 0; i < vertexCount; i++) {
            graph.addVertex((V) Integer.valueOf(i));
        }

        List<V> vertices = new ArrayList<>(graph.getVertices());


        for (int i = 1; i < vertices.size(); i++) {
            int parent = random.nextInt(i);
            graph.addEdge(vertices.get(parent), vertices.get(i), defaultWeight);
            if (!graph.isDirected()) {
                graph.addEdge(vertices.get(i), vertices.get(parent), defaultWeight);
            }
        }


        for (int i = 0; i < vertices.size(); i++) {
            for (int j = 0; j < vertices.size(); j++) {
                if (i != j && !graph.containsEdge(vertices.get(i), vertices.get(j))
                        && random.nextDouble() < additionalEdgeProbability) {
                    graph.addEdge(vertices.get(i), vertices.get(j), defaultWeight);
                }
            }
        }

        return graph;
    }

    public DirectedGraph<Integer, Double> generateDirectedGraph(int vertexCount, double edgeProbability) throws Exception {
        DirectedGraph<Integer, Double> graph = new DirectedGraph<>(1.0);

        for (int i = 0; i < vertexCount; i++) {
            graph.addVertex(i);
        }

        Random random = new Random();
        for (int i = 0; i < vertexCount; i++) {
            for (int j = 0; j < vertexCount; j++) {
                if (i != j && random.nextDouble() < edgeProbability) {
                    graph.addEdge(i, j, random.nextDouble() * 10.0);
                }
            }
        }

        return graph;
    }

    public UndirectedGraph<Integer, Integer> generateUndirectedGraph(int vertexCount, double edgeProbability) throws Exception {
        UndirectedGraph<Integer, Integer> graph = new UndirectedGraph<>(1);

        for (int i = 0; i < vertexCount; i++) {
            graph.addVertex(i);
        }

        Random random = new Random();
        for (int i = 0; i < vertexCount; i++) {
            for (int j = i + 1; j < vertexCount; j++) {
                if (random.nextDouble() < edgeProbability) {
                    graph.addEdge(i, j, random.nextInt(10) + 1);
                }
            }
        }

        return graph;
    }
}