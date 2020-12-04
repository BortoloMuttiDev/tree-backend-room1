package com.finalproject.treebackendroom1.entity;

import javax.persistence.*;

@Entity
public class Utente {





    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "POST_COMMENTS", joinColumns = {
            @JoinColumn(name = "POST_ID", referencedColumnName = "ID") }, inverseJoinColumns = {
            @JoinColumn(name = "COMMENT_ID", referencedColumnName = "ID") })
    private List<Comment> comments;

}
