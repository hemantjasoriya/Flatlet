package in.flatlet.www.Flatlet.Home;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
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
    private final BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            AccessToken accessToken = AccountKit.getCurrentAccessToken();
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    fragment = new HomeFragment();
                    fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.content, fragment, "fragmetHome");
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                    return true;
                case R.id.navigation_search:
                    fragment = new LocalityListFragment();
                    fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.content, fragment, null);
                    fragmentTransaction.commit();
                    return true;

                case R.id.navigation_profile:
                    SharedPreferences sharedPreferences = getSharedPreferences("personalInfo", Context.MODE_PRIVATE);

                   if (accessToken==null){
                       fragment=new LoginFragment();
                       /*Bundle args = new Bundle();
                       String hostel_title = getIntent().getStringExtra("hostel_title");
                       args.putString("hostel_title",hostel_title);
                       fragment.setArguments(args);*/

                   }
                   else if (!sharedPreferences.getString("userName","johndoe").equalsIgnoreCase("johndoe")){
                       fragment=new SavedProfileFragment();
                   }
                   else {
                       fragment=new CreateProfileFragment();
                   }
                    fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.content, fragment, "fragmentProfile");
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                    return true;
                case R.id.navigation_favourites:
                    if (accessToken == null)
                        fragment = new LogoutFavouriteFragment();
                    else
                        fragment = new FavouriteFragment();

                    fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.content, fragment, "fragmentFavourite");
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                    return true;

                case R.id.navigation_more:
                    fragment = new MoreFragment();
                    fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.content, fragment, "fragmentSearch");
                    fragmentTransaction.commit();

                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "onCreate: oncreate of firstactivity started");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        BottomNavigationViewHelper.disableShiftMode(navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        Intent intent = getIntent();
        int i = intent.getFlags();
        Log.i(TAG, "onCreate: Flag value is" +i);

        if (i == 1) {
            fragment = new HomeFragment();
            fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.add(R.id.content, fragment, "fragmetHome");
            fragmentTransaction.commit();
        } else {
            fragment = new LoginFragment();
            fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.add(R.id.content, fragment, "fragmetHome");
            fragmentTransaction.commit();
            navigation.setSelectedItemId(R.id.navigation_profile);
        }


    }


    @Override
    public void onBackPressed() {
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


}
