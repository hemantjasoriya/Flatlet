package in.flatlet.www.Flatlet.filter;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.SeekBar;

import in.flatlet.www.Flatlet.R;
import in.flatlet.www.Flatlet.recyclerView.MainActivity;


public class FilterActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    GetDataAdapter getDataAdapter = new GetDataAdapter();


    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private FilterAdapter adapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    CheckBox checkBox6, checkBox7, checkBox8, checkBox9, checkBox10;
    RadioButton pricehtl, pricelth, dimhtl, dimlth, ac, nonac, checkBox, checkBox2, checkBox3, checkBox4;
    SeekBar seekBar;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.filter_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        adapter = new FilterAdapter(getSupportFragmentManager());
        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(adapter);
        String query= null;
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

    }
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        String query=null;
        checkBox = (RadioButton) findViewById(R.id.checkBox);
        checkBox2 = (RadioButton) findViewById(R.id.checkBox2);
        checkBox3 = (RadioButton) findViewById(R.id.checkBox3);
        checkBox4 = (RadioButton) findViewById(R.id.checkBox4);
        checkBox6 = (CheckBox) findViewById(R.id.checkBox6);
        checkBox7 = (CheckBox) findViewById(R.id.checkBox7);
        checkBox8 = (CheckBox) findViewById(R.id.checkBox8);
        checkBox9 = (CheckBox) findViewById(R.id.checkBox9);
        checkBox10 = (CheckBox) findViewById(R.id.checkBox10);
        pricehtl = (RadioButton) findViewById(R.id.pricehtl);
        pricelth = (RadioButton) findViewById(R.id.pricelth);
        dimhtl = (RadioButton) findViewById(R.id.dimhtl);
        dimlth = (RadioButton) findViewById(R.id.dimlth);
        ac = (RadioButton) findViewById(R.id.ac);
        nonac = (RadioButton) findViewById(R.id.nonac);
        seekBar = (SeekBar) findViewById(R.id.seekBar1);

        switch (item.getItemId()) {

            case R.id.action_reset:

                checkBox.setChecked(false);
                checkBox2.setChecked(false);
                checkBox3.setChecked(false);
                checkBox4.setChecked(false);
                checkBox6.setChecked(false);
                checkBox7.setChecked(false);
                checkBox8.setChecked(false);
                checkBox9.setChecked(false);
                checkBox10.setChecked(false);
                pricehtl.setChecked(false);
                pricelth.setChecked(false);
                dimlth.setChecked(false);
                dimhtl.setChecked(false);
                ac.setChecked(false);
                nonac.setChecked(false);
                seekBar.setProgress(20000);
                break;

            case R.id.action_apply:
                String roomType=null;

                if (checkBox4.isChecked() && ac.isChecked()) {
                    query = "select * from `hostel_specs` where rent_single_ac>0 AND rent_single_ac<" + seekBar.getProgress();
                    roomType="rent_single_ac";
                }
                if (checkBox4.isChecked() && nonac.isChecked()) {
                    query = "select * from `hostel_specs` where rent_single_nonac>0 AND rent_single_nonac<" + seekBar.getProgress();
                    roomType="rent_single_nonac";
                }
                if (checkBox3.isChecked() && ac.isChecked()) {
                    query = "select * from `hostel_specs` where rent_double_ac>0 AND rent_double_ac<" + seekBar.getProgress();
                    roomType="rent_double_ac";
                }
                if (checkBox3.isChecked() && nonac.isChecked()) {
                    query = "select * from `hostel_specs` where rent_double_nonac>0 AND rent_double_nonac<" + seekBar.getProgress();
                    roomType="rent_double_nonac";
                }

                if (checkBox.isChecked()) {
                    query = query + " AND gender='boys' ";
                    Log.i(TAG, "onOptionsItemSelected: query formed yet is" + query);
                }
                if (checkBox2.isChecked()) {
                    query = query + " AND gender='girls' ";
                    Log.i(TAG, "onOptionsItemSelected: string formed yet is" + query);
                }
                if (checkBox7.isChecked()) {
                    query = query + " AND ame_eve_snacks=1";
                    Log.i(TAG, "onOptionsItemSelected: query formed yet is" + query);
                }
                if (checkBox6.isChecked()) {
                    query = query + " AND ame_elevator=1";
                    Log.i(TAG, "onOptionsItemSelected: query formed yet is" + query);
                }
                if (checkBox8.isChecked()) {
                    query = query + " AND ame_toilet_attached=0x01";
                    Log.i(TAG, "onOptionsItemSelected: query formed yet is" + query);
                }
                if (checkBox9.isChecked()) {
                    query = query + " AND CCTV=1";
                    Log.i(TAG, "onOptionsItemSelected: query formed yet is" + query);
                }
                if (checkBox10.isChecked()) {
                    query = query + " AND ownership=1";
                    Log.i(TAG, "onOptionsItemSelected: query formed yet is" + query);
                }
                query = query.replace(" ","%20");
                String locality = getIntent().getStringExtra("locality");
                query=query+locality;

                if (pricehtl.isChecked()) {
                    if (checkBox4.isChecked() && ac.isChecked()) {
                        query = query + " ORDER BY rent_single_ac DESC";
                    }
                    if (checkBox4.isChecked() && nonac.isChecked()) {
                        query = query + " ORDER BY rent_single_nonac DESC";
                    }
                    if (checkBox3.isChecked() && ac.isChecked()) {
                        query = " ORDER BY rent_double_ac DESC";
                    }
                    if (checkBox3.isChecked() && nonac.isChecked()) {
                        query = "ORDER BY rent_double_nonac DESC";
                    }

                }
                if (pricelth.isChecked()) {
                    if (checkBox4.isChecked() && ac.isChecked()) {
                        query = query + " ORDER BY rent_single_ac ASC";
                    }
                    if (checkBox4.isChecked() && nonac.isChecked()) {
                        query = query + " ORDER BY rent_single_nonac ASC";
                    }
                    if (checkBox3.isChecked() && ac.isChecked()) {
                        query = query + " ORDER BY rent_double_ac ASC";
                    }
                    if (checkBox3.isChecked() && nonac.isChecked()) {
                        query = query + " ORDER BY rent_double_nonac ASC";
                    }
                }
                if (dimhtl.isChecked()) {
                    if (checkBox3.isChecked()) {
                        query = query + " ORDER BY (dim_single_length*dim_single_width) DESC";
                    }
                    if (checkBox4.isChecked()) {
                        query = query + " ORDER BY (dim_double_length*dim_double_width) DESC";
                    }
                }
                if (dimlth.isChecked()) {
                    if (checkBox3.isChecked()) {
                        query = query + " ORDER BY (dim_single_length*dim_single_width) ASC";
                    }
                    if (checkBox4.isChecked()) {
                        query = query + " ORDER BY (dim_double_length*dim_double_width) ASC";
                    }
                }
                query = query.replace(" ","%20");
                locality="";
                Log.i(TAG, "onOptionsItemSelected:just before StringRequest the query is" + query);
                Intent intent = new Intent(FilterActivity.this, MainActivity.class);
                intent.putExtra("locality",locality);
                intent.putExtra("dbqry",query);
                intent.putExtra("roomType",roomType);
                Log.i(TAG, "onOptionsItemSelected: Data sent to MainActivity is locality and query"+locality +query);
                startActivity(intent);
                query = null;
                Log.i(TAG, "onOptionsItemSelected:query after null " + query);


        }
        return true;

    }


}

