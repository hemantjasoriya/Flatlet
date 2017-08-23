package in.flatlet.www.Flatlet.Home.fragments.profilefragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.squareup.picasso.Picasso;
import org.json.JSONObject;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import de.hdodenhof.circleimageview.CircleImageView;
import in.flatlet.www.Flatlet.R;
import in.flatlet.www.Flatlet.utility.Utility;


public class ProfileFragment extends Fragment implements AdapterView.OnItemClickListener, View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {
    private final String TAG = "FragmentDashboard";
    private Toolbar toolbar;
    private String[] loginArray;
    private int[] imgid;
    /*private ListView listView;*/
    private Button SignOut;
    private SignInButton SignIn;
    private LoginButton loginButton;

    private TextView Name, Email;
    private CircleImageView prof_pic;
    private GoogleApiClient googleApiClient;
    private static final int REQ_CODE = 9001;
    private CardView login_card;
    private String name;
    private String email;
    private String img_url;
    private SharedPreferences sharedPreferences;
    private int i = 1;
    private CallbackManager callbackManager;
    private AccessTokenTracker accessTokenTracker;
    private ProfileTracker mProfileTracker;
    private String dbqry;
    private AccessToken accessToken;
    String userID;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        printHashKey();
        Log.i(TAG, "onCreate: ");
        callbackManager = CallbackManager.Factory.create();
    }
    @Nullable
    @Override

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        Log.i(TAG, "onCreateView: FragmentDashboard");
        View view = inflater.inflate(R.layout.profile_fragment, container, false);
        loginButton = (LoginButton) view.findViewById(R.id.login_button);
        loginButton.setReadPermissions(Arrays.asList(
                "public_profile", "email"));
        loginButton.setReadPermissions("email");
        loginButton.setFragment(this);

        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {


            @Override
            public void onSuccess(LoginResult loginResult) {

                GraphRequest graphRequest= GraphRequest.newMeRequest(
                        loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject me, GraphResponse response) {
                                if (response.getError() != null) {
                                    // handle error
                                } else {
                                    name=me.optString("name");
                                    email = me.optString("email");
                                    Log.i(TAG, "onCompleted: "+email);
                                    Name.setText(name);
                                    Email.setText(email);
                                    userID = me.optString("id");
                                    Picasso.with(getActivity()).load("https://graph.facebook.com/" + userID+ "/picture?type=large").into(prof_pic);
                                    updateUIFromFacebook(true);
                                    sharedPreferences = getActivity().getSharedPreferences("fbpersonalInfo", Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putString("userName", name);
                                    editor.putString("email", email);
                                    editor.putString("userID",userID);
                                    editor.apply();
                                    dbqry = "INSERT INTO `our_users`(`user_ka_naam`,`user_emailid`) VALUES ('" + name + "','" + email + "')";
                                    Log.i(TAG, "handleResult: dbqry is " + dbqry);
                                    // Instantiate the RequestQueue.

                                    String url = "http://flatlet.in/flatletuserinsert/flatletuserinsert.jsp?dbqry=" + dbqry;
                                    String urlFinal = url.replace(" ", "%20");

// Request a string response from the provided URL.
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
// Add the request to the RequestQueue.
                                    RequestQueue queue1 = Volley.newRequestQueue(getActivity());
                                    queue1.add(stringRequest);

                                }
                            }
                        });
                Bundle bundle=new Bundle();
                bundle.putString("fields","id,email,name");
                graphRequest.setParameters(bundle);
                graphRequest.executeAsync();
            }

            @Override
            public void onCancel() {
            }
            @Override
            public void onError(FacebookException error) {
            }
        });
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        Log.i(TAG, "onViewCreated: ");
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);
        Log.i(TAG, "onActivityCreated: ");
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);

        loginArray = new String[]{"My Favourites", "My Reviews", "Invite"};
        imgid = new int[]{R.drawable.favourite,R.drawable.ic_rate_review_black_24dp,
                R.drawable.ic_insert_invitation_black_24dp};
        login_card = (CardView) getActivity().findViewById(R.id.login_card);
        login_card.setVisibility(View.GONE);

        ;
        SignOut = (Button) getActivity().findViewById(R.id.logout_button);
        SignIn = (SignInButton) getActivity().findViewById(R.id.signin_button);

        Name = (TextView) getActivity().findViewById(R.id.user_name);
        Email = (TextView) getActivity().findViewById(R.id.email);
        /*listView = (ListView) getActivity().findViewById(R.id.login_list);*/
        prof_pic = (CircleImageView) getActivity().findViewById(R.id.circle_image_view);
        login_card = (CardView) getActivity().findViewById(R.id.login_card);
        /*listView.setOnItemClickListener(this);*/
        SignIn.setOnClickListener(this);
        SignOut.setOnClickListener(this);

        GoogleSignInOptions signInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        googleApiClient = new GoogleApiClient.Builder(getActivity()).enableAutoManage(getActivity(), this).addApi(Auth.GOOGLE_SIGN_IN_API, signInOptions).build();
        Log.i(TAG, "onActivityCreated: googleapi connected");
        sharedPreferences = getActivity().getSharedPreferences("personalInfo", Context.MODE_PRIVATE);
        if (!sharedPreferences.getString("userName", "johndoe").equalsIgnoreCase("flatlet") && !sharedPreferences.getString("userName", "johndoe").equalsIgnoreCase("Application")) {
            Log.i(TAG, "onActivityCreated: keep me logged in");
            Name.setText(sharedPreferences.getString("userName", "johndoe"));
            Email.setText(sharedPreferences.getString("email", "@johndoe"));

            Glide.with(this).load(sharedPreferences.getString("img_url", "@drawable/dummy.jpg")).into(prof_pic);
            updateUI(true);
        }

        if (isLoggedIn()) {
            Log.i(TAG, "onActivityCreated: fb is logged in");
            sharedPreferences = getActivity().getSharedPreferences("fbpersonalInfo", Context.MODE_PRIVATE);
            Name.setText(sharedPreferences.getString("userName", "johndoe"));
            Email.setText(sharedPreferences.getString("email", "@johndoe"));
            Picasso.with(getActivity()).load("https://graph.facebook.com/" + sharedPreferences.getString("userID","")+ "/picture?type=large").into(prof_pic);
            updateUIFromFacebook(true);
        }

        /*MyArrayAdapter adpt = new MyArrayAdapter(getActivity(), loginArray, imgid);*/
       /* listView.setAdapter(adpt);*/
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.i(TAG, "onItemClick: ");
        /*String s = listView.getItemAtPosition(position).toString();*/
       /* Toast.makeText(getActivity().getApplicationContext(), "you clicked on " + s, Toast.LENGTH_SHORT).show();*/
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.signin_button:
                signIn();
                break;
            case R.id.logout_button:
                Log.i(TAG, "onClick: signout");
                signOut();
                break;
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }

    private void signIn() {
        Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(intent, REQ_CODE);

    }
    private void signOut() {
        Log.i(TAG, "signOut: if");
        /*if (googleApiClient.isConnected()){*/

        Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {
                Log.i(TAG, "onResult: signout");
                sharedPreferences = getActivity().getSharedPreferences("personalInfo", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("userName", "flatlet");
                editor.apply();
                Log.i(TAG, "onResult: after apply");
                updateUI(false);
            }
        });

    }

    private void handleResult(GoogleSignInResult result) {
        if (result.isSuccess()) {
            GoogleSignInAccount account = result.getSignInAccount();
            name = account != null ? account.getDisplayName() : null;

            Log.i(TAG, "handleResult: " + name);
            Utility utility=new Utility();
            utility.setUser_name(account.getDisplayName());

            email = account.getEmail();
            name=account.getDisplayName();
            Log.i(TAG, "handleResult: " + email);
            Uri uri = account.getPhotoUrl();
            if (uri == null) {
                prof_pic.setImageResource(R.drawable.dummy);
            } else {
                img_url = uri.toString();
                Glide.with(this).load(img_url).into(prof_pic);
            }
            Name.setText(name);
            Email.setText(email);
            sharedPreferences = getActivity().getSharedPreferences("personalInfo", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("userName", name);
            editor.putString("email", email);
            editor.putString("img_url", img_url);
            editor.apply();
            updateUI(true);
            dbqry = "INSERT INTO `our_users`(`user_ka_naam`,`user_emailid`) VALUES ('" + name + "','" + email + "')";
            Log.i(TAG, "handleResult: dbqry is " + dbqry);
            // Instantiate the RequestQueue.

            String url = "http://flatlet.in/flatletuserinsert/flatletuserinsert.jsp?dbqry=" + dbqry;
            String urlFinal = url.replace(" ", "%20");

// Request a string response from the provided URL.
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
// Add the request to the RequestQueue.
            RequestQueue queue = Volley.newRequestQueue(getActivity());
            queue.add(stringRequest);

        } else {
            updateUI(false);
        }

    }

    private void updateUI(boolean isLogin) {
        if (isLogin) {
            login_card.setVisibility(View.VISIBLE);
            SignIn.setVisibility(View.GONE);
            loginButton.setVisibility(View.GONE);
            SignOut.setVisibility(View.VISIBLE);
        } else {
            Log.i(TAG, "updateUI: ");
            login_card.setVisibility(View.GONE);
            SignIn.setVisibility(View.VISIBLE);
            loginButton.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i(TAG, "onActivityResult: ");
        if (requestCode == REQ_CODE) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleResult(result);
        }
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG, "onResume: ");
        if (i != 1) {
            Log.i(TAG, "onResume: " + i);
            googleApiClient.connect();
        }
        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken,
                                                       AccessToken currentAccessToken) {
                if (currentAccessToken == null) {
                    //write your code here what to do when user logout
                    updateUIFromFacebook(false);
                }
            }
        };

    }

    @Override
    public void onPause() {
        super.onPause();
        Log.i(TAG, "onPause: " + i);
    }

    @Override
    public void onStop() {
        Log.i(TAG, "onStop: ");
        super.onStop();
        i++;
        googleApiClient.stopAutoManage(getActivity());
        googleApiClient.disconnect();
        Log.i(TAG, "onStop: disconnected " + i);
        Log.i(TAG, "fbDataSharedPreferences: ");
        Log.i(TAG, "fbDataSharedPreferences: name"+name);
    }
    @Override
    public void onDestroy() {
        Log.i(TAG, "onDestroy: ");
        super.onDestroy();
        mProfileTracker.stopTracking();
    }

    private void printHashKey() {
        Log.i(TAG, "printHashKey: started");
        try {
            PackageInfo info =getContext().getPackageManager().getPackageInfo(
                    "in.flatlet.www.Flatlet",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        }
        catch (PackageManager.NameNotFoundException e) {
            Log.d(TAG, "printHashKey: "+e);

        }
        catch (NoSuchAlgorithmException e) {
            Log.d(TAG, "printHashKey: "+e);

        }
    }
    private void updateUIFromFacebook(boolean isLogin) {
        if (isLogin) {
            Log.i(TAG, "updateUIFromFacebook: ");
            login_card.setVisibility(View.VISIBLE);
            SignIn.setVisibility(View.GONE);
            SignOut.setVisibility(View.GONE);
        }
        else {
            login_card.setVisibility(View.GONE);
            SignIn.setVisibility(View.VISIBLE);
            SignOut.setVisibility(View.VISIBLE);
        }
    }
    private boolean isLoggedIn() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        return accessToken != null;
    }









}
