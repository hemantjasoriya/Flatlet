package in.flatlet.www.Flatlet.recyclerView;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Locale;

import in.flatlet.www.Flatlet.R;
import in.flatlet.www.Flatlet.Utility.MySingleton;
import in.flatlet.www.Flatlet.secondActivity.Activity2;


class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private final String MyRequestTag = "MyTag";
    private final Context context;
    private final List<GetDataAdapter> dataModelArrayList;
    private final SQLiteDatabase db_favourite;
    private Cursor cursor;
    private RequestQueue queue1;
    private final RecyclerView recyclerView;
    private int i = 0;
    Handler mHandler = new Handler();
    /*private static final int AD_VIEW_TYPE =1;
    private static final int MENU_ITEM_VIEW_TYPE =0;*/


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

            feedReaderDbHelper.onCreateOriginal(db_favourite);

            SharedPreferences.Editor editor = pref_default.edit();
            editor.putBoolean("thirdTime", true);
            editor.apply();
        }
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_cards, parent, false);
        return new ViewHolder(v);

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {


        final GetDataAdapter getDataAdapter1 = dataModelArrayList.get(position);
        final String title = getDataAdapter1.getName().replace(" ", "%20");
        Picasso.with(context).load("http://images.flatlet.in/images/" + title + "/Thumb/1.webp").resize(200, 120).centerCrop().into(holder.imageView2);
        holder.card_rating.setText(String.format(Locale.US, "%.1f", getDataAdapter1.getCardRating()));
        holder.hostel_title.setText(getDataAdapter1.getName());
        holder.hostel_rent.setText(getDataAdapter1.getRent());
        holder.hostel_address.setText(getDataAdapter1.getAddress());
        holder.gender.setText(getDataAdapter1.getGender());
        holder.imageView2.setAlpha(1.0f);
        String selection = FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE + " = ?";
        String[] projection = {FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE};
        String[] selectionArgs = {getDataAdapter1.getName()};
        cursor = db_favourite.query(FeedReaderContract.FeedEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, null);
        cursor.moveToNext();
        if (cursor.getCount() != 0 && cursor.getString(0).equalsIgnoreCase(getDataAdapter1.getName())) {
            holder.toggle.setBackgroundResource(R.drawable.ic_favorite_red_24dp);
            i++;
            holder.toggle.setChecked(true);

        } else {
            holder.toggle.setBackgroundResource(R.drawable.ic_favorite_white_24dp);
            holder.toggle.setChecked(false);
        }

        holder.toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {


                SharedPreferences sharedPreferences = context.getSharedPreferences("personalInfo", Context.MODE_PRIVATE);
                String dbqry = null;


                if (i > 0) {

                    i = 0;
                    return;
                }
                if (isChecked) {
                    holder.toggle.setBackgroundResource(R.drawable.ic_favorite_red_24dp);

                    new Thread(new Runnable() {
                        @Override
                        public void run() {

                            ContentValues values = new ContentValues();
                            values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE, getDataAdapter1.getName());
                            values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_SECONDARY_ADDRESS, getDataAdapter1.getAddress());
                            values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_RENT, getDataAdapter1.getRent());
                            values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_RATING, getDataAdapter1.getCardRating());
                            values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_IMG_URL, "http://images.flatlet.in/images/"+title+"/Thumb/1.webp");
                            db_favourite.insert(FeedReaderContract.FeedEntry.TABLE_NAME, null, values);

                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(context, getDataAdapter1.getName() + " added to favourites", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }).start();

                    dbqry = "INSERT INTO `user_favourites`( `title`, `user_mobile`, `secondary_address`, `rent`, `img_url`, `rating`) VALUES ('" + getDataAdapter1.getName() + "'" +
                            ",'" + sharedPreferences.getString("userMobile", "911") + "','" + getDataAdapter1.getAddress() + "','" + getDataAdapter1.getRent() + "'," +
                            "'http://images.flatlet.in/images/" + title + "/Thumb/1.webp','" + getDataAdapter1.getCardRating() + "')";

                } else {

                    LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                    int i = layoutManager.findFirstVisibleItemPosition();
                    int j = layoutManager.findLastVisibleItemPosition();
                    if (position >= i && position <= j) {
                        holder.toggle.setBackgroundResource(R.drawable.ic_favorite_white_24dp);
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                db_favourite.delete(FeedReaderContract.FeedEntry.TABLE_NAME, FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE + " = ?", new String[]{getDataAdapter1.getName()});

                            }
                        }).start();
                        dbqry = "DELETE FROM `user_favourites` WHERE `title`='" + getDataAdapter1.getName() + "'  AND" +
                                " `user_mobile`='" + sharedPreferences.getString("userMobile", "911") + "'";
                    }
                }


                String url = "http://flatlet.in/webservices/flatletuserinsert.jsp?dbqry="+dbqry;
                String urlFinal = url.replace(" ", "%20");

                StringRequest stringRequest = new StringRequest(Request.Method.POST, urlFinal,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, "Some Error Occured !! Please Try again", Toast.LENGTH_SHORT).show();

                    }
                });

                stringRequest.setTag(MyRequestTag);
                MySingleton.getInstance(context).addToRequestQueue(stringRequest);

            }
        });
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, Activity2.class);
                intent.putExtra("hostel_title", getDataAdapter1.getName());
                intent.putExtra("hostel_rent",getDataAdapter1.getRent());

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
        final TextView gender;

        ViewHolder(View itemView) {
            super(itemView);

            card_rating =(TextView)itemView.findViewById(R.id.card_rating);

            hostel_title = (TextView) itemView.findViewById(R.id.hostel_title);
            hostel_rent = (TextView) itemView.findViewById(R.id.hostel_rent);
            hostel_address = (TextView) itemView.findViewById(R.id.hostel_address);
            imageView2 = (ImageView) itemView.findViewById(R.id.imageView2);
            cardView = (CardView) itemView.findViewById(R.id.cardview1);
            toggle = (ToggleButton) itemView.findViewById(R.id.toggleButton);
            gender=(TextView)itemView.findViewById(R.id.hostel_gender);
        }
    }


}
