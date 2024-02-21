package com.app.twittercloneapi.Posts;

import java.sql.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Entity
@Table(name = "posts")
public class Post {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String content;
    private int resource;
    private Date date;
    private int user;
    private int parent;


    public Post() {
    }

    public Post(String content, int resource, Date date, int user, int parent) {
        this.content = content;
        this.resource = resource;
        this.date = date;
        this.user = user;
        this.parent = parent;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getResource() {
        return this.resource;
    }

    public void setResource(int resource) {
        this.resource = resource;
    }

    public Date getDate() {
        return this.date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getUser() {
        return this.user;
    }

    public void setUser(int user) {
        this.user = user;
    }

    public int getParent() {
        return this.parent;
    }

    public void setParent(int parent) {
        this.parent = parent;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Post)) {
            return false;
        }
        Post post = (Post) o;
        return id == post.id && Objects.equals(content, post.content) && resource == post.resource && Objects.equals(date, post.date) && Objects.equals(user, post.user) && parent == post.parent;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, content, resource, date, user, parent);
    }

    @Override
    public String toString() {
        return "{" +
            " id='" + getId() + "'" +
            ", content='" + getContent() + "'" +
            ", resource='" + getResource() + "'" +
            ", date='" + getDate() + "'" +
            ", user='" + getUser() + "'" +
            ", parent='" + getParent() + "'" +
            "}";
    }
    
    public Map<String,Object> toMap() {
        Map<String,Object> map = new HashMap<>();
        map.put("id",id);
        map.put("content", content);
        map.put("resource",resource);
        map.put("date",date);
        map.put("user",user);
        map.put("parent",parent);
        return map;
    }
}
