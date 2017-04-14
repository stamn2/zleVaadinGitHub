package com.project1.controller;

import com.project1.domain.User;

public class LoginController {

    public static void addUser(String email, String firstname, String lastname, String street, String plz, String city,String tel){
        User user = new User(email, firstname, lastname, street, plz, city, tel);
        user.generatePassword(); //TODO
    }

}
