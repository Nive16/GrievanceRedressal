package com.vision.grievanceredressal;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.ViewFlipper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.loopj.android.http.RequestParams;


public class registerComplaint extends Home {

    private static final int READ_REQUEST_CODE = 42;
    private static final int CAMERA_REQUEST = 1888;
    private static final int MY_CAMERA_REQUEST_CODE = 100;
    private static final int MY_SOCKET_TIMEOUT_MS = 15000;
    ArrayList<String> statesName;
    ArrayList<String> districtsName;
    private JSONArray stateArray, districtArray;
    private Spinner districtSpinner;
    private ArrayAdapter<String> districtAdapter;

    public static String getPath(Context context, Uri uri) throws URISyntaxException {
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = {"_data"};
            Cursor cursor = null;

            try {
                cursor = context.getContentResolver().query(uri, projection, null, null, null);
                int column_index = cursor.getColumnIndexOrThrow("_data");
                if (cursor.moveToFirst()) {
                    return cursor.getString(column_index);
                }
            } catch (Exception e) {
                // Eat it
            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ViewFlipper view = (ViewFlipper) findViewById(R.id.viewer);
        view.setDisplayedChild(2);

        Spinner stateSpinner = (Spinner) findViewById(R.id.state);
        ArrayAdapter<String> stateAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1);
        stateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        stateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int stateID = 0;
                try {
                    JSONObject currentState = stateArray.getJSONObject(position);
                    stateID = currentState.getInt("id");
                    districtAdapter.clear();
                    for (int i = 0; i < districtArray.length(); i++) {
                        JSONObject district = districtArray.getJSONObject(i);
                        if (district.getInt("state") == stateID) {
                            districtAdapter.add(district.getString("name"));
                        }
                    }
                    districtSpinner.setAdapter(districtAdapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        districtSpinner = (Spinner) findViewById(R.id.district);
        districtAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1);
        districtAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        Spinner categorySpinner = (Spinner) findViewById(R.id.problemCategory);
        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.problemCategory));
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(categoryAdapter);

        Button uploadbutton = (Button) findViewById(R.id.uploadImage);

        uploadbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser();
            }
        });

        Button submitbutton = (Button) findViewById(R.id.buttonSubmit);
        submitbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    register();
            }
        });



        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_CAMERA_REQUEST_CODE);
        }

        String statesUrl = "http://13.59.251.253:8000/problems/states/?format=json";
        String districtsUrl = "http://13.59.251.253:8000/problems/districts/?format=json";
        String states, districts;

        HttpGetRequest getRequest = new HttpGetRequest();
        try {
            states = getRequest.execute(statesUrl).get();
            stateArray = new JSONArray(states);
            for (int i = 0; i < stateArray.length(); i++) {
                JSONObject state = stateArray.getJSONObject(i);

                if (state.getBoolean("status") == true) {
//                    statesName.add();
                    stateAdapter.add(state.getString("name"));
                }
            }
            stateSpinner.setAdapter(stateAdapter);
            getRequest.cancel(true);

            getRequest = new HttpGetRequest();

            districts = getRequest.execute(districtsUrl).get();
            districtArray = new JSONArray(districts);

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Internet Connection Error", Toast.LENGTH_LONG).show();
            Intent myin = new Intent(getApplicationContext(), Home.class);
            startActivity(myin);
        }

    }

    private void register() {
        final Spinner state = (Spinner) findViewById(R.id.state);
        final Spinner district = (Spinner) findViewById(R.id.district);
        final EditText location = (EditText) findViewById(R.id.location);
        final Spinner category = (Spinner) findViewById(R.id.problemCategory);
        final EditText age = (EditText) findViewById(R.id.age);
        final EditText title = (EditText) findViewById(R.id.problemTitlleText);
        final EditText description = (EditText) findViewById(R.id.problemdesctext);
        final ImageView probImage = (ImageView) findViewById(R.id.problemImage);

        String url="http://13.59.251.253:8000/problems/apiregistrer/?format=json";
//        url = "http://192.168.43.1:8080/sd.php";
        VolleyMultipartRequest registerComp = new VolleyMultipartRequest(Request.Method.POST, url, new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse response) {
                String resultResponse = new String(response.data);
                try {
                    JSONObject result = new JSONObject(resultResponse);
                    String status = result.getString("status");
                    String key = result.getString("unique_key");
                    if(status.equals("success"))
                    {
                        AlertDialog.Builder builder = new AlertDialog.Builder(registerComplaint.this);
                        builder.setMessage("Your Unique key is \n\"" + key+"\"").setTitle("Registered Successfully");
                        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent myin = new Intent(getApplicationContext(), Home.class);
                                startActivity(myin);
                            }
                        });
                        builder.setCancelable(false);
                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }
                    else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(registerComplaint.this);
                        builder.setMessage("Complaint not registered. Pleaase try again.").setTitle("Registeration Failed");
                        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent myin = new Intent(getApplicationContext(), Home.class);
                                startActivity(myin);
                            }
                        });
                        builder.setCancelable(false);
                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }
                    Log.d("Response",resultResponse);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String errorMessage = new String(error.networkResponse.data);
                AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
                builder.setMessage(errorMessage).setTitle("Registeration Failed");
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
                builder.setCancelable(false);
                AlertDialog dialog = builder.create();
                dialog.show();
                Log.i("Error", errorMessage);
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("category", Integer.toString(category.getSelectedItemPosition()));
                for (int i = 0; i < stateArray.length(); i++) {
                    JSONObject sta = null;
                    try {
                        sta = stateArray.getJSONObject(i);
                        if (sta.getString("name") == state.getSelectedItem().toString()) {
                            params.put("state", sta.getString("id"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                for (int i = 0; i < districtArray.length(); i++) {
                    JSONObject sta = null;
                    try {
                        sta = districtArray.getJSONObject(i);
                        if (sta.getString("name") == district.getSelectedItem().toString()) {
                            params.put("district", sta.getString("id"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                params.put("anganwadi_centres", location.getText().toString());
                params.put("age", "20");
                params.put("title", title.getText().toString());
                params.put("description", description.getText().toString());
                return params;
            }

                @Override
                protected Map<String, DataPart> getByteData() {
                    Map<String, DataPart> params = new HashMap<>();
                    // file name could found file base or direct access from real path
                    // for now just get bitmap data from ImageView
                    Bitmap image=((BitmapDrawable) probImage.getDrawable()).getBitmap();
                    ByteArrayOutputStream stream=new ByteArrayOutputStream();
                    image.compress(Bitmap.CompressFormat.JPEG, 90, stream);
                    params.put("image", new DataPart("image.jpg", stream.toByteArray(), "image/jpeg"));
                    return params;
                }
        };
        registerComp.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        VolleySingleton.getInstance(getBaseContext()).addToRequestQueue(registerComp);
    }







/*

@Override
protected void onActivityResult(int requestCode, int resultCode, Intent data) {
if (requestCode == CAMERA_REQUEST) {
Bitmap photo = (Bitmap) data.getExtras().get("data");
ImageView probImage = (ImageView) findViewById(R.id.problemImage);
probImage.setImageBitmap(photo);
probImage.setVisibility(View.VISIBLE);
}
}
*/

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            Intent myin = new Intent(getApplicationContext(), Home.class);
            startActivity(myin);
        }
    }

    private void showFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        try {
            startActivityForResult(
                    Intent.createChooser(intent, "Select a File to Upload"),
                    0);
        } catch (android.content.ActivityNotFoundException ex) {
            // Potentially direct the user to the Market with a Dialog
            Toast.makeText(this, "Please install a File Manager.",
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 0:
                if (resultCode == RESULT_OK) {
                    // Get the Uri of the selected file
                    Uri uri = data.getData();
                    /*
                    Log.d(TAG, "File Uri: " + uri.toString());
                    Get the path
                    */
                    String path = "";
                    try {
                        path = getPath(this, uri);
                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    }

                    /*
                    Log.d(TAG, "File Path: " + path);
                    Get the file instance
                    File file = new File(path);
                    Initiate the upload
                    */

//                    Toast.makeText(this, uri.toString(), Toast.LENGTH_SHORT).show();
                    ImageView probImage = (ImageView) findViewById(R.id.problemImage);
                    probImage.setImageURI(uri);
                    probImage.setVisibility(View.VISIBLE);
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


}
