/**
 * #------ Customer class ------#
 * This clas is unecessary to the TSP
 * simulation. This class is only here
 * because we need to be able to read
 * an order form an XML file for our
 * school project.
 */

package Interface;

public class Customer {

    private String firstname;
    private String lastname;
    private String address;
    private String zipcode;
    private String city;

    public Customer(String firstname, String lastname, String address, String zipcode, String city) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.address = address;
        this.zipcode = zipcode;
        this.city = city;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getAddress() {
        return address;
    }

    public String getZipcode() {
        return zipcode;
    }

    public String getCity() {
        return city;
    }

}