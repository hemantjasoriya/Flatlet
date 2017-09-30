package in.flatlet.www.Flatlet.splash;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.facebook.accountkit.AccessToken;
import com.facebook.accountkit.AccountKit;

import org.json.JSONException;
import org.json.JSONObject;

import in.flatlet.www.Flatlet.BuildConfig;
import in.flatlet.www.Flatlet.Home.FirstActivity;
import in.flatlet.www.Flatlet.R;
import in.flatlet.www.Flatlet.Utility.MySingleton;
import in.flatlet.www.Flatlet.welcome.LoginActivity;
import in.flatlet.www.Flatlet.welcome.WelcomeActivity;


public class Splash extends AppCompatActivity {
    private ImageView imageView;

    SharedPreferences sharedPreferences1;


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.splash_screen);
        imageView = (ImageView) findViewById(R.id.imageView);
        sharedPreferences1 = getSharedPreferences("personalInfo", Context.MODE_PRIVATE);
        final AccessToken accessToken = AccountKit.getCurrentAccessToken();

        if (MySingleton.getInstance(getApplicationContext()).isOnline()) {

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest("http://flatlet.in/webservices/versionChecker.jsp", null, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
                    try {

                        int mandatoryUpdate = response.getInt("priority");
                        String versionAvailable = response.getString("version_name");

                        if (mandatoryUpdate == 1 && versionAvailable.equalsIgnoreCase(BuildConfig.VERSION_NAME)) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(Splash.this);
                            builder.setTitle("Update Alert")
                                    .setMessage("There is some mandatory update available")
                                    .setPositiveButton("UPDATE", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=in.flatlet.www.Flatlet"));
                                            startActivity(intent);
                                            finish();
                                        }
                                    }).create();
                            builder.setCancelable(false);
                            builder.show();

                        } else {

                            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                            if (!(sharedPreferences.getBoolean("welcome", false))) {
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {


                                        Intent intent = new Intent(Splash.this, WelcomeActivity.class);
                                        intent.setFlags(1);
                                        startActivity(intent);
                                        overridePendingTransition(R.anim.mainfadein, R.anim.splashfadeout);
                                        finish();
                                    }
                                }, 1500);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putBoolean("welcome", true);
                                editor.apply();
                            } else {

                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (accessToken == null) {
                                            startActivity(new Intent(Splash.this, LoginActivity.class).setFlags(0));
                                        } else if (sharedPreferences1.getString("userName", "johndoe").equals("johndoe")) {
                                            startActivity(new Intent(Splash.this, LoginActivity.class).setFlags(1));

                                        } else {
                                            startActivity(new Intent(Splash.this, FirstActivity.class).setFlags(1));

                                        }
                                        imageView.setImageResource(R.drawable.splash3);
                                        overridePendingTransition(R.anim.mainfadein, R.anim.splashfadeout);
                                        finish();
                                    }
                                }, 1500);
                            }

                        }

                    } catch (JSONException ignored) {

                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    if (!(sharedPreferences.getBoolean("welcome", false))) {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {


                                Intent intent = new Intent(Splash.this, WelcomeActivity.class);
                                intent.setFlags(1);
                                startActivity(intent);
                                overridePendingTransition(R.anim.mainfadein, R.anim.splashfadeout);
                                finish();
                            }
                        }, 1500);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putBoolean("welcome", true);
                        editor.apply();
                    } else {

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                if (accessToken == null) {
                                    startActivity(new Intent(Splash.this, LoginActivity.class).setFlags(0));
                                } else if (sharedPreferences1.getString("userName", "johndoe").equals("johndoe")) {
                                    startActivity(new Intent(Splash.this, LoginActivity.class).setFlags(1));

                                } else {
                                    startActivity(new Intent(Splash.this, FirstActivity.class).setFlags(1));
                                }
                                imageView.setImageResource(R.drawable.splash3);
                                overridePendingTransition(R.anim.mainfadein, R.anim.splashfadeout);
                                finish();
                            }
                        }, 1500);
                    }

                }
            });
            jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(12000, 0,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            MySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest);


        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("No Internet Connection")
                    .setMessage("Please make sure you have Internet Turned On")
                    .setIcon(R.drawable.ic_no_internet)
                    .setNegativeButton("Go to Settings", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startActivity(new Intent(Settings.ACTION_SETTINGS));
                            finish();
                        }
                    })
                    .setPositiveButton("Exit", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();


                        }
                    }).create();
            builder.setCancelable(false);
            builder.show();


        }

    }


}