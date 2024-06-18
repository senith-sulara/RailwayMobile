package com.example.railridemate.models;

public class UserModel {

    private String uid;
    private String address;
    private String email;

    private String gender;

    private String name;
    private String tel;


    public UserModel(String uid, String address, String email, String gender, String name, String tel) {
        this.uid = uid;
        this.address = address;
        this.email = email;
        this.gender = gender;
        this.name = name;
        this.tel = tel;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public UserModel() {
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }
}
