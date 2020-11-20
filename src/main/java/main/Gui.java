package main;

import java.awt.*;
import javax.swing.*;

public class Gui extends JFrame {
    //Declaration des attributs de la fenetre (private tata bla;)

    //Declaration des elemants graphiques de la fenetre (boutons)

    //Constructor
    public Gui() {
        //window name
        super("Title");

        //Dimensions et layout
        this.setSize(800,800);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.setLayout(new BorderLayout()); //ou FlowLayout()

        //Panels
        JPanel base = new JPanel(new BorderLayout()); // ou FlowLayout()
        JPanel map = new JPanel();
        JPanel sidebar = new JPanel();

        //Attributes (bla= new tata())
        base.setBackground(Color.DARK_GRAY);
        map.setBackground(Color.CYAN);
        sidebar.setBackground(Color.BLUE);
        //Add
        base.add(map,BorderLayout.CENTER);
        base.add(sidebar, BorderLayout.WEST);
        this.add(base);
        //bouton.addActionListener(new Listener(this,#));

        //END of Constructor
        this.setVisible(true);
    }

    //methode qui dit que fait Listener
    public void  METHODE (){
        //public void  METHODE (int bout){  <-- Si plusieurs Boutons

    }
}