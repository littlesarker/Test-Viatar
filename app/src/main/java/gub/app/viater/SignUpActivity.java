package gub.app.viater;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
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

import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {


    TextView textView;
    Button signupBtn;
    public EditText name, mobile, password;

    public ProgressBar progressBar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }


        name = findViewById(R.id.nameID);
        mobile = findViewById(R.id.MobileNumID);
        password = findViewById(R.id.upPassID);
        signupBtn = findViewById(R.id.signUpBtn);
        textView = findViewById(R.id.existID);
        progressBar = findViewById(R.id.progress_barID);


        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (validate() == true) {

                    //Send data to api
                    Send_Data();

                    //Empty input
                    name.setText("");
                    mobile.setText("");
                    password.setText("");
                    startProgress();

                }

            }
        });


        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent innt = new Intent(getApplicationContext(), SignInActivity.class);
                startActivity(innt);
            }
        });

    }

    public void Send_Data() {


        String S_name = name.getText().toString();
        String S_mobile = mobile.getText().toString();
        String S_pass = password.getText().toString();


        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://viater.vercel.app/api/user/signup";


        StringRequest postRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("Response", response);
                Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();


            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("ERROR", "error => " + error.toString());

                if (error.networkResponse != null) {
                    int statusCode = error.networkResponse.statusCode;
                    // Handle different status codes
                    switch (statusCode) {
                        case 200:
                            //The request was successful, and the server has returned the requested data
                            Toast.makeText(getApplicationContext(), "Account Created", Toast.LENGTH_LONG).show();
                            break;

                        case 400:
                            // Bad Request

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
                params.put("full_name", S_name);
                params.put("mobile_number", S_mobile);
                params.put("password", S_pass);
                params.put("role", "USER");

                return params;
            }
        };

        queue.add(postRequest);

        Intent i = new Intent(SignUpActivity.this, OtpActivity.class);
        i.putExtra("message_key", S_mobile);
        startActivity(i);



    }


    public boolean validate() {

        boolean f1 = false, f2 = false, f3 = false, f4 = false, f7 = false;

        String mobileNumber = mobile.getText().toString();

        if (mobileNumber.equals("")) {
            mobile.setError("Enter Mobile number");
            mobile.requestFocus();
        } else {
            f1 = true;
        }
        if (!mobileNumber.matches("^(?:\\+88|88)?(01[3-9]\\d{8})$")) {
            mobile.setError("Plz Enter valid Mobile Number ");
            mobile.requestFocus();
        } else {
            f2 = true;
        }

        if (password.getText().toString().length() < 6) {
            password.setError("password minimum contain 6 character");
            password.requestFocus();
        } else {
            f3 = true;
        }

        if (password.getText().toString().equals("")) {
            password.setError("please enter password");
            password.requestFocus();
        } else {
            f4 = true;
        }


        String nam = name.getText().toString();
        if (nam.equals("")) {
            name.setError("Enter Your name ");
            name.requestFocus();
        } else {
            f7 = true;
        }


        if (f1 & f2 & f3 & f4 & f7 == true)
            return true;
        else
            return false;

    }

    private void startProgress() {
        progressBar.setVisibility(View.VISIBLE);
        signupBtn.setEnabled(false);

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
                        signupBtn.setEnabled(true);
                    }
                });
            }
        });

        thread.start();
    }


}