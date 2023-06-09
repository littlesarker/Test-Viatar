package gub.app.viater;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.net.HttpCookie;
import java.util.HashMap;
import java.util.Map;

public class SignInActivity extends AppCompatActivity {

    EditText pass, mobilenumber;
    TextView textView;
    Button signinBtn;
    ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        pass = findViewById(R.id.passID);
        mobilenumber = findViewById(R.id.numberID);
        textView = findViewById(R.id.createAcID);
        signinBtn = findViewById(R.id.signINBtn);
        progressBar = findViewById(R.id.progress_bar);


        signinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validation() == true) {

                    SendData();
                    mobilenumber.setText("");
                    pass.setText("");
                    startProgress();


                }

            }
        });


        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent is = new Intent(getApplicationContext(), SignUpActivity.class);
                startActivity(is);
            }
        });


    }

    public void SendData() {


        String mobile_S = mobilenumber.getText().toString();
        String pass_S = pass.getText().toString();

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://viater.vercel.app/api/user/signin";


        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Response", response);
                        Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();

                        Intent i = new Intent(SignInActivity.this, HomeActivity.class);
                        startActivity(i);
                        finish();

                        SharedPreferences sharedPreferences = getSharedPreferences("my_preferences", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("username", mobile_S);
                        editor.putString("password", pass_S);
                        editor.apply();


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
                                    Toast.makeText(getApplicationContext(), "successful", Toast.LENGTH_LONG).show();
                                    break;
                                case 400:
                                    // Bad Request
                                    Toast.makeText(getApplicationContext(), "No User Found /Bad Request", Toast.LENGTH_LONG).show();
                                    break;
                                case 401:
                                    // Unauthorized
                                    Toast.makeText(getApplicationContext(), "Unauthorized", Toast.LENGTH_LONG).show();
                                    break;
                                case 404:
                                    // Not Found
                                    Toast.makeText(getApplicationContext(), "not Found", Toast.LENGTH_LONG).show();
                                    break;
                                // Handle other status codes as needed
                            }
                        }

                    }
                }
        ) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("mobile_number", mobile_S);
                params.put("password", pass_S);

                return params;
            }

            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                // since we don't know which of the two underlying network vehicles
                // will Volley use, we have to handle and store session cookies manually
                Log.i("response",response.headers.toString());
                Map<String, String> responseHeaders = response.headers;
                String rawCookies = responseHeaders.get("Set-Cookie");

                SharedPreferences sharedPreferences = getSharedPreferences("my_preferences", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("csk", rawCookies);
                editor.apply();


                return super.parseNetworkResponse(response);
            }
        };

        queue.add(postRequest);

    }

    public boolean validation() {
        boolean f1 = false, f2 = false, f3 = false, f4 = false;
        String mm = mobilenumber.getText().toString();

        if (mm.equals("")) {
            mobilenumber.setError("Enter Mobile number");
            mobilenumber.requestFocus();
        } else {
            f1 = true;
        }
        if (!mm.matches("^(?:\\+88|88)?(01[3-9]\\d{8})$")) {
            mobilenumber.setError("Plz Enter valid Mobile Number ");
            mobilenumber.requestFocus();
        } else {
            f2 = true;
        }

        if (pass.getText().toString().length() < 6) {
            pass.setError("password minimum contain 6 character");
            pass.requestFocus();
        } else {
            f3 = true;
        }

        if (pass.getText().toString().equals("")) {
            pass.setError("please enter password");
            pass.requestFocus();
        } else {
            f4 = true;
        }
        if (f1 & f2 & f3 & f4 == true)
            return true;
        else
            return false;
    }

    private void startProgress() {
        progressBar.setVisibility(View.VISIBLE);
        signinBtn.setEnabled(false);

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i <= 50; i++) {
                    final int progress = i;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setProgress(progress);
                        }
                    });

                    try {
                        Thread.sleep(50); // Simulate work
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                        signinBtn.setEnabled(true);
                    }
                });
            }
        });

        thread.start();
    }


}