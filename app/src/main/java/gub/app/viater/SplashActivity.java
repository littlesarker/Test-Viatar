package gub.app.viater;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

public class SplashActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // on below line we are
                // creating a new intent

                SharedPreferences sharedPreferences = getSharedPreferences("my_preferences", Context.MODE_PRIVATE);
                String username = sharedPreferences.getString("username", "");
                String password = sharedPreferences.getString("password", "");

                if (!username.isEmpty() && !password.isEmpty()) {
                    // The user has already logged in. Go to the home activity.
                    Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                    startActivity(intent);
                } else {
                    // The user has not logged in yet. Go to the login activity.
                    Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
                    startActivity(intent);
                }

                // on the below line we are finishing
                // our current activity.
                finish();
            }
        }, 2500);





    }
}