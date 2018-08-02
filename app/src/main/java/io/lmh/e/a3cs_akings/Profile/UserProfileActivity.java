package io.lmh.e.a3cs_akings.Profile;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import UI.CircleTransformation;
import io.lmh.e.a3cs_akings.Fragment.ProfileFollowers;
import io.lmh.e.a3cs_akings.Fragment.ProfilePost;
import io.lmh.e.a3cs_akings.NetworkHelper.CheckConnection;
import io.lmh.e.a3cs_akings.R;
import io.lmh.e.a3cs_akings.Static.FunctionsStatic;
import io.lmh.e.a3cs_akings.Static.VarStatic;

public class UserProfileActivity extends AppCompatActivity {
    private ImageView background, profile, imgmajor, imggender;
    private String coverName, profileName;
    TabLayout profileTabs;
    ViewPager viewPager;
    private String userId, profileId, username;
    Button followBtn;
    CoordinatorLayout activityProfile;
    LinearLayout parentProfile;
    String self = "no";

    public String getProfileId() {
        return profileId;
    }

    public void setProfileId(String profileId) {
        this.profileId = profileId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    String accInfoId, accId, accname, profile_views, followers, followed, posts,
            gender, age, year, major, nickname, about, following;
    TextView viewCount, postCount, followerCount, userName, accbio, accyear, accmajor, accgender;
    Bundle bundle = null;
    Intent intent = null;
    ProfilePost profilePost = null;
    ProfileFollowers profileFollowers = null;
    CheckConnection checkConnection = null;
    SwipeRefreshLayout profileRefresh = null;


    @Override
    protected void onResume() {

        super.onResume();
        if (checkConnection.isNewtowrkOnline(getApplicationContext())) {
            new GetFollowerInfo().execute();
            setImages();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        //sending data to profile and follower fragments
    }

    //adapters
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkConnection = new CheckConnection();
        System.out.println("network state" + checkConnection.isNewtowrkOnline(getApplicationContext()));
        setContentView(R.layout.activity_user_profile);
        parentProfile = (LinearLayout) findViewById(R.id.user_profile_view);
        activityProfile = (CoordinatorLayout) findViewById(R.id.activity_show_profile);
        profileRefresh = (SwipeRefreshLayout) findViewById(R.id.refresh_profile);
        if (!checkConnection.isNewtowrkOnline(getApplicationContext())) {
            ((ViewManager) activityProfile.getParent()).removeView(activityProfile);
            profileRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    if (checkConnection.isNewtowrkOnline(getApplicationContext())) {
                        profileRefresh.setRefreshing(false);
                        profileRefresh.setVisibility(View.INVISIBLE);
                        parentProfile.addView(activityProfile, 0);
                        setupProfileView();
                    }
                    profileRefresh.setRefreshing(false);
                }
            });

        } else {
            setupProfileView();
        }
    }

    private void setupProfileView() {

        userId = FunctionsStatic.getUserId(this);
        username = FunctionsStatic.getUserName(this);
        System.out.println("on profile activity create");
        intent = getIntent();
        profileId = intent.getStringExtra("profileId");
        if (profileId == null) {
            profileId = userId;
        }

        //for post fragment
        //
        coverName = FunctionsStatic.getCoverImageUrl(profileId);
        profileName = FunctionsStatic.getProfileImageUrl(profileId);
        background = (ImageView) findViewById(R.id.img_profile_bg);
        profile = (ImageView) findViewById(R.id.profile_img);


        //user info
        followBtn = (Button) findViewById(R.id.btn_profile_follow);
        accyear = (TextView) findViewById(R.id.pf_year);
        accbio = (TextView) findViewById(R.id.pf_bio);
        accmajor = (TextView) findViewById(R.id.pf_major);
        accgender = (TextView) findViewById(R.id.pf_gender);
        viewCount = (TextView) findViewById(R.id.txt_views_count);
        postCount = (TextView) findViewById(R.id.txt_posts_count);
        followerCount = (TextView) findViewById(R.id.txt_followers_count);
        userName = (TextView) findViewById(R.id.profile_user_name);
        imgmajor = (ImageView) findViewById(R.id.img_major);
        imggender = (ImageView) findViewById(R.id.img_gender);
        activityProfile = (CoordinatorLayout) findViewById(R.id.activity_show_profile);
        //

        profileTabs = (TabLayout) findViewById(R.id.profile_tabs);
        viewPager = (ViewPager) findViewById(R.id.profile_viewpager);
        setUpViewPager(viewPager);
        profileTabs.setupWithViewPager(viewPager);
        //toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), EditProfileActivity.class);
                startActivity(intent);
            }
        });
        //checkup if not user is itself
        if (!userId.equals(profileId)) {
            ((ViewManager) fab.getParent()).removeView(fab);
        }

        if (profileId.equals(userId)) {
            ((ViewManager) followBtn.getParent()).removeView(followBtn);
            self = "yes";
        }

        //

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setImages();
    }


    private void setUpViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new ProfilePost(), "posts");
        adapter.addFragment(new ProfileFollowers(), "followers");
        viewPager.setAdapter(adapter);

    }


    //setting profile images
    private void setImages() {
        Picasso.with(getApplicationContext()).load(coverName).resize(800, 300).centerCrop().placeholder(R.mipmap.background_placeholder)
                .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                .error(R.mipmap.background_placeholder)
                .into(background);
        Picasso.with(this).load(profileName).placeholder(R.mipmap.avator_placeholder).
                transform(new CircleTransformation()).
                resize(100, 100).centerCrop().
                error(R.mipmap.avator_placeholder).memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE).
                into(profile);
    }

    public void onFollowButtonClick(View view) {
        if (followBtn.getText().equals("follow")) {
            //
            followBtn.setText("unfollow");
            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            String url = VarStatic.getHostName() + "/follow/follow.php?userId=" + URLEncoder.encode(userId) +
                    "&following=" + URLEncoder.encode(profileId) + "&username=" + URLEncoder.encode(username);
            StringRequest stringReq = new StringRequest(StringRequest.Method.GET, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String s) {
                    if (!(s.equals("err"))) {
                        followBtn.setText("unfollow");
                        int foll = Integer.parseInt(followerCount.getText().toString());
                        foll = foll + 1;
                        followers = Integer.toString(foll);
                        followerCount.setText(followers);
                    } else {
                        followBtn.setText("follow");
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    followBtn.setText("follow");
                }
            }

            );
            requestQueue.add(stringReq);
            requestQueue.start();
        } else {
            followBtn.setText("follow");
            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            String url = VarStatic.getHostName() + "/follow/unfollow.php?userId=" + URLEncoder.encode(userId) +
                    "&following=" + URLEncoder.encode(profileId);
            StringRequest stringReq = new StringRequest(StringRequest.Method.GET, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String s) {
                    if (!(s.equals("err"))) {
                        followBtn.setText("follow");
                        int foll = Integer.parseInt(followerCount.getText().toString());
                        foll = foll - 1;
                        followers = Integer.toString(foll);
                        followerCount.setText(followers);
                    } else {
                        followBtn.setText("unfollow");
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    followBtn.setText("unfollow");
                }
            }

            );
            requestQueue.add(stringReq);
            requestQueue.start();
        }
    }

    //for fragments
    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragements = new ArrayList<>();
        private final List<String> mFragmentName = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragements.get(position);
        }

        @Override
        public int getCount() {
            return mFragements.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragements.add(fragment);
            mFragmentName.add(title);
        }

        public CharSequence getPageTitle(int position) {
            return mFragmentName.get(position);
        }

    }

    //===================================NetWorkClassess==============================
    private class GetFollowerInfo extends AsyncTask<String, String, String> {
        HttpURLConnection conn;
        ProgressDialog progressDialog;


        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onPostExecute(String s) {
            System.out.println(s);
            super.onPostExecute(s);

            try {
                JSONObject userinfo = new JSONObject(s);
                accInfoId = userinfo.getString("acc_info_id");
                accId = userinfo.getString("acc_id");
                accname = userinfo.getString("acc_name");
                profile_views = userinfo.getString("profile_views");
                followers = userinfo.getString("followers");
                followed = userinfo.getString("follows");
                posts = userinfo.getString("posts");
                gender = userinfo.getString("gender");
                age = userinfo.getString("age");
                year = userinfo.getString("year");
                major = userinfo.getString("major");
                nickname = userinfo.getString("nickname");
                about = userinfo.getString("about");
                following = userinfo.getString("following");
                //set ifo
                viewCount.setText(profile_views);
                postCount.setText(posts);
                followerCount.setText(followers);
                userName.setText(accname);
                accbio.setText(about);
                accyear.setText(year);
                accmajor.setText(major);
                accgender.setText(gender);

                //set icons
                if (major.equals("CS")) {
                    imgmajor.setImageResource(R.drawable.ic_cs);
                }
                if (major.equals("CT")) {
                    imgmajor.setImageResource(R.drawable.ic_ct);
                }
                if (major.equals("CST")) {
                    imgmajor.setImageResource(R.drawable.ic_cst);
                }

                if (gender.equals("Female")) {
                    imggender.setImageResource(R.drawable.ic_computer_girl);
                } else {
                    imggender.setImageResource(R.drawable.ic_computer_boy);
                }

                //set follow button
                if (following.equals("yes")) {
                    followBtn.setText("unfollow");
                } else {
                    followBtn.setText("follow");
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        @Override
        protected String doInBackground(String... params) {
            String ans = "";
            URL url = null;
            try {
                url = new URL(VarStatic.getHostName() + "/profile/get_profile_info.php?userId=" +
                        URLEncoder.encode(userId) + "&profileId=" + URLEncoder.encode(profileId)
                        + "&self=" + URLEncoder.encode(self))
                ;
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


}
