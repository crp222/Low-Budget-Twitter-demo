package com.app.twittercloneapi.Messages.Message;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Table(name = "message")
@Entity
public class Message {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    @Column(columnDefinition = "mediumtext")
    private String messsage;
    
    @Column(name = "group_id")
    private int group;
    private int user;

    public String getMesssage() {
        return this.messsage;
    }

    public void setMesssage(String messsage) {
        this.messsage = messsage;
    }

    public int getGroup() {
        return this.group;
    }

    public void setGroup(int group) {
        this.group = group;
    }

    public int getUser() {
        return this.user;
    }

    public void setUser(int user) {
        this.user = user;
    }

}
