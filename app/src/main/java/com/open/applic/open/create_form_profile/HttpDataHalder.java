package com.open.applic.open.create_form_profile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

/**
 * Created by lucas on 20/10/2017.
 */

public class HttpDataHalder {
    public HttpDataHalder() {
    }

    public String getHTTPhalder(String requestURL){
        URL url;
        String stringResponse="";
        try {

            url=new URL(requestURL);
            HttpURLConnection connection=(HttpURLConnection)url.openConnection();
            connection.setRequestMethod("GET");
            connection.setReadTimeout(15000);
            connection.setConnectTimeout(15000);
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type","application/x-www-form-urlencoded");

            int responseCode=connection.getResponseCode();

            if(responseCode == HttpURLConnection.HTTP_OK){

                String line;
                BufferedReader br= new BufferedReader(new InputStreamReader(connection.getInputStream()));
                while((line =br.readLine()) != null){
                    stringResponse+=line;
                }
            }else{
                stringResponse="";
            }

        }catch (Exception ex){}
        return stringResponse;

    }


}
