import java.util.Objects;

public class City {
    private final String name;
    private final double xCordinate;
    private final double yCordinate;

    public City(String name, double xCordinate, double yCordinate) {
        this.name = name;
        this.xCordinate = xCordinate;
        this.yCordinate = yCordinate;
    }

    public String getName() {
        return name;
    }

    public double getxCordinate() {
        return xCordinate;
    }

    public double getyCordinate() {
        return yCordinate;
    }

    public boolean equals(Object other) {
        if (other instanceof City city) {
            return name.equals(city.name);
        }
        return false;
    }

    public int hashCode() {
        return Objects.hash(name);
    }

    public String toString() {
        return name;
    }
}