package io.lmh.e.a3cs_akings.SignUp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import io.lmh.e.a3cs_akings.R;

public class PhoneAuthorization extends AppCompatActivity {
    //
    EditText phonecode;
    String phoneCodeStr,phoneNumber;

    //fireb



    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_authorization);

        phonecode=(EditText)findViewById(R.id.phone_auth_code);

        intent=getIntent();
        phoneNumber=intent.getStringExtra("phonenumber");



    }

    public void onVerifyPhone(View view) {
    }

    public void onResendCode(View view) {
    }
}
