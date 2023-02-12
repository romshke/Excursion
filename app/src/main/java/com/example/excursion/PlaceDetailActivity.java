package com.example.excursion;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.text.HtmlCompat;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
        if (getIntent().hasExtra("sightName") && getIntent().hasExtra("sightAddress") && getIntent().hasExtra("sightImagePath") && getIntent().hasExtra("sightDetails")){

            Log.d(TAG, "getIncomingIntent: get intent");

            String sightName = getIntent().getStringExtra("sightName");
            String sightAddress = getIntent().getStringExtra("sightAddress");
            String sightImagePath = getIntent().getStringExtra("sightImagePath");
            String sightDetails = getIntent().getStringExtra("sightDetails");
            String sightSourceLink = getIntent().getStringExtra("sightSourceLink");

            fillActivity(sightName, sightAddress, sightImagePath, sightDetails, sightSourceLink);
        }
    }

    private void fillActivity(String sightName, String sightAddress, String sightImagePath, String sightDetails, final String sightSourceLink) {
        ((ImageView) findViewById(R.id.place_detail_image)).setImageResource(this.getResources().getIdentifier(sightImagePath, null, Objects.requireNonNull(this).getPackageName()));
        ((TextView) findViewById(R.id.place_detail_name)).setText(sightName);
        ((TextView) findViewById(R.id.place_detail_address)).setText(sightAddress);
        ((TextView) findViewById(R.id.place_detail_details)).setText(HtmlCompat.fromHtml(sightDetails, HtmlCompat.FROM_HTML_MODE_LEGACY));
        findViewById(R.id.place_detail_source_link).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(sightSourceLink));
                startActivity(intent);
            }
        });
    }
}
