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
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Vraka on 12/4/2017.
 */

public class HTTPPostReq {

    private static String token;
    private static String username;
    private static boolean isAdmin;

    public HTTPPostReq() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        isAdmin = false;
    }

    protected static void setUsername(String username_input) {
         username = username_input;
    }

    public static String getUsername() {
        return username;
    }

    protected static void setToken(String token_input) {
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
        Log.d("DATABASE", resp);
        String msg = parseMessage(resp);
        if(msg.equalsIgnoreCase("rat added successfully")) {
            return true;
        } else {

            return false;
        }
    }

    public static ArrayList<RatSighting> sort(String sortBy, String data, int limit, int offset) {
        if(sortBy.equalsIgnoreCase("date")) {
            return searchDate(data.substring(0,data.indexOf(",")), data.substring(data.indexOf(",") + 1), limit, offset);
        } else if(sortBy.equalsIgnoreCase("borough")) {
            return searchBorough(data, limit, offset);
        } else if(sortBy.equalsIgnoreCase("zip")) {
            return searchZip(data, limit, offset);
        } else if(sortBy.equalsIgnoreCase("loc_type")) {
            return searchLocationType(data, limit, offset);
        } else {
            return new ArrayList<RatSighting>();
        }
    }

    private static ArrayList<RatSighting> searchDate(String start_date, String end_date, int limit, int offset) {
        ArrayList<RatSighting> list = new ArrayList<>();
        String resp = HTTPPostReq.sendPost("https://desolate-taiga-94108.herokuapp.com/api/search/dateRange", "{\"token\":\""+getToken()+"\", \"End_Date\":\""+end_date+"\", \"Start_Date\":\""+start_date+"\", \"limit\":"+limit+", \"offset\":"+offset+"}");
        try {
            JSONObject json = new JSONObject(resp);
            Log.d("DATABASE", json.toString());
            JSONArray jsonArray = json.getJSONObject("body").getJSONArray("data");
            Log.d("DATABASE", jsonArray.toString());
            if (jsonArray != null) {
                int len = jsonArray.length();
                for (int i=0;i<len;i++){
                    JSONObject ratSighting = jsonArray.getJSONObject(i);
                    list.add(new RatSighting(ratSighting.get("Unique_Key").toString(), ratSighting.get("Created_Date").toString(), ratSighting.getString("Location_Type"), ratSighting.get("Incident_Zip").toString(), ratSighting.getString("Incident_Address"), ratSighting.getString("City"), ratSighting.getString("Borough"), ratSighting.get("Latitude").toString(),ratSighting.get("Longitude").toString()));
                }
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return list;
    }

    private static ArrayList<RatSighting> searchBorough(String borough, int limit, int offset) {
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
                    list.add(new RatSighting(ratSighting.get("Unique_Key").toString(), ratSighting.get("Created_Date").toString(), ratSighting.getString("Location_Type"), ratSighting.get("Incident_Zip").toString(), ratSighting.getString("Incident_Address"), ratSighting.getString("City"), ratSighting.getString("Borough"), ratSighting.get("Latitude").toString(),ratSighting.get("Longitude").toString()));
                }
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return list;
    }

    private static ArrayList<RatSighting> searchZip(String zip, int limit, int offset) {
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
                    list.add(new RatSighting(ratSighting.get("Unique_Key").toString(), ratSighting.get("Created_Date").toString(), ratSighting.getString("Location_Type"), ratSighting.get("Incident_Zip").toString(), ratSighting.getString("Incident_Address"), ratSighting.getString("City"), ratSighting.getString("Borough"), ratSighting.get("Latitude").toString(),ratSighting.get("Longitude").toString()));
                }
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return list;
    }

    private static ArrayList<RatSighting> searchLocationType(String loc_type, int limit, int offset) {
        ArrayList<RatSighting> list = new ArrayList<>();
        String resp = HTTPPostReq.sendPost("https://desolate-taiga-94108.herokuapp.com/api/search/Location_Type", "{\"token\":\""+getToken()+"\",\"Location_Type\":\"" + loc_type + "\", \"limit\":"+limit+", \"offset\":"+offset+"}");
        try {
            JSONObject json = new JSONObject(resp);
            Log.d("DATABASE", json.toString());
            JSONArray jsonArray = json.getJSONObject("body").getJSONArray("data");
            Log.d("DATABASE", jsonArray.toString());
            if (jsonArray != null) {
                int len = jsonArray.length();
                for (int i=0;i<len;i++){
                    JSONObject ratSighting = jsonArray.getJSONObject(i);
                    list.add(new RatSighting(ratSighting.get("Unique_Key").toString(), ratSighting.get("Created_Date").toString(), ratSighting.getString("Location_Type"), ratSighting.get("Incident_Zip").toString(), ratSighting.getString("Incident_Address"), ratSighting.getString("City"), ratSighting.getString("Borough"), ratSighting.get("Latitude").toString(),ratSighting.get("Longitude").toString()));
                }
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return list;
    }

    public static ArrayList<User> getUsers() {
        ArrayList<User> list = new ArrayList<>();
        String resp = HTTPPostReq.sendPost("https://desolate-taiga-94108.herokuapp.com/api/users", "{\"token\": \""+getToken()+"\"}");
        try {
            JSONObject json = new JSONObject(resp);
            Log.d("DATABASE", json.toString());
            JSONArray jsonArray = json.getJSONObject("body").getJSONArray("data");
            Log.d("DATABASE", jsonArray.toString());
            if (jsonArray != null) {
                int len = jsonArray.length();
                for (int i=0;i<len;i++){
                    JSONObject user = jsonArray.getJSONObject(i);
                    list.add(new User(user.getString("_id"), user.getString("login_name"), user.getString("contact_info"), user.getBoolean("isAdmin"), user.getBoolean("isLocked")));
                }
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return list;
    }



    public static boolean lockUser(String id) {

        String resp = HTTPPostReq.sendPost("https://desolate-taiga-94108.herokuapp.com/api/lock", "{\"token\": \""+getToken()+"\",\"id\": \""+id+"\"}");
        String msg = parseMessage(resp);
        if(msg.equalsIgnoreCase("locked account")) {
            return true;
        } else {
            return false;
        }

    }

    public static boolean unlockUser(String id) {

        String resp = HTTPPostReq.sendPost("https://desolate-taiga-94108.herokuapp.com/api/unlock", "{\"token\": \""+getToken()+"\",\"id\": \""+id+"\"}");
        String msg = parseMessage(resp);
        if(msg.equalsIgnoreCase("unlocked account successfully")) {
            return true;
        } else {
            return false;
        }

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
            setUsername(login_name);
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
        String resp = HTTPPostReq.sendPost("https://desolate-taiga-94108.herokuapp.com/api/register", "{\"token\": \""+getToken()+"\", \"login_name\": \""+login_name+"\",\"password\": \""+password+"\",\"contact_info\": \""+contact_info+"\",\"isAdmin\": "+isAdmin+"}");
        String msg = parseMessage(resp);
        if(msg.equalsIgnoreCase("database error")) {
            return 1;
        } else if(msg.equalsIgnoreCase("register successful")) {
            setToken(parseToken(resp));
            setUsername(login_name);
            if(isAdmin) {
                setAdmin(true);
            }
            return 0;
        } else {
            return -1;
        }
    }

    public static int createAdmin(String login_name, String password, String contact_info, boolean isAdmin) {
        String resp = HTTPPostReq.sendPost("https://desolate-taiga-94108.herokuapp.com/api/createUser", "{\"token\": \""+getToken()+"\"\"login_name\": \""+login_name+"\",\"password\": \""+password+"\",\"contact_info\": \""+contact_info+"\",\"isAdmin\": "+isAdmin+"}");
        String msg = parseMessage(resp);
        if(msg.equalsIgnoreCase("database error")) {
            return 1;
        } else if(msg.equalsIgnoreCase("register successful")) {
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

    private static String parseStatus(String jsonString) {
        String msg = "\"status\":\"";
        String secondhalf = jsonString.substring(jsonString.indexOf(msg)+msg.length(),jsonString.length());
        String response = secondhalf.substring(0, secondhalf.indexOf(","));
        return response;
    }

    public static boolean changeLoginName(String login_name, String password) {
        String resp = HTTPPostReq.sendPost("https://desolate-taiga-94108.herokuapp.com/api/account/changeLoginName", "{\"token\": \""+getToken()+"\"\"login_name\": \""+login_name+"\",\"password\": \""+password+"\"}");
        String status = parseStatus(resp);
        if(status.equalsIgnoreCase("200")) {
            setToken(parseToken(resp));
            setUsername(login_name);
            return true;
        } else {
            return false;
        }
    }
    public static boolean changeContactInfo(String contact_info, String password) {
        String resp = HTTPPostReq.sendPost("https://desolate-taiga-94108.herokuapp.com/api/account/changeContactInfo", "{\"token\": \""+getToken()+"\"\"contact_info\": \""+contact_info+"\",\"password\": \""+password+"\"}");
        String status = parseStatus(resp);
        if(status.equalsIgnoreCase("200")) {
            setToken(parseToken(resp));
            return true;
        } else {
            return false;
        }
    }
    public static boolean changePassword(String newpassword, String oldpassword) {
        String resp = HTTPPostReq.sendPost("https://desolate-taiga-94108.herokuapp.com/api/account/changePassword", "{\"token\": \""+getToken()+"\"\"newpassword\": \""+newpassword+"\",\"password\": \""+oldpassword+"\"}");
        String status = parseStatus(resp);
        if(status.equalsIgnoreCase("200")) {
            return true;
        } else {
            return false;
        }
    }

    public static HashMap<String, Integer> countBorough() {
        String resp = HTTPPostReq.sendPost("https://desolate-taiga-94108.herokuapp.com/api/stats/countBorough", "{\"token\": \""+getToken()+"\"}");
        HashMap<String, Integer> stats = new HashMap<>();
        try {
            JSONObject json = new JSONObject(resp);
            stats.put("MANHATTAN", json.getInt("MANHATTAN"));
            stats.put("STATEN_ISLAND", json.getInt("STATEN_ISLAND"));
            stats.put("BRONX", json.getInt("BRONX"));
            stats.put("QUEENS", json.getInt("QUEENS"));
            stats.put("BROOKLYN", json.getInt("BROOKLYN"));
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return stats;
    }

    public static HashMap<String, Integer> countLocationType() {
        String resp = HTTPPostReq.sendPost("https://desolate-taiga-94108.herokuapp.com/api/stats/countLocationType", "{\"token\": \""+getToken()+"\"}");
        HashMap<String, Integer> stats = new HashMap<>();
        try {
            JSONObject json = new JSONObject(resp);
            stats.put("1-2 Family Dwelling", json.getInt("a"));
            stats.put("3+ Family Apt. Building", json.getInt("b"));
            stats.put("3+ Family Mixed Use Building", json.getInt("c"));
            stats.put("Commercial Building", json.getInt("d"));
            stats.put("Vacant Lot", json.getInt("e"));
            stats.put("Construction Site", json.getInt("f"));
            stats.put("Hospital", json.getInt("g"));
            stats.put("Catch Basin/Sewer", json.getInt("h"));
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return stats;
    }

}
