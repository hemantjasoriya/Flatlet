package in.flatlet.www.Flatlet.Home;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.RadioButton;

import in.flatlet.www.Flatlet.Home.fragments.homefragment.HomeFragment;
import in.flatlet.www.Flatlet.R;
import in.flatlet.www.Flatlet.recyclerView.MainActivity;


public class LocalityListFragment extends Fragment {
    AutoCompleteTextView autoCompleteTextView;
   private final String [] localityArray = {"Mahaveer Nagar 1", "Rajeev Gandhi Nagar","New Rajeev Gandhi Nagar"};
    final static String TAG = "LocalityListFragment";
    Button searchButton;
    String dbqry;
    String roomType;
     public String locality;
    RadioButton checkBoxGirls,checkBoxBoys, checkBoxSingleRoom,checkBoxDoubleRoom,checkBoxAc,checkBoxNonAc;
    Toolbar toolbar;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.localitylist_fragment,container,false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        searchButton = (Button)getActivity().findViewById(R.id.searchButton);
        Log.i(TAG, "onActivityCreated: started");
        checkBoxBoys=(RadioButton)getActivity().findViewById(R.id.checkBoxBoys);
        checkBoxGirls=(RadioButton)getActivity().findViewById(R.id.checkBoxGirls);
        checkBoxSingleRoom=(RadioButton)getActivity().findViewById(R.id.checkBoxSingleRoom);
        checkBoxDoubleRoom=(RadioButton)getActivity().findViewById(R.id.checkBoxDoubleRoom);
        checkBoxAc=(RadioButton)getActivity().findViewById(R.id.checkBoxAc);
        checkBoxNonAc=(RadioButton)getActivity().findViewById(R.id.checkBoxNonAc);
        /*localityList=(ListView)getActivity().findViewById(R.id.locality_list);*/
        autoCompleteTextView=(AutoCompleteTextView)getActivity().findViewById(R.id.autoCompleteText);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getActivity(),R.layout.autocomplete_text,localityArray);
        autoCompleteTextView.setAdapter(arrayAdapter);
        toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setHomeButtonEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HomeFragment homeFragment = new HomeFragment();
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.content,homeFragment,null);
                fragmentTransaction.commit();
            }
        });

       /* autoCompleteTextView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });*/
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbqry= "Select%20*%20from%20`hostel_specs`%20where%20";
                if (checkBoxBoys.isChecked())
                    dbqry= dbqry +"gender="+"'boys' ";
                if(checkBoxGirls.isChecked())
                    dbqry=dbqry+"gender="+"'girls' ";
                if (checkBoxAc.isChecked()&&checkBoxSingleRoom.isChecked())
                    dbqry=dbqry+"AND rent_single_ac>0";
                roomType="rent_single_ac";
                if (checkBoxAc.isChecked()&&checkBoxDoubleRoom.isChecked())
                    dbqry=dbqry+"AND rent_double_ac>0";
                roomType="rent_double_ac";
                if (checkBoxNonAc.isChecked()&&checkBoxSingleRoom.isChecked())
                    dbqry=dbqry+"AND rent_single_nonac>0";
                roomType="rent_single_nonac";
                if (checkBoxNonAc.isChecked()&&checkBoxDoubleRoom.isChecked())
                    dbqry=dbqry+"AND rent_double_nonac>0";
                roomType="rent_double_nonac";

                dbqry=dbqry.replace(" ","%20");
                Intent intent = new Intent(getActivity(), MainActivity.class);
                intent.putExtra("dbqry",dbqry);
                //text fetched from autocomplete text view
                locality=autoCompleteTextView.getText().toString();
                locality= locality.replace(" ","%20");
                Log.i(TAG, "onActivityCreated: Text received from autocompletetextview is"+locality);
                intent.putExtra("locality","%20AND%20address_secondary="+"'"+locality+"'");
                intent.putExtra("roomType",roomType);
                startActivity(intent);
                dbqry= null;


            }
        });



    }
}
