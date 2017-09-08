package in.flatlet.www.Flatlet.thirdActivity;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import in.flatlet.www.Flatlet.R;


class ImageSwitcherAdapter extends PagerAdapter {
    private final Context context;
    /* private int[] GalleryImages = new int[]{R.drawable.battleship,R.drawable.ic_elevator,
     R.drawable.ic_elevator,R.drawable.battleship};*/
    private final String[] GalleryURL = new String[]{"http://images.flatlet.in/images_thumbs/1/1.jpg"
            , "http://images.flatlet.in/images_thumbs/6/1.jpg", "http://images.flatlet.in/images_thumbs/18/1.jpg"
            , "http://images.flatlet.in/images_thumbs/13/1.jpg"};

    ImageSwitcherAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return GalleryURL.length;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ImageView imageView = new ImageView(context);
        Picasso.with(context).load(GalleryURL[position]).error(R.drawable.ic_cctv).into(imageView);
       /* imageView.setImageResource(GalleryImages[position]);*/
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
