package main;

import com.teamdev.jxbrowser.browser.Browser;
import com.teamdev.jxbrowser.engine.Engine;
import com.teamdev.jxbrowser.engine.EngineOptions;
import com.teamdev.jxbrowser.view.swing.BrowserView;
import controller.Controller;
import model.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.*;
import javax.swing.border.Border;
import java.util.List;

import static com.teamdev.jxbrowser.engine.RenderingMode.HARDWARE_ACCELERATED;


public class Gui extends JFrame {

    //Graphic Elements
    JPanel base;
    JPanel info;
    JPanel topBar;
    JButton mapRead;
    JButton reqRead;
    JButton getBestTour;
    JTextField mapPath;
    JTextField reqPath;
    Canvas mapCanvas;

    Controller controller;

    Browser browser;


    private static final int MIN_ZOOM = 0;
    private static final int MAX_ZOOM = 21;
    private static int zoomValue = 13;

    //Constructor
    public Gui(Controller controller) {
        //window name
        super("Delivelov");
        this.controller = controller;
        setGui();
    }

    private void setGui() {
        //Dimensions et layout
        this.setSize(1000,600);
        this.setMinimumSize(new Dimension(1200, 600));
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(new BorderLayout()); //ou FlowLayout()


        //Panels
        base = new JPanel(new BorderLayout()); // ou FlowLayout()
        topBar = new JPanel();

        info = new JPanel();

        //JLabel
        JLabel mapReadLabel = new JLabel("Path To Map");
        JLabel reqReadLabel = new JLabel("Path To Requests");
        JLabel temp = new JLabel("If you Click on the Map you will receive information here");
        temp.setForeground(Color.WHITE);
        mapReadLabel.setForeground(Color.WHITE);
        reqReadLabel.setForeground(Color.WHITE);

        //JTextField
        mapPath = new JTextField(20);
        reqPath = new JTextField(20);

        //Buttons
        mapRead = new JButton("Load Map");
        reqRead = new JButton("Load Requests");
        getBestTour = new JButton("Find Best Tour");

        //Canvas
        mapCanvas = new Canvas();

        //Attributes
        base.setBackground(Color.DARK_GRAY);
        //map.setBackground(new Color(0,51,102));
        topBar.setBackground(Color.BLACK);
        info.setBackground(Color.DARK_GRAY);
        info.setMaximumSize(new Dimension(300,Integer.MAX_VALUE));
        //Add to topBar

        //MapReading
        topBar.add(mapReadLabel);
        topBar.add(mapPath);
        topBar.add(mapRead);
        //ReqReading
        topBar.add(reqReadLabel);
        topBar.add(reqPath);
        topBar.add(reqRead);
        //getBestTour
        topBar.add(getBestTour);

        //Add to info
        info.add(temp);

        // Add button listeners
        mapRead.addActionListener(event -> {
            Plan plan = controller.loadMap(mapPath.getText());

            if (plan == null) {
                JOptionPane.showMessageDialog(this,
                        "File doesn't exist",
                        "ERROR",
                        JOptionPane.ERROR_MESSAGE);
            } else {
                // TODO SHOW MAP IN THE UI

            }
        });

        reqRead.addActionListener(event -> {
            Tour tour = controller.loadRequests(reqPath.getText());

            if (tour == null) {
                JOptionPane.showMessageDialog(this,
                        "File doesn't exist",
                        "ERROR",
                        JOptionPane.ERROR_MESSAGE);
            } else {

                // Remove previous markers
                browser.navigation().loadUrl("file:/Users/javigabe/Documents/universidad/erasmus/AGILE/Agile/src/main/java/resources/map.html");


                // SHOW REQUESTS IN THE UI
                Intersection departure = tour.getAddressDeparture();

                System.out.println(tour.toString());

                try {
                    String markerScript = "var myLatlng = new google.maps.LatLng(" + departure.getLatitude()
                            + "," + departure.getLongitude() + ");\n" +
                            "var marker = new google.maps.Marker({\n" +
                            "    position: myLatlng,\n" +
                            "    map: map,\n" +
                            "    title: 'Departure!'\n" +
                            "});";

                    browser.mainFrame().ifPresent(frame ->
                            frame.executeJavaScript(markerScript));
                } catch (NullPointerException e) {
                    e.printStackTrace();
                    System.out.println("DEPARTURE ADRESS NOT FOUND\n");
                }

                System.out.println(tour.getRequests().toString());

                for (Request request: tour.getRequests()) {
                    try {
                        String deliveryMarkerScript = "var deliveryLatLong = new google.maps.LatLng(" + request.getDeliveryAddress().getLatitude()
                                + "," + request.getDeliveryAddress().getLongitude() + ");\n" +
                                "var deliveryMarker = new google.maps.Marker({\n" +
                                "    position: deliveryLatLong,\n" +
                                "    map: map,\n" +
                                "    title: 'Delivery!'\n" +
                                "});";

                        String pickupMarkerScript = "var deliveryLatLong = new google.maps.LatLng(" + request.getPickupAddress().getLatitude()
                                + "," + request.getPickupAddress().getLongitude() + ");\n" +
                                "var deliveryMarker = new google.maps.Marker({\n" +
                                "    position: deliveryLatLong,\n" +
                                "    map: map,\n" +
                                "    title: 'Pick up!'\n" +
                                "});";;

                        browser.mainFrame().ifPresent(frame ->
                                frame.executeJavaScript(deliveryMarkerScript));
                        browser.mainFrame().ifPresent(frame ->
                                frame.executeJavaScript(pickupMarkerScript));
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                        System.out.println("CRAP");
                    }
                }
            }
        });

        getBestTour.addActionListener(event -> {
            // TODO
            controller.findBestTour();
        });

        //Add panels
        //base.add(map,BorderLayout.CENTER);
        base.add(topBar, BorderLayout.PAGE_START);
        base.add(info, BorderLayout.WEST);
        setMap(base);
        this.add(base);

        //END of Constructor
        this.setVisible(true);
    }


