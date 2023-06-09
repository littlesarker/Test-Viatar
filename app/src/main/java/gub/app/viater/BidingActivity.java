package gub.app.viater;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class BidingActivity extends AppCompatActivity {


    private String RequestID,chk;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_biding);

        textView=findViewById(R.id.requestBidID);


        BottomNavigationView bottomNavigationView=findViewById(R.id.bottom_navigation2);
        // Set Home selected
        bottomNavigationView.setSelectedItemId(R.id.biding);




        chk=RequestID;
        // Perform item selected listener
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch(item.getItemId())
                {
                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(),HomeActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.biding:
                        return true;
                    case R.id.settings:
                        startActivity(new Intent(getApplicationContext(),SettingsActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });











    }
    public  void GeTBidRequest(){
        String urlRide = "https://viater.vercel.app/api/bid/get-bids";

        JSONObject postData = new JSONObject();
        try {


            postData.put("id", RequestID);


        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, urlRide, postData, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Toast.makeText(getApplicationContext(), "Response: "+response, Toast.LENGTH_LONG).show();


                try {
                    String reqID = response.getJSONObject("data").getString("id");

                     textView.setText(reqID);

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

    }


}