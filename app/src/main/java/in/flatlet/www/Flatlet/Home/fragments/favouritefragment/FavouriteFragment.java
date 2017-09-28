package in.flatlet.www.Flatlet.Home.fragments.favouritefragment;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import java.util.ArrayList;

import in.flatlet.www.Flatlet.R;
import in.flatlet.www.Flatlet.recyclerView.FeedReaderContract;
import in.flatlet.www.Flatlet.recyclerView.FeedReaderDbHelper;


public class FavouriteFragment extends Fragment {
    private SQLiteDatabase db;
    private final ArrayList<FavouriteHostelDataModel> favouriteHostelList = new ArrayList<>();
    private ProgressBar progressBar;
    RelativeLayout RL_favourite;
    RecyclerView favouriteRecyclerView;
    FavouriteListRecyclerAdapter adapter;


    @Nullable
    @Override

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.favourites_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        db = new FeedReaderDbHelper(getContext()).getWritableDatabase();
        favouriteRecyclerView = (RecyclerView) getActivity().findViewById(R.id.favouriteRecyclerView);
        progressBar = (ProgressBar) getActivity().findViewById(R.id.progres_bar);
        RL_favourite = (RelativeLayout) getActivity().findViewById(R.id.RL_favourite);

        adapter = new FavouriteListRecyclerAdapter(getActivity(), favouriteHostelList, db);

        new AddToSQLiteTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

    }

    private void addSqliteDataToList() {
        progressBar.setVisibility(View.VISIBLE);


        String[] projection = {FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE, FeedReaderContract.FeedEntry.COLUMN_NAME_SECONDARY_ADDRESS,
                FeedReaderContract.FeedEntry.COLUMN_NAME_RENT, FeedReaderContract.FeedEntry.COLUMN_NAME_IMG_URL, FeedReaderContract.FeedEntry.COLUMN_NAME_RATING};
        Cursor cursor = db.query(FeedReaderContract.FeedEntry.TABLE_NAME, projection, null, null, null, null, null);

        while (cursor.moveToNext()) {
            FavouriteHostelDataModel favouriteHostelDataModel = new FavouriteHostelDataModel();
            favouriteHostelDataModel.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE)));
            favouriteHostelDataModel.setAddress_secondary(cursor.getString(cursor.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_SECONDARY_ADDRESS)));
            favouriteHostelDataModel.setUrl(cursor.getString(cursor.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_IMG_URL)));
            favouriteHostelDataModel.setRent(cursor.getInt(cursor.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_RENT)));
            favouriteHostelDataModel.setFavouriteCardRating((float) cursor.getDouble(cursor.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_RATING)));
            favouriteHostelList.add(favouriteHostelDataModel);

        }
    }

    private class AddToSQLiteTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            addSqliteDataToList();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (favouriteHostelList.size() == 0) {
                RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                lp.addRule(RelativeLayout.CENTER_IN_PARENT);
                ImageView imageView = new ImageView(getContext());
                RL_favourite.addView(imageView, lp);
                RL_favourite.removeView(favouriteRecyclerView);
                imageView.getLayoutParams().width = 900;
                imageView.getLayoutParams().height = 900;
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView.setAlpha(0.7f);
                imageView.setImageResource(R.drawable.nf);


            }

            favouriteRecyclerView.setHasFixedSize(true);
            LinearLayoutManager recyclerViewLayoutManager = new LinearLayoutManager(getContext());
            favouriteRecyclerView.setLayoutManager(recyclerViewLayoutManager);
            favouriteRecyclerView.setAdapter(adapter);

            progressBar.setVisibility(View.GONE);
        }
    }
}
