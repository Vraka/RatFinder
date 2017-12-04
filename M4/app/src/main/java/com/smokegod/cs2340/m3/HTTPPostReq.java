package com.smokegod.cs2340.m3;

import android.os.StrictMode;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Vraka on 12/4/2017.
 */

public class HTTPPostReq {

    private static String token;
    private static boolean isAdmin;

    public HTTPPostReq() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        token = null;
        isAdmin = false;
    }

    private static void setToken(String token_input) {
        HTTPPostReq.token = token_input;
    }

    public static String getToken() {
        return token;
    }

    private static void setAdmin(boolean admin_input) {
        isAdmin = admin_input;
    }

    public static boolean isAdmin() {
        return isAdmin;
    }

    public static String sendPost(String urlStr, String dataJSON) {
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
            Log.d("Database: SBSTRING", sb.toString());
            return sb.toString();
        } catch (Exception e) {
            Log.d("Database: Error", e.toString());
            return e.getMessage();
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }

    }

    public static int login(String login_name, String password) {
        String resp = HTTPPostReq.sendPost("https://desolate-taiga-94108.herokuapp.com/api/login", "{\"login_name\": \""+login_name+"\",\"password\": \""+password+"\"}");
        String msg = parseMessage(resp);
        if(msg.equalsIgnoreCase("login successful")) {
            setToken(parseToken(resp));

            //System.out.println(Jwt.);
            return 0;
        } else if(msg.equalsIgnoreCase("cannot find user")) {
            return 2;
        } else if(msg.equalsIgnoreCase("incorrect password")) {
            return 1;
        } else {
            return -1;
        }
    }

    public static int register(String login_name, String password, String contact_info, boolean isAdmin) {
        String resp = HTTPPostReq.sendPost("https://desolate-taiga-94108.herokuapp.com/api/register", "{\"login_name\": \""+login_name+"\",\"password\": \""+password+"\",\"contact_info\": \""+contact_info+"\",\"isAdmin\": "+isAdmin+"}");
        String msg = parseMessage(resp);
        if(msg.equalsIgnoreCase("database error")) {
            return 1;
        } else if(msg.equalsIgnoreCase("register successful")) {
            setToken(parseToken(resp));
            if(isAdmin) {
                setAdmin(true);
            }
            return 0;
        } else {
            return -1;
        }
    }

    private static String parseMessage(String jsonString) {
        String msg = "\"msg\":\"";
        String secondhalf = jsonString.substring(jsonString.indexOf(msg)+msg.length(),jsonString.length());
        String response = secondhalf.substring(0, secondhalf.indexOf("\""));
        return response;

    }

    private static String parseToken(String jsonString) {
        String msg = "\"token\":\"";
        String secondhalf = jsonString.substring(jsonString.indexOf(msg)+msg.length(),jsonString.length());
        String response = secondhalf.substring(0, secondhalf.indexOf("\""));
        return response;

    }

}
