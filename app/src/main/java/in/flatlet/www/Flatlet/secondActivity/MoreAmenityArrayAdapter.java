package in.flatlet.www.Flatlet.secondActivity;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import in.flatlet.www.Flatlet.R;


public class MoreAmenityArrayAdapter extends ArrayAdapter {
    public Context context;
    private ArrayList<String> ameTitle;
    private ArrayList<Integer> ameVector;

    public MoreAmenityArrayAdapter(Context context, ArrayList<String> ameTitle, ArrayList<Integer> ameVector) {
        super(context, R.layout.listitems,ameTitle);
        this.context=context;
        this.ameTitle=ameTitle;
        this.ameVector=ameVector;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView==null){
            LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView=inflater.inflate(R.layout.listitems,parent,false);
        }
        TextView textView=(TextView)convertView.findViewById(R.id.list_text);
        textView.setText(ameTitle.get(position));
        ImageView imageView=(ImageView)convertView.findViewById(R.id.list_vector);
        imageView.setImageResource(ameVector.get(position));
        return convertView;
    }
}
