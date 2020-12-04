package xml;

import model.Intersection;
import model.Plan;
import model.Request;
import model.Tour;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;

public class XMLrequest {

    public static Tour readData(String filename) {

        Tour tour = null;

        try {
            LinkedList<Request> requestsList = new LinkedList<>();

            File fXmlFile = new File("resources/" + "requests"+"Large9"+".xml");// filename);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(fXmlFile);
            doc.getDocumentElement().normalize();

            // Get all requests
            NodeList requests = doc.getElementsByTagName("request");
            for (int temp = 0; temp < requests.getLength(); temp++) {

                Node request = requests.item(temp);

                if (request.getNodeType() == Node.ELEMENT_NODE) {

                    Element eElement = (Element) request;

                    long pickupAddressId = Long.parseLong(eElement.getAttribute("pickupAddress"));
                    long deliveryAddressId = Long.parseLong(eElement.getAttribute("deliveryAddress"));
                    int pickupDuration = Integer.parseInt(eElement.getAttribute("pickupDuration"));
                    int deliveryDuration = Integer.parseInt(eElement.getAttribute("deliveryDuration"));

                    Intersection pickupIntersection = Plan.plan.getIntersectionById(pickupAddressId);
                    Intersection deliveryIntersection = Plan.plan.getIntersectionById(deliveryAddressId);

                    Request requestObj = new Request(pickupIntersection, deliveryIntersection, pickupDuration, deliveryDuration);
                    requestsList.add(requestObj);
                }
            }

            // Get departure time and address
            Node depotNode = doc.getElementsByTagName("depot").item(0);

            if (depotNode.getNodeType() == Node.ELEMENT_NODE) {
                Element depot = (Element) depotNode;
                Long depotAdress = Long.parseLong(depot.getAttribute("address"));
                String depotTime = depot.getAttribute("departureTime");
                Intersection departureIntersection = Plan.plan.getIntersectionById(depotAdress);

                String[] parts = depotTime.split(":");
                int hour = Integer.parseInt(parts[0]);
                int minutes = Integer.parseInt(parts[1]);
                int seconds = Integer.parseInt(parts[2]);

                Calendar cal = Calendar.getInstance();
                cal.set(Calendar.HOUR_OF_DAY, hour);
                cal.set(Calendar.MINUTE, minutes);
                cal.set(Calendar.SECOND, seconds);
                Date departureDate = cal.getTime();

                tour = new Tour(departureIntersection, departureDate, requestsList);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return tour;
    }
}
