package gub.app.viater;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class OtpActivity extends AppCompatActivity {


    public EditText Editotp;
    public Button verifyBtn, reSendBtn;
    public String number, otp;
   

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);

        //Hide Action Bar
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        //
        verifyBtn = findViewById(R.id.buttonVerify);
        reSendBtn = findViewById(R.id.ResendOTP);
        Editotp = findViewById(R.id.otpID);


        Intent intent = getIntent();
        number = intent.getStringExtra("message_key");


        verifyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                otp = Editotp.getText().toString();

                vOTP();

            }
        });

        reSendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Rotp();

            }
        });


    }

    public void vOTP() {

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://viater.vercel.app/api/user/verify";

        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Response", response);
                        Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();
                         Intent intent=new Intent(getApplicationContext(),SignInActivity.class);
                         startActivity(intent);
                         finish();

                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("ERROR", "error => " + error.toString());

                        if (error.networkResponse != null) {
                            int statusCode = error.networkResponse.statusCode;
                            // Handle different status codes
                            switch (statusCode) {
                                case 200:
                                    //The request was successful, and the server has returned the requested data
                                    Toast.makeText(getApplicationContext(), "Otp verify done", Toast.LENGTH_LONG).show();
                                    break;
                                case 400:
                                    // Bad Request
                                    Toast.makeText(getApplicationContext(), "Verification code not matched", Toast.LENGTH_LONG).show();
                                    break;
                                case 401:
                                    // Unauthorized
                                    Toast.makeText(getApplicationContext(), "Unauthorized", Toast.LENGTH_LONG).show();
                                    break;
                                case 404:
                                    // Not Found
                                    Toast.makeText(getApplicationContext(), "Not Found", Toast.LENGTH_LONG).show();

                                    break;
                                // Handle other status codes as needed
                            }
                        }
                    }
                }

        ) {
            // this is the relevant method
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("mobile_number", number);
                params.put("verification_code", otp);
                return params;
            }
        };

        queue.add(postRequest);


    }

    public void Rotp() {

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://viater.vercel.app/api/user/reverify";

        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Response", response);
                        Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();

                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("ERROR", "error => " + error.toString());

                        if (error.networkResponse != null) {
                            int statusCode = error.networkResponse.statusCode;
                            // Handle different status codes
                            switch (statusCode) {
                                case 200:
                                    //The request was successful, and the server has returned the requested data
                                    Toast.makeText(getApplicationContext(), "Verification code sent successfully", Toast.LENGTH_LONG).show();
                                    break;
                                case 400:
                                    // Bad Request
                                    Toast.makeText(getApplicationContext(), "Bad Request", Toast.LENGTH_LONG).show();
                                    break;
                                case 401:
                                    // Unauthorized

                                    break;
                                case 404:
                                    // Not Found

                                    break;
                                // Handle other status codes as needed
                            }
                        }

                    }
                }


        ) {
            // this is the relevant method
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("mobile_number", number);
                return params;
            }
        };

        queue.add(postRequest);


    }


}