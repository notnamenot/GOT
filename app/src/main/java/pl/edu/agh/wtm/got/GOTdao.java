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
import pl.edu.agh.wtm.got.models.MountainChain;
import pl.edu.agh.wtm.got.models.MountainRange;
import pl.edu.agh.wtm.got.models.Subroute;

//View -> Tool Windows -> Device File Explorer -> data -> data
//https://www.youtube.com/watch?v=hDSVInZ2JCs
//handles operations on database
public class GOTdao extends SQLiteOpenHelper{

    public static final String MOUNTAIN_RANGE_TABLE = "MOUNTAIN_RANGE_TABLE";
    public static final String MOUNTAIN_CHAIN_TABLE = "MOUNTAIN_CHAIN_TABLE";
    public static final String GOT_POINT_TABLE = "GOT_POINT_TABLE";
    public static final String ROUTE_TABLE = "ROUTE_TABLE";

    public static final String COLUMN_ID = "ID";
    public static final String COLUMN_MOUNTAIN_RANGE_ID = "MOUNTAIN_RANGE_ID";
    public static final String COLUMN_MOUNTAIN_CHAIN_ID = "MOUNTAIN_CHAIN_ID";
    public static final String COLUMN_HEIGHT = "HEIGHT";
    public static final String COLUMN_NAME = "NAME";
    public static final String COLUMN_LENGTH = "LENGTH";
    public static final String COLUMN_TIME = "TIME";
    public static final String COLUMN_PEAK = "PEAK";
    public static final String COLUMN_POINTS = "POINTS";
    public static final String COLUMN_FROM = "GOT_POINT_FROM";
    public static final String COLUMN_TO = "GOT_POINT_TO";
    public static final String COLUMN_SUM_DOWNS = "SUM_DOWNS";
    public static final String COLUMN_SUM_UPS = "SUM_UPS";

    // łańcuch górski - np. Sudety
    public static final String CREATE_MOUNTAIN_RANGE_TABLE_STATEMENT = "CREATE TABLE " + MOUNTAIN_RANGE_TABLE + " (" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            COLUMN_NAME + " TEXT," +
            COLUMN_LENGTH + " INTEGER," +
            COLUMN_PEAK + " TEXT)"; // id?

    public static final String CREATE_MOUNTAIN_CHAIN_TABLE_STATEMENT = "CREATE TABLE " + MOUNTAIN_CHAIN_TABLE + " (" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            COLUMN_NAME + " TEXT," +
            COLUMN_LENGTH + " INTEGER," +
            COLUMN_MOUNTAIN_RANGE_ID + " INTEGER)";

    public static final String CREATE_GOT_POINT_TABLE_STATEMENT = "CREATE TABLE " + GOT_POINT_TABLE + " (" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            COLUMN_NAME + " TEXT," +
            COLUMN_HEIGHT + " INTEGER," +
            COLUMN_MOUNTAIN_CHAIN_ID + " INTEGER)";

    public static final String CREATE_ROUTE_TABLE_STATEMENT = "CREATE TABLE " + ROUTE_TABLE + " (" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            COLUMN_FROM + " INTEGER," + // GOTPointId
            COLUMN_TO + " INTEGER," +   //GOTPointId
            COLUMN_POINTS + " INTEGER," +
            COLUMN_LENGTH + " REAL," +
            COLUMN_TIME + " INTEGER," +
            COLUMN_SUM_UPS + " INTEGER," +
            COLUMN_SUM_DOWNS + " INTEGER)";

    private  Context context;

    // context - reference to apliccation itself, name of db to create
//    public GOTdao(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
    public GOTdao(@Nullable Context context) {
        super(context, "GOT.db", null, 2);
        this.context = context;
    }

    //this is called the first time a database  is accessed
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_MOUNTAIN_RANGE_TABLE_STATEMENT);
        db.execSQL(CREATE_MOUNTAIN_CHAIN_TABLE_STATEMENT);
        db.execSQL(CREATE_GOT_POINT_TABLE_STATEMENT);
        db.execSQL(CREATE_ROUTE_TABLE_STATEMENT);

