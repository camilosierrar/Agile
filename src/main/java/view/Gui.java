package view;

import controller.Controller;
import errorTesting.SimpleErrorHandler;
import model.Request;
import model.Segment;


import javax.swing.*;
import java.awt.*;
import java.io.File;
import javax.swing.filechooser.FileFilter;

import config.Variable;
import org.w3c.dom.Document;
import model.TableContent;
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

    int zoom;

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
    JButton recalculate;
    JButton getBestTour;
    JTextField mapPath;
    JTextField reqPath;
    JSlider zoomSlide;

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
        zoom = 0;
        solution = null;
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

        map = new MapGui(this, null,null, null, null, 1, null);
        info = new JTextArea(5,30);
    /*
        inter.put((long) 25175791, new Intersection((long)25175791,45.75406,4.857418));
        inter.put((long) 26057064, new Intersection((long)26057064,45.75704,4.8625107));
        inter.put((long) 26317229, new Intersection((long)26317229,45.75465,4.8674865));
        inter.put((long) 26079653, new Intersection((long) 26079653,45.758255,4.865917));
        inter.put((long) 208769145, new Intersection((long) 208769145,45.7595,4.8722763));
        inter.put((long) 208769027, new Intersection((long) 208769027,45.758717,4.8737717));
        inter.put((long) 251167391, new Intersection((long) 251167391,45.74758,4.875293));
        inter.put((long) 26079656, new Intersection((long) 26079656,45.75858,4.862883));
        inter.put((long) 26079655, new Intersection((long) 26079655,45.757935,4.8685865));
        inter.put((long) 208822503, new Intersection((long) 208822503,45.76229,4.869225));
        inter.put((long) 479185290, new Intersection((long) 479185290,45.75072,4.858164));
        inter.put((long) 54803905, new Intersection((long) 54803905,45.76194,4.8739953));
        inter.put((long) 21992964, new Intersection((long) 21992964,45.74778,4.8682485));
        inter.put((long) 208769133, new Intersection((long) 208769133,45.759453,4.8698664));
        inter.put((long) 342873658, new Intersection((long) 342873658,45.76038,4.8775625));
        inter.put((long) 342873532, new Intersection((long) 342873532,45.76051,4.8783274));
        inter.put((long) 208769499, new Intersection((long) 208769499,45.760597,4.87622));
        inter.put((long) 975886496, new Intersection((long) 975886496,45.756874,4.8574047));
        inter.put((long) 1036842114, new Intersection((long) 1036842114,45.74993,4.8595204));
        inter.put((long) 26033324, new Intersection((long) 26033324,45.757076,4.857407));
        inter.put((long) 2034547834, new Intersection((long) 2034547834,45.749058,4.8610687));
        inter.put((long) 25336247, new Intersection((long) 25336247,45.75039,4.8738017));
        inter.put((long) 18697372, new Intersection((long) 18697372,45.747116,4.859554));
        inter.put((long) 55475052, new Intersection((long) 55475052,45.758472,4.8751354));
        inter.put((long) 474605986, new Intersection((long) 474605986,45.747997,4.864131));
        //inter.put((long) 3267479470, new Intersection((long) 3267479470,45.75974,4.872321));
        inter.put((long) 21992996, new Intersection((long) 21992996,45.747612,4.86394));
        inter.put((long) 208769180, new Intersection((long) 208769180,45.759502,4.872345));
        inter.put((long) 474605980, new Intersection((long) 474605980,45.74871,4.8645));
        inter.put((long) 21992995, new Intersection((long) 21992995,45.74773,4.8634377));
        //inter.put((long) 2383442941, new Intersection((long) 2383442941,45.754566,4.861602));

        seg.add(new Segment(new Intersection((long)25175791,45.75406,4.857418),
                new Intersection((long)26057064, 45.75704,4.8625107), "Avenue du Tonkin", 2000));

        seg.add(new Segment(new Intersection((long) 55475052,45.758472,4.8751354),
                new Intersection((long) 208769180,45.759502,4.872345), "Rue de la Paix", 4000));

        seg.add(new Segment(new Intersection((long) 26033324,45.757076,4.857407)
                , new Intersection((long) 342873532,45.76051,4.8783274), "Boulevard  du roi", 3000));
        seg.add(new Segment(new Intersection((long) 251167391,45.74758,4.875293)
                ,new Intersection((long) 54803905,45.76194,4.8739953), "Impasse de la Servigne", 2000));


        Plan plantest = Plan.createPlan(inter, seg);
        */

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
        getBestTour = new JButton("Find Best Tour");
        deleteSel = new JButton("Delete selection");
        addStep = new JButton("Add a step");
        recalculate = new JButton("Recalculate itinerary");

        //Slider (Zoom)
        zoomSlide = new JSlider();
        zoomSlide.setMajorTickSpacing(10);
        zoomSlide.setMinorTickSpacing(5);
        zoomSlide.setSnapToTicks(true);
        zoomSlide.setValue(1);
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
        leftButtonBar.add(recalculate);

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

        //Add to info
        info.setText(temp);
        tableSection.setMaximumSize(new Dimension(Integer.MAX_VALUE,300));
        table.setBackground(Color.GRAY);

        //Add to Toolbar
        toolbar.add(zoomSlide);

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
                mapContainer.removeAll();
                map = new MapGui(this, Variable.cityPlan, Variable.tour, controller,null, 1,mapScroll.getViewport().getSize());
                map.setBackground(Color.lightGray);
                mapContainer.add(map,BorderLayout.CENTER);
                System.out.println("Map Loaded");
                mapContainer.validate();
                mapContainer.repaint();
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
                        "File doesn't exist",
                        "ERROR",
                        JOptionPane.ERROR_MESSAGE);
            } else {
                mapContainer.removeAll();
                map = new MapGui(this, Variable.cityPlan, Variable.tour, controller, null,zoom,mapScroll.getViewport().getSize());
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
            map = new MapGui(this, Variable.cityPlan, Variable.tour, controller, solution, zoom, mapScroll.getViewport().getSize());
            map.setBackground(Color.lightGray);
            mapContainer.add(map,BorderLayout.CENTER);
            System.out.println("Map Loaded");
            mapContainer.validate();
            mapContainer.repaint();

            controlFlagSelectionEvent = false;
            TableContent table = new TableContent(solution, Variable.tour);
            // System.out.println("tableContent = "+tableContent);
            if (this.table == null){ this.table = new JTable(table); }
            else{ this.table.setModel(table); }
            tableSection.validate();
            tableSection.repaint();
            controlFlagSelectionEvent = true;
        });
        //Changing Zoom listenner
        zoomSlide.addChangeListener( changeEvent -> {
            if (!zoomSlide.getValueIsAdjusting()) {
                zoom = (int)zoomSlide.getValue();
                Dimension tmp = map.getNewDim(zoom);
                //System.out.println("Zoom = "+zoom);
                mapContainer.removeAll();
                map = new MapGui(this, Variable.cityPlan, Variable.tour, controller, solution,zoom,mapScroll.getViewport().getSize());
                map.setBackground(Color.lightGray);
                mapContainer.add(map,BorderLayout.CENTER);
                mapContainer.setPreferredSize(tmp);
                mapContainer.setSize(tmp);
                mapContainer.validate();
                mapContainer.repaint();
            }
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

        //Deleting a couple of Delivery and pickup
        this.deleteSel.addActionListener(event -> {
            if (this.table.getSelectionModel().isSelectionEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "0 element selected",
                        "Warning",
                        JOptionPane.WARNING_MESSAGE);
            } else {
                controller.deleteSelection(table.getSelectionModel().getMaxSelectionIndex(), table.getSelectionModel().getMinSelectionIndex(), (TableContent) this.table.getModel());
                System.out.println("Index 1 : "+ table.getSelectionModel().getMinSelectionIndex() + " Index 2 : "+ table.getSelectionModel().getMaxSelectionIndex());
            }
        });

        //Adding a couple of Delivery and pickup
        this.addStep.addActionListener(event -> {
            JOptionPane.showMessageDialog(this,
                    "Please click where you want to pickup a package",
                    "Create Pick-up",
                    JOptionPane.WARNING_MESSAGE);

            //controller.addRequest(Request r = new Request(), false);
            System.out.println("Index 1 : "+ table.getSelectionModel().getMinSelectionIndex() + " Index 2 : "+ table.getSelectionModel().getMaxSelectionIndex());

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


    /**
     * In map.html file default zoom value is set to 13.
     */

   /* private void setMap(JPanel base) {
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
    }*/
}

