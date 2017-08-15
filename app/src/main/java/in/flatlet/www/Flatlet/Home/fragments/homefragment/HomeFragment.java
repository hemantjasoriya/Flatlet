package in.flatlet.www.Flatlet.Home.fragments.homefragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import in.flatlet.www.Flatlet.Home.LocalityListFragment;
import in.flatlet.www.Flatlet.R;


public class HomeFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.home_fragment,container,false);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        EditText searchEditText = (EditText)getActivity().findViewById(R.id.search_edit_text);
        searchEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LocalityListFragment localityListFragment = new LocalityListFragment();
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.content,localityListFragment,"locality search");
                fragmentTransaction.commit();
                /*Intent intent = new Intent(getActivity(), LocalityList.class);
                startActivity(intent);*/
            }
        });

    }
}
