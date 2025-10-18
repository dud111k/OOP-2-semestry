import java.util.List;
import java.util.Map;

// Main.java
public class Main {
    public static void main(String[] args) {
        try {
            GraphGenerator generator = new GraphGenerator();

            System.out.println("=== Способ 1: Специализированные методы ===");

            DirectedGraph<Integer, Double> directedGraph = generator.generateDirectedGraph(5, 0.4);
            System.out.println("Ориентированный граф:");
            printGraphInfo(directedGraph);

            UndirectedGraph<Integer, Integer> undirectedGraph = generator.generateUndirectedGraph(6, 0.3);
            System.out.println("\nНеориентированный граф:");
            printGraphInfo(undirectedGraph);

            System.out.println("\n=== Способ 2: Общий метод с приведением типов ===");

            @SuppressWarnings("unchecked")
            Graph<Integer, Double> directedGraph2 = generator.generateRandomGraph(
                    DirectedGraph.class, 4, 0.5, 1.0);

            System.out.println("Ориентированный граф (общий метод):");
            printGraphInfo(directedGraph2);

            testAlgorithms(directedGraph);
            testWeightedGraph();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static <V, E> void printGraphInfo(Graph<V, E> graph) {
        System.out.println("Тип: " + (graph.isDirected() ? "Ориентированный" : "Неориентированный"));
        System.out.println("Количество вершин: " + graph.getVertexCount());
        System.out.println("Количество рёбер: " + graph.getEdgeCount());
        System.out.println("Вершины: " + graph.getVertices());
        System.out.println("Рёбра: " + graph.getEdges());

        if (!graph.getVertices().isEmpty()) {
            V firstVertex = graph.getVertices().iterator().next();
            System.out.println("Соседи вершины " + firstVertex + ": " + graph.getNeighbors(firstVertex));
        }
    }

    private static void testAlgorithms(Graph<Integer, Double> graph) {
        System.out.println("\n=== Тестирование алгоритмов ===");

        if (!graph.getVertices().isEmpty()) {
            Integer startVertex = graph.getVertices().iterator().next();

            System.out.println("DFS от вершины " + startVertex + ": " +
                    GraphAlgorithms.dfs(graph, startVertex));

            System.out.println("BFS от вершины " + startVertex + ": " +
                    GraphAlgorithms.bfs(graph, startVertex));

            if (!graph.isDirected()) {
                System.out.println("Граф связный: " + GraphAlgorithms.isConnected(graph));
            }
        }
    }

    private static void testWeightedGraph() {
        System.out.println("\n=== Тестирование взвешенного графа ===");

        DirectedGraph<String, Double> weightedGraph = new DirectedGraph<>();

        // Добавляем вершины и рёбра с весами
        weightedGraph.addEdge("A", "B", 4.0);
        weightedGraph.addEdge("A", "C", 2.0);
        weightedGraph.addEdge("B", "C", 1.0);
        weightedGraph.addEdge("B", "D", 5.0);
        weightedGraph.addEdge("C", "D", 8.0);
        weightedGraph.addEdge("C", "E", 10.0);
        weightedGraph.addEdge("D", "E", 2.0);
        weightedGraph.addEdge("D", "F", 6.0);
        weightedGraph.addEdge("E", "F", 3.0);

        System.out.println("Взвешенный граф:");
        printGraphInfo(weightedGraph);

        System.out.println("\nАлгоритм Дейкстры от вершины A:");
        Map<String, Double> distances = GraphAlgorithms.dijkstra(weightedGraph, "A");
        for (Map.Entry<String, Double> entry : distances.entrySet()) {
            System.out.println("Расстояние до " + entry.getKey() + ": " +
                    (entry.getValue() == Double.MAX_VALUE ? "∞" : entry.getValue()));
        }
        testTopologicalSort();
    }

    private static void testTopologicalSort() {
        System.out.println("\n=== Тестирование топологической сортировки ===");

        DirectedGraph<String, Integer> dag = new DirectedGraph<>(); // Directed Acyclic Graph

        dag.addEdge("A", "B", 1);
        dag.addEdge("A", "C", 1);
        dag.addEdge("B", "D", 1);
        dag.addEdge("C", "D", 1);
        dag.addEdge("D", "E", 1);
        dag.addEdge("C", "F", 1);

        try {
            List<String> topologicalOrder = GraphAlgorithms.topologicalSort(dag);
            System.out.println("Топологический порядок: " + topologicalOrder);
        } catch (IllegalArgumentException e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }
}