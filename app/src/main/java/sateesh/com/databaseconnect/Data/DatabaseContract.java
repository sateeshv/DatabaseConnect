package sateesh.com.databaseconnect.Data;

import android.net.Uri;

/**
 * Created by Sateesh on 1/18/2016.
 */
public class DatabaseContract {

    public static final String AUTHORITY = "sateesh.com.DatabaseContract";
    public static final Uri BASIC_URI = Uri.parse("content://" + AUTHORITY);
    public static final String PATH_QUOTE_INFO = "QuoteInfo";


    public static class QuoteInfo{
        public static final int LIMIT = 7;
        public static final Uri CONTENT_URI = BASIC_URI.buildUpon().appendPath(PATH_QUOTE_INFO).build();
        public static final String TABLE_NAME = "QuoteInfo";

        public static final String _ID = "_id";
        public static final String COLUMN_CREATED_DATE = "CreatedDate";
        public static final String COLUMN_CREATED_TIME = "Time";
        public static final String COLUMN_AUTHOR = "Author";
        public static final String COLUMN_CATEGORY = "Category";
        public static final String COLUMN_DESC = "Description";
        public static final String COLUMN_STATUS = "Status";
        public static final String COLUMN_FAVORITES = "Favorites";

    }

}
