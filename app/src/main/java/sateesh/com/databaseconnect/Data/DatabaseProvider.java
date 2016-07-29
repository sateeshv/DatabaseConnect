package sateesh.com.databaseconnect.Data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;

import java.sql.SQLException;

/**
 * Created by Sateesh on 1/18/2016.
 */
public class DatabaseProvider extends ContentProvider {
    DatabaseHelper databaseHelper;
    SQLiteDatabase database;
    public static final String LOG_CAT = "Sateesh: ";
    public static final int ALL_RECORDS = 0;
    public static final int LAST_RECORD = 1;
    public static final int SEVEN_RECORDS = 2;
//    public static final int CITY = 3;

    @Override
    public boolean onCreate() {
        databaseHelper = new DatabaseHelper(getContext());
        return true;
    }

    public static final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
//        matcher.addURI(DatabaseContract.AUTHORITY, DatabaseContract.ClassInfo.LIMIT, 1);
        matcher.addURI(DatabaseContract.AUTHORITY, DatabaseContract.PATH_QUOTE_INFO+ "/0", ALL_RECORDS);
        matcher.addURI(DatabaseContract.AUTHORITY, DatabaseContract.PATH_QUOTE_INFO + "/1", LAST_RECORD);
        matcher.addURI(DatabaseContract.AUTHORITY, DatabaseContract.PATH_QUOTE_INFO + "/2", SEVEN_RECORDS);
//        matcher.addURI(DatabaseContract.AUTHORITY, DatabaseContract.PATH_CITYINFO + "/3", CITY);
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        Cursor cursorData = null;
        int match = matcher.match(uri);

        switch (match) {
            case LAST_RECORD:
                String[] _id = selectionArgs;
                cursorData = databaseHelper.getReadableDatabase().query(DatabaseContract.QuoteInfo.TABLE_NAME, projection, selection, selectionArgs, sortOrder, null, DatabaseContract.QuoteInfo.COLUMN_SNo + " DESC ", " 1");
                cursorData.setNotificationUri(getContext().getContentResolver(), uri);
                break;

            case SEVEN_RECORDS:
                Log.v("Sateesh: ", "*** selection value is: " +selection);
                Log.v("Sateesh: ", "*** selection filter is: " +selectionArgs);
                cursorData = databaseHelper.getReadableDatabase().query(DatabaseContract.QuoteInfo.TABLE_NAME, projection, selection, selectionArgs, null, null, DatabaseContract.QuoteInfo.COLUMN_CREATED_DATE + " DESC ", " 7");
                Log.v("Sateesh: ", "*** cursor filtered data: " + DatabaseUtils.dumpCursorToString(cursorData));
                cursorData.setNotificationUri(getContext().getContentResolver(), uri);
                break;

//            case CITY:
//                cursorData = databaseHelper.getReadableDatabase().query(DatabaseContract.CityInfo.TABLE_NAME, null, null, null, null, null, null, null);
//                cursorData.setNotificationUri(getContext().getContentResolver(), uri);
//                break;

            case ALL_RECORDS:
                Log.v("Sateesh: ", "*** selection value is: " +selection);
                Log.v("Sateesh: ", "*** selection filter is: " +selectionArgs);
                cursorData = databaseHelper.getReadableDatabase().query(DatabaseContract.QuoteInfo.TABLE_NAME, projection, selection, selectionArgs, null, null, DatabaseContract.QuoteInfo.COLUMN_CREATED_DATE + " ASC ", null);
                Log.v("Sateesh: ", "*** cursor filtered data: " + DatabaseUtils.dumpCursorToString(cursorData));
                cursorData.setNotificationUri(getContext().getContentResolver(), uri);
                break;
        }


        return cursorData;
//
//        Cursor query = databaseHelper.getReadableDatabase().query(DatabaseContract.ClassInfo.TABLE_NAME, null, null, null, null, null, DatabaseContract.ClassInfo.COLUMN_DATE + " DESC ", " 5");
//        return query;

    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        return null;
    }

    public int bulkInsert(Uri uri, ContentValues[] values) {
        SQLiteDatabase writeToDatabase = databaseHelper.getWritableDatabase();
        int insertedRecords = 0;
        final int match = matcher.match(uri);
        switch (match) {
//            case CITY:
//                writeToDatabase.beginTransaction();
//                try {
//                    for (ContentValues value : values) {
//                        long insertRecord = writeToDatabase.insert(DatabaseContract.CityInfo.TABLE_NAME, null, value);
//                        if (insertRecord > 0) {
//                            ContentUris.withAppendedId(uri, insertRecord);
//                            Log.v(LOG_CAT, "*** Inserted City Record is: " + ContentUris.withAppendedId(uri, insertRecord));
//                            insertedRecords++;
//                        } else {
//                            Log.v(LOG_CAT, "*** No City records to Insert");
//                        }
//                    }
//                    writeToDatabase.setTransactionSuccessful();
//                } finally {
//                    writeToDatabase.endTransaction();
//                }
//                getContext().getContentResolver().notifyChange(uri, null);
//                break;

            case ALL_RECORDS:
                writeToDatabase.beginTransaction();
                try {
                    for (ContentValues records : values) {
                        long insertRecord = writeToDatabase.insert(DatabaseContract.QuoteInfo.TABLE_NAME, null, records);
                        if (insertRecord > 0) {
                            ContentUris.withAppendedId(uri, insertRecord);
                            Log.v(LOG_CAT, "*** Inserted Record is: " + ContentUris.withAppendedId(uri, insertRecord));
                            insertedRecords++;
                        } else {
                            Log.v(LOG_CAT, "*** No records to Insert");
                        }

                    }
                    writeToDatabase.setTransactionSuccessful();
                } finally {
                    writeToDatabase.endTransaction();
                }

                getContext().getContentResolver().notifyChange(uri, null);
                break;

            default:
                return super.bulkInsert(uri, values);
        }
        return insertedRecords;

    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {

        final int match = matcher.match(uri);
        int delete = 0;
        switch (match) {
            case ALL_RECORDS:
            delete = databaseHelper.getWritableDatabase().delete(DatabaseContract.QuoteInfo.TABLE_NAME, null, null);
            if (delete > 0) {
                ContentUris.withAppendedId(uri, delete);
                Log.v("Sateesh: ", "**** Data records deleted " + delete);
            } else {
                try {
                    throw new SQLException("No records to Delete" + uri);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            getContext().getContentResolver().notifyChange(uri, null);
                break;

//            case CITY:
//                delete = databaseHelper.getWritableDatabase().delete(DatabaseContract.CityInfo.TABLE_NAME, null, null);
//                if (delete > 0) {
//                    ContentUris.withAppendedId(uri, delete);
//                    Log.v("Sateesh: ", "**** City records deleted " + delete);
//                } else {
//                    try {
//                        throw new SQLException("No City records to Delete" + uri);
//                    } catch (SQLException e) {
//                        e.printStackTrace();
//                    }
//                }
        }
        return delete;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}
