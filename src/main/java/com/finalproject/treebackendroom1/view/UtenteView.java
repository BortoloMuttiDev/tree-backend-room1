package com.finalproject.treebackendroom1.view;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.finalproject.treebackendroom1.Gender;

import java.util.Date;

public class UtenteView {
    //Attributi
    private String username;
    private String name;
    private String surname;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date birthDate;
    private Gender gender;
    private String password;

    //Costruttore
    public UtenteView(String username, String name, String surname, Date birthDate, Gender gender, String password){
        this.username = username;
        this.name = name;
        this.surname = surname;
        this.birthDate = birthDate;
        this.gender = gender;
        this.password = password;

    }


    //Metodi


    public String getUsername() {
        return username;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public Gender getGender() {
        return gender;
    }

    public String getPassword() {
        return password;
    }
}
