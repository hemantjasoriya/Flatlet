package in.flatlet.www.Flatlet.welcome;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.facebook.accountkit.AccessToken;
import com.facebook.accountkit.AccountKit;

import in.flatlet.www.Flatlet.Home.FirstActivity;
import in.flatlet.www.Flatlet.R;


public class WelcomeActivity extends AppCompatActivity {
    private final String TAG = "WelcomeActivity";
    private ViewPager pager;
    SharedPreferences sharedPreferences;
    AccessToken accessToken;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_activity);
        sharedPreferences=getSharedPreferences("personalInfo", Context.MODE_PRIVATE);
        accessToken= AccountKit.getCurrentAccessToken();
        Button buttonPrev = (Button) findViewById(R.id.buttonPrev);
        Button buttonNext = (Button) findViewById(R.id.buttonNext);
        final WelcomePagerAdapter pagerAdapter = new WelcomePagerAdapter(getSupportFragmentManager());
        pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(pagerAdapter);
        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getItem(1) < 3) {


                    pager.setCurrentItem(getItem(+1), true);
                }
               else {
                    if (accessToken==null){
                        startActivity(new Intent(WelcomeActivity.this,LoginActivity.class).setFlags(0));
                        finish();
                    }
                    else if (sharedPreferences.getString("userName","johndoe").equals("johndoe")){
                        startActivity(new Intent(WelcomeActivity.this,LoginActivity.class).setFlags(1));
                        finish();

                    }
                    else {
                        startActivity(new Intent(WelcomeActivity.this, FirstActivity.class).setFlags(1));
                        finish();
                    }
                }
            }
        });
        buttonPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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

