package com.app.twittercloneapi.User;

import java.sql.Blob;
import java.util.Base64;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "user")
public class UserInfo implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String email;

    @Column(name="displayed_name")
    private String displayedName;
    
    @Column(name="profile_picture")
    private Blob profilePicture;

    private String username;

    private String password;

    @Column(name="authority")
    private String role = "USER";

    @Column(name = "enabled")
    private boolean isEnabled = true;

    public UserInfo() {
    }

    public UserInfo(String email, String displayedName, Blob profilePicture, String username, String password) {
        this.email = email;
        this.displayedName = displayedName;
        this.profilePicture = profilePicture;
        this.username = username;
        this.password = password;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDisplayedName() {
        return this.displayedName;
    }

    public void setDisplayedName(String displayedName) {
        this.displayedName = displayedName;
    }

    public Blob getProfilePicture() {
        return this.profilePicture;
    }

    public void setProfilePicture(Blob profilePicture) {
        this.profilePicture = profilePicture;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<GrantedAuthority> authoritySet = new HashSet<GrantedAuthority>();
        authoritySet.add(new SimpleGrantedAuthority(this.role));
        return authoritySet;
    }

    public boolean isIsEnabled() {
        return this.isEnabled;
    }

    public boolean getIsEnabled() {
        return this.isEnabled;
    }

    public void setIsEnabled(boolean isEnabled) {
        this.isEnabled = isEnabled;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.isEnabled;
    }

    public Map<String,Object> toMap() {
        Map<String,Object> result = new HashMap<>();
        result.put("email",email);
        result.put("username",username);
        result.put("displayed_name",displayedName);
        result.put("role",role);
        String base64_profilepic = "";
        try {
            base64_profilepic = Base64.getEncoder().encodeToString(profilePicture.getBytes(1,(int)profilePicture.length()));
        }catch(Exception e){}
        result.put("profile_picture",base64_profilepic);
        return result;
    }

}
