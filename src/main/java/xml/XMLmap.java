package xml;

import model.Intersection;
import model.Segment;
import model.Plan;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import config.Variable;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;


/**
 * The class that will allow us to extract all the informations from the XML File
 * to add data to our map
 * and checking that the file is correct
 */
public class XMLmap {

    public static void readData(String fileName) {
        HashMap<Long, Intersection> intersectionsList = new HashMap<>();
        ArrayList<Segment> segmentsList = new ArrayList<>();

        try {

            File fXmlFile = new File("resources/" + fileName );//fileName);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(fXmlFile);
            doc.getDocumentElement().normalize();

            // READ INTERSECTIONS
            NodeList intersections = doc.getElementsByTagName("intersection");
            for (int temp = 0; temp < intersections.getLength(); temp++) {
                Node intersection = intersections.item(temp);

                if (intersection.getNodeType() == Node.ELEMENT_NODE) {

                    Element elem = (Element) intersection;

                    long id = Long.parseLong(elem.getAttribute("id"));
                    double longitude = Double.parseDouble(elem.getAttribute("longitude"));
                    double latitude = Double.parseDouble(elem.getAttribute("latitude"));

                    Intersection intersectionObj = new Intersection(id, longitude, latitude);
                    intersectionsList.put(intersectionObj.getId(), intersectionObj);

                }
            }

            // READ SEGMENTS
            NodeList segments = doc.getElementsByTagName("segment");

            for (int temp = 0; temp < segments.getLength(); temp++) {
                Node segment = segments.item(temp);

                if (segment.getNodeType() == Node.ELEMENT_NODE) {
                    
                    Element elem = (Element) segment;

                    long destination = Long.parseLong(elem.getAttribute("destination"));
                    double length = Double.parseDouble(elem.getAttribute("length"));
                    String name = elem.getAttribute("name");
                    long origin = Long.parseLong(elem.getAttribute("origin"));

                    Intersection destinationObj = intersectionsList.get(destination);
                    Intersection originObj = intersectionsList.get(origin);

                    Segment segmentObj = new Segment(originObj, destinationObj, name, length);
                    segmentsList.add(segmentObj);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        Variable.cityPlan = Plan.createPlan(intersectionsList, segmentsList);
        return;
    }
}
