package main;

import java.awt.*;
import javax.swing.*;

public class Gui extends JFrame {
    //Declaration des attributs de la fenetre (private tata bla;)

    //Declaration des elemants graphiques de la fenetre (boutons)

    //constructeur
    public Gui() {
        //nom qu'aura la fenetre
        super("Title");

        //Dimensions et layout
        this.setSize(800,800);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.setLayout(new BorderLayout()); //ou FlowLayout()

        //creainstanciation des panels
        JPanel p0 = new JPanel(new BorderLayout()); // ou FlowLayout()

        //instanciation des attributs (bla= new tata())

        //ajout des panels (/XChartPanel) a la fenetre this.add(panel);

        //ajout des boutons/label/textarea... au JPanel   panel.add(bla);

        //bouton.addActionListener(new Listener(this,#));

        //Rendre la fenêtre visible  FIN CONSTRUCTEUR
        this.setVisible(true);

    }

    //methode qui dit que fait Listener
    public void  METHODE (){
        //public void  METHODE (int bout){  <-- Si plusieurs Boutons

    }
}