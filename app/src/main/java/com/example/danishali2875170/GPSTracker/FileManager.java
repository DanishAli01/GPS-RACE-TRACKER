package com.example.danishali2875170.GPSTracker;

import android.icu.text.SimpleDateFormat;
import android.os.Environment;
import java.io.File;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class FileManager {


    private File DIRECTORY;
    private File THIS_FILE;
    private SimpleDateFormat dateFormat;
    private Calendar calendar;

    public FileManager(String GIVEN_NAME){


        DIRECTORY =  new File(Environment.getExternalStorageDirectory()+"/GPSDATA");
        DIRECTORY.mkdirs();
        THIS_FILE = new File(DIRECTORY,get_time_stamp()+GIVEN_NAME);

    }
    public FileManager(){

        DIRECTORY =  new File(Environment.getExternalStorageDirectory()+"/GPSDATA");
        DIRECTORY.mkdirs();

    }

    /**
     * Get time stamp for GPX file to create.
     * @return String stamp.
     */
    private String get_time_stamp(){

        dateFormat = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
        calendar = Calendar.getInstance();
        return dateFormat.format(calendar.getTime());

    }

    public File getTHIS_FILE(){
        return THIS_FILE;
    }

    /**
     * Gather all files in DIRECTORY in list & returns last created (GPX file) in DIRECTORY
     * @return File latest
     */
    public File READ_FILE(){

        List<File> files = Arrays.asList(DIRECTORY.listFiles());
        return files.get(files.size()-1);

    }




}
