package in.flatlet.www.Flatlet.filter;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import in.flatlet.www.Flatlet.R;

public class FilterFragment extends Fragment implements SeekBar.OnSeekBarChangeListener {
    SeekBar seekBar1;
    TextView tv1,tv2;


    final static String TAG="Fragment 2";
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.filter_fragment,container,false);
        seekBar1=(SeekBar)v.findViewById(R.id.seekBar);
        seekBar1.setOnSeekBarChangeListener(this);
        tv1=(TextView)v.findViewById(R.id.tv1);
        tv2=(TextView)v.findViewById(R.id.tv2);
        seekBar1.setMax(25000);
        seekBar1.setProgress(25000);
        return v;
    }
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        tv2.setText(String.valueOf(progress));
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        Log.i(TAG, "onStartTrackingTouch: ");
    }
    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        Log.i(TAG, "onStopTrackingTouch: ");
    }
}