package com.example.minhaj.jsonparser;

import android.content.Context;
import android.os.AsyncTask;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by minhaj on 22/08/2017.
 */

public class Async extends AsyncTask<String,Void,InputStream> {

    Context context;
    public Async(Context context){
        this.context = context;
    }
    @Override
    protected InputStream doInBackground(String... strings) {

        URL url = null;
        InputStream is = null;
        try {
            url = new URL(strings[0]);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        try {
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            is = httpURLConnection.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return is;
    }

    @Override
    protected void onPostExecute(InputStream inputStream) {
        super.onPostExecute(inputStream);
        ((MainActivity) context).getJsonReader(inputStream);
    }
}
