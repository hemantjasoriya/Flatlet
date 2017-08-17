package in.flatlet.www.Flatlet.recyclerView;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceManager;
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

import java.util.List;

import in.flatlet.www.Flatlet.R;
import in.flatlet.www.Flatlet.secondActivity.Activity2;



public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    Context context;
    List<GetDataAdapter> dataModelArrayList;
    SQLiteDatabase db_favourite;
    FeedReaderDbHelper feedReaderDbHelper ;
    Cursor cursor;
    private final String TAG = "RecyclerViewAdapter";


    RecyclerViewAdapter(List<GetDataAdapter> getDataAdapter, Context context) {
        super();
        this.dataModelArrayList = getDataAdapter;
        this.context = context;
        feedReaderDbHelper = new FeedReaderDbHelper(context);
        db_favourite=feedReaderDbHelper.getWritableDatabase();

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
        Log.i(TAG, "onBindViewHolder: invoked" +position);
        final GetDataAdapter getDataAdapter1 = dataModelArrayList.get(position);
        holder.hostel_title.setText(getDataAdapter1.getName());
        holder.hostel_rent.setText(getDataAdapter1.getRent());
        holder.hostel_address.setText(getDataAdapter1.getAddress());
        Picasso.with(context).load("http://images.flatlet.in/images_thumbs/" + (position + 1) + "/1.jpg").into(holder.imageView2);
        /*Log.i(TAG, "onBindViewHolder: hostel name is "+getDataAdapter1.getName());*/

        String selection= FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE+" = ?";
        String[] projection={FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE};
        String [] selectionArgs={getDataAdapter1.getName()};
        cursor=db_favourite.query(FeedReaderContract.FeedEntry.TABLE_NAME,projection,selection,selectionArgs,null,null,null);

        Log.i(TAG, "onBindViewHolder: before if and get count is "+cursor.getCount());
        String[] projection1 = {
                FeedReaderContract.FeedEntry._ID,
                FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE,

        };

        Cursor cursor1 = db_favourite.query(FeedReaderContract.FeedEntry.TABLE_NAME, projection1, null, null, null, null, null);


        if (cursor1.getCount()!=0 && cursor.getCount()!=0){
            cursor.moveToNext();
            Log.i(TAG, "onBindViewHolder: cursor.isnull returned true "+cursor.getString(0));

        if (cursor.getString(0).equalsIgnoreCase(getDataAdapter1.getName())){
            holder.toggle.setBackgroundResource(R.drawable.ic_favorite_red_24dp);
        }
        else
        {
            holder.toggle.setBackgroundResource(R.drawable.ic_favorite_white_24dp);
        }}
        else {
            holder.toggle.setBackgroundResource(R.drawable.ic_favorite_white_24dp);
        }

        
        
        holder.toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    holder.toggle.setBackgroundResource(R.drawable.ic_favorite_red_24dp);

                    ContentValues values=new ContentValues();
                    values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE,getDataAdapter1.getName());
                    values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_SECONDARY_ADDRESS,getDataAdapter1.getAddress());
                    values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_RENT,getDataAdapter1.getRent());
                    values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_RATING,1);
                    values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_IMG_URL,"http://images.flatlet.in/images_thumbs/" + (position + 1) + "/1.jpg");
                   db_favourite.insert(FeedReaderContract.FeedEntry.TABLE_NAME,null,values);

                    String[] projection1 = {
                            FeedReaderContract.FeedEntry._ID,
                            FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE,

                    };

                    cursor = db_favourite.query(FeedReaderContract.FeedEntry.TABLE_NAME, projection1, null, null, null, null, null);
                    int i = cursor.getCount();
                    Log.i(TAG, "onCreate: No of entries now is" + i);

                    Toast.makeText(context,"Added to favourites",Toast.LENGTH_SHORT).show();
                }
                else {
                    holder.toggle.setBackgroundResource(R.drawable.ic_favorite_white_24dp);
                    db_favourite.delete(FeedReaderContract.FeedEntry.TABLE_NAME, FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE+" = ?",new String[]{getDataAdapter1.getName()});

                }
            }
        });
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "onClicked on" + position +getDataAdapter1.getName());
                Intent intent = new Intent(context, Activity2.class);
                intent.putExtra("hostel_title",getDataAdapter1.getName());
                Log.i(TAG, "onClick: data sent to activity2 is "+getDataAdapter1.getName());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        Log.i(TAG, "getItemCount: ");

        return dataModelArrayList.size();
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
