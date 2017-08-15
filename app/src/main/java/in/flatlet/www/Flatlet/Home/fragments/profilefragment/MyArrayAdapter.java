package in.flatlet.www.Flatlet.Home.fragments.profilefragment;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import in.flatlet.www.Flatlet.R;


class MyArrayAdapter extends ArrayAdapter<String> {
    public Context context;
    private String[] loginArray;
    private int[] imgid;
    public MyArrayAdapter(@NonNull Context context,String[] loginArray , int[] imgid) {
        super(context,R.layout.listitems,loginArray);
        this.context=context;
        this.loginArray=loginArray;
        this.imgid=imgid;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView==null){
            LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView=inflater.inflate(R.layout.listitems,parent,false);
        }
        TextView textView=(TextView)convertView.findViewById(R.id.list_text);
        textView.setText(loginArray[position]);
        ImageView imageView=(ImageView)convertView.findViewById(R.id.list_vector);
        imageView.setImageResource(imgid[position]);
        return convertView;
    }
}
