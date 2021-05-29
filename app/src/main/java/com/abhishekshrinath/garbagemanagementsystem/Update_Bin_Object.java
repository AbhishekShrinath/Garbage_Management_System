package com.abhishekshrinath.garbagemanagementsystem;

public class Update_Bin_Object {
    private String id,area,locality,updatelandmark,city,load,Driver,cleaning,route,latitude,longtiude,status,workcompletetime;

    public Update_Bin_Object() {
    }

    public Update_Bin_Object(String id,String area, String locality, String updatelandmark, String city, String load, String driver, String cleaning, String route, String latitude, String longtiude,String status,String workcompletetime) {
        this.id = id;
        this.area = area;
        this.locality = locality;
        this.updatelandmark = updatelandmark;
        this.city = city;
        this.load = load;
        this.Driver = driver;
        this.workcompletetime = workcompletetime;
        this.cleaning = cleaning;
        this.route = route;
        this.latitude = latitude;
        this.longtiude = longtiude;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getUpdatelandmark() {
        return updatelandmark;
    }

    public void setUpdatelandmark(String landmark) {
        this.updatelandmark = updatelandmark;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getLoad() {
        return load;
    }

    public void setLoad(String load) {
        this.load = load;
    }

    public String getDriver() {
        return Driver;
    }

    public void setDriver(String driver) {
        Driver = driver;
    }

    public String getCleaning() {
        return cleaning;
    }

    public void setCleaning(String cleaning) {
        this.cleaning = cleaning;
    }

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongtiude() {
        return longtiude;
    }

    public void setLongtiude(String longtiude) {
        this.longtiude = longtiude;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getWorkcompletetime() {
        return workcompletetime;
    }

    public void setWorkcompletetime(String workcompletetime) {
        this.workcompletetime = workcompletetime;
    }

    @Override
    public String toString() {
        return "Update_Bin_Object{" +
                "area='" + area + '\'' +
                ", locality='" + locality + '\'' +
                ", updatelandmark='" + updatelandmark + '\'' +
                ", city='" + city + '\'' +
                ", load='" + load + '\'' +
                ", Driver='" + Driver + '\'' +
                ", cleaning='" + cleaning + '\'' +
                ", route='" + route + '\'' +
                '}';
    }
}
