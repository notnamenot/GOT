package pl.edu.agh.wtm.got;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
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
    public static final String ROUTE_TABLE = "ROUTE_TABLE";

    public static final String COLUMN_ID = "ID";
    public static final String COLUMN_MOUNTAIN_RANGE_ID = "MOUNTAIN_RANGE_ID";
    public static final String COLUMN_MOUNTAIN_CHAIN_ID = "MOUNTAIN_CHAIN_ID";
    public static final String COLUMN_HEIGHT = "HEIGHT";
    public static final String COLUMN_NAME = "NAME";
    public static final String COLUMN_LENGTH = "LENGTH";
    public static final String COLUMN_PEAK = "PEAK";
//    public static final Object COLUMN_TARGET_NAME = "TARGET_NAME";
    public static final Object COLUMN_POINTS = "POINTS";
    public static final String COLUMN_FROM = "GOT_POINT_FROM";
    public static final String COLUMN_TO = "GOT_POINT_TO";

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
//                COLUMN_TARGET_NAME + " TEXT," + // docelowo powino być ID
//                COLUMN_POINTS + " INTEGER)";

        String createRouteTableStatement = "CREATE TABLE " + ROUTE_TABLE + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_FROM + " INTEGER," + // GOTPointId
                COLUMN_TO + " INTEGER," +   //GOTPointId
                COLUMN_POINTS + " INTEGER," +
                COLUMN_LENGTH + " REAL)";

        db.execSQL(createMountainRangeTableStatement);
        db.execSQL(createMountainChainTableStatement);
        db.execSQL(createGOTPointTableStatement);
        db.execSQL(createRouteTableStatement);

