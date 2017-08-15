package in.flatlet.www.Flatlet.recyclerView;

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

import java.util.List;
import in.flatlet.www.Flatlet.R;
import in.flatlet.www.Flatlet.secondActivity.Activity2;



public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    Context context;
   /*private ToggleButton toggle;*/
   SharedPreferences sharedPreferences;
    List<GetDataAdapter> dataModelArrayList;
   /* SharedPreferences sharedPreferences= context.getSharedPreferences("mypref",Context.MODE_PRIVATE);*/

    private final String TAG = "RecyclerViewAdapter";

    RecyclerViewAdapter(List<GetDataAdapter> getDataAdapter, Context context) {
        super();
        this.dataModelArrayList = getDataAdapter;
        this.context = context;

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
       /* holder.hostel_address.setText(position+"");*/
        Picasso.with(context).load("http://images.flatlet.in/images_thumbs/" + (position + 1) + "/1.jpg").into(holder.imageView2);
        sharedPreferences=context.getSharedPreferences("mypref",Context.MODE_PRIVATE);
        if (getDataAdapter1.getName().equalsIgnoreCase(sharedPreferences.getString("hostelname",null))){
            holder.toggle.setBackgroundResource(R.drawable.ic_favorite_red_24dp);
        }
        else
        {
            holder.toggle.setBackgroundResource(R.drawable.ic_favorite_white_24dp);
        }

       holder.toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    holder.toggle.setBackgroundResource(R.drawable.ic_favorite_red_24dp);
                    sharedPreferences= context.getSharedPreferences("mypref",Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor =  sharedPreferences.edit();
                    editor.putString("hostelname",getDataAdapter1.getName());
                    editor.apply();
                    Log.i(TAG, "onCheckedChanged: Added to sharedpref"+getDataAdapter1.getName());
                    Toast.makeText(context,"Added to favourites",Toast.LENGTH_SHORT).show();
                }
                else {
                    Log.i(TAG, "onCheckedChanged: else chal giyo");
                    holder.toggle.setBackgroundResource(R.drawable.ic_favorite_white_24dp);
                    sharedPreferences= context.getSharedPreferences("mypref",Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor =  sharedPreferences.edit();
                    editor.remove("hostelname");
                    editor.apply();
                    Log.i(TAG, "onCheckedChanged: removed from sharedpref"+getDataAdapter1.getName());

                    Toast.makeText(context,"Removed From favourites",Toast.LENGTH_SHORT).show();
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
