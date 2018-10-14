package io.lmh.e.a3cs_akings.Login;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import io.lmh.e.a3cs_akings.MainTask;
import io.lmh.e.a3cs_akings.R;
import io.lmh.e.a3cs_akings.Static.FunctionsStatic;
import io.lmh.e.a3cs_akings.Static.VarStatic;

public class Login extends AppCompatActivity {
    //declare vars
    String userName,userPassword;
    //declare ui vars
    EditText name,password;
    TextView status;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //initialize ui vars
        status=(TextView)findViewById(R.id.tv_login_status);
        name=(EditText)findViewById(R.id.edt_login_user_name);
        password=(EditText)findViewById(R.id.edt_login_password);
    }

    public void onLogin(View view) {
        userName=name.getText().toString();
        userPassword=password.getText().toString();
        new LoginAccouontsAsyn().execute();

    }

    public void onLoginClear(View view) {
        clearForm();
    }

    private void clearForm() {
        status.setText("");
        name.setText("");
        password.setText("");
    }

    //asyn login task
    private class LoginAccouontsAsyn extends AsyncTask<String, String, String> {
        HttpURLConnection conn;
        ProgressDialog progressDialog;
        String ans = "";
        Boolean networkError=false;


        @Override
        protected void onPreExecute() {

            super.onPreExecute();
            progressDialog = new ProgressDialog(Login.this);
            progressDialog.setTitle("Logging in");
            progressDialog.setMessage("please wait..");
            progressDialog.show();

        }

        @Override
        protected void onPostExecute(String s) {
            System.out.println("login return:"+ans);
            progressDialog.hide();
            if(!networkError){
            if(s.equals("nameerror")){
                status.setTextColor(Color.RED);
                status.setText("User name does not exist");
            }
            if(s.equals("namepasserror")){
                status.setTextColor(Color.RED);
                status.setText("User name and password does not match");
            }
            if(!s.equals("nameerror")&&!s.equals("namepasserror")){
                status.setTextColor(Color.GREEN);
                status.setText("Logged in");
                SharedPreferences sharedPrefs;
                SharedPreferences.Editor editor;
                sharedPrefs=getSharedPreferences("accountInfo", Context.MODE_PRIVATE);
                editor=sharedPrefs.edit();
                String accId=s,name=userName;
                //accId= FunctionsStatic.decode64(accId);
                //name=FunctionsStatic.decode64(name);
                editor.putString("USERID",accId);
                editor.putString("USERName",name);
                editor.commit();
                Intent intent=new Intent(getApplicationContext(), MainTask.class);
                startActivity(intent);
                finish();

            }}else {
                status.setTextColor(Color.RED);
                status.setText("Sorry Network Error");
            }

        }

        @Override
        protected String doInBackground(String... params) {



            URL url = null;
            try {
                url = new URL(VarStatic.getHostName()+"/account/login.php?name="+ URLEncoder.encode(userName)+"&password="+URLEncoder.encode(userPassword));
                conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(6000);
                conn.connect();
                InputStream in = null;
                in = conn.getInputStream();
                InputStreamReader inReader = new InputStreamReader(in);

                BufferedReader br = new BufferedReader(inReader);

                String s = null;
                while ((s = br.readLine()) != null) {
                    ans=s;
                    System.out.println(s);


                }

                System.out.println("Success Connection");
                conn.disconnect();
                System.out.println("returned is"+ans);
                return ans;
            } catch (MalformedURLException e) {
                e.printStackTrace();
                networkError=true;
            } catch (IOException e) {
                e.printStackTrace();
                networkError=true;
            }
            return ans;
        }
    }


}
