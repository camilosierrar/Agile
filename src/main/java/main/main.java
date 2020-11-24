package main;

import controller.Controller;

import java.awt.*;

public class main {
    public static void main (String[] args){
        System.out.println("Bye world");
        //XMLmap.readData();
        //XMLrequest.readData();
        Controller controller = new Controller();
        Gui gui = new Gui(controller);
    }
}

