package main;

import controller.Controller;
import model.Intersection;
import model.Plan;
import model.Segment;
import model.Tour;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.*;
import java.util.List;


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

            //Testing
        HashMap<Long, Intersection> inter = new HashMap<Long,Intersection>();
        List<Segment> seg = new ArrayList<Segment>();

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

        map = new MapGui(plantest);
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
}