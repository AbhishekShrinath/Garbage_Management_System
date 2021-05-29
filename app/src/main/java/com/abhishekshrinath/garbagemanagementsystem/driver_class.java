package com.abhishekshrinath.garbagemanagementsystem;

public class
driver_class {
    private String name,email,password,number,aadhar;

    public driver_class() {
    }

    public driver_class(String name, String email,String password, String number, String aadhar) {
        this.name = name;
        this.email = email;
        this.password=password;
        this.number = number;
        this.aadhar = aadhar;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getAadhar() {
        return aadhar;
    }

    public void setAadhar(String aadhar) {
        this.aadhar = aadhar;
    }

    @Override
    public String toString() {
        return "driver_class{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", number='" + number + '\'' +
                ", aadhar='" + aadhar + '\'' +
                '}';
    }
}
