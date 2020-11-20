package model;

import java.util.HashMap;
import java.util.List;

public class Plan {
    static HashMap<Long, Intersection> intersections;
    List<Segment> segments;
    private static boolean created = false;
    public static Plan plan;


    private Plan(HashMap<Long, Intersection> intersections, List<Segment> segments) {
        this.intersections = intersections;
        this.segments = segments;
        Plan.created = true;
    }

    public static Plan createPlan(HashMap<Long, Intersection> intersections, List<Segment> segments){
        if(!created) {
            plan = new Plan(intersections, segments);
            return plan;
        }
        else{
            return plan;
        }
    }

    public HashMap<Long, Intersection> getIntersections() {
        return intersections;
    }

    public List<Segment> getSegments() {
        return segments;
    }

    public static Intersection getIntersectionById(long intersectionId) {
        return intersections.get(intersectionId);
    }

    @Override
    public String toString() {
        return "Map{" +
                "intersections=" + intersections +
                ", segments=" + segments +
                '}';
    }
}
