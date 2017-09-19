package in.flatlet.www.Flatlet.Home.fragments.profilefragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.facebook.accountkit.Account;
import com.facebook.accountkit.AccountKit;
import com.facebook.accountkit.AccountKitCallback;
import com.facebook.accountkit.AccountKitError;
import com.facebook.accountkit.AccountKitLoginResult;
import com.facebook.accountkit.PhoneNumber;
import com.facebook.accountkit.ui.AccountKitActivity;
import com.facebook.accountkit.ui.AccountKitConfiguration;
import com.facebook.accountkit.ui.LoginType;
import com.facebook.accountkit.ui.SkinManager;
import com.facebook.accountkit.ui.UIManager;

import org.json.JSONException;
import org.json.JSONObject;

import in.flatlet.www.Flatlet.Home.FirstActivity;
import in.flatlet.www.Flatlet.R;
import in.flatlet.www.Flatlet.Utility.MySingleton;

/**
 * Created by javax on 20-Aug-17.
 */

public class LoginFragment extends Fragment {

    private final String TAG = "loginfragment";
    private static final int APP_REQUEST_CODE = 99;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        return inflater.inflate(R.layout.login_fragment, container, false);


    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG, "onResume: ");

    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.i(TAG, "onActivityCreated: ");

        Button loginButton = (Button) getActivity().findViewById(R.id.loginButton);


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MySingleton.getInstance(getContext()).isOnline()) {
                    Log.i(TAG, "button clicked ");
                    final Intent intent = new Intent(getActivity(), AccountKitActivity.class);
                    AccountKitConfiguration.AccountKitConfigurationBuilder configurationBuilder =
                            new AccountKitConfiguration.AccountKitConfigurationBuilder(
                                    LoginType.PHONE,
                                    AccountKitActivity.ResponseType.TOKEN);
                    UIManager uiManager;
                    uiManager = new SkinManager(SkinManager.Skin.TRANSLUCENT, ContextCompat.getColor(getContext(), R.color.secondaryLightColor), R.drawable.splash, SkinManager.Tint.BLACK, 0);
                    configurationBuilder.setUIManager(uiManager);

                    configurationBuilder.setUIManager(uiManager);
                    // or .ResponseType.TOKEN
                    // ... perform additional configuration ...
                    configurationBuilder.setDefaultCountryCode("IN");

                    intent.putExtra(
                            AccountKitActivity.ACCOUNT_KIT_ACTIVITY_CONFIGURATION,
                            configurationBuilder.build());
                    startActivityForResult(intent, APP_REQUEST_CODE);
                } else {
                    Toast.makeText(getContext(), "No Internet Connection ! Please Try Again", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onActivityResult(
            final int requestCode,
            final int resultCode,
            final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == APP_REQUEST_CODE) { // confirm that this response matches your request
            AccountKitLoginResult loginResult = data.getParcelableExtra(AccountKitLoginResult.RESULT_KEY);
            String toastMessage = null;
            if (loginResult.getError() != null) {
                toastMessage = loginResult.getError().getErrorType().getMessage();
                    /*showErrorActivity(loginResult.getError());*/
            } else if (loginResult.wasCancelled()) {
                toastMessage = "Login Cancelled";
            } else {
                if (loginResult.getAccessToken() != null) {
                    Log.i(TAG, "onActivityResult: access token");
                    //fetching phone no
                    AccountKit.getCurrentAccount(new AccountKitCallback<Account>() {
                        @Override
                        public void onSuccess(final Account account) {
                            // Get phone number
                            PhoneNumber phoneNumber = account.getPhoneNumber();
                            final String phoneNumberString = (phoneNumber.toString()).replace("+91", "");
                            SharedPreferences sharedPreferences = getActivity().getSharedPreferences("personalInfo", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("userMobile", phoneNumberString);
                            editor.apply();
                            Log.i(TAG, "onSuccess: " + phoneNumberString);
                            String url = "http://flatlet.in/flatletusercheck/flatletusercheck.jsp?phoneNumberString=" + phoneNumberString;
                            Log.i(TAG, "onSuccess: " + url);

                            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                                    new Response.Listener<JSONObject>() {
                                        @Override
                                        public void onResponse(JSONObject response) {
                                            // Display the first 500 characters of the response string.
                                            Log.i(TAG, "onResponse: " + response);
                                            try {
                                                if (response.getInt("tag") == 0) {
                                                    Log.i(TAG, "onResponse: if chala");
                                                    Fragment fragment = new CreateProfileFragment();
                                                    android.support.v4.app.FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                                                    fragmentTransaction.replace(R.id.login_relative, fragment, "fragmetHome");
                                                    fragmentTransaction.addToBackStack(null);
                                                    fragmentTransaction.commit();

                                                } else {
                                                    Log.i(TAG, "onResponse: else started");
                                                    // go to main activity

                                                    Intent intent = new Intent(getActivity(), FirstActivity.class);
                                                    intent.setFlags(1);
                                                    getActivity().startActivity(intent);

                                                    // fetching data from database
                                                    String url = "http://flatlet.in/flatletuserdatafetcher/flatletuserdatafetcher.jsp?phoneNumberString=" + phoneNumberString;
                                                    JsonObjectRequest jsObjRequest = new JsonObjectRequest
                                                            (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                                                                @Override
                                                                public void onResponse(JSONObject response) {
                                                                    SharedPreferences sharedPreferences = getActivity().getSharedPreferences("personalInfo", Context.MODE_PRIVATE);
                                                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                                                    try {
                                                                        editor.putString("userName", response.getString("user_ka_naam"));
                                                                        editor.putString("userEmail", response.getString("user_emailid"));
                                                                        editor.putString("hostel1_name", response.getString("hostel1_name"));
                                                                        editor.putFloat("hostel1_rating", ((float) response.getDouble("hostel1_rating")));
                                                                        editor.putInt("hostel1_food", response.getInt("hostel1_food"));
                                                                        editor.putInt("hostel1_accommodation", response.getInt("hostel1_accommodation"));
                                                                        editor.putInt("hostel1_staffbehaviour", response.getInt("hostel1_staffbehaviour"));
                                                                        editor.putInt("hostel1_studyenvironment", response.getInt("hostel1_studyenvironment"));
                                                                        editor.putFloat("hostel2_rating", (float) (response.getDouble("hostel2_rating")));
                                                                        editor.putInt("hostel2_food", response.getInt("hostel2_food"));
                                                                        editor.putInt("hostel2_accommodation", response.getInt("hostel2_accommodation"));
                                                                        editor.putInt("hostel2_staffbehaviour", response.getInt("hostel2_staffbehaviour"));
                                                                        editor.putInt("hostel2_studyenvironment", response.getInt("hostel2_studyenvironment"));
                                                                    } catch (JSONException e) {
                                                                        e.printStackTrace();
                                                                        Log.i(TAG, "onResponse: JSONException " + e);
                                                                    }
                                                                    editor.apply();


                                                                }
                                                            }, new Response.ErrorListener() {

                                                                @Override
                                                                public void onErrorResponse(VolleyError error) {
                                                                    // TODO Auto-generated method stub
                                                                    Log.i(TAG, "onErrorResponse: " + error);


                                                                }
                                                            });

                                                    jsObjRequest.setTag("MyTag");
                                                    MySingleton.getInstance(getActivity().getApplicationContext()).addToRequestQueue(jsObjRequest);

                                                }
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }


                                        }
                                    }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Log.i(TAG, "onErrorResponse: main " + error);

                                }
                            });

                            jsonObjectRequest.setTag("MyTag");
                            MySingleton.getInstance(getActivity().getApplicationContext()).addToRequestQueue(jsonObjectRequest);


                        }

                        @Override
                        public void onError(final AccountKitError error) {
                            Log.i(TAG, "onError: ");
                            // Handle Error
                        }
                    });


                } else {
                    toastMessage = String.format(
                            "Success:%s...",
                            loginResult.getAuthorizationCode().substring(0, 10));
                }

                // If you have an authorization code, retrieve it from
                // loginResult.getAuthorizationCode()
                // and pass it to your server and exchange it for an access token.

                // Success! Start your next activity...
                   /* goToMyLoggedInActivity();*/
            }

            // Surface the result to your user in an appropriate way.
            Toast.makeText(
                    getActivity(),
                    toastMessage,
                    Toast.LENGTH_LONG)
                    .show();
        }
    }


}



