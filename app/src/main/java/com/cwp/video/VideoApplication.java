package com.cwp.video;

import android.app.Application;
import android.content.Context;

public class VideoApplication extends Application {

    private static Application app;

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
    }

    public static Application getApp()
    {
        return app;
    }

    public static Context getAppContext() {
        return getApp().getApplicationContext();
    }

}
