package com.example.matrimonyapp.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteSiblingDetails extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "MatrimonySibling.db";
    public static final String TABLE_NAME = "sibling";
    public static final String  ID = "id";
    public static final String  SIBLING_DETAILS_ID = "sibling_details_id";
    public static final String  NAME = "name";
    public static final String  MOBILE_NO = "mobileno";
    public static final String  EDUCATION_ID = "education_id";
    public static final String  EDUCATION_NAME = "education_name";
    public static final String  OCCUPATION_ID = "occupation_id";
    public static final String  OCCUPATION_NAME = "occupation_name";
    public static final String  RELATION_ID = "relation_id";
    public static final String  RELATION = "relation";
    public static final String  MARITAL_STATUS = "marital_status";
    public static final String  SPOUSE_NAME = "spouse_name";
    public static final String  FATHER_IN_LAW_NAME = "father_in_law_name";
    public static final String  FATHER_IN_LAW_MOBILE_NO = "father_in_law_mobile_no";
    public static final String  FATHER_IN_LAW_VILLAGE = "father_in_law_village";
    public static final String  FATHER_IN_LAW_STATE_ID = "father_in_law_state_id";
    public static final String  FATHER_IN_LAW_COUNTRY_ID = "father_in_law_country_id";
    public static final String  FATHER_IN_LAW_CITY_ID =  "father_in_law_city_id";
    public static final String  FATHER_IN_LAW_STATE_NAME = "father_in_law_state_name";
    public static final String  FATHER_IN_LAW_COUNTRY_NAME = "father_in_law_country_name";
    public static final String  FATHER_IN_LAW_CITY_NAME =  "father_in_law_city_name";

    public SQLiteSiblingDetails(Context context)
    {
        super(context, DATABASE_NAME, null, 2);
    }


    // create table
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        sqLiteDatabase.execSQL("create table "+TABLE_NAME+" ( "
                + ID +" INTEGER PRIMARY KEY autoincrement, "
                + SIBLING_DETAILS_ID +" int, "
                + NAME +" text, "
                + MOBILE_NO +" text, "
                + EDUCATION_ID +" int, "
                + EDUCATION_NAME +" text, "
                + OCCUPATION_ID +" int, "
                + OCCUPATION_NAME +" text, "
                + RELATION_ID +" int, "
                + RELATION +" text, "
                + MARITAL_STATUS +" text, "
                + SPOUSE_NAME +" text, "
                + FATHER_IN_LAW_NAME +" text, "
                + FATHER_IN_LAW_MOBILE_NO +" text, "
                + FATHER_IN_LAW_VILLAGE +" text, "
                + FATHER_IN_LAW_COUNTRY_ID +" int, "
                + FATHER_IN_LAW_COUNTRY_NAME +" text, "
                + FATHER_IN_LAW_STATE_ID +" int, "
                + FATHER_IN_LAW_STATE_NAME +" text, "
                + FATHER_IN_LAW_CITY_ID+" int, "
                + FATHER_IN_LAW_CITY_NAME+" text"
                +" )");
    }


    // drop table if already exists
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    // fetch data by id
    public Cursor getDataById(int id)
    {

        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor res = sqLiteDatabase.rawQuery("SELECT * FROM "+TABLE_NAME+" WHERE "+ID+" = "+id, null);
        return res;

    }

    public int getNoOfSibling(String relation_name)
    {

        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        int res = sqLiteDatabase.rawQuery("SELECT * FROM "+TABLE_NAME+" WHERE "+RELATION+" = '"+relation_name+"'", null).getCount();
        return res;

    }

    // fetch all data
    public Cursor getAllData()
    {

        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor res = sqLiteDatabase.rawQuery("SELECT * FROM "+TABLE_NAME, null);
        return res;

    }

    public long insertSibling(String sibling_details_id, String name, String mobileNo, String educationId, String educationName,
                              String occupationId, String occupationName, String maritalStatus,
                              String relationId, String relation, String spouseName, String fil_name, String fil_mobileNo, String fil_village,
                              String fil_country_id, String fil_state_id,  String fil_city_id,
                              String fil_country_name, String fil_state_name, String fil_city_name)
    {

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put(SIBLING_DETAILS_ID, sibling_details_id);
        contentValues.put(NAME, name);
        contentValues.put(MOBILE_NO, mobileNo);
        contentValues.put(EDUCATION_ID, educationId);
        contentValues.put(EDUCATION_NAME, educationName);
        contentValues.put(OCCUPATION_ID, occupationId);
        contentValues.put(OCCUPATION_NAME, occupationName);
        contentValues.put(MARITAL_STATUS, maritalStatus);
        contentValues.put(RELATION_ID, relationId);
        contentValues.put(RELATION, relation);
        contentValues.put(SPOUSE_NAME, spouseName);
        contentValues.put(FATHER_IN_LAW_NAME, fil_name);
        contentValues.put(FATHER_IN_LAW_MOBILE_NO, fil_mobileNo);
        contentValues.put(FATHER_IN_LAW_VILLAGE, fil_village);
        contentValues.put(FATHER_IN_LAW_COUNTRY_ID, fil_country_id);
        contentValues.put(FATHER_IN_LAW_COUNTRY_NAME, fil_country_name);
        contentValues.put(FATHER_IN_LAW_STATE_ID, fil_state_id);
        contentValues.put(FATHER_IN_LAW_STATE_NAME, fil_state_name);
        contentValues.put(FATHER_IN_LAW_CITY_ID, fil_city_id);
        contentValues.put(FATHER_IN_LAW_CITY_NAME, fil_city_name);
        //contentValues.put(, );

        return sqLiteDatabase.insert(TABLE_NAME, null, contentValues);
        //insert returns rowId of newly inserted row or -1 if an errror occured


    }


    public int updateSibling(String id, String sibling_details_id, String name, String mobileNo, String educationId, String educationName,
                             String occupationId, String occupationName, String maritalStatus,
                             String relationId, String relation, String spouseName, String fil_name, String fil_mobileNo, String fil_village,
                             String fil_country_id, String fil_state_id,  String fil_city_id,
                             String fil_country_name, String fil_state_name, String fil_city_name)
    {

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put(NAME, name);
        contentValues.put(MOBILE_NO, mobileNo);
        contentValues.put(EDUCATION_ID, educationId);
        contentValues.put(EDUCATION_NAME, educationName);
        contentValues.put(OCCUPATION_ID, occupationId);
        contentValues.put(OCCUPATION_NAME, occupationName);
        contentValues.put(MARITAL_STATUS, maritalStatus);
        contentValues.put(RELATION_ID, relationId);
        contentValues.put(RELATION, relation);
        contentValues.put(SPOUSE_NAME, spouseName);
        contentValues.put(FATHER_IN_LAW_NAME, fil_name);
        contentValues.put(FATHER_IN_LAW_MOBILE_NO, fil_mobileNo);
        contentValues.put(FATHER_IN_LAW_VILLAGE, fil_village);
        contentValues.put(FATHER_IN_LAW_COUNTRY_ID, fil_country_id);
        contentValues.put(FATHER_IN_LAW_COUNTRY_NAME, fil_country_name);
        contentValues.put(FATHER_IN_LAW_STATE_ID, fil_state_id);
        contentValues.put(FATHER_IN_LAW_STATE_NAME, fil_state_name);
        contentValues.put(FATHER_IN_LAW_CITY_ID, fil_city_id);
        contentValues.put(FATHER_IN_LAW_CITY_NAME, fil_city_name);
        //contentValues.put(, );

        return sqLiteDatabase.update(TABLE_NAME, contentValues, SIBLING_DETAILS_ID+" = ? and "+ID+" = ?",
                new String[]{sibling_details_id, id});
        //insert returns rowId of newly inserted row or -1 if an errror occured


    }


    public  int numberOfRows()
    {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        return (int) DatabaseUtils.queryNumEntries(sqLiteDatabase, TABLE_NAME);

    }


    public int deleteSibling(Integer id)
    {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        //delete method returns no. of rows deleted or 0 otherwise
        return sqLiteDatabase.delete(TABLE_NAME,ID+" = ?", new String[]{Integer.toString(id)});


    }

    public int deleteAll()
    {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        return sqLiteDatabase.delete(TABLE_NAME,"1",null);

    }

}
