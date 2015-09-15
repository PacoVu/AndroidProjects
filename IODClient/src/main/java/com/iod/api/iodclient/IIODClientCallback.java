package com.iod.api.iodclient;

/**
 * Created by Paco on 8/28/2015.
 */
public interface IIODClientCallback {
    public void requestCompletedWithContent(String response);
    public void requestCompletedWithJobID(String response);
    public void onErrorOccurred(String errorMessage);
}