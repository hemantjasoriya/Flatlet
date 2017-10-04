package in.flatlet.www.Flatlet.Home.fragments.morefragment.FAQFragment;

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


public class FAQFragment extends Fragment {
    private ExpandableListView listView;
    private ExpandableFAQListAdapter listAdapter;
    private List<String> listDataHeader;
    private HashMap<String, List<String>> listHash;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.faq_fragment, container, false);
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

        listView = (ExpandableListView) getActivity().findViewById(R.id.lvExp);
        initData();
        listAdapter = new ExpandableFAQListAdapter(getActivity(), listDataHeader, listHash);
        listView.setAdapter(listAdapter);

    }

    private void initData() {
        listDataHeader = new ArrayList<>();
        listHash = new HashMap<>();

        listDataHeader.add("Q1. Aren't the Hostel and P.G.same thing?");
        listDataHeader.add("Q2. What if I want to have Double Room all that for myself? How much would that cost me?");
        listDataHeader.add("Q3. Are all the rooms in a hostel identical(same)? If not why there is only one virtual tour?");
        listDataHeader.add("Q4. Why are the images are stretched out at the ends? ");
        listDataHeader.add("Q5. The virtual tour won't open on my phone?");
        listDataHeader.add("Q6. There is some error in the information given. How do I report it?");
        listDataHeader.add("Q7. I have some questions that could come in handy for other people, How about adding them to this list?");


        List<String> questionOne = new ArrayList<>();
        questionOne.add("Answer: NO THEY ARE NOT THE SAME. In Kota's vocabulary hostel is more or less professionally managed where hostel wardens, owners cater to all your needs in a responsive manner.Hostels provide food to the students in the same building, Students need not go out or opt for tiffin.Most/All Hostels listed here have a separate hall with dining tables for food. Every hostel listed here has Breakfast, Lunch, dinner, Laundry, Room cleaning,Water purifier, bedding, pillows, bed, chair,study table are default*. Where PG refers to a place which is relatively smaller than hostel like (6-7 people in a building)and you have to subscribe for every service yourself like laundry, breakfast,lunch-dinner");

        List<String> questionTwo = new ArrayList<>();
        questionTwo.add("Answer:  The prices displayed for Double Room are for two people but if you want to live by yourself, you are gonna have to negotiate with the owner.");

        List<String> questionThree = new ArrayList<>();
        questionThree.add("Answer: No!, Not all the rooms are same they all have some variations like some have balcony some don't.Some rooms have a different layout than others.");
        questionThree.add("For the sake of simplicity, we only added only one virtual tour so that you can get the idea about the hostel building and furniture.");


        List<String> questionFour = new ArrayList<>();
        questionFour.add("Answer: These images are captured with very lower focal length(effective) so that we can cover more area in a single picture.");
        questionFour.add("The subject in images might look bigger than it actually is so We'd recommend you to see the dimensions of the room before jumping to any conclusions.");

        List<String> questionFive = new ArrayList<>();
        questionFive.add("Answer: In order to see the virtual tour you have to update the Google's \"Android System WebView\" from the play store. Just search for the 'Android System WebView' on play store and update/install it. ");

        List<String> questionSix = new ArrayList<>();
        questionSix.add("Answer: In the description screen of every particular hostel there are three vertical dots on top right corner of the screen click on that and tell us in details what happened wrong. We promise you we'd try to correct it as fast as possible.");

        List<String> questionSeven = new ArrayList<>();
        questionSeven.add("Answer: Drop us an email at flatletindia@gmail.com.We would be more than happy to add those questions to this list. :)");


        listHash.put(listDataHeader.get(0), questionOne);
        listHash.put(listDataHeader.get(1), questionTwo);
        listHash.put(listDataHeader.get(2), questionThree);
        listHash.put(listDataHeader.get(3), questionFour);
        listHash.put(listDataHeader.get(4), questionFive);
        listHash.put(listDataHeader.get(5), questionSix);
        listHash.put(listDataHeader.get(6), questionSeven);
    }

}
