package io.lmh.e.a3cs_akings;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import io.lmh.e.a3cs_akings.Login.Login;
import io.lmh.e.a3cs_akings.SignUp.SignUp;

public class MainActivity extends AppCompatActivity {
     String strAccount="",strInfo="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences sharedPreferences = getSharedPreferences("accountInfo", Context.MODE_PRIVATE);
        strAccount=sharedPreferences.getString("USERID","");
        strInfo=sharedPreferences.getString("USERName","");
        if(!(strAccount.equals("")&&strInfo.equals(""))){
            Intent intent=new Intent(getApplicationContext(), MainTask.class);
            startActivity(intent);
            MainActivity.this.finish();
        }
    }

    public void Login(View view) {
        Intent intent=new Intent(getApplicationContext(), Login.class);
        startActivity(intent);
    }

    public void signUp(View view) {
        Intent intent=new Intent(getApplicationContext(), SignUp.class);
        startActivity(intent);
    }
}
