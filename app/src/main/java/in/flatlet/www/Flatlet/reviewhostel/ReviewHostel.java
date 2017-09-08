package in.flatlet.www.Flatlet.reviewhostel;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import in.flatlet.www.Flatlet.R;
import in.flatlet.www.Flatlet.Utility.MySingleton;
import in.flatlet.www.Flatlet.secondActivity.Activity2;


public class ReviewHostel extends AppCompatActivity {
    private SQLiteDatabase db;
    private RequestQueue requestQueue;
    private RequestQueue requestQueue1;
    private static final String TAG = "ReviewHostel";
    private AutoCompleteTextView autoComplete;
    private Cursor cursor1;
    private final ArrayList<String> list = new ArrayList<>();
    private ArrayAdapter<String> adapter;
    private ImageView reviewImageView;
    private TextView reviewHostelTitle;
    private TextView reviewSecAddress;
    private CardView reviewCard;
    private JsonObjectRequest jsonObjRequest;
    private String hostel_title;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.review_hostel);
        autoComplete = (AutoCompleteTextView) findViewById(R.id.autoComplete);
        reviewImageView = (ImageView) findViewById(R.id.review_imageView);
        reviewHostelTitle = (TextView) findViewById(R.id.review_hostel_title);
        reviewSecAddress = (TextView) findViewById(R.id.review_sec_address);
        reviewCard = (CardView) findViewById(R.id.review_hostel_card);
        reviewCard.setVisibility(View.GONE);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        FeedReaderDbHelperReviewHostel mDbHelper = new FeedReaderDbHelperReviewHostel(this);
        db = mDbHelper.getWritableDatabase();
        Log.i(TAG, "onCreate: of main");

        // Gets the data repository in write mode

       /* mDbHelper.onCreate(db);*/
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        if (!prefs.getBoolean("secondTime", false)) {
            // <---- run your one time code here
            Log.i(TAG, "onCreate: before oncreate");
            mDbHelper.onCreateOriginal(db);
            Log.i(TAG, "onCreate: called");


            // mark first time has run.
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("secondTime", true);
            editor.apply();
        }
        String[] projection = {
                FeedReaderContractReviewHostel.FeedEntry._ID,
                FeedReaderContractReviewHostel.FeedEntry.COLUMN_NAME_TITLE,

        };

        Cursor cursor = db.query(FeedReaderContractReviewHostel.FeedEntry.TABLE_NAME, projection, null, null, null, null, null);
        int i = cursor.getCount();
        Log.i(TAG, "onCreate: k" + i);


        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest("http://flatlet.in/flatlettitlefetcher/titlefetcher.jsp?count=" + i,

                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.i(TAG, "onResponse: " + response.length());

                        for (int j = 0; j < response.length(); j++) {
                            try {
                                String title = response.getString(j);
                                Log.i(TAG, "onResponse: " + title);
                                ContentValues values = new ContentValues();
                                values.put(FeedReaderContractReviewHostel.FeedEntry.COLUMN_NAME_TITLE, title);
                                long newRowId = db.insert(FeedReaderContractReviewHostel.FeedEntry.TABLE_NAME, null, values);
                                Log.i(TAG, "onResponse:1 " + newRowId);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Log.i(TAG, "onErrorResponse: " + error);


                    }
                });
        requestQueue = MySingleton.getInstance(getApplicationContext()).getRequestQueue();
        jsonArrayRequest.setTag("MyRequestTag");
        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonArrayRequest);

        autoComplete = (AutoCompleteTextView)findViewById(R.id.autoComplete);
        autoComplete.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                Log.i(TAG, "beforeTextChanged: ");
                if (start <= 7)
                    reviewCard.setVisibility(View.GONE);

                reviewSecAddress.setText(null);
                reviewImageView.setImageResource(0);
                list.clear();
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                Log.i(TAG, "onTextChanged: before if " + count);
                Log.i(TAG, "onTextChanged: before " + before);
                Log.i(TAG, "onTextChanged: start " + start);
                if (start > 1) {
                    Log.i(TAG, "onTextChanged: after if" + s);


                    cursor1 = db.rawQuery("Select " + FeedReaderContractReviewHostel.FeedEntry.COLUMN_NAME_TITLE + " from "
                            + FeedReaderContractReviewHostel.FeedEntry.TABLE_NAME + " WHERE " + FeedReaderContractReviewHostel.FeedEntry.COLUMN_NAME_TITLE + " LIKE '%" + s + "%'", null);

                    Log.i(TAG, "onTextChanged: query=" + "Select " + FeedReaderContractReviewHostel.FeedEntry.COLUMN_NAME_TITLE + " from "
                            + FeedReaderContractReviewHostel.FeedEntry.TABLE_NAME + " WHERE " + FeedReaderContractReviewHostel.FeedEntry.COLUMN_NAME_TITLE + " LIKE '%" + s + "%'", null);

                    while (cursor1.moveToNext()) {


                        String title = cursor1.getString(0);
                        Log.i(TAG, "onTextChanged: " + title);
                        Log.i(TAG, "onTextChanged: " + cursor1.getCount());

                        list.add(title);
                        Log.i(TAG, "onTextChanged: after add" + title);
                        Log.i(TAG, "onTextChanged: list size " + list.size());
                    }
                    adapter = new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_item, list);
                    autoComplete.setAdapter(adapter);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.i(TAG, "afterTextChanged: ");

            }
        });

        autoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

                if (MySingleton.getInstance(getApplicationContext()).isOnline()){
                    Log.i(TAG, "onItemClick: clicked hostel is " + adapter.getItem(arg2));
                    reviewHostelTitle.setText(adapter.getItem(arg2));
                    String title = adapter.getItem(arg2).replace(" ", "%20");
                    hostel_title = title;

                    jsonObjRequest = new JsonObjectRequest
                            ("http://flatlet.in/flatletreviewdata/reviewdata.jsp?title=" + title, null, new Response.Listener<JSONObject>() {

                                @Override
                                public void onResponse(JSONObject response) {
                                    try {
                                        Log.i(TAG, "onResponse: ");
                                        reviewSecAddress.setText(response.getString("address_secondary"));
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                        Log.i(TAG, "onResponse: catch " + e);
                                    }
                                }
                            }, new Response.ErrorListener() {

                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Log.i(TAG, "onErrorResponse: " + error);
                                }
                            });
                    requestQueue1 = MySingleton.getInstance(getApplicationContext()).getRequestQueue();
                    jsonObjRequest.setTag("MyRequestTag2");
                    MySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjRequest);

                    Picasso.with(getBaseContext()).load("http://images.flatlet.in/images/24%20Paradise/IMG_20170607_203707-01.jpg").into(reviewImageView);
                    reviewCard.setVisibility(View.VISIBLE);
                }
                    else {
                    Toast.makeText(getApplicationContext(),"No Internet Connection ! Please Try Again",Toast.LENGTH_SHORT).show();
                }

            }
        });

reviewCard.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Intent intent = new Intent(ReviewHostel.this, Activity2.class);
        intent.putExtra("hostel_title",hostel_title);
        Log.i(TAG, "onClick: title sent to Activity 2 is");
        startActivity(intent);
    }
});
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(requestQueue!=null){
            requestQueue.cancelAll("MyRequestTag");
        }
        else if(requestQueue1!=null){
            requestQueue1.cancelAll("MyRequestTag2");
        }
    }


}
