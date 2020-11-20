package xml;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;  
import org.w3c.dom.Node;  
import org.w3c.dom.Element;

import model.Plan;

public class XMLmap {

    public static Plan readData() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Choose the file to load the map");
        String fileName = scanner.next();
        scanner.close();

        File xmlMap = new File("../fichiersXML2020/" + fileName);
        Plan carte;
        /*if (xmlMap.isFile()) {
            FileReader fr;
            String documentString = "";
            try {
                fr = new FileReader(xmlMap);
                int ch;
                while ((ch = fr.read()) != -1)
                    documentString += (char) ch;
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }*/
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db;
        Document doc = null;
        try {
            db = dbf.newDocumentBuilder();
            doc = db.parse(xmlMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        doc.getDocumentElement().normalize();  

        NodeList intersections = doc.getElementsByTagName("intersection"); 
        for (int i = 0; i < intersections.getLength(); i++){  
            Node node = intersections.item(i);
            System.out.println("\nNode Name :" + node.getNodeName());  
            if (node.getNodeType() == Node.ELEMENT_NODE){  
                Element intersection_i = (Element) node;
                System.out.println("Student id: "+ intersection_i.getAttribute("id");    
            }
        }
        return null;
    }
}
