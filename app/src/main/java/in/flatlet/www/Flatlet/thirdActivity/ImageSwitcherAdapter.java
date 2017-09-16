package in.flatlet.www.Flatlet.thirdActivity;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import in.flatlet.www.Flatlet.R;


class ImageSwitcherAdapter extends PagerAdapter {
    private final Context context;
    private int arraySize;
    private String title;
    private String TAG = "ImageSwitcherAdapter";
    private ArrayList<String> GalleryURL = new ArrayList<>();

    ImageSwitcherAdapter(Context context, int arraySize, String title) {
        this.context = context;
        this.arraySize = arraySize;
        this.title = title;
        Log.i(TAG, "ImageSwitcherAdapter: ImageswitcherAdapter called and received" + arraySize + title);
        arrayFormation();
    }

    private ArrayList arrayFormation() {
        for (int i = 1; i <= arraySize; i++) {

            Log.i(TAG, "arrayFormation: Hi");
            GalleryURL.add(i - 1, "http://images.flatlet.in/images/" + title + "/" + i + ".webp");

        }
        Log.i(TAG, "arrayFormation: GalleyURl array is" + GalleryURL.toString());
        return GalleryURL;


    }

    @Override
    public int getCount() {
        return arraySize;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ImageView imageView = new ImageView(context);
        Picasso.with(context).load(GalleryURL.get(position)).error(R.drawable.ic_cctv).into(imageView);
       /* TextView textCount = new TextView(context);
        container.addView(textCount,0);
        textCount.setText(position+"/"+arraySize);*/
        container.addView(imageView, 0);
        return imageView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
}
