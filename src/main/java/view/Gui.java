package view;

import controller.Controller;
import errorTesting.SimpleErrorHandler;
import model.*;


import javax.swing.*;
import java.awt.*;
import java.io.File;
import javax.swing.filechooser.FileFilter;

import config.Variable;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.List;
import javax.swing.*;
import javax.swing.event.ChangeEvent;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;


//import static com.teamdev.jxbrowser.engine.RenderingMode.HARDWARE_ACCELERATED;


public class Gui extends JFrame {

    //Util
    Boolean mapFromFile;
    Boolean reqFromFile;
    boolean controlFlagSelectionEvent = true;
    boolean  isPickup = false;

    int zoom;

    //Selected intersections
    long selId1, selId2;

    //Solution
    List<Segment> solution;

    //Graphic Elements
    JPanel base;
    JTextArea info;
    //JPanel info;
    JTable table;
    JPanel leftSection;
    JPanel leftButtonBar;
    JPanel tableSection;
    JPanel topBar;
    MapGui map;
    JPanel toolbar;
    JPanel mapContainer;
    JScrollPane mapScroll;
    JButton mapFile;
    JButton reqFile;
    JButton deleteSel;
    JButton addStep;
    JButton modify;
    JButton getBestTour;
    JButton undo;
    JButton redo;
    JButton stopAlgo;
    JButton stopAlgoButton;
    JTextField mapPath;
    JTextField reqPath;
    JSlider zoomSlide;

    TableContent tableCont;

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

    public void setSelection(long id) {
        int index = tableCont.getIndexbyId(id);
        controlFlagSelectionEvent = false;
        table.getSelectionModel().clearSelection();
        controlFlagSelectionEvent = true;
        table.getSelectionModel().setSelectionInterval(index, index);
        table.validate();
        table.repaint();
    }

