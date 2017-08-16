package in.flatlet.www.Flatlet.Home.fragments.favouriteFragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;

import java.util.ArrayList;

import in.flatlet.www.Flatlet.R;


public class FavouriteFragment extends Fragment {
    ArrayList<String> list = new ArrayList<>();
    RequestQueue requestQueue;
    JsonArrayRequest jsonArrayRequest;
    Context context;
    @Nullable
    @Override




    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.favourites_fragment,container,false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, "", null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(jsonArrayRequest);


        FavouriteListRecyclerAdapter adapter = new FavouriteListRecyclerAdapter(context,list);


    }
}
