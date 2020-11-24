package main;

import controller.Controller;
import model.Plan;
import model.Tour;

import java.awt.*;
import javax.swing.*;


public class Gui extends JFrame {

    //Graphic Elements
    JPanel base;
    MapGui map;
    JPanel info;
    JPanel topBar;
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

    private void setGui() {
        //Dimensions et layout
        this.setSize(1000,600);
        this.setMinimumSize(new Dimension(1200, 600));
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(new BorderLayout()); //ou FlowLayout()


        //Panels
        base = new JPanel(new BorderLayout()); // ou FlowLayout()
        topBar = new JPanel();
        map = new MapGui();
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
            String file = getFilePath(this);
            Plan plan = controller.loadMap(file);

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
            String file = getFilePath(this);
            Tour tour = controller.loadRequests(file);

            if (tour == null) {
                JOptionPane.showMessageDialog(this,
                        "File doesn't exist",
                        "ERROR",
                        JOptionPane.ERROR_MESSAGE);
            } else {
                // TODO SHOW REQUESTS IN THE UI
            }
        });

        getBestTour.addActionListener(event -> {
            // TODO
            controller.findBestTour();
        });

        //Add panels
        base.add(map,BorderLayout.CENTER);
        base.add(topBar, BorderLayout.PAGE_START);
        base.add(info, BorderLayout.WEST);
        this.add(base);

        //END of Constructor
        this.setVisible(true);
    }


    private String getFilePath(Frame frame) {
        String filePath = (String)JOptionPane.showInputDialog(
                frame,
                "What file you want to read?",
                "File path",
                JOptionPane.PLAIN_MESSAGE,
                null,
                null, "");

        return filePath;
    }
}