package com.pacovu.ocrdocumentapidemo;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;

import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

/**
 * Created by Paco on 6/25/2015.
 */

interface OnServerRequestCompleteListener {
    void onServerRequestComplete(String response);
    void onErrorOccurred(String errorMessage);
}

public class CommsEngine {
    private final String apikey = "YOUR API KEY HERE";
    HttpClient httpClient = null;
    HttpGet httpGet = null;
    HttpPost httpPost = null;
    public enum HTTP_METHOD  { GET, POST };
    public HTTP_METHOD httpMethod = HTTP_METHOD.GET;

    OnServerRequestCompleteListener mListener;
    public CommsEngine() {
        if (httpClient == null) {
            HttpParams httpParameters = new BasicHttpParams();
            int timeoutConnection = 20000;
            HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
            httpClient = new DefaultHttpClient();
        }
        if (httpGet == null)
            httpGet = new HttpGet();
        if (httpPost == null)
            httpPost = new HttpPost();
    }
    public void ServiceGetRequest(String serviceType, String param, OnServerRequestCompleteListener listener) {
        mListener = listener;
        httpMethod = HTTP_METHOD.GET;
        new MakeAsyncActivitiesTask().execute(serviceType, param);
    }
    public void ServicePostRequest(String serviceType, String fileType, Map<String,String> param, OnServerRequestCompleteListener listener) {
        mListener = listener;
        httpMethod = HTTP_METHOD.POST;
        new MakeAsyncActivitiesTask().execute(serviceType, fileType, param);
    }
    private void ParseResponse(String response) {
        mListener.onServerRequestComplete(response);
    }
    class MakeAsyncActivitiesTask extends AsyncTask<Object, String, String> {
        @Override
        protected String doInBackground(Object... params)
        {
            String url = "";
            URI uri;
            if (httpMethod == HTTP_METHOD.GET) {
                url = params[0] + "apikey=" +  apikey;
                url += (String) params[1];
                try {
                    uri = new URI(url);
                    httpGet.setURI(uri);
                } catch (URISyntaxException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                    mListener.onErrorOccurred(e1.getMessage());
                }
                try {
                    HttpResponse response = httpClient.execute(httpGet);
                    StatusLine statusLine = response.getStatusLine();
                    if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
                        HttpEntity entity = response.getEntity();
                        ByteArrayOutputStream out = new ByteArrayOutputStream();
                        entity.writeTo(out);
                        out.close();
                        String responseStr = out.toString();
                        return responseStr;
                    } else {
                        mListener.onErrorOccurred("Bad response");
                    }
                } catch (IOException e) {
                    mListener.onErrorOccurred(e.getMessage());
                }
            }
            else if (httpMethod == HTTP_METHOD.POST) {
                try {
                    url = (String) params[0];
                    uri = new URI(url);
                    httpPost.setURI(uri);
                    MultipartEntityBuilder reqEntity = MultipartEntityBuilder.create();

                    reqEntity.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
                    Map<String,String> map = (Map) params[2];
                    reqEntity.addPart("apikey", new StringBody(apikey, ContentType.TEXT_PLAIN));
                    for (Map.Entry<String, String> e : map.entrySet()) {
                        String key = e.getKey();
                        String value = e.getValue();
                        if (key.equals("file")) {
                            ContentType type = ContentType.create((String) params[1], Consts.ISO_8859_1);
                            reqEntity.addBinaryBody("file", new File(value), type, "");
                        } else
                            reqEntity.addPart(key, new StringBody(value, ContentType.TEXT_PLAIN));
                    }
                    httpPost.setEntity(reqEntity.build());
                } catch (URISyntaxException e) {
                    mListener.onErrorOccurred(e.getMessage());
                }
                try {
                    HttpResponse response = httpClient.execute(httpPost);
                    StatusLine statusLine = response.getStatusLine();
                    if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
                        HttpEntity entities = response.getEntity();
                        ByteArrayOutputStream out = new ByteArrayOutputStream();
                        entities.writeTo(out);
                        out.close();
                        String responseStr = out.toString();
                        if (responseStr != null)
                            return responseStr;
                        else
                            mListener.onErrorOccurred("Unknown error");

                    } else {
                        mListener.onErrorOccurred("Bad response");
                    }
                } catch (IOException  e) {
                    mListener.onErrorOccurred(e.getMessage());
                }
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(String... unsued) {

        }

        @Override
        protected void onPostExecute(String sResponse) {
            ParseResponse(sResponse);
        }
    }
}