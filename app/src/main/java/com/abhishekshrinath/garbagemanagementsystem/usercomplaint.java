package com.abhishekshrinath.garbagemanagementsystem;

public class usercomplaint {
    private String id,area,locality,city,email,complaint,status;

    public usercomplaint() {
    }

    public usercomplaint(String id,String area, String locality, String city, String email, String complaint,String status) {
        this.id = id;
        this.area = area;
        this.locality = locality;
        this.city = city;
        this.email = email;
        this.complaint = complaint;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getComplaint() {
        return complaint;
    }

    public void setComplaint(String complaint) {
        this.complaint = complaint;
    }
}
