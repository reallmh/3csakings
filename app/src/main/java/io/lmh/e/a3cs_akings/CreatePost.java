package io.lmh.e.a3cs_akings;

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
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

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

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import io.lmh.e.a3cs_akings.Static.FunctionsStatic;
import io.lmh.e.a3cs_akings.Static.ImageStatic;
import io.lmh.e.a3cs_akings.Static.UIStatic;
import io.lmh.e.a3cs_akings.Static.VarStatic;

public class CreatePost extends AppCompatActivity {
    String imageurl = "";
    String postText;
    String userId, userName;

    EditText edtPost;
    ImageView imgPost;

    Context context;


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                Uri selectedImage = data.getData();
                imageurl = getPath(selectedImage);
                if (!imageurl.equals(null)) {
                    File file = new File(imageurl);
                    Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                    Picasso.with(getApplicationContext())
                            .load(file)
                            .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE).
                            placeholder(R.mipmap.background_placeholder).
                            error(R.mipmap.background_placeholder)
                            .into(imgPost);
                }
                String file_extn = imageurl.substring(imageurl.lastIndexOf(".") + 1);

                if (file_extn.equals("img") || file_extn.equals("jpg") || file_extn.equals("jpeg") || file_extn.equals("gif") || file_extn.equals("png")) {
                    //FINE
                } else {
                    //NOT IN REQUIRED FORMAT
                }
            }
        }
    }

    private String getPath(Uri uri) {
        String[] projection = {MediaStore.MediaColumns.DATA};
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        cursor.moveToFirst();
        imageurl = cursor.getString(column_index);

        return cursor.getString(column_index);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);

        context = getApplicationContext();
        userId = FunctionsStatic.getUserId(this);
        userName = FunctionsStatic.getUserName(this);
        //initialize
        edtPost = (EditText) findViewById(R.id.edt_post_text);
        imgPost = (ImageView) findViewById(R.id.img_post);

        //

    }

    public void onCreatePost(View view) {
        postText = edtPost.getText().toString();
        new PostPost().execute();
    }

    public void onChooseImage(View view) {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, 1);
    }

    public void onExitPost(View view) {
        finish();
    }


    class PostPost extends AsyncTask<String, String, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();


        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (!s.equals("false")) {
                UIStatic.showSnack(getWindow(), "Successfully posted", "success");
                finish();
            } else {
                UIStatic.showSnack(getWindow(), "Error posting...", "error");
            }

        }

        @Override
        protected String doInBackground(String... params) {
            String ans = "";
            final ArrayList<NameValuePair> nameValuePairs = new
                    ArrayList<NameValuePair>();
            //adding request parameters
            nameValuePairs.add(new BasicNameValuePair("userid", userId));
            if (!imageurl.equals("")) {
                String background = ImageStatic.getFileInString(imageurl);
                nameValuePairs.add(new BasicNameValuePair("img", background));
            }
            nameValuePairs.add(new BasicNameValuePair("username", userName));
            nameValuePairs.add(new BasicNameValuePair("posttext", postText));
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = null;
            httppost = new HttpPost(VarStatic.getHostName() + "/post/createpost.php");
            try {

                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            try {

                HttpResponse response = httpclient.execute(httppost);
                HttpEntity entity = response.getEntity();
                ans = EntityUtils.toString(entity);

            } catch (IOException e) {
                e.printStackTrace();
            }


            return ans;
        }


    }


}
