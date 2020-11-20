package xml;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;
import model.Map;


public class XMLmap {

    public static Map readData() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Choose the file to load the map");
        String fileName = scanner.next();
        scanner.close();

        File xmlMap = new File("../fichiersXML2020/" + fileName);
        Map carte;
        if (xmlMap.isFile()) {
            FileReader fr;
            String documentString = "";
            try {
                fr = new FileReader(xmlMap);
                int ch;
                while ((ch = fr.read()) != -1) {
                    documentString += (char) ch;
                }

            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }

        return null;
    }
}
