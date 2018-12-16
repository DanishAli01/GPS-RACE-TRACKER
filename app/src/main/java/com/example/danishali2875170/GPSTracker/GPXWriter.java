package com.example.danishali2875170.GPSTracker;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class GPXWriter {

    private static final String TAG = GPXWriter.class.getName();
    private Activity activity;
    private  Context context;

    GPXWriter(Context context, Activity activity){

        this.activity=activity;
        this.context=context;

    }

    /**
     * Write GPX file using file_writer
     * @param file
     * @param author
     * @param locations
     */
    public  void WriterGPX(File file, String author, List<Location> locations) {

        String header = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\" ?><gpx xmlns=\"http://www.topografix.com/GPX/1/1\" creator=\"MapSource 6.15.5\" version=\"1.1\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"  xsi:schemaLocation=\"http://www.topografix.com/GPX/1/1 http://www.topografix.com/GPX/1/1/gpx.xsd\"><trk>\n";
        String name = "<name>" + author + "</name><trkseg>\n";

        String elementwrite = "";
       DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        for (Location l : locations) {
            elementwrite += "<trkpt lat=\"" + l.getLatitude() + "\" lon=\"" + l.getLongitude() + "\">" +
                    "<time>" + df.format(new Date(l.getTime()))+ "</time>" +
                    "<timelon>" + l.getTime()+ "</timelon>"+
                     "<speed>" + l.getSpeed() + "</speed>" +
                    "<ele>" + l.getAltitude() + "</ele>" +
                    "</trkpt>\n";
        }
        String footer = "</trkseg></trk></gpx>";

        try {
            if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(activity, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }
            else {
                FileWriter fileWriter = new FileWriter(file, false);
                fileWriter.append(header);
                fileWriter.append(name);
                fileWriter.append(elementwrite);
                fileWriter.append(footer);
                fileWriter.flush();
                fileWriter.close();
                if (BuildConfig.DEBUG)
                    Log.i(TAG, "Saved " + locations.size() + " locations.");
            }
        }catch (IOException e) {
            // TODO Auto-generated catch block
            Log.e(TAG, "Error in Write",e);
        }


    }

}
