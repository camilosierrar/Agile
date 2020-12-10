package model;

import java.util.HashMap;
import java.util.List;

public class Plan {
    private HashMap<Long, Intersection> intersections;
    private List<Segment> segments;
    public static Plan plan;


    private Plan(HashMap<Long, Intersection> intersections, List<Segment> segments) {
        this.intersections = intersections;
        this.segments = segments;
    }

    public static Plan createPlan(HashMap<Long, Intersection> intersections, List<Segment> segments){
        plan = new Plan(intersections, segments);
        return plan;
    }

    public HashMap<Long, Intersection> getIntersections() {
        return intersections;
    }

    public List<Segment> getSegments() {
        return segments;
    }

    public Intersection getIntersectionById(long intersectionId) {
        return this.intersections.get(intersectionId);
    }

    @Override
    public String toString() {
        return "Map{" +
                "intersections=" + intersections +
                ", segments=" + segments +
                '}';
    }
}
