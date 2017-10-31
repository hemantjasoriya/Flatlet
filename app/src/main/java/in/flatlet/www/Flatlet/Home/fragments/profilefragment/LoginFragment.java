package in.flatlet.www.Flatlet.Home.fragments.profilefragment;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import in.flatlet.www.Flatlet.Home.FirstActivity;
import in.flatlet.www.Flatlet.R;
import in.flatlet.www.Flatlet.Utility.MySingleton;
import in.flatlet.www.Flatlet.recyclerView.FeedReaderContract;
import in.flatlet.www.Flatlet.recyclerView.FeedReaderDbHelper;

/**
 * Created by javax on 20-Aug-17.
 */

public class LoginFragment extends Fragment {

    public static int APP_REQUEST_CODE = 99;
    private Button loginButton;
    private FeedReaderDbHelper feedReaderDbHelper;
    private SQLiteDatabase db_favourite;
    private String phoneNumberString;
    private ProgressBar progressBar;
    RelativeLayout RL;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.login_fragment, container, false);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        RL = (RelativeLayout) view.findViewById(R.id.RL);


        return view;


    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);



        loginButton = (Button) getActivity().findViewById(R.id.loginButton);


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MySingleton.getInstance(getContext()).isOnline()) {

                    final Intent intent = new Intent(getActivity(), AccountKitActivity.class);
                    AccountKitConfiguration.AccountKitConfigurationBuilder configurationBuilder =
                            new AccountKitConfiguration.AccountKitConfigurationBuilder(
                                    LoginType.PHONE,
                                    AccountKitActivity.ResponseType.TOKEN);
                    UIManager uiManager;
                    uiManager = new SkinManager(SkinManager.Skin.TRANSLUCENT, ContextCompat.getColor(getContext(), R.color.secondaryLightColor), R.drawable.splash, SkinManager.Tint.BLACK, 0);
                    configurationBuilder.setUIManager(uiManager);// or .ResponseType.TOKEN
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


        //disabling any touch gesture during this period

        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);


        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == APP_REQUEST_CODE) { // confirm that this response matches your request
            AccountKitLoginResult loginResult = data.getParcelableExtra(AccountKitLoginResult.RESULT_KEY);
            String toastMessage = null;
            if (loginResult.getError() != null) {
                toastMessage = loginResult.getError().getErrorType().getMessage();

            } else if (loginResult.wasCancelled()) {
                toastMessage = "Login Cancelled";
            } else {
                if (loginResult.getAccessToken() != null) {
                    //fetching phone no
                    AccountKit.getCurrentAccount(new AccountKitCallback<Account>() {
                        @Override
                        public void onSuccess(final Account account) {
                            // Get phone number
                            progressBar.setVisibility(View.VISIBLE);
                            RL.setAlpha(0.4f);
                            progressBar.setAlpha(1);
                            PhoneNumber phoneNumber = account.getPhoneNumber();
                            phoneNumberString = (phoneNumber.toString()).replace("+91", "");
                            SharedPreferences sharedPreferences = getActivity().getSharedPreferences("personalInfo", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("userMobile", phoneNumberString);
                            editor.apply();
                            String url = "http://flatlet.in/flatletusercheck/flatletusercheck.jsp?phoneNumberString=" + phoneNumberString;

                            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                                    new Response.Listener<JSONObject>() {
                                        @Override
                                        public void onResponse(JSONObject response) {
                                            // Display the first 500 characters of the response string.
                                            try {
                                                if (response.getInt("tag") == 0) {
                                                    Fragment fragment = new CreateProfileFragment();
                                                    android.support.v4.app.FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                                                    fragmentTransaction.replace(R.id.login_relative, fragment, "fragmetHome");
                                                    fragmentTransaction.addToBackStack(null);
                                                    fragmentTransaction.commit();

                                                } else {

                                                    fetchDataFromDatabase();

                                                }
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }


                                            // fetching favourite hostels of user from database
                                            fetchFavouriteHostels();

                                        }
                                    }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(getContext(), "Server error", Toast.LENGTH_SHORT).show();

                                }
                            });
                            RequestQueue queue1 = Volley.newRequestQueue(getActivity());
                            queue1.add(jsonObjectRequest);

                        }

                        @Override
                        public void onError(final AccountKitError error) {
                            // Handle Error
                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                            builder.setTitle("Some error occured")
                                    .setMessage("Please try again!")
                                    .setIcon(R.drawable.ic_no_internet)
                                    .setPositiveButton("OK", null).create();
                            builder.setCancelable(false);
                            builder.show();

                        }
                    });


                } else {
                    toastMessage = String.format(
                            "Success:%s...",
                            loginResult.getAuthorizationCode().substring(0, 10));
                }
            }

            Toast.makeText(
                    getActivity(),
                    toastMessage,
                    Toast.LENGTH_LONG)
                    .show();
        }
    }

    public void fetchDataFromDatabase() {
        String url1 = "http://flatlet.in/flatletuserdatafetcher/flatletuserdatafetcher.jsp?phoneNumberString=" + phoneNumberString;
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url1, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        new MyTask().execute(response);


                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
        RequestQueue queue2 = Volley.newRequestQueue(getContext());
        queue2.add(jsObjRequest);
    }

    public void fetchFavouriteHostels() {

        feedReaderDbHelper = new FeedReaderDbHelper(getContext());
        db_favourite = feedReaderDbHelper.getWritableDatabase();

        SharedPreferences pref_default = PreferenceManager.getDefaultSharedPreferences(getContext());
        if (!pref_default.getBoolean("thirdTime", false)) {
            feedReaderDbHelper.onCreateOriginal(db_favourite);
            SharedPreferences.Editor editor = pref_default.edit();
            editor.putBoolean("thirdTime", true);
            editor.apply();
        }
        String url2 = "http://flatlet.in/flatletfavouritefetcher/flatletfavouritefetcher.jsp?phoneNumberString=" + phoneNumberString;
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url2,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        new SaveDBTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, response);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();

                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(jsonArrayRequest);
    }

    private class MyTask extends AsyncTask<JSONObject, Void, Void> {

        @Override
        protected Void doInBackground(JSONObject... response) {
            SharedPreferences sharedPreferences = getActivity().getSharedPreferences("personalInfo", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            try {
                editor.putString("userName", response[0].getString("user_ka_naam"));
                editor.putString("userEmail", response[0].getString("user_emailid"));
                editor.putString("hostel1_name", response[0].getString("hostel1_name"));
                editor.putFloat("hostel1_rating", ((float) response[0].getDouble("hostel1_rating")));
                editor.putInt("hostel1_food", response[0].getInt("hostel1_food"));
                editor.putInt("hostel1_accommodation", response[0].getInt("hostel1_accommodation"));
                editor.putInt("hostel1_staffbehaviour", response[0].getInt("hostel1_staffbehaviour"));
                editor.putInt("hostel1_studyenvironment", response[0].getInt("hostel1_studyenvironment"));
                editor.putString("hostel2_name", response[0].getString("hostel2_name"));
                editor.putFloat("hostel2_rating", (float) (response[0].getDouble("hostel2_rating")));
                editor.putInt("hostel2_food", response[0].getInt("hostel2_food"));
                editor.putInt("hostel2_accommodation", response[0].getInt("hostel2_accommodation"));
                editor.putInt("hostel2_staffbehaviour", response[0].getInt("hostel2_staffbehaviour"));
                editor.putInt("hostel2_studyenvironment", response[0].getInt("hostel2_studyenvironment"));
            } catch (JSONException e) {
                e.printStackTrace();

            }
            editor.apply();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            progressBar.setVisibility(View.INVISIBLE);
            Intent intent = new Intent(getActivity(), FirstActivity.class);
            intent.setFlags(1);
            getActivity().startActivity(intent);

        }
    }

    private class SaveDBTask extends AsyncTask<JSONArray, Void, Void> {

        @Override
        protected Void doInBackground(JSONArray... response) {
            for (int i = 0; i < response[0].length(); i++) {
                try {
                    JSONObject object = response[0].getJSONObject(i);
                    ContentValues values = new ContentValues();
                    values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE, object.getString("title"));
                    values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_SECONDARY_ADDRESS, object.getString("secondary_address"));
                    values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_RENT, object.getInt("rent"));
                    values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_RATING, object.getDouble("rating"));
                    values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_IMG_URL, object.getString("img_url"));
                    db_favourite.insert(FeedReaderContract.FeedEntry.TABLE_NAME, null, values);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }
    }


}
