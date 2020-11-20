package model;

import java.util.HashMap;
import java.util.List;

public class Plan {
    HashMap<Long, Intersection> intersections;
    List<Segment> segments;


    public Plan(HashMap<Long, Intersection> intersections, List<Segment> segments) {
        this.intersections = intersections;
        this.segments = segments;
    }

    public HashMap<Long, Intersection> getIntersections() {
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