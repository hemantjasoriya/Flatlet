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
    CheckBox checkBoxElevator, checkBoxEveningSnacks, checkBoxToiletAttached, checkBoxCCTV, checkBoxOwner;
    RadioButton checkBoxAc, checkBoxNonAc, checkBoxBoys, checkBoxGirls, checkBoxSingleRoom, checkBoxDoubleRoom;
    RadioButton pricehtl, pricelth, dimhtl, dimlth;
    SeekBar seekBar;


    protected void onCreate(Bundle savedInstanceState) {
        Log.i("bjb", "onCreate: ");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.filter_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Log.i("bjb", "onCreate: after toolbar ");
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        adapter = new FilterAdapter(getSupportFragmentManager());
        Log.i("bjb", "onCreate: after adapter");
        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(adapter);
        String query = null;
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        Log.i("bjb", "onCreate:last ");


    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        String query = null;
        checkBoxBoys = (RadioButton) findViewById(R.id.checkBoxBoys);
        checkBoxGirls = (RadioButton) findViewById(R.id.checkBoxGirls);
        checkBoxSingleRoom = (RadioButton) findViewById(R.id.checkBoxSingleRoom);
        checkBoxDoubleRoom = (RadioButton) findViewById(R.id.checkBoxDoubleRoom);
        checkBoxNonAc = (RadioButton) findViewById(R.id.checkBoxNonAc);
        checkBoxAc = (RadioButton) findViewById(R.id.checkBoxAc);
        checkBoxElevator = (CheckBox) findViewById(R.id.checkBoxElevator);
        checkBoxEveningSnacks = (CheckBox) findViewById(R.id.checkBoxEveningSnacks);
        checkBoxToiletAttached = (CheckBox) findViewById(R.id.checkBoxToiletAttached);
        checkBoxCCTV = (CheckBox) findViewById(R.id.checkBoxCCTV);
        checkBoxOwner = (CheckBox) findViewById(R.id.checkBoxOwner);
        pricehtl = (RadioButton) findViewById(R.id.pricehtl);
        pricelth = (RadioButton) findViewById(R.id.pricelth);
        dimhtl = (RadioButton) findViewById(R.id.dimhtl);
        dimlth = (RadioButton) findViewById(R.id.dimlth);
        checkBoxAc = (RadioButton) findViewById(R.id.checkBoxAc);
        checkBoxNonAc = (RadioButton) findViewById(R.id.checkBoxNonAc);
        seekBar = (SeekBar) findViewById(R.id.seekBar);

        switch (item.getItemId()) {

            case R.id.action_reset:

                checkBoxBoys.setChecked(true);
                checkBoxGirls.setChecked(false);
                checkBoxSingleRoom.setChecked(true);
                checkBoxDoubleRoom.setChecked(false);
                checkBoxAc.setChecked(false);
                checkBoxNonAc.setChecked(false);
                checkBoxCCTV.setChecked(false);
                checkBoxElevator.setChecked(false);
                checkBoxOwner.setChecked(false);
                checkBoxEveningSnacks.setChecked(false);
                checkBoxToiletAttached.setChecked(false);


                pricehtl.setChecked(false);
                pricelth.setChecked(false);
                dimlth.setChecked(false);
                dimhtl.setChecked(false);
                seekBar.setProgress(20000);
                break;

            case R.id.action_apply:
                String roomType = null;

                if (checkBoxSingleRoom.isChecked() && checkBoxAc.isChecked()) {
                    query = "select * from `hostel_specs` where rent_single_ac>0 AND rent_single_ac<" + seekBar.getProgress();
                    roomType = "rent_single_ac";
                }
                if (checkBoxSingleRoom.isChecked() && checkBoxNonAc.isChecked()) {
                    query = "select * from `hostel_specs` where rent_single_nonac>0 AND rent_single_nonac<" + seekBar.getProgress();
                    roomType = "rent_single_nonac";
                }
                if (checkBoxDoubleRoom.isChecked() && checkBoxAc.isChecked()) {
                    query = "select * from `hostel_specs` where rent_double_ac>0 AND rent_double_ac<" + seekBar.getProgress();
                    roomType = "rent_double_ac";
                }
                if (checkBoxDoubleRoom.isChecked() && checkBoxNonAc.isChecked()) {
                    query = "select * from `hostel_specs` where rent_double_nonac>0 AND rent_double_nonac<" + seekBar.getProgress();
                    roomType = "rent_double_nonac";
                }

                if (checkBoxBoys.isChecked()) {
                    query = query + " AND gender='boys' ";
                    Log.i(TAG, "onOptionsItemSelected: query formed yet is" + query);
                }
                if (checkBoxGirls.isChecked()) {
                    query = query + " AND gender='girls' ";
                    Log.i(TAG, "onOptionsItemSelected: string formed yet is" + query);
                }
                if (checkBoxEveningSnacks.isChecked()) {
                    query = query + " AND ame_eve_snacks=1";
                    Log.i(TAG, "onOptionsItemSelected: query formed yet is" + query);
                }
                if (checkBoxElevator.isChecked()) {
                    query = query + " AND ame_elevator=1";
                    Log.i(TAG, "onOptionsItemSelected: query formed yet is" + query);
                }
                if (checkBoxToiletAttached.isChecked()) {
                    query = query + " AND ame_toilet_attached=1";
                    Log.i(TAG, "onOptionsItemSelected: query formed yet is" + query);
                }
                if (checkBoxCCTV.isChecked()) {
                    query = query + " AND CCTV=1";
                    Log.i(TAG, "onOptionsItemSelected: query formed yet is" + query);
                }
                if (checkBoxOwner.isChecked()) {
                    query = query + " AND ownership=1";
                    Log.i(TAG, "onOptionsItemSelected: query formed yet is" + query);
                }
                query = query.replace(" ", "%20");
                String locality = getIntent().getStringExtra("locality");
                query = query + locality;

                if (pricehtl.isChecked()) {
                    if (checkBoxSingleRoom.isChecked() && checkBoxAc.isChecked()) {
                        query = query + " ORDER BY rent_single_ac DESC";
                    }
                    if (checkBoxSingleRoom.isChecked() && checkBoxNonAc.isChecked()) {
                        query = query + " ORDER BY rent_single_nonac DESC";
                    }
                    if (checkBoxDoubleRoom.isChecked() && checkBoxAc.isChecked()) {
                        query = " ORDER BY rent_double_ac DESC";
                    }
                    if (checkBoxDoubleRoom.isChecked() && checkBoxNonAc.isChecked()) {
                        query = "ORDER BY rent_double_nonac DESC";
                    }

                }
                if (pricelth.isChecked()) {
                    if (checkBoxSingleRoom.isChecked() && checkBoxAc.isChecked()) {
                        query = query + " ORDER BY rent_single_ac ASC";
                    }
                    if (checkBoxSingleRoom.isChecked() && checkBoxNonAc.isChecked()) {
                        query = query + " ORDER BY rent_single_nonac ASC";
                    }
                    if (checkBoxDoubleRoom.isChecked() && checkBoxAc.isChecked()) {
                        query = query + " ORDER BY rent_double_ac ASC";
                    }
                    if (checkBoxDoubleRoom.isChecked() && checkBoxNonAc.isChecked()) {
                        query = query + " ORDER BY rent_double_nonac ASC";
                    }
                }
                if (dimhtl.isChecked()) {
                    if (checkBoxDoubleRoom.isChecked()) {
                        query = query + " ORDER BY (dim_single_length*dim_single_width) DESC";
                    }
                    if (checkBoxSingleRoom.isChecked()) {
                        query = query + " ORDER BY (dim_double_length*dim_double_width) DESC";
                    }
                }
                if (dimlth.isChecked()) {
                    if (checkBoxDoubleRoom.isChecked()) {
                        query = query + " ORDER BY (dim_single_length*dim_single_width) ASC";
                    }
                    if (checkBoxSingleRoom.isChecked()) {
                        query = query + " ORDER BY (dim_double_length*dim_double_width) ASC";
                    }
                }
                query = query.replace(" ", "%20");
                locality = "";
                Log.i(TAG, "onOptionsItemSelected:just before StringRequest the query is" + query);
                Intent intent = new Intent(FilterActivity.this, MainActivity.class);
                intent.putExtra("locality", locality);
                intent.putExtra("dbqry", query);
                intent.putExtra("roomType", roomType);
                Log.i(TAG, "onOptionsItemSelected: Data sent to MainActivity is locality and query" + locality + query);
                startActivity(intent);
                query = null;
                Log.i(TAG, "onOptionsItemSelected:query after null " + query);


        }
        return true;

    }


}