    private void setGui() {
        zoom = 0;
        solution = null;
        selId1 = 0;
        selId2 = 0;
        //Dimensions and layout
        this.setSize(1000,600);
        this.setMinimumSize(new Dimension(1200, 600));
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(new BorderLayout()); //ou FlowLayout()


        //Panels
        base = new JPanel(new BorderLayout()); // ou FlowLayout()
        topBar = new JPanel();
        mapContainer = new JPanel(new BorderLayout());
        toolbar = new JPanel();
        leftSection = new JPanel(new BorderLayout());
        leftButtonBar = new JPanel();
        tableSection = new JPanel(new BorderLayout());
        table = new JTable(new TableContent());

        map = new MapGui(this, null,null, null, null, 1, null, 0,0);
        info = new JTextArea(5,30);


        //JLabel
        JLabel mapReadLabel = new JLabel("Path To Map");
        JLabel reqReadLabel = new JLabel("Path To Requests");
        //JLabel temp = new JLabel("If you Click on the Map you will receive information here");
        String temp = "If you Click on the Map you will receive information here";
        //temp.setForeground(Color.WHITE);
        mapReadLabel.setForeground(Color.WHITE);
        reqReadLabel.setForeground(Color.WHITE);

        //JTextField
        mapPath = new JTextField(10);
        reqPath = new JTextField(10);

        //Buttons
        mapFile = new JButton("Load Map file");
        reqFile = new JButton("Load Requests file");
        stopAlgoButton = new JButton("Stop loading");
        getBestTour = new JButton("Find Best Tour");
        getBestTour.setBackground(Color.GREEN);
        getBestTour.setBorder(null);

        deleteSel = new JButton("Delete selection");
        addStep = new JButton("Add a step");
        modify = new JButton("Modify Order");

        undo = new JButton("Undo");
        undo.setBackground(Color.RED);
        undo.setBorder(null);

        redo = new JButton("Redo");
        redo.setBackground(Color.GREEN);
        redo.setBorder(null);

        stopAlgo = new JButton("Stop loading");
        stopAlgo.setBackground(Color.RED);
        stopAlgo.setBorder(null);

        //Slider (Zoom)
        zoomSlide = new JSlider();
        zoomSlide.setMajorTickSpacing(10);
        zoomSlide.setMinorTickSpacing(5);
        zoomSlide.setSnapToTicks(true);
        zoomSlide.setValue(1);
        zoomSlide.setEnabled(false);
        //zoomSlide.setPaintTicks(true);
        //zoomSlide.setPaintLabels(true);
        //Canvas
        //mapCanvas = new Canvas();

        //Attributes
        base.setBackground(Color.DARK_GRAY);
        toolbar.setBackground(Color.black);
        //map.setBackground(new Color(0,51,102));
        topBar.setBackground(Color.BLACK);
        info.setBackground(Color.DARK_GRAY);
        leftSection.setBackground(Color.LIGHT_GRAY);
        leftSection.setSize(300,Integer.MAX_VALUE);
        info.setMaximumSize(new Dimension(Integer.MAX_VALUE, 200));
        info.setForeground(Color.WHITE);

        //Add to mapContainer
        mapContainer.add(map,BorderLayout.CENTER);

        //Add to leftButtonBar
        leftButtonBar.add(deleteSel);
        leftButtonBar.add(addStep);
        //leftButtonBar.add(modify);

        //Add to topBar
            //MapReading
        topBar.add(mapReadLabel);
        topBar.add(mapPath);
        topBar.add(mapFile);
            //ReqReading
        topBar.add(reqReadLabel);
        topBar.add(reqPath);
        topBar.add(reqFile);
     
            //getBestTour
        topBar.add(getBestTour);
        topBar.add(stopAlgoButton);

        //Add to info
        info.setText(temp);
        tableSection.setMaximumSize(new Dimension(Integer.MAX_VALUE,300));
        table.setBackground(Color.GRAY);
        table.getColumnModel().getColumn(0).setPreferredWidth(300);

        //Add to Toolbar
        toolbar.add(zoomSlide);
        toolbar.add(undo);
        toolbar.add(redo);

        //Scroll
        mapScroll = new JScrollPane(mapContainer, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        
        // Add button listeners

        //Load map action listenner
        mapFile.addActionListener(event -> {
            this.mapFromFile = true;

            final JFileChooser fc = new JFileChooser("resources/");
            FileFilter filter1 = new Utils.ExtensionFileFilter("XML", new String[] { "XML" }, "Map");
            fc.setFileFilter( filter1);

            int returnVal = fc.showOpenDialog(Gui.this);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                factory.setValidating(false);
                factory.setNamespaceAware(true);

                DocumentBuilder builder = null;
                Document document = null;
                try {
                    builder = factory.newDocumentBuilder();
                    builder.setErrorHandler(new SimpleErrorHandler());
                    document =  builder.parse(new InputSource("resources/" + file.getName()));
                } catch (Exception e) {
                    e.printStackTrace();
                }


                if (document == null) {
                    JOptionPane.showMessageDialog(this,
                            "The file requested for the map is not valid, please choose another one",
                            "ERROR",
                            JOptionPane.ERROR_MESSAGE);
                }
                else {
                    System.out.println("Opening: " + file.getName() + ".");
                    controller.loadMap(file.getName());
                    this.mapPath.setText(file.getName());
                    System.out.println("Map Loading");
                }
            } else {
                System.out.println("Opening nothing sad smiley face");
            }

            if (Variable.cityPlan == null) {
                JOptionPane.showMessageDialog(this,
                        "File doesn't exist",
                        "ERROR",
                        JOptionPane.ERROR_MESSAGE);
            } else {
                selId2 = selId1 = 0;
                mapContainer.removeAll();
                map = new MapGui(this, Variable.cityPlan, Variable.tour, controller,null, 1,mapScroll.getViewport().getSize(), selId1, selId2);
                map.setBackground(Color.lightGray);
                mapContainer.add(map,BorderLayout.CENTER);
                System.out.println("Map Loaded");
                mapContainer.validate();
                mapContainer.repaint();
                zoomSlide.setEnabled(true);
            }
        });

        //Load Requests Action Listenner
        reqFile.addActionListener(event -> {
            this.reqFromFile = true;

            final JFileChooser fc = new JFileChooser("resources/");
            FileFilter filter1 = new Utils.ExtensionFileFilter("XML", new String[] { "XML" }, "requests");
            fc.setFileFilter( filter1);

            int returnVal = fc.showOpenDialog(Gui.this);

            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                factory.setValidating(false);
                factory.setNamespaceAware(true);

                DocumentBuilder builder = null;
                Document document = null;
                try {
                    builder = factory.newDocumentBuilder();
                    builder.setErrorHandler(new SimpleErrorHandler());
                    document = builder.parse(new InputSource("resources/" + file.getName()));
                } catch (Exception e) {
                    e.printStackTrace();
                }


                System.out.println("Opening: " + file.getName() + ".");
                if (document == null) {
                    JOptionPane.showMessageDialog(this,
                            "The file requested for the map is not valid, please choose another one",
                            "ERROR",
                            JOptionPane.ERROR_MESSAGE);
                }
                else {
                    controller.loadRequests(file.getName());
                    this.reqPath.setText(file.getName());
                }
            } else {
                System.out.println("Opening nothing sad smiley face");
            }

            if (Variable.tour == null) {
                JOptionPane.showMessageDialog(this,
                        "This requests file contains intersections out of map, please load another map or another requests file",
                        "ERROR",
                        JOptionPane.ERROR_MESSAGE);
            } else {
                selId2 = selId1 = 0;
                mapContainer.removeAll();
                map = new MapGui(this, Variable.cityPlan, Variable.tour, controller, null,zoom,mapScroll.getViewport().getSize(), selId1, selId2);
                map.setBackground(Color.lightGray);
                mapContainer.add(map,BorderLayout.CENTER);
                System.out.println("Map Loaded");
                mapContainer.validate();
                mapContainer.repaint();
            }
        });

