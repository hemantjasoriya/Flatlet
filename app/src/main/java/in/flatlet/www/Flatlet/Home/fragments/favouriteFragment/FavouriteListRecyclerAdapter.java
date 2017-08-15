package in.flatlet.www.Flatlet.Home.fragments.favouriteFragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import in.flatlet.www.Flatlet.R;
import in.flatlet.www.Flatlet.recyclerView.GetDataAdapter;
import in.flatlet.www.Flatlet.secondActivity.Activity2;

public class FavouriteListRecyclerAdapter extends RecyclerView.Adapter<FavouriteListRecyclerAdapter.ViewHolder>{
    Context context;
    SharedPreferences sharedPreferences;


    private final String TAG = "RecyclerViewAdapter";


    FavouriteListRecyclerAdapter(Context getDataAdapter, ArrayList<String> context) {
        super();


    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.i(TAG, "onCreateViewHolder:invoked ");
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_cards, parent, false);
        return new FavouriteListRecyclerAdapter.ViewHolder(v);
    }
    @Override
    public void onBindViewHolder(final FavouriteListRecyclerAdapter.ViewHolder holder, final int position) {
        Log.i(TAG, "onBindViewHolder: invoked" +position);

        Picasso.with(context).load("http://images.flatlet.in/images_thumbs/" + (position + 1) + "/1.jpg").into(holder.imageView2);
        sharedPreferences=context.getSharedPreferences("mypref",Context.MODE_PRIVATE);

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });
    }

    @Override
    public int getItemCount() {
        Log.i(TAG, "getItemCount: ");

        return 2;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView hostel_title;
        TextView hostel_rent;
        ImageView imageView2;
        TextView hostel_address;
        CardView cardView;
        ToggleButton toggle;

        ViewHolder(View itemView) {
            super(itemView);
            Log.i(TAG, "ViewHolder: constructor invoked and xml inflated to java object");
            hostel_title = (TextView) itemView.findViewById(R.id.hostel_title);
            hostel_rent = (TextView) itemView.findViewById(R.id.hostel_rent);
            hostel_address = (TextView) itemView.findViewById(R.id.hostel_address);
            imageView2 = (ImageView) itemView.findViewById(R.id.imageView2);
            cardView = (CardView) itemView.findViewById(R.id.cardview1);
            toggle=(ToggleButton)itemView.findViewById(R.id.toggleButton);
        }
    }
    }