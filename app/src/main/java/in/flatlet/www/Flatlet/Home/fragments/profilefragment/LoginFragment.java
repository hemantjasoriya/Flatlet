package in.flatlet.www.Flatlet.Home.fragments.profilefragment;

import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.accountkit.Account;
import com.facebook.accountkit.AccountKit;
import com.facebook.accountkit.AccountKitCallback;
import com.facebook.accountkit.AccountKitError;
import com.facebook.accountkit.AccountKitLoginResult;
import com.facebook.accountkit.PhoneNumber;
import com.facebook.accountkit.ui.AccountKitActivity;
import com.facebook.accountkit.ui.AccountKitConfiguration;
import com.facebook.accountkit.ui.LoginType;

import in.flatlet.www.Flatlet.Home.fragments.homefragment.HomeFragment;
import in.flatlet.www.Flatlet.R;

/**
 * Created by javax on 20-Aug-17.
 */

public class LoginFragment extends Fragment  {

    private final String TAG="loginfragment";
    public static int APP_REQUEST_CODE = 99;
    private Button loginButton;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.login_fragment, container, false);



        return view;

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

        loginButton=(Button)getActivity().findViewById(R.id.loginButton);




        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "button clicked ");
                final Intent intent = new Intent(getActivity(), AccountKitActivity.class);
                AccountKitConfiguration.AccountKitConfigurationBuilder configurationBuilder =
                        new AccountKitConfiguration.AccountKitConfigurationBuilder(
                                LoginType.PHONE,
                                AccountKitActivity.ResponseType.TOKEN); // or .ResponseType.TOKEN
                // ... perform additional configuration ...
                configurationBuilder.setDefaultCountryCode("IN");

                intent.putExtra(
                        AccountKitActivity.ACCOUNT_KIT_ACTIVITY_CONFIGURATION,
                        configurationBuilder.build());
                startActivityForResult(intent, APP_REQUEST_CODE);

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
                String toastMessage;
                if (loginResult.getError() != null) {
                    toastMessage = loginResult.getError().getErrorType().getMessage();
                    /*showErrorActivity(loginResult.getError());*/
                } else if (loginResult.wasCancelled()) {
                    toastMessage = "Login Cancelled";
                } else {
                    if (loginResult.getAccessToken() != null) {
                        Log.i(TAG, "onActivityResult: access token");
                        Fragment fragment= new CreateProfileFragment();
                        android.support.v4.app.FragmentTransaction fragmentTransaction= getActivity().getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.content,fragment,"fragmetHome");
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                        toastMessage = "Success:" + loginResult.getAccessToken().getAccountId();
                    } else {
                        toastMessage = String.format(
                                "Success:%s...",
                                loginResult.getAuthorizationCode().substring(0,10));
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







