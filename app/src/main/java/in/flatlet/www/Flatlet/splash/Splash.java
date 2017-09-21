package in.flatlet.www.Flatlet.splash;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.facebook.accountkit.AccessToken;
import com.facebook.accountkit.AccountKit;

import in.flatlet.www.Flatlet.Home.FirstActivity;
import in.flatlet.www.Flatlet.R;
import in.flatlet.www.Flatlet.welcome.LoginActivity;
import in.flatlet.www.Flatlet.welcome.WelcomeActivity;


public class Splash extends AppCompatActivity {
    private ImageView imageView;
    private AccessToken accessToken;
    private String TAG = "Splash";
    SharedPreferences sharedPreferences1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int absoluteHeightInPx = displayMetrics.heightPixels;
        int absoluteWidthInPx = displayMetrics.widthPixels;
        double diagonalPixels = Math.sqrt((absoluteHeightInPx * absoluteHeightInPx) + (absoluteWidthInPx * absoluteWidthInPx));
        double diagonalInInches = diagonalPixels / displayMetrics.densityDpi;
        float deviceDensity = displayMetrics.density;
        float physicalPixelPerInchX = displayMetrics.xdpi;
        float physicalPixelPerInchY = displayMetrics.ydpi;
        Log.i(TAG, "onCreate: dimentions of phone abs ht in px"+absoluteHeightInPx+"abs width in px"+absoluteWidthInPx+
        "device density is "+displayMetrics.densityDpi+displayMetrics.density+displayMetrics.scaledDensity+"diag in inches"+diagonalInInches+diagonalPixels);
*/

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        setContentView(R.layout.splash_screen);
        imageView = (ImageView) findViewById(R.id.imageView);
        sharedPreferences1=getSharedPreferences("personalInfo", Context.MODE_PRIVATE);
        final AccessToken accessToken= AccountKit.getCurrentAccessToken();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String TAG = "SplashActivity";
        if(!(sharedPreferences.getBoolean("welcome",false)))
        {

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    Intent intent = new Intent(Splash.this, WelcomeActivity.class);
                    intent.setFlags(1);
                    startActivity(intent);
                    overridePendingTransition(R.anim.mainfadein,R.anim.splashfadeout);
                    finish();
                }
            }, 2000);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("welcome",true);
            editor.apply();
        }
        else {
            Log.i(TAG, "onCreate:  else wala" + sharedPreferences.getBoolean("welcome",false));
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent;
                    if (accessToken==null){
                        startActivity(new Intent(Splash.this,LoginActivity.class).setFlags(0));
                    }
                    else if (sharedPreferences1.getString("userName","johndoe").equals("johndoe")){
                        startActivity(new Intent(Splash.this,LoginActivity.class).setFlags(1));

                    }
                    else {
                        startActivity(new Intent(Splash.this, FirstActivity.class).setFlags(1));
                    }
                    imageView.setImageResource(R.mipmap.ic_launcher);
                    overridePendingTransition(R.anim.mainfadein,R.anim.splashfadeout);
                    finish();
                }
            },3000);
        }
    }

}