package in.flatlet.www.Flatlet.filter;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
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
import in.flatlet.www.Flatlet.Utility.MySingleton;
import in.flatlet.www.Flatlet.recyclerView.MainActivity;


public class FilterActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";


    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.filter_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FilterAdapter adapter = new FilterAdapter(getSupportFragmentManager());

        ViewPager mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(adapter);
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
        RadioButton checkBoxBoys = (RadioButton) findViewById(R.id.checkBoxBoys);
        RadioButton checkBoxGirls = (RadioButton) findViewById(R.id.checkBoxGirls);
        RadioButton checkBoxSingleRoom = (RadioButton) findViewById(R.id.checkBoxSingleRoom);
        RadioButton checkBoxDoubleRoom = (RadioButton) findViewById(R.id.checkBoxDoubleRoom);
        RadioButton checkBoxNonAc = (RadioButton) findViewById(R.id.checkBoxNonAc);
        RadioButton checkBoxAc = (RadioButton) findViewById(R.id.checkBoxAc);
        CheckBox checkBoxElevator = (CheckBox) findViewById(R.id.checkBoxElevator);
        CheckBox checkBoxEveningSnacks = (CheckBox) findViewById(R.id.checkBoxEveningSnacks);
        CheckBox checkBoxToiletAttached = (CheckBox) findViewById(R.id.checkBoxToiletAttached);
        CheckBox checkBoxCCTV = (CheckBox) findViewById(R.id.checkBoxCCTV);
        CheckBox checkBoxOwner = (CheckBox) findViewById(R.id.checkBoxOwner);
        RadioButton pricehtl = (RadioButton) findViewById(R.id.pricehtl);
        RadioButton pricelth = (RadioButton) findViewById(R.id.pricelth);
        RadioButton dimhtl = (RadioButton) findViewById(R.id.dimhtl);
        RadioButton dimlth = (RadioButton) findViewById(R.id.dimlth);
        checkBoxAc = (RadioButton) findViewById(R.id.checkBoxAc);
        checkBoxNonAc = (RadioButton) findViewById(R.id.checkBoxNonAc);
        SeekBar seekBar = (SeekBar) findViewById(R.id.seekBar);

        switch (item.getItemId()) {

            case R.id.action_reset:

                checkBoxBoys.setChecked(true);
                checkBoxGirls.setChecked(false);
                checkBoxSingleRoom.setChecked(true);
                checkBoxDoubleRoom.setChecked(false);
                checkBoxNonAc.setChecked(false);
                checkBoxAc.setChecked(true);
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

                if (MySingleton.getInstance(getApplicationContext()).isOnline()){
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
                    Intent intent = new Intent(FilterActivity.this, MainActivity.class);
                    intent.setFlags(13);
                    intent.putExtra("locality", locality);
                    intent.putExtra("dbqry", query);
                    intent.putExtra("roomType", roomType);
                    Log.i(TAG, "onOptionsItemSelected: Data sent to MainActivity is locality and query" + locality + query);
                    startActivity(intent);
                    query = null;
                    Log.i(TAG, "onOptionsItemSelected:query after null " + query);
                }
        }
        return true;

    }


}