package view;

import controller.Controller;

import java.io.IOException;

/**
 * The main function that is called to launch the application
 */
public class main {
    public static void main (String[] args){
        Controller controller = new Controller();
        Gui gui = new Gui(controller);
        /*try {
            controller.getAddress(45.7,4.8790674);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/
    }
}

