// GraphAlgorithms.java
import java.util.*;

public class GraphAlgorithms {



    public static <V, E> List<V> dfs(Graph<V, E> graph, V start) {
        List<V> result = new ArrayList<>();
        Set<V> visited = new HashSet<>();
        dfsRecursive(graph, start, visited, result);
        return result;
    }

    private static <V, E> void dfsRecursive(Graph<V, E> graph, V current,
                                            Set<V> visited, List<V> result) {
        if (!visited.contains(current)) {
            visited.add(current);
            result.add(current);
            for (V neighbor : graph.getNeighbors(current)) {
                dfsRecursive(graph, neighbor, visited, result);
            }
        }
    }

    public static <V, E> List<V> bfs(Graph<V, E> graph, V start) {
        List<V> result = new ArrayList<>();
        Set<V> visited = new HashSet<>();
        Queue<V> queue = new LinkedList<>();

        queue.offer(start);
        visited.add(start);

        while (!queue.isEmpty()) {
            V current = queue.poll();
            result.add(current);

            for (V neighbor : graph.getNeighbors(current)) {
                if (!visited.contains(neighbor)) {
                    visited.add(neighbor);
                    queue.offer(neighbor);
                }
            }
        }

        return result;
    }

    public static <V> Map<V, Double> dijkstra(Graph<V, Double> graph, V start) {
        Map<V, Double> distances = new HashMap<>();
        PriorityQueue<VertexDistance<V>> pq = new PriorityQueue<>();
        Set<V> visited = new HashSet<>();

        for (V vertex : graph.getVertices()) {
            distances.put(vertex, Double.MAX_VALUE);
        }
        distances.put(start, 0.0);
        pq.offer(new VertexDistance<>(start, 0.0));

        while (!pq.isEmpty()) {
            VertexDistance<V> current = pq.poll();
            V currentVertex = current.vertex;

            if (visited.contains(currentVertex)) continue;
            visited.add(currentVertex);

            for (V neighbor : graph.getNeighbors(currentVertex)) {
                if (!visited.contains(neighbor)) {
                    double edgeWeight = graph.getEdgeWeight(currentVertex, neighbor);
                    double newDistance = distances.get(currentVertex) + edgeWeight;

                    if (newDistance < distances.get(neighbor)) {
                        distances.put(neighbor, newDistance);
                        pq.offer(new VertexDistance<>(neighbor, newDistance));
                    }
                }
            }
        }

        return distances;
    }

    public static <V, E> boolean isConnected(Graph<V, E> graph) {
        if (graph.getVertexCount() == 0) return true;

        Set<V> vertices = graph.getVertices();
        V start = vertices.iterator().next();
        List<V> visited = bfs(graph, start);

        return visited.size() == graph.getVertexCount();
    }

    public static <V, E> List<V> topologicalSort(DirectedGraph<V, E> graph) {
        List<V> result = new ArrayList<>();
        Set<V> visited = new HashSet<>();
        Set<V> temp = new HashSet<>();

        for (V vertex : graph.getVertices()) {
            if (!visited.contains(vertex)) {
                if (!topologicalSortDFS(graph, vertex, visited, temp, result)) {
                    throw new IllegalArgumentException("Graph contains cycles");
                }
            }
        }

        Collections.reverse(result);
        return result;
    }

    private static <V, E> boolean topologicalSortDFS(DirectedGraph<V, E> graph, V vertex,
                                                     Set<V> visited, Set<V> temp, List<V> result) {
        if (temp.contains(vertex)) return false; // Найден цикл
        if (visited.contains(vertex)) return true;

        temp.add(vertex);

        for (V neighbor : graph.getNeighbors(vertex)) {
            if (!topologicalSortDFS(graph, neighbor, visited, temp, result)) {
                return false;
            }
        }

        temp.remove(vertex);
        visited.add(vertex);
        result.add(vertex);

        return true;
    }

    private static class VertexDistance<V> implements Comparable<VertexDistance<V>> {
        V vertex;
        double distance;

        VertexDistance(V vertex, double distance) {
            this.vertex = vertex;
            this.distance = distance;
        }

        @Override
        public int compareTo(VertexDistance<V> other) {
            return Double.compare(this.distance, other.distance);
        }
    }
}