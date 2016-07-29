package sateesh.com.databaseconnect.Data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Sateesh on 1/18/2016.
 */

/**
 * SQLiteAssetHelper used when application has already in built database.
 *          In this case, we no need to create database. That means no onCreate Method.
 * If, we want to create new database, then SQLiteOpenHelper
 *          In this case, we need to create database. That means we need to have onCreate Method.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "QuotesDetails.db";
    public static final int DATABASE_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String CREATE_QUOTE_INFO_TABLE = "CREATE TABLE " + DatabaseContract.QuoteInfo.TABLE_NAME  +
                "( "  + DatabaseContract.QuoteInfo._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                DatabaseContract.QuoteInfo.COLUMN_SNo + " INT NOT NULL, " +
                DatabaseContract.QuoteInfo.COLUMN_CREATED_DATE + " LONG NOT NULL, " +
                DatabaseContract.QuoteInfo.COLUMN_CREATED_TIME + " LONG NOT NULL, " +
                DatabaseContract.QuoteInfo.COLUMN_AUTHOR + " TEXT NOT NULL, " +
                DatabaseContract.QuoteInfo.COLUMN_CATEGORY + " INTEGER NOT NULL, " +
                DatabaseContract.QuoteInfo.COLUMN_DESC + " TEXT NOT NULL, " +
                DatabaseContract.QuoteInfo.COLUMN_STATUS + " TEXT NOT NULL, " +
                DatabaseContract.QuoteInfo.COLUMN_FAVORITES + " TEXT NOT NULL " + ");";


        db.execSQL(CREATE_QUOTE_INFO_TABLE);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DatabaseContract.QuoteInfo.TABLE_NAME);
        onCreate(db);
    }
}
