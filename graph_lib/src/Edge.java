// Edge.java
import java.util.Objects;

public class Edge<V, E> {
    private final V source;
    private final V target;
    private final E weight;
    private final boolean directed;

    public Edge(V source, V target, E weight, boolean directed) {
        this.source = source;
        this.target = target;
        this.weight = weight;
        this.directed = directed;
    }

    public V getSource() { return source; }
    public V getTarget() { return target; }
    public E getWeight() { return weight; }
    public boolean isDirected() { return directed; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Edge)) return false;
        Edge<?, ?> edge = (Edge<?, ?>) o;
        return directed == edge.directed &&
                Objects.equals(source, edge.source) &&
                Objects.equals(target, edge.target);
    }

    @Override
    public int hashCode() {
        return Objects.hash(source, target, directed);
    }

    @Override
    public String toString() {
        String arrow = directed ? " -> " : " - ";
        return source + arrow + target + " (" + weight + ")";
    }
}