//        ContentValues cv = new ContentValues(); //  tablica asocjacyjna/hashmap valuename-content
//        cv.put(COLUMN_NAME, "Sudety");
//        cv.put(COLUMN_LENGTH, 300);
//        cv.put(COLUMN_PEAK, "Śnieżka");
//        db.insert(MOUNTAIN_RANGE_TABLE, null,cv);
//
//        int id = 1;//getMountainRangeId("Sudety");
//        db.execSQL("INSERT INTO " + MOUNTAIN_CHAIN_TABLE + "(NAME,LENGTH,MOUNTAIN_RANGE_ID) VALUES('Góry Orlickie',50,"+id +")" );
//
//
//        GOTPoint GOTpoint;
//        GOTpoint = new GOTPoint(1,"Orlica",1084,1);
//        db.execSQL("INSERT INTO " + GOT_POINT_TABLE + "(NAME,HEIGHT,MOUNTAIN_CHAIN_ID) VALUES('Orlica',1084,"+id +")" );

        fillMountainRanges(db);
        fillMountainChains(db);
        fillGOTPoints(db);
        fillRoutes(db);
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



    public List<GOTPoint> getAllGOTPoints() {
        List<GOTPoint> list = new ArrayList<>();

        String queryString = "SELECT * FROM " + GOT_POINT_TABLE;

        System.out.println(queryString);

        SQLiteDatabase db = this.getReadableDatabase(); // kiedy jest Writeable do robi na niej locka

        Cursor cursor = db.rawQuery(queryString,null);

        if (cursor.moveToFirst()) {//move to the first result of resultset - if true there were results
            System.out.println("cursor not empty");
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
        //String queryString = "INSERT INTO " + MOUNTAIN_RANGE_TABLE + "(NAME,LENGTH,PEAK) VALUES('Sudety',300,'Śnieżka')";
        //String insertQuery;

        //insertQuery = makeInsertQuery(MOUNTAIN_RANGE_TABLE,new String[] {"Sudety",Integer.toString(300),"Śnieżka"});
        db.execSQL(makeInsertQuery(MOUNTAIN_RANGE_TABLE,new String[] {getResource(R.string.r_sudety),Integer.toString(300),getResource(R.string.sniezka)}));
        db.execSQL(makeInsertQuery(MOUNTAIN_RANGE_TABLE,new String[] {getResource(R.string.r_karpaty),Integer.toString(1500),getResource(R.string.gerlach)}));
        db.execSQL(makeInsertQuery(MOUNTAIN_RANGE_TABLE,new String[] {getResource(R.string.r_gory_swietokrzyskie),Integer.toString(70),getResource(R.string.lysica)}));
    }

    private void fillMountainChains(SQLiteDatabase db) {
        int mountainRangeId = getMountainRangeId(db, getResource(R.string.r_sudety));
        System.out.println("mountainRangeId" + mountainRangeId);

        db.execSQL(makeInsertQuery(MOUNTAIN_CHAIN_TABLE,new String[] {getResource(R.string.c_gory_orlickie),Integer.toString(50),Integer.toString(mountainRangeId)}));
        db.execSQL(makeInsertQuery(MOUNTAIN_CHAIN_TABLE,new String[] {getResource(R.string.c_gory_bystrzyckie),Integer.toString(40),Integer.toString(mountainRangeId)}));
        db.execSQL(makeInsertQuery(MOUNTAIN_CHAIN_TABLE,new String[] {getResource(R.string.c_gory_stolowe),Integer.toString(100),Integer.toString(mountainRangeId)}));

//        mountainRangeId = getMountainRangeId(db, getResource(R.string.r_karpaty));
    }

    private void fillGOTPoints(SQLiteDatabase db) {
        int mountainChainId = getMountainChainId(db, getResource(R.string.c_gory_orlickie));

        System.out.println("mountainChainId" + mountainChainId);

        db.execSQL(makeInsertQuery(GOT_POINT_TABLE,new String[] {getResource(R.string.orlica),Integer.toString(1084),Integer.toString(mountainChainId)}));
        db.execSQL(makeInsertQuery(GOT_POINT_TABLE,new String[] {getResource(R.string.soltysia_kopa),Integer.toString(896),Integer.toString(mountainChainId)}));
        db.execSQL(makeInsertQuery(GOT_POINT_TABLE,new String[] {getResource(R.string.sch_zieleniec),Integer.toString(850),Integer.toString(mountainChainId)}));
    }

    private String getResource(int resource) {
        return this.context.getString(resource);

    }

    private void fillRoutes(SQLiteDatabase db) {
        int orlica = getGOTPointId(db,getResource(R.string.orlica)); // TPG - turystyczne przejście graniczne
        int soltysia_kopa = getGOTPointId(db,getResource(R.string.soltysia_kopa));
        int sch_zieleniec = getGOTPointId(db,getResource(R.string.sch_zieleniec));

        db.execSQL(makeInsertQuery(ROUTE_TABLE,new String[] {Integer.toString(orlica),Integer.toString(soltysia_kopa),Integer.toString(2), Double.toString(1.6)}));
        db.execSQL(makeInsertQuery(ROUTE_TABLE,new String[] {Integer.toString(soltysia_kopa),Integer.toString(orlica),Integer.toString(4), Double.toString(1.6)}));
        db.execSQL(makeInsertQuery(ROUTE_TABLE,new String[] {Integer.toString(orlica),Integer.toString(sch_zieleniec),Integer.toString(3), Double.toString(3.6)}));
        db.execSQL(makeInsertQuery(ROUTE_TABLE,new String[] {Integer.toString(sch_zieleniec),Integer.toString(orlica),Integer.toString(5), Double.toString(3.6)}));
        db.execSQL(makeInsertQuery(ROUTE_TABLE,new String[] {Integer.toString(sch_zieleniec),Integer.toString(soltysia_kopa),Integer.toString(3), Double.toString(4.6)}));
        db.execSQL(makeInsertQuery(ROUTE_TABLE,new String[] {Integer.toString(soltysia_kopa),Integer.toString(sch_zieleniec),Integer.toString(6), Double.toString(4.6)}));
    }





    public int getMountainRangeId(SQLiteDatabase db, String mountainRangeName) {
        int id;
        String queryString = "SELECT ID FROM " + MOUNTAIN_RANGE_TABLE + " WHERE NAME = '" + mountainRangeName + "'";

        System.out.println(queryString);
//        SQLiteDatabase db = this.getReadableDatabase(); // kiedy jest readable do robi na niej locka

        Cursor cursor = db. rawQuery(queryString,null);
        if (cursor.moveToFirst()) {
            id = cursor.getInt(0);
//            System.out.println("got id from cursor" + id );
        }
        else {
            id = 1;
//            System.out.println("didnt get id from cursor" );
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
//            System.out.println("got id from cursor" + id );
        }
        else {
            id = 1;
//            System.out.println("didnt get id from cursor" );
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

    private String makeInsertQuery(String table, String[] values) {
        switch (table) {
            case MOUNTAIN_RANGE_TABLE:
                return "INSERT INTO " + table + "(" + COLUMN_NAME + "," + COLUMN_LENGTH + "," + COLUMN_PEAK + ") VALUES('" + values[0] + "'," + values[1] + ",'" + values[2] + "')";
            case MOUNTAIN_CHAIN_TABLE:
                return "INSERT INTO " + table + "(" + COLUMN_NAME + "," + COLUMN_LENGTH + "," + COLUMN_MOUNTAIN_RANGE_ID + ") VALUES('" + values[0] + "'," + values[1] + "," + values[2] + ")";
            case GOT_POINT_TABLE:
                return "INSERT INTO " + table + "(" + COLUMN_NAME + "," + COLUMN_HEIGHT + "," + COLUMN_MOUNTAIN_CHAIN_ID + ") VALUES('" + values[0] + "'," + values[1] + "," + values[2] + ")";
            case ROUTE_TABLE:
                for (String s : values) System.out.println(s);
                return "INSERT INTO " + table + "("+ COLUMN_FROM + "," + COLUMN_TO + "," + COLUMN_POINTS + "," + COLUMN_LENGTH + ") VALUES (" + values[0] + "," + values[1] + "," + values[2] + "," + values[3] +")";
            default:
                return null;
        }
    }

}
