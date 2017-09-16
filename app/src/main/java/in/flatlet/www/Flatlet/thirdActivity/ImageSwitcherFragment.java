package in.flatlet.www.Flatlet.thirdActivity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.RequestQueue;

import in.flatlet.www.Flatlet.R;


public class ImageSwitcherFragment extends Fragment {
    private final String TAG = "ImageSwitcherFragment";
    private RequestQueue requestqueue;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView: ImageSwitcher onCreateView chala");
        return inflater.inflate(R.layout.imageview_third, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        String title = getActivity().getIntent().getStringExtra("hostel_title").replace(" ", "%20");
        int arraySize = getActivity().getIntent().getIntExtra("imageCount", 3);
        ViewPager viewPager = (ViewPager) getActivity().findViewById(R.id.viewPager1);
        ImageSwitcherAdapter adapter = new ImageSwitcherAdapter(getContext(), arraySize, title);
        viewPager.setAdapter(adapter);
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (requestqueue!=null){
            requestqueue.cancelAll("MyRequestTag");
        }
    }
}
