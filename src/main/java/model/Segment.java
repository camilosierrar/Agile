package model;
/**
 * The class representing a segment
 */
public class Segment {
    /**
     * The origin of the segment
     */
    private Intersection origin;
    /**
     * The destination of the segment
     */
    private Intersection destination;
    /**
     * The name of the segment
     */
    private String name;
    /**
     * The length of the segment
     */
    double length;

    public Segment(Intersection origin, Intersection destination, String name, double length) {
        this.origin = origin;
        this.destination = destination;
        this.name = name;
        this.length = length;
    }

    public Intersection getOrigin() {
        return origin;
    }

    public Intersection getDestination() {
        return destination;
    }

    public String getName() {
        return name;
    }

    public double getLength() {
        return length;
    }

    @Override
    public String toString() {
        return "Segment{" +
                "origin=" + origin +
                ", destination=" + destination +
                ", name='" + name + '\'' +
                ", length=" + length +
                '}';
    }
}
