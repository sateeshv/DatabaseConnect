package sateesh.com.databaseconnect;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            FetchSheetTask task = new FetchSheetTask(getApplicationContext());
            task.execute();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
