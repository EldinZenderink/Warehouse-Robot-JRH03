package asrs.Models;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

public class Product {
    private String productId;
    private String name;
    private Location location;
    private int size;
    private int stock;
    private boolean exists = true;
    private boolean goLeft = true;

    public boolean isGoLeft() {
        return goLeft;
    }

    public void setGoLeft(boolean goLeft) {
        this.goLeft = goLeft;
    }

    public Product() {
        setProductId("0");
        setName("");
        setLocation(new Location(0, 0));
        setSize(0);
        setStock(0);
    }

    public Product(String productId) {
        this(); //fills this object with the default values.

        setProductId(productId);

        /**
         * Retrieve properties from db...
         */
        File xmlFile = new File("src/asrs/database.xml");
        try {
            XmlDocument xmlDocument = new XmlDocument(xmlFile);

            if (xmlDocument.getElementByID("product", this.productId) != null) {
                Element productElement = xmlDocument.getElementByID("product", getProductId());

                setName(productElement.getElementsByTagName("name").item(0).getTextContent());
                setSize(Integer.parseInt(productElement.getElementsByTagName("size").item(0).getTextContent()));
                setStock(Integer.parseInt(productElement.getElementsByTagName("stock").item(0).getTextContent()));

                //Retrieve location
                Node nNode = productElement.getElementsByTagName("location").item(0);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    int locationX;
                    int locationY;
                    Element eElement = (Element) nNode;
                    locationX = Integer.parseInt(eElement.getElementsByTagName("x").item(0).getTextContent());
                    locationY = Integer.parseInt(eElement.getElementsByTagName("y").item(0).getTextContent());
                    setLocation(new Location(locationX,locationY));

                }


            } else {
                this.exists = false;
            }


        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Product (int ID, String name, Location location, int size){


        setProductId(Integer.toString(ID));
        setName(name);
        setLocation(location);
        setSize(size);

    }


    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getName() {
        return name;
    }

    public boolean isExisting() {
        return exists;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String toString() {
        return "\n Product id: " + getProductId() + " | Name: " + getName() + " | Size: " + getSize() + " | Location: " + getLocation();
    }
}