        //Calculate Best Tour Listenner
        getBestTour.addActionListener(event -> {
            mapContainer.removeAll();
            solution = controller.findBestTour();
            map = new MapGui(this, Variable.cityPlan, Variable.tour, controller, solution, zoom, mapScroll.getViewport().getSize(), selId1, selId2);
            map.setBackground(Color.lightGray);
            mapContainer.add(map,BorderLayout.CENTER);
            System.out.println("Map Loaded");
            mapContainer.validate();
            mapContainer.repaint();
            //List
            controlFlagSelectionEvent = false;
            tableCont = new TableContent(solution, Variable.tour, controller);
            // System.out.println("tableContent = "+tableContent);
            if (this.table == null){ this.table = new JTable(tableCont); }
            else{ this.table.setModel(tableCont); }
            table.getColumnModel().getColumn(0).setPreferredWidth(300);
            tableSection.validate();
            tableSection.repaint();
            controlFlagSelectionEvent = true;
        });
        
        //Stop the loading of best tour finder and give a solution
        stopAlgoButton.addActionListener(event -> {
            controller.stopAlgo();
        });
        //Changing Zoom listenner
        zoomSlide.addChangeListener( changeEvent -> {
            if (!zoomSlide.getValueIsAdjusting()) {
                zoom = (int)zoomSlide.getValue();
                Dimension tmp = map.getNewDim(zoom);
                //System.out.println("Zoom = "+zoom);
                mapContainer.removeAll();
                map = new MapGui(this, Variable.cityPlan, Variable.tour, controller, solution,zoom,mapScroll.getViewport().getSize(), selId1, selId2);
                map.setBackground(Color.lightGray);
                mapContainer.add(map,BorderLayout.CENTER);
                mapContainer.setPreferredSize(tmp);
                mapContainer.setSize(tmp);
                mapContainer.validate();
                mapContainer.repaint();
            }
        });

        //Stop the algorithm loading
        stopAlgo.addChangeListener( changeEvent -> {
            //controller.stopAlgo();
        });

