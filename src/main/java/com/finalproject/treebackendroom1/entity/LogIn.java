package com.finalproject.treebackendroom1.entity;

import javax.persistence.*;
import java.util.UUID;

@Entity
public class LogIn {

    @Id
    private Utente utente;

    //non salvare cookie in database ma il valore asociado a la cookie
    private UUID cookie;


    public LogIn(){

    }

    public LogIn(Utente utente, UUID cookie){
        this.utente = utente;
        this.cookie = cookie;

    }


    public Utente getUsername() {
        return utente;
    }

    public UUID getCookie() {
        return cookie;
    }

    public void setUsername(Utente utente) {
        this.utente = utente;
    }

    public void setCookie(UUID cookie) {
        this.cookie = cookie;
    }
}
