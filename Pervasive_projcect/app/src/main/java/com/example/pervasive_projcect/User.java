package com.example.pervasive_projcect;

public class User {
//    private final boolean rememberMe;
    String name;
    String username;
    String email;
    String password;
    String birthdate;
    String profilePicture;
    String rememberMe;


    public String getNotFirst() {
        return notFirst;
    }

    public void setNotFirst(String notFirst) {
        this.notFirst = notFirst;
    }

    String notFirst;

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }


    public String getRememberMe() {
        return rememberMe;
    }

    public void setRememberMe(String rememberMe) {
        this.rememberMe = rememberMe;
    }

//    public User(String name, String username, String email, String password, String birthdate, String profilePicture, String rememberMe, String notFirst){
    public User(String name, String username, String email, String password, String birthdate, String profilePicture){
        this.name=name;
        this.username=username;
        this.email=email;
        this.password=password;
        this.birthdate=birthdate;
        this.profilePicture=profilePicture;
//        this.rememberMe = rememberMe;
//        this.notFirst = notFirst;
    }
    public String getName() {
        return name;
    }

    public String getUsername() {
        return username;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getBirthdate() {
        return birthdate;
    }


}
