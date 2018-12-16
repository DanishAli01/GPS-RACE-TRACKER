package com.example.danishali2875170.GPSTracker;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import android.location.Location;
import android.util.Log;
import android.widget.Toast;

public class ManageLocation {

    private List<Location> lc;

    public ManageLocation(List<Location> locationlist){
        if(!locationlist.isEmpty())
            lc = locationlist;
        else
            Log.i("mlclass","Error");

    }

    /**
     * <bold>Previous Location Finder, given location index isc!= 0. (Speed Finder Helper)</bold>
     * @param loc
     * @return Previous Location
     */
    public Location getprevious(Location loc) {

        if (lc.contains(loc)) {
            if(lc.indexOf(loc)!=0)
            return lc.get(lc.indexOf(loc) - 1);
        }
            return loc;
    }

    /**
     * <bold>Calculates distance between two given location. (Speed Finder Helper)</bold>
     * @param start
     * @param end
     * @return double distance
     */
    public double getdistance(Location start, Location end){

       return start.distanceTo(end);
    }

    /**
     * Calculates Speed = distance/time.
     * @param distance
     * @param time
     * @return
     */
    public double getSpeed(double distance,long time){

        return distance/time;
    }

    /**
     * <bold>Calculate time difference associated with given locations.  (Speed Finder Helper)</bold>
     * @param start
     * @param end
     * @return
     */
    public long time_diff(Location start, Location end){

        return end.getTime()-start.getTime();

    }

    /**
     * <bold>setSpeed if getspeed()==0, get distance && time from previous location to new location, call getSpeed to compute speed</bold>
     * @param loc
     */
    public void checkspeed(Location loc){
        Location locp;
        double d;
        long time;
        float s;

        if(loc.getSpeed()==0){
            locp = getprevious(loc);
            d = getdistance(locp,loc);
            time = time_diff(getprevious(loc),loc);
            loc.setSpeed((float)getSpeed(d,time));
        }


    }

    /**
     * Min Speed Finder.
     * @return float speed
     */
    public float minspeed(){
        List<Float> d = new ArrayList<>();

        for(int i = 1; i<lc.size(); i++){

            d.add(lc.get(i).getSpeed());
        }
        if(d.isEmpty())
            return 0;
        else
            return d.get(d.indexOf(Collections.min(d)));
    }

    /**
     * Max Speed Finder
     * @return float speed
     */

    public float maxspeed(){
        List<Float> d = new ArrayList<>();

        for(int i = 0; i<lc.size(); i++){

            d.add(lc.get(i).getSpeed());
        }
        if(d.isEmpty())
            return 0;
        else
            return d.get(d.indexOf(Collections.max(d)));
    }

    /**
     * Average Speed Finder
     * @return double speed
     */
    public double averagespeed(){
        List<Float> d = new ArrayList<>();
        float sum = 0;

        for(int i = 0; i<lc.size(); i++){

            d.add(lc.get(i).getSpeed());
            sum += d.get(i);
        }
        return sum/d.size();
    }

    /**
     * <bold>Total distance covered in race.</bold>
     * @return double distance
     */
    public double totaldistance(){
        double distance = 0;
      for(int i = 0; i<lc.size()-1; i++){

          distance += lc.get(i).distanceTo(lc.get(i+1));
      }
      return distance;
    }

    /**
     * Total Time taken to cover all locations, calculated based on time associated with each location.
     * @return long time
     */
    public long totaltime(){

        long totaltime = 0;
        for(int i = 0; i<lc.size()-1; i++){

            totaltime += time_diff(lc.get(i),lc.get(i+1));
        }

        return totaltime;
    }

    /**
     * Calculate Altitude loss durning race.
     * @return double loss
     */
    public double searchloss(){
        double loss = 0;
        double temp = 0;
        for(int i=1; i<lc.size(); i++){
            temp = lc.get(i-1).getAltitude();
            if(lc.get(i).getAltitude()<temp) {
                loss += lc.get(i).getAltitude()-temp;
            }
        }
        return loss;
    }
    /**
     * Calculate Altitude gain durning race.
     * @return double gain
     */
    public double searchgain(){
        double loss = 0;
        double temp = 0;
        for(int i=1; i<lc.size(); i++) {
            temp = lc.get(i-1).getAltitude();
            if (lc.get(i).getAltitude() > temp) {
                loss += lc.get(i).getAltitude() - temp;
                temp = lc.get(i).getAltitude();
            }
        }
        return loss;
    }

    /**
     * Get all Altitudes in array []
     * @return double array []
     */
    public double[] all_altitudes() {
        double [] ad = new double[lc.size()];
        for (int i = 0; i < lc.size(); i++) {
            ad[i]= lc.get(i).getAltitude();
        }
        return ad;
    }
    /**
     * Get max Altitudes in array []
     * @return double Altitude
     */
    public double getmaxaltitude(){
        double a [] = all_altitudes();
        Arrays.sort(a);
        return a[a.length-1];
    }
    /**
     * Get min Altitudes in array []
     * @return double Altitude
     */
    public double getminaltitude(){
        double a [] = all_altitudes();
        Arrays.sort(a);
        return a[0];
    }
}
