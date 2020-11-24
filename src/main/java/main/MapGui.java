package main;

import model.Plan;

import java.awt.*;
import javax.swing.*;

public class MapGui extends JPanel {

    int width;
    int height;
    Plan plan;
    public MapGui() {

    }
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.fillOval(20,20,20,20);
    }

}