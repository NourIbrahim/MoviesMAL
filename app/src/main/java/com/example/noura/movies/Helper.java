package com.example.noura.movies;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by noura on 16/01/16.
 */
public class Helper extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 20;
    // Database Name
    public static final String DATABASE_NAME = "Favorites.db";


    public Helper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }



    @Override
    public void onCreate(SQLiteDatabase db) {
        // SQL statement to create book table
        String CREATE_FAVOR_TABLE = "CREATE TABLE '" + TABLE_N+ "'( " +
              /*  KEY_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, " +
                KEY_TITLE+" title TEXT, "+
                KEY_IMAGE+" image TEXT, "+
               KEY_OVERVIEW+"overview TEXT,"+
               KEY_VOTE_AVERAGE+"rate INTEGER ,"+
               KEY_RELEASE_DATA+"data TEXT );";*/
                KEY_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, " +
                KEY_TITLE+"  TEXT, "+
                KEY_IMAGE+"  TEXT, "+
                KEY_OVERVIEW+" TEXT,"+
                KEY_VOTE_AVERAGE+" INTEGER ,"+
                KEY_RELEASE_DATA+" TEXT );";
            //    KEY_BACK+"TEXT

        // create books table
        db.execSQL(CREATE_FAVOR_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older books table if existed
        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_N);

        // create fresh books table

        this.onCreate(db);
       

    }
    //---------------------------------------------------------------------

    /**
     * CRUD operations (create "add", read "get", update, delete) book + get all books + delete all books
     */

// Book table name
    public static final String TABLE_N = "Favori";

    // Book Table Columns names
    public static final String KEY_ID = "_id";
    public static final String KEY_TITLE = "title";
    public static final String KEY_IMAGE = "image";

    public static final String KEY_OVERVIEW = "overview";
  public static final String KEY_VOTE_AVERAGE = "rate";
   public static final String KEY_RELEASE_DATA = "data";
  //  public static final String KEY_BACK="back";







    public static final String[] COLUMNS = {KEY_ID,KEY_TITLE,KEY_IMAGE,KEY_OVERVIEW,KEY_VOTE_AVERAGE,KEY_RELEASE_DATA};

    public void addFavor(MovieItem f){
        Log.d("addFavor", f.toString());
        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        // 2. create ContentValues to add key "column"/value
        ContentValues values = new ContentValues();
        values.put(KEY_ID, f.get_id()); //get id
        values.put(KEY_TITLE, f.getTitle()); // get title
        values.put(KEY_IMAGE, f.getImage()); // get author
        values.put(KEY_OVERVIEW,f.getOverview());
       values.put(KEY_VOTE_AVERAGE,f.getVote_average());
        values.put(KEY_RELEASE_DATA,f.getRelease_date());
     //   values.put(KEY_BACK, f.getBackdrop_path());


        // 3. insert
        db.insert(TABLE_N, // table
                null, //nullColumnHack
                values); // key/value -> keys = column names/ values = column values

        // 4. close
        db.close();
    }

    public  Integer getFavor( int _id){

        // 1. get reference to readable DB
        SQLiteDatabase db =this.getReadableDatabase();

        // 2. build query
        Cursor cursor =
                db.query(TABLE_N, // a. table
                        COLUMNS, // b. column names
                        " _id = ?", // c. selections
                        new String[] { String.valueOf(_id) }, // d. selections args
                        null, // e. group by
                        null, // f. having
                        null, // g. order by
                        null); // h. limit

        // 3. if we got results get the first one
//        if (cursor != null)
//            cursor.moveToFirst();

        int numRows = cursor.getCount();
        cursor.close();
        return numRows;

//
//        // 4. build book object
//        MovieItem f = new MovieItem();
//
//        //why not make this  f.set_Id(cursor.getString(0));? because id auto
//        //why make setTitle (1) not (0) ? because index 0 refer to _id
//        f.setTitle(cursor.getString(1));
//        f.setImage(cursor.getString(2));
//        f.setOverview(cursor.getString(3));
//        f.setVote_average(cursor.getString(4));
//        f.setRelease_date(cursor.getString(5));
//
//        Log.d("getFavor("+_id+")", f.toString());

        // 5. return book
       // return f;
    }

    // Get All Book
    public List<MovieItem> getAllFavors() {
        List<MovieItem> fs = new ArrayList<>();

        // 1. build the query
        String query = "SELECT  * FROM " + TABLE_N;

        // 2. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        // 3. go over each row, build book and add it to list
        MovieItem f = null;
        if (cursor.moveToFirst()) {
            do {
                f = new MovieItem();
                // f.set_id(cursor.getInt(0));
                f.setTitle(cursor.getString(1));
                f.setImage(cursor.getString(2));
                f.setOverview(cursor.getString(3));
                f.setVote_average(cursor.getString(4));
                f.setRelease_date(cursor.getString(5));
           //     f.setBackdrop_path(cursor.getString(6));


                // Add favor to books
                fs.add(f);
            } while (cursor.moveToNext());
        }
//make for loop to display in log cat
        // for(int i =0; i<)
        Log.d("getAllFavors()", fs.toString());

        // return favors
        return fs;
    }
//    }
//
//    // Updating single book
//    public int updateFavor(MovieItem f) {
//
//        // 1. get reference to writable DB
//        SQLiteDatabase db = this.getWritableDatabase();
//
//        // 2. create ContentValues to add key "column"/value
//        ContentValues values = new ContentValues();
//        values.put("_id", f.get_id());
//        values.put("title", f.getTitle()); // get title
//        values.put("image", f.getImage()); // get author
//
//        // 3. updating row
//        int i = db.update(TABLE_N, //table
//                values, // column/value
//                KEY_ID+" = ?", // selections
//                new String[] { String.valueOf(f.get_id()) }); //selection args
//
//        // 4. close
//        db.close();
//
//        return i;
//
//    }

    // Deleting single book
    public void deleteFavor(MovieItem f) {

        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        // 2. delete
        db.delete(TABLE_N,
                KEY_ID+" = ?",
                new String[] { String.valueOf(f.get_id()) });

        // 3. close
        db.close();

        Log.d("deleteFavor", f.toString());

    }



}
