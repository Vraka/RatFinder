package com.smokegod.cs2340.m3;

import java.io.PipedInputStream;
import java.io.Serializable;

/**
 * Created by Vraka on 10/10/2017.
 */

public class RatSighting implements Serializable {
    private String key, date, loc_type, zip, address, city, borough, latitude, longitude;


    /**
     * Constructor class to create RatSighting object
     *
     * @param key Unique key of the sighting
     * @param date Creation date of the sighting
     * @param loc_type Location type of the sighting
     * @param zip Zip code of the sighting
     * @param address Address of the sighting
     * @param city City of the sighting
     * @param borough Borough of the sighting
     * @param latitude Latitude of the sighting
     * @param longitude Longitude of the sighting
     */
    public RatSighting(String key, String date, String loc_type, String zip, String address, String city, String borough, String latitude, String longitude) {
        this.key = key;
        this.date = date;
        this.loc_type = loc_type;
        if(zip != null) {
            this.zip = zip;
        } else {
            throw new java.lang.IllegalArgumentException("Cannot have a null zip");
        }
        if(address != null) {
            this.address = address;
        } else {
            throw new java.lang.IllegalArgumentException("Cannot have a null address");
        }
        this.city = city;
        this.borough = borough;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String toString() {
        String s = "Unique Key: " + key + "\n"
                + "Date Created: " + date + "\n"
                + "Address: " + address;
        return s;
    }

    /**
     * Returns the Unique key of the sighting object
     *
     * @return Unique key of the sighting
     */
    public String getKey() {
        return key;
    }


    /**
     * Sets the value of the unique key of the sighting object
     *
     * @param key Unique key to be assigned to the object
     */
    public void setKey(String key) {
        if(key != null) {
            this.key = key;
        } else {
            throw new java.lang.IllegalArgumentException("Input Key cannot be null");
        }
    }

    /**
     * Returns the date of the sighting
     *
     * @return The creation date of the sighting
     */
    public String getDate() {
        return date;
    }

    /**
     * Sets the date of sighting
     * @param date Date to be assigned to the sighting
     */
    public void setDate(String date) {
        if(date != null) {
            this.date = date;
        } else {
            throw new java.lang.IllegalArgumentException("Date cannot be null");
        }
    }

    /**
     * Returns the location type of the sighting
     * @return The location type of the sighting
     */
    public String getLoc_type() { return loc_type; }

    /**
     * Sets the location type of the sighting
     * @param loc_type to be assigned to the sighting
     */
    public void setLoc_type(String loc_type) {
        if(loc_type != null) {
            this.loc_type = loc_type;
        } else {
            throw new java.lang.IllegalArgumentException("Location type cannot be null");
        }
    }

    /**
     * Returns the zip code of the sighting
     * @return Zip code of the sighting
     */
    public String getZip() {
        return zip;
    }

    /**
     * Sets the zip code of the sighting
     * @param zip Zip code to be assigned to the sighting
     */
    public void setZip(String zip) {
        if(zip != null) {
            if(zip.length() != 5) {
                throw new java.lang.IllegalArgumentException("Zip must be a 5 number string");
            }
            for(int i = 0; i < zip.length(); i++) {
                if (!Character.isDigit(zip.charAt(i))) {
                    throw new java.lang.IllegalArgumentException("Zip must be a 5 number string");
                }
            }
            this.zip = zip;
        } else {
            throw new java.lang.IllegalArgumentException("Zip cannot be null");
        }
    }

    /**
     * Returns the address of the sighting
     * @return Address of the sighting
     */
    public String getAddress() {
        return address;
    }

    /**
     * Sets the address of the sighting
     * @param address to be assigned to the sighting
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * Returns the city of the sighting
     * @return City of the sighting
     */
    public String getCity() {
        return city;
    }

    /**
     * Sets the city of the sighting
     * @param city to be assigned to the sighting
     */
    public void setCity(String city) {
        if(city != null) {
            this.city = city;
        } else {
            throw new java.lang.IllegalArgumentException("City cannot be null");
        }
    }

    /**
     * Returns the borough of the sighting
     * @return Borough of the sighting
     */
    public String getBorough() {
        return borough;
    }

    /**
     * Sets the borough fo the sighting
     * @param borough Borough to be assigned to the sighting
     */
    public void setBorough(String borough) {
        this.borough = borough;
    }

    /**
     * Returns the latitude of the sighting
     * @return Latitude of the sighting
     */
    public String getLatitude() {
        return latitude;
    }

    /**
     * Sets the latitude of the sighting
     * @param latitude Latitude to be assigned to the sighting
     */
    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    /**
     * Returns longitude of the sighting
     * @return Longitude of the sighting
     */
    public String getLongitude() {
        return longitude;
    }

    /**
     * Sets the longitude of the sighting
     * @param longitude Longitude to be assigned to the sighting
     */
    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
}
