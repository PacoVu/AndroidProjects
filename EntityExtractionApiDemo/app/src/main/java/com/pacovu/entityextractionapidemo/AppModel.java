package com.pacovu.entityextractionapidemo;

import android.app.Application;

/**
 * Created by Paco on 7/1/2015.
 */
public class AppModel extends Application {
    CommsEngine mCommsEngine;
    public String mLink = "http://www.bbc.com";
    @Override
    public void onCreate() {
        super.onCreate();
        mCommsEngine = new CommsEngine();
    }
}
