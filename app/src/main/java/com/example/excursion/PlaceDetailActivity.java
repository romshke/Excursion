package com.example.excursion;

import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Objects;

public class PlaceDetailActivity extends AppCompatActivity {

    private static final String TAG = "PlaceDetailActivity1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        Objects.requireNonNull(getSupportActionBar()).hide(); // hide the title bar
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setContentView(R.layout.activity_place_detail);

        getIncomingIntent();
    }

    private void getIncomingIntent() {
        if (getIntent().hasExtra("sightName") && getIntent().hasExtra("sightAddress") && getIntent().hasExtra("sightImagePath")){

            Log.d(TAG, "getIncomingIntent: get intent");

            String sightName = getIntent().getStringExtra("sightName");
            String sightAddress = getIntent().getStringExtra("sightAddress");
            String sightImagePath = getIntent().getStringExtra("sightImagePath");

            fillActivity(sightName, sightAddress, sightImagePath);
        }
    }

    private void fillActivity(String sightName, String sightAddress, String sightImagePath) {
        ((TextView) findViewById(R.id.place_detail_name)).setText(sightName);
        ((TextView) findViewById(R.id.place_detail_address)).setText(sightAddress);
        ((ImageView) findViewById(R.id.place_detail_image)).setImageResource(this.getResources().getIdentifier(sightImagePath, null, Objects.requireNonNull(this).getPackageName()));
    }
}
