package io.lmh.e.a3cs_akings.Static;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SyncStatusObserver;
import android.util.Base64;

import java.io.UnsupportedEncodingException;
import java.sql.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * Created by E on 6/21/2018.
 */

public class FunctionsStatic {
    //get user id
  public static String getUserId(Activity activity){
      SharedPreferences sharedPreferences;
      sharedPreferences=activity.getSharedPreferences("accountInfo", Context.MODE_PRIVATE);
      String accountId=sharedPreferences.getString("USERID","");
      return accountId;
  }

    public static String getUserName(Activity activity){
        SharedPreferences sharedPreferences;
        sharedPreferences=activity.getSharedPreferences("accountInfo", Context.MODE_PRIVATE);
        String userName=sharedPreferences.getString("USERName","");
        return userName;
    }
    //get cover image url
    public static String getCoverImageUrl(String userId){
        return  VarStatic.getHostName() + "/user_images/" + userId+"cover.png";
    }
    //get cover image url
    public static String getPostUrl(String postid){
        return  VarStatic.getHostName() + "/user_images/" + postid+".png";
    }

    //get profile image url
    public static String getProfileImageUrl(String userId){
        return  VarStatic.getHostName() + "/user_images/" + userId+"profile.png";
    }
    //get time string
    public static String getNiceTime(String time){
        String returnedDate="";
        SimpleDateFormat dateFormat=new SimpleDateFormat("yy-MM-dd kk:mm:ss");
        try {
            java.util.Date date=dateFormat.parse(time);
            java.util.Date now=Calendar.getInstance().getTime();
            if(date.getDate()==now.getDate()){
                int nowHour=date.getHours();
                int dateHour=now.getHours();
                if(nowHour==dateHour){
                    returnedDate=now.getMinutes()-date.getMinutes()+"minutes ago";
                }else {
                    int hour = date.getHours();
                    int nowhour = now.getHours();
                    int anshour = nowhour - hour;
                  returnedDate=anshour + "hours ago";
                }
            }else {
                Calendar cal=new GregorianCalendar();
                cal.setTime(date);
                int year=cal.get(Calendar.YEAR);
                System.out.println();
            returnedDate=date.getDate()+":"+date.getMonth()+":"+year;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return ":"+returnedDate;
    }

}
