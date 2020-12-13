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

/**
 * This class takes care of the map part of our UI
 */
public class MapGui  extends JPanel implements MouseListener{

    private boolean adding = false;
    /**
     * The hashmap regrouping all of our intersections
     */
    private HashMap<Long, Intersection> intersections;
    /**
     * The list of all the segments composing the map
     */
    private List<Segment> segments;
    /**
     *Dimension of whole Map
     */
    private Dimension dim; //
    /**
     * Dimension of the part of the map that can be seen
     */
    private Dimension screenSize; //
    /**
     * List of requests
     */
    private List<Request> requests;
    /**
     * Departure address
     */
    private Intersection departureAddress;
    /**
     * Departure point
     */
    private Point departurePoint;
    /**
     * Pickup table
     */
    private HashMap<Point, Intersection> pickUpTable;
    /**
     * Delivery table
     */
    private HashMap<Point, Intersection> deliveryTable;
    /**
     * Hashmap containing all of the intersections
     */
    private HashMap<Point, Intersection> allIntersections;
    /**
     * Pickup intersection
     */
    private Intersection pickup;
    /**
     * The counter
     */
    private int counter=1;
    /**
     * The gui that prints our UI
     */
    private Gui gui;
    /**
     * ids
     */
    private long id1, id2;
    /**
     * Ratios that allow us to transforms latitudes into the map
     */
    private double ratioHeight, ratioWidth;
    /**
     * The width of our dots
     */
    private final int DOT_RADIUS = 5;

    /**
     * List of important points
     */
    private List<Point> points;
    /**
     * The controller that commands everything
     */
    private Controller controller;
    /**
     * The list of solutions from our algorithm
     */
    private List<Segment> solution = null;
    /**
     * The zoom value
     */
    private double zoom;

    /**
     * Initiates the Map that will be seen on the GUI
     * @param gui the UI
     * @param plan the plan
     * @param tour the current tour
     * @param controller the controller
     * @param solution The list of segments that compose the best solution found by the algorithm
     * @param zoom the level of zoom
     * @param screenSize the screen size
     * @param id1 the first id
     * @param id2 the second id

     */
    public MapGui(Gui gui, Plan plan, Tour tour, Controller controller, List<Segment> solution, int zoom, Dimension screenSize, long id1, long id2) {
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
        allIntersections = new HashMap<>();
        this.zoom = zoom;
        this.id1=id1;
        this.id2=id2;
        dim = new Dimension(0,0);
        this.screenSize = screenSize;
    }

    /**
     * Main function that paints the window
     * @param g is the graphic we will configure
     */
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

            for(Intersection i: intersections.values()) {
                int x1 = (int) ((i.getLongitude() - minLong) * ratioWidth);
                int y1 = dim.height - (int) ((i.getLatitude() - minLat) * ratioHeight);
                Point point = new Point(x1,y1);
                points.add(point);
                allIntersections.put(point,i);
            }

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
                //System.out.println(s.toString());
                g.drawLine(x1 + DOT_RADIUS, y1 + DOT_RADIUS -10, x2 + DOT_RADIUS, y2 + DOT_RADIUS -10);

            }
            
            if (requests != null) {
                //Pickup Marker
                g.setColor(new Color(0, 100, 0, 100));
                if (intersections.get(id2) != null && id1 != 0) {
                    Intersection tmp = intersections.get(id1);
                    int x = (int) ((tmp.getLongitude() - minLong) * ratioWidth);
                    int y = dim.height - (int) ((tmp.getLatitude() - minLat) * ratioHeight);
                    g.fillOval(x-DOT_RADIUS, y-10-DOT_RADIUS, DOT_RADIUS * 4, DOT_RADIUS * 4);
                }
                if (intersections.get(id2) != null && id2 != 0) {
                    Intersection tmp = intersections.get(id2);
                    int x = (int) ((tmp.getLongitude() - minLong) * ratioWidth);
                    int y = dim.height - (int) ((tmp.getLatitude() - minLat) * ratioHeight);
                    g.fillOval(x-DOT_RADIUS, y-10-DOT_RADIUS, DOT_RADIUS * 4, DOT_RADIUS * 4);
                }
                g.setColor(Color.red);
                for (Request r : requests) {
                    //System.out.println("Requests line 118 : " + r.toString());
                    int x = (int) ((r.getPickupAddress().getLongitude() - minLong) * ratioWidth);
                    int y = dim.height - (int) ((r.getPickupAddress().getLatitude() - minLat) * ratioHeight);
                    g.fillOval(x, y - 10, DOT_RADIUS * 2, DOT_RADIUS * 2);
                    Point point = new Point(x, y);
                    points.add(point);
                    pickUpTable.put(point, r.getPickupAddress());
                }

                //Delivery Marker
                g.setColor(Color.green);
                for (Request r : requests) {
                    //System.out.println(r.toString());
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
                departurePoint = point;
            }
        }
    }
    /**
     * Returns the dimension of the window
     */
    public Dimension getDim() {
        return dim;
    }
    /**
     * Returns the new dimension that is modified by the zoom
     * @param zoom the percentage of zooming in the window
     */
    public Dimension getNewDim(int zoom) {
        Dimension tmp = screenSize;
        //System.out.println("this.getSize() : " + tmp);
        tmp.setSize(tmp.getWidth()*(1+(zoom/10)), tmp.getHeight()*(1+(zoom/10)));
        return tmp;
    }
    /**
     * The event listener that manages the actions made when the mouse is clicked
     * @param e the mouse event, the click
     */
    @Override
    public void mouseClicked(MouseEvent e) {
        System.out.println("209 Clear");

        if (e.getClickCount()==1) {
            Intersection intersection = null;
            for (Point point: points) {
                if ((e.getPoint().x > point.x - 10 && e.getPoint().x < point.x + 10) && (e.getPoint().y > point.y - 10 && e.getPoint().y < point.y + 10)) {
                    System.out.println("212 Clear");
                    String temp;
                    if (pickUpTable.containsKey(point)) {
                        // It is a pickup point
                        intersection = pickUpTable.get(point);
                        temp = "Pick up point\n" + intersection.toString();
                        System.out.println("Pick up point");
                        System.out.println(intersection.toString());
                        gui.setSelection(intersection.getId());
                    } else if (deliveryTable.containsKey(point)) {
                        // It is a delivery point
                        intersection = deliveryTable.get(point);
                        temp = "Delivery point\n" + intersection.toString();
                        System.out.println("Delivery point");
                        System.out.println(intersection.toString());
                        gui.setSelection(intersection.getId());
                    } else if (point == departurePoint){
                        gui.setSelection(departureAddress.getId());
                        intersection = departureAddress;
                    } else if(allIntersections.containsKey(point)) {
                        System.out.println("228 Clear");
                        intersection = allIntersections.get(point);
                        if (adding) {
                            if (counter == 2) {
                                System.out.println("231 Clear");
                                gui.addRequest(pickup, allIntersections.get(point));
                                System.out.println("233 Clear");
                                counter = 1;
                                break;
                            } else if (counter == 1) {
                                System.out.println("236 Clear");
                                pickup = allIntersections.get(point);
                                System.out.println("238 Clear");
                                counter++;
                                break;
                            }
                        }
                    }
                    System.out.println("239 Clear");
                    //this.gui.setInfo(temp);

                }
            }
            if(intersection != null) {this.gui.setInfo(controller.getAddress(intersection.getLatitude(),intersection.getLongitude()));}

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

    public void setAdding(boolean adding) {
        this.adding = adding;
    }
}
