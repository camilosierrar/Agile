package view;

import controller.Controller;
import model.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import javax.swing.*;
import javax.swing.table.TableModel;
import javax.swing.ListSelectionModel;


public class Gui extends JFrame {

    //Plan
    Plan plan;
    //Tour
    Tour tour;

    //Graphic Elements
    JPanel base;
    JTextArea info;
    JTable table;
    JPanel topBar;
    JPanel map;
    JPanel leftSection;
    JPanel leftButtonBar;
    JPanel tableSection;
    JPanel mapContainer;
    JButton mapRead;
    JButton reqRead;
    JButton deleteSel;
    JButton addStep;
    JButton recalculate;
    JButton getBestTour;
    JTextField mapPath;
    JTextField reqPath;
    Canvas mapCanvas;

    Controller controller;

    boolean controlFlagSelectionEvent = true;

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
        leftSection = new JPanel(new BorderLayout());
        leftButtonBar = new JPanel();
        tableSection = new JPanel(new BorderLayout());
        map = new MapGui(this, null,null, null, null);
        info = new JTextArea(5,30);
        table = new JTable(new TableContent());

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
        deleteSel = new JButton("Delete selection");
        addStep = new JButton("Add a step");
        recalculate = new JButton("Recalculate itinerary");
        getBestTour = new JButton("Find Best Tour");

        //Canvas
        //mapCanvas = new Canvas();

        //Attributes
        leftSection.setBackground(Color.LIGHT_GRAY);
        leftSection.setSize(300,Integer.MAX_VALUE);
        base.setBackground(Color.DARK_GRAY);
        //map.setBackground(new Color(0,51,102));
        topBar.setBackground(Color.BLACK);
        info.setBackground(Color.DARK_GRAY);
        info.setMaximumSize(new Dimension(Integer.MAX_VALUE, 200));
        info.setForeground(Color.WHITE);

        //Add to mapContainer
        mapContainer.add(map,BorderLayout.CENTER);

        leftButtonBar.add(deleteSel);
        leftButtonBar.add(addStep);
        leftButtonBar.add(recalculate);
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
        tableSection.setMaximumSize(new Dimension(Integer.MAX_VALUE,300));
        table.setBackground(Color.GRAY);

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

            mapContainer.removeAll();
            List<Segment> solution = controller.findBestTour(tour);
            map = new MapGui(this, plan, tour, controller, solution);
            map.setBackground(Color.lightGray);
            mapContainer.add(map,BorderLayout.CENTER);
            System.out.println("Map Loaded");
            mapContainer.validate();
            mapContainer.repaint();

            controlFlagSelectionEvent = false;
            TableContent table = new TableContent(solution, tour);
            // System.out.println("tableContent = "+tableContent);
            if (this.table == null){ this.table = new JTable(table); }
            else{ this.table.setModel(table); }
            tableSection.validate();
            tableSection.repaint();
            controlFlagSelectionEvent = true;
        });

        this.table.getSelectionModel().addListSelectionListener(event -> {
            if(controlFlagSelectionEvent && this.table.getSelectionModel().getAnchorSelectionIndex() != -1) {
                controlFlagSelectionEvent = false;
                this.table.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                if (!event.getValueIsAdjusting()) {
                    ListSelectionModel lsm = (ListSelectionModel) event.getSource();
                    int indexCouple = controller.findCoupleIndex(lsm.getAnchorSelectionIndex());
                    this.table.getSelectionModel().setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
                    boolean includes = false;
                    for (int index : this.table.getSelectedRows()) {
                        if (index == indexCouple) {
                            includes = true;
                            break;
                        }
                    }
                    if (!includes) {
                        this.table.addRowSelectionInterval(indexCouple, indexCouple);
                        this.table.validate();
                        this.table.repaint();
                    }
                }
                controlFlagSelectionEvent = true;
            }
        });

        this.deleteSel.addActionListener(event -> {
            if (this.table.getSelectionModel().isSelectionEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "0 element selected",
                        "Warning",
                        JOptionPane.WARNING_MESSAGE);
            } else {
                controller.deleteSelection(table.getSelectionModel().getMaxSelectionIndex(), table.getSelectionModel().getMinSelectionIndex(), (TableContent) this.table.getModel());
            }
        });



        //Add panels
        base.add(mapContainer,BorderLayout.CENTER);
        base.add(topBar, BorderLayout.PAGE_START);
        leftSection.add(info, BorderLayout.NORTH);
        leftSection.add(tableSection, BorderLayout.CENTER);
        tableSection.add(new JScrollPane(this.table),BorderLayout.CENTER);
        leftSection.add(leftButtonBar, BorderLayout.SOUTH);
        base.add(leftSection, BorderLayout.WEST);
        this.add(base);

        //END of Constructor
        this.setVisible(true);
    }
}
