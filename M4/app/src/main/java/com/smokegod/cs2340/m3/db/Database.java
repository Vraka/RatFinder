package com.smokegod.cs2340.m3.db;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * Created by yoyor on 12/3/2017.
 */

public class Database {
    public static void register(String email, String password) throws IOException{
        https://desolate-taiga-94108.herokuapp.com/api/

        try {
            URL url = new URL("https://desolate-taiga-94108.herokuapp.com/api/");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "JSON");
            Map<String, String> parameters = new HashMap<String, String>();
            parameters.put("contact_info", email);
            parameters.put("password", password);
            con.setDoOutput(true);
            int code = con.getResponseCode();
//            DataOutputStream out = new DataOutputStream(con.getOutputStream());
            try {
                Scanner scanner = new Scanner(con.getInputStream());
                String responseBody = scanner.useDelimiter("\\A").next();
                System.out.println(responseBody);
            } catch (Exception e) {

            }
//            out.writeBytes(ParameterStringBuilder.getParamsString(parameters));
//            out.flush();
//            out.close();
//
        } catch (Exception e) {

        }

//        HttpClient httpclient = HttpClients.createDefault();
//        HttpPost httppost = new HttpPost("http://www.a-domain.com/foo/");
//
//// Request parameters and other properties.
//        List<NameValuePair> params = new ArrayList<NameValuePair>(2);
//        params.add(new BasicNameValuePair("param-1", "12345"));
//        params.add(new BasicNameValuePair("param-2", "Hello!"));
//        httppost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
//
////Execute and get the response.
//        HttpResponse response = httpclient.execute(httppost);
//        HttpEntity entity = response.getEntity();
//
//        if (entity != null) {
//            InputStream instream = entity.getContent();
//            try {
//                // do something useful
//            } finally {
//                instream.close();
//            }
//        }
    }
}
