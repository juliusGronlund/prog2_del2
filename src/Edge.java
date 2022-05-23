import java.util.Objects;

public class Edge<T> {

    private final T destination;
    private final String name;
    private int weight;

    public Edge(T destination, String name, int weight) {
        this.destination = Objects.requireNonNull(destination);
        this.name = Objects.requireNonNull(name);

        if (weight < 0) {
            throw new IllegalArgumentException();
        }
        this.weight = weight;
    }

    public T getDestination() {
        return destination;
    }

    public String getName() {
        return name;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        if(weight < 0){
            throw new IllegalArgumentException();
        }
        this.weight = weight;
    }

    @Override
    public String toString() {
        return "till " + destination + " med " + name + " tar " + weight;
    }
}
