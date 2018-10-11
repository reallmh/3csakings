package io.lmh.e.a3cs_akings.Profile;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

import UI.CircleTransformation;
import io.lmh.e.a3cs_akings.R;
import io.lmh.e.a3cs_akings.Static.FunctionsStatic;
import io.lmh.e.a3cs_akings.Static.ImageStatic;
import io.lmh.e.a3cs_akings.Static.UIStatic;
import io.lmh.e.a3cs_akings.Static.VarStatic;

public class EditProfileActivity extends AppCompatActivity {
    String coverFile = "", profileFile = "";
    String userId = "";
    String coverName, profileName;
    String coverImagePath, profileImagePath;

    String accbio, year, major, gender, accname;

    ImageView cover_image, profile_image;
    View editProfile;

    Context context = this;
    TextView txtName;
    RadioGroup radioGender, radioMajor, radioYear;
    RadioButton rgender, ryear, rmajor;
    EditText bio;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                Uri selectedImage = data.getData();
                coverFile = getPath(selectedImage);
                System.out.println("file path is " + coverFile);
                if (!coverFile.equals(null)) {
                    File file = new File(coverFile);
                    Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                    Picasso.with(getApplicationContext())
                            .load(file).resize(editProfile.getWidth(), 300)
                            .memoryPolicy(MemoryPolicy.NO_CACHE,MemoryPolicy.NO_STORE).
                            placeholder(R.mipmap.background_placeholder).
                            error(R.mipmap.background_placeholder)
                            .centerCrop()
                            .into(cover_image);
                }
                String file_extn = coverFile.substring(coverFile.lastIndexOf(".") + 1);

