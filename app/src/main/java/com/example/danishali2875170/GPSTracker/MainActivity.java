package com.example.danishali2875170.GPSTracker;
/**
 * @author : Danish Ali <em>2875170</em>
 * @version: 1.0
 * @Assignment: 03
 * @title: Mountain Race
 * <h2>First Activity displays:
 * <ul><li>Rules of Game<li>
 *     <li>Start/Stop Button<li>
 *         <li>Upon press location saved after 5sec delay,
 *         @cgraph and calculation related are displayed in
 *         @link: LocationCalculation activity</li>
 *
 *         </ul></h2>
 */
import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends Activity {

    private Button btnLoc;
    private Button btnstopLoc;
    private ManageLocation ml;
    private GPSTracker gt;
    private GPXWriter gpxwriter;
    private boolean check = false;
    private FileManager fm;
    private final String GPX_FILE_NAME = "v2.0.gpx";
    private TextView timer;
    private TextView textInfo;
    private long startTime= 0L;
    private Handler customHandler;
    long timeInMilliseconds = 0L;
    long timeSwapBuff = 0L;
    static long updatedTime = 0L;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();


        btnLoc = (Button) findViewById(com.example.danishali2875170.GPSTracker.R.id.btnGetLoc);
        btnstopLoc = (Button) findViewById(com.example.danishali2875170.GPSTracker.R.id.btnstopLoc);
        textInfo = (TextView)findViewById(R.id.info);
        timer = (TextView)findViewById(R.id.timer);


        btnLoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (
                        ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED

                        ) {

                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                    startActivity(getIntent());
                  btnLoc.performClick();
                } else {
                    startTime = SystemClock.uptimeMillis();
                    customHandler.postDelayed(TimerThread, 0);
                    check = true;
                    Toast.makeText(getApplicationContext(), "Location tracking started", Toast.LENGTH_LONG).show();
                    btnLoc.setEnabled(false);
                    gt = new GPSTracker(getApplicationContext(), MainActivity.this);
                    gt.getLocation();
                }
            }
        });

        btnstopLoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED

                        ) {

                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);


                    startActivity(getIntent());
                    btnstopLoc.performClick();

                } else {
                    if (check == true && !gt.getlocationlist().isEmpty() && (int) updatedTime/1000 >06) {


                        Toast.makeText(getApplicationContext(), "Location tracking stopped", Toast.LENGTH_LONG).show();

                        ml = new ManageLocation(gt.getlocationlist());
                        speedupdate(gt.getlocationlist());
                        timeSwapBuff += timeInMilliseconds;
                        customHandler.removeCallbacks(TimerThread);


                        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED&&
                                ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                                ) {
                            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);


                        }
                        else {

                           gpxwriter.WriterGPX(fm.getTHIS_FILE(), "By Danish Ali @@ 2875170", gt.getlocationlist());
                           Intent i = new Intent(MainActivity.this, LocationCalculation.class);
                            startActivity(i);

                        }
                    } else
                        Toast.makeText(getApplicationContext(), "Start Race First or Let 5 secs pass", Toast.LENGTH_SHORT).show();

                }
            }
        });

    }

    /**
     * Instantiate GPXWriter Class & File Manager Class, after checking checkselfPermission
     * Instantiate Handler for Race Timer too.
     */

    private void init(){

        gpxwriter = new GPXWriter(getApplicationContext(),this);
        fm = new FileManager(GPX_FILE_NAME);
        customHandler = new Handler();
    }

    /**
     * Speed Calculator, if single location is recorded speed will be set to 0(zero),else calculate using speed = rate of [distance/time]
     * will help of checkspeed method from LocationCalculation Class
     * @param ls
     */
    private void speedupdate(List<Location> ls){

            if (ls.size()==1)
                ls.get(0).setSpeed((float)000);
        else {
                for (int loc = 1; loc < ls.size(); loc++) {
                    ml.checkspeed(ls.get(loc));
                }
            }

    }

    /**
     *Timer Thread Handler
     */
    private Runnable TimerThread = new Runnable() {

        public void run() {

            timeInMilliseconds = SystemClock.uptimeMillis() - startTime;

            updatedTime = timeSwapBuff + timeInMilliseconds;

            timer.setText("" + ((int) (updatedTime / 1000)) / 60 + ":"

                            + String.format("%02d", (int) (updatedTime / 1000)) + ":"

                            + String.format("%03d",(int) (updatedTime % 1000)));

            customHandler.postDelayed(this, 0);

        }



    };


    @Override
    protected void onRestart() {
        super.onRestart();  // Always call the superclass method first

    }



}