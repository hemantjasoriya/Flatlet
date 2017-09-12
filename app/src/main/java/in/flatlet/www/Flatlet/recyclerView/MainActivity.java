package in.flatlet.www.Flatlet.recyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
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
    private final String TAG = "MAINACTIVTY";
    private final String MyRequestTag = "MyTag";
    private FloatingActionButton filterFloatingButton;
    private List<GetDataAdapter> dataModelArrayList;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private String GET_JSON_DATA_HTTP_URL = null;
    private RequestQueue requestQueue;
    private String locality;
    private String roomType;
    private String gender;
    private Toolbar toolbar ;

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(MainActivity.this, FirstActivity.class);
        intent.setFlags(1);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate:started ");
        setContentView(R.layout.activity_main);
        dataModelArrayList = new ArrayList<>();
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView1);
        toolbar = (Toolbar)findViewById(R.id.toolbar);
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
       /* //this add a divider between two cards
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.HORIZONTAL));*/
        Log.i(TAG, "onCreate: RecyclerVIew is hinged");
        progressBar = (ProgressBar) findViewById(R.id.progressBar1);
        filterFloatingButton = (FloatingActionButton) findViewById(R.id.filterFloatingButton);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager recyclerViewlayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(recyclerViewlayoutManager);
        progressBar.setVisibility(View.VISIBLE);
        recyclerView.setAlpha(0.7f);
        toolbar.setAlpha(0.7f);
        locality = getIntent().getStringExtra("locality");
        String dbqry = getIntent().getStringExtra("dbqry");
        roomType = getIntent().getStringExtra("roomType");
        gender = getIntent().getStringExtra("gender");

        String finalDbQuery;
        if(getIntent().getFlags()!=13){
            finalDbQuery = dbqry + locality +"%20ORDER%20BY%20RAND()";
        }
        else {
            finalDbQuery = dbqry +locality;
        }
        Log.i(TAG, "onCreate: DATA INCOMING CHECK locality is " + locality + "and roomType is " + roomType + "and dbqry is" + dbqry + "and gender is" + gender);
        GET_JSON_DATA_HTTP_URL = "http://flatlet.in/flatletwebservices/partialHostelData.jsp?dbqry=" + finalDbQuery;
        JSON_DATA_WEB_CALL();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) {
                    filterFloatingButton.hide();
                    toolbar.setVisibility(View.GONE);
                }
                else {

                    filterFloatingButton.show();
                    toolbar.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void JSON_DATA_WEB_CALL() {
        if (MySingleton.getInstance(getApplicationContext()).isOnline()){
            Log.i(TAG, "url went to volley request is " + GET_JSON_DATA_HTTP_URL);
            JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(GET_JSON_DATA_HTTP_URL,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {

                            JSON_PARSE_DATA_AFTER_WEBCALL(response);
                            Log.i(TAG, "onResponse: data is send further for parsing");
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            error.printStackTrace();
                            Log.i(TAG, "onErrorResponse: response error ");
                        }
                    });

            requestQueue = MySingleton.getInstance(this.getApplicationContext()).getRequestQueue();
            jsonArrayRequest.setTag(MyRequestTag);
            jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy(2000,3,1f));
            Log.i(TAG, "JSON_DATA_WEB_CALL: RequestQueue's object formation");
            MySingleton.getInstance(this).addToRequestQueue(jsonArrayRequest);
        }
        else {
            Toast.makeText(getApplicationContext(),"No Internet Connection ! Please Try Again",Toast.LENGTH_SHORT).show();
        }

    }

    private void JSON_PARSE_DATA_AFTER_WEBCALL(JSONArray array) {
        for (int i = 0; i < array.length(); i++) {
            GetDataAdapter GetDataAdapter2 = new GetDataAdapter();
            JSONObject jsonObject;
            try {
                jsonObject = array.getJSONObject(i);
                GetDataAdapter2.setName(jsonObject.getString("title"));
                Log.i(TAG, "JSON_PARSE_DATA_AFTER_WEBCALL: data is being extracted" + jsonObject.getString("title"));
                GetDataAdapter2.setRent(jsonObject.getString(roomType));
                GetDataAdapter2.setAddress(jsonObject.getString("address_secondary"));
                GetDataAdapter2.setCardRating((float) jsonObject.getDouble("rating"));
                Log.i(TAG, "JSON_PARSE_DATA_AFTER_WEBCALL: "+((float) jsonObject.getDouble("rating")));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            dataModelArrayList.add(GetDataAdapter2);
        }
        RecyclerView.Adapter recyclerViewAdapter = new RecyclerViewAdapter(dataModelArrayList, MainActivity.this, recyclerView);
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerView.setAlpha(1f);
        toolbar.setAlpha(1.0f);
        progressBar.setVisibility(View.GONE);
    }

    public void onFilterClick(View view) {
        Intent intent = new Intent(MainActivity.this, FilterActivity.class);
        intent.putExtra("locality", locality);
        intent.putExtra("gender", gender);
        intent.putExtra("roomType", roomType);
        Log.i(TAG, "onFilterClick: data sent to FilterActivity i.e. locality" + locality + gender + roomType);
        startActivity(intent);
    }
    @Override
    protected void onStop() {
        super.onStop();
        if (requestQueue!=null){
            requestQueue.cancelAll(MyRequestTag);
        }
    }
}