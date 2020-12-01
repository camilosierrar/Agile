package view;

import controller.Controller;
import model.*;

import java.awt.*;
import java.util.List;
import javax.swing.*;


public class Gui extends JFrame {

    //Plan
    Plan plan;
    //Tour
    Tour tour;

    //Graphic Elements
    JPanel base;
    JTextArea info;
    JPanel topBar;
    JPanel map;
    JPanel mapContainer;
    JButton mapRead;
    JButton reqRead;
    JButton getBestTour;
    JTextField mapPath;
    JTextField reqPath;
    Canvas mapCanvas;

    Controller controller;

    //Constructor
    public Gui(Controller controller) {
        //window name
        super("Delivelov");
        this.controller = controller;
        setGui();
    }

    public void setInfo(String info) {
        this.info.setText(info);
        this.info.setForeground(Color.white);

        this.info.validate();
        this.info.repaint();
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
        mapContainer = new JPanel(new BorderLayout());

        map = new MapGui(this, null,null, null, null);
        info = new JTextArea(5,30);

        //JLabel
        JLabel mapReadLabel = new JLabel("Path To Map");
        JLabel reqReadLabel = new JLabel("Path To Requests");
        String temp = "If you Click on the Map you will receive information here";
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
        //mapCanvas = new Canvas();

        //Attributes
        base.setBackground(Color.DARK_GRAY);
        //map.setBackground(new Color(0,51,102));
        topBar.setBackground(Color.BLACK);
        info.setBackground(Color.DARK_GRAY);
        info.setMaximumSize(new Dimension(300,Integer.MAX_VALUE));
        info.setForeground(Color.WHITE);

        //Add to mapContainer
        mapContainer.add(map,BorderLayout.CENTER);

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
        info.setText(temp);

        // Add button listeners
        mapRead.addActionListener(event -> {
            this.plan = controller.loadMap(mapPath.getText());

            if (plan == null) {
                JOptionPane.showMessageDialog(this,
                        "File doesn't exist",
                        "ERROR",
                        JOptionPane.ERROR_MESSAGE);
            } else {
                mapContainer.removeAll();
                map = new MapGui(this, plan, tour, controller, null);
                map.setBackground(Color.lightGray);
                mapContainer.add(map,BorderLayout.CENTER);
                System.out.println("Map Loaded");
                mapContainer.validate();
                mapContainer.repaint();
            }
        });

        reqRead.addActionListener(event -> {
            tour = controller.loadRequests(reqPath.getText());
            if (tour == null) {
                JOptionPane.showMessageDialog(this,
                        "File doesn't exist",
                        "ERROR",
                        JOptionPane.ERROR_MESSAGE);
            } else {
                mapContainer.removeAll();
                map = new MapGui(this, plan, tour, controller, null);
                map.setBackground(Color.lightGray);
                mapContainer.add(map,BorderLayout.CENTER);
                System.out.println("Map Loaded");
                mapContainer.validate();
                mapContainer.repaint();
            }
        });

        getBestTour.addActionListener(event -> {
            tour = controller.loadRequests(reqPath.getText());
            if (tour == null) {
                JOptionPane.showMessageDialog(this,
                        "File doesn't exist",
                        "ERROR",
                        JOptionPane.ERROR_MESSAGE);
            } else {
                mapContainer.removeAll();
                List<Segment> solution = controller.findBestTour(tour);
                map = new MapGui(this, plan, tour, controller, solution);
                map.setBackground(Color.lightGray);
                mapContainer.add(map,BorderLayout.CENTER);
                System.out.println("Map Loaded");
                mapContainer.validate();
                mapContainer.repaint();
            }
        });



        //Add panels
        base.add(mapContainer,BorderLayout.CENTER);
        base.add(topBar, BorderLayout.PAGE_START);
        base.add(info, BorderLayout.WEST);
        this.add(base);

        //END of Constructor
        this.setVisible(true);
    }
}
