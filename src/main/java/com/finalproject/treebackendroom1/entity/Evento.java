package com.finalproject.treebackendroom1.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.hibernate.annotations.common.util.StringHelper;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.sql.Timestamp;
import java.util.UUID;

@Entity
public class Evento {

    //Attributi
    @Id
    private UUID eventid;

    private Boolean owned;
    private String name;

    @JsonFormat(pattern = "yyyy-MM-dd'T'hh:mm:ss")
    private Timestamp date;
    private String place;
    private Integer capacity;

    @ManyToOne
    private Utente creatore;

    //Costruttori

    public Evento(){

    }

    public Evento(UUID eventid, Boolean owned, String name, Timestamp date, String place, Integer capacity, Utente creatore) {
        this.eventid = eventid;
        this.owned = owned;
        this.name = name;
        this.date = date;
        this.place = place;
        this.capacity = capacity;
        this.creatore = creatore;
    }

    //metodi


    public UUID getEventid() {
        return eventid;
    }

    public Boolean getOwned() {
        return owned;
    }

    public String getName() {
        return name;
    }

    public Timestamp getDate() {
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

    public void setOwned(Boolean owned) {
        this.owned = owned;
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
