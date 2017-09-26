package in.flatlet.www.Flatlet.thirdActivity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import in.flatlet.www.Flatlet.R;


public class ImageSwitcherFragment extends Fragment {
   /* private String TAG = "ImageSwitcherFragment";*/


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.imageview_third, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
       /* Log.i(TAG, "onActivityCreated: called");

        ConnectivityManager cm =
                (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo info = cm.getActiveNetworkInfo();
       *//* boolean isConnected = info != null && info.isConnectedOrConnecting();
        boolean isWiFi = info.getType() == ConnectivityManager.TYPE_WIFI;*//*
        if (info.getType() == ConnectivityManager.TYPE_WIFI) {
            // do something
            Log.i(TAG, "onActivityCreated: internet type WIFI");
        } else if (info.getType() == ConnectivityManager.TYPE_MOBILE) {
            // check NetworkInfo subtype
            Log.i(TAG, "onActivityCreated: internet type MOBILE");
            if (info.getType() == TelephonyManager.NETWORK_TYPE_GPRS) {
                // Bandwidth between 100 kbps and below
                Log.i(TAG, "onActivityCreated: GPRS");
            } else if (info.getSubtype() == TelephonyManager.NETWORK_TYPE_EDGE) {
                // Bandwidth between 50-100 kbps
                Log.i(TAG, "onActivityCreated: EDGE");
            } else if (info.getType() == TelephonyManager.NETWORK_TYPE_EVDO_0) {
                // Bandwidth between 400-1000 kbps
                Log.i(TAG, "onActivityCreated: FAST");
            } else if (info.getType() == TelephonyManager.NETWORK_TYPE_EVDO_A) {
                // Bandwidth between 600-1400 kbps
                Log.i(TAG, "onActivityCreated: superfast");
            } else if (info.getType() == TelephonyManager.NETWORK_TYPE_1xRTT) {
                Log.i(TAG, "onActivityCreated: RTT");
            } else if (info.getType() == TelephonyManager.NETWORK_TYPE_GSM) {
                Log.i(TAG, "onActivityCreated: CDMA");
            } else if (info.getType() == TelephonyManager.NETWORK_TYPE_HSDPA) {
                Log.i(TAG, "onActivityCreated: HSDPA");
            } else if (info.getType() == TelephonyManager.NETWORK_TYPE_HSPA) {
                Log.i(TAG, "onActivityCreated: HSPA");
            } else if (info.getType() == TelephonyManager.NETWORK_TYPE_HSUPA) {
                Log.i(TAG, "onActivityCreated: HSUPA");
            } else if (info.getType() == TelephonyManager.NETWORK_TYPE_UMTS) {
                Log.i(TAG, "onActivityCreated:UMTS");
            } else if (info.getType() == TelephonyManager.NETWORK_TYPE_UNKNOWN) {
                Log.i(TAG, "onActivityCreated: UNKNOWN");
            } else if (info.getType() == TelephonyManager.NETWORK_TYPE_LTE) {
                Log.i(TAG, "onActivityCreated: LTE");
            } else if (info.getType() == TelephonyManager.NETWORK_TYPE_EHRPD) {
                Log.i(TAG, "onActivityCreated: EHRPD");
            } else if (info.getType() == TelephonyManager.NETWORK_TYPE_EVDO_B) {
                Log.i(TAG, "onActivityCreated: EVDO_B");
            } else if (info.getType() == TelephonyManager.NETWORK_TYPE_HSPAP) {
                Log.i(TAG, "onActivityCreated: HSPAP");
            } else if (info.getType() == TelephonyManager.NETWORK_TYPE_EHRPD) {
                Log.i(TAG, "onActivityCreated: EHRPD");
            } else {
                Log.i(TAG, "onActivityCreated: Kuch nhi pata chal rha");
            }
        }*/
        ProgressBar progressBar = (ProgressBar) getActivity().findViewById(R.id.progresBar);
        String title = getActivity().getIntent().getStringExtra("hostel_title").replace(" ", "%20");
        int arraySize = getActivity().getIntent().getIntExtra("imageCount", 3);
        ViewPager viewPager = (ViewPager) getActivity().findViewById(R.id.viewPager1);
        ImageSwitcherAdapter adapter = new ImageSwitcherAdapter(getContext(), arraySize, title);
        viewPager.setAdapter(adapter);
        progressBar.setVisibility(View.INVISIBLE);

    }
}
