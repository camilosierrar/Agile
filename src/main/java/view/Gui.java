package view;

import controller.Controller;
<<<<<<< HEAD
import model.Segment;
=======
import model.Plan;
import model.Segment;
import model.Tour;
>>>>>>> 6efcd1bc44e3c4dcd01dcc93a555cd0ba8285d2e


import javax.swing.*;
import java.awt.*;
import java.io.File;
import javax.swing.filechooser.FileFilter;
import java.util.List;

import config.Variable;

import java.util.List;
import javax.swing.*;
import javax.swing.event.ChangeEvent;


//import static com.teamdev.jxbrowser.engine.RenderingMode.HARDWARE_ACCELERATED;


public class Gui extends JFrame {

    //Util
    Boolean mapFromFile;
    Boolean reqFromFile;

    int zoom;

    //Solution
    List<Segment> solution;

    //Graphic Elements
    JPanel base;
    JTextArea info;
    //JPanel info;
    JPanel topBar;
    MapGui map;
    JPanel toolbar;
    JPanel mapContainer;
    JScrollPane mapScroll;
    JButton mapRead;
    JButton reqRead;
    JButton mapFile;
    JButton reqFile;
    JButton getBestTour;
    JTextField mapPath;
    JTextField reqPath;
    JSlider zoomSlide;
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
        map = new MapGui(null,null, null, null, null,1,null);
        //info = new JPanel();

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
        mapRead = new JButton("Load Map");
        reqRead = new JButton("Load Requests");
        mapFile = new JButton("Load Map file");
        reqFile = new JButton("Load Requests file");
        getBestTour = new JButton("Find Best Tour");

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
        info.setMaximumSize(new Dimension(300,Integer.MAX_VALUE));

        //Add to mapContainer
        mapContainer.add(map,BorderLayout.CENTER);

        //Add to topBar
            //MapReading
        topBar.add(mapReadLabel);
        topBar.add(mapPath);
        topBar.add(mapRead);
        topBar.add(mapFile);
            //ReqReading
        topBar.add(reqReadLabel);
        topBar.add(reqPath);
        topBar.add(reqRead);
        topBar.add(reqFile);
            //getBestTour
        topBar.add(getBestTour);

        //Add to info
        info.setText(temp);
        
        //Add to Toolbar
        toolbar.add(zoomSlide);

        //Scroll
        mapScroll = new JScrollPane(mapContainer, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        
        // Add button listeners

        mapFile.addActionListener(event -> {
            this.mapFromFile = true;


            final JFileChooser fc = new JFileChooser("resources/");
            FileFilter filter1 = new Utils.ExtensionFileFilter("XML", new String[] { "XML" }, "Map");
            fc.setFileFilter( filter1);

            int returnVal = fc.showOpenDialog(Gui.this);

            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
                System.out.println("Opening: " + file.getName() + ".");
                controller.loadMap(file.getName());
                this.mapPath.setText(file.getName());
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

        mapRead.addActionListener(event -> {
            controller.loadMap(mapPath.getText());

            if (Variable.cityPlan == null) {
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

        reqFile.addActionListener(event -> {
            this.reqFromFile = true;

            final JFileChooser fc = new JFileChooser("resources/");
            FileFilter filter1 = new Utils.ExtensionFileFilter("XML", new String[] { "XML" }, "requests");
            fc.setFileFilter( filter1);

            int returnVal = fc.showOpenDialog(Gui.this);

            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
                System.out.println("Opening: " + file.getName() + ".");
                controller.loadRequests(file.getName());
                this.reqPath.setText(file.getName());
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

        reqRead.addActionListener(event -> {
            controller.loadRequests(reqPath.getText());

            if (Variable.tour == null) {
                JOptionPane.showMessageDialog(this,
                        "File doesn't exist",
                        "ERROR",
                        JOptionPane.ERROR_MESSAGE);
            } else {
                mapContainer.removeAll();
                //solution = controller.findBestTour(tour);
                solution = controller.findBestTour();
                map = new MapGui(this, Variable.cityPlan, Variable.tour, controller, solution,zoom,mapScroll.getViewport().getSize());
                map.setBackground(Color.lightGray);
                mapContainer.add(map,BorderLayout.CENTER);
                System.out.println("Map Loaded");
                mapContainer.validate();
                mapContainer.repaint();
            }
        });

        getBestTour.addActionListener(event -> {
            mapContainer.removeAll();
            List<Segment> solution = controller.findBestTour();
            map = new MapGui(this, plan, tour, controller, solution);
            map.setBackground(Color.lightGray);
            mapContainer.add(map,BorderLayout.CENTER);
            System.out.println("Map Loaded");
            mapContainer.validate();
            mapContainer.repaint();

        });

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

        //Add panels
        base.add(mapScroll,BorderLayout.CENTER);
        base.add(topBar, BorderLayout.PAGE_START);
        base.add(info, BorderLayout.WEST);
        base.add(toolbar,BorderLayout.PAGE_END);
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

