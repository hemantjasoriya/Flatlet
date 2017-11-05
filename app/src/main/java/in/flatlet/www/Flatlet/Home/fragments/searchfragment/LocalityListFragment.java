package in.flatlet.www.Flatlet.Home.fragments.searchfragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import in.flatlet.www.Flatlet.Home.fragments.homefragment.HomeFragment;
import in.flatlet.www.Flatlet.R;
import in.flatlet.www.Flatlet.Utility.MySingleton;
import in.flatlet.www.Flatlet.recyclerView.MainActivity;


public class LocalityListFragment extends Fragment {
    private String dbqry;
    private String roomType;
    private String gender;
    private String locality;
    private RadioButton checkBoxGirls;
    private RadioButton checkBoxBoys;
    private RadioButton checkBoxSingleRoom;
    private RadioButton checkBoxDoubleRoom;
    private RadioButton checkBoxAc;
    private RadioButton checkBoxNonAc;
    private BottomNavigationView navigation;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.localitylist_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Button searchButton = (Button) getActivity().findViewById(R.id.searchButton);
        navigation = (BottomNavigationView) getActivity().findViewById(R.id.navigation);

        checkBoxBoys = (RadioButton) getActivity().findViewById(R.id.checkBoxBoys);
        checkBoxGirls = (RadioButton) getActivity().findViewById(R.id.checkBoxGirls);
        checkBoxSingleRoom = (RadioButton) getActivity().findViewById(R.id.checkBoxSingleRoom);
        checkBoxDoubleRoom = (RadioButton) getActivity().findViewById(R.id.checkBoxDoubleRoom);
        checkBoxAc = (RadioButton) getActivity().findViewById(R.id.checkBoxAc);
        checkBoxNonAc = (RadioButton) getActivity().findViewById(R.id.checkBoxNonAc);
        Spinner spinner = (Spinner) getActivity().findViewById(R.id.spinner);
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("personalInfo", Context.MODE_PRIVATE);

        if (sharedPreferences.getString("userGender", "male").equalsIgnoreCase("male")) {
            checkBoxBoys.setChecked(true);
        }
        Log.i("TAG", "onActivityCreated: " + sharedPreferences.getString("userGender", "male"));
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.localityArray,
                android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                locality = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeButtonEnabled(true);


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HomeFragment homeFragment = new HomeFragment();
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.content, homeFragment, null);
                fragmentTransaction.commit();
                navigation.setSelectedItemId(R.id.navigation_home);
            }
        });

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MySingleton.getInstance(getContext()).isOnline()) {
                    dbqry = "Select%20*%20from%20`hostel_specs`%20where%20";
                    if (checkBoxBoys.isChecked()) {
                        dbqry = dbqry + "gender=" + "'boys' ";
                        gender = "boys";
                    }
                    if (checkBoxGirls.isChecked()) {
                        dbqry = dbqry + "gender=" + "'girls' ";
                        gender = "girls";
                    }
                    if (checkBoxAc.isChecked() && checkBoxSingleRoom.isChecked()) {
                        dbqry = dbqry + "AND rent_single_ac>0";
                        roomType = "rent_single_ac";
                    }
                    if (checkBoxAc.isChecked() && checkBoxDoubleRoom.isChecked()) {
                        dbqry = dbqry + "AND rent_double_ac>0";
                        roomType = "rent_double_ac";
                    }
                    if (checkBoxNonAc.isChecked() && checkBoxSingleRoom.isChecked()) {
                        dbqry = dbqry + "AND rent_single_nonac>0";
                        roomType = "rent_single_nonac";
                    }
                    if (checkBoxNonAc.isChecked() && checkBoxDoubleRoom.isChecked()) {
                        dbqry = dbqry + "AND rent_double_nonac>0";
                        roomType = "rent_double_nonac";
                    }
                    /*dbqry = dbqry+" ORDER BY RAND()";*/

                    dbqry = dbqry.replace(" ", "%20");
                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    intent.putExtra("dbqry", dbqry);
                    locality = locality.replace(" ", "%20");
                    intent.putExtra("locality", "%20AND%20address_secondary=" + "'" + locality + "'");
                    intent.putExtra("roomType", roomType);
                    intent.putExtra("gender", gender);
                    startActivity(intent);
                    dbqry = null;
                } else {
                    Toast.makeText(getContext(), "No Internet Connection!! Please Try Again", Toast.LENGTH_LONG).show();
                }

            }
        });


    }
}
