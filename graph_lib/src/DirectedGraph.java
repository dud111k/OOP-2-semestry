// DirectedGraph.java
import java.util.*;
import java.util.stream.Collectors;

public class DirectedGraph<V, E> implements Graph<V, E> {
    private final Map<V, Map<V, E>> adjacencyMap;
    private final E defaultWeight;

    public DirectedGraph(E defaultWeight) {
        this.adjacencyMap = new HashMap<>();
        this.defaultWeight = defaultWeight;
    }

    public DirectedGraph() {
        this(null);
    }

    @Override
    public void addVertex(V vertex) {
        adjacencyMap.putIfAbsent(vertex, new HashMap<>());
    }

    @Override
    public void removeVertex(V vertex) {
        adjacencyMap.remove(vertex);
        for (Map<V, E> neighbors : adjacencyMap.values()) {
            neighbors.remove(vertex);
        }
    }

    @Override
    public void addEdge(V source, V target, E weight) {
        addVertex(source);
        addVertex(target);
        adjacencyMap.get(source).put(target, weight != null ? weight : defaultWeight);
    }

    @Override
    public void removeEdge(V source, V target) {
        if (adjacencyMap.containsKey(source)) {
            adjacencyMap.get(source).remove(target);
        }
    }

    @Override
    public boolean containsVertex(V vertex) {
        return adjacencyMap.containsKey(vertex);
    }

    @Override
    public boolean containsEdge(V source, V target) {
        return adjacencyMap.containsKey(source) &&
                adjacencyMap.get(source).containsKey(target);
    }

    @Override
    public Set<V> getVertices() {
        return Collections.unmodifiableSet(adjacencyMap.keySet());
    }

    @Override
    public Set<Edge<V, E>> getEdges() {
        Set<Edge<V, E>> edges = new HashSet<>();
        for (V source : adjacencyMap.keySet()) {
            for (V target : adjacencyMap.get(source).keySet()) {
                edges.add(new Edge<>(source, target,
                        adjacencyMap.get(source).get(target), true));
            }
        }
        return edges;
    }

    @Override
    public List<V> getNeighbors(V vertex) {
        if (!adjacencyMap.containsKey(vertex)) {
            return Collections.emptyList();
        }
        return new ArrayList<>(adjacencyMap.get(vertex).keySet());
    }

    @Override
    public E getEdgeWeight(V source, V target) {
        if (containsEdge(source, target)) {
            return adjacencyMap.get(source).get(target);
        }
        return null;
    }

    @Override
    public int getVertexCount() {
        return adjacencyMap.size();
    }

    @Override
    public int getEdgeCount() {
        return adjacencyMap.values().stream()
                .mapToInt(Map::size)
                .sum();
    }

    @Override
    public boolean isDirected() {
        return true;
    }

    public List<V> getIncomingNeighbors(V vertex) {
        List<V> incoming = new ArrayList<>();
        for (V source : adjacencyMap.keySet()) {
            if (adjacencyMap.get(source).containsKey(vertex)) {
                incoming.add(source);
            }
        }
        return incoming;
    }

    public int getInDegree(V vertex) {
        return getIncomingNeighbors(vertex).size();
    }

    public int getOutDegree(V vertex) {
        return getNeighbors(vertex).size();
    }
}