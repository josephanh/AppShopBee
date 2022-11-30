package nta.nguyenanh.code_application.model;

import java.util.ArrayList;

public class User {
    private int id ;
    private String datebirth,fullname,password,phonenumber,username,userID;
    ArrayList<Address> address;

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public User() {
    }

    public User(String fullname, String username) {
        this.fullname = fullname;
        this.username = username;
    }

    public User(int id, ArrayList<Address> address, String datebirth, String fullname, String password, String phonenumber, String username, String userID) {
        this.id = id;
        this.address = address;
        this.datebirth = datebirth;
        this.fullname = fullname;
        this.password = password;
        this.phonenumber = phonenumber;
        this.username = username;
        this.userID = userID;
    }

    public User(ArrayList<Address> address, String datebirth, String fullname, String password, String phonenumber, String username, String userID) {
        this.address = address;
        this.datebirth = datebirth;
        this.fullname = fullname;
        this.password = password;
        this.phonenumber = phonenumber;
        this.username = username;
        this.userID = userID;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ArrayList<Address> getAddress() {
        return address;
    }

    public void setAddress(ArrayList<Address> address) {
        this.address = address;
    }

    public String getDatebirth() {
        return datebirth;
    }

    public void setDatebirth(String datebirth) {
        this.datebirth = datebirth;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