                if (file_extn.equals("img") || file_extn.equals("jpg") || file_extn.equals("jpeg") || file_extn.equals("gif") || file_extn.equals("png")) {
                    //FINE
                } else {
                    //NOT IN REQUIRED FORMAT
                }
            }
        }
        if (requestCode == 1999) {
            if (resultCode == Activity.RESULT_OK) {
                Uri selectedImage = data.getData();
                profileFile = getPath(selectedImage);
                System.out.println("file path is " + profileFile);
                if (!profileFile.equals(null)) {
                    File file = new File(profileFile);
                    Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                    Picasso.with(getApplicationContext())
                            .load(file)
                            .placeholder(R.mipmap.avator_placeholder)
                            .error(R.mipmap.avator_placeholder)
                            .memoryPolicy(MemoryPolicy.NO_CACHE,MemoryPolicy.NO_STORE)
                            .transform(new CircleTransformation())
                            .resize(85, 85)
                            .centerCrop().
                            into(profile_image);
                }
                String file_extn = profileFile.substring(profileFile.lastIndexOf(".") + 1);

                if (file_extn.equals("img") || file_extn.equals("jpg") || file_extn.equals("jpeg") || file_extn.equals("gif") || file_extn.equals("png")) {
                    //FINE
                } else {
                    //NOT IN REQUIRED FORMAT
                }
            }
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        editProfile = (View) findViewById(R.id.edit_profile);
        coverName = FunctionsStatic.getCoverImageUrl(FunctionsStatic.getUserId(this));
        profileName = FunctionsStatic.getProfileImageUrl(FunctionsStatic.getUserId(this));
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setTitle("Edit Profile");
        userId = FunctionsStatic.getUserId(this);


        //initialize views
        profile_image = (ImageView) findViewById(R.id.img_profile);
        cover_image = (ImageView) findViewById(R.id.img_profile_cover);
        radioGender = (RadioGroup) findViewById(R.id.edt_acc_gender);
        radioMajor = (RadioGroup) findViewById(R.id.edt_acc_major);
        radioYear = (RadioGroup) findViewById(R.id.edt_acc_attending_year);
        bio = (EditText) findViewById(R.id.edt_bio);
        txtName = (TextView) findViewById(R.id.edt_name);

        new GetFollowerInfo().execute();
        Picasso.with(getApplicationContext()).load(coverName)
                .placeholder(R.mipmap.background_placeholder)
                .memoryPolicy(MemoryPolicy.NO_CACHE,MemoryPolicy.NO_STORE)
                .resize(600, 300).error(R.mipmap.background_placeholder)
                .centerCrop().into(cover_image);
        Picasso.with(getApplicationContext())
                .load(profileName)
                .placeholder(R.drawable.ic_profile_loading)
                .transform(new CircleTransformation())
                .centerCrop()
                .memoryPolicy(MemoryPolicy.NO_CACHE,MemoryPolicy.NO_STORE)
                .resize(85, 85)
                .error(R.drawable.ic_profile)
                .into(profile_image);

        //on click listeners
        radioYear.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                ryear = (RadioButton) findViewById(checkedId);
                if (ryear.getText().toString().equals("First Year")) {
                    for (int i = 0; i < radioMajor.getChildCount(); i++) {
                        ((RadioButton) radioMajor.getChildAt(i)).setEnabled(false);
                    }
                } else {
                    for (int i = 0; i < radioMajor.getChildCount(); i++) {
                        ((RadioButton) radioMajor.getChildAt(i)).setEnabled(true);
                    }
                }
            }
        });
    }

    public void onUpdateCover(View view) {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, 1);

    }


    private String getPath(Uri uri) {
        String[] projection = {MediaStore.MediaColumns.DATA};
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        cursor.moveToFirst();
        coverImagePath = cursor.getString(column_index);

        return cursor.getString(column_index);
    }

    public void onSaveButtonClick(View view) {
        accbio = bio.getText().toString();
        System.out.println(accbio);
        int i = radioGender.getCheckedRadioButtonId();
        rgender = (RadioButton) findViewById(i);
        i = radioMajor.getCheckedRadioButtonId();
        rmajor = (RadioButton) findViewById(i);
        i = radioYear.getCheckedRadioButtonId();
        ryear = (RadioButton) findViewById(i);

        gender = rgender.getText().toString();
        major = rmajor.getText().toString();
        year = ryear.getText().toString();

        new AsynPostImage().execute();
        new UpdateProfileInfo().execute();

    }

    public void onProfileImageClick(View view) {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, 1999);
    }

    class AsynPostImage extends AsyncTask<String, String, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(context);
            progressDialog.setTitle("profile update");
            progressDialog.setMessage("updating profile,please wait..");
            progressDialog.show();

        }

        @Override
        protected void onPostExecute(String s) {
            progressDialog.cancel();
            System.out.println(s);

            super.onPostExecute(s);
            if (!s.equals("false")) {
                UIStatic.showSnack(getWindow(), "Success,Images updated", "success");
            } else {
                UIStatic.showSnack(getWindow(), "Error updating Images", "error");
            }

        }

        @Override
        protected String doInBackground(String... params) {
            String ans = "";
            final ArrayList<NameValuePair> nameValuePairs = new
                    ArrayList<NameValuePair>();
            //adding request parameters
            nameValuePairs.add(new BasicNameValuePair("userid", userId));
            if (!coverFile.equals("")) {
                String background = ImageStatic.getFileInString(coverFile);
                nameValuePairs.add(new BasicNameValuePair("background", background));
            }
            if (!profileFile.equals("")) {
                String profile = ImageStatic.getFileInString(profileFile);
                nameValuePairs.add(new BasicNameValuePair("profile", profile));
            }


            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = null;
            httppost = new HttpPost(VarStatic.getHostName() + "/image/uploadprofileimage.php");
            try {

                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            try {

                HttpResponse response = httpclient.execute(httppost);
                HttpEntity entity = response.getEntity();
                ans = EntityUtils.toString(entity);
                System.out.println(ans);
            } catch (IOException e) {
                e.printStackTrace();
            }


            return ans;
        }


    }

    //===================================NetWorkClassess==============================
    private class GetFollowerInfo extends AsyncTask<String, String, String> {
        HttpURLConnection conn;
        ProgressDialog progressDialog;


        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(context);
            progressDialog.setTitle("please wait");
            progressDialog.setMessage("getting profile info..");
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(String s) {
            progressDialog.cancel();
            super.onPostExecute(s);

            try {
                JSONObject userinfo = new JSONObject(s);
                accname = userinfo.getString("acc_name");
                gender = userinfo.getString("gender");
                year = userinfo.getString("year");
                major = userinfo.getString("major");
                accbio = userinfo.getString("about");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            txtName.setText(accname);
            bio.setText(accbio);
            //for gender
            for (int i = 0; i < radioGender.getChildCount(); i++) {

                RadioButton rd = (RadioButton) radioGender.getChildAt(i);
                if (rd.getText().toString().equals(gender)) {
                    rd.setChecked(true);
                }
            }
            //for year
            for (int i = 0; i < radioYear.getChildCount(); i++) {
                RadioButton rd = (RadioButton) radioYear.getChildAt(i);
                if (rd.getText().equals(year)) {
                    rd.setChecked(true);
                }
            }
            //for major
            for (int i = 0; i < radioMajor.getChildCount(); i++) {
                RadioButton rd = (RadioButton) radioMajor.getChildAt(i);
                if (rd.getText().toString().equals(major)) {
                    rd.setChecked(true);
                }
            }


        }

        @Override
        protected String doInBackground(String... params) {
            String ans = "";
            URL url = null;
            try {
                url = new URL(VarStatic.getHostName() + "/profile/get_profile_info.php?userId=" +
                        URLEncoder.encode(userId) + "&profileId=" + URLEncoder.encode(userId)
                +"&self="+URLEncoder.encode("no"));
                conn = (HttpURLConnection) url.openConnection();
                conn.connect();
                InputStream in = null;
                in = conn.getInputStream();
                InputStreamReader inReader = new InputStreamReader(in);

                BufferedReader br = new BufferedReader(inReader);

                String s = null;
                while ((s = br.readLine()) != null) {
                    ans += s + "\n";
                }
                conn.disconnect();
                return ans;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return ans;
        }
    }
    //Sign up Network class

    private class UpdateProfileInfo extends AsyncTask<String, String, String> {
        HttpURLConnection conn;
        ProgressDialog progressDialog;
        String ans = "";


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(context);
            progressDialog.setTitle("Wait a sec..");
            progressDialog.setMessage("updating profile info please wait..");
            progressDialog.show();

        }

        @Override
        protected void onPostExecute(String s) {
            System.out.println("returned" + ans);
            progressDialog.cancel();
            if (s.equals("success")) {
                UIStatic.showSnack(getWindow(), "Success,Images updated", "success");
            } else {
                UIStatic.showSnack(getWindow(), "Error updating profile info", "err");
            }


        }

        @Override
        protected String doInBackground(String... params) {


            URL url = null;
            try {
                url = new URL(VarStatic.getHostName() + "/image/editprofileinfo.php?userid="
                        +
                        URLEncoder.encode(userId) + "&bio=" +
                        URLEncoder.encode(accbio) + "&gender=" +
                        URLEncoder.encode(gender)
                        + "&year=" +
                        URLEncoder.encode(year)
                        + "&major=" +
                        URLEncoder.encode(major))
                ;
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
                System.out.println(ans);
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
