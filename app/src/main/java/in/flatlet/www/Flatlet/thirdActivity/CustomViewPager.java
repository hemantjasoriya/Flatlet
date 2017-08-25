package in.flatlet.www.Flatlet.thirdActivity;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;


public class CustomViewPager extends ViewPager {
  /*  private boolean isPagingEnabled = true;*/

    public CustomViewPager(Context context) {
        super(context);
    }

    public CustomViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        /*return this.isPagingEnabled && super.onTouchEvent(event);*/
        return false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        /*return this.isPagingEnabled && super.onInterceptTouchEvent(event);*/
        return false;
    }

    /*public void setPagingEnabled(boolean b) {
        this.isPagingEnabled = b;
    }*/
}
