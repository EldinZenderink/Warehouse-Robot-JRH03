package Interface;

import jdk.internal.org.xml.sax.SAXParseException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;


public class Order {

    private Customer klant;
    private String datum;
    private String ordernummer;
    private ArrayList<String> artikelnummer;

    public Order(File xmlFile) throws NullPointerException, SAXParseException {
        if (xmlFile != null) {
            try {
                DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                Document doc = dBuilder.parse(xmlFile);

                doc.getDocumentElement().normalize();

                datum = doc.getElementsByTagName("datum").item(0).getTextContent();
                ordernummer = doc.getElementsByTagName("ordernummer").item(0).getTextContent();

                NodeList subcatklant = doc.getElementsByTagName("klant");

                for (int temp = 0; temp < subcatklant.getLength(); temp++) {
                    Node node = subcatklant.item(temp);

                    if (node.getNodeType() == Node.ELEMENT_NODE) {
                        Element eElement = (Element) node;
                        String voornaam = eElement.getElementsByTagName("voornaam").item(0).getTextContent();
                        String achternaam = eElement.getElementsByTagName("achternaam").item(0).getTextContent();
                        String adres = eElement.getElementsByTagName("adres").item(0).getTextContent();
                        String postcode = eElement.getElementsByTagName("postcode").item(0).getTextContent();
                        String plaats = eElement.getElementsByTagName("plaats").item(0).getTextContent();
                        klant = new Customer(voornaam, achternaam, adres, postcode, plaats);
                    }


                }

                NodeList artikelnr = doc.getElementsByTagName("artikelnr");
                artikelnummer = new ArrayList<>();
                for (int i = 0; i < artikelnr.getLength(); i++) {
                    artikelnummer.add(doc.getElementsByTagName("artikelnr").item(i).getTextContent());
                }


            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            } catch (SAXException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    public Customer getCustomer() {
        return klant;
    }

    public String getDatum() {
        return datum;
    }

    public ArrayList<String> getArtikelnummer() {
        return artikelnummer;
    }

    public String getOrdernummer() {
        return ordernummer;
    }
}
