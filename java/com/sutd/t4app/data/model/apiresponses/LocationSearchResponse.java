/**
 * The `LocationSearchResponse` class represents a response containing a list of locations with details
 * such as name, distance, bearing, and address.
 */
package com.sutd.t4app.data.model.apiresponses;
import java.util.List;

public class LocationSearchResponse {
    private List<Location> data;

    // Getters and Setters
    public List<Location> getData() {
        return data;
    }

    public void setData(List<Location> data) {
        this.data = data;
    }

    public static class Location {
        private String location_id;
        private String name;
        private String distance;
        private String bearing;
        private AddressObj address_obj;

        // Getters and Setters
        public String getLocation_id() {
            return location_id;
        }

        public void setLocation_id(String location_id) {
            this.location_id = location_id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDistance() {
            return distance;
        }

        public void setDistance(String distance) {
            this.distance = distance;
        }

        public String getBearing() {
            return bearing;
        }

        public void setBearing(String bearing) {
            this.bearing = bearing;
        }

        public AddressObj getAddress_obj() {
            return address_obj;
        }

        public void setAddress_obj(AddressObj address_obj) {
            this.address_obj = address_obj;
        }
    }

    public static class AddressObj {
        private String street1;
        private String street2;
        private String city;
        private String country;
        private String postalcode;
        private String address_string;

        // Getters and Setters
        public String getStreet1() {
            return street1;
        }

        public void setStreet1(String street1) {
            this.street1 = street1;
        }

        public String getStreet2() {
            return street2;
        }

        public void setStreet2(String street2) {
            this.street2 = street2;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        public String getPostalcode() {
            return postalcode;
        }

        public void setPostalcode(String postalcode) {
            this.postalcode = postalcode;
        }

        public String getAddress_string() {
            return address_string;
        }

        public void setAddress_string(String address_string) {
            this.address_string = address_string;
        }
    }
}

