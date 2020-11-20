package xml;

import java.util.Scanner;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;

public class XMLmap {

    public static Map readData() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Choose the file to load the map");
        String fileName = scanner.next();
        scanner.close();

        File xmlMap = new File("../fichiersXML2020/"+fileName);

        DocumentBuilderFactory dbFactory = new DocumentBuilderFactory();
        DocumentBuilder db = dbFactory.newDocumentBuilder();
        Document d = db.parse(xmlMap);
        System.out.print(d);

        return null;
    }
}
