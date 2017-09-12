package in.flatlet.www.Flatlet.splash;

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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        setContentView(R.layout.splash_screen);
        imageView = (ImageView) findViewById(R.id.imageView);
        final AccessToken accessToken= AccountKit.getCurrentAccessToken();
        Log.i("MainActivity", "onCreate: image loaded");

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String TAG = "SplashActivity";
        if(!(sharedPreferences.getBoolean("welcome",false)))
        {
            Log.i(TAG, "onCreate: if wala ");
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    imageView.setImageResource(R.drawable.splash);
               /* Intent intent = new Intent(Splash.this, FirstActivity.class);
                intent.setFlags(1);
                startActivity(intent);*/

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
                        intent = new Intent(Splash.this, LoginActivity.class);
                    }
                    else {
                        intent=new Intent(Splash.this,FirstActivity.class);
                        intent.setFlags(1);
                    }
                    imageView.setImageResource(R.drawable.splash);
                    startActivity(intent);
                    overridePendingTransition(R.anim.mainfadein,R.anim.splashfadeout);
                    finish();
                }
            },3000);
        }
    }

}