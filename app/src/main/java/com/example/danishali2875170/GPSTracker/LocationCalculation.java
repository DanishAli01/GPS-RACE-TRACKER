package com.example.danishali2875170.GPSTracker;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

public class LocationCalculation extends Activity {

    private ManageLocation ml;
    private GPXReader gpxReader;
    private List<Location> gpxList;
    private TextView welcome_title;
    private TextView totaldistance;
    private TextView totaltime;
    private TextView minspeed;
    private TextView maxspeed;
    private TextView avgspeed;
    private TextView loss;
    private TextView gain;
    private TextView maxalt;
    private Button goback;
    private TextView racetime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.locationcalculation);

        init();

        AltitudeGraph altitudeGraph = (AltitudeGraph)findViewById(R.id.histogram_view);
        double graphArray[] = ml.all_altitudes();

        altitudeGraph.setgraphco_ordinates(graphArray);

        welcome_title = (TextView)findViewById(R.id.welcome_title);
        totaldistance = (TextView)findViewById(R.id.totaldistance);
        totaltime = (TextView)findViewById(R.id.totaltime);
        minspeed = (TextView)findViewById(R.id.minspeed);
        maxspeed = (TextView)findViewById(R.id.maxspeed);
        avgspeed = (TextView)findViewById(R.id.avgspeed);
        loss = (TextView)findViewById(R.id.loss);
        gain = (TextView)findViewById(R.id.gain);
        maxalt = (TextView) findViewById(R.id.maxalt);
        goback = (Button) findViewById(R.id.goback);
        racetime = (TextView) findViewById(R.id.racetime);


        totaldistance.setText("Total Distance : "+ml.totaldistance());
        totaltime.setText("Total Time : "+ml.totaltime()/1000+" sec");
        minspeed.setText("Min Speed : "+ml.minspeed());
        maxspeed.setText("Max Speed : "+ml.maxspeed());
        avgspeed.setText("Average Speed : "+ml.averagespeed());
        loss.setText("Altitude Loss :  "+ml.searchloss());
        gain.setText("Altitude Gain : "+ml.searchgain());
        maxalt.setText("Max Alt : "+ml.getmaxaltitude()+"      Min Alt : "+ml.getminaltitude());
        racetime.setText("Race Time : " + (int) (MainActivity.updatedTime / 1000)/60 + ":"

                + String.format("%02d", (int) (MainActivity.updatedTime / 1000)) + ":"

                + String.format("%03d", (int) (MainActivity.updatedTime %1000)));

        goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LocationCalculation.this, MainActivity.class);
                startActivity(i);
            }
        });

    }
    /**
     * Instantiate GPXReader Class & File Manager Class, after checking checkselfPermission
     * Instantiate Handler for Race Timer too.
     */
    private void init(){
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }
        else {
            FileManager fm = new FileManager();
            gpxReader = new GPXReader(fm.READ_FILE(), Environment.getExternalStorageDirectory() + "/GPSDATA", getApplicationContext(), this);
            gpxList = gpxReader.ReadGPX(fm.READ_FILE());
            ml = new ManageLocation(gpxList);
        }

    }


}

