package com.smokegod.cs2340.m3;

/**
 * Created by Vraka on 11/19/2017.
 */


import android.util.Log;

import org.json.JSONObject;
import org.junit.Test;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.net.ssl.HttpsURLConnection;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class RatSightingTest {

    @Test (expected = IllegalArgumentException.class)
    public void ratsightingConstructor_Test() {
        RatSighting valid = new RatSighting("key", "date", "loc_type", "00000", "address", "city", "borough", "latitude", "longitude");
        assertEquals(valid.getAddress(), new RatSighting("key", "date", "loc_type", "00000", "address", "city", "borough", "latitude", "longitude").getAddress());
        assertEquals(valid.getBorough(), new RatSighting("key", "date", "loc_type", "00000", "address", "city", "borough", "latitude", "longitude").getBorough());
        assertEquals(valid.getCity(), new RatSighting("key", "date", "loc_type", "00000", "address", "city", "borough", "latitude", "longitude").getCity());
        assertEquals(valid.getDate(), new RatSighting("key", "date", "loc_type", "00000", "address", "city", "borough", "latitude", "longitude").getDate());
        assertEquals(valid.getKey(), new RatSighting("key", "date", "loc_type", "00000", "address", "city", "borough", "latitude", "longitude").getKey());
        assertEquals(valid.getLatitude(), new RatSighting("key", "date", "loc_type", "00000", "address", "city", "borough", "latitude", "longitude").getLatitude());
        assertEquals(valid.getLoc_type(), new RatSighting("key", "date", "loc_type", "00000", "address", "city", "borough", "latitude", "longitude").getLoc_type());
        assertEquals(valid.getLongitude(), new RatSighting("key", "date", "loc_type", "00000", "address", "city", "borough", "latitude", "longitude").getLongitude());
        assertEquals(valid.getZip(), new RatSighting("key", "date", "loc_type", "00000", "address", "city", "borough", "latitude", "longitude").getZip());

        RatSighting addressException = new RatSighting("key", "date", "loc_type", "00000", null, "city", "borough", "latitude", "longitude");
        RatSighting zipException = new RatSighting("key", "date", "loc_type", null, "address", "city", "borough", "latitude", "longitude");
    }

    @Test (expected = IllegalArgumentException.class)
    public void ratsightingSetKey_Test() {
        RatSighting keytest = new RatSighting("key", "date", "loc_type", "00000", "address", "city", "borough", "latitude", "longitude");
        assertEquals(keytest.getKey(), "key");
        keytest.setKey("key2");
        assertEquals(keytest.getKey(), "key2");
        keytest.setKey("anotherkey");
        assertEquals(keytest.getKey(), "anotherkey");
        keytest.setKey(null);
    }

    @Test
    public void ratsightingSetZip_Test() {
        RatSighting ziptest = new RatSighting("key", "date", "loc_type", "00000", "address", "city", "borough", "latitude", "longitude");
        assertEquals(ziptest.getZip(), "00000");
        ziptest.setZip("12345");
        assertEquals(ziptest.getZip(), "12345");
        try {
            ziptest.setZip("toolong");
            assertEquals(0,1);
        } catch (Exception e) {
            if(e instanceof IllegalArgumentException) {
                assertEquals(e.getMessage(), "Zip must be a 5 number string");
            }
        }
        try {
            ziptest.setZip(null);
            assertEquals(0,1);
        } catch (Exception e) {
            if(e instanceof IllegalArgumentException) {
                assertEquals(e.getMessage(), "Zip cannot be null");
            }
        }
        try {
            ziptest.setZip("fives");
            assertEquals(0,1);
        } catch (Exception e) {
            if(e instanceof IllegalArgumentException) {
                assertEquals(e.getMessage(), "Zip must be a 5 number string");
            }
        }
    }

    @Test (expected = IllegalArgumentException.class)
    public void ratsightingSetDate_Test() {
        RatSighting datetest = new RatSighting("key", "date", "loc_type", "00000", "address", "city", "borough", "latitude", "longitude");
        assertEquals(datetest.getDate(), "date");
        datetest.setDate("date2");
        assertEquals(datetest.getDate(), "date2");
        datetest.setDate("anotherdate");
        assertEquals(datetest.getDate(), "anotherdate");
        datetest.setDate(null);
    }

    @Test (expected = IllegalArgumentException.class)
    public void ratsightingSetCity_Test() {
        RatSighting citytest = new RatSighting("key", "date", "loc_type", "00000", "address", "city", "borough", "latitude", "longitude");
        assertEquals(citytest.getCity(), "city");
        citytest.setCity("city2");
        assertEquals(citytest.getCity(), "city2");
        citytest.setCity("anothercity");
        assertEquals(citytest.getCity(), "anothercity");
        citytest.setCity(null);
    }

    @Test (expected = IllegalArgumentException.class)
    public void ratsightingSetLocType_Test() {
        RatSighting loc_typetest = new RatSighting("key", "date", "loc_type", "00000", "address", "city", "borough", "latitude", "longitude");
        assertEquals(loc_typetest.getLoc_type(), "loc_type");
        loc_typetest.setLoc_type("loc_type2");
        assertEquals(loc_typetest.getLoc_type(), "loc_type2");
        loc_typetest.setLoc_type("anotherloc_type");
        assertEquals(loc_typetest.getLoc_type(), "anotherloc_type");
        loc_typetest.setLoc_type(null);
    }

    @Test
    public void HTTP_Test() {
        HTTPPostReq.login("androidusr1","password");
        ArrayList<String> list = HTTPPostReq.getSightings();
        for(int i = 0; i < list.size(); i++) {
            System.out.println(list.get(i));
        }

    }


}
