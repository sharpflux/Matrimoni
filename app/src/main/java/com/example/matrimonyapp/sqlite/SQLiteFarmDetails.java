package com.example.matrimonyapp.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteFarmDetails extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "MatrimonyFarm.db";
    public static final String TABLE_NAME = "farm";
    public static final String  ID = "id";
    public static final String  FARM_DETAILS_ID = "farm_details_id";
    public static final String  TOTAL_AREA = "total_area";
    public static final String  IRRIGATED_AREA = "irrigated_area";
    public static final String  PARTIALLY_IRRIGATED_AREA = "partially_irrigated_area";
    public static final String  NON_IRRIGATED_AREA = "non_irrigated_area";
    //public static final String  TYPE = "type";
    public static final String  CROPS_ID = "crops_id";
    public static final String  CROPS_NAME = "crops_name";
   // public static final String  IRRIGATION_TYPE = "irrigation_type";
/*    public static final String  DISTRICT_ID = "district_id";
    public static final String  TALUKA_ID =  "taluka_id";
    public static final String  STATE_NAME = "state_name";
    public static final String  DISTRICT_NAME = "district_name";
    public static final String  TALUKA_NAME =  "taluka_name";*/

    public SQLiteFarmDetails(Context context)
    {
        super(context, DATABASE_NAME, null, 2);
    }


    // create table
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        try {

            sqLiteDatabase.execSQL("create table " + TABLE_NAME + " ( "
                    + ID + " INTEGER PRIMARY KEY autoincrement, "
                    + FARM_DETAILS_ID + " int, "
                    + TOTAL_AREA + " text, "
                    + IRRIGATED_AREA + " text, "
                    + PARTIALLY_IRRIGATED_AREA + " text, "
                    + NON_IRRIGATED_AREA + " text, "
                    + CROPS_ID + " text, "
                    + CROPS_NAME + " text "

                    + " )");
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }


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

    // fetch all data
    public Cursor getAllData()
    {

        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor res = sqLiteDatabase.rawQuery("SELECT * FROM "+TABLE_NAME, null);
        return res;

    }

    public String containsDetails(String farm_details_id)
    {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor res = sqLiteDatabase.rawQuery("SELECT * FROM "+TABLE_NAME+" WHERE "+FARM_DETAILS_ID+" = "+farm_details_id, null);

        if(res.getCount()<1)
        {
            res.close();
            return "0";
        }
        else
        {
            res.moveToFirst();
            String id = res.getString(res.getColumnIndex(ID));
            res.close();

            return id;
        }

    }

    public long insertFarmDetails(String farm_details_id, String totalArea, String irrigatedArea,
                                  String partiallyIrrigatedArea, String nonIrrigatedArea, String cropsId, String cropsName)
    {

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        //contentValues.put(ID, null);
        contentValues.put(FARM_DETAILS_ID, farm_details_id);
        contentValues.put(TOTAL_AREA, totalArea);
        contentValues.put(IRRIGATED_AREA, irrigatedArea);
        contentValues.put(PARTIALLY_IRRIGATED_AREA, partiallyIrrigatedArea);
        contentValues.put(NON_IRRIGATED_AREA, nonIrrigatedArea);
        contentValues.put(CROPS_ID, cropsId);
        contentValues.put(CROPS_NAME, cropsName);
        //contentValues.put(IRRIGATION_TYPE, irrigation_type);

        //contentValues.put(, );

        return sqLiteDatabase.insert(TABLE_NAME, null, contentValues);
        //insert returns rowId of newly inserted row or -1 if an errror occured


    }


    public int updateFarmDetails( String id, String farm_details_id, String totalArea, String irrigatedArea,
                                  String partiallyIrrigatedArea, String nonIrrigatedArea, String cropsId, String cropsName)
    {

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put(FARM_DETAILS_ID, farm_details_id);
        contentValues.put(TOTAL_AREA, totalArea);
        contentValues.put(IRRIGATED_AREA, irrigatedArea);
        contentValues.put(PARTIALLY_IRRIGATED_AREA, partiallyIrrigatedArea);
        contentValues.put(NON_IRRIGATED_AREA, nonIrrigatedArea);
        contentValues.put(CROPS_ID, cropsId);
        contentValues.put(CROPS_NAME, cropsName);

        return sqLiteDatabase.update(TABLE_NAME, contentValues, FARM_DETAILS_ID+" = ? and "+ID+" = ?",
                new String[]{farm_details_id, id});
        //insert returns rowId of newly inserted row or -1 if an errror occured
        //update returns no. of rows updated
    }


    public  int numberOfRows()
    {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        return (int) DatabaseUtils.queryNumEntries(sqLiteDatabase, TABLE_NAME);

    }


    public int deleteFarmDetails(Integer id)
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
