package main;

import model.Intersection;
import model.Plan;
import model.Segment;

import java.awt.*;
import java.util.Collections;
import java.util.HashMap;
import javax.swing.*;
import java.util.List;

public class MapGui extends JPanel {

    HashMap<Long, Intersection> intersections;
    List<Segment> segments;
    Dimension dim;
    //Plan plan;
    
    public MapGui(Plan plan) {
        //this.plan = plan;
        if (plan != null) {
            intersections = plan.getIntersections();
            segments = plan.getSegments();
        }
        System.out.println("Inter : "+ intersections);
    }
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        dim = this.getSize();
        System.out.println(dim);
        double minLat=Double.MAX_VALUE, maxLat=Double.MIN_VALUE, minLong=Double.MAX_VALUE, maxLong=Double.MIN_VALUE;
        //Collections.min(intersections.getLatitude().values());
        //Size of Real Map
        for (Intersection i : intersections.values()) {
            if(i.getLatitude()>maxLat) maxLat = i.getLatitude();
            if(i.getLatitude()<minLat) minLat = i.getLatitude();
            if(i.getLongitude()>maxLong) maxLong = i.getLongitude();
            if(i.getLongitude()<minLong) minLong = i.getLongitude();
        }
        System.out.println(maxLat+" "+minLat+" "+maxLong+" "+minLong);
        //TODO Rajouter Une Marge de Chaque Cote
        double coordHeight = maxLat-minLat;
        double coordWidth = maxLong-minLong;
        //Ratios (scaling)
        double ratioHeight, ratioWidth;
        ratioHeight = dim.height/coordHeight;
        ratioWidth = dim.width/coordWidth;

        //Render Intersections
        for (Intersection i : intersections.values()) {
            int x = (int)((i.getLongitude()-minLong)*ratioWidth);
            int y = (int)((i.getLatitude()-minLat)*ratioHeight);
            g.fillOval(x,y,10,10);
            System.out.println("Point coord : "+x+" "+y);
        }
        //g.fillOval(100,100,10,10);
        //g.fillOval(860,266,10,10);

        //System.out.println("Dim: " + dim);

    }

}