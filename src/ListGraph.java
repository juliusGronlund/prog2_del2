import java.util.*;

public class ListGraph<T> implements Graph<T> {
    private final Map<T, Set<Edge<T>>> nodes = new HashMap<>();

    @Override
    public void add(T node) {
        nodes.putIfAbsent(node, new HashSet<>());
    }

    @Override
    public void connect(T node1, T node2, String name, int weight) {
        if(!nodes.containsKey(node1) || !nodes.containsKey(node2)){
            throw new NoSuchElementException();
        }

        if(weight < 0){
            throw new IllegalArgumentException();
        }

        if(getEdgeBetween(node1, node2) != null){
            throw new IllegalStateException();
        }

        Set<Edge<T>> node1Edges = nodes.get(node1);
        Set<Edge<T>> node2Edges = nodes.get(node2);

        node1Edges.add(new Edge<T>(node2, name, weight));
        node2Edges.add(new Edge<T>(node1, name, weight));
    }

    @Override
    public void setConnectionWeight(T node1, T node2, int weight) {
        if(!nodes.containsKey(node1) || !nodes.containsKey(node2) || getEdgeBetween(node1, node2) == null){
            throw new NoSuchElementException();
        }

        if(weight < 0){
            throw new IllegalArgumentException();
        }

        Edge<T> firstEdgeToChangeWeightOn = getEdgeBetween(node1, node2);
        Edge<T> secondEdgeToChangeWeightOn = getEdgeBetween(node2, node1);

        firstEdgeToChangeWeightOn.setWeight(weight);
        secondEdgeToChangeWeightOn.setWeight(weight);
    }

    @Override
    public Set getNodes() {
        return Set.copyOf(nodes.keySet());
    }

    @Override
    public Collection<Edge<T>> getEdgesFrom(T node) {
        if(!nodes.containsKey(node)){
            throw new NoSuchElementException();
        }
        Set<Edge<T>> edgesFromNode = nodes.get(node);
        return edgesFromNode;
    }

    @Override
    public Edge<T> getEdgeBetween(T node1, T node2) {
        if(!nodes.containsKey(node1) || !nodes.containsKey(node2)){
            throw new NoSuchElementException();
        }
        for (Edge<T> edge : nodes.get(node1)) {
            if(edge.getDestination().equals(node2)) {
                return edge;
            }
        }
        return null;
    }

    @Override
    public void disconnect(T node1, T node2) {
        if(!nodes.containsKey(node1) || !nodes.containsKey(node2)){
            throw new NoSuchElementException();
        }

        if(getEdgeBetween(node1, node2) == null){
            throw new IllegalStateException();
        }

        Edge<T> firstEdgeBetween = getEdgeBetween(node1, node2);
        Edge<T> secondEdgeBetween = getEdgeBetween(node2, node1);

        nodes.get(node1).remove(firstEdgeBetween);
        nodes.get(node2).remove(secondEdgeBetween);
        // Dubbelchecka med G40
    }

    @Override
    public void remove(T nodeToRemove) {
        if(!nodes.containsKey(nodeToRemove)){
            throw new NoSuchElementException();
        }
        Set<Edge<T>> edgesFromOriginalNode = nodes.get(nodeToRemove);
        for (Edge<T> edgeFromOriginalNode : edgesFromOriginalNode) {
            T destinationFromOriginalNode = edgeFromOriginalNode.getDestination();
            Set<Edge<T>> edgesFromDestination = nodes.get(destinationFromOriginalNode);
            for (Edge<T> returningEdge : edgesFromDestination) {
                if(returningEdge.getDestination().equals(nodeToRemove)){
                    edgesFromDestination.remove(returningEdge);
                    break;
                }
            }
        }
        nodes.remove(nodeToRemove);
    }

    @Override
    public boolean pathExists(T from, T to) {
        if(!nodes.containsKey(from) || !nodes.containsKey(to)){
            return false;
        }
        return getPath(from, to) != null;
    }

    @Override
    public List<Edge<T>> getPath(T from, T to) {
        Map<T, T> connection = new HashMap<>();
        depthFirstConnection(from, null, connection);
        if(!connection.containsKey(to)){
            return null;
        }
        return gatherPath(from, to, connection);
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        for(T node : nodes.keySet()) {
            sb.append(node).append(": ").append(nodes.get(node)).append("\n");
        }
        return sb.toString();
    }

    // Hjälpmetoder
    private void depthFirstConnection(T to, T from, Map<T, T> connection){
        connection.put(to, from);
        for(Edge<T> edge : nodes.get(to)){
            if(!connection.containsKey(edge.getDestination())){
                depthFirstConnection(edge.getDestination(), to, connection);
            }
        }
    }

    private List<Edge<T>> gatherPath(T from, T to, Map<T, T> connection) {
        LinkedList<Edge<T>> path = new LinkedList<>();
        T current = to;
        while (!current.equals(from)) {
            T next = connection.get(current);
            Edge<T> edge = getEdgeBetween(next, current);
            path.addFirst(edge);
            current = next;
        }
        return Collections.unmodifiableList(path);
    }
}
