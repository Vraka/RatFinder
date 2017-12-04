package com.smokegod.cs2340.m3;

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

}
