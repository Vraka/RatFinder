package com.smokegod.cs2340.m3.db;

import android.os.StrictMode;
import android.util.Log;

import java.io.DataOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * Created by yoyor on 12/3/2017.
 */

public class Database {
    public static void register(final String email, final String password) {
//        https://desolate-taiga-94108.herokuapp.com/api/
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Map<String, String> params = new LinkedHashMap<>();
                    params.put("contact_info", email);
                    params.put("login_info", email);
                    params.put("password", password);
                    String urlParameters  = getQuery(params);
                    byte[] postData       = urlParameters.getBytes(StandardCharsets.UTF_8);
                    int    postDataLength = postData.length;
                    String request        = "https://desolate-taiga-94108.herokuapp.com/api/register";
                    URL url            = new URL( request );
                    HttpURLConnection conn= (HttpURLConnection) url.openConnection();
                    conn.setDoOutput( true );
                    conn.setInstanceFollowRedirects( false );
                    conn.setRequestMethod( "POST" );
                    conn.setRequestProperty( "Content-Type", "application/x-www-form-urlencoded");
                    conn.setRequestProperty( "charset", "utf-8");
                    conn.setRequestProperty( "Content-Length", Integer.toString( postDataLength ));
                    conn.setUseCaches( false );
                    try( DataOutputStream wr = new DataOutputStream( conn.getOutputStream())) {
                        wr.write( postData );
                    }

                    Scanner scanner = new Scanner(conn.getInputStream());
                    String responseBody = scanner.useDelimiter("\\A").next();
                    Log.d("UDRDB", responseBody);

//                    URL url = new URL("https://desolate-taiga-94108.herokuapp.com/api/register");
//                    Map<String, Object> params = new LinkedHashMap<>();
//                    params.put("contact_info", email);
//                    params.put("login_info", email);
//                    params.put("password", password);
//                    StringBuilder postData = new StringBuilder();
//                    for (Map.Entry<String, Object> param : params.entrySet()) {
//                        if (postData.length() != 0) postData.append('&');
//                        postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
//                        postData.append('=');
//                        postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
//                    }
//                    byte[] postDataBytes = postData.toString().getBytes("UTF-8");
//
//                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//                    conn.setRequestMethod("POST");
//                    conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
//                    conn.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
//                    conn.setDoOutput(true);
//                    conn.getOutputStream().write(postDataBytes);
//
//                    Reader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
//
//                    for (int c; (c = in.read()) >= 0; )
//                        System.out.print((char) c);


//                    URL url = new URL("https://desolate-taiga-94108.herokuapp.com/api/register");
//                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
//                    con.setRequestMethod("POST");
//                    con.setRequestProperty( "Content-Type", "application/x-www-form-urlencoded");
//                    con.setRequestProperty( "charset", "utf-8");
//                    con.setDoOutput(true);
//                    con.setDoInput(true);
//                    Map<String, String> parameters = new HashMap<String, String>();
//                    parameters.put("contact_info", email);
//                    parameters.put("login_info", email);
//                    parameters.put("password", password);
//
//                    con.getOutputStream().write(getQuery(parameters).getBytes());
////                    con.connect();
//
////                    int code = con.getResponseCode();
////            DataOutputStream out = new DataOutputStream(con.getOutputStream());
//                    Scanner scanner = new Scanner(con.getInputStream());
//                    String responseBody = scanner.useDelimiter("\\A").next();
//                    Log.d("UDRDB", responseBody);
////            out.writeBytes(ParameterStringBuilder.getParamsString(parameters));
////            out.flush();
////            out.close();
                } catch (Exception e) {
                    Log.d("UDRDB Error", e.toString());
                }
            }
        }).run();


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

    private static String getQuery(Map<String, String> params) throws UnsupportedEncodingException
    {
        StringBuilder result = new StringBuilder();
        boolean first = true;


        for (String key : params.keySet())
        {
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(key, "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(params.get(key), "UTF-8"));
        }

        return result.toString();
    }
}
