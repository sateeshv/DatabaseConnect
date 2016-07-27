package sateesh.com.databaseconnect;

import android.content.Context;
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

/**
 * Created by Sateesh on 22-07-2016.
 */
public class FetchSheetTask extends AsyncTask<Void, Void, Void> {
    Context context;

    public FetchSheetTask(Context context) throws IOException {
        this.context = context;
    }








    @Override
    protected Void doInBackground(Void... params) {
        String KEY = "1h9LyG7grqOeYJT7xStVnbfhjXt0r2mjl-5Ow-i13OnY";
        String sheetURL = "https://spreadsheets.google.com/feeds/list/" + KEY + "/od6/public/values?alt=json";

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
            Log.v("Sateesh-sheetString", "*** " + sheetString);


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
            Log.v("Sateesh", "Is this running");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void getSheetDataFromJSON(String sheetString) throws JSONException {

        int numberOfRows;
        String date, time, author, category, description, status;
        date = null;
        time = null;
        author = null;
        category = null;
        category = null;
        description = null;
        status = null;

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

                Log.v("Sateesh - Data", "date Values are: " + date);
                Log.v("Sateesh - Data", "time Values are: " + time);
                Log.v("Sateesh - Data", "author Values are: " + author);
                Log.v("Sateesh - Data", "category Values are: " + category);
                Log.v("Sateesh - Data", "description Values are: " + description);
                Log.v("Sateesh - Data", "status Values are: " + status);

            }
        }
        else {
            Log.v("Sateesh: ", "NO New Data Available");
            long endTime = System.currentTimeMillis();
            Log.v("Sateesh: ", "*** Time taken to Check new Data Available " );
        }







    }
}
