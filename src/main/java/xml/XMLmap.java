package xml;

import model.Intersection;
import model.Request;
import model.Segment;
import model.Map;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

public class XMLmap {

    public static Map readData() {
        ArrayList<Intersection> intersectionsList = new ArrayList<>();
        ArrayList<Segment> segmentsList = new ArrayList<>();

        Scanner scanner = new Scanner(System.in);
        System.out.println("Choose the file to load the map");
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
            NodeList intersections = doc.getElementsByTagName("intersection");

            for (int temp = 0; temp < intersections.getLength(); temp++) {

                Node intersection = intersections.item(temp);

                System.out.println("\nCurrent Element :" + intersection.getNodeName());

                if (intersection.getNodeType() == Node.ELEMENT_NODE) {

                    Element eElement = (Element) intersection;

                    long id = Long.parseLong(eElement.getAttribute("id"));
                    double longitude = Double.parseDouble(eElement.getAttribute("longitude"));
                    double latitude = Double.parseDouble(eElement.getAttribute("latitude"));

                    Intersection intersectionObj = new Intersection(id, longitude, latitude);
                    intersectionsList.add(intersectionObj);

                }
            }

            // TODO read the input
            NodeList segments = doc.getElementsByTagName("intersection");

            for (int temp = 0; temp < segments.getLength(); temp++) {

                Node intersection = segments.item(temp);

                System.out.println("\nCurrent Element :" + intersection.getNodeName());

                if (intersection.getNodeType() == Node.ELEMENT_NODE) {

                    Element eElement = (Element) segments;

                    long destination = Long.parseLong(eElement.getAttribute("destination"));
                    double length = Double.parseDouble(eElement.getAttribute("length"));
                    String name = eElement.getAttribute("name");
                    long origin = Long.parseLong(eElement.getAttribute("origin"));

                    Intersection destinationObj = new Intersection(destination);
                    Intersection originObj = new Intersection(origin);

                    Segment segmentObj = new Segment(originObj, destinationObj, name, length);
                    segmentsList.add(segmentObj);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        Map map = new Map(intersectionsList, segmentsList);
        return map;
    }
}
