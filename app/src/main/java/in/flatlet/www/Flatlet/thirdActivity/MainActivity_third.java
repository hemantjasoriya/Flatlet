package in.flatlet.www.Flatlet.thirdActivity;

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
import android.view.View;

import in.flatlet.www.Flatlet.R;


public class MainActivity_third extends AppCompatActivity {
    private Toolbar toolbar;
    private ViewPager pager;
    private TabLayout tabLayout;
    private MyPageAdapter myPageAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_third);
        myPageAdapter = new MyPageAdapter(getSupportFragmentManager());
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        pager = (ViewPager) findViewById(R.id.pager);
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
                }
            });
        }
    }
}

class MyPageAdapter extends FragmentStatePagerAdapter {
    public final String TAG = "MainActivity";

    public MyPageAdapter(FragmentManager fm) {

        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Log.i(TAG, "getItem:called ");

        //this method is going to give the position at
        //which you have to return the fragment so we have to create
        //a object of MyFragment class'
        /*MainActivity.MyFragment myFragment = MainActivity.MyFragment.newInstance(position);
        return myFragment;*/
        switch (position) {
            case 1:
                MyWebView webView = new MyWebView();
                return webView;
            case 0:
                ImageSwitcherFragment imageSwitcherFragment = new ImageSwitcherFragment();
                return imageSwitcherFragment;

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

