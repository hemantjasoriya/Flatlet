package in.flatlet.www.Flatlet.Home.fragments.favouriteFragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import in.flatlet.www.Flatlet.R;


public class FavouriteFragment extends Fragment {
    ArrayList<String> list = new ArrayList<>();
    SharedPreferences sharedPreferences;

    Context context;
    @Nullable
    @Override



    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        sharedPreferences = context.getSharedPreferences("mypref",context.MODE_PRIVATE);
        String hostelname = sharedPreferences.getString("hostelname",null);
        if (!hostelname.isEmpty()){
            for (int i=0;i>0;i++){
                list.add(i,hostelname);
            }

        }

        return inflater.inflate(R.layout.favourites_fragment,container,false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        FavouriteListRecyclerAdapter adapter = new FavouriteListRecyclerAdapter(context,list);


    }
}
