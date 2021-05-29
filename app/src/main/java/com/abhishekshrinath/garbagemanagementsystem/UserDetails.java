package com.abhishekshrinath.garbagemanagementsystem;

public class UserDetails {
    private String U_name,U_email;
    private String U_number;

    public UserDetails() {
    }

    public UserDetails(String u_name, String u_email, String u_number) {
        U_name = u_name;
        U_email = u_email;
        U_number = u_number;
    }

    public String getU_name() {
        return U_name;
    }

    public void setU_name(String u_name) {
        U_name = u_name;
    }

    public String getU_email() {
        return U_email;
    }

    public void setU_email(String u_email) {
        U_email = u_email;
    }

    public String getU_number() {
        return U_number;
    }

    public void setU_number(String u_number) {
        U_number = u_number;
    }

    @Override
    public String
    toString() {
        return "UserDetails{" +
                "U_name='" + U_name + '\'' +
                ", U_email='" + U_email + '\'' +
                ", U_number='" + U_number + '\'' +
                '}';
    }
}
