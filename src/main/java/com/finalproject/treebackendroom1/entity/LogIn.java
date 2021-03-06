package com.finalproject.treebackendroom1.entity;

import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Entity
public class LogIn {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NaturalId
    private String username;

    //non salvare cookie in database ma il valore asociado a la cookie
    private UUID cookie;

    /*
    //Podria ser util tener una lista de login pasados, agregando data
    private Date dataLogIn;
    private Boolean isValid; //solo la data mas reciente sera valido
    */

    public LogIn(){

    }

    public LogIn(String username, UUID cookie){
        this.username = username;
        this.cookie = cookie;

    }


    public String getUsername() {
        return username;
    }

    public UUID getCookie() {
        return cookie;
    }

    public void setUsername(String utente) {
        this.username = utente;
    }

    public void setCookie(UUID cookie) {
        this.cookie = cookie;
    }
}
