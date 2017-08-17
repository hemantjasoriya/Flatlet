package in.flatlet.www.Flatlet.application;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.squareup.leakcanary.LeakCanary;

public class MyApplication extends Application {
    private static String TAG = "Application";
    private SharedPreferences sharedPreferences;

    @Override
    public void onCreate() {
        super.onCreate();
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        if (!prefs.getBoolean("firstTime", false)) {
            // <---- run your one time code here
            sharedPreferences = this.getSharedPreferences("personalInfo", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("userName", "Application");
            editor.apply();



            Log.i(TAG, "onCreate: first time has runned");
            editor = prefs.edit();
            editor.putBoolean("firstTime", true);
            editor.commit();

            LeakCanary.install(this);
        }
    }
}
