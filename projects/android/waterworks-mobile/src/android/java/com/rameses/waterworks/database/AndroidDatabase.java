package com.rameses.waterworks.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.rameses.waterworks.bean.Setting;
import java.util.ArrayList;
import java.util.List;

public class AndroidDatabase extends SQLiteOpenHelper implements Database{
    
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "etracs_waterworks";
    
    private static final String TABLE_SETTING = "setting";
    
    private static final String KEY_SETTING_NAME = "name";
    private static final String KEY_SETTING_VALUE = "value";
    
    public AndroidDatabase(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    
    @Override
    public void onCreate(SQLiteDatabase sqld) {
        sqld.execSQL("CREATE TABLE "+TABLE_SETTING+" ("+KEY_SETTING_NAME+" TEXT, "+KEY_SETTING_VALUE+", TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqld, int i, int i1) {
        sqld.execSQL("DROP TABLE IF EXIST " + TABLE_SETTING);
        onCreate(sqld);
    }

    @Override
    public void createSetting(Setting s) {
        ContentValues values = new ContentValues();
        values.put(KEY_SETTING_NAME, s.getName());
        values.put(KEY_SETTING_VALUE, s.getValue());
        
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(TABLE_SETTING, null, values);
        db.close();
    }

    @Override
    public void updateSetting(Setting s) {
        
    }

    @Override
    public List<Setting> getAllSettings() {
        List<Setting> list = new ArrayList<Setting>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_SETTING, null);
        if(cursor.moveToFirst()){
            do{
                Setting setting = new Setting(cursor.getString(0),cursor.getString(1));
                list.add(setting);
            }while(cursor.moveToNext());
        }
        return list;
    }
    
}
