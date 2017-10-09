package in.flatlet.www.Flatlet.Home.fragments.profilefragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.facebook.accountkit.Account;
import com.facebook.accountkit.AccountKit;
import com.facebook.accountkit.AccountKitCallback;
import com.facebook.accountkit.AccountKitError;
import com.facebook.accountkit.PhoneNumber;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import in.flatlet.www.Flatlet.Home.FirstActivity;
import in.flatlet.www.Flatlet.R;
import in.flatlet.www.Flatlet.Utility.MySingleton;

/**
 * Created by javax on 21-Aug-17.
 */

public class CreateProfileFragment extends Fragment {

    private EditText mobileEditText, nameEditText, emailEditText;
    private RadioButton maleRadioButton, femaleRadioButton;
    RequestQueue queue1;
    String userGender;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.createprofile_fragment, container, false);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Button saveProfileButton = (Button) getActivity().findViewById(R.id.saveProfileButton);
        mobileEditText = (EditText) getActivity().findViewById(R.id.mobileEditText);
        nameEditText = (EditText) getActivity().findViewById(R.id.nameEditText);
        emailEditText = (EditText) getActivity().findViewById(R.id.emailEditText);
        TextView personalDetailsTextView = (TextView) getActivity().findViewById(R.id.personalDetailsTextView);
        maleRadioButton = (RadioButton) getActivity().findViewById(R.id.maleRadioButton);
        femaleRadioButton = (RadioButton) getActivity().findViewById(R.id.femaleRadioButton);

        if (maleRadioButton.isChecked()) {
            userGender = "male";
        } else {
            userGender = "female";
        }

        //handling button click of save profile
        saveProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MySingleton.getInstance(getContext()).isOnline()) {
                    // filling the user data in shared preferences
                    SharedPreferences sharedPreferences = getActivity().getSharedPreferences("personalInfo", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    Drawable drawable = ContextCompat.getDrawable(getContext(), R.drawable.ic_error);


                    drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());

                    if (nameEditText.getText().toString().matches("")) {
                        nameEditText.setError("You can't leave this blank", drawable);
                        return;
                    }

                    if (!isEmailValid(emailEditText.getText().toString())) {
                        emailEditText.setError("Please enter a valid email id", drawable);
                        return;
                    }
                    if (!isNameValid(nameEditText.getText().toString())) {
                        nameEditText.setError("Please enter a valid name ", drawable);
                        return;
                    }
                    if (!maleRadioButton.isChecked() && !femaleRadioButton.isChecked()) {
                        Toast.makeText(getActivity(), "Please select your Identity", Toast.LENGTH_SHORT);
                        return;
                    }
                    editor.putString("userName", nameEditText.getText().toString());
                    editor.putString("userEmail", emailEditText.getText().toString());
                    editor.putString("userGender", userGender);
                    editor.apply();
                    Intent intent = new Intent(getActivity(), FirstActivity.class);
                    intent.setFlags(1);
                    getActivity().startActivity(intent);
                    sendToDatabase();
                } else {
                    Toast.makeText(getContext(), "No Internet Connection ! Please Try Again", Toast.LENGTH_SHORT).show();
                }

            }
        });


    }

    @Override
    public void onResume() {
        super.onResume();
        AccountKit.getCurrentAccount(new AccountKitCallback<Account>() {
            @Override
            public void onSuccess(final Account account) {
                // Get phone number
                PhoneNumber phoneNumber = account.getPhoneNumber();
                String phoneNumberString = phoneNumber.toString();
                mobileEditText.setText(phoneNumberString);
            }

            @Override
            public void onError(final AccountKitError error) {

            }
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (queue1 != null) {
            queue1.cancelAll("MyRequestTag");
        }
    }

    private static boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private static boolean isNameValid(String name) {
        String expression = "^[\\p{L} .'-]+$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(name);
        return matcher.matches();
    }

   /* private void logout() {
        AccountKit.logOut();
        Fragment fragment = new LoginFragment();
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.content, fragment, "fragmetHome");
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
        // delete sahred preferences
        new Thread(new Runnable() {
            @Override
            public void run() {
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("personalInfo", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.apply();
                // delete all rows from sqlite database
                FeedReaderDbHelper feedReaderDbHelper = new FeedReaderDbHelper(getContext());
                SQLiteDatabase db_favourite = feedReaderDbHelper.getWritableDatabase();
                db_favourite.execSQL("delete from " + FeedReaderContract.FeedEntry.TABLE_NAME);
            }
        }).start();

    }*/


    private void sendToDatabase() {

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("personalInfo", Context.MODE_PRIVATE);
        String dbqry = null;
        if (maleRadioButton.isChecked()) {
            dbqry = "INSERT INTO `our_users`(`user_ka_naam`,`user_mobile`,`user_emailid`,`sex`) VALUES ('"
                    + sharedPreferences.getString("userName", "johndoe") + "','" + sharedPreferences.
                    getString("userMobile", "911") + "','" + sharedPreferences.
                    getString("userEmail", "@johdoe") + "','male')";

        } else {
            dbqry = "INSERT INTO `our_users`(`user_ka_naam`,`user_mobile`,`user_emailid`,`sex`) VALUES ('"
                    + sharedPreferences.getString("userName", "johndoe") + "','" + sharedPreferences.
                    getString("userMobile", "911") + "','" + sharedPreferences.
                    getString("userEmail", "@johdoe") + "','female')";

        }
        String url = "http://flatlet.in/flatletuserinsert/flatletuserinsert.jsp?dbqry=" + dbqry;
        String urlFinal = url.replace(" ", "%20");

        StringRequest stringRequest = new StringRequest(Request.Method.GET, urlFinal,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("CreateProfileFragment", "onResponse: ");
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("CreateProfileFragment", "onErrorResponse: ");

            }
        });

        stringRequest.setTag("MyRequestTag");
        MySingleton.getInstance(getActivity().getApplicationContext()).addToRequestQueue(stringRequest);

    }
}
