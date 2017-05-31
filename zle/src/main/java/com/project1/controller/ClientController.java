package com.project1.controller;

import com.project1.domain.Client;
import com.project1.entetyManager.ZLEEntityManager;

import java.util.List;

/**
 * @author Rosalie Truong & Nils Stampfli
 */
public class ClientController {

    private static ZLEEntityManager zem = new ZLEEntityManager();

    /**
     * Add a new client in the system
     * @param companyName name of the company
     * @param firstname first name of the client
     * @param lastname last name of the client
     * @param street street of the client
     * @param plz plz of the client
     * @param city city of the client
     * @param email email of the client
     * @param tel tel of the client
     * @return the new client
     */
    public static Client addClient(String companyName, String firstname, String lastname, String street, String plz,
                                   String city, String email, String tel){
        Client client = new Client(companyName, firstname, lastname, street, plz, city, email, tel); // Create client
        zem.persistObject(client); //Save in database
        return client;
    }

    /**
     * Update the details from a client
     * @param client client that need to be updated
     * @param companyName name of the company
     * @param firstname first name of the client
     * @param lastname last name of the client
     * @param street street of the client
     * @param plz plz of the client
     * @param city city of the client
     * @param email email of the client
     * @param tel tel of the client
     */
    public static void updateClient(Client client, String companyName, String firstname, String lastname, String street,
                                    String plz, String city, String email, String tel){
        zem.startTransaction(); //begin modifications in database
        client.setCompanyName(companyName);
        client.setFirstname(firstname);
        client.setLastname(lastname);
        client.setStreet(street);
        client.setPlz(plz);
        client.setCity(city);
        client.setEmail(email);
        client.setTel(tel);
        zem.endTransaction(); //end modifications in databse
    }

    /**
     * Find a client with his id
     * @param clientId id from client
     * @return the cient with the given id
     */
    public static Client getClient(long clientId) {
        return (Client)zem.findObject(Client.class, clientId);
    }

    /**
     * Get all clients
     * @return clients of the company
     */
    public static List<Client> getClients(){
        return zem.getClients();
    }
}
