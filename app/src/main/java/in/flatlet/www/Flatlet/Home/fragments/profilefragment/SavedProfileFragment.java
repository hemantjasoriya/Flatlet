package in.flatlet.www.Flatlet.Home.fragments.profilefragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.accountkit.AccountKit;

import in.flatlet.www.Flatlet.R;
import in.flatlet.www.Flatlet.Utility.MySingleton;
import in.flatlet.www.Flatlet.recyclerView.FeedReaderContract;
import in.flatlet.www.Flatlet.recyclerView.FeedReaderDbHelper;
import in.flatlet.www.Flatlet.welcome.LoginActivity;

/**
 * Created by javax on 21-Aug-17.
 */

public class SavedProfileFragment extends Fragment {
    private EditText userNameEditText;
    private EditText userEmailEditText;
    private TextView userNameEditButton, emailEditButton, textView;
    private int i = 0;
    private RequestQueue queue1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.savedprofile_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        userNameEditText = (EditText) getActivity().findViewById(R.id.userNameEditText);
        userEmailEditText = (EditText) getActivity().findViewById(R.id.userEmailEditText);
        EditText userMobileEditText = (EditText) getActivity().findViewById(R.id.userMobileEditText);
        userNameEditButton = (TextView) getActivity().findViewById(R.id.userNameEditButton);
        emailEditButton = (TextView) getActivity().findViewById(R.id.emailEditButton);
        Button logoutButton1 = (Button) getActivity().findViewById(R.id.logoutButton1);
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("personalInfo", Context.MODE_PRIVATE);
        userNameEditText.setText(sharedPreferences.getString("userName", "john doe"));
        userEmailEditText.setText(sharedPreferences.getString("userEmail", "@johndoe"));
        userMobileEditText.setText(sharedPreferences.getString("userMobile", "911"));

        //clicking on user name edit button
        userNameEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("personalInfo", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                if (((userNameEditButton.getText().toString()).equalsIgnoreCase("Edit"))) {
                    userNameEditButton.setText("save changes");
                    userNameEditText.setEnabled(true);

                } else {

                    userNameEditButton.setText("Edit");
                    userNameEditText.setEnabled(false);
                    editor.putString("userName", userNameEditText.getText().toString());
                    editor.apply();
                    i++;

                }

            }
        });

        //clicking on user email edit button
        emailEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("personalInfo", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                if (((emailEditButton.getText().toString()).equalsIgnoreCase("Edit"))) {
                    emailEditButton.setText("save changes");
                    userEmailEditText.setEnabled(true);
                } else {
                    emailEditButton.setText("Edit");
                    userEmailEditText.setEnabled(false);
                    editor.putString("userEmail", userEmailEditText.getText().toString());
                    editor.apply();
                    i++;

                }

            }
        });

        // log out button onlick
        logoutButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });

    }

    private void logout() {
        AccountKit.logOut();
        startActivity(new Intent(getActivity(),LoginActivity.class));
        // changing user name in shared preferences
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("personalInfo", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        // delete all rows from sqlite database
        FeedReaderDbHelper feedReaderDbHelper = new FeedReaderDbHelper(getContext());
        SQLiteDatabase db_favourite = feedReaderDbHelper.getWritableDatabase();
        db_favourite.execSQL("delete from " + FeedReaderContract.FeedEntry.TABLE_NAME);
    }

    @Override
    public void onStop() {
        super.onStop();

        if (i > 0 && MySingleton.getInstance(getContext()).isOnline()) {
            //sending changes to database
            SharedPreferences sharedPreferences = getActivity().getSharedPreferences("personalInfo", Context.MODE_PRIVATE);

            String dbqry = "UPDATE `our_users` SET `user_ka_naam`='" + sharedPreferences.getString("userName", "john doe") + "'," +
                    "`user_emailid`='" + sharedPreferences.getString("userEmail", "@johndoe") + "' WHERE `user_mobile`=" +
                    "'" + sharedPreferences.getString("userMobile", "911") + "'";


            String url = "http://flatlet.in/flatletuserinsert/flatletuserinsert.jsp?dbqry=" + dbqry;
            String urlFinal = url.replace(" ", "%20");


            StringRequest stringRequest = new StringRequest(Request.Method.GET, urlFinal,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // Display the first 500 characters of the response string.


                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });
            queue1 = Volley.newRequestQueue(getActivity());
            stringRequest.setTag("MyRequestTag");
            queue1.add(stringRequest);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (queue1 != null) {
            queue1.cancelAll("MyRequestTag");
        }
    }
}
