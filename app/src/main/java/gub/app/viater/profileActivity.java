package gub.app.viater;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class profileActivity extends AppCompatActivity {


    TextView id,name,mobile,verify;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        name=findViewById(R.id.UserName_ID);
        mobile=findViewById(R.id.phoneNumber);



        SharedPreferences sharedPreferences = getSharedPreferences("meDB", Context.MODE_PRIVATE);
        String Uid = sharedPreferences.getString("id", "");
        String userName = sharedPreferences.getString("UserName", "");
        String U_mobile = sharedPreferences.getString("mobile", "");
        String Vv=sharedPreferences.getString("vv", "");



        name.setText("Name : "+userName);
        mobile.setText("Mobile : " +U_mobile);






    }





}