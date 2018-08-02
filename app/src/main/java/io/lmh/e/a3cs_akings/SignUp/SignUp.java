package io.lmh.e.a3cs_akings.SignUp;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import io.lmh.e.a3cs_akings.R;
import io.lmh.e.a3cs_akings.Static.UIStatic;
import io.lmh.e.a3cs_akings.Static.VarStatic;

public class SignUp extends AppCompatActivity {
    //initialize  vars
    String accName, accPassword, accConfirmPassword, accPhoneNo;
    //initialize ui vars
    EditText Name, Password, ConfirmPassword, PhoneNo;
    TextView tvStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        //
        tvStatus=(TextView)findViewById(R.id.tv_status);

        Name = (EditText) findViewById(R.id.edt_acc_name);
        Password = (EditText) findViewById(R.id.edt_pass);
        ConfirmPassword = (EditText) findViewById(R.id.edt_pass_confirm);
        PhoneNo = (EditText) findViewById(R.id.edt_phone_no);

    }

    public void onClear(View view) {
        clearTextViews();
    }

    private void clearTextViews() {
        Name.setText("");
        Password.setText("");
        ConfirmPassword.setText("");
        PhoneNo.setText("");
    }

    public void onCreateAccount(View view) {
        System.out.println("create acc clicked");
        accName = Name.getText().toString();
        accPassword = Password.getText().toString();
        accConfirmPassword = ConfirmPassword.getText().toString();
        accPhoneNo = PhoneNo.getText().toString();
        if (accPassword.equals(accConfirmPassword)) {
             createAccount();
        } else {
            tvStatus.setText("Confirm password correctly");
            tvStatus.setTextColor(Color.RED);
        }

    }

    private void createAccount() {
        new SignUpAccouontsAsyn().execute();
    }
    //Sign up Network class

    private class SignUpAccouontsAsyn extends AsyncTask<String, String, String> {
        HttpURLConnection conn;
        ProgressDialog progressDialog;
        String ans = "";


        @Override
        protected void onPreExecute() {

            super.onPreExecute();
            progressDialog = new ProgressDialog(SignUp.this);
            progressDialog.setTitle("creating an account");
            progressDialog.setMessage("please wait..");
            progressDialog.show();

        }

        @Override
        protected void onPostExecute(String s) {
            System.out.println("returned" + ans);
            progressDialog.hide();
            if(ans.equals("success")){
                tvStatus.setText("Succefull created an account");
                tvStatus.setTextColor(Color.GREEN);
                clearTextViews();
            }else{
                tvStatus.setText("Something went wrong,is your internet working?");
                tvStatus.setTextColor(Color.RED);
            }


        }

        @Override
        protected String doInBackground(String... params) {


            URL url = null;
            try {
                url = new URL(VarStatic.getHostName() + "/account/signup.php?name="
                        +
                        URLEncoder.encode(accName) + "&password=" +
                        URLEncoder.encode(accPassword) + "&phno=" +
                        URLEncoder.encode(accPhoneNo));
                conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(7000);
                conn.connect();
                InputStream in = null;
                in = conn.getInputStream();
                InputStreamReader inReader = new InputStreamReader(in);

                BufferedReader br = new BufferedReader(inReader);

                String s = null;
                while ((s = br.readLine()) != null) {
                    ans = s;
                    System.out.println(s);


                }

                System.out.println("Success Connection");
                conn.disconnect();
                System.out.println("returned is" + ans);
                return ans;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return ans;
        }
    }
}
