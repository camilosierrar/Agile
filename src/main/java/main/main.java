package main;
import model.Tour;
import xml.XMLmap;
import xml.XMLrequest;

public class main {
    public static void main (String[] args){
        System.out.println("Bye world");
        XMLmap.readData();
        Tour tour = XMLrequest.readData();
        Gui gui = new Gui();
    }
}

