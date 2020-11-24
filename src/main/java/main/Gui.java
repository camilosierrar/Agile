package main;

import controller.Controller;

import java.awt.*;
import javax.swing.*;

public class Gui extends JFrame {
    //Declaration des attributs de la fenetre (private tata bla;)

    //Declaration des elemants graphiques de la fenetre (boutons)
    JPanel base;
    JPanel map;
    JPanel info;
    JPanel topBar;
    JButton mapRead;
    JButton reqRead;
    JButton getBestTour;
    JTextField mapPath;
    JTextField reqPath;

    //Constructor
    public Gui(Controller controller) {
        //window name
        super("Delivelov");

        //Dimensions et layout
        this.setSize(1000,600);
        this.setMinimumSize(new Dimension(1200, 600));
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(new BorderLayout()); //ou FlowLayout()

        //Panels
        base = new JPanel(new BorderLayout()); // ou FlowLayout()
        topBar = new JPanel();
        map = new JPanel();
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

        //Attributes
        base.setBackground(Color.DARK_GRAY);
        map.setBackground(new Color(0,51,102));
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

        //Add panels
        base.add(map,BorderLayout.CENTER);
        base.add(topBar, BorderLayout.PAGE_START);
        base.add(info, BorderLayout.WEST);
        this.add(base);

        //END of Constructor
        this.setVisible(true);
    }

    //methode qui dit que fait Listener
    public void  METHODE (){
        //public void  METHODE (int bout){  <-- Si plusieurs Boutons

    }
}