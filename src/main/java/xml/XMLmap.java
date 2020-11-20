package xml;

import java.util.Scanner;
import model.Map;

public class XMLmap {

    public static Map readData() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Choose the file to load the map");
        String myString = scanner.next();
        scanner.close();

        return null;
    }
}
