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
    private Dimension dim; //Dimesion of whole Map
    private Dimension screenSize; //Dimension of the part of the map that can be seen
    private List<Request> requests;
    private Intersection departureAddress;
    private HashMap<Point, Intersection> pickUpTable;
    private HashMap<Point, Intersection> deliveryTable;
    private Gui gui;

    private double ratioHeight, ratioWidth;

    private final int DOT_RADIUS = 5;

    private List<Point> points;
    private Controller controller;
    private List<Segment> solution = null;
    private double zoom;


    public MapGui(Gui gui, Plan plan, Tour tour, Controller controller, List<Segment> solution, int zoom, Dimension screenSize) {
        //this.plan = plan;
        this.gui = gui;
        this.controller = controller;
        if (plan != null) {
            intersections = plan.getIntersections();
            segments = plan.getSegments();
        }
        if (tour != null) {
            this.requests = tour.getRequests();
            this.departureAddress = tour.getAddressDeparture();
            this.solution = solution;
;        }
        addMouseListener(this);
        points = new ArrayList<>();
        pickUpTable = new HashMap<>();
        deliveryTable = new HashMap<>();
        this.zoom = zoom;
        dim = new Dimension(0,0);
        this.screenSize = screenSize;
    }


    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (this.intersections != null && this.segments!=null) {
            dim = this.getSize();
            //Zoom
            dim.setSize(screenSize.getWidth()*(1+(zoom/10)), screenSize.getHeight()*(1+(zoom/10)));
            this.setPreferredSize(dim);
            //System.out.println("2 : "+dim);
            double minLat = Double.MAX_VALUE, maxLat = Double.MIN_VALUE, minLong = Double.MAX_VALUE, maxLong = Double.MIN_VALUE;
            //Collections.min(intersections.getLatitude().values());
            //Size of Real Map
            for (Intersection i : intersections.values()) {
                if (i.getLatitude() > maxLat) maxLat = i.getLatitude();
                if (i.getLatitude() < minLat) minLat = i.getLatitude();
                if (i.getLongitude() > maxLong) maxLong = i.getLongitude();
                if (i.getLongitude() < minLong) minLong = i.getLongitude();
            }
            //System.out.println(maxLat + " " + minLat + " " + maxLong + " " + minLong);
            //TODO Rajouter Une Marge de Chaque Cote
            double coordHeight = maxLat - minLat;
            double coordWidth = maxLong - minLong;

            //Ratios (scaling)
            ratioHeight = (dim.height -10) / coordHeight;
            ratioWidth = (dim.width -10) / coordWidth;

            for (int i = 0; i < segments.size(); i++) {
                g.setColor(Color.white);
                Segment s = segments.get(i);
                Intersection origin = intersections.get(s.getOrigin().getId());
                Intersection destination = intersections.get(s.getDestination().getId());
                int x1 = (int) ((origin.getLongitude() - minLong) * ratioWidth);
                int y1 = dim.height - (int) ((origin.getLatitude() - minLat) * ratioHeight);
                int x2 = (int) ((destination.getLongitude() - minLong) * ratioWidth);
                int y2 = dim.height - (int) ((destination.getLatitude() - minLat) * ratioHeight);
                g.drawLine(x1 + DOT_RADIUS, y1 + DOT_RADIUS - 10, x2 + DOT_RADIUS, y2 + DOT_RADIUS -10);
            }
            for (int i = 0; i < segments.size(); i++) {
                g.setColor(Color.RED);
                Segment s = segments.get(i);

                if (solution == null) {
                    break;
                } else if (!solution.contains(s)) {
                    continue;
                }

                Intersection origin = intersections.get(s.getOrigin().getId());
                Intersection destination = intersections.get(s.getDestination().getId());
                int x1 = (int) ((origin.getLongitude() - minLong) * ratioWidth);
                int y1 = dim.height - (int) ((origin.getLatitude() - minLat) * ratioHeight);
                int x2 = (int) ((destination.getLongitude() - minLong) * ratioWidth);
                int y2 = dim.height - (int) ((destination.getLatitude() - minLat) * ratioHeight);
                System.out.println(s.toString());
                g.drawLine(x1 + DOT_RADIUS, y1 + DOT_RADIUS -10, x2 + DOT_RADIUS, y2 + DOT_RADIUS -10);

            }
            
            if (requests != null) {
                //Pickup Marker
                g.setColor(Color.red);
                for (Request r : requests) {
                    if(r.getPickupAddress()==null) {
                        JOptionPane.showMessageDialog(this,
                                "The file requested for the requests contains Delivery points that are out of the limits of the map, please " +
                                        "select a bigger map",
                                "ERROR",
                                JOptionPane.ERROR_MESSAGE);
                    }
                    else {
                        System.out.println("Requests line 118 : " + r.toString());
                        int x = (int) ((r.getPickupAddress().getLongitude() - minLong) * ratioWidth);
                        int y = dim.height - (int) ((r.getPickupAddress().getLatitude() - minLat) * ratioHeight);
                        g.fillOval(x, y - 10, DOT_RADIUS * 2, DOT_RADIUS * 2);
                        Point point = new Point(x, y);
                        points.add(point);
                        pickUpTable.put(point, r.getPickupAddress());
                    }
                }

                //Delivery Marker
                g.setColor(Color.green);
                for (Request r : requests) {
                    System.out.println(r.toString());
                    int x = (int) ((r.getDeliveryAddress().getLongitude() - minLong) * ratioWidth);
                    int y = dim.height - (int) ((r.getDeliveryAddress().getLatitude() - minLat) * ratioHeight);
                    g.fillOval(x, y-10, DOT_RADIUS * 2, DOT_RADIUS * 2);
                    Point point = new Point(x,y);
                    points.add(point);
                    deliveryTable.put(point, r.getDeliveryAddress());
                }
            }
            if(departureAddress != null){
                //departure address
                g.setColor(Color.yellow);
                int x = (int) ((departureAddress.getLongitude() - minLong) * ratioWidth);
                int y = dim.height - (int) ((departureAddress.getLatitude() - minLat) * ratioHeight);
                g.fillOval(x, y-10, DOT_RADIUS * 2, DOT_RADIUS * 2);
                Point point = new Point(x,y);
                points.add(point);
            }
        }
    }

    public Dimension getDim() {
        return dim;
    }

    public Dimension getNewDim(int zoom) {
        Dimension tmp = screenSize;
        //System.out.println("this.getSize() : " + tmp);
        tmp.setSize(tmp.getWidth()*(1+(zoom/10)), tmp.getHeight()*(1+(zoom/10)));
        return tmp;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        for (Point point: points) {
            if ((e.getPoint().x > point.x - 10 && e.getPoint().x < point.x + 10) && (e.getPoint().y > point.y - 10 && e.getPoint().y < point.y + 10)) {
                String temp;
                if (pickUpTable.containsKey(point)) {
                    // It is a pickup point
                    Intersection intersection = pickUpTable.get(point);
                    temp = "Pick up point\n" + intersection.toString();
                    System.out.println("Pick up point");
                    System.out.println(intersection.toString());
                } else if (deliveryTable.containsKey(point)) {
                    // It is a delivery point
                    Intersection intersection = deliveryTable.get(point);
                    temp = "Delivery point\n" + intersection.toString();
                    System.out.println("Delivery point");
                    System.out.println(intersection.toString());
                }
                else{
                    temp = "Departure Point\n" + this.departureAddress.toString();
                }

                //this.gui.setInfo(temp);
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
