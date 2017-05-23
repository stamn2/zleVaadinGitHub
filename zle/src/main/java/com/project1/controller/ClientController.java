package com.project1.controller;

import com.project1.domain.Client;
import com.project1.entetyManager.ZLEEntityManager;

import java.util.List;

public class ClientController {

    private static ZLEEntityManager zem = new ZLEEntityManager();

    public static Client addClient(String companyName, String firstname, String lastname, String street, String plz,
                                   String city, String email, String tel){
        Client client = new Client(companyName, firstname, lastname, street, plz, city, email, tel);
        zem.persistObject(client);
        return client;
    }

    public static void updateClient(Client client, String companyName, String firstname, String lastname, String street,
                                    String plz, String city, String email, String tel){
        zem.startTransaction();
        client.setCompanyName(companyName);
        client.setFirstname(firstname);
        client.setLastname(lastname);
        client.setStreet(street);
        client.setPlz(plz);
        client.setCity(city);
        client.setEmail(email);
        client.setTel(tel);
        zem.endTransaction();
    }

    public static Client getClient(long clientId) {
        return (Client)zem.findObject(Client.class, clientId);
    }

    public static List<Client> getClients(){
        return zem.getClients();
    }
}
