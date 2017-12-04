package com.smokegod.cs2340.m3;

/**
 * Created by Vraka on 11/19/2017.
 */


import android.util.Log;

import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
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

import static org.apache.http.HttpHeaders.USER_AGENT;
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
        //assertEquals("test",sendPost());
        assertEquals("test", sendPost("https://desolate-taiga-94108.herokuapp.com/api/register", "{\"login_name\": \"androidusr2\",\"password\": \"password\",\"contact_info\": \"test@test.com\"}"));
    }

    private static String sendPost(String urlStr, String dataJSON) {
        HttpURLConnection conn = null;
        try {

            URL url = new URL(urlStr);
            conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestMethod("POST");

            OutputStream os = conn.getOutputStream();
            os.write(dataJSON.getBytes("UTF-8"));
            os.close();

            int responseCode = conn.getResponseCode();
            System.out.println("\nSending 'POST' request to URL : " + url);
            System.out.println("Response Code : " + responseCode);
            //System.out.println(conn.getResponseMessage());
            /*Scanner errors = new Scanner(conn.getErrorStream());
            String errorStr = "";
            while(errors.hasNext()) {
                errorStr += errors.nextLine();
            }
            System.out.println("Error message: " + errorStr);*/

            // read the response
            InputStream in = new BufferedInputStream(conn.getInputStream());
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line+"\n");
            }
            br.close();
            System.out.println("Message: " + sb.toString());
            return sb.toString();
        } catch (Exception e) {
            return e.getMessage();
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }

    }


    /*private String sendPost(){
        HttpsURLConnection con = null;
        try {
            String url = "https://desolate-taiga-94108.herokuapp.com/api/register";
            URL obj = new URL(url);
            con = (HttpsURLConnection) obj.openConnection();

            //add reuqest header
            con.setRequestMethod("POST");
            con.setRequestProperty("User-Agent", USER_AGENT);
            con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

            String urlParameters = "login_name=username&password=password&contact_infor=test@test.com&isAdmin=true";

            // Send post request
            con.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.writeBytes(urlParameters);
            wr.flush();
            wr.close();

            int responseCode = con.getResponseCode();
            System.out.println("\nSending 'POST' request to URL : " + url);
            System.out.println("Post parameters : " + urlParameters);
            System.out.println("Response Code : " + responseCode);

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null) {
                sb.append(line+"\n");
            }
            in.close();
            System.out.println("Message: " + sb.toString());
            return sb.toString();

            //print result
        } catch (Exception e) {
            return e.getMessage();
        } finally {
            if (con != null) {
                con.disconnect();
            }
        }

    }

    private String attemptRegister() {
        HttpURLConnection client = null;
        String output = "";

        try {
            URL url = new URL("https://desolate-taiga-94108.herokuapp.com/api/register");
            client = (HttpURLConnection) url.openConnection();
            client.setRequestMethod("POST");
            //HashMap<String, String> params = new HashMap<String, String>();
            //params.put("login_name", "username");
            //params.put("password", "password");
            //params.put("contact_info", "test@test.com");
            //params.put("isAdmin", "true");
            client.setRequestProperty("login_name","username");
            client.setRequestProperty("password","password");
            client.setRequestProperty("contact_info","test@test.com");
            client.setRequestProperty("isAdmin","true");
            client.setDoOutput(true);
            BufferedOutputStream outputPost = new BufferedOutputStream(client.getOutputStream());
            Scanner scanner = new Scanner(client.getInputStream());
            while(scanner.hasNext()) {
                output += scanner.nextLine();
            }

            outputPost.flush();
            outputPost.close();
        } catch (Exception e) {

        } finally {
            if(client != null) // Make sure the connection is not null.
                client.disconnect();
        }

        return output;
        //mAuth.createUserWithEmailAndPassword(usernameET.getEditableText().toString(),
        //        passwordET.getEditableText().toString()).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
        //   @Override
        //    public void onComplete(@NonNull Task<AuthResult> task) {
        //        if (!task.isSuccessful()) {
        //            Toast t = Toast.makeText(RegisterActivity.this, "There seems to be an error " +
        //                            "with your registration information! Maybe try a longer password!",
        //                    Toast.LENGTH_LONG);
        //            t.show();
        //        }
        //    }
        //});
    }*/
}
