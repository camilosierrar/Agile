package view;

import controller.Controller;
import model.*;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.*;
import java.util.List;


public class MapGui  extends JPanel implements MouseListener{

    private HashMap<Long, Intersection> intersections;
    private List<Segment> segments;
    private Dimension dim;
    private List<Request> requests;
    private Intersection departureAddress;
    private HashMap<Point, Intersection> pickUpTable;
    private HashMap<Point, Intersection> deliveryTable;

    double ratioHeight, ratioWidth;

    final int DOT_RADIUS = 5;

    private List<Point> points;
    private Controller controller;


    public MapGui(Plan plan, Tour tour, Controller controller) {
        //this.plan = plan;
        if (plan != null) {
            intersections = plan.getIntersections();
            segments = plan.getSegments();
        }
        if (tour != null) {
            this.requests = tour.getRequests();
            this.departureAddress = tour.getAddressDeparture();
;        }
        addMouseListener(this);
        points = new ArrayList<>();
        pickUpTable = new HashMap<>();
        deliveryTable = new HashMap<>();
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
            }
            if (requests != null) {
                //Pickup Marker
                g.setColor(Color.red);
                for (Request r : requests) {
                    int x = (int) ((r.getPickupAddress().getLongitude() - minLong) * ratioWidth);
                    int y = (int) ((r.getPickupAddress().getLatitude() - minLat) * ratioHeight);
                    g.fillOval(x, y, DOT_RADIUS * 2, DOT_RADIUS * 2);
                    Point point = new Point(x,y);
                    points.add(point);
                    pickUpTable.put(point, r.getPickupAddress());
                }

                //Delivery Marker
                g.setColor(Color.green);
                for (Request r : requests) {
                    int x = (int) ((r.getDeliveryAddress().getLongitude() - minLong) * ratioWidth);
                    int y = (int) ((r.getDeliveryAddress().getLatitude() - minLat) * ratioHeight);
                    g.fillOval(x, y, DOT_RADIUS * 2, DOT_RADIUS * 2);
                    Point point = new Point(x,y);
                    points.add(point);
                    deliveryTable.put(point, r.getDeliveryAddress());
                }
            }
            if(departureAddress != null){
                //departure address
                g.setColor(Color.yellow);
                int x = (int) ((departureAddress.getLongitude() - minLong) * ratioWidth);
                int y = (int) ((departureAddress.getLatitude() - minLat) * ratioHeight);
                g.fillOval(x, y, DOT_RADIUS * 2, DOT_RADIUS * 2);
                Point point = new Point(x,y);
                points.add(point);
            }
        }
    }


    @Override
    public void mouseClicked(MouseEvent e) {
        for (Point point: points) {
            if ((e.getPoint().x > point.x - 10 && e.getPoint().x < point.x + 10) && (e.getPoint().y > point.y - 10 && e.getPoint().y < point.y + 10)) {
                if (pickUpTable.containsKey(point)) {
                    // It is a pickup point
                    Intersection intersection = pickUpTable.get(point);
                    System.out.println("Pick up point");
                    System.out.println(intersection.toString());
                } else if (deliveryTable.containsKey(point)) {
                    // It is a delivery point
                    Intersection intersection = deliveryTable.get(point);
                    System.out.println("Delivery point");
                    System.out.println(intersection.toString());
                }
            }
        }

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
