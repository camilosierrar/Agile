package xml;

import model.Intersection;
import model.Request;
import model.Tour;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
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
            NodeList requests = doc.getElementsByTagName("request");

            for (int temp = 0; temp < requests.getLength(); temp++) {

                Node request = requests.item(temp);

                System.out.println("\nCurrent Element :" + request.getNodeName());

                if (request.getNodeType() == Node.ELEMENT_NODE) {

                    Element eElement = (Element) request;

                    long pickupAddressId = Long.parseLong(eElement.getAttribute("pickupAddress"));
                    long deliveryAddressId = Long.parseLong(eElement.getAttribute("deliveryAddress"));
                    int pickupDuration = Integer.parseInt(eElement.getAttribute("pickupDuration"));
                    int deliveryDuration = Integer.parseInt(eElement.getAttribute("deliveryDuration"));

                    Intersection pickupIntersection = new Intersection(pickupAddressId);
                    Intersection deliveryIntersection = new Intersection(deliveryAddressId);

                    Request requestObj = new Request(pickupIntersection, deliveryIntersection, pickupDuration, deliveryDuration);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        return null;
    }
}
