package in.flatlet.www.Flatlet.Home.fragments.homefragment;

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

import in.flatlet.www.Flatlet.R;
import in.flatlet.www.Flatlet.Utility.MySingleton;
import in.flatlet.www.Flatlet.recyclerView.MainActivity;


public class CoachingLocalityFragment extends Fragment {
    private String dbqry;
    private String roomType;
    private String gender;
    private String locality, subLocality;
    private RadioButton checkBoxGirls;
    private RadioButton checkBoxBoys;
    private RadioButton checkBoxSingleRoom;
    private RadioButton checkBoxDoubleRoom;
    private RadioButton checkBoxAc;
    private RadioButton checkBoxNonAc;
    private Spinner spinner1, spinner2;
    private BottomNavigationView navigation;
    private final String TAG = "CoachingLocalityFt";
    String arg;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.coaching_locality_fragment, container, false);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.i("TAG", "onActivityCreated: " + arg);
        Button searchButton = getActivity().findViewById(R.id.searchButton);
        navigation = getActivity().findViewById(R.id.navigation);
        checkBoxBoys = getActivity().findViewById(R.id.checkBoxBoys);
        checkBoxGirls = getActivity().findViewById(R.id.checkBoxGirls);
        checkBoxSingleRoom = getActivity().findViewById(R.id.checkBoxSingleRoom);
        checkBoxDoubleRoom = getActivity().findViewById(R.id.checkBoxDoubleRoom);
        checkBoxAc = getActivity().findViewById(R.id.checkBoxAc);
        checkBoxNonAc = getActivity().findViewById(R.id.checkBoxNonAc);
        spinner1 = getActivity().findViewById(R.id.spinner1);
        spinner2 = getActivity().findViewById(R.id.spinner2);

        SharedPreferences sharedPreferences = getContext().getSharedPreferences("personalInfo", Context.MODE_PRIVATE);

        if (sharedPreferences.getString("userGender", "male").equalsIgnoreCase("male")) {

            checkBoxBoys.setChecked(true);
        }

//Checking which coaching round button was clicked and accordingly which spinner to fill with relevant information
        arg = getArguments().getString("arg");

        assert arg != null;
        switch (arg) {
            case "aakash": {
                spinner1Selected();
                spinner1.setSelection(0);
                spinner2Selected(R.array.aakashArray);
                break;
            }
            case "ables": {
                spinner1Selected();
                spinner1.setSelection(1);
                spinner2Selected(R.array.ablesArray);
                break;
            }
            case "allen": {
                spinner1Selected();
                spinner1.setSelection(2);
                spinner2Selected(R.array.allenArray);
                break;
            }
            case "bansal": {
                spinner1Selected();
                spinner1.setSelection(3);
                spinner2Selected(R.array.bansalArray);
                break;
            }
            case "btrix": {
                spinner1Selected();
                spinner1.setSelection(4);
                spinner2Selected(R.array.btrixArray);
                break;
            }
            case "careerpoint": {
                spinner1Selected();
                spinner1.setSelection(5);
                spinner2Selected(R.array.cpArray);
                break;
            }
            case "etoos": {
                spinner1Selected();
                spinner1.setSelection(6);
                spinner2Selected(R.array.etoosArray);
                break;

            }
            case "motion": {
                spinner1Selected();
                spinner1.setSelection(7);
                spinner2Selected(R.array.motionArray);
                break;
            }
            case "nucleus": {
                spinner1Selected();
                spinner1.setSelection(8);
                spinner2Selected(R.array.nucleusArray);
                break;
            }

            case "photon": {
                spinner1Selected();
                spinner1.setSelection(9);
                spinner2Selected(R.array.photonArray);
                break;
            }
            case "resonance": {
                spinner1Selected();
                spinner1.setSelection(10);
                spinner2Selected(R.array.resonanceArray);
                break;
            }
            case "vibrant": {
                spinner1Selected();
                spinner1.setSelection(11);
                spinner2Selected(R.array.vibrantArray);
                break;
            }

        }

        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
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
                    dbqry = dbqry + " ORDER BY `" + subLocality + "` ASC";

                    dbqry = dbqry.replace(" ", "%20");
                   /* Log.i("TAG", "onClick: dbqry is"+dbqry);*/
                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    intent.putExtra("dbqry", dbqry);
                    subLocality = subLocality.replace(" ", "%20");
                    intent.putExtra("locality", subLocality);
                    intent.putExtra("roomType", roomType);
                    intent.putExtra("gender", gender);
                    Log.i(TAG, "onClick: " + gender);

                    intent.setFlags(15);
                    startActivity(intent);
                    dbqry = null;
                } else {
                    Toast.makeText(getContext(), "No Internet Connection!! Please Try Again", Toast.LENGTH_LONG).show();
                }

            }
        });

    }

    private void spinner2Selected(int betaArrayName) {
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(getActivity(), betaArrayName,
                android.R.layout.simple_spinner_dropdown_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(adapter2);
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                subLocality = parent.getItemAtPosition(position).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    private void spinner1Selected() {

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.coachingArray,
                android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adapter);
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                locality = parent.getItemAtPosition(position).toString();

                switch (locality) {
                    case "Aakash Institute": {
                        spinner2Selected(R.array.aakashArray);
                        break;
                    }
                    case "Ables Education": {
                        spinner2Selected(R.array.ablesArray);
                        break;
                    }
                    case "Allen Career Institute": {
                        spinner2Selected(R.array.allenArray);
                        break;
                    }
                    case "Bansal Classes": {
                        spinner2Selected(R.array.bansalArray);
                        break;
                    }
                    case "Btrix Medical Classes": {
                        spinner2Selected(R.array.btrixArray);
                        break;
                    }
                    case "Career Point": {
                        spinner2Selected(R.array.cpArray);
                        break;
                    }
                    case "Etoos Education": {
                        spinner2Selected(R.array.etoosArray);
                    }
                    case "Motion": {
                        spinner2Selected(R.array.motionArray);
                        break;
                    }
                    case "Nucleus Education": {
                        spinner2Selected(R.array.nucleusArray);
                        break;
                    }
                    case "Photon Academy": {
                        spinner2Selected(R.array.photonArray);

                    }
                    case "Resonance": {
                        spinner2Selected(R.array.resonanceArray);
                        break;
                    }
                    case "Vibrant Academy": {
                        spinner2Selected(R.array.vibrantArray);
                        break;
                    }


                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }
}
