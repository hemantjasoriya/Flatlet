package in.flatlet.www.Flatlet.recyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import in.flatlet.www.Flatlet.R;
import in.flatlet.www.Flatlet.Utility.MySingleton;
import in.flatlet.www.Flatlet.filter.FilterActivity;

public class MainActivity extends AppCompatActivity {
    private final String TAG = "MAINACTIVTY";
    private final String MyRequestTag = "MyTag";
    private FloatingActionButton filterFloatingButton;
    private List<GetDataAdapter> dataModelArrayList;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager recyclerViewlayoutManager;
    private RecyclerView.Adapter recyclerViewAdapter;
    private ProgressBar progressBar;
    private String GET_JSON_DATA_HTTP_URL = null;
    private JsonArrayRequest jsonArrayRequest;
    private RequestQueue requestQueue;
    private String locality;
    private String dbqry = null;
    private String roomType;
    public String gender;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate:started ");
        setContentView(R.layout.activity_main);
        dataModelArrayList = new ArrayList<>();
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView1);
       /* //this add a divider between two cards
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.HORIZONTAL));*/
        Log.i(TAG, "onCreate: RecyclerVIew is hinged");
        progressBar = (ProgressBar) findViewById(R.id.progressBar1);
        filterFloatingButton = (FloatingActionButton) findViewById(R.id.filterFloatingButton);
        recyclerView.setHasFixedSize(true);
        recyclerViewlayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(recyclerViewlayoutManager);
        progressBar.setVisibility(View.VISIBLE);
        locality = getIntent().getStringExtra("locality");
        dbqry = getIntent().getStringExtra("dbqry");
        roomType = getIntent().getStringExtra("roomType");
        gender = getIntent().getStringExtra("gender");
        String finalDbQuery = dbqry + locality;
        Log.i(TAG, "onCreate: DATA INCOMING CHECK locality is " + locality + "and roomType is " + roomType + "and dbqry is" + dbqry + "and gender is" + gender);
        GET_JSON_DATA_HTTP_URL = "http://flatlet.in/flatletwebservices/partialHostelData.jsp?dbqry=" + finalDbQuery;
        JSON_DATA_WEB_CALL();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0)
                    filterFloatingButton.hide();
                else
                    filterFloatingButton.show();
            }
        });
    }

    private void JSON_DATA_WEB_CALL() {
        if (MySingleton.getInstance(getApplicationContext()).isOnline()){
            Log.i(TAG, "url went to volley request is " + GET_JSON_DATA_HTTP_URL);
            jsonArrayRequest = new JsonArrayRequest(GET_JSON_DATA_HTTP_URL,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            progressBar.setVisibility(View.GONE);
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
            return;
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
            } catch (JSONException e) {
                e.printStackTrace();
            }
            dataModelArrayList.add(GetDataAdapter2);
        }
        recyclerViewAdapter = new RecyclerViewAdapter(dataModelArrayList,MainActivity.this, recyclerView);
        recyclerView.setAdapter(recyclerViewAdapter);
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