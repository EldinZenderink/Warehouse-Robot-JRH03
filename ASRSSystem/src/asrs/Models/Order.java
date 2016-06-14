package asrs.Models;

import asrs.Utils.PathCalculator;
import jdk.internal.org.xml.sax.SAXParseException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;

public class Order {
    private int orderId;
    private Customer customer;
    private LocalDate date;
    private ArrayList<Product> products;
    private ArrayList<Product> notExisting = new ArrayList<>();
    private PathCalculator pathCalculator = new PathCalculator();

    public Order() {
        products = new ArrayList<>();
    }

    public ArrayList<Product> getNotExisting() {
        return notExisting;
    }

    /**
     * To initialize an order, a XML file is needed
     * @param xmlFile
     * @throws NullPointerException
     * @throws SAXParseException
     */
    public Order(File xmlFile) throws NullPointerException, SAXParseException {
        if (xmlFile != null) {
            try {
                XmlDocument xmlDocument = new XmlDocument(xmlFile);
                date = LocalDate.parse(xmlDocument.getElementsByTagName("datum").item(0).getTextContent());
                orderId = Integer.parseInt(xmlDocument.getElementsByTagName("ordernummer").item(0).getTextContent());
                NodeList nlCustommer = xmlDocument.getElementsByTagName("klant");

                for (int i = 0; i < nlCustommer.getLength(); i++) {
                    Node node = nlCustommer.item(i);
                    /**
                     * Check for valid element node
                     */
                    if (node.getNodeType() == Node.ELEMENT_NODE) {
                        Element eElement = (Element) node;
                        String firstName = eElement.getElementsByTagName("voornaam").item(0).getTextContent();
                        String surname = eElement.getElementsByTagName("achternaam").item(0).getTextContent();
                        String address = eElement.getElementsByTagName("adres").item(0).getTextContent();
                        String zipCode = eElement.getElementsByTagName("postcode").item(0).getTextContent();
                        String location = eElement.getElementsByTagName("plaats").item(0).getTextContent();
                        customer = new Customer(firstName, surname, address, zipCode, location);
                    }
                }

                NodeList productIds = xmlDocument.getElementsByTagName("artikelnr");
                products = new ArrayList<>();
                for (int i = 0; i < productIds.getLength(); i++) {
                    String productId = xmlDocument.getElementsByTagName("artikelnr").item(i).getTextContent();

                    Product product = getProductById(productId);
                    if (product.isExisting()){
                       products.add(getProductById(productId));

                    } else{
                        System.err.println("Requested Product with id " + product.getProductId() + " does not exist.");
                        notExisting.add(getProductById(productId));
                    }
                }
                // Perform rearrangement by algorithm
                rearrange();
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            } catch (SAXException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private Product getProductById(String productId) {
        Product product = new Product(productId);
        System.out.println(product);
        return product;
    }

    public void rearrange() {
        pathCalculator.setProducts(getProducts());
        pathCalculator.calculate();
        setProducts(pathCalculator.getProducts());
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public ArrayList<Product> getProducts() {
        return products;
    }

    /**
     * Get the locations of the products
     * @return
     */
    public ArrayList<Location> getLocations() {
        ArrayList<Location> locations = new ArrayList<>();
        for (Product product : getProducts()) {
            locations.add(product.getLocation());
            //subtract 1 from stock
            File xmlFile = new File("src/asrs/database.xml");
            XmlDocument xmlDocument = null;
            try {
                xmlDocument = new XmlDocument(xmlFile);
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            } catch (SAXException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (xmlDocument.getElementByID("product", product.getProductId()) != null) {
                Element productElement = xmlDocument.getElementByID("product", product.getProductId());
                int currentValue = Integer.parseInt(productElement.getElementsByTagName("stock").item(0).getTextContent());
                int newValue = currentValue - 1;
                xmlDocument.getElementByID("product", product.getProductId()).getElementsByTagName("stock").item(0).setTextContent(Integer.toString(newValue));
                try {
                    XmlDocument.writeToFile(new File("src/asrs/database.xml"), xmlDocument.getXmlDoc());
                } catch (TransformerException e) {
                    e.printStackTrace();
                }
            }
        }
        return locations;
    }

    public void setProducts(ArrayList<Product> products) {
        this.products = products;
    }

    public String toString() {
        String orderList = "";
        for (Product product : getProducts()) {
            orderList = orderList + "\n\r" + product;
        }

        return "Order id: " + getOrderId() + "\r\n" +
                "Fist name: " + customer.getFirstName() + "\r\n" +
                "Surname: " + customer.getSurname() + "\r\n" +
                "Address: " + customer.getAddress() + "\r\n" +
                "Zip Code: " + customer.getZipCode() + "\r\n" +
                "Location: " + customer.getLocation() + "\r\n" +
                "Order date: " + date + "\r\n" +
                "Products: " + orderList + "\r\n";
    }
}
