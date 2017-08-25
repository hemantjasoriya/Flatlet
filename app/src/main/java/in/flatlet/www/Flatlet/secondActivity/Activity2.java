package in.flatlet.www.Flatlet.secondActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import in.flatlet.www.Flatlet.R;
import in.flatlet.www.Flatlet.thirdActivity.MainActivity_third;


public class Activity2 extends AppCompatActivity implements OnMapReadyCallback {
    private final String TAG = "Activity2";
    private Toolbar toolbar;
    private RequestQueue requestQueue;
    private String hostel_title;
    private String dbqry;
    private Button moreAmeButton;
    private ListView listView;
    RatingBar ratingBarFood, ratingBarStaff, ratingBarAccommodation, ratingBarStudyEnvironment;
    private TextView text_single_nonac, text_single_ac, text_double_nonac, text_double_ac, area_single_room, area_double_room, gender, locality;
    ImageView imageHead;
    String CCTV, ame_elevator, ame_toilet_attached, eve_snacks, ownership;
    ProgressBar progressBar;
    ArrayList<String> ameTitle = new ArrayList<>();
    ArrayList<Integer> ameVector = new ArrayList<>();
    Double location_latitude = 3.14;
    Double location_longitude = 3.14;
    SupportMapFragment mapFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: ACtivity2 onCreate started");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity2);
        hostel_title = getIntent().getStringExtra("hostel_title");
        dbqry = "Select * from hostel_specs where title=" + "'" + hostel_title + "'";
        dbqry = dbqry.replace(" ", "%20");
        final String url = "http://flatlet.in/flatletwebservicescomplete/completeHostelData.jsp?dbqry=" + dbqry;
        Log.i(TAG, "onCreate: string received from prev activity is" + hostel_title + url);
        Log.i(TAG, "onCreate: toolbar set ho gya");
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_fragment);
        text_single_nonac = (TextView) findViewById(R.id.text_single_nonac);
        text_single_ac = (TextView) findViewById(R.id.text_single_ac);
        text_double_nonac = (TextView) findViewById(R.id.text_double_nonac);
        text_double_ac = (TextView) findViewById(R.id.text_double_ac);
        area_single_room = (TextView) findViewById(R.id.area_single_room);
        area_double_room = (TextView) findViewById(R.id.area_double_room);
        ratingBarFood = (RatingBar) findViewById(R.id.ratingBarFood);
        ratingBarStaff = (RatingBar) findViewById(R.id.ratingBarStaff);
        ratingBarAccommodation = (RatingBar) findViewById(R.id.ratingBarAccommodation);
        ratingBarStudyEnvironment = (RatingBar) findViewById(R.id.ratingBarStudyEnvironment);
        gender = (TextView) findViewById(R.id.gender);
        locality = (TextView) findViewById(R.id.locality);
        imageHead = (ImageView) findViewById(R.id.imageHead);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        moreAmeButton = (Button) findViewById(R.id.moreAmeButton);
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
                onBackPressed();
            }
        });
        ImageView imageHead = (ImageView) findViewById(R.id.imageHead);
        imageHead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Activity2.this, MainActivity_third.class);
                Activity2.this.startActivity(intent);
            }
        });
        fetch_details();

        moreAmeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                while (ameTitle.size() < 11) {
                    ameTitle.add(0, "CCTV Surveillance");
                    ameTitle.add(1, "Elevator");
                    ameTitle.add(2, "Attached Toilet");
                    ameTitle.add(3, "Ownership");
                    ameTitle.add(4, "Evening Snacks");
                    ameTitle.add(5, "Daily Cleaning");
                    ameTitle.add(6, "Water Purifier(RO)");
                    ameTitle.add(7, "Laundry");
                    ameTitle.add(8, "Security Guard");
                    ameTitle.add(9, "Geyser");
                    ameTitle.add(10, "Study Table");
                }

                Log.i(TAG, "onClick: ametitle is filled");
                while (ameVector.size() < 11) {

                    if ("1".equalsIgnoreCase(CCTV)) {
                        ameVector.add(0, R.drawable.ic_cctv);
                        Log.i(TAG, "parseResponse: CCTV is available and added to arraylist");
                    } else {
                        ameVector.add(0, R.drawable.ic_cctv_na);
                    }
                    if ("1".equalsIgnoreCase(ame_elevator)) {
                        ameVector.add(1, R.drawable.ic_ame_elevator);
                    } else {
                        ameVector.add(1, R.drawable.ic_ame_elevator_na);
                    }
                    if ("1".equalsIgnoreCase(ame_toilet_attached)) {
                        ameVector.add(2, R.drawable.ic_ame_attached_toilet);
                    } else {
                        ameVector.add(2, R.drawable.ic_ame_attached_toilet_na);
                    }
                    if ("1".equalsIgnoreCase(ownership)) {
                        ameVector.add(3, R.drawable.ic_owner);
                    } else {
                        ameVector.add(3, R.drawable.ic_owner_na);
                    }
                    if ("1".equalsIgnoreCase(eve_snacks)) {
                        ameVector.add(4, R.drawable.ic_eve_snacks);
                    } else {
                        ameVector.add(4, R.drawable.ic_eve_sncaks_na);
                    }
                    ameVector.add(5, R.drawable.ic_ame_cleaning);
                    ameVector.add(6, R.drawable.ic_ame_ro);
                    ameVector.add(7, R.drawable.ic_ame_laundry);
                    ameVector.add(8, R.drawable.ic_security);
                    ameVector.add(9, R.drawable.ic_geyser);
                    ameVector.add(10, R.drawable.ic_studytable);
                }
                listView = new ListView(Activity2.this);

                MoreAmenityArrayAdapter adapter1 = new MoreAmenityArrayAdapter(getApplicationContext(), ameTitle, ameVector);
                listView.setAdapter(adapter1);
                AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(Activity2.this);
                builder.setTitle("Amenities")
                        .setView(listView)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ameTitle.clear();
                                ameVector.clear();
                            }
                        }).create().show();
            }
        });
    }


    private void fetch_details() {
        Log.d(TAG, "fetch_details: Volley Request Sent");
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest("http://flatlet.in/flatletwebservicescomplete/completeHostelData.jsp?dbqry=" + dbqry, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    parseResponse(response);
                    Log.i(TAG, "onResponse: Response sent for parsing ");
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.i(TAG, "onResponse: couldn't sent the data for parsing" + e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(TAG, "onErrorResponse: Error in getting response" + error);
            }
        });
        requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonObjectRequest);
        Log.i(TAG, "fetch_details: Volley Request is sent ");
    }

    private void parseResponse(JSONObject response) throws JSONException {
        Log.d(TAG, "parseResponse: resopse parsing initiated");
        location_latitude = response.getDouble("location_latitude");
        location_longitude = response.getDouble("location_longitude");
        text_single_nonac.setText(response.getString("rent_single_nonac"));
        text_single_ac.setText(response.getString("rent_single_ac"));
        text_double_nonac.setText(response.getString("rent_double_nonac"));
        text_double_ac.setText(response.getString("rent_double_ac"));
        area_single_room.setText(String.valueOf(response.getInt("dim_single_length") * response.getInt("dim_single_width") * 0.001));
        area_double_room.setText(String.valueOf(response.getInt("dim_double_length") * response.getInt("dim_double_width") * 0.001));
        gender.setText(response.getString("gender"));
        locality.setText(response.getString("address_secondary"));
        toolbar.setTitle(response.getString("title"));
        CCTV = response.getString("CCTV");
        Log.i(TAG, "parseResponse: CCTV Values store in String object" + CCTV);
        ame_elevator = response.getString("ame_elevator");
        eve_snacks = response.getString("eve_snacks");
        ownership = response.getString("ownership");
        mapFragment.getMapAsync(this);

        Picasso.with(this).load("http://images.flatlet.in/images/24%20Paradise/IMG_20170607_203707-01.jpg").into(imageHead);
        progressBar.setVisibility(View.GONE);

        Log.i(TAG, "parseResponse: coordinates are  " + location_latitude + "," + location_longitude);
        Log.i(TAG, "parseResponse: Response parsed succesfully");
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d(TAG, "onMapReady: omMapReadyCallBack Called");
        LatLng place = new LatLng(location_latitude, location_longitude);
        Log.d(TAG, "onMapReady:coordinate received are" + location_latitude + "," + location_longitude);
        googleMap.addMarker(new MarkerOptions().position(place)
                .title(hostel_title));

        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        googleMap.setBuildingsEnabled(true);
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(place));
        googleMap.moveCamera(CameraUpdateFactory.zoomTo(15f));


    }

    public void onSubmitRatingButton(View view) {

        Float rating_food = ratingBarFood.getRating();
        Float rating_staff = ratingBarStaff.getRating();
        Float rating_accommodation = ratingBarAccommodation.getRating();
        Float rating_studyEnvironment = ratingBarStudyEnvironment.getRating();
        ratingBarFood.setIsIndicator(true);
        ratingBarAccommodation.setIsIndicator(true);
        ratingBarStaff.setIsIndicator(true);
        ratingBarStudyEnvironment.setIsIndicator(true);

        Log.i(TAG, "onSubmitRatingButton: ratings are" + rating_food + rating_accommodation + rating_staff + rating_studyEnvironment);

    }

    public void onEditRatingsButton(View view) {

        ratingBarFood.setIsIndicator(false);
        ratingBarAccommodation.setIsIndicator(false);
        ratingBarStaff.setIsIndicator(false);
        ratingBarStudyEnvironment.setIsIndicator(false);

    }
}