    /**
     * In map.html file default zoom value is set to 13.
     */



    private void setMap(JPanel base) {
        System.setProperty("jxbrowser.license.key", "1BNDHFSC1FXEWE7VRF2L36BWMILQ32DMIU9ODTZZ9PT6OA9WADNKY3PV8JNUDYNG0CN370");
        EngineOptions options = EngineOptions.newBuilder(HARDWARE_ACCELERATED).build();
        Engine engine = Engine.newInstance(options);
        browser = engine.newBrowser();

        SwingUtilities.invokeLater(() -> {
            BrowserView view = BrowserView.newInstance(browser);

            JButton zoomInButton = new JButton("Zoom In");
            zoomInButton.addActionListener(e -> {
                if (zoomValue < MAX_ZOOM) {
                    browser.mainFrame().ifPresent(frame ->
                            frame.executeJavaScript("map.setZoom(" +
                                    ++zoomValue + ")"));
                }
            });

            JButton zoomOutButton = new JButton("Zoom Out");
            zoomOutButton.addActionListener(e -> {
                if (zoomValue > MIN_ZOOM) {
                    browser.mainFrame().ifPresent(frame ->
                            frame.executeJavaScript("map.setZoom(" +
                                    --zoomValue + ")"));
                }
            });

            JPanel toolBar = new JPanel();
            toolBar.add(zoomInButton);
            toolBar.add(zoomOutButton);

            base.add(toolBar, BorderLayout.SOUTH);
            base.add(view, BorderLayout.CENTER);

            browser.navigation().loadUrl("file:/Users/javigabe/Documents/universidad/erasmus/AGILE/Agile/src/main/java/resources/map.html");
            //browser.navigation().loadUrl("https://www.google.com/maps/place/Lyon,+Francia/@45.7579341,4.7650812,13z/data=!3m1!4b1!4m5!3m4!1s0x47f4ea516ae88797:0x408ab2ae4bb21f0!8m2!3d45.764043!4d4.835659");
        });
    }
}