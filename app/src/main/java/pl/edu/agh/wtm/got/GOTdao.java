package pl.edu.agh.wtm.got;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.Nullable;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import pl.edu.agh.wtm.got.models.GOTPoint;
import pl.edu.agh.wtm.got.models.MountainChain;
import pl.edu.agh.wtm.got.models.MountainRange;
import pl.edu.agh.wtm.got.models.Route;
import pl.edu.agh.wtm.got.models.Subroute;
import pl.edu.agh.wtm.got.models.Trip;

//View -> Tool Windows -> Device File Explorer -> data -> data
//https://www.youtube.com/watch?v=hDSVInZ2JCs
//handles operations on database
public class GOTdao extends SQLiteOpenHelper{

    public static final String MOUNTAIN_RANGE_TABLE = "MOUNTAIN_RANGE_TABLE";
    public static final String MOUNTAIN_CHAIN_TABLE = "MOUNTAIN_CHAIN_TABLE";
    public static final String GOT_POINT_TABLE = "GOT_POINT_TABLE";
    public static final String SUBROUTE_TABLE = "SUBROUTE_TABLE";
    public static final String TRIP_TABLE = "TRIP_TABLE";

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
    public static final String COLUMN_DATE = "DATE_INS";

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

    public static final String CREATE_SUBROUTE_TABLE_STATEMENT = "CREATE TABLE " + SUBROUTE_TABLE + " (" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            COLUMN_FROM + " INTEGER," + // GOTPointId
            COLUMN_TO + " INTEGER," +   //GOTPointId
            COLUMN_POINTS + " INTEGER," +
            COLUMN_LENGTH + " REAL," +
            COLUMN_TIME + " INTEGER," +
            COLUMN_SUM_UPS + " INTEGER," +
            COLUMN_SUM_DOWNS + " INTEGER)";

    public static final String CREATE_TRIP_TABLE_STATEMENT = "CREATE TABLE " + TRIP_TABLE + " (" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            COLUMN_FROM + " INTEGER," + // first GOTPointId
            COLUMN_TO + " INTEGER," +   //last GOTPointId
            COLUMN_POINTS + " INTEGER," +
            COLUMN_LENGTH + " REAL," +
            COLUMN_TIME + " INTEGER," +
            COLUMN_SUM_UPS + " INTEGER," +
            COLUMN_SUM_DOWNS + " INTEGER," +
//            COLUMN_DATE + " INTEGER)";
            COLUMN_DATE + " TEXT)";
//            COLUMN_DATE + " created_at DATETIME DEFAULT CURRENT_TIMESTAMP)";

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
        db.execSQL(CREATE_SUBROUTE_TABLE_STATEMENT);
        db.execSQL(CREATE_TRIP_TABLE_STATEMENT);

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
        else {
            cursor.close();
            return false;
        }
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

