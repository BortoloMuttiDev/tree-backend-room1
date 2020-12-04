package com.finalproject.treebackendroom1.view;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.sql.Timestamp;
import java.util.UUID;

public class EventoView {

    //Attributi
    private UUID eventid;
    private Boolean owned;
    private String name;

    @JsonFormat(pattern = "yyyy-MM-dd'T'hh:mm:ss")
    private Timestamp date;
    private String place;
    private Integer capacity;

    //Costruttore

    public EventoView(UUID eventid, String name, Timestamp date, String place, Integer capacity){
        this.eventid = eventid;
        this.name = name;
        this.date = date;
        this.place = place;
        this.capacity = capacity;

    }

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

    public void setOwned(Boolean owned) {
        this.owned = owned;
    }
}
