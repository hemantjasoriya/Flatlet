package in.flatlet.www.Flatlet.Home.fragments.morefragment.HowItWorksFragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import in.flatlet.www.Flatlet.Home.fragments.morefragment.MoreFragment;
import in.flatlet.www.Flatlet.R;


public class HowItWorksFagment extends Fragment {
    private List<String> listDataHeader1;
    private HashMap<String, List<String>> listHash;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.howitworks_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeButtonEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MoreFragment moreFragment = new MoreFragment();
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.content, moreFragment);
                fragmentTransaction.commit();
            }
        });
/*
        Picasso.with(getContext()).load("http://images.flatlet.in/tutorial/1.webp").into(i1);
        Picasso.with(getContext()).load("http://images.flatlet.in/tutorial/2.webp").into(i2);
        Picasso.with(getContext()).load("http://images.flatlet.in/tutorial/3.webp").into(i3);
        Picasso.with(getContext()).load("http://images.flatlet.in/tutorial/4.webp").into(i4);
        Picasso.with(getContext()).load("http://images.flatlet.in/tutorial/5.webp").into(i5);
        Picasso.with(getContext()).load("http://images.flatlet.in/tutorial/6.webp").into(i6);
        Picasso.with(getContext()).load("http://images.flatlet.in/tutorial/7.webp").into(i7);
        Picasso.with(getContext()).load("http://images.flatlet.in/tutorial/8.webp").into(i8);
        Picasso.with(getContext()).load("http://images.flatlet.in/tutorial/9.webp").into(i9);
        Picasso.with(getContext()).load("http://images.flatlet.in/tutorial/10.webp").into(i10);*/

        ExpandableListView listView = (ExpandableListView) getActivity().findViewById(R.id.lvExp);
        initData();
        ExpandableHowItWorksListAdapter listAdapter = new ExpandableHowItWorksListAdapter(getContext(), listDataHeader1, listHash);
        listView.setAdapter(listAdapter);

    }

    private void initData() {
        listDataHeader1 = new ArrayList<>();
        listHash = new HashMap<>();

        listDataHeader1.add("STEP1: Click on the 'Enter locality here'");
        listDataHeader1.add("STEP2: Select the locality, gender and type of room (single/double) select the Air conditioning and press Search Button");
        listDataHeader1.add("STEP3: Click on the Filter button placed in right bottom corner.");
        listDataHeader1.add("STEP4:Select the essentials you are looking for ");
        listDataHeader1.add("STEP5:Sort the results according to Price or Area of the room.");
        listDataHeader1.add("STEP6:Click on the hostel card.");
        listDataHeader1.add("STEP7: Click on the image header to see full HQ images and 360 Virtual Tour");
        listDataHeader1.add("STEP8:Swipe right to see images from the stack");
        listDataHeader1.add("STEP9:Click on the 360° tab and Enjoy VR Tour");


        List<String> questionOne = new ArrayList<>();
        questionOne.add("http://images.flatlet.in/tutorial/1.webp");

        List<String> questionTwo = new ArrayList<>();
        questionTwo.add("http://images.flatlet.in/tutorial/2.webp");
        questionTwo.add("http://images.flatlet.in/tutorial/3.webp");

        List<String> questionThree = new ArrayList<>();
        questionThree.add("http://images.flatlet.in/tutorial/4.webp");

        List<String> questionFour = new ArrayList<>();
        questionFour.add("http://images.flatlet.in/tutorial/5.webp");


        List<String> questionFive = new ArrayList<>();
        questionFive.add("http://images.flatlet.in/tutorial/6.webp");

        List<String> questionSix = new ArrayList<>();
        questionSix.add("http://images.flatlet.in/tutorial/7.webp");

        List<String> questionSeven = new ArrayList<>();
        questionSeven.add("http://images.flatlet.in/tutorial/8.webp");

        List<String> questionEight = new ArrayList<>();
        questionEight.add("http://images.flatlet.in/tutorial/9.webp");

        List<String> questionNine = new ArrayList<>();
        questionNine.add("http://images.flatlet.in/tutorial/10.webp");


        listHash.put(listDataHeader1.get(0), questionOne);
        listHash.put(listDataHeader1.get(1), questionTwo);
        listHash.put(listDataHeader1.get(2), questionThree);
        listHash.put(listDataHeader1.get(3), questionFour);
        listHash.put(listDataHeader1.get(4), questionFive);
        listHash.put(listDataHeader1.get(5), questionSix);
        listHash.put(listDataHeader1.get(6), questionSeven);
        listHash.put(listDataHeader1.get(7), questionEight);
        listHash.put(listDataHeader1.get(8), questionNine);

    }

}