    public GOTPoint getGOTPoint(int gotPointId_) {

//        private int getMountainRangeId(SQLiteDatabase db, String mountainRangeName) {
//            int id;
            String queryString = "SELECT * FROM " + GOT_POINT_TABLE + " WHERE ID = " + gotPointId_;
//
            System.out.println(queryString);
//
        SQLiteDatabase db = this.getReadableDatabase();

        GOTPoint point = null;
        Cursor cursor = db. rawQuery(queryString,null);
        if (cursor.moveToFirst()) {//move to the first result of resultset - if true there were results
//            do {
                int GOTPointId = cursor.getInt(0);
                String name = cursor.getString(1);
                int height = cursor.getInt(2);
                int mountainChainId = cursor.getInt(3);

            point = new GOTPoint(GOTPointId,name,height,mountainChainId);
//            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return point;
    }

    public List<Subroute> getSubroutes(int _mountainChainId) {
        List<Subroute> list = new ArrayList<>();

        String queryString = "SELECT * FROM " + SUBROUTE_TABLE + " WHERE " +
                COLUMN_FROM + " in ( SELECT " + COLUMN_ID + " FROM " + GOT_POINT_TABLE + " WHERE " +
                    COLUMN_MOUNTAIN_CHAIN_ID + " = " + _mountainChainId + ")";

        System.out.println(queryString);

        SQLiteDatabase db = this.getReadableDatabase(); // kiedy jest Writeable do robi na niej locka

        Cursor cursor = db.rawQuery(queryString,null);

        if (cursor.moveToFirst()) {//move to the first result of resultset - if true there were results
            do {
                int subrouteIid = cursor.getInt(0);
                int gotPointFrom = cursor.getInt(1);
                int gotPointTo = cursor.getInt(2);
                int points = cursor.getInt(3);
                double length = cursor.getDouble(4);
                int time = cursor.getInt(5);
                int ups = cursor.getInt(6);
                int downs = cursor.getInt(7);

                Subroute subroute = new Subroute(subrouteIid,gotPointFrom,gotPointTo,points,length,time,ups,downs);
                list.add(subroute);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return list;
    }


    public Subroute getSubroute(int from_, int to_) {
//        List<Subroute> list = new ArrayList<>();

        String queryString = "SELECT * FROM " + SUBROUTE_TABLE + " WHERE " +
                COLUMN_FROM + " = ? and " + COLUMN_TO + " = ?";

        String[] selectionArgs = new String[] {String.valueOf(from_), String.valueOf(to_)};

        System.out.println(queryString);

        SQLiteDatabase db = this.getReadableDatabase(); // kiedy jest Writeable do robi na niej locka

        Subroute subroute = null;
        Cursor cursor = db.rawQuery(queryString,selectionArgs);
        if (cursor.moveToFirst()) {//move to the first result of resultset - if true there were results
//            do {
                int subrouteId = cursor.getInt(0); //cursor.getInt(cursor.getColumnIndex("id"));
                int gotPointFrom = cursor.getInt(1);
                int gotPointTo = cursor.getInt(2);
                int points = cursor.getInt(3);
                double length = cursor.getDouble(4);
                int time = cursor.getInt(5);
                int ups = cursor.getInt(6);
                int downs = cursor.getInt(7);

                subroute = new Subroute(subrouteId,gotPointFrom,gotPointTo,points,length,time,ups,downs);
//                list.add(subroute);
//            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return subroute;
    }

    public boolean insertTrip(Route route) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues(); //  tablica asocjacyjna/hashmap valuename-content

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String today = sdf.format((new Date()));
        System.out.println("today " + today);

        cv.put(COLUMN_FROM, route.getGotPoints().get(0).getId());
        cv.put(COLUMN_TO, route.getGotPoints().get(route.getGotPoints().size()-1).getId());
        cv.put(COLUMN_POINTS, route.getPoints());
        cv.put(COLUMN_LENGTH, route.getLength());
        cv.put(COLUMN_TIME, route.getTime());
        cv.put(COLUMN_SUM_UPS, route.getUps());
        cv.put(COLUMN_SUM_DOWNS, route.getDowns());
        cv.put(COLUMN_DATE, today);

        long insert = db.insert(TRIP_TABLE, null, cv);
        db.close();

        return insert == -1 ? false : true;
    }

    public List<Trip> getAllTrips() {
        List<Trip> list = new ArrayList<>();

        String queryString = "SELECT * FROM " + TRIP_TABLE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString,null);
        if (cursor.moveToFirst()) {//move to the first result of resultset - if true there were results
            do {
                int tripId = cursor.getInt(0); //cursor.getInt(cursor.getColumnIndex("id"));
                int gotPointFrom = cursor.getInt(1);
                int gotPointTo = cursor.getInt(2);
                int points = cursor.getInt(3);
                double length = cursor.getDouble(4);
                int time = cursor.getInt(5);
                int sumUps = cursor.getInt(6);
                int sumDowns = cursor.getInt(7);
                String date = cursor.getString(8);

                Trip trip = new Trip(tripId,gotPointFrom,gotPointTo,length,time,points,sumUps,sumDowns,date);
                list.add(trip);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return list;
    }


    public int getGotPointsCnt(int mountainChainId) {
//        String queryString = "SELECT DISTINCT " + COLUMN_FROM + ", COUNT(*)" + " FROM " + ROUTE_TABLE + " WHERE " +
//                COLUMN_FROM + " in ( SELECT " + COLUMN_ID + " FROM " + GOT_POINT_TABLE + " WHERE " +
//                COLUMN_MOUNTAIN_CHAIN_ID + " = " + _mountainChainId + ")";

        String queryString = "SELECT COUNT(*) FROM " + GOT_POINT_TABLE + " WHERE " + COLUMN_MOUNTAIN_CHAIN_ID + " = " + mountainChainId;

        SQLiteDatabase db = this.getReadableDatabase();

        System.out.println(queryString);
        int cnt;
        Cursor cursor = db.rawQuery(queryString,null);
        if (cursor.moveToFirst()) {
            cnt = cursor.getInt(0);
        }
        else {
            cnt = 0;
        }
        cursor.close();
        db.close();
        System.out.println(cnt);
        return cnt;
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
        gotPoints.add(new GOTPoint(0, getResource(R.string.kozia_hala),740, mountainChainId));
        gotPoints.add(new GOTPoint(0, getResource(R.string.duszniki_zdroj),533, mountainChainId));
        gotPoints.add(new GOTPoint(0, getResource(R.string.duszniki_zdroj_park),540, mountainChainId));
        gotPoints.add(new GOTPoint(0, getResource(R.string.prz_polskie_wrota),660, mountainChainId));
        gotPoints.add(new GOTPoint(0, getResource(R.string.ludowe),675, mountainChainId));

        insertGOTPoints(db, gotPoints);
    }

    private void fillSubroutes(SQLiteDatabase db) {
        int orlica = getGOTPointId(db,getResource(R.string.orlica)); // TPG - turystyczne przejście graniczne
        int soltysia_kopa = getGOTPointId(db,getResource(R.string.soltysia_kopa));
        int sch_zieleniec = getGOTPointId(db,getResource(R.string.sch_zieleniec));
        int kozia_hala = getGOTPointId(db,getResource(R.string.kozia_hala));
        int duszniki_zdroj = getGOTPointId(db,getResource(R.string.duszniki_zdroj));
        int duszniki_zdroj_park = getGOTPointId(db,getResource(R.string.duszniki_zdroj_park));
        int prz_polskie_wrota = getGOTPointId(db,getResource(R.string.prz_polskie_wrota));
        int ludowe = getGOTPointId(db,getResource(R.string.ludowe));

        List<Subroute> subroutes = new ArrayList<>();
        subroutes.add(new Subroute(0, soltysia_kopa,orlica,2,1.6,45,177,1));
        subroutes.add(new Subroute(0, orlica,soltysia_kopa,2,1.6,25,1,177));
        subroutes.add(new Subroute(0, orlica,sch_zieleniec,3,3.6,55,11,227));
        subroutes.add(new Subroute(0, sch_zieleniec,orlica,5,3.6,75,227,11));
//        subroutes.add(new Subroute(0, sch_zieleniec,soltysia_kopa,3,4.6,90,201,161)); // po drodze jest orlica!
//        subroutes.add(new Subroute(0, soltysia_kopa,sch_zieleniec,6,4.6,25,161,201));
        subroutes.add(new Subroute(0, soltysia_kopa,kozia_hala,3,3.1,45,26,191));
        subroutes.add(new Subroute(0, kozia_hala,soltysia_kopa,5,3.1,65,191,26));
        subroutes.add(new Subroute(0, duszniki_zdroj_park,kozia_hala,5,3.4,70,237,34));
        subroutes.add(new Subroute(0, kozia_hala,duszniki_zdroj_park,3,3.4,55,34,237));
        subroutes.add(new Subroute(0, duszniki_zdroj_park,duszniki_zdroj,1,1.2,20,15,22));
        subroutes.add(new Subroute(0, duszniki_zdroj,duszniki_zdroj_park,1,1.2,20,22,15));
        subroutes.add(new Subroute(0, kozia_hala,prz_polskie_wrota,2,2.1,40,42,125));
        subroutes.add(new Subroute(0, prz_polskie_wrota,kozia_hala,3,2.1,45,125,42));
        subroutes.add(new Subroute(0, prz_polskie_wrota,ludowe,0,0.3,5,19,4));
        subroutes.add(new Subroute(0, ludowe,prz_polskie_wrota,0,0.3,5,4,19));
        subroutes.add(new Subroute(0, ludowe,duszniki_zdroj,3,3.3,50,22,164));
        subroutes.add(new Subroute(0, duszniki_zdroj,ludowe,5,3.3,65,164,22));

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
    private void insertGOTPoints(SQLiteDatabase db, List<GOTPoint> gotPoints) { // TODO czy db może należeć do klasy?
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
            cv.put(COLUMN_SUM_UPS, subroute.getUps());
            cv.put(COLUMN_SUM_DOWNS, subroute.getDowns());

            db.insert(SUBROUTE_TABLE,null,cv);
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
            id = 0;
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