        //Selection sur Liste des Trajets
        this.table.getSelectionModel().addListSelectionListener(event -> {
            if(controlFlagSelectionEvent && this.table.getSelectionModel().getAnchorSelectionIndex() != -1) {
                controlFlagSelectionEvent = false;
                this.table.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                if (!event.getValueIsAdjusting()) {
                    ListSelectionModel lsm = (ListSelectionModel) event.getSource();
                    selId1 = tableCont.getIDbyIndex(lsm.getAnchorSelectionIndex());
                    // ralenti beaucoup setInfo(controller.getAddress(Variable.cityPlan.getIntersectionById(selId1).getLatitude(), Variable.cityPlan.getIntersectionById(selId1).getLongitude()));
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
                        selId2 = tableCont.getIDbyIndex(indexCouple);
                        //Redraw Map with markers
                        mapContainer.removeAll();
                        map = new MapGui(this, Variable.cityPlan, Variable.tour, controller, solution,zoom,mapScroll.getViewport().getSize(), selId1, selId2);
                        map.setBackground(Color.lightGray);
                        mapContainer.add(map,BorderLayout.CENTER);
                        System.out.println("Map Loaded");
                        mapContainer.validate();
                        mapContainer.repaint();
                    }
                }
                controlFlagSelectionEvent = true;
            }
            this.table.validate();
            this.table.repaint();
        });

        //Deleting a couple of Delivery and pickup
        this.deleteSel.addActionListener(event -> {
            if (this.table.getSelectionModel().isSelectionEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "0 element selected",
                        "Warning",
                        JOptionPane.WARNING_MESSAGE);
            } else {
                int maxSelection = table.getSelectionModel().getMaxSelectionIndex();
                long id = tableCont.getIDbyIndex(maxSelection);
                controller.deleteSelection(table.getSelectionModel().getMaxSelectionIndex(), table.getSelectionModel().getMinSelectionIndex(), (TableContent) this.table.getModel());
                System.out.println("Index 1 : "+ table.getSelectionModel().getMinSelectionIndex() + " Index 2 : "+ table.getSelectionModel().getMaxSelectionIndex());
                Tour tour = controller.remRequest(Variable.tour,id);

                //Redraw Map
                mapContainer.removeAll();
                map = new MapGui(this, Variable.cityPlan, tour, controller, solution,zoom,mapScroll.getViewport().getSize(), selId1, selId2);
                map.setBackground(Color.lightGray);
                mapContainer.add(map,BorderLayout.CENTER);
                System.out.println("Map Loaded");
                mapContainer.validate();
                mapContainer.repaint();
            }
        });

        //Adding a couple of Delivery and pickup
        this.addStep.addActionListener(event -> {
           /* JOptionPane.showMessageDialog(this,
                    "Please click where you want to pickup a package",
                    "Create Pick-up",
                    JOptionPane.WARNING_MESSAGE);*/
            map.setAdding(true);

            //controller.addRequest(Request r = new Request(), false);
            System.out.println("Index 1 : "+ table.getSelectionModel().getMinSelectionIndex() + " Index 2 : "+ table.getSelectionModel().getMaxSelectionIndex());
        });

        undo.addActionListener(event -> {
            controller.undo();

            //Redraw Map
            mapContainer.removeAll();
            map = new MapGui(this, Variable.cityPlan, Variable.tour, controller, solution,zoom,mapScroll.getViewport().getSize(), selId1, selId2);
            map.setBackground(Color.lightGray);
            mapContainer.add(map,BorderLayout.CENTER);
            System.out.println("Map Loaded");
            mapContainer.validate();
            mapContainer.repaint();
        });

        redo.addActionListener(event -> {
            controller.redo();

            //Redraw Map
            mapContainer.removeAll();
            map = new MapGui(this, Variable.cityPlan, Variable.tour, controller, solution,zoom,mapScroll.getViewport().getSize(), selId1, selId2);
            map.setBackground(Color.lightGray);
            mapContainer.add(map,BorderLayout.CENTER);
            System.out.println("Map Loaded");
            mapContainer.validate();
            mapContainer.repaint();
        });

        //Add panels
        base.add(mapScroll,BorderLayout.CENTER);
        base.add(topBar, BorderLayout.PAGE_START);
        base.add(info, BorderLayout.WEST);
        base.add(toolbar,BorderLayout.PAGE_END);
        leftSection.add(info, BorderLayout.NORTH);
        leftSection.add(tableSection, BorderLayout.CENTER);
        tableSection.add(new JScrollPane(this.table),BorderLayout.CENTER);
        leftSection.add(leftButtonBar, BorderLayout.SOUTH);
        base.add(leftSection, BorderLayout.WEST);
        this.add(base);

        //END of Constructor
        this.setVisible(true);
    }

    public void addRequest(Intersection pickup, Intersection delivery) {
        System.out.println("483 Clear");
        Request req = controller.makeRequest(pickup,delivery);
        System.out.println("485 Clear");
        Tour tour = controller.addRequestToTour(req,Variable.tour);
        System.out.println("487 Clear");
        solution = controller.addRequest(req,false);
        //Add to Table
        System.out.println("490 Clear");
        controlFlagSelectionEvent = false;
        tableCont = new TableContent(solution, tour, controller);
        // System.out.println("tableContent = "+tableContent);
        if (this.table == null){ this.table = new JTable(tableCont); }
        else{ this.table.setModel(tableCont); }
        tableSection.validate();
        tableSection.repaint();
        controlFlagSelectionEvent = true;
        //Draw
        mapContainer.removeAll();
        map = new MapGui(this, Variable.cityPlan, tour, controller, solution,zoom,mapScroll.getViewport().getSize(), selId1, selId2);
        map.setBackground(Color.lightGray);
        mapContainer.add(map,BorderLayout.CENTER);
        System.out.println("Map Loaded");
        mapContainer.validate();
        mapContainer.repaint();

        map.setAdding(false);
    }
}

