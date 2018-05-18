//package com.vision.grievanceredressal;
//
//import android.app.Activity;
//import android.os.AsyncTask;
//
//import com.loopj.android.http.RequestParams;
//import com.loopj.android.http.SyncHttpClient;
//import com.loopj.android.http.TextHttpResponseHandler;
//
//import java.io.BufferedReader;
//import java.io.File;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.net.HttpURLConnection;
//import java.net.URL;
//
//import org.apache.http.HttpResponse;
//import org.apache.http.client.HttpClient;
//import org.apache.http.client.methods.HttpPost;
//import org.apache.http.entity.mime.Header;
//import org.apache.http.entity.mime.HttpMultipartMode;
//import org.apache.http.entity.mime.MultipartEntity;
//import org.apache.http.entity.mime.content.FileBody;
//import org.apache.http.entity.mime.content.StringBody;
//import org.apache.http.impl.client.DefaultHttpClient;
//import org.apache.http.protocol.BasicHttpContext;
//import org.apache.http.protocol.HttpContext;
//
//public class HttpPostRequest extends AsyncTask<String, Void, String> {
//    public static final String REQUEST_METHOD = "POST";
//    public static final int READ_TIMEOUT = 15000;
//    public static final int CONNECTION_TIMEOUT = 15000;
//
//    @Override
//    protected String doInBackground(String... params) {
//        String stringUrl = params[0];
//        String result;
//        String inputLine = params[1];
//
////        try {
////            SyncHttpClient client = new SyncHttpClient();
////            RequestParams param = new RequestParams();
////            param.put("text", "some string");
////            param.put("image", new File(imagePath));
////
////            client.post("http://example.com", param, new TextHttpResponseHandler() {
////
////                public void onSuccess(int statusCode, Header[] headers, String responseString) {
////                    // success
////                    HttpPostRequest.this.toString();
////                }
////            });
////            } catch (Exception e) {
////                e.printStackTrace();
////            }
//
//    }
//
//    protected void onPostExecute(String result) {
////            Toast.makeText(parent,result,Toast.LENGTH_LONG).show();
//        super.onPostExecute(result);
//    }
//
//}
//