//        ContentValues cv = new ContentValues(); //  tablica asocjacyjna/hashmap valuename-content
//        cv.put(COLUMN_NAME, "Sudety");
//        cv.put(COLUMN_LENGTH, 300);
//        cv.put(COLUMN_PEAK, "Śnieżka");
//        db.insert(MOUNTAIN_RANGE_TABLE, null,cv);
//
//
        fillMountainRanges(db);
        fillMountainChains(db);
        fillGOTPoints(db);
        fillSubroutes(db);
    }

    // this is called when the database (version) changes
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public boolean deleteOne(GOTPoint point){
        //if found and deleted return true else false
        SQLiteDatabase db = this.getWritableDatabase();

        String queryString = "DELETE FROM " + GOT_POINT_TABLE + " WHERE " + COLUMN_ID + " = " + point.getId();

        Cursor cursor = db.rawQuery(queryString,null);

        if (cursor.moveToFirst()) {
            cursor.close();
            return true;
        }
        else
            cursor.close();
            return false;
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


    public List<MountainRange> getAllMountainRanges() {
        List<MountainRange> list = new ArrayList<>();

        String queryString = "SELECT * FROM " + MOUNTAIN_RANGE_TABLE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString,null);
        if (cursor.moveToFirst()) {//move to the first result of resultset - if true there were results
            do {
                int mountainRangeId = cursor.getInt(0);
                String name = cursor.getString(1);
                int length = cursor.getInt(2);
                String peak = cursor.getString(3);

                MountainRange range = new MountainRange(mountainRangeId,name,length,peak);
                list.add(range);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return list;
    }

    public List<MountainChain> getMountainChains(int _mountainRangeId) {
        List<MountainChain> list = new ArrayList<>();

        String queryString = "SELECT * FROM " + MOUNTAIN_CHAIN_TABLE + " WHERE " + COLUMN_MOUNTAIN_RANGE_ID + " = " + _mountainRangeId ;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString,null);
        if (cursor.moveToFirst()) {
            do {
                int mountainChainId = cursor.getInt(0);
                String name = cursor.getString(1);
                int length = cursor.getInt(2);
                int mountainRangeId = cursor.getInt(3);

                MountainChain range = new MountainChain(mountainChainId,name,length,mountainRangeId);
                list.add(range);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return list;
    }

    public List<GOTPoint> getAllGOTPoints() {
        List<GOTPoint> list = new ArrayList<>();

        String queryString = "SELECT * FROM " + GOT_POINT_TABLE;
        System.out.println(queryString);

        SQLiteDatabase db = this.getReadableDatabase(); // kiedy jest Writeable do robi na niej locka

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

    public List<GOTPoint> getGOTPoints(int _mountainChainId) {
        List<GOTPoint> list = new ArrayList<>();

        String queryString = "SELECT * FROM " + GOT_POINT_TABLE + " WHERE " + COLUMN_MOUNTAIN_CHAIN_ID + " = " + _mountainChainId;

        System.out.println(queryString);

        SQLiteDatabase db = this.getReadableDatabase(); // kiedy jest Writeable do robi na niej locka

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

    private void fillMountainRanges(SQLiteDatabase db) {
        List<MountainRange> mountainRanges = new ArrayList<>();
        mountainRanges.add(new MountainRange(0, getResource(R.string.r_sudety),300, getResource(R.string.sniezka)));
        mountainRanges.add(new MountainRange(0, getResource(R.string.r_karpaty),1500, getResource(R.string.gerlach)));
        mountainRanges.add(new MountainRange(0, getResource(R.string.r_gory_swietokrzyskie),70, getResource(R.string.lysica)));

        insertMountainRanges(db, mountainRanges);
//        db.execSQL(makeInsertQuery(MOUNTAIN_RANGE_TABLE,new String[] {getResource(R.string.r_sudety),Integer.toString(300),getResource(R.string.sniezka)}));
    }

    private void fillMountainChains(SQLiteDatabase db) {
        int mountainRangeId = getMountainRangeId(db, getResource(R.string.r_sudety));
        System.out.println("mountainRangeId" + mountainRangeId);

        List<MountainChain> mountainChains = new ArrayList<>();
        mountainChains.add(new MountainChain(0,getResource(R.string.c_gory_orlickie), 50, mountainRangeId));
        mountainChains.add(new MountainChain(0,getResource(R.string.c_gory_bystrzyckie), 40, mountainRangeId));
        mountainChains.add(new MountainChain(0,getResource(R.string.c_gory_stolowe), 100, mountainRangeId));

        insertMountainChains(db, mountainChains);
    }

    private void fillGOTPoints(SQLiteDatabase db) {
        int mountainChainId = getMountainChainId(db, getResource(R.string.c_gory_orlickie));
        System.out.println("mountainChainId" + mountainChainId);

        List<GOTPoint> gotPoints = new ArrayList<GOTPoint>();
        gotPoints.add(new GOTPoint(0, getResource(R.string.orlica),1084, mountainChainId));
        gotPoints.add(new GOTPoint(0, getResource(R.string.soltysia_kopa),896, mountainChainId));
        gotPoints.add(new GOTPoint(0, getResource(R.string.sch_zieleniec),850, mountainChainId));

        insertGOTPoints(db, gotPoints);
    }

    private void fillSubroutes(SQLiteDatabase db) {
        int orlica = getGOTPointId(db,getResource(R.string.orlica)); // TPG - turystyczne przejście graniczne
        int soltysia_kopa = getGOTPointId(db,getResource(R.string.soltysia_kopa));
        int sch_zieleniec = getGOTPointId(db,getResource(R.string.sch_zieleniec));

        List<Subroute> subroutes = new ArrayList<>();
        subroutes.add(new Subroute(0, soltysia_kopa, orlica,2,1.6,45,177,1));
        subroutes.add(new Subroute(0, orlica, soltysia_kopa,2,1.6,25,1,177));
        subroutes.add(new Subroute(0, orlica,sch_zieleniec,3,3.6,55,227,11));
        subroutes.add(new Subroute(0, sch_zieleniec,orlica,5,3.6,75,227,11));
        subroutes.add(new Subroute(0, sch_zieleniec,soltysia_kopa,3,4.6,90,201,161));
        subroutes.add(new Subroute(0, soltysia_kopa,sch_zieleniec,6,4.6,25,161,201));

        insertSubroutes(db,subroutes);
    }

    private void insertMountainRanges(SQLiteDatabase db, List<MountainRange> mountainRanges) {
        ContentValues cv = new ContentValues();
        for (MountainRange mountainRange : mountainRanges) {
            cv.put(COLUMN_NAME, mountainRange.getName());
            cv.put(COLUMN_LENGTH, mountainRange.getLength());
            cv.put(COLUMN_PEAK, mountainRange.getPeak());
            db.insert(MOUNTAIN_RANGE_TABLE,null,cv);
            cv.clear();
        }
    }

    private void insertMountainChains(SQLiteDatabase db, List<MountainChain> mountainChains) {
        ContentValues cv = new ContentValues();
        for (MountainChain mountainChain : mountainChains) {
            cv.put(COLUMN_NAME, mountainChain.getName());
            cv.put(COLUMN_LENGTH, mountainChain.getLength());
            cv.put(COLUMN_MOUNTAIN_RANGE_ID, mountainChain.getMountainRangeId());

            db.insert(MOUNTAIN_CHAIN_TABLE,null,cv);
            cv.clear();
        }
    }
    // TODO funkcja ktora dostaje listę kolumn i listę obiektów do wstawienia
    private void insertGOTPoints(SQLiteDatabase db, List<GOTPoint> gotPoints) { // TODO czy db może dależeć do klasy?
        ContentValues cv = new ContentValues();
        for (GOTPoint gotPoint : gotPoints) {
            cv.put(COLUMN_NAME, gotPoint.getName());
            cv.put(COLUMN_HEIGHT, gotPoint.getHeight());
            cv.put(COLUMN_MOUNTAIN_CHAIN_ID, gotPoint.getMountainChainId());

            db.insert(GOT_POINT_TABLE,null,cv);
            cv.clear();
        }
    }

    private void insertSubroutes(SQLiteDatabase db, List<Subroute> subroutes) {
        ContentValues cv = new ContentValues();
        for (Subroute subroute : subroutes) {
            cv.put(COLUMN_FROM, subroute.getFrom());
            cv.put(COLUMN_TO, subroute.getTo());
            cv.put(COLUMN_LENGTH, subroute.getLength());
            cv.put(COLUMN_TIME, subroute.getTime());
            cv.put(COLUMN_POINTS, subroute.getPoints());
            cv.put(COLUMN_SUM_UPS, subroute.getSum_ups());
            cv.put(COLUMN_SUM_DOWNS, subroute.getSum_downs());

            db.insert(ROUTE_TABLE,null,cv);
            cv.clear();
        }
    }

    private int getMountainRangeId(SQLiteDatabase db, String mountainRangeName) {
        int id;
        String queryString = "SELECT ID FROM " + MOUNTAIN_RANGE_TABLE + " WHERE NAME = '" + mountainRangeName + "'";

        System.out.println(queryString);

        Cursor cursor = db. rawQuery(queryString,null);
        if (cursor.moveToFirst()) {
            id = cursor.getInt(0);
        }
        else {
            id = 1;
        }

        cursor.close();
//        db.close();
        return id;
    }

    private int getMountainChainId(SQLiteDatabase db, String mountainChainName) {
        String queryString = "SELECT ID FROM " + MOUNTAIN_CHAIN_TABLE + " WHERE NAME = '" + mountainChainName + "'";

        System.out.println(queryString);
        int id;
        Cursor cursor = db. rawQuery(queryString,null);
        if (cursor.moveToFirst()) {
            id = cursor.getInt(0);
        }
        else {
            id = 1;
        }
        cursor.close();

        return id;
    }

    private int getGOTPointId(SQLiteDatabase db, String GOTPointName) {
        String queryString = "SELECT ID FROM " + GOT_POINT_TABLE + " WHERE NAME = '" + GOTPointName + "'";
        int id;

        System.out.println(queryString);
        Cursor cursor = db.rawQuery(queryString,null);
        if (cursor.moveToFirst()) {
            id = cursor.getInt(0);
            System.out.println("getGOTPointId: got id from cursor" + id );
        }
        else {
            id = 1;
            System.out.println("getGOTPointId : didnt get id from cursor" );
        }
        cursor.close();

        return id;
    }



    private String getResource(int resource) {
        return this.context.getString(resource);

    }

}
