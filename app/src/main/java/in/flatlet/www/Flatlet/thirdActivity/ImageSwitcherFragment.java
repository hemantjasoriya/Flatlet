package in.flatlet.www.Flatlet.thirdActivity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import in.flatlet.www.Flatlet.R;


public class ImageSwitcherFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.imageview_third,container,false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ViewPager viewPager = (ViewPager)getActivity().findViewById(R.id.viewPager1);
        ImageSwitcherAdapter adapter = new ImageSwitcherAdapter(getContext());
        viewPager.setAdapter(adapter);
    }
}
