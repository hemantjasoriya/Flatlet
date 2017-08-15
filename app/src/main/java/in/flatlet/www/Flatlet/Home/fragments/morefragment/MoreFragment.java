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
    ListView moreFragmentList;

    private Fragment fragment;
    private FragmentTransaction fragmentTransaction;
    String s = null;
    private String[] moreFragmentItems = {"How it works", "Rate us on PlayStore", "Privacy Policy",
            "Contact us", "invite", "feedback", "About us"};
    private int[] moreFragmentVectors = {R.drawable.ic_lightbulb_outline_black_24dp, R.drawable.ic_star_black_24dp,
            R.drawable.ic_security, R.drawable.ic_contact_mail_black_24dp,
            R.drawable.ic_insert_invitation_black_24dp,
            R.drawable.ic_feedback_black_24dp, R.drawable.ic_group_black_24dp};

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.more_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        moreFragmentList = (ListView) getActivity().findViewById(R.id.moreFragmentList);
        moreFragmentList.setOnItemClickListener(this);
        MoreFragmentArrayAdapter adapter = new MoreFragmentArrayAdapter(getActivity(), moreFragmentItems, moreFragmentVectors);
        moreFragmentList.setAdapter(adapter);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
         /*s= moreFragmentList.getItemAtPosition(position).toString();*/
        /*Toast.makeText(getActivity().getApplicationContext(),"you clicked on "+s +position,Toast.LENGTH_SHORT).show();*/
        switch (position) {
            case 0:
                fragment = new HowItWorksFagment();
                fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.content, fragment, "how it works");
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                break;
            case 1:
                Toast.makeText(getActivity().getApplicationContext(), "you clicked on " + s + position, Toast.LENGTH_SHORT).show();
                break;

            case 2:
                fragment = new PrivacyPolicyFragment();
                fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.content, fragment, "privacy policy");
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                break;

            case 3:
                fragment = new ContactUsFragment();
                fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.content, fragment, "contact us");
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                break;
            case 4:
                Toast.makeText(getActivity().getApplicationContext(), "you clicked on " + s + position, Toast.LENGTH_SHORT).show();
                break;
            case 5:
                fragment = new FeedBackFragment();
                fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.content, fragment, "feedback");
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                break;
            case 6:
                fragment = new AboutUsFragment();
                fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.content, fragment, "about us");
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                break;


        }
    }
}
