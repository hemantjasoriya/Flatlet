package in.flatlet.www.Flatlet.Home.fragments.favouriteFragment;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import java.util.ArrayList;

import in.flatlet.www.Flatlet.R;
import in.flatlet.www.Flatlet.recyclerView.FeedReaderContract;
import in.flatlet.www.Flatlet.recyclerView.FeedReaderDbHelper;


public class FavouriteFragment extends Fragment {
    SQLiteDatabase db;
    final String TAG = "FavouriteFragment";
    ArrayList<FavouriteHostelDataModel> favouriteHostelList = new ArrayList<>();

    RecyclerView favouriteRecyclerView;
    LinearLayoutManager recyclerViewLayoutManager;

    ProgressBar progressBar;
    @Nullable
    @Override

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.favourites_fragment,container,false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        db = new FeedReaderDbHelper(getContext()).getWritableDatabase();

        Log.i(TAG, "onActivityCreated: SQLite Object is Created successfully");



        favouriteRecyclerView = (RecyclerView)getActivity().findViewById(R.id.favouriteRecyclerView);
        progressBar =(ProgressBar)getActivity().findViewById(R.id.progres_bar);
        addSqliteDataToList();
        FavouriteListRecyclerAdapter adapter = new FavouriteListRecyclerAdapter(getContext(),favouriteHostelList,db);
        favouriteRecyclerView.setHasFixedSize(true);
        recyclerViewLayoutManager = new LinearLayoutManager(getContext());
        favouriteRecyclerView.setLayoutManager(recyclerViewLayoutManager);
        favouriteRecyclerView.setAdapter(adapter);
        progressBar.setVisibility(View.GONE);


    }
    private void addSqliteDataToList(){
        Log.i(TAG, "addSqliteDataToList: Startig to add data into Model class from SQLiteDatabase");
        progressBar.setVisibility(View.VISIBLE);

        String [] projection = {FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE, FeedReaderContract.FeedEntry.COLUMN_NAME_SECONDARY_ADDRESS,
                FeedReaderContract.FeedEntry.COLUMN_NAME_RENT, FeedReaderContract.FeedEntry.COLUMN_NAME_IMG_URL, FeedReaderContract.FeedEntry.COLUMN_NAME_RATING};
        Cursor cursor = db.query(FeedReaderContract.FeedEntry.TABLE_NAME,projection,null,null,null,null,null);

        while(cursor.moveToNext()){
            Log.i(TAG, "addSqliteDataToList: The records inside the sqlite dtabase is "+cursor.getString(0));
            FavouriteHostelDataModel favouriteHostelDataModel = new FavouriteHostelDataModel();
            favouriteHostelDataModel.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE)));
            favouriteHostelDataModel.setAddress_secondary(cursor.getString(cursor.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_SECONDARY_ADDRESS)));
            favouriteHostelDataModel.setUrl(cursor.getString(cursor.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_IMG_URL)));
            favouriteHostelDataModel.setRent(cursor.getInt(cursor.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_RENT)));
            favouriteHostelDataModel.setRating(cursor.getDouble(cursor.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_RATING)));
            favouriteHostelList.add(favouriteHostelDataModel);

        }
        Log.i(TAG, "addSqliteDataToList:  Items added in database are"+cursor.getCount());



    }
}
