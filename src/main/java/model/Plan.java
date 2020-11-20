package model;

import java.util.HashMap;

public class Plan {
    HashMap<Long, Intersection> intersections;
    HashMap<Long, Segment> segments;


    public Plan(HashMap<Long, Intersection> intersections, HashMap<Long, Segment> segments) {
        this.intersections = intersections;
        this.segments = segments;
    }

    public HashMap<Long, Intersection> getIntersections() {
        return intersections;
    }

    public HashMap<Long, Segment> getSegments() {
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
