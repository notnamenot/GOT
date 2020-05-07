package pl.edu.agh.wtm.got;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import pl.edu.agh.wtm.got.models.GOTPoint;

//View -> Tool Windows -> Device File Explorer -> data -> data
//https://www.youtube.com/watch?v=hDSVInZ2JCs
//handles operations on database
public class GOTdao extends SQLiteOpenHelper{

    public static final String MOUNTAIN_RANGE_TABLE = "MOUNTAIN_RANGE_TABLE";
    public static final String MOUNTAIN_CHAIN_TABLE = "MOUNTAIN_CHAIN_TABLE";
    public static final String GOT_POINT_TABLE = "GOT_POINT_TABLE";

    public static final String COLUMN_ID = "ID";
    public static final String COLUMN_MOUNTAIN_RANGE_ID = "MOUNTAIN_RANGE_ID";
    public static final String COLUMN_MOUNTAIN_CHAIN_ID = "MOUNTAIN_CHAIN_ID";
    public static final String COLUMN_HEIGHT = "HEIGHT";
    public static final String COLUMN_NAME = "NAME";
    public static final String COLUMN_LENGTH = "LENGTH";
    public static final String COLUMN_PEAK = "PEAK";

    // context - reference to apliccation itself, name of db to create
//    public GOTdao(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
    public GOTdao(@Nullable Context context) {
        super(context, "GOT.db", null, 2);
    }

    //this is called the first time a database  is accessed
    @Override
    public void onCreate(SQLiteDatabase db) {

        // łańcuch górski - np. Sudety
        String createMountainRangeTableStatement = "CREATE TABLE " + MOUNTAIN_RANGE_TABLE + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_NAME + " TEXT," +
                COLUMN_LENGTH + " INTEGER," +
                COLUMN_PEAK + " TEXT)"; // Zamiast tego id ?

        //pasmo
        String createMountainChainTableStatement = "CREATE TABLE " + MOUNTAIN_CHAIN_TABLE + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_NAME + " TEXT," +
                COLUMN_LENGTH + " INTEGER," +
                COLUMN_MOUNTAIN_RANGE_ID + " INTEGER)";

        String createGOTPointTableStatement = "CREATE TABLE " + GOT_POINT_TABLE + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_NAME + " TEXT," +
                COLUMN_HEIGHT + " INTEGER," +
                COLUMN_MOUNTAIN_CHAIN_ID + " INTEGER)";

        db.execSQL(createMountainRangeTableStatement);
        db.execSQL(createMountainChainTableStatement);
        db.execSQL(createGOTPointTableStatement);

        ContentValues cv = new ContentValues(); //  tablica asocjacyjna/hashmap valuename-content
        cv.put(COLUMN_NAME, "Sudety");
        cv.put(COLUMN_LENGTH, 300);
        cv.put(COLUMN_PEAK, "Śnieżka");
        db.insert(MOUNTAIN_RANGE_TABLE, null,cv);

        int id = 1;//getMountainRangeId("Sudety");
        db.execSQL("INSERT INTO " + MOUNTAIN_CHAIN_TABLE + "(NAME,LENGTH,MOUNTAIN_RANGE_ID) VALUES('Góry Orlickie',50,"+id +")" );


        GOTPoint GOTpoint;
        GOTpoint = new GOTPoint(1,"Orlica",1084,1);
        db.execSQL("INSERT INTO " + GOT_POINT_TABLE + "(NAME,HEIGHT,MOUNTAIN_CHAIN_ID) VALUES('Orlica',1084,"+id +")" );


    }

    // this is called when the database (version) changes
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public boolean addOne(GOTPoint point) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues(); //  tablica asocjacyjna/hashmap valuename-content

        cv.put(COLUMN_NAME, point.getName());
        cv.put(COLUMN_HEIGHT, point.getHeight());
        cv.put(COLUMN_MOUNTAIN_CHAIN_ID, point.getMountainChainId());

        long insert = db.insert(GOT_POINT_TABLE, null, cv);
        db.close();

        return insert == -1 ? false : true;
    }

    public int getMountainRangeId(String mountainRangeName) {
        int id;
        String queryString = "SELECT ID FROM " + MOUNTAIN_RANGE_TABLE + " WHERE NAME LIKE 'Sudety'";

        SQLiteDatabase db = this.getReadableDatabase(); // kiedy jest readable do robi na niej locka

        Cursor cursor = db. rawQuery(queryString,null);
        id = cursor.getInt(0);

        cursor.close();
        db.close();
        return id;

    }

    public List<GOTPoint> getAllGOTPoints() {
        List<GOTPoint> list = new ArrayList<>();

        String queryString = "SELECT * FROM " + GOT_POINT_TABLE;

        SQLiteDatabase db = this.getReadableDatabase(); // kiedy jest readable do robi na niej locka

        Cursor cursor = db.rawQuery(queryString,null);

        if (cursor.moveToFirst()) {//move to the first result of resultset - if true there were results
            do {
                int GOTPointId = cursor.getInt(0);
                String name = cursor.getString(1);
                int height = cursor.getInt(2);
                int mountainChainId = cursor.getInt(3);

                GOTPoint point = new GOTPoint(GOTPointId,name,height,mountainChainId);
                list.add(point);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return list;
    }

}
