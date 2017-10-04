package in.flatlet.www.Flatlet.recyclerView;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import in.flatlet.www.Flatlet.Home.FirstActivity;
import in.flatlet.www.Flatlet.R;
import in.flatlet.www.Flatlet.Utility.MySingleton;
import in.flatlet.www.Flatlet.filter.FilterActivity;

public class MainActivity extends AppCompatActivity {
    private final String MyRequestTag = "MyTag";
    private FloatingActionButton filterFloatingButton;
    private List<GetDataAdapter> dataModelArrayList;
    private RecyclerView recyclerView;
    private ImageView progressBar;
    private String GET_JSON_DATA_HTTP_URL = null;
    private RequestQueue requestQueue;
    private String locality;
    private String roomType;
    private String gender;
    private Toolbar toolbar;
    private TextView noHostelTextView;

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(MainActivity.this, FirstActivity.class);
        intent.setFlags(1);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main);
        dataModelArrayList = new ArrayList<>();
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView1);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, FirstActivity.class);
                intent.setFlags(1);
                startActivity(intent);
            }
        });
        progressBar = (ImageView) findViewById(R.id.progressBar1);
        ObjectAnimator animation = ObjectAnimator.ofFloat(progressBar, "rotationY", 0.0f, 360f);
        animation.setDuration(5500);
        animation.setInterpolator(new AccelerateDecelerateInterpolator());
        animation.start();
        progressBar.setVisibility(View.VISIBLE);
        recyclerView.setAlpha(0.7f);
        toolbar.setAlpha(0.7f);
        filterFloatingButton = (FloatingActionButton) findViewById(R.id.filterFloatingButton);
        filterFloatingButton.bringToFront();
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager recyclerViewlayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(recyclerViewlayoutManager);
        locality = getIntent().getStringExtra("locality");
        String dbqry = getIntent().getStringExtra("dbqry");
        roomType = getIntent().getStringExtra("roomType");
        gender = getIntent().getStringExtra("gender");

        String finalDbQuery;
        if (getIntent().getFlags() == 13) {
            finalDbQuery = dbqry + locality;

        } else if (getIntent().getFlags() == 14) {
            finalDbQuery = dbqry + "%20AND%20gender='" + gender + "'" + "%20ORDER%20BY%20RAND()";

        } else {
            finalDbQuery = dbqry + locality + "%20ORDER%20BY%20RAND()";
        }
        GET_JSON_DATA_HTTP_URL = "http://flatlet.in/webservices/partialHostelData.jsp?dbqry=" + finalDbQuery;

        JSON_DATA_WEB_CALL();

        if (android.os.Build.VERSION.SDK_INT >= 21) {
            //if version is >lollipop then animate the FAB while scrolling up and down


            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    if (dy > 0) {
                        filterFloatingButton.hide();
                        toolbar.setVisibility(View.GONE);
                    } else {

                        filterFloatingButton.show();
                        toolbar.setVisibility(View.VISIBLE);
                    }
                }
            });

        } else {
            Log.i("MainActivity", "onCreate android version is : " + android.os.Build.VERSION.SDK_INT);
            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    if (dy > 0) {
                        filterFloatingButton.hide();
                        toolbar.setVisibility(View.GONE);
                    } else {

                        filterFloatingButton.show();
                        toolbar.setVisibility(View.VISIBLE);
                    }
                }
            });
        }

    }

    private void JSON_DATA_WEB_CALL() {
        if (MySingleton.getInstance(getApplicationContext()).isOnline()) {

            JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(GET_JSON_DATA_HTTP_URL,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            new ParseResponseTask().execute(response);

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    });

            requestQueue = MySingleton.getInstance(this.getApplicationContext()).getRequestQueue();
            jsonArrayRequest.setTag(MyRequestTag);
            jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy(15000, 3, 1f));

            MySingleton.getInstance(this).addToRequestQueue(jsonArrayRequest);
        } else {
            Toast.makeText(getApplicationContext(), "No Internet Connection ! Please Try Again", Toast.LENGTH_SHORT).show();
        }

    }

    public void onFilterClick(View view) {
        Intent intent = new Intent(MainActivity.this, FilterActivity.class);
        intent.putExtra("locality", locality);
        intent.putExtra("gender", gender);
        intent.putExtra("roomType", roomType);

        startActivity(intent);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (requestQueue != null) {
            requestQueue.cancelAll(MyRequestTag);
        }
    }

    private class ParseResponseTask extends AsyncTask<org.json.JSONArray, Void, Void> {


        @Override
        protected Void doInBackground(JSONArray... array) {
            for (int i = 0; i < array[0].length(); i++) {
                GetDataAdapter GetDataAdapter2 = new GetDataAdapter();
                JSONObject jsonObject;
                try {
                    jsonObject = array[0].getJSONObject(i);
                    GetDataAdapter2.setName(jsonObject.getString("title"));
                    GetDataAdapter2.setRent(jsonObject.getString(roomType));
                    GetDataAdapter2.setAddress(jsonObject.getString("address_secondary"));
                    GetDataAdapter2.setCardRating((float) jsonObject.getDouble("rating"));
                    GetDataAdapter2.setGender(jsonObject.getString("gender"));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                dataModelArrayList.add(GetDataAdapter2);

            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            RecyclerView.Adapter recyclerViewAdapter = new RecyclerViewAdapter(dataModelArrayList, MainActivity.this, recyclerView);
            recyclerView.setAdapter(recyclerViewAdapter);
            recyclerView.setAlpha(1f);
            toolbar.setAlpha(1.0f);
            progressBar.setVisibility(View.GONE);
        }
    }
}