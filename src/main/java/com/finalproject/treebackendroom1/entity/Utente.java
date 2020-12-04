package com.finalproject.treebackendroom1.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.finalproject.treebackendroom1.Gender;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Entity
public class Utente {
    //Attributi
    @Id
    private String username;

    private String name;
    private String surname;

    @Temporal(TemporalType.DATE)
    @JsonFormat (pattern = "yyyy-MM-dd")
    private Date birthDate;

    private Gender gender;
    private String password;

    @OneToMany
    private List<Evento> eventiCreati;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "User_Events", joinColumns = {
            @JoinColumn(name = "User_Id", referencedColumnName = "username") }, inverseJoinColumns = {
            @JoinColumn(name = "Event_Id", referencedColumnName = "eventid") })
    private List<Evento> eventiPartecipazione;


    //Costruttore
    public Utente(){

    }

    public Utente(String username, String name, String surname, Date birthDate, Gender gender, String password){
        this.username = username;
        this.name = name;
        this.surname = surname;
        this.birthDate = birthDate;
        this.gender = gender;
        this.password = password;
        this.eventiCreati = new ArrayList<>();
        this.eventiPartecipazione = new ArrayList<>();

    }

    //Metodi

    public void addEventoCreato(Evento evento){
        this.eventiCreati.add(evento);
    }

    public void addEventoPartecipazione(Evento evento){
        this.eventiPartecipazione.add(evento);
    }


    public List<Evento> getEventiCreati() {
        return eventiCreati;
    }

    public List<Evento> getEventiPartecipazione() {
        return eventiPartecipazione;
    }

    public Gender getGender() {
        return gender;
    }

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

    public String getPassword() {
        return password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public void setPassword(String password) {
        this.password = password;
    }




    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Utente utente = (Utente) o;
        return username.equals(utente.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username);
    }



}
