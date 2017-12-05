package com.smokegod.cs2340.m3;

import android.os.StrictMode;
import android.util.Log;

import com.auth0.android.jwt.JWT;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;

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
            //Log.d("Database: SBSTRING", sb.toString());
            return sb.toString();
        } catch (Exception e) {
            //Log.d("Database: Error", e.toString());
            return e.getMessage();
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }

    }

    public static boolean addSighting(RatSighting sighting) {
        String resp = HTTPPostReq.sendPost("https://desolate-taiga-94108.herokuapp.com/api/addRat", "{\"token\": \""+getToken()+"\",\"Location_Type\":\""+sighting.getLoc_type()+"\", \"Incident_Zip\":\""+sighting.getZip()+"\",\"Incident_Address\":\"" + sighting.getAddress() + "\",\"City\":\"" + sighting.getCity() + "\",\"Borough\":\"" + sighting.getBorough() + "\",\"Latitude\":\"" + sighting.getLatitude() + "\",\"Longitude\":\"" + sighting.getLongitude() + "\"}");
        String msg = parseMessage(resp);
        if(msg.equalsIgnoreCase("rat added successfully")) {
            return true;
        } else {
            return false;
        }
    }

    public static ArrayList<RatSighting> searchDate(String start_date, String end_date, int limit, int offset) {
        ArrayList<RatSighting> list = new ArrayList<>();
        String resp = HTTPPostReq.sendPost("https://desolate-taiga-94108.herokuapp.com/api/search/dateRange", "{\"token\":\""+getToken()+"\",\"Start_Date\":\""+start_date+"\",\"End_Date\":\"" + end_date + "\", \"limit\":"+limit+", \"offset\":"+offset+"}");
        try {
            JSONObject json = new JSONObject(resp);
            Log.d("DATABASE", json.toString());
            JSONArray jsonArray = json.getJSONObject("body").getJSONArray("data");
            Log.d("DATABASE", jsonArray.toString());
            if (jsonArray != null) {
                int len = jsonArray.length();
                for (int i=0;i<len;i++){
                    JSONObject ratSighting = jsonArray.getJSONObject(i);
                    list.add(new RatSighting(ratSighting.getString("Unique_Key"), ratSighting.getString("Created_Date"), ratSighting.getString("Location_Type"), ratSighting.getString("Incident_Zip"), ratSighting.getString("Incident_Address"), ratSighting.getString("City"), ratSighting.getString("Borough"), ratSighting.getString("Latitude"),ratSighting.getString("Longitude")));
                }
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return list;
    }

    public static ArrayList<RatSighting> searchBorough(String borough, int limit, int offset) {
        ArrayList<RatSighting> list = new ArrayList<>();
        String resp = HTTPPostReq.sendPost("https://desolate-taiga-94108.herokuapp.com/api/search/Borough", "{\"token\":\""+getToken()+"\",\"Borough\":\"" + borough + "\", \"limit\":"+limit+", \"offset\":"+offset+"}");
        try {
            JSONObject json = new JSONObject(resp);
            Log.d("DATABASE", json.toString());
            JSONArray jsonArray = json.getJSONObject("body").getJSONArray("data");
            Log.d("DATABASE", jsonArray.toString());
            if (jsonArray != null) {
                int len = jsonArray.length();
                for (int i=0;i<len;i++){
                    JSONObject ratSighting = jsonArray.getJSONObject(i);
                    list.add(new RatSighting(ratSighting.getString("Unique_Key"), ratSighting.getString("Created_Date"), ratSighting.getString("Location_Type"), ratSighting.getString("Incident_Zip"), ratSighting.getString("Incident_Address"), ratSighting.getString("City"), ratSighting.getString("Borough"), ratSighting.getString("Latitude"),ratSighting.getString("Longitude")));
                }
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return list;
    }

    public static ArrayList<RatSighting> searchZip(String zip, int limit, int offset) {
        ArrayList<RatSighting> list = new ArrayList<>();
        String resp = HTTPPostReq.sendPost("https://desolate-taiga-94108.herokuapp.com/api/search/Incident_Zip", "{\"token\":\""+getToken()+"\",\"Incident_Zip\":\"" + zip + "\", \"limit\":"+limit+", \"offset\":"+offset+"}");
        try {
            JSONObject json = new JSONObject(resp);
            Log.d("DATABASE", json.toString());
            JSONArray jsonArray = json.getJSONObject("body").getJSONArray("data");
            Log.d("DATABASE", jsonArray.toString());
            if (jsonArray != null) {
                int len = jsonArray.length();
                for (int i=0;i<len;i++){
                    JSONObject ratSighting = jsonArray.getJSONObject(i);
                    list.add(new RatSighting(ratSighting.getString("Unique_Key"), ratSighting.getString("Created_Date"), ratSighting.getString("Location_Type"), ratSighting.getString("Incident_Zip"), ratSighting.getString("Incident_Address"), ratSighting.getString("City"), ratSighting.getString("Borough"), ratSighting.getString("Latitude"),ratSighting.getString("Longitude")));
                }
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return list;
    }

    public static ArrayList<RatSighting> searchLocationType(String loc_type, int limit, int offset) {
        ArrayList<RatSighting> list = new ArrayList<>();
        String resp = HTTPPostReq.sendPost("https://desolate-taiga-94108.herokuapp.com/api/search/Borough", "{\"token\":\""+getToken()+"\",\"Location_Type\":\"" + loc_type + "\", \"limit\":"+limit+", \"offset\":"+offset+"}");
        try {
            JSONObject json = new JSONObject(resp);
            Log.d("DATABASE", json.toString());
            JSONArray jsonArray = json.getJSONObject("body").getJSONArray("data");
            Log.d("DATABASE", jsonArray.toString());
            if (jsonArray != null) {
                int len = jsonArray.length();
                for (int i=0;i<len;i++){
                    JSONObject ratSighting = jsonArray.getJSONObject(i);
                    list.add(new RatSighting(ratSighting.getString("Unique_Key"), ratSighting.getString("Created_Date"), ratSighting.getString("Location_Type"), ratSighting.getString("Incident_Zip"), ratSighting.getString("Incident_Address"), ratSighting.getString("City"), ratSighting.getString("Borough"), ratSighting.getString("Latitude"),ratSighting.getString("Longitude")));
                }
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return list;
    }

    public static ArrayList<RatSighting> getSightings(int limit, int offset) {
        ArrayList<RatSighting> list = new ArrayList<>();
        String resp = HTTPPostReq.sendPost("https://desolate-taiga-94108.herokuapp.com/api/rats", "{\"token\": \""+getToken()+"\",\"limit\":"+limit+", \"offset\":"+offset+"}");
        try {
            JSONObject json = new JSONObject(resp);
            Log.d("DATABASE", json.toString());
            JSONArray jsonArray = json.getJSONObject("body").getJSONArray("data");
            Log.d("DATABASE", jsonArray.toString());
            if (jsonArray != null) {
                int len = jsonArray.length();
                for (int i=0;i<len;i++){
                    JSONObject ratSighting = jsonArray.getJSONObject(i);
                    list.add(new RatSighting(ratSighting.getString("Unique_Key"), ratSighting.getString("Created_Date"), ratSighting.getString("Location_Type"), ratSighting.getString("Incident_Zip"), ratSighting.getString("Incident_Address"), ratSighting.getString("City"), ratSighting.getString("Borough"), ratSighting.getString("Latitude"),ratSighting.getString("Longitude")));
                }
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return list;
    }

    public static int login(String login_name, String password) {
        String resp = HTTPPostReq.sendPost("https://desolate-taiga-94108.herokuapp.com/api/login", "{\"login_name\": \""+login_name+"\",\"password\": \""+password+"\"}");
        String msg = parseMessage(resp);
        if(msg.equalsIgnoreCase("login successful")) {
            setToken(parseToken(resp));
            JWT jwt = new JWT(getToken());
            setAdmin(jwt.getClaim("isAdmin").asBoolean());
            System.out.println(isAdmin);
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
