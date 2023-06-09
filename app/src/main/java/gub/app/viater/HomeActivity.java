package gub.app.viater;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.icu.text.SimpleDateFormat;
import android.icu.util.TimeZone;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.CookieManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpClientStack;
import com.android.volley.toolbox.HttpStack;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.CookieStore;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import gub.app.viater.databinding.ActivityHomeBinding;


public class HomeActivity extends AppCompatActivity implements OnMapReadyCallback {


    private GoogleMap mMap;
    private LatLng mOrigin;
    private LatLng mDestination;
    private Polyline mPolyline;
    ArrayList<LatLng> mMarkerPoints;

    private Calendar calendar;
    private Button requestBtn;
    private EditText infoText, priceInput;
    private TextView res, MyName;
    ImageView notify, profile;

    private int mYear, mMonth, mDay, mHour, mMinute;
    private String date,time,ff;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //Finding Id for view
        MyName = findViewById(R.id.UsernameID);
        profile = findViewById(R.id.profileID);
        notify = findViewById(R.id.notificationID);
        requestBtn = findViewById(R.id.requestBtnID);
        infoText = findViewById(R.id.additionalInfoID);
        priceInput = findViewById(R.id.priceID);
        res = findViewById(R.id.date_TimeID);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation1);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.MY_MAP);

        mapFragment.getMapAsync(this);
        mMarkerPoints = new ArrayList<>();
        calendar = Calendar.getInstance();


        db();
        GPS();

        res.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showDateTimePicker();

            }
        });


        requestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!res.getText().toString().isEmpty() && !priceInput.getText().toString().isEmpty() && !infoText.getText().toString().isEmpty()) {
                    RideRequest();
                } else {
                    Toast.makeText(getApplicationContext(), "Fields Are Empty", Toast.LENGTH_SHORT).show();
                }

            }
        });


        notify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), NotificationActivity.class);
                startActivity(intent);
            }
        });


        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), profileActivity.class);
                startActivity(intent);
            }
        });

        // Set Home selected
        bottomNavigationView.setSelectedItemId(R.id.home);
        // Perform item selected listener
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.biding:
                        startActivity(new Intent(getApplicationContext(), BidingActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.home:

                        return true;
                    case R.id.settings:
                        startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                }
                return false;
            }
        });

    }

    private void showDateTimePicker() {
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                         date=(year + "-" + (monthOfYear+1 ) + "-" + dayOfMonth)+"T";
                         Timepicker();

                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();


        // Get Current Time

    }
    private void Timepicker(){
       final Calendar cc = Calendar.getInstance();
        mHour = cc.get(Calendar.HOUR_OF_DAY);
        mMinute = cc.get(Calendar.MINUTE);

        // Launch Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {

            @Override
            public void onTimeSet(TimePicker view, int hourOfDay,int minute) {

                 time=(hourOfDay + ":" + minute+":"+"00.619Z");

                 ff=date+time;

                 res.setText(ff);

            }
        }, mHour, mMinute, false);
        timePickerDialog.show();


    }


    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng point) {
                // Already two locations
                if (mMarkerPoints.size() > 1) {
                    mMarkerPoints.clear();
                    mMap.clear();
                }

                // Adding new item to the ArrayList
                mMarkerPoints.add(point);

                // Creating MarkerOptions
                MarkerOptions options = new MarkerOptions();

                // Setting the position of the marker
                options.position(point);

                /**
                 * For the start location, the color of marker is GREEN and
                 * for the end location, the color of marker is RED.
                 */
                if (mMarkerPoints.size() == 1) {
                    options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));


                } else if (mMarkerPoints.size() == 2) {
                    options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                }

                // Add new marker to the Google Map Android API V2
                mMap.addMarker(options);

                // Checks, whether start and end locations are captured
                if (mMarkerPoints.size() >= 2) {
                    mOrigin = mMarkerPoints.get(0);
                    mDestination = mMarkerPoints.get(1);

                    drawRoute();
                }

            }
        });

    }

    private void drawRoute() {

        // Getting URL to the Google Directions API
        String url = getDirectionsUrl(mOrigin, mDestination);

        DownloadTask downloadTask = new DownloadTask();

        // Start downloading json data from Google Directions API
        downloadTask.execute(url);
    }

    private String getDirectionsUrl(LatLng origin, LatLng dest) {

        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;

        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;

        // Key
        String key = "key=AIzaSyCNH7S-mofbGeKMW91aQR9_ayenxDJrLXM";

        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + key;

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/json?" + parameters;

        return url;
    }

    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        } catch (Exception e) {
            Log.d("Exception on download", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    private class DownloadTask extends AsyncTask<String, Void, String> {

        // Downloading data in non-ui thread
        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service
            String data = "";

            try {
                // Fetching the data from web service
                data = downloadUrl(url[0]);
                Log.d("DownloadTask", "DownloadTask : " + data);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        // Executes in UI thread, after the execution of
        // doInBackground()
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask();

            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);
        }
    }

    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();

                // Starts parsing data
                routes = parser.parse(jObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }

        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points = null;
            PolylineOptions lineOptions = null;

            // Traversing through all the routes
            for (int i = 0; i < result.size(); i++) {
                points = new ArrayList<LatLng>();
                lineOptions = new PolylineOptions();

                // Fetching i-th route
                List<HashMap<String, String>> path = result.get(i);

                // Fetching all the points in i-th route
                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                // Adding all the points in the route to LineOptions
                lineOptions.addAll(points);
                lineOptions.width(8);
                lineOptions.color(Color.RED);
            }

            // Drawing polyline in the Google Map for the i-th route
            if (lineOptions != null) {
                if (mPolyline != null) {
                    mPolyline.remove();
                }
                mPolyline = mMap.addPolyline(lineOptions);

            } else
                Toast.makeText(getApplicationContext(), "No route is found", Toast.LENGTH_LONG).show();
        }
    }

    public void RideRequest() {

        String rfromlat = String.valueOf(mOrigin.latitude).toString();
        String rfromlng = String.valueOf(mOrigin.longitude).toString();
        String rtolat = String.valueOf(mDestination.latitude).toString();
        String rtolng = String.valueOf(mDestination.longitude).toString();

        String tDate = res.getText().toString();
        String Ubudget = priceInput.getText().toString();
        String addiInfo = infoText.getText().toString();


        String urlRide = "https://viater.vercel.app/api/order/create";

         JSONObject postData = new JSONObject();
        try {
            postData.put("from_lat", rfromlat);
            postData.put("from_lng", rfromlng);
            postData.put("to_lat", rtolat);
            postData.put("to_lng", rtolng);
            postData.put("budget", Ubudget);
            postData.put("additional_requirements", addiInfo);
            postData.put("departure_time", "2023-10-21T23:52:04.619Z");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, urlRide, postData, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Toast.makeText(getApplicationContext(), "Response: "+response, Toast.LENGTH_LONG).show();

                try {
                    String reqID = response.getJSONObject("data").getString("id");





                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        } ){
            @Override
            public Map<String, String> getHeaders()  {
                SharedPreferences sharedPreferences = getSharedPreferences("my_preferences", Context.MODE_PRIVATE);
                String ck = sharedPreferences.getString("csk", "");
                Map<String, String> pp = new HashMap<String, String>();
                pp.put("Cookie", ck);

                return pp;
            }
        };


        Volley.newRequestQueue(this).add(jsonObjectRequest);


        //Call API


    }


    public void db() {
        SharedPreferences sharedPreferences = getSharedPreferences("meDB", Context.MODE_PRIVATE);
        String id = sharedPreferences.getString("id", "");
        String userName = sharedPreferences.getString("UserName", "");

        MyName.setText(userName);

        if (!id.isEmpty() && !userName.isEmpty()) {
            // The user has already logged in. Go to the home activity.
            Toast.makeText(getApplicationContext(), "Welcome", Toast.LENGTH_SHORT).show();
        } else {
            // The user has not logged in yet. Go to the login activity.
            GetMe();
        }

    }

    public void GPS() {
        GpsTracker gpsTracker = new GpsTracker(HomeActivity.this);
        if (gpsTracker.canGetLocation()) {
            double dla = gpsTracker.getLatitude();
            double dlo = gpsTracker.getLongitude();

        } else {
            gpsTracker.showSettingsAlert();
        }
    }

    public void GetMe() {


        RequestQueue requestQueue = Volley.newRequestQueue(this);

        String myUrl = "https://viater.vercel.app/api/user/me";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, myUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.


                        try {
                            //Create a JSON object containing information from the API.
                            JSONObject myJsonObject = new JSONObject(response);

                            String ID = myJsonObject.getJSONObject("data").getString("id");
                            String NAME = myJsonObject.getJSONObject("data").getString("full_name");
                            String MOBILE = myJsonObject.getJSONObject("data").getString("mobile_number");
                            String VERIFY = myJsonObject.getJSONObject("data").getString("verified");

                            SharedPreferences sharedPreferences = getSharedPreferences("meDB", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("id", ID);
                            editor.putString("UserName", NAME);
                            editor.putString("mobile", MOBILE);
                            editor.putString("vv", VERIFY);
                            editor.apply();


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Don't get Data", Toast.LENGTH_LONG).show();
            }

        }) {
            public Map<String, String> getHeaders() {
                SharedPreferences sharedPreferences = getSharedPreferences("my_preferences", Context.MODE_PRIVATE);
                String ck = sharedPreferences.getString("csk", "");
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/json; charset=utf-8");
                params.put("Cookie", ck);
                return params;
            }
        };

        requestQueue.add(stringRequest);

    }


}