package com.smokegod.cs2340.m3;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Vraka on 10/9/2017.
 */

public class CSVFile {
    InputStream inputStream;

    public CSVFile(InputStream inputStream){
        this.inputStream = inputStream;
    }

    public List read(){
        List resultList = new ArrayList<RatSighting>();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        try {
            String csvLine;
            while ((csvLine = reader.readLine()) != null) {
                String[] row = csvLine.split(",");
                resultList.add(new RatSighting(row[0], row[1], row[7], row[8], row[9], row[16], row[23], row[49], row[50]));
            }
        }
        catch (IOException ex) {
            throw new RuntimeException("Error in reading CSV file: "+ex);
        }
        finally {
            try {
                inputStream.close();
            }
            catch (IOException e) {
                throw new RuntimeException("Error while closing input stream: "+e);
            }
        }
        return resultList;
    }

    public ArrayList read(int length){
        ArrayList resultList = new ArrayList<RatSighting>();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        try {
            String csvLine;
            int count = 0;
            while ((csvLine = reader.readLine()) != null && count < length) {
                String[] row = csvLine.split(",");
                if(row.length > 40) {
                    resultList.add(new RatSighting(row[0], row[1], row[7], row[8], row[9], row[16], row[23], row[49], row[50]));
                }

                count++;
            }
        }
        catch (IOException ex) {
            throw new RuntimeException("Error in reading CSV file: "+ex);
        }
        finally {
            try {
                inputStream.close();
            }
            catch (IOException e) {
                throw new RuntimeException("Error while closing input stream: "+e);
            }
        }
        return resultList;
    }

    public ArrayList read(int start, int length){
        ArrayList resultList = new ArrayList<RatSighting>();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        start = start*20;
        try {
            String csvLine;
            int count = 0;
            while ((csvLine = reader.readLine()) != null && count < length + start) {
                if(count > start) {
                    String[] row = csvLine.split(",");
                    if(row.length > 40) {
                        resultList.add(new RatSighting(row[0], row[1], row[7], row[8], row[9], row[16], row[23], row[49], row[50]));
                    }
                }
                count++;
            }
        }
        catch (IOException ex) {
            throw new RuntimeException("Error in reading CSV file: "+ex);
        }
        finally {
            try {
                inputStream.close();
            }
            catch (IOException e) {
                throw new RuntimeException("Error while closing input stream: "+e);
            }
        }
        return resultList;
    }

    public ArrayList search(long startDate, long endDate){
        ArrayList resultList = new ArrayList<RatSighting>();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        try {
            String csvLine;

            //Skip first line
            reader.readLine();

            while ((csvLine = reader.readLine()) != null) {
                String[] row = csvLine.split(",");
                String createdString = row[1].substring(0, 10);
                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
                    Date date = sdf.parse(createdString);

                    long created = date.getTime();

                    if ((startDate <= created) && (endDate >= created) && (row.length > 40)) {
                        resultList.add(new RatSighting(row[0], row[1], row[7], row[8], row[9], row[16], row[23], row[49], row[50]));
                    }
                } catch (Exception e) {
                    Log.d("MapsActivity", "Error: " + e.toString());
                }
            }
        }
        catch (IOException ex) {
            throw new RuntimeException("Error in reading CSV file: "+ex);
        }
        finally {
            try {
                inputStream.close();
            }
            catch (IOException e) {
                throw new RuntimeException("Error while closing input stream: "+e);
            }
        }
        return resultList;
    }
}