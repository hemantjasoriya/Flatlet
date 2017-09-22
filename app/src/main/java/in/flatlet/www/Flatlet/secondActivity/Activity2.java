package in.flatlet.www.Flatlet.secondActivity;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
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
import in.flatlet.www.Flatlet.Utility.MySingleton;
import in.flatlet.www.Flatlet.recyclerView.FeedReaderContract;
import in.flatlet.www.Flatlet.recyclerView.FeedReaderDbHelper;
import in.flatlet.www.Flatlet.thirdActivity.MainActivity_third;


public class Activity2 extends AppCompatActivity implements OnMapReadyCallback {
    private Toolbar toolbar;
    private String hostel_title, hostel_rent, eve_snacks, ownership, ame_toilet_attached, ame_elevator, dbqry, primary_contact, secondary_contact, CCTV1, rent_single_nonac, rent_single_ac, rent_double_nonac, rent_double_ac, gender1, address_secondary, title, CCTV, totalViews1;
    private Button ratingSubmitButton;
    private ListView listView;
    private RatingBar ratingBarFood, ratingBarStaff, ratingBarAccommodation, ratingBarStudyEnvironment;
    private TextView text_single_nonac, text_single_ac, text_double_nonac, text_double_ac, area_single_room, area_double_room, gender, locality, textViewRating, textViewTotalRating, totalViews;
    private ImageView imageHead;
    private ProgressBar progressBar;
    private final ArrayList<String> ameTitle = new ArrayList<>();
    private final ArrayList<Integer> ameVector = new ArrayList<>();
    private double location_latitude = 3.14, x, y;
    private double location_longitude = 3.14;
    private SupportMapFragment mapFragment;
    private SharedPreferences sharedPreferences;
    private float rating, hostel_rating_food, hostel_rating_accommodation, hostel_rating_staff, hostel_rating_study;
    private int total_ratings, imageCount;
    private boolean birthSort;
    private SQLiteDatabase db_favourite;
    private Cursor cursor;
    private static final int REQUEST_CALL = 1;
    Intent callIntent;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity2);
        init();
        FeedReaderDbHelper feedReaderDbHelper = new FeedReaderDbHelper(this);
        db_favourite = feedReaderDbHelper.getWritableDatabase();

        SharedPreferences pref_default = PreferenceManager.getDefaultSharedPreferences(this);
        if (!pref_default.getBoolean("thirdTime", false)) {

            feedReaderDbHelper.onCreateOriginal(db_favourite);

            SharedPreferences.Editor editor = pref_default.edit();
            editor.putBoolean("thirdTime", true);
            editor.apply();
        }
        sharedPreferences = getSharedPreferences("personalInfo", Context.MODE_PRIVATE);
        hostel_title = getIntent().getStringExtra("hostel_title");
        hostel_rent = getIntent().getStringExtra("hostel_rent");
        dbqry = "Select * from hostel_specs where title=" + "'" + hostel_title + "'";
        dbqry = dbqry.replace(" ", "%20");
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_fragment);

        text_single_nonac = (TextView) findViewById(R.id.text_single_nonac);
        text_single_ac = (TextView) findViewById(R.id.text_single_ac);
        text_double_nonac = (TextView) findViewById(R.id.text_double_nonac);
        text_double_ac = (TextView) findViewById(R.id.text_double_ac);
        area_single_room = (TextView) findViewById(R.id.area_single_room);
        area_double_room = (TextView) findViewById(R.id.area_double_room);
        totalViews = (TextView) findViewById(R.id.total_views);
        textViewRating = (TextView) findViewById(R.id.textViewRating);
        textViewTotalRating = (TextView) findViewById(R.id.textViewTotalRating);
        ratingBarFood = (RatingBar) findViewById(R.id.ratingBarFood);
        ratingBarStaff = (RatingBar) findViewById(R.id.ratingBarStaff);
        ratingBarAccommodation = (RatingBar) findViewById(R.id.ratingBarAccommodation);
        ratingBarStudyEnvironment = (RatingBar) findViewById(R.id.ratingBarStudyEnvironment);
        gender = (TextView) findViewById(R.id.gender);
        locality = (TextView) findViewById(R.id.locality);
        imageHead = (ImageView) findViewById(R.id.imageHead);
        ImageButton imageHead1 = (ImageButton) findViewById(R.id.imageHead1);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        Button moreAmeButton = (Button) findViewById(R.id.moreAmeButton);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        ratingSubmitButton = (Button) findViewById(R.id.ratingSubmitButton);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }
        // checking whether this hostel is present in sqlite favourite table or not
        // setting rating bar if logged in
        SharedPreferences sharedPreferences = getSharedPreferences("personalInfo", Context.MODE_PRIVATE);
        if (sharedPreferences.getString("hostel1_name", "default residency").equals(hostel_title))

        {
            ratingBarFood.setRating((sharedPreferences.getInt("hostel1_food", 5)));
            ratingBarAccommodation.setRating(sharedPreferences.getInt("hostel1_accommodation", 5));
            ratingBarStaff.setRating(sharedPreferences.getInt("hostel1_staffbehaviour", 5));
            ratingBarStudyEnvironment.setRating(sharedPreferences.getInt("hostel1_studyenvironment", 5));
            ratingSubmitButton.setText("Edit");
            ratingBarFood.setIsIndicator(true);
            ratingBarAccommodation.setIsIndicator(true);
            ratingBarStaff.setIsIndicator(true);
            ratingBarStudyEnvironment.setIsIndicator(true);
        }
        if (sharedPreferences.getString("hostel2_name", "default residency").equals(hostel_title)) {
            ratingBarFood.setRating(sharedPreferences.getInt("hostel2_food", 5));
            ratingBarAccommodation.setRating(sharedPreferences.getInt("hostel2_accommodation", 5));
            ratingBarStaff.setRating(sharedPreferences.getInt("hostel2_staffbehaviour", 5));
            ratingBarStudyEnvironment.setRating(sharedPreferences.getInt("hostel2_studyenvironment", 5));
            ratingSubmitButton.setText("Edit");
            ratingBarFood.setIsIndicator(true);
            ratingBarAccommodation.setIsIndicator(true);
            ratingBarStaff.setIsIndicator(true);
            ratingBarStudyEnvironment.setIsIndicator(true);
        }

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        textViewRating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle=new Bundle();
                bundle.putFloat("hostel_rating_food",hostel_rating_food);
                bundle.putFloat("hostel_rating_accommodation",hostel_rating_accommodation);
                bundle.putFloat("hostel_rating_study",hostel_rating_study);
                bundle.putFloat("hostel_rating_staff",hostel_rating_staff);
                MyDialogFragment fragment = new MyDialogFragment();
                fragment.setArguments(bundle);
                fragment.show(getSupportFragmentManager(),"fragment Dialog");
            }
        });

        ImageView imageHead = (ImageView) findViewById(R.id.imageHead);
        imageHead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Activity2.this, MainActivity_third.class);
                intent.putExtra("hostel_title", hostel_title);
                intent.putExtra("imageCount", imageCount);
                Activity2.this.startActivity(intent);
            }
        });
        imageHead1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Activity2.this, MainActivity_third.class);
                intent.putExtra("hostel_title", hostel_title);
                intent.putExtra("imageCount", imageCount);
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
                    ameTitle.add(3, "Run By Owner");
                    ameTitle.add(4, "Evening Snacks");
                    ameTitle.add(5, "Daily Cleaning");
                    ameTitle.add(6, "Water Purifier(RO)");
                    ameTitle.add(7, "Laundry");
                    ameTitle.add(8, "Security Guard");
                    ameTitle.add(9, "Geyser");
                    ameTitle.add(10, "Study Table");
                }


                while (ameVector.size() < 11) {

                    if ("1".equalsIgnoreCase(CCTV)) {
                        ameVector.add(0, R.drawable.ic_cctv);
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

    private void init() {
        FloatingActionButton mCallButton = (FloatingActionButton) findViewById(R.id.callOwner);
        mCallButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (primary_contact != null) {

                    callIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + primary_contact));
                } else {
                    callIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + secondary_contact));
                }
                if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(Activity2.this, new String[]{android.Manifest.permission.CALL_PHONE}, REQUEST_CALL);
                } else {
                    startActivity(callIntent);
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CALL: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startActivity(callIntent);
                } else {
                    ////
                }
            }
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        hostel_title = savedInstanceState.getString("hostel_title");

    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putString("hostel_title", hostel_title);
    }


    private void fetch_details() {

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest("http://flatlet.in/webservices/completeHostelData.jsp?dbqry=" + dbqry, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                new ParsingResponseTask().execute(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        RequestQueue requestqueue = Volley.newRequestQueue(getApplicationContext());
        requestqueue.add(jsonObjectRequest);
        String myRequestTag = "MyTag";
        jsonObjectRequest.setTag(myRequestTag);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        LatLng place = new LatLng(location_latitude, location_longitude);
        googleMap.addMarker(new MarkerOptions().position(place)
                .title(hostel_title));

        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        googleMap.setBuildingsEnabled(true);
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(place));
        googleMap.moveCamera(CameraUpdateFactory.zoomTo(15f));


    }

    public void onSubmitRatingButton(View view) {

        if (MySingleton.getInstance(getApplicationContext()).isOnline()) {


            if (ratingSubmitButton.getText().toString().equalsIgnoreCase("Edit")) {
                ratingBarFood.setIsIndicator(false);
                ratingBarAccommodation.setIsIndicator(false);
                ratingBarStaff.setIsIndicator(false);
                ratingBarStudyEnvironment.setIsIndicator(false);
                ratingSubmitButton.setText("SUBMIT");
                return;
            }
            int rating_food = (int) ratingBarFood.getRating();
            int rating_staff = (int) ratingBarStaff.getRating();
            int rating_accommodation = (int) ratingBarAccommodation.getRating();
            int rating_studyEnvironment = (int) ratingBarStudyEnvironment.getRating();
            float rating_final = (float) (rating_food + rating_staff + rating_accommodation + rating_studyEnvironment) / 4;


            // saving the new data in sharedpreferences
            SharedPreferences.Editor editor = sharedPreferences.edit();
            if (sharedPreferences.getString("hostel2_name", "default residency").equals(hostel_title)) {
                ratingSubmitButton.setText("Edit");

                hostel_rating_food = ((hostel_rating_food * total_ratings) - (sharedPreferences.getInt("hostel2_food", 5) - rating_food)) / total_ratings;
                hostel_rating_staff = ((hostel_rating_staff * total_ratings) - (sharedPreferences.getInt("hostel2_staffbehaviour", 5) - rating_staff)) / total_ratings;
                hostel_rating_accommodation = ((hostel_rating_accommodation * total_ratings) - (sharedPreferences.getInt("hostel2_accommodation", 5) - rating_accommodation)) / total_ratings;
                hostel_rating_study = ((hostel_rating_study * total_ratings) - (sharedPreferences.getInt("hostel2_studyenvironment", 5) - rating_studyEnvironment)) / total_ratings;
                rating = hostel_rating_food + hostel_rating_staff + hostel_rating_accommodation + hostel_rating_study / 4;

                editor.putInt("hostel2_food", rating_food);
                editor.putInt("hostel2_accommodation", rating_accommodation);
                editor.putInt("hostel2_staffbehaviour", rating_staff);
                editor.putInt("hostel2_studyenvironment", rating_studyEnvironment);
                editor.putFloat("hostel2_ratingFinal", rating_final);
                editor.apply();

                // sending rating to user database
                String dbqry = "UPDATE `our_users` SET `hostel2_name`='" + hostel_title + "',`hostel2_food`='" + rating_food + "',`hostel2_accommodation`='" + rating_accommodation + "',`hostel2_staffbehaviour`='" + rating_staff + "',`hostel2_studyenvironment`='" + rating_studyEnvironment + "'" +
                        " WHERE `user_mobile`='" + sharedPreferences.getString("userMobile", "could not fetch") + "'";
                String dbqry1 = "UPDATE `hostel_specs` SET `rating`='" + rating + "',`total_ratings`='" + total_ratings + "',`rating_food`='" + hostel_rating_food + "',`rating_accommodation`='" + hostel_rating_accommodation + "',`rating_staff`='" + hostel_rating_staff + "',`rating_study`='" + hostel_rating_study + "'" + "WHERE `title`='" + hostel_title + "'";

                String url = "http://flatlet.in/webservices/flatletsubmitbutton.jsp?dbqry=" + dbqry + "&dbqry1=" + dbqry1;


                String urlFinal = url.replace(" ", "%20");

                StringRequest stringRequest = new StringRequest(Request.Method.GET, urlFinal, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {


                    }
                });
                stringRequest.setTag("MyTag2");
                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                requestQueue.add(stringRequest);
                /*MySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);*/

            } else if (sharedPreferences.getString("hostel1_name", "default residency").equals(hostel_title)) {

                ratingSubmitButton.setText("Edit");
                hostel_rating_food = ((hostel_rating_food * total_ratings) - (sharedPreferences.getInt("hostel1_food", 5) - rating_food)) / total_ratings;

                hostel_rating_staff = ((hostel_rating_staff * total_ratings) - (sharedPreferences.getInt("hostel1_staffbehaviour", 5) - rating_staff)) / total_ratings;

                hostel_rating_accommodation = ((hostel_rating_accommodation * total_ratings) - (sharedPreferences.getInt("hostel1_accommodation", 5) - rating_accommodation)) / total_ratings;

                hostel_rating_study = ((hostel_rating_study * total_ratings) - (sharedPreferences.getInt("hostel1_studyenvironment", 5) - rating_studyEnvironment)) / total_ratings;

                rating = (hostel_rating_food + hostel_rating_staff + hostel_rating_accommodation + hostel_rating_study) / 4;


                editor.putInt("hostel1_food", rating_food);
                editor.putInt("hostel1_accommodation", rating_accommodation);
                editor.putInt("hostel1_staffbehaviour", rating_staff);
                editor.putInt("hostel1_studyenvironment", rating_studyEnvironment);
                editor.putFloat("hostel1_ratingFinal", rating_final);
                editor.apply();


                // sending rating to user database
                String dbqry = "UPDATE `our_users` SET `hostel1_name`='" + hostel_title + "',`hostel1_food`='" + rating_food + "',`hostel1_accommodation`='" + rating_accommodation + "',`hostel1_staffbehaviour`='" + rating_staff + "',`hostel1_studyenvironment`='" + rating_studyEnvironment + "'" +
                        " WHERE `user_mobile`='" + sharedPreferences.getString("userMobile", "could not fetch") + "'";
                String dbqry1 = "UPDATE `hostel_specs` SET `rating`='" + rating + "',`total_ratings`='" + total_ratings + "',`rating_food`='" + hostel_rating_food + "',`rating_accommodation`='" + hostel_rating_accommodation + "',`rating_staff`='" + hostel_rating_staff + "',`rating_study`='" + hostel_rating_study + "'" + "WHERE `title`='" + hostel_title + "'";
                /*String url = "http://flatlet.in/flatletsubmitbutton/flatletsubmitbutton.jsp?dbqry=" + dbqry + "&dbqry1=" + dbqry1;*/
                String url = "http://flatlet.in/webservices/flatletsubmitbutton.jsp?dbqry=" + dbqry + "&dbqry1=" + dbqry1;


                String urlFinal = url.replace(" ", "%20");
                StringRequest stringRequest = new StringRequest(Request.Method.GET, urlFinal, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

                stringRequest.setTag("MyTag3");
                MySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
            } else if (sharedPreferences.getString("hostel1_name", "default residency").equals("default residency")
                    && sharedPreferences.getString("hostel2_name", "default residency").equals("default residency")) {
                ratingSubmitButton.setText("Edit");
                rating = ((rating * total_ratings) + rating_final) / (total_ratings + 1);
                hostel_rating_food = ((hostel_rating_food * total_ratings) + rating_food) / (total_ratings + 1);
                hostel_rating_staff = ((hostel_rating_staff * total_ratings) + rating_staff) / (total_ratings + 1);
                hostel_rating_accommodation = ((hostel_rating_accommodation * total_ratings) + rating_accommodation) / (total_ratings + 1);
                hostel_rating_study = ((hostel_rating_study * total_ratings) + rating_studyEnvironment) / (total_ratings + 1);
                total_ratings = (total_ratings + 1);

                editor.putString("hostel1_name", hostel_title);
                editor.putInt("hostel1_food", rating_food);
                editor.putInt("hostel1_accommodation", rating_accommodation);
                editor.putInt("hostel1_staffbehaviour", rating_staff);
                editor.putInt("hostel1_studyenvironment", rating_studyEnvironment);
                editor.putFloat("hostel1_ratingFinal", rating_final);
                editor.apply();


                // sending rating to user database
                String dbqry = "UPDATE `our_users` SET `hostel1_name`='" + hostel_title + "',`hostel1_food`='" + rating_food + "',`hostel1_accommodation`='" + rating_accommodation + "',`hostel1_staffbehaviour`='" + rating_staff + "',`hostel1_studyenvironment`='" + rating_studyEnvironment + "'" +
                        " WHERE `user_mobile`='" + sharedPreferences.getString("userMobile", "could not fetch") + "'";
                String dbqry1 = "UPDATE `hostel_specs` SET `rating`='" + rating + "',`total_ratings`='" + total_ratings + "',`rating_food`='" + hostel_rating_food + "',`rating_accommodation`='" + hostel_rating_accommodation + "',`rating_staff`='" + hostel_rating_staff + "',`rating_study`='" + hostel_rating_study + "'" + "WHERE `title`='" + hostel_title + "'";
                String url = "http://flatlet.in/webservices/flatletsubmitbutton.jsp?dbqry=" + dbqry + "&dbqry1=" + dbqry1;


                String urlFinal = url.replace(" ", "%20");
                StringRequest stringRequest = new StringRequest(Request.Method.GET, urlFinal, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {


                    }
                });

                stringRequest.setTag("MyTag4");
                MySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);

            } else if (sharedPreferences.getString("hostel2_name", "default residency").equals("default residency")) {

                ratingSubmitButton.setText("Edit");
                rating = ((rating * total_ratings) + rating_final) / (total_ratings + 1);
                hostel_rating_food = ((hostel_rating_food * total_ratings) + rating_food) / (total_ratings + 1);
                hostel_rating_staff = ((hostel_rating_staff * total_ratings) + rating_staff) / (total_ratings + 1);
                hostel_rating_accommodation = ((hostel_rating_accommodation * total_ratings) + rating_accommodation) / (total_ratings + 1);
                hostel_rating_study = ((hostel_rating_study * total_ratings) + rating_studyEnvironment) / (total_ratings + 1);
                total_ratings = (total_ratings + 1);

                editor.putString("hostel2_name", hostel_title);
                editor.putInt("hostel2_food", rating_food);
                editor.putInt("hostel2_accommodation", rating_accommodation);
                editor.putInt("hostel2_staffbehaviour", rating_staff);
                editor.putInt("hostel2_studyenvironment", rating_studyEnvironment);
                editor.putFloat("hostel2_ratingFinal", rating_final);
                editor.apply();


                // sending rating to user database
                String dbqry = "UPDATE `our_users` SET `hostel2_name`='" + hostel_title + "',`hostel2_food`='" + rating_food + "',`hostel2_accommodation`='" + rating_accommodation + "',`hostel2_staffbehaviour`='" + rating_staff + "',`hostel2_studyenvironment`='" + rating_studyEnvironment + "'" +
                        " WHERE `user_mobile`='" + sharedPreferences.getString("userMobile", "could not fetch") + "'";
                String dbqry1 = "UPDATE `hostel_specs` SET `rating`='" + rating + "',`total_ratings`='" + total_ratings + "',`rating_food`='" + hostel_rating_food + "',`rating_accommodation`='" + hostel_rating_accommodation + "',`rating_staff`='" + hostel_rating_staff + "',`rating_study`='" + hostel_rating_study + "'" + "WHERE `title`='" + hostel_title + "'";
                /*String url = "http://flatlet.in/flatletsubmitbutton/flatletsubmitbutton.jsp?dbqry=" + dbqry + "&dbqry1=" + dbqry1;*/
                String url = "http://flatlet.in/webservices/flatletsubmitbutton.jsp?dbqry=" + dbqry + "&dbqry1=" + dbqry1;

                String urlFinal = url.replace(" ", "%20");
                StringRequest stringRequest = new StringRequest(Request.Method.GET, urlFinal, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {


                    }
                });

                stringRequest.setTag("MyTag5");
                MySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);

            } else {
                Toast.makeText(getApplicationContext(), "you can not review more than 2 hostels", Toast.LENGTH_SHORT).show();
            }


            // setting rating bar as indicator
            ratingBarFood.setIsIndicator(true);
            ratingBarAccommodation.setIsIndicator(true);
            ratingBarStaff.setIsIndicator(true);
            ratingBarStudyEnvironment.setIsIndicator(true);


        } else {
            Toast.makeText(getApplicationContext(), "No Internet Connection ! Please Try Again", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.activity2_action, menu);
        MenuItem menuItem = menu.findItem(R.id.favouriteactionbutton);
        String selection = FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE + " = ?";
        String[] projection = {FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE};
        String[] selectionArgs = {hostel_title};
        cursor = db_favourite.query(FeedReaderContract.FeedEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, null);



        cursor.moveToNext();
        if (cursor.getCount() != 0 && cursor.getString(0).equalsIgnoreCase(hostel_title)) {

            menuItem.setIcon(R.drawable.ic_favorite_red_24dp);
            birthSort = false;

        } else {
            menuItem.setIcon(R.drawable.ic_favorite_white_24dp);
            birthSort = true;
        }
        cursor.close();
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        SharedPreferences sharedPreferences = getSharedPreferences("personalInfo", Context.MODE_PRIVATE);
        String dbqry;
        switch (item.getItemId()) {
            case R.id.favouriteactionbutton:
                if (birthSort) {

                    item.setIcon(R.drawable.ic_favorite_red_24dp);
                    ContentValues values = new ContentValues();
                    values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE, hostel_title);
                    values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_SECONDARY_ADDRESS, locality.getText().toString());
                    values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_RENT, hostel_rent);
                    values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_RATING, textViewRating.getText().toString());
                    values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_IMG_URL, "http://images.flatlet.in/images_thumbs/1/1.jpg");
                    db_favourite.insert(FeedReaderContract.FeedEntry.TABLE_NAME, null, values);
                    dbqry = "INSERT INTO `user_favourites`( `title`, `user_mobile`, `secondary_address`, `rent`, `img_url`, `rating`) VALUES ('" + hostel_title + "'" +
                            ",'" + sharedPreferences.getString("userMobile", "911") + "','" + locality.getText() + "','" + hostel_rent + "'," +
                            "'http://images.flatlet.in/images_thumbs/1/1.jpg','" + textViewRating.getText() + "')";

                    // checking the size of sqlite database
                    String[] projection1 = {
                            FeedReaderContract.FeedEntry._ID,
                            FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE,
                    };

                    cursor = db_favourite.query(FeedReaderContract.FeedEntry.TABLE_NAME, projection1, null, null, null, null, null);
                    int i = cursor.getCount();


                    Toast.makeText(this, "Added to favourites", Toast.LENGTH_SHORT).show();
                    birthSort = false;

                } else {
                    item.setIcon(R.drawable.ic_favorite_white_24dp);
                    db_favourite.delete(FeedReaderContract.FeedEntry.TABLE_NAME, FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE + " = ?", new String[]{hostel_title});
                    dbqry = "DELETE FROM `user_favourites` WHERE `title`='" + hostel_title + "'  AND" +
                            " `user_mobile`='" + sharedPreferences.getString("userMobile", "911") + "'";
                    birthSort = true;

                }


                String url = "http://flatlet.in/webservices/flatletuserinsert.jsp?dbqry=" + dbqry;
                String urlFinal = url.replace(" ", "%20");

                StringRequest stringRequest = new StringRequest(Request.Method.GET, urlFinal,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                // Display the first 500 characters of the response string.

                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                requestQueue.add(stringRequest);
                return true;
            case R.id.report:
                Intent intent = new Intent (Intent.ACTION_VIEW , Uri.parse("mailto:" + "flatletindia@gmail.com"));
                intent.putExtra(Intent.EXTRA_SUBJECT, "your_subject");
                intent.putExtra(Intent.EXTRA_TEXT, "your_text "+hostel_title);
                startActivity(intent);
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private class ParsingResponseTask extends AsyncTask<JSONObject, Void, Void> {


        @Override
        protected Void doInBackground(JSONObject... response) {
            try {
                primary_contact = response[0].getString("contact_primary");
                secondary_contact = response[0].getString("contact_secondary");
                location_latitude = response[0].getDouble("location_latitude");
                location_longitude = response[0].getDouble("location_longitude");
                rent_single_nonac = response[0].getString("rent_single_nonac");
                rent_single_ac = response[0].getString("rent_single_ac");
                rent_double_nonac = response[0].getString("rent_double_nonac");
                rent_double_ac = response[0].getString("rent_double_ac");
                x = response[0].getInt("dim_single_length") * response[0].getInt("dim_single_width") * 0.001;
                y = response[0].getInt("dim_double_length") * response[0].getInt("dim_double_width") * 0.001;
                gender1 = response[0].getString("gender");
                address_secondary = response[0].getString("address_secondary");
                totalViews1 = String.valueOf(response[0].getInt("totalViews"));
                title = response[0].getString("title");
                CCTV = response[0].getString("CCTV");
                ame_toilet_attached = response[0].getString("ame_toilet_attached");
                ame_elevator = response[0].getString("ame_elevator");
                eve_snacks = response[0].getString("eve_snacks");
                ownership = response[0].getString("ownership");
                rating = (float) response[0].getDouble("rating");
                hostel_rating_accommodation = (float) response[0].getDouble("rating_accommodation");
                hostel_rating_food = (float) response[0].getDouble("rating_food");
                hostel_rating_staff = (float) response[0].getDouble("rating_staff");
                hostel_rating_study = (float) response[0].getDouble("rating_study");
                imageCount = Integer.parseInt(response[0].getString("ImgCount"));
                total_ratings = response[0].getInt("total_ratings");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Picasso.with(Activity2.this).load("http://images.flatlet.in/images/" + hostel_title.replace(" ", "%20") + "/Thumb/1.webp").resize(400, 300).centerCrop().into(imageHead);
            text_single_nonac.setText(rent_single_nonac);
            text_single_ac.setText(rent_single_ac);
            text_double_nonac.setText(rent_double_nonac);
            text_double_ac.setText(rent_double_ac);
            area_single_room.setText(String.valueOf((int) x));
            area_double_room.setText(String.valueOf((int) y));
            gender.setText(gender1);
            locality.setText(address_secondary);
            totalViews.setText(totalViews1);
            toolbar.setTitle(title);
            textViewRating.setText(String.format(java.util.Locale.US, "%.1f", rating));
            textViewTotalRating.setText(String.valueOf(total_ratings));
            mapFragment.getMapAsync(Activity2.this);
            imageHead.setAlpha(0.9f);
            progressBar.setVisibility(View.GONE);
        }
    }

    /*private class sendToDatabaseTask extends AsyncTask<Void,Void,Void>{
        @Override
        protected Void doInBackground(Void... params) {
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }*/
}

