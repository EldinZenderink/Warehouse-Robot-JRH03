package asrs.Models;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;

public class XmlDocument {
    private Document xmlDoc;

    public XmlDocument(File file) throws ParserConfigurationException, SAXException, IOException {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        xmlDoc = dBuilder.parse(file);
        xmlDoc.getDocumentElement().normalize();
    }

    public NodeList getElementsByTagName(String tagName) {
        return xmlDoc.getElementsByTagName(tagName);
    }

    public Element getElementByID(String tagName, String ID) {
        int length = xmlDoc.getElementsByTagName(tagName).getLength();
        for (int i = 0; i < length; i++) {
            Node nNode = xmlDoc.getElementsByTagName(tagName).item(i);

            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) nNode;
                if (eElement.getAttribute("id").equals(ID)) {
                    return eElement;
                }
            }
        }
        return null;
    }

    public Document getXmlDoc() {
        return xmlDoc;
    }

    public static void writeToFile(File file, Document doc) throws TransformerException {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(file);

        transformer.transform(source, result);
        System.out.println("Database updated!");

    }

}
