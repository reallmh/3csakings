package io.lmh.e.a3cs_akings.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by E on 8/7/2018.
 */

public class KingDBHelper extends SQLiteOpenHelper {
    private static final String DB_NAME="csakings.db";
    private static final String TABLE_NAME="messageitem";
    private static final String COL_1="id";
    private static final String COL_2="receiverid";
    private static final String COL_3="receivername";
    private static final String COL_4="lastmessage";
    private static final String COL_5="lastmessagetime";

    public KingDBHelper(Context context){
        super(context,DB_NAME,null,1);
        SQLiteDatabase db=this.getWritableDatabase();
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
       db.execSQL("create table "+TABLE_NAME+" (id TEXT PRIMARY KEY ,receiverid TEXT,receivername  TEXT,lastmessage TEXT,lastmessagetime TEXT);");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);
    }

    public boolean insertMessageData(String id,String receiverid,String receivername,String lastmessage,String lastmsgtime){
        SQLiteDatabase database=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(COL_1,id);
        contentValues.put(COL_2,receiverid);
        contentValues.put(COL_3,receivername);
        contentValues.put(COL_4,lastmessage);
        contentValues.put(COL_5,lastmsgtime);

        long result=database.insert(TABLE_NAME,null,contentValues);
        if(result==-1){
            return false;
        }else {
            return true;
        }
    }

    public Cursor getMessageItems(){
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor cursor=db.rawQuery("select * from "+TABLE_NAME,null);
        return cursor;
    }
    public void deleteMessageItems() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.rawQuery("delete from "+TABLE_NAME,null);
        db.close();
    }

}
