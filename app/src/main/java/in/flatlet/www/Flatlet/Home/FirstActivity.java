package in.flatlet.www.Flatlet.Home;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import in.flatlet.www.Flatlet.Home.fragments.favouritefragment.FavouriteFragment;
import in.flatlet.www.Flatlet.Home.fragments.homefragment.HomeFragment;
import in.flatlet.www.Flatlet.Home.fragments.morefragment.MoreFragment;
import in.flatlet.www.Flatlet.Home.fragments.profilefragment.CreateProfileFragment;
import in.flatlet.www.Flatlet.Home.fragments.profilefragment.SavedProfileFragment;
import in.flatlet.www.Flatlet.Home.fragments.searchfragment.LocalityListFragment;
import in.flatlet.www.Flatlet.R;
import in.flatlet.www.Flatlet.recyclerView.FeedReaderDbHelper;


public class FirstActivity extends AppCompatActivity {
    private static Fragment fragment;
    private static FragmentTransaction fragmentTransaction;
    BottomNavigationView navigation;

    private final BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
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


                case R.id.navigation_profile:
                    SharedPreferences sharedPreferences = getSharedPreferences("personalInfo", Context.MODE_PRIVATE);


                    if (!sharedPreferences.getString("userName", "johndoe").equalsIgnoreCase("johndoe")) {
                        fragment = new SavedProfileFragment();
                    } else {
                        fragment = new CreateProfileFragment();
                    }
                    break;

                case R.id.navigation_favourites:

                    fragment = new FavouriteFragment();
                    break;


                case R.id.navigation_more:
                    fragment = new MoreFragment();
                    break;

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

        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);
        navigation = findViewById(R.id.navigation);
        BottomNavigationViewHelper.disableShiftMode(navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        Intent intent = getIntent();
        int i = intent.getFlags();

        fragment = new HomeFragment();
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.replace(R.id.content, fragment, "fragmentHome");
        fragmentTransaction.commit();


        FeedReaderDbHelper feedReaderDbHelper = new FeedReaderDbHelper(FirstActivity.this);

        SQLiteDatabase db_favourite = feedReaderDbHelper.getWritableDatabase();

        SharedPreferences pref_default = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        if (!pref_default.getBoolean("thirdTime", false)) {
            // <---- run your one time code here

            feedReaderDbHelper.onCreateOriginal(db_favourite);

            SharedPreferences.Editor editor = pref_default.edit();
            editor.putBoolean("thirdTime", true);
            editor.apply();
        }
    }

    @Override
    public void onBackPressed() {
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentByTag("fragmentSearch");
        Fragment fragment1 = fm.findFragmentByTag("fragmentHome");
        if (fragment1.isVisible()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Hey !")
                    .setMessage("Are you sure you want to exit?")
                    .setNegativeButton("no", null)
                    .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent homeIntent = new Intent(Intent.ACTION_MAIN);
                            homeIntent.addCategory(Intent.CATEGORY_HOME);
                            homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(homeIntent);
                            finish();
                        }
                    }).create();
            builder.setCancelable(false);
            builder.show();

        } else if (fragment.isVisible()) {
            fragment = new HomeFragment();
            fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.replace(R.id.content, fragment, "fragmentSearch");
            fragmentTransaction.commit();
            navigation.setSelectedItemId(R.id.navigation_home);
        } else {

            navigation.setSelectedItemId(R.id.navigation_more);
        }
    }
}
