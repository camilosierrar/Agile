package view;

import controller.Controller;

public class main {
    public static void main (String[] args){
        System.out.println("Bye world");
        Controller controller = new Controller();
        Gui gui = new Gui(controller);
    }
}

