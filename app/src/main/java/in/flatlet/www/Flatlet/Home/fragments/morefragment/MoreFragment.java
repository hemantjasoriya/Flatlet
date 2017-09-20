package in.flatlet.www.Flatlet.Home.fragments.morefragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import in.flatlet.www.Flatlet.R;


public class MoreFragment extends Fragment implements AdapterView.OnItemClickListener {

    private final String s = null;
    private final String[] moreFragmentItems = {"How it works", "Rate us on PlayStore", "Privacy Policy", "invite", "feedback", "About us"};
    private final int[] moreFragmentVectors = {R.drawable.ic_lightbulb_outline_black_24dp, R.drawable.ic_star_black_24dp,
            R.drawable.ic_security,
            R.drawable.ic_insert_invitation_black_24dp,
            R.drawable.ic_feedback_black_24dp, R.drawable.ic_group};

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.more_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ListView moreFragmentList = (ListView) getActivity().findViewById(R.id.moreFragmentList);
        moreFragmentList.setOnItemClickListener(this);
        MoreFragmentArrayAdapter adapter = new MoreFragmentArrayAdapter(getActivity(), moreFragmentItems, moreFragmentVectors);
        moreFragmentList.setAdapter(adapter);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        switch (position) {
            case 0:
                Fragment fragment = new HowItWorksFagment();
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.replace(R.id.content, fragment, "how it works");

                fragmentTransaction.commit();
                break;
            case 1:
                Toast.makeText(getActivity().getApplicationContext(), "you clicked on " + s + position, Toast.LENGTH_SHORT).show();
                break;

            case 2:
                fragment = new PrivacyPolicyFragment();
                fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.replace(R.id.content, fragment, "privacy policy");

                fragmentTransaction.commit();
                break;
            case 3:
                Toast.makeText(getActivity().getApplicationContext(), "you clicked on " + s + position, Toast.LENGTH_SHORT).show();
                break;
            case 4:
                fragment = new FeedBackFragment();
                fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.replace(R.id.content, fragment, "feedback");

                fragmentTransaction.commit();
                break;
            case 5:
                fragment = new AboutUsFragment();
                fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.replace(R.id.content, fragment, "about us");

                fragmentTransaction.commit();
                break;


        }
    }
}
