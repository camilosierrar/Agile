package main;

import model.*;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Collections;
import java.util.HashMap;
import javax.swing.*;
import java.util.List;


public class MapGui  extends JPanel implements MouseListener{

    HashMap<Long, Intersection> intersections;
    List<Segment> segments;
    Dimension dim;
    List<Request> requests;
    double ratioHeight, ratioWidth;

    final int DOT_RADIUS = 5;
    final int MARKER_RADIUS = 3;

    public MapGui(Plan plan, Tour tour) {
        //this.plan = plan;
        if (plan != null) {
            intersections = plan.getIntersections();
            segments = plan.getSegments();
        }
        if (tour != null) {
            this.requests = tour.getRequests();
        }

        addMouseListener(this);

        //System.out.println("Inter : "+ intersections);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (this.intersections != null && this.segments!=null) {
            dim = this.getSize();
            System.out.println(dim);
            double minLat = Double.MAX_VALUE, maxLat = Double.MIN_VALUE, minLong = Double.MAX_VALUE, maxLong = Double.MIN_VALUE;
            //Collections.min(intersections.getLatitude().values());
            //Size of Real Map
            for (Intersection i : intersections.values()) {
                if (i.getLatitude() > maxLat) maxLat = i.getLatitude();
                if (i.getLatitude() < minLat) minLat = i.getLatitude();
                if (i.getLongitude() > maxLong) maxLong = i.getLongitude();
                if (i.getLongitude() < minLong) minLong = i.getLongitude();
            }
            System.out.println(maxLat + " " + minLat + " " + maxLong + " " + minLong);
            //TODO Rajouter Une Marge de Chaque Cote
            double coordHeight = maxLat - minLat;
            double coordWidth = maxLong - minLong;
            //Ratios (scaling)

            ratioHeight = dim.height / coordHeight;
            ratioWidth = dim.width / coordWidth;

            //Render Intersections
            /*int R,G,B;
            int seed = 13;
            R=seed;
            G=seed;
            B=seed;
            Color p = new Color(R,G,B);*/
            g.setColor(Color.orange);
            /*for (Intersection i : intersections.values()) {
                int x = (int) ((i.getLongitude() - minLong) * ratioWidth);
                int y = (int) ((i.getLatitude() - minLat) * ratioHeight);
                g.fillOval(x, y, DOT_RADIUS * 2, DOT_RADIUS * 2);
                System.out.println("Point coord : "+x+" "+y + "color :" +p);
                R*=3333;
                G*=5555;
                B*=7777;
                R%=255;
                G%=255;
                B%=255;
                g.setColor( p = new Color(R,G,B));
            }*/

            //Color c;
            //g.setColor( c = new Color(255,0,0));
            g.setColor(Color.black);
            for (int i = 0; i < segments.size(); i++) {
                Segment s = segments.get(i);
                Intersection origin = intersections.get(s.getOrigin().getId());
                Intersection destination = intersections.get(s.getDestination().getId());
                int x1 = (int) ((origin.getLongitude() - minLong) * ratioWidth);
                int y1 = (int) ((origin.getLatitude() - minLat) * ratioHeight);
                int x2 = (int) ((destination.getLongitude() - minLong) * ratioWidth);
                int y2 = (int) ((destination.getLatitude() - minLat) * ratioHeight);
                g.drawLine(x1 + DOT_RADIUS, y1 + DOT_RADIUS, x2 + DOT_RADIUS, y2 + DOT_RADIUS);
                //System.out.println("Origine Long :"+origin.getLongitude());
            }
            if (requests != null) {
                //Pickup Marker
                g.setColor(Color.red);
                for (Request r : requests) {
                    int x = (int) ((r.getPickupAddress().getLongitude() - minLong) * ratioWidth);
                    int y = (int) ((r.getPickupAddress().getLatitude() - minLat) * ratioHeight);
                    g.fillOval(x, y, DOT_RADIUS * 2, DOT_RADIUS * 2);
                }

                //Delivery Marker
                g.setColor(Color.green);
                for (Request r : requests) {
                    int x = (int) ((r.getDeliveryAddress().getLongitude() - minLong) * ratioWidth);
                    int y = (int) ((r.getDeliveryAddress().getLatitude() - minLat) * ratioHeight);
                    g.fillOval(x, y, DOT_RADIUS * 2, DOT_RADIUS * 2);
                }
            }
            //g.fillOval(100,100,10,10);
            //g.fillOval(860,266,10,10);

            //System.out.println("Dim: " + dim);
        }
    }


    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}