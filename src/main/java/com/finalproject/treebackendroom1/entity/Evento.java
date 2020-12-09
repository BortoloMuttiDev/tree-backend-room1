package com.finalproject.treebackendroom1.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.hibernate.annotations.common.util.StringHelper;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;
import java.util.UUID;

@Entity
public class Evento {

    //Attributi
    @Id
    private UUID eventid;

    //private Boolean owned;  //non dentro database ma nella view
    private String name;

    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern = "yyyy-MM-ddThh:mm:ss")
    private Date date;
    private String place;
    private Integer capacity;
    private Integer numUtentiRegistrati;


    @ManyToOne
    private Utente creatore;

    //Costruttori

    public Evento(){

    }

    public Evento(/*Boolean owned,*/UUID eventoid, String name, Timestamp date, String place, Integer capacity, Utente creatore) {
        this.eventid = eventoid;
        //this.eventid = UUID.randomUUID();
        //this.owned = owned;
        this.name = name;
        this.date = date;
        this.place = place;
        this.capacity = capacity;
        this.creatore = creatore;
        this.numUtentiRegistrati = 0;
    }

    //metodi

    public boolean aumentaNumeroPartecipanti(){
        if(numUtentiRegistrati< capacity){
            this.numUtentiRegistrati++;
            return true;
        }
        return false;
    }

    public boolean disminuisciNumeroParticipanti(){
        if(numUtentiRegistrati > 0){
            this.numUtentiRegistrati--;
            return true;
        }
        return false;
    }

    public Integer getNumUtentiRegistrati() {
        return numUtentiRegistrati;
    }

    public UUID getEventid() {
        return eventid;
    }

    public String getName() {
        return name;
    }

    public Date getDate() {
        return date;
    }

    public String getPlace() {
        return place;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public Utente getCreatore() {
        return creatore;
    }

    public void setEventid(UUID eventid) {
        this.eventid = eventid;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

}
