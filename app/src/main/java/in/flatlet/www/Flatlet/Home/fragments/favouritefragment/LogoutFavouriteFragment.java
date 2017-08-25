package in.flatlet.www.Flatlet.Home.fragments.favouriteFragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import in.flatlet.www.Flatlet.R;
import in.flatlet.www.Flatlet.recyclerView.MainActivity;

/**
 * Created by javax on 23-Aug-17.
 */

public class LogoutFavouriteFragment extends Fragment {
    private final String TAG = "LogoutFavouriteFragment";
    private Button exploreButton;
    private Context context;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.logoutfavourite_fragment, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        context = getActivity();
        exploreButton = (Button) getActivity().findViewById(R.id.explore_button);
        exploreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "onClick: explore clicked");
                Intent intent = new Intent(context, MainActivity.class);
                intent.putExtra("locality", "");
                intent.putExtra("dbqry", "Select%20*%20from%20`hostel_specs`%20where%20rent_single_ac>0");
                intent.putExtra("roomType", "rent_single_ac");
               /* intent.putExtra("gender","girls");*/
                context.startActivity(intent);

            }
        });
    }
}
