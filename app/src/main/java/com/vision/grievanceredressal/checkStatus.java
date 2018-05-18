package com.vision.grievanceredressal;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class checkStatus extends Home {


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        ViewFlipper view = (ViewFlipper) findViewById(R.id.viewer);
        view.setDisplayedChild(3);

        Button statusButton = (Button) findViewById(R.id.statusButton);

        statusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getStatus();
            }
        });
    }

    private void getStatus() {
        final EditText uniqueKey = (EditText) findViewById(R.id.uniqueKey);
        final String key = uniqueKey.getText().toString();

        String url = "http://13.59.251.253:8000/problems/problems/check_status_api/";
        StringRequest getRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            ViewFlipper view = (ViewFlipper) findViewById(R.id.viewer);
                            view.setDisplayedChild(4);
                            JSONObject result = new JSONObject(response);
                            Integer id = result.getInt("id");
                            TextView cat = findViewById(R.id.catText);
                            TextView state = findViewById(R.id.stateText);
                            TextView district = findViewById(R.id.districtText);
                            TextView centre = findViewById(R.id.centreText);
                            TextView age = findViewById(R.id.ageText);
                            TextView title = findViewById(R.id.titleText);
                            TextView desc = findViewById(R.id.descText);
                            TextView status = findViewById(R.id.statusText);
                            String[] categ = {"Education","Electricity","Hospital","Food","Cleanliness","Clothing","Transport","Finance","Maintanence"};
                            cat.setText(categ[result.getInt("category")]);

                            state.setText(result.getString("state"));

                            district.setText(result.getString("district"));

                            centre.setText(result.getString("anganwadi_centres"));

                            age.setText(Integer.toString(result.getInt("age")));

                            title.setText(result.getString("title"));
                            desc.setText(result.getString("description"));
                            String[] sts = {"Submitted","Seen","Progress","Completed"};
                            status.setText(sts[result.getInt("status")]);
//                            URL imgurl = new URL("http://52.14.128.224:8000" + result.getString("image"));
//                            Bitmap image = BitmapFactory.decodeStream(imgurl.openConnection().getInputStream());
//                            img.setImageBitmap(image);
                            new GetImage().execute("http://13.59.251.253:8000" + result.getString("image")).get();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        Log.d("Response", response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        AlertDialog.Builder builder = new AlertDialog.Builder(checkStatus.this);
                        builder.setMessage(new String(error.networkResponse.data)).setTitle("Error");
                        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                            }
                        });
                        builder.setCancelable(false);
                        AlertDialog dialog = builder.create();
                        dialog.show();
                        Log.d("Error.Response", error.toString());
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("hash_key", key);

                return params;
            }
        };
        VolleySingleton.getInstance(getBaseContext()).addToRequestQueue(getRequest);

    }

    @Override
    public void onBackPressed() {
        Intent myin = new Intent(getApplicationContext(), Home.class);
        startActivity(myin);
        super.onBackPressed();
    }

    public String getData(String url, Integer i){
        final String[] value = {null};
        final Integer id = i;
        StringRequest postRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray result = new JSONArray(response);
                            for (int i = 0; i < result.length(); i++) {
                                JSONObject data = result.getJSONObject(i);
                                if (data.getInt("id") == id) {
                                    value[0] = data.getString("name");
                                }
                            }
                        }
                        catch (Exception e){
                            e.printStackTrace();
                        }
                        Log.d("Response", value[0]);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error

                        Log.d("Error.Response", error.toString());
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("format", "json");

                return params;
            }
        };
        VolleySingleton.getInstance(getBaseContext()).addToRequestQueue(postRequest);
        return value[0];
    }


    class GetImage extends AsyncTask<String, Void, Stream> {
        public static final String REQUEST_METHOD = "GET";
        public static final int READ_TIMEOUT = 15000;
        public static final int CONNECTION_TIMEOUT = 15000;

        @Override
        protected Stream doInBackground(String... params) {
            String stringUrl = params[0];
            String result;
            String inputLine;

            try {
                URL myUrl = new URL(stringUrl);
                HttpURLConnection connection = (HttpURLConnection)
                        myUrl.openConnection();
                connection.setRequestMethod(REQUEST_METHOD);
                connection.setReadTimeout(READ_TIMEOUT);
                connection.setConnectTimeout(CONNECTION_TIMEOUT);
                connection.connect();
                ImageView img = findViewById(R.id.image);
                Bitmap image = BitmapFactory.decodeStream(connection.getInputStream());
                img.setImageBitmap(image);
                return null;
            } catch (IOException e) {
                e.printStackTrace();
                result = null;
            }
            return null;
        }


    }
}
