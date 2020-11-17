package xml;

import model.Tour;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.Scanner;

public class XMLrequest {


    public static Tour readData() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Choose the file to load the requests");
        String file = scanner.next();
        scanner.close();

        try {

            File fXmlFile = new File(file);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(fXmlFile);
            doc.getDocumentElement().normalize();

            //System.out.println("Root element :" + doc.getDocumentElement().getNodeName());

            // TODO read the input
            NodeList nList = doc.getElementsByTagName("staff");
        } catch (Exception e) {
            e.printStackTrace();
        }


        return null;
    }
}
