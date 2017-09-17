package in.flatlet.www.Flatlet.application;

import android.app.Application;

import com.squareup.leakcanary.LeakCanary;

public class MyApplication extends Application {
    private static String TAG = "Application";

    @Override
    public void onCreate() {
        super.onCreate();
        LeakCanary.install(this);
    }
}

