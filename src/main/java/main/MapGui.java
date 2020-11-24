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
        int R,G,B;
        int seed = 13;
        R=seed;
        G=seed;
        B=seed;
        Color p = new Color(R,G,B);
        g.setColor( p );
        for (Intersection i : intersections.values()) {
            int x = (int)((i.getLongitude()-minLong)*ratioWidth);
            int y = (int)((i.getLatitude()-minLat)*ratioHeight);
            g.fillOval(x,y,10,10);
            System.out.println("Point coord : "+x+" "+y + "color :" +p);
            R*=3333;
            G*=5555;
            B*=7777;
            R%=255;
            G%=255;
            B%=255;
            g.setColor( p = new Color(R,G,B));
        }

        Color c;
        g.setColor( c = new Color(255,0,0));
        for(int i=0;i<segments.size();i++){
            Segment s = segments.get(i);
            Intersection origin = s.getOrigin();
            Intersection destination = s.getDestination();
            int x1 = (int)((origin.getLongitude()-minLong)*ratioWidth);
            int y1 = (int)((origin.getLatitude()-minLat)*ratioHeight);
            int x2 = (int)((destination.getLongitude()-minLong)*ratioWidth);
            int y2 = (int)((destination.getLatitude()-minLat)*ratioHeight);
            g.drawLine(x1,y1,x2,y2);
            System.out.println("Ligne coord : x1"+x1+" y1"+y1 +"x2"+ x2+"+y2+"+ y2);
        }
        //g.fillOval(100,100,10,10);
        //g.fillOval(860,266,10,10);

        //System.out.println("Dim: " + dim);

    }

}