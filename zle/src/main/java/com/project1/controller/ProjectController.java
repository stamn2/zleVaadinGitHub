package com.project1.controller;

import com.project1.domain.Client;
import com.project1.domain.Employee;
import com.project1.entetyManager.ZLEEntityManager;

import java.util.ArrayList;
import java.util.List;

public class ProjectController {
    private static ZLEEntityManager zem = new ZLEEntityManager();

    public static void addClient(String companyName, String firstname, String lastname, String street, String plz, String city, String email, String tel){
        Client client = new Client(companyName, firstname, lastname, street, plz, city, email, tel);
        zem.persistObject(client);
    }

    public static List<Client> getClients(){
        //return zem.getClients(); TODO
        return new ArrayList<>();
    }

}
