// Graph.java
import java.util.List;
import java.util.Set;

public interface Graph<V, E> {
    void addVertex(V vertex);
    void removeVertex(V vertex);
    void addEdge(V source, V target, E weight);
    void removeEdge(V source, V target);
    boolean containsVertex(V vertex);
    boolean containsEdge(V source, V target);
    Set<V> getVertices();
    Set<Edge<V, E>> getEdges();
    List<V> getNeighbors(V vertex);
    E getEdgeWeight(V source, V target);
    int getVertexCount();
    int getEdgeCount();
    boolean isDirected();
}