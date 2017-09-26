package in.flatlet.www.Flatlet.thirdActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import in.flatlet.www.Flatlet.R;
import in.flatlet.www.Flatlet.Utility.MySingleton;


public class MainActivity_third extends AppCompatActivity {
    private String hostel_title;
    /*private int imageCount;*/

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.activity3_action, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.reportImages:
                Intent intent2 = new Intent (Intent.ACTION_VIEW , Uri.parse("mailto:" + "flatletindia@gmail.com"));
                intent2.putExtra(Intent.EXTRA_SUBJECT, "Reporting Incorrect images for "+hostel_title);
                intent2.putExtra(Intent.EXTRA_TEXT, "Hi there !! the provided images for the " + hostel_title + " are incorrect.");
                startActivity(intent2);
        }
        return true;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hostel_title = getIntent().getStringExtra("hostel_title");
        /*imageCount = getIntent().getIntExtra("imageCount",3);*/

        if (MySingleton.getInstance(getApplicationContext()).isOnline()){
            setContentView(R.layout.activity_main_third);
            MyPageAdapter myPageAdapter = new MyPageAdapter(getSupportFragmentManager());
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
            ViewPager pager = (ViewPager) findViewById(R.id.pager);
            pager.setAdapter(myPageAdapter);
            //now when the pager changes we wanna change the tab and vice-versa
            tabLayout.setupWithViewPager(pager);
            pager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
            if (getSupportActionBar() != null) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setDisplayShowHomeEnabled(true);
                getSupportActionBar().setHomeButtonEnabled(true);
                toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onBackPressed();
                        finish();
                    }
                });
            }
        }
        else {
            Toast.makeText(getApplicationContext(),"No Internet Connection ! Please Try Again",Toast.LENGTH_SHORT).show();
        }

    }
}

class MyPageAdapter extends FragmentStatePagerAdapter {

    MyPageAdapter(FragmentManager fm) {

        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        String TAG = "MainActivity";
        Log.i(TAG, "getItem:called ");

        //this method is going to give the position at
        //which you have to return the fragment so we have to create
        //a object of MyFragment class'
        switch (position) {
            case 1:
                return new MyWebView();
            case 0:
                return new ImageSwitcherFragment();

        }
        return null;

    }

    @Override
    public int getCount() {

        return 2;
    }

    public CharSequence getPageTitle(int position) {
            /*return super.getPageTitle(position);*/
        switch (position) {
            case 1:
                return "360° Live Tour";

            case 0:
                return "Gallery";

        }
       /* return "Page" + position;*/
        return null;
    }

}

