// UndirectedGraph.java
import java.util.*;

public class UndirectedGraph<V, E> implements Graph<V, E> {
    private final Map<V, Map<V, E>> adjacencyMap;
    private final E defaultWeight;

    public UndirectedGraph(E defaultWeight) {
        this.adjacencyMap = new HashMap<>();
        this.defaultWeight = defaultWeight;
    }

    public UndirectedGraph() {
        this(null);
    }

    @Override
    public void addVertex(V vertex) {
        adjacencyMap.putIfAbsent(vertex, new HashMap<>());
    }

    @Override
    public void removeVertex(V vertex) {
        if (!adjacencyMap.containsKey(vertex)) return;

        // Удаляем все связи с этой вершиной
        for (V neighbor : getNeighbors(vertex)) {
            adjacencyMap.get(neighbor).remove(vertex);
        }
        adjacencyMap.remove(vertex);
    }

    @Override
    public void addEdge(V source, V target, E weight) {
        addVertex(source);
        addVertex(target);
        E actualWeight = weight != null ? weight : defaultWeight;
        adjacencyMap.get(source).put(target, actualWeight);
        adjacencyMap.get(target).put(source, actualWeight);
    }

    @Override
    public void removeEdge(V source, V target) {
        if (adjacencyMap.containsKey(source)) {
            adjacencyMap.get(source).remove(target);
        }
        if (adjacencyMap.containsKey(target)) {
            adjacencyMap.get(target).remove(source);
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
        Set<String> added = new HashSet<>();

        for (V source : adjacencyMap.keySet()) {
            for (V target : adjacencyMap.get(source).keySet()) {
                // Чтобы избежать дублирования ребер
                String edgeKey = generateEdgeKey(source, target);
                if (!added.contains(edgeKey)) {
                    edges.add(new Edge<>(source, target,
                            adjacencyMap.get(source).get(target), false));
                    added.add(edgeKey);
                }
            }
        }
        return edges;
    }

    private String generateEdgeKey(V a, V b) {
        return a.hashCode() <= b.hashCode() ?
                a + "-" + b : b + "-" + a;
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
        return getEdges().size();
    }

    @Override
    public boolean isDirected() {
        return false;
    }

    public int getDegree(V vertex) {
        return getNeighbors(vertex).size();
    }
}