package com.example.benjaminxerri.dis_detector.utilities;

import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by benjaminxerri on 9/16/17.
 */

public class Network {
    final static String BASE_URL = "http://api.fixer.io/latest";
    final static String API_KEY = "5ba5d3a63dff25195392c59733959af7";
    final static String QUERY_PARAM = "base";
    static URL url = null;

    public static URL buildURL(String inputValue) {
        //build URI that takes in the base as a query param, so the user can enter the base currency
        Uri buildUri = Uri.parse("https://api.meaningcloud.com/sentiment-2.1").buildUpon()
                .appendQueryParameter("key",API_KEY)
                .appendQueryParameter("txt", inputValue)
                .appendQueryParameter("lang", "en")
                .build();
        try {
           url = new URL(buildUri.toString());
            return url;
        }catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }

}
