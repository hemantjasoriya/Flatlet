package in.flatlet.www.Flatlet.welcome;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import in.flatlet.www.Flatlet.Home.FirstActivity;
import in.flatlet.www.Flatlet.R;


public class WelcomeActivity extends AppCompatActivity {
    private final String TAG = "WelcomeActivity";
    private ViewPager pager;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_activity);
        Button buttonPrev = (Button) findViewById(R.id.buttonPrev);
        Button buttonNext = (Button) findViewById(R.id.buttonNext);
        final WelcomePagerAdapter pagerAdapter = new WelcomePagerAdapter(getSupportFragmentManager());
        pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(pagerAdapter);
        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getItem(1) < 3) {
                    Log.i(TAG, "onClick: " + pager.getCurrentItem());

                    pager.setCurrentItem(getItem(+1), true);
                } else {
                    startActivity(new Intent(WelcomeActivity.this, FirstActivity.class).setFlags(1));
                }
            }
        });
        buttonPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "onClick: " + pager.getCurrentItem());
                pager.setCurrentItem(getItemPrev(+1), true);

            }
        });

    }

    private int getItem(int i) {
        return pager.getCurrentItem() + i;
    }

    private int getItemPrev(int i) {
        return pager.getCurrentItem() - i;
    }

    private class WelcomePagerAdapter extends FragmentStatePagerAdapter {

        WelcomePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:

                    return new WelcomeFragment1();
                case 1:

                    return new WelcomeFragment2();
                case 2:

                    return new WelcomeFragment3();
            }
            return null;
        }

        @Override
        public int getCount() {
            return 3;
        }

    }

}

