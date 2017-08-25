package in.flatlet.www.Flatlet.thirdActivity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import in.flatlet.www.Flatlet.R;


public class ImageSwitcherFragment extends Fragment {
    private final String TAG = "ImageSwitcherFragment";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        StringRequest request = new StringRequest(Request.Method.GET,
                "http://flatlet.in/sum/count_dir/GetCount.jsp", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i(TAG, "onResponse:Raw response is" + response);
                try {
                    response = response.replaceAll("[\n]", "");
                    response = response.trim();
                    int result = Integer.parseInt(response);
                    Log.i(TAG, "onResponse: string converted into integer" + result);
                    /*tv.setText(result+"");*/
                } catch (NumberFormatException ex) {
                    Log.i(TAG, "onResponse: " + ex);
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i(TAG, "onErrorResponse: error in volley response");
                    }
                });
        RequestQueue requestqueue = Volley.newRequestQueue(getContext());
        requestqueue.add(request);

        return inflater.inflate(R.layout.imageview_third, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ViewPager viewPager = (ViewPager) getActivity().findViewById(R.id.viewPager1);
        ImageSwitcherAdapter adapter = new ImageSwitcherAdapter(getContext());
        viewPager.setAdapter(adapter);
    }
}
