package in.flatlet.www.Flatlet.recyclerView;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.facebook.accountkit.AccessToken;
import com.facebook.accountkit.AccountKit;
import com.squareup.picasso.Picasso;

import java.util.List;

import in.flatlet.www.Flatlet.Home.FirstActivity;
import in.flatlet.www.Flatlet.R;
import in.flatlet.www.Flatlet.Utility.MySingleton;
import in.flatlet.www.Flatlet.secondActivity.Activity2;


public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private final String MyRequestTag = "MyTag";
    private final Context context;
    private final List<GetDataAdapter> dataModelArrayList;
    private final SQLiteDatabase db_favourite;
    private Cursor cursor;
    private RequestQueue queue1;
    private final String TAG = "RecyclerViewAdapter";
    private final RecyclerView recyclerView;
    private int i = 0;


    RecyclerViewAdapter(List<GetDataAdapter> getDataAdapter, Context context, RecyclerView recyclerView) {
        super();
        this.dataModelArrayList = getDataAdapter;
        this.context = context;
        this.recyclerView = recyclerView;
        FeedReaderDbHelper feedReaderDbHelper = new FeedReaderDbHelper(context);
        db_favourite = feedReaderDbHelper.getWritableDatabase();

        SharedPreferences pref_default = PreferenceManager.getDefaultSharedPreferences(context);
        if (!pref_default.getBoolean("thirdTime", false)) {
            // <---- run your one time code here
            Log.i(TAG, "onCreate: before oncreate");


            feedReaderDbHelper.onCreateOriginal(db_favourite);
            Log.i(TAG, "onCreate: called");
            SharedPreferences.Editor editor = pref_default.edit();
            editor.putBoolean("thirdTime", true);
            editor.apply();
        }

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.i(TAG, "onCreateViewHolder:invoked ");

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_cards, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        Log.i(TAG, "onBindViewHolder: invoked" + position);
        final GetDataAdapter getDataAdapter1 = dataModelArrayList.get(position);
        holder.card_rating.setText(String.valueOf(getDataAdapter1.getCardRating()));
        holder.hostel_title.setText(getDataAdapter1.getName());
        holder.hostel_rent.setText(getDataAdapter1.getRent());
        holder.hostel_address.setText(getDataAdapter1.getAddress());
        Log.i(TAG, "onBindViewHolder: " + getDataAdapter1.getCardRating());



        Picasso.with(context).load("http://images.flatlet.in/images_thumbs/" + (position + 1) + "/1.jpg").into(holder.imageView2);
        /*Log.i(TAG, "onBindViewHolder: hostel name is "+getDataAdapter1.getName());*/

        String selection = FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE + " = ?";
        String[] projection = {FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE};
        String[] selectionArgs = {getDataAdapter1.getName()};
        cursor = db_favourite.query(FeedReaderContract.FeedEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, null);

        Log.i(TAG, "onBindViewHolder: before if and get count is " + cursor.getCount());


        cursor.moveToNext();
        if (cursor.getCount() != 0 && cursor.getString(0).equalsIgnoreCase(getDataAdapter1.getName())) {

            Log.i(TAG, "onBindViewHolder: cursor.isnull returned true " + cursor.getString(0));

            holder.toggle.setBackgroundResource(R.drawable.ic_favorite_red_24dp);
            i++;
            holder.toggle.setChecked(true);

        } else {
            holder.toggle.setBackgroundResource(R.drawable.ic_favorite_white_24dp);
            holder.toggle.setChecked(false);
        }


        holder.toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                AccessToken accessToken = AccountKit.getCurrentAccessToken();
                SharedPreferences sharedPreferences = context.getSharedPreferences("personalInfo", Context.MODE_PRIVATE);
                String dbqry = null;
                if (accessToken == null) {
                    holder.toggle.setChecked(false);
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                    alertDialog.setTitle("Login alert");
                    alertDialog.setIcon(R.drawable.poipo);
                    alertDialog.setMessage("You first have to login to make favourites");
                    MyListener my = new MyListener();
                    alertDialog.setPositiveButton("LogIn", my);
                    alertDialog.setNegativeButton("Cancel", my);
                    alertDialog.show();
                    return;
                }
                if (i > 0) {
                    Log.i(TAG, "onCheckedChanged: value of i " + i);
                    i = 0;
                    return;
                }
                if (isChecked) {
                    Log.i(TAG, "onCheckedChanged: is checked start");
                    holder.toggle.setBackgroundResource(R.drawable.ic_favorite_red_24dp);
                    ContentValues values = new ContentValues();
                    values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE, getDataAdapter1.getName());
                    values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_SECONDARY_ADDRESS, getDataAdapter1.getAddress());
                    values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_RENT, getDataAdapter1.getRent());
                    values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_RATING, getDataAdapter1.getCardRating());
                    values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_IMG_URL, "http://images.flatlet.in/images_thumbs/" + (position + 1) + "/1.jpg");
                    db_favourite.insert(FeedReaderContract.FeedEntry.TABLE_NAME, null, values);
                    dbqry = "INSERT INTO `user_favourites`( `title`, `user_mobile`, `secondary_address`, `rent`, `img_url`, `rating`) VALUES ('" + getDataAdapter1.getName() + "'" +
                            ",'" + sharedPreferences.getString("userMobile", "911") + "','" + getDataAdapter1.getAddress() + "','" + getDataAdapter1.getRent() + "'," +
                            "'http://images.flatlet.in/images_thumbs/" + (position + 1) + "/1.jpg','"+getDataAdapter1.getCardRating()+"')";

                    // checking the size of sqlite database
                    String[] projection1 = {
                            FeedReaderContract.FeedEntry._ID,
                            FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE,
                    };

                    cursor = db_favourite.query(FeedReaderContract.FeedEntry.TABLE_NAME, projection1, null, null, null, null, null);
                    int i = cursor.getCount();
                    Log.i(TAG, "onCreate: No of entries now is" + i);

                    Toast.makeText(context, "Added to favourites", Toast.LENGTH_SHORT).show();
                } else {
                    Log.i(TAG, "onCheckedChanged: is checked false chala");
                    LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                    int i = layoutManager.findFirstVisibleItemPosition();
                    int j = layoutManager.findLastVisibleItemPosition();
                    if (position >= i && position <= j) {
                        holder.toggle.setBackgroundResource(R.drawable.ic_favorite_white_24dp);
                        db_favourite.delete(FeedReaderContract.FeedEntry.TABLE_NAME, FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE + " = ?", new String[]{getDataAdapter1.getName()});
                        dbqry = "DELETE FROM `user_favourites` WHERE `title`='" + getDataAdapter1.getName() + "'  AND" +
                                " `user_mobile`='" + sharedPreferences.getString("userMobile", "911") + "'";

                    }
                }
                String url = "http://flatlet.in/flatletuserinsert/flatletuserinsert.jsp?dbqry="+dbqry;
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


                stringRequest.setTag(MyRequestTag);
                MySingleton.getInstance(context).addToRequestQueue(stringRequest);
                Log.i(TAG, "onCheckedChanged: after volley request");
            }
        });
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "onClicked on" + position + getDataAdapter1.getName());
                Intent intent = new Intent(context, Activity2.class);
                intent.putExtra("hostel_title", getDataAdapter1.getName());
                intent.putExtra("hostel_rent",getDataAdapter1.getRent());
                Log.i(TAG, "onClick: data sent to activity2 is " + getDataAdapter1.getName());
                context.startActivity(intent);
            }
        });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            holder.cardView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
                @Override
                public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {

                }
            });
        }
    }

    @Override
    public int getItemCount() {
        Log.i(TAG, "getItemCount: ");

        return dataModelArrayList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        final TextView hostel_rent;
        final TextView card_rating;
        final TextView hostel_title;
        final TextView hostel_address;
        final ImageView imageView2;
        final CardView cardView;
        final ToggleButton toggle;

        ViewHolder(View itemView) {
            super(itemView);
            Log.i(TAG, "ViewHolder: constructor invoked and xml inflated to java object");
            card_rating =(TextView)itemView.findViewById(R.id.card_rating);
            Log.i(TAG, "ViewHolder: hostel rating textview is inflated now");
            hostel_title = (TextView) itemView.findViewById(R.id.hostel_title);
            hostel_rent = (TextView) itemView.findViewById(R.id.hostel_rent);
            hostel_address = (TextView) itemView.findViewById(R.id.hostel_address);
            imageView2 = (ImageView) itemView.findViewById(R.id.imageView2);
            cardView = (CardView) itemView.findViewById(R.id.cardview1);
            toggle = (ToggleButton) itemView.findViewById(R.id.toggleButton);
        }
    }

    private class MyListener implements DialogInterface.OnClickListener {

        @Override
        public void onClick(DialogInterface dialog, int which) {
            if (which == -1) {
                Intent intent = new Intent(context, FirstActivity.class);
                intent.setFlags(2);
                Log.i(TAG, "onClick: ");
                context.startActivity(intent);

            } else
                Toast.makeText(context, "Negative button clicked", Toast.LENGTH_LONG).show();


        }
    }

}
