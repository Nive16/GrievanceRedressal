package com.vision.grievanceredressal;

import android.os.AsyncTask;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;

class Requestt extends AsyncTask<String, Void, String> {

    @Override
    protected String doInBackground(String... strings) {
        try {
            HttpURLConnection con = (HttpURLConnection) (new URL("")).openConnection();
            con.setRequestMethod("POST");
            con.setDoInput(true);
            con.setDoOutput(true);
            con.connect();
            con.getOutputStream().write(("username=").getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
