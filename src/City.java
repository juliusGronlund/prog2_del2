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

    /*
    public boolean equals(Object other) {
        if (other instanceof City city) {
            return name.equals(city.name);
        }
        return false;
    }
     */

    /*
    public int hashCode() {
        return Objects.hash(name);
    }
     */

    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }

        if (o.getClass() != this.getClass()) {
            return false;
        }

        final City other = (City) o;
        if ((this.name == null) ? (other.name != null) : !this.name.equals(other.name)) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 53 * hash + (this.name != null ? this.name.hashCode() : 0);
        return hash;
    }
}