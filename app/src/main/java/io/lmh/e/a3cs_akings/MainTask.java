package io.lmh.e.a3cs_akings;

import android.app.IntentService;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;
import java.util.List;

import UI.CircleTransformation;
import io.lmh.e.a3cs_akings.Fragment.MessageFragment;
import io.lmh.e.a3cs_akings.Fragment.NotificationFragment;
import io.lmh.e.a3cs_akings.Fragment.PeopleListFragment;
import io.lmh.e.a3cs_akings.Fragment.PostFragment;
import io.lmh.e.a3cs_akings.Fragment.SearchFragment;
import io.lmh.e.a3cs_akings.Message.MessageListFragment;
import io.lmh.e.a3cs_akings.Profile.UserProfileActivity;
import io.lmh.e.a3cs_akings.Services.ActiveReporter;
import io.lmh.e.a3cs_akings.Static.FunctionsStatic;

public class MainTask extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    String coverName,profileName;
    //initialize vars
    String strAccount, strInfo;
    SharedPreferences sharedPreferences;
    View header;
    //initialize ui
    private ImageView profile;
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private int tabIcons[] = {R.drawable.ic_chat_bubble_outline_black_24dp, R.drawable.ic_people_outline_black_24dp, R.drawable.ic_mail_outline_black_24dp,R.drawable.ic_notifications_none_black_24dp};
    private TextView accountName, accountInfo;

    public Toolbar getToolbar() {
        return toolbar;
    }

    @Override
    protected void onResume() {
        super.onResume();
        setAccountInfo();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //goto initialize activity
        setContentView(R.layout.activity_main_task);
        coverName=FunctionsStatic.getCoverImageUrl(FunctionsStatic.getUserId(this));
        profileName=FunctionsStatic.getProfileImageUrl(FunctionsStatic.getUserId(this));
        //set account info
        NavigationView navView = (NavigationView) findViewById(R.id.nav_view);
        navView.setNavigationItemSelectedListener(this);
        header = navView.getHeaderView(0);
        profile = (ImageView) header.findViewById(R.id.profile_image);
        accountName = (TextView) header.findViewById(R.id.account_name);
        accountInfo = (TextView) header.findViewById(R.id.account_info);
        setAccountInfo();
        //action bar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setUpViewPager(viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);

        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        tabLayout.getTabAt(2).setIcon(tabIcons[2]);
        tabLayout.getTabAt(3).setIcon(tabIcons[3]);

       
        //
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Intent intent=new Intent(getApplicationContext(),CreatePost.class);
                startActivity(intent);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);



    }

    //setting account info
    private void setAccountInfo() {
        //account info setting
        sharedPreferences = getSharedPreferences("accountInfo", Context.MODE_PRIVATE);
        strAccount = sharedPreferences.getString("USERID", "");
        strInfo = sharedPreferences.getString("USERName", "");
        //text
        accountName.setText(strAccount);
        accountInfo.setText(strInfo);
        //image
        //header width height


           Picasso.with(this).load(profileName).placeholder(R.mipmap.avator_placeholder).
                transform(new CircleTransformation()).
                resize(100, 100).centerCrop().
                error(R.mipmap.avator_placeholder).memoryPolicy(MemoryPolicy.NO_CACHE,MemoryPolicy.NO_STORE).
                into(profile);
           Picasso.with(this).load(coverName).placeholder(R.mipmap.background_placeholder).placeholder(R.mipmap.background_placeholder)
                   .error(R.mipmap.background_placeholder)
                   .resize(300,160).noFade()
                   .centerCrop().
                   memoryPolicy(MemoryPolicy.NO_CACHE,MemoryPolicy.NO_STORE).
                   into(new Target() {
           @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
           @Override
           public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom loadedFrom) {
               header.setBackground(new BitmapDrawable(getApplicationContext().getResources(),bitmap));
           }

           @Override
           public void onBitmapFailed(Drawable drawable) {

           }

           @Override
           public void onPrepareLoad(Drawable drawable) {

           }
       });


    }

    private void setUpViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new PostFragment(), "post");
        adapter.addFragment(new PeopleListFragment(), "people");
        adapter.addFragment(new MessageListFragment(), "message");
        adapter.addFragment(new NotificationFragment(),"noti");
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        switch (id) {
            case R.id.menulogout:
                AlertDialog alertDialog = new AlertDialog.Builder(MainTask.this).create();
                alertDialog.setTitle("Log Out");
                alertDialog.setMessage("Are you sure you want to log out?");
                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SharedPreferences sharedPrefs;
                        SharedPreferences.Editor editor;
                        sharedPrefs = getSharedPreferences("accountInfo", Context.MODE_PRIVATE);
                        editor = sharedPrefs.edit();
                        editor.putString("USERID", null);
                        editor.putString("USERName", null);
                        editor.commit();
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                        MainTask.this.finish();
                    }
                });
                alertDialog.show();
                break;
            case R.id.about:
                Intent intent = new Intent(getApplicationContext(), AboutActivity.class);
                startActivity(intent);
                break;


        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void onEditProfileClick(View view) {
        Intent intent = new Intent(getApplicationContext(), UserProfileActivity.class);
        intent.putExtra("profileId",FunctionsStatic.getUserId(this));
        startActivity(intent);
    }

    public void onProfileClick(View view) {
        Intent intent = new Intent(getApplicationContext(), UserProfileActivity.class);
        intent.putExtra("profileId",FunctionsStatic.getUserId(this));
        startActivity(intent);
    }


    //app bar

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
            return null;
        }

    }
}
