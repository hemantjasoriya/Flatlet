package in.flatlet.www.Flatlet.Home;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;

import com.facebook.accountkit.AccessToken;
import com.facebook.accountkit.AccountKit;

import in.flatlet.www.Flatlet.Home.fragments.favouriteFragment.FavouriteFragment;
import in.flatlet.www.Flatlet.Home.fragments.favouriteFragment.LogoutFavouriteFragment;
import in.flatlet.www.Flatlet.Home.fragments.homefragment.HomeFragment;
import in.flatlet.www.Flatlet.Home.fragments.morefragment.MoreFragment;
import in.flatlet.www.Flatlet.Home.fragments.profilefragment.CreateProfileFragment;
import in.flatlet.www.Flatlet.Home.fragments.profilefragment.LoginFragment;
import in.flatlet.www.Flatlet.Home.fragments.profilefragment.SavedProfileFragment;
import in.flatlet.www.Flatlet.Home.fragments.searchfragment.LocalityListFragment;
import in.flatlet.www.Flatlet.R;


public class FirstActivity extends AppCompatActivity {
    private final static String TAG = "MainActivity";
    private static Fragment fragment;
    private static FragmentTransaction fragmentTransaction;
    BottomNavigationView navigation;
    private final BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            AccessToken accessToken = AccountKit.getCurrentAccessToken();
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    fragment = new HomeFragment();
                    fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.replace(R.id.content, fragment, "fragmentHome");

                    fragmentTransaction.commit();
                    return true;
                case R.id.navigation_search:
                    fragment = new LocalityListFragment();
                    break;
                    /*fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.replace(R.id.content, fragment, "search");

                    fragmentTransaction.commit();
                    return true;*/

                case R.id.navigation_profile:
                    SharedPreferences sharedPreferences = getSharedPreferences("personalInfo", Context.MODE_PRIVATE);

                   if (accessToken==null){
                       fragment=new LoginFragment();
                       Bundle args = new Bundle();
                       String hostel_title = getIntent().getStringExtra("hostel_title");
                       args.putString("hostel_title",hostel_title);
                       fragment.setArguments(args);

                   }
                   else if (!sharedPreferences.getString("userName","johndoe").equalsIgnoreCase("johndoe")){
                       fragment=new SavedProfileFragment();
                   }
                   else {
                       fragment=new CreateProfileFragment();
                   }
                   break;
                    /*fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.replace(R.id.content, fragment, "fragmentProfile");

                    fragmentTransaction.commit();
                    return true;*/
                case R.id.navigation_favourites:
                    if (accessToken == null)
                        fragment = new LogoutFavouriteFragment();
                    else
                        fragment = new FavouriteFragment();
                    break;

                   /* fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.replace(R.id.content, fragment, "fragmentFavourite");


                    fragmentTransaction.commit();
                    return true;*/

                case R.id.navigation_more:
                    fragment = new MoreFragment();
                    break;
                   /* fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.replace(R.id.content, fragment, "fragmentSearch");

                    fragmentTransaction.commit();

                    return true;*/
            }
             fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.replace(R.id.content, fragment, "fragmentSearch");

                    fragmentTransaction.commit();

                    return true;

        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "onCreate: oncreate of firstactivity started");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);
        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        BottomNavigationViewHelper.disableShiftMode(navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        Intent intent = getIntent();
        int i = intent.getFlags();
        Log.i(TAG, "onCreate: Flag value is" +i);

        if (i == 1) {
            fragment = new HomeFragment();
            fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.replace(R.id.content, fragment, "fragmetHome");
            fragmentTransaction.commit();
        }
        else if (i==2){
            Log.i(TAG, "onCreate: ");
            fragment = new LoginFragment();
            fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.replace(R.id.content, fragment, "loginfragmentmain");
            fragmentTransaction.commit();
            navigation.setSelectedItemId(R.id.navigation_profile);
        }
        else {
            fragment = new LoginFragment();
            fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.replace(R.id.content, fragment, "loginfragment");
            fragmentTransaction.commit();
            navigation.setSelectedItemId(R.id.navigation_profile);
        }


    }


    @Override
    public void onBackPressed() {
        FragmentManager fm = getSupportFragmentManager();

       Fragment fragment=fm.findFragmentByTag("fragmentSearch");
        Fragment fragment1=fm.findFragmentByTag("fragmentHome");
        Fragment fragment2=fm.findFragmentByTag("loginfragmentmain");
        if (fragment.isVisible()){
            fragment = new HomeFragment();
            fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.replace(R.id.content, fragment, "fragmentSearch");

            fragmentTransaction.commit();
            navigation.setSelectedItemId(R.id.navigation_home);
        }
        else if (fragment2.isVisible()){
            super.onBackPressed();
        }


        else if (fragment1.isVisible()){
            new AlertDialog.Builder(this)
                    .setTitle("Really Exit?")
                    .setMessage("Are you sure you want to exit?")
                    .setNegativeButton("no", null)
                    .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent homeIntent = new Intent(Intent.ACTION_MAIN);
                            homeIntent.addCategory(Intent.CATEGORY_HOME);
                            homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(homeIntent);
                        }
                    }).create().show();
        }
        else {
            fragment = new MoreFragment();
            fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.replace(R.id.content, fragment, "fragmentSearch");
            fragmentTransaction.commit();

        }
            //additional code




    }


}
