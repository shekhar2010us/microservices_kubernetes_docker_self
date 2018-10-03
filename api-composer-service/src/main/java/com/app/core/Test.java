package com.app.core;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class Test {

    static String https_url = "http://34.205.24.3:31003/user-service-1.0/rest/userDesign/getuser?userid=102";

    public static String getText(String url) throws Exception {
        URL website = new URL(url);
        URLConnection connection = website.openConnection();
        BufferedReader in = new BufferedReader(
                new InputStreamReader(
                        connection.getInputStream()));

        StringBuilder response = new StringBuilder();
        String inputLine;

        while ((inputLine = in.readLine()) != null)
            response.append(inputLine);

        in.close();

        return response.toString();
    }

    public static void main(String[] args) throws Exception {
        String content = getText(https_url);
        System.out.println(content);
    }

}
