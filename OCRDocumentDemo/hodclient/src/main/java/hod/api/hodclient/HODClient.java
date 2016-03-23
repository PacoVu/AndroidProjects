package hod.api.hodclient;

/**
 * Created by vuv on 10/10/2015.
 */
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.webkit.MimeTypeMap;

import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpResponseException;
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
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Map;


public class HODClient {
    public  HODApps hodApp;
    public enum REQ_MODE {SYNC, ASYNC };
    private String apiKey = "";
    private String hodBase = "https://api.havenondemand.com/1/api/";
    private String hodJobResult = "https://api.havenondemand.com/1/job/result/";
    private String hodJobStatus = "https://api.havenondemand.com/1/job/status/";
    private boolean getJobID = true;
    private String version = "v1";
    private boolean isBusy = false;

    IHODClientCallback mCallback;
    HttpClient httpClient = null;
    HttpGet httpGet = null;
    HttpPost httpPost = null;
    private enum HTTP_METHOD  { GET, POST };
    private HTTP_METHOD httpMethod = HTTP_METHOD.GET;

    public HODClient(String apiKey, String version, IHODClientCallback callback) {
        this.apiKey = apiKey;
        this.version = version;
        mCallback = callback;
        initializeHTTP();
    }
    public HODClient(String apiKey, IHODClientCallback callback) {
        this.apiKey = apiKey;
        this.version = "v1";
        mCallback = callback;
        initializeHTTP();;
    }
    private void initializeHTTP() {
        hodApp = new HODApps();
        if (httpClient == null) {
            HttpParams httpParameters = new BasicHttpParams();
            int timeoutConnection = 20000;
            HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
            HttpConnectionParams.setSoTimeout(httpParameters, 600000);
            httpClient = new DefaultHttpClient(httpParameters);
        }
        if (httpGet == null)
            httpGet = new HttpGet();
        if (httpPost == null)
            httpPost = new HttpPost();
    }
    public void GetJobResult(String jobID) {
        httpMethod = HTTP_METHOD.GET;
        String queryStr = String.format("%s%s", hodJobResult, jobID);
        getJobID = false;
        new MakeAsyncActivitiesTask().execute(null, queryStr, "");
    }
    public void GetJobStatus(String jobID) {
        httpMethod = HTTP_METHOD.GET;
        String queryStr = String.format("%s%s", hodJobStatus, jobID);
        getJobID = false;
        new MakeAsyncActivitiesTask().execute(null, queryStr, "");
    }
    public void GetRequest(Map<String,Object> params, String hodApp, REQ_MODE mode) {
        httpMethod = HTTP_METHOD.GET;
        String endPoint = hodBase;
        if (mode == REQ_MODE.SYNC) {
            endPoint += String.format("sync/%s/%s", hodApp, version);
            getJobID = false;
        }
        else {
            endPoint += String.format("async/%s/%s", hodApp, version);
            getJobID = true;
        }
        new MakeAsyncActivitiesTask().execute(params, endPoint, "");
    }
    public void PostRequest(Map<String,Object> params, String hodApp, REQ_MODE mode) {
        httpMethod = HTTP_METHOD.POST;
        String endPoint = hodBase;
        if (mode == REQ_MODE.SYNC) {
            endPoint += String.format("sync/%s/%s", hodApp, version);
            getJobID = false;
        }
        else {
            endPoint += String.format("async/%s/%s", hodApp, version);
            getJobID = true;
        }
        new MakeAsyncActivitiesTask().execute(params, endPoint, "");
    }
    private void ParseResponse(String response) {
        if (getJobID)
            mCallback.requestCompletedWithJobID(response);
        else
            mCallback.requestCompletedWithContent(response);
    }
    private void ParseError(String error) {
        mCallback.onErrorOccurred(error);
    }
    class MakeAsyncActivitiesTask extends AsyncTask<Object, String, String> {
        boolean isError = false;
        @Override
        protected String doInBackground(Object... params)
        {
            isError = false;
            String url = "";
            URI uri;
            if (httpMethod == HTTP_METHOD.GET) {
                url = String.format("%s?apikey=%s", params[1], apiKey);
                if (params[0] != null) {
                    Map<String, Object> map = (Map) params[0];
                    for (Map.Entry<String, Object> e : map.entrySet()) {
                        String key = e.getKey();
                        if (key.equals("file")) {
                            isError = true;
                            return "Failed. File upload must be used with PostRequest method.";
                        }
                        Object val = e.getValue();
                        String type =  val.getClass().getName();
                        if (type.equals("java.util.ArrayList")) {
                            for (String m : (ArrayList<String>) val) {
                                try {
                                    String value = URLEncoder.encode(m, "utf-8");
                                    url += String.format("&%s=%s", key, value);
                                } catch (UnsupportedEncodingException ex) {
                                    isError = true;
                                    return ex.getMessage();
                                }
                            }
                        } else {
                            try {
                                String value = URLEncoder.encode(val.toString(), "utf-8");
                                url += String.format("&%s=%s", key, value);
                            } catch (UnsupportedEncodingException ex) {
                                isError = true;
                                return ex.getMessage();
                            }
                        }
                    }
                }
                try {
                    uri = new URI(url);
                    httpGet.setURI(uri);
                } catch (URISyntaxException e1) {
                    isError = true;
                    return e1.getMessage();
                }
            }
            else if (httpMethod == HTTP_METHOD.POST) {
                try {
                    url = (String) params[1];
                    uri = new URI(url);
                    httpPost.setURI(uri);
                    MultipartEntityBuilder reqEntity = MultipartEntityBuilder.create();

                    reqEntity.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
                    Map<String,Object> map = (Map) params[0];
                    reqEntity.addPart("apikey", new StringBody(apiKey, ContentType.TEXT_PLAIN));
                    for (Map.Entry<String, Object> e : map.entrySet()) {
                        String key = e.getKey();
                        Object val = e.getValue();
                        String objType =  val.getClass().getName();
                        if (objType.equals("java.util.ArrayList")) {
                            if (key.equals("file")) {
                                for (String m : (ArrayList<String>) val) {
                                    String fileName = m.substring(m.lastIndexOf("/") + 1);
                                    File pFile = new File(m);
                                    if (pFile.exists()) {
                                        Uri pUri = Uri.fromFile(pFile);
                                        String mimeType = getMimeType(pUri.toString());
                                        ContentType type = ContentType.create(mimeType, Consts.ISO_8859_1);
                                        reqEntity.addBinaryBody("file", pFile, type, fileName);
                                    } else {
                                        isError = true;
                                        Log.e("HODClient", "Failed. File not found");
                                        return "Failed. File not found";
                                    }
                                }
                            } else {
                                for (String m : (ArrayList<String>) val) {
                                    reqEntity.addPart(key, new StringBody(m, ContentType.TEXT_PLAIN));
                                }
                            }
                        } else {
                            if (key.equals("file")) {
                                String fileFullName = val.toString();
                                String fileName = fileFullName.substring(fileFullName.lastIndexOf("/") + 1);
                                File pFile = new File(fileFullName);
                                if (pFile.exists()) {
                                    Uri pUri = Uri.fromFile(pFile);
                                    String mimeType = getMimeType(pUri.toString());
                                    ContentType type = ContentType.create(mimeType, Consts.ISO_8859_1);
                                    reqEntity.addBinaryBody("file", pFile, type, fileName);
                                } else {
                                    isError = true;
                                    Log.e("HODClient", "Failed. File not found");
                                    return "Failed. File not found";
                                }
                            } else {
                                reqEntity.addPart(key, new StringBody((String)val, ContentType.TEXT_PLAIN));
                            }
                        }
                    }
                    httpPost.setEntity(reqEntity.build());
                } catch (URISyntaxException e) {
                    isError = true;
                    return e.getMessage();
                }
            }

            try {
                HttpResponse response;
                if (httpMethod == HTTP_METHOD.GET)
                    response = httpClient.execute(httpGet);
                else
                    response = httpClient.execute(httpPost);
                StatusLine statusLine = response.getStatusLine();
                if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
                    HttpEntity entities = response.getEntity();
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    entities.writeTo(out);
                    out.close();
                    String responseStr = out.toString();
                    if (responseStr != null)
                        return responseStr;
                    else {
                        isError = true;
                        return "Unknown server error";
                    }
                } else {
                    isError = true;
                    String errorMsg = new HttpResponseException(statusLine.getStatusCode(),statusLine.getReasonPhrase()).getMessage();
                    return errorMsg;
                }
            } catch (IOException  e) {
                isError = true;
                return  e.getMessage();
            }

            //return null;
        }

        @Override
        protected void onProgressUpdate(String... unsued) {

        }

        @Override
        protected void onPostExecute(String sResponse) {
            if (isError) {
                ParseError(sResponse);
            } else {
                ParseResponse(sResponse);
            }
        }
    }
    private static String getMimeType(String url) {
        String extension = url.substring(url.lastIndexOf("."));
        String mimeTypeMap = MimeTypeMap.getFileExtensionFromUrl(extension);
        String mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(mimeTypeMap);
        return mimeType;
    }
}
