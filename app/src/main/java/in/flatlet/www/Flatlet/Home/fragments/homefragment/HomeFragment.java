package in.flatlet.www.Flatlet.Home.fragments.homefragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import in.flatlet.www.Flatlet.Home.fragments.searchfragment.LocalityListFragment;
import in.flatlet.www.Flatlet.R;
import in.flatlet.www.Flatlet.Utility.MySingleton;
import in.flatlet.www.Flatlet.recyclerView.MainActivity;
import in.flatlet.www.Flatlet.reviewhostel.ReviewHostel;


public class HomeFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.home_fragment, container, false);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        EditText searchEditText = (EditText) getActivity().findViewById(R.id.search_edit_text);
        CardView reviewHostelCard = (CardView) getActivity().findViewById(R.id.cardViewReview);
        ImageView exploreNowButton = (ImageView) getActivity().findViewById(R.id.exploreNowButton);
        /*Picasso.with(getContext()).load("http://images.flatlet.in/images/explore2.jpg").error(R.drawable.explore2).into(exploreNowButton);*/
       /* ImageView imageRateButton = (ImageView)getActivity().findViewById(R.id.imageRateButton);
        Picasso.with(getContext()).load("http://images.flatlet.in/images_thumbs/ratehostel.jpg").into(imageRateButton);*/
        reviewHostelCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ReviewHostel.class);
                startActivity(intent);
            }
        });
        exploreNowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MySingleton.getInstance(getContext()).isOnline()){
                    Log.i("ExploreNow check", "onClick: "+MySingleton.getInstance(getContext()).isOnline());
                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    intent.putExtra("locality", "");
                   /* intent.putExtra("dbqry", "Select%20*%20from%20`hostel_specs`%20where%20rent_single_ac>0%20ORDER%20BY%20RAND()");*/
                   //testing
                    intent.putExtra("dbqry", "Select%20*%20from%20`hostel_specs`%20where%20rent_single_ac>0");
                    intent.putExtra("roomType", "rent_single_ac");
                    intent.putExtra("gender", "girls");
                    getActivity().startActivity(intent);
                }
                else {
                    Toast.makeText(getContext(),"No Internet Connection ! Please Try Again",Toast.LENGTH_SHORT).show();
                }

            }
        });

        searchEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LocalityListFragment localityListFragment = new LocalityListFragment();
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.content, localityListFragment, "locality search");
                fragmentTransaction.commit();
                /*Intent intent = new Intent(getActivity(), LocalityList.class);
                startActivity(intent);*/
            }
        });


    }
}
