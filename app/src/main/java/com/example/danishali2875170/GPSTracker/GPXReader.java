package com.example.danishali2875170.GPSTracker;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class GPXReader {

    File file;
    String path;
    Context context;
    Activity activity;

    public GPXReader(File file, String path, Context context,Activity activity) {

        this.file = file;
        this.path = path;
        this.context=context;
        this.activity = activity;
    }

    /**
     * Read GPX File using Element -> NodeList->Node
     * @param file
     * @return List<Location>
     */
    public List<Location> ReadGPX(File file) {
        List<Location> list = new ArrayList<Location>();

        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        try {
            if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(activity, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            } else {
                DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
                FileInputStream fileInputStream = new FileInputStream(file);
                Document document = documentBuilder.parse(fileInputStream);
                Element elementRoot = document.getDocumentElement();

                NodeList nodelist_trkpt = elementRoot.getElementsByTagName("trkpt");
                NodeList nodelist_time = elementRoot.getElementsByTagName("timelon");
                NodeList nodelist_ele = elementRoot.getElementsByTagName("ele");
                NodeList nodelist_speed = elementRoot.getElementsByTagName("speed");


                for (int i = 0; i < nodelist_trkpt.getLength(); i++) {

                    Node node = nodelist_trkpt.item(i);
                    Node timenode = nodelist_time.item(i);
                    Node elenode = nodelist_ele.item(i);
                    Node speednode = nodelist_speed.item(i);

                    NamedNodeMap attributes = node.getAttributes();


                    String newLatitude = attributes.getNamedItem("lat").getTextContent();
                    Double newLatitude_double = Double.parseDouble(newLatitude);
                    String new_time = timenode.getTextContent();

                    String new_ele = elenode.getTextContent();

                    String new_speed = speednode.getTextContent();
                    float newspeed_float = Float.parseFloat(new_speed);


                    String newLongitude = attributes.getNamedItem("lon").getTextContent();
                    Double newLongitude_double = Double.parseDouble(newLongitude);

                    Double newAltitude_double = Double.parseDouble(new_ele);

                    String newLocationName = newLatitude + ":" + newLongitude + " -> " + new_time;
                    Log.i("new Location", newLocationName);

                    long new_time_long = Long.parseLong(new_time);

                    Location newLocation = new Location(newLocationName);
                    newLocation.setLatitude(newLatitude_double);
                    newLocation.setLongitude(newLongitude_double);
                    newLocation.setAltitude(newAltitude_double);
                    newLocation.setTime(new_time_long);
                    newLocation.setSpeed(newspeed_float);


                    list.add(newLocation);

                }

                fileInputStream.close();
            }
        }catch (ParserConfigurationException e) {
// TODO Auto-generated catch block
            e.printStackTrace();
        } catch (FileNotFoundException e) {
// TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SAXException e) {
// TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
// TODO Auto-generated catch block
            e.printStackTrace();
        }

        return list;
    }

    public String displayGPXFile(List<Location> gpxList){

        String info = ""+gpxList.size();

        for(int i = 0; i < gpxList.size(); i++){
            info += ((Location)gpxList.get(i)).getLatitude()
                    + " : "
                    + ((Location)gpxList.get(i)).getLongitude() + "\n";
        }

        return info;
    }

}
