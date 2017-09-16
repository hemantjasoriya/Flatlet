package in.flatlet.www.Flatlet.Home.fragments.favouritefragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import in.flatlet.www.Flatlet.R;
import in.flatlet.www.Flatlet.Utility.MySingleton;
import in.flatlet.www.Flatlet.recyclerView.FeedReaderContract;
import in.flatlet.www.Flatlet.secondActivity.Activity2;

public class FavouriteListRecyclerAdapter extends RecyclerView.Adapter<FavouriteListRecyclerAdapter.ViewHolder> {
    private final Context context;
    private final List<FavouriteHostelDataModel> favouriteHostelList;
    private final SQLiteDatabase db;
    private final String TAG = "RecyclerViewAdapter";
    private RequestQueue queue1;

    public FavouriteListRecyclerAdapter(Context context1, ArrayList<FavouriteHostelDataModel> favouriteHostelList, SQLiteDatabase db) {
        super();
        context = context1;
        this.favouriteHostelList = favouriteHostelList;
        this.db = db;
        Log.i(TAG, "FavouriteListRecyclerAdapter: Context and List containing Model class object received");


    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.i(TAG, "onCreateViewHolder:invoked ");
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.favourite_recycler_view_cards, parent, false);
        return new FavouriteListRecyclerAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final FavouriteListRecyclerAdapter.ViewHolder holder, final int position) {

        Log.i(TAG, "onBindViewHolder: invoked" + position);


        final FavouriteHostelDataModel favouriteHostelDataModel = favouriteHostelList.get(position);
        holder.hostel_title.setText(favouriteHostelDataModel.getTitle());

        holder.hostel_address.setText(favouriteHostelDataModel.getAddress_secondary());

        holder.hostel_rent.setText(favouriteHostelDataModel.getRent() + "");
        holder.favouriteCardRating.setText(favouriteHostelDataModel.getFavouriteCardRating() + "");

        Picasso.with(context).load(favouriteHostelDataModel.getUrl()).into(holder.imageView2);
        Log.i(TAG, "onBindViewHolder: image bhi set bro");
        holder.toggle.setBackgroundResource(R.drawable.ic_favorite_red_24dp);
        holder.toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                                     @Override
                                                     public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                                         if (!isChecked) {
                                                             SharedPreferences sharedPreferences = context.getSharedPreferences("personalInfo", Context.MODE_PRIVATE);
                                                             favouriteHostelList.remove(position);
                                                             notifyDataSetChanged();
                                                             notifyItemRemoved(position);
                                                             notifyItemRangeChanged(position, favouriteHostelList.size());
                                                             db.delete(FeedReaderContract.FeedEntry.TABLE_NAME, FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE + " = ?", new String[]{favouriteHostelDataModel.getTitle()});
                                                             String dbqry = "DELETE FROM `user_favourites` WHERE `title`='" + favouriteHostelDataModel.getTitle() + "'  AND" +
                                                                     " `user_mobile`='" + sharedPreferences.getString("userMobile", "911") + "'";


                                                             String url = "http://flatlet.in/flatletuserinsert/flatletuserinsert.jsp?dbqry=" + dbqry;
                                                             String urlFinal = url.replace(" ", "%20");


                                                             StringRequest stringRequest = new StringRequest(Request.Method.GET, urlFinal,
                                                                     new Response.Listener<String>() {
                                                                         @Override
                                                                         public void onResponse(String response) {
                                                                             // Display the first 500 characters of the response string.
                                                                             Log.i(TAG, "onResponse: " + response);
                                                                         }
                                                                     }, new Response.ErrorListener() {
                                                                 @Override
                                                                 public void onErrorResponse(VolleyError error) {
                                                                     Log.i(TAG, "onErrorResponse: " + error);
                                                                 }
                                                             });

                                                             queue1 = MySingleton.getInstance(context).getRequestQueue();
                                                             stringRequest.setTag("MyRequestTag");
                                                             queue1.add(stringRequest);
                                                             MySingleton.getInstance(context).addToRequestQueue(stringRequest);
                                                             db.delete(FeedReaderContract.FeedEntry.TABLE_NAME, FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE + " = ?", new String[]{favouriteHostelDataModel.getTitle()});
                                                         }
                                                     }


                                                 }

        );

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Activity2.class);
                intent.putExtra("hostel_title", favouriteHostelDataModel.getTitle());
                Log.i(TAG, "onClick: data sent to activity2 is " + favouriteHostelDataModel.getTitle());
                context.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        Log.i(TAG, "getItemCount: ");
        return favouriteHostelList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        final TextView hostel_title;
        final TextView hostel_rent;
        final TextView hostel_address;
        final ImageView imageView2;
        final CardView cardView;
        final ToggleButton toggle;
        final TextView favouriteCardRating;
        /*RelativeLayout RL_favourite;*/

        ViewHolder(View itemView) {
            super(itemView);
            Log.i(TAG, "ViewHolder: constructor invoked and xml inflated to java object");
            hostel_title = (TextView) itemView.findViewById(R.id.hostel_title);
            hostel_rent = (TextView) itemView.findViewById(R.id.hostel_rent);
            hostel_address = (TextView) itemView.findViewById(R.id.hostel_address);
            imageView2 = (ImageView) itemView.findViewById(R.id.imageView2);
            cardView = (CardView) itemView.findViewById(R.id.cardview1);
            toggle = (ToggleButton) itemView.findViewById(R.id.toggleButton);
            favouriteCardRating = (TextView) itemView.findViewById(R.id.favourite_card_rating);
           /* RL_favourite=(RelativeLayout)itemView.findViewById(R.id.RL_favourite);*/
        }
    }


}