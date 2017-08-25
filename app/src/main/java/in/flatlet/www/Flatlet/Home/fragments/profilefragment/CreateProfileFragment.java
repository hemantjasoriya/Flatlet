package in.flatlet.www.Flatlet.Home.fragments.profilefragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
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
import com.android.volley.toolbox.Volley;
import com.facebook.accountkit.Account;
import com.facebook.accountkit.AccountKit;
import com.facebook.accountkit.AccountKitCallback;
import com.facebook.accountkit.AccountKitError;
import com.facebook.accountkit.PhoneNumber;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import in.flatlet.www.Flatlet.R;

/**
 * Created by javax on 21-Aug-17.
 */

public class CreateProfileFragment extends Fragment {
    private Button logoutButton, saveProfileButton;
    private EditText mobileEditText, nameEditText, emailEditText;
    private TextView personalDetailsTextView;
    private final String TAG = "CreateProfileFragment";
    private RadioButton maleRadioButton, femaleRadioButton;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.createprofile_fragment, container, false);

        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        logoutButton = (Button) getActivity().findViewById(R.id.logoutButton);
        saveProfileButton = (Button) getActivity().findViewById(R.id.saveProfileButton);
        mobileEditText = (EditText) getActivity().findViewById(R.id.mobileEditText);
        nameEditText = (EditText) getActivity().findViewById(R.id.nameEditText);
        emailEditText = (EditText) getActivity().findViewById(R.id.emailEditText);
        personalDetailsTextView = (TextView) getActivity().findViewById(R.id.personalDetailsTextView);
        maleRadioButton = (RadioButton) getActivity().findViewById(R.id.maleRadioButton);
        femaleRadioButton = (RadioButton) getActivity().findViewById(R.id.femaleRadioButton);

        //handling button click of save profile
        saveProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // filling the user data in shared preferences
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("personalInfo", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                Drawable drawable = getResources().getDrawable(R.drawable.ic_error);
                drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());

                if (nameEditText.getText().toString().matches("")) {
                    nameEditText.setError("Please enter name", drawable);
                    return;
                }

                if (!isEmailValid(emailEditText.getText().toString())) {
                    emailEditText.setError("Please enter a valid email id", drawable);
                    return;
                }
                if (!maleRadioButton.isChecked() && !femaleRadioButton.isChecked()) {
                    Toast.makeText(getActivity(), "Please select your Identity", Toast.LENGTH_SHORT);
                    return;
                }
                editor.putString("userName", nameEditText.getText().toString());
                editor.putString("userEmail", emailEditText.getText().toString());
                editor.apply();
                // launching SavedprofileFragment
                Fragment fragment = new SavedProfileFragment();
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.content, fragment, "fragmetHome");
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                sendToDatabase();
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
                //putting  mobile no in shared preferences
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("personalInfo", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("userMobile", (phoneNumberString).replace("+91", ""));
                editor.apply();
                Log.i(TAG, "onSuccess: " + phoneNumberString);

            }

            @Override
            public void onError(final AccountKitError error) {
                Log.i(TAG, "onError: ");
                // Handle Error
            }
        });


        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });
    }

    public static boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public void logout() {
        AccountKit.logOut();
        //launching login fragment
        Fragment fragment = new LoginFragment();
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.content, fragment, "fragmetHome");
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
        // changing user name in shared preferences
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("personalInfo", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("userName", "Application");
        editor.apply();
    }


    public void sendToDatabase() {
        Log.i(TAG, "sendToDatabase: started");
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("personalInfo", Context.MODE_PRIVATE);
        String dbqry = null;
        if (maleRadioButton.isChecked()) {
            dbqry = "INSERT INTO `our_users`(`user_ka_naam`,`user_mobile`,`user_emailid`,`sex`) VALUES ('"
                    + sharedPreferences.getString("userName", "johndoe") + "','" + sharedPreferences.
                    getString("userMobile", "911") + "','" + sharedPreferences.
                    getString("userEmail", "@johdoe") + "','male')";
            Log.i(TAG, "sendToDatabase: " + dbqry);
        } else {
            dbqry = "INSERT INTO `our_users`(`user_ka_naam`,`user_mobile`,`user_emailid`,`sex`) VALUES ('"
                    + sharedPreferences.getString("userName", "johndoe") + "','" + sharedPreferences.
                    getString("userMobile", "911") + "','" + sharedPreferences.
                    getString("userEmail", "@johdoe") + "','female')";
            Log.i(TAG, "sendToDatabase: " + dbqry);
        }
        String url = "http://flatlet.in/flatletuserinsert/flatletuserinsert.jsp?dbqry=" + dbqry;
        String urlFinal = url.replace(" ", "%20");


        StringRequest stringRequest = new StringRequest(Request.Method.GET, urlFinal,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        Log.i(TAG, "onResponse: " + response);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(TAG, "onErrorResponse: " + error);

            }
        });
        RequestQueue queue1 = Volley.newRequestQueue(getActivity());
        queue1.add(stringRequest);

    }
}
