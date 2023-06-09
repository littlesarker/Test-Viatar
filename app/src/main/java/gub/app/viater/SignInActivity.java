package gub.app.viater;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class SignInActivity extends AppCompatActivity {

    EditText pass,mobilenumber;
    TextView textView;
    Button signinBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        pass=findViewById(R.id.passID);
        mobilenumber=findViewById(R.id.numberID);
        textView=findViewById(R.id.createAcID);
        signinBtn=findViewById(R.id.signINBtn);



       textView.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent is=new Intent(getApplicationContext(),SignUpActivity.class);
               startActivity(is);
           }
       });




    }


}