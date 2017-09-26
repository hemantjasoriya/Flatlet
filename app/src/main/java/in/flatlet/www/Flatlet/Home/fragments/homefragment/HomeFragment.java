package in.flatlet.www.Flatlet.Home.fragments.homefragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

import in.flatlet.www.Flatlet.Home.fragments.searchfragment.LocalityListFragment;
import in.flatlet.www.Flatlet.R;
import in.flatlet.www.Flatlet.Utility.MySingleton;
import in.flatlet.www.Flatlet.recyclerView.MainActivity;
import in.flatlet.www.Flatlet.reviewhostel.ReviewHostel;
import in.flatlet.www.Flatlet.thirdActivity.MainActivity_third;


public class HomeFragment extends Fragment {
    BottomNavigationView navigation;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.home_fragment, container, false);



    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ArrayList<String> virtualTourName = new ArrayList<>();
        virtualTourName.add(0, "Royal%20Cottage%208");
        virtualTourName.add(1, "Shourya%20Residency");
        virtualTourName.add(2, "");
        navigation = (BottomNavigationView) getActivity().findViewById(R.id.navigation);
        EditText searchEditText = (EditText) getActivity().findViewById(R.id.search_edit_text);
        CardView cardExploreGirls = (CardView) getActivity().findViewById(R.id.cardExploreGirls);
        CardView cardExploreBoys = (CardView) getActivity().findViewById(R.id.cardExploreBoys);
        CardView cardRateHostel = (CardView) getActivity().findViewById(R.id.cardRateHostel);
        CardView cardExploreVirtualTour = (CardView) getActivity().findViewById(R.id.cardExploreVirtualTour);


        cardRateHostel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ReviewHostel.class);
                startActivity(intent);
            }
        });

        cardExploreBoys.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MySingleton.getInstance(getContext()).isOnline()) {
                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    intent.putExtra("locality", "");
                    intent.putExtra("dbqry", "Select%20*%20from%20`hostel_specs`%20where%20rent_single_ac>0");
                    intent.putExtra("roomType", "rent_single_ac");
                    intent.putExtra("gender", "boys");
                    intent.setFlags(14);
                    getActivity().startActivity(intent);
                } else {
                    Toast.makeText(getContext(), "No Internet Connection ! Please Try Again", Toast.LENGTH_SHORT).show();
                }

            }
        });
        cardExploreGirls.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MySingleton.getInstance(getContext()).isOnline()) {
                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    intent.putExtra("locality", "");
                    intent.putExtra("dbqry", "Select%20*%20from%20`hostel_specs`%20where%20rent_single_ac>0");
                    intent.putExtra("roomType", "rent_single_ac");
                    intent.putExtra("gender", "girls");
                    intent.setFlags(14);
                    getActivity().startActivity(intent);
                } else {
                    Toast.makeText(getContext(), "No Internet Connection ! Please Try Again", Toast.LENGTH_SHORT).show();
                }

            }
        });
        cardExploreVirtualTour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MainActivity_third.class);
                intent.putExtra("hostel_title", "Shourya%20Residency");
                intent.putExtra("imageCount", 1);
                startActivity(intent);
            }
        });

        searchEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LocalityListFragment localityListFragment = new LocalityListFragment();
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.content, localityListFragment, "locality search");
                fragmentTransaction.commit();
                navigation.setSelectedItemId(R.id.navigation_search);
            }
        });


    }
}
