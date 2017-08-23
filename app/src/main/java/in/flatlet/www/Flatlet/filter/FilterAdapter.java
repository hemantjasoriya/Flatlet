package in.flatlet.www.Flatlet.filter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

public class FilterAdapter extends FragmentStatePagerAdapter {
    public FilterAdapter(FragmentManager fm) {

        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Log.i("bjb", "getItem: ");
        Fragment fragment = null;
        if (position == 1)
            fragment = new SortFragment();
        if (position == 0)
            fragment = new FilterFragment();

        return fragment;
    }

    @Override
    public int getCount() {

        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 1)
            return "Sort By";
        else
            return "Filters";
    }

}