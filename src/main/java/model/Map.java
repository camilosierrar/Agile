package model;

import java.util.List;

public class Map {
    List<Intersection> intersections;
    List<Segment> segments;


    public Map(List<Intersection> intersections, List<Segment> segments) {
        this.intersections = intersections;
        this.segments = segments;
    }

    public List<Intersection> getIntersections() {
        return intersections;
    }

    public List<Segment> getSegments() {
        return segments;
    }

    @Override
    public String toString() {
        return "Map{" +
                "intersections=" + intersections +
                ", segments=" + segments +
                '}';
    }
}
