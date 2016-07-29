package sateesh.com.databaseconnect;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import sateesh.com.databaseconnect.Data.DatabaseContract;

/**
 * Created by Sateesh on 22-07-2016.
 */
public class FetchSheetTask extends AsyncTask<Void, Void, Void> {
    Context context;
    long startTime;

    public FetchSheetTask(Context context) throws IOException {
        this.context = context;
    }








    @Override
    protected Void doInBackground(Void... params) {

        startTime = System.currentTimeMillis();

        Uri uri = Uri.withAppendedPath(DatabaseContract.QuoteInfo.CONTENT_URI, "1");
        Log.v("Sateesh: ", "*** URI link is: " + uri);

        Cursor cursorLastRecord = context.getContentResolver().query(uri, null, null, null, null);
        cursorLastRecord.moveToFirst();
        String lastInserted_SNo = null;
        String sheetURL = null;
        String KEY = "1h9LyG7grqOeYJT7xStVnbfhjXt0r2mjl-5Ow-i13OnY";

        try {
            if (cursorLastRecord.getCount() != 0) {
                lastInserted_SNo = cursorLastRecord.getString(cursorLastRecord.getColumnIndexOrThrow(DatabaseContract.QuoteInfo.COLUMN_SNo));
                Log.v("Sateesh: ", "*** Last Inserted Date is: " + lastInserted_SNo);
                sheetURL = "https://spreadsheets.google.com/feeds/list/" + KEY + "/od6/public/values?alt=json" + "&sq=sno>" + lastInserted_SNo;
            } else {
                Log.v("Sateesh: ", "*** No insertions till Now");
                sheetURL = "https://spreadsheets.google.com/feeds/list/" + KEY + "/od6/public/values?alt=json";
                Log.v("Sateesh: ", "*** new Data sheet URL is: " + sheetURL);
            }
        } catch (CursorIndexOutOfBoundsException e) {
            Log.v("Sateesh: ", "*** cursor Index Out of Bound");
        }


//        String sheetURL = "https://spreadsheets.google.com/feeds/list/" + KEY + "/od6/public/values?alt=json";

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String sheetString = null;

        try {
            URL url = new URL(sheetURL.toString());
            /**
             * Create request to Google Spreadsheet
             */
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            /**
             * Read inputStream to a String
             */
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                return null;
            }
            sheetString = buffer.toString();
            Log.v("Sateesh: -sheetString", "*** " + sheetString);


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        try {
            getSheetDataFromJSON(sheetString);
            } catch (JSONException e) {
            e.printStackTrace();
        }catch(NullPointerException e1){
            e1.printStackTrace();
        }
        return null;
    }

    private void getSheetDataFromJSON(String sheetString) throws JSONException {

        int numberOfRows;
        String sNo, date, time, author, category, description, status;
         sNo = null;
        date = null;
        time = null;
        author = null;
        category = null;
        category = null;
        description = null;
        status = null;

        List<ContentValues> data = new ArrayList<ContentValues>();

        JSONObject mainloop = new JSONObject(sheetString);
        JSONObject feed = mainloop.getJSONObject("feed");
        JSONObject searchResultsObject = feed.getJSONObject("openSearch$totalResults");

        String searchResultsValue = searchResultsObject.getString("$t");
        int value = Integer.parseInt(searchResultsValue);
        if (value > 0) {
            Log.v("Sateesh: ", "New Data Available");
            JSONArray entry = feed.getJSONArray("entry");

            JSONObject searchTotalCount = feed.getJSONObject("openSearch$totalResults");
            numberOfRows = searchTotalCount.getInt("$t");

            for (int i = 0; i < numberOfRows; i++) {
                String[] rowData = new String[numberOfRows - 1];



                JSONObject eachRow = entry.getJSONObject(i);

                JSONObject sNoObject = eachRow.getJSONObject("gsx$sno");
                sNo = sNoObject.getString("$t");

                JSONObject dateObject = eachRow.getJSONObject("gsx$date");
                date = dateObject.getString("$t");

                JSONObject timeObect = eachRow.getJSONObject("gsx$time");
                time = timeObect.getString("$t");

                JSONObject authorObject = eachRow.getJSONObject("gsx$author");
                author = authorObject.getString("$t");

                JSONObject categoryObject = eachRow.getJSONObject("gsx$category");
                category = categoryObject.getString("$t");

                JSONObject descriptionObject = eachRow.getJSONObject("gsx$description");
                description = descriptionObject.getString("$t");

                JSONObject statusObject = eachRow.getJSONObject("gsx$status");
                status = statusObject.getString("$t");

                Log.v("Sateesh: - Data", "sNo Values are: " + sNo);
                Log.v("Sateesh: - Data", "date Values are: " + date);
                Log.v("Sateesh: - Data", "time Values are: " + time);
                Log.v("Sateesh: - Data", "author Values are: " + author);
                Log.v("Sateesh: - Data", "category Values are: " + category);
                Log.v("Sateesh: - Data", "description Values are: " + description);
                Log.v("Sateesh: - Data", "status Values are: " + status);

                ContentValues values = new ContentValues();
                values.put(DatabaseContract.QuoteInfo.COLUMN_SNo, sNo);
                values.put(DatabaseContract.QuoteInfo.COLUMN_CREATED_DATE, date);
                values.put(DatabaseContract.QuoteInfo.COLUMN_CREATED_TIME, time);
                values.put(DatabaseContract.QuoteInfo.COLUMN_AUTHOR, author);
                values.put(DatabaseContract.QuoteInfo.COLUMN_CATEGORY, category);
                values.put(DatabaseContract.QuoteInfo.COLUMN_DESC, description);
                values.put(DatabaseContract.QuoteInfo.COLUMN_STATUS, status);
                boolean favorites = false;
                values.put(DatabaseContract.QuoteInfo.COLUMN_FAVORITES, favorites );

                data.add(values);



            }
            if (data.size() > 0) {
                ContentValues[] dataArray = new ContentValues[data.size()];
                ContentValues[] printValues = data.toArray(dataArray);
                Log.v("Sateesh: ", "**** content Values data " + printValues);

                Uri data_uri = Uri.withAppendedPath(DatabaseContract.QuoteInfo.CONTENT_URI, "0");
                int insertedRecords = context.getContentResolver().bulkInsert(data_uri, dataArray);
                Log.v("Sateesh: ", "*** FetchSheetTask + Data Inserted Records: " + insertedRecords);
            }
        }
        else {
            Log.v("Sateesh: ", "NO New Data Available");
            long endTime = System.currentTimeMillis();
            Log.v("Sateesh: ", "*** Time taken to Check new Data Available " );
        }







    }
}
