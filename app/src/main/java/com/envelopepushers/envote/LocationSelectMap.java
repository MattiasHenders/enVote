package com.envelopepushers.envote;

import androidx.core.app.ActivityCompat;
import android.Manifest;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.api.Response;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Polygon;
import org.osmdroid.views.overlay.Polyline;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class LocationSelectMap extends Activity implements LocationListener {

    private MapView mapMain;
    private IMapController mapController;
    private Button btnSubmitLocation;
    protected LocationManager locationManager;
    protected LocationListener locationListener;
    private boolean setLocation = false;
    private JsonFromWeb returnObj;
    public ExtendedFloatingActionButton titleView;
    Timer updateMapTimer;

    private double userLat = 0, userLon = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_select_map);

        titleView = findViewById(R.id.view_current_location);

        //Check permissions
        checkPermissions();

        //load/initialize the osmdroid configuration, this can be done
        Context ctx = getApplicationContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            System.out.println("Permission not granted");
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);

        //Set the map objects
        mapMain = findViewById(R.id.map_main);
        mapMain.setTileSource(TileSourceFactory.MAPNIK);

        mapController = mapMain.getController();
        mapController.setZoom(14);

        GeoPoint startPoint;
        if (userLon == 0 || userLat == 0) {
            //Burnaby lat/lon as default
            startPoint = new GeoPoint(49.2488, -122.9805);
        } else {
            startPoint = new GeoPoint(userLat, userLon);
        }
        mapController.setCenter(startPoint);

        btnSubmitLocation = findViewById(R.id.btn_location_submit);

        btnSubmitLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String callURL = "https://represent.opennorth.ca/representatives/?point=" + userLat +"%2C" + userLon;
                System.out.println(new JsonFromWeb(callURL).getJSONString());

                openActivityIssue();
            }
        });
    }

    @Override
    public void onLocationChanged(Location location) {
        System.out.println("Latitude:" + location.getLatitude() + ", Longitude:" + location.getLongitude());
        userLat = location.getLatitude();
        userLon = location.getLongitude();
        if (!setLocation) {
            GeoPoint startPoint = new GeoPoint(userLat, userLon);
            mapController.setCenter(startPoint);
            setLocation = true;

            String callURL = "https://represent.opennorth.ca/boundaries/simple_shape?contains=" + userLat + "%2C" + userLon;
            returnObj = new JsonFromWeb(callURL);

            scheduleMapUpdate();
        }
    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.d("Latitude","disable");
    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.d("Latitude","enable");
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.d("Latitude","status");
    }


    public void openActivityIssue() {
        Intent intent = new Intent(this, activity_issue_select.class);
        startActivity(intent);
        finish();
    }


    /**
     * Checks the phones permissions for storage writing.
     * Needed for MapView
     */
    private void checkPermissions() {
        if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            Log.v("PERMS", "Permission is granted");
        } else {
            Log.v("PERMS", "Permission is revoked");
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
    }

    private void scheduleMapUpdate() {

        updateMapTimer = new Timer();
        updateMapTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (returnObj != null && returnObj.getJSONObject() != null) {
                    try {
                        addBoundariesToMap();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, 50, 100);
    }

    private void addBoundariesToMap() throws JSONException {

        updateMapTimer.cancel();

        String lat = "" + userLat;
        String lon = "" + userLon;

        Polyline myPath = new Polyline();
        JSONObject cordsObj = returnObj.getJSONObject();
        JSONArray array = cordsObj.getJSONArray("objects");
        String cordString = "";


        String areaName = array.getJSONObject(0).getString("name");

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                btnSubmitLocation.setVisibility(View.VISIBLE);
                titleView.setText(("\t"+ areaName + "\t"));
            }
        });

        JSONArray cordObj = array.getJSONObject(0)
                .getJSONObject("simple_shape").getJSONArray("coordinates");


        cordString = cordObj.toString()
                .replace("[", "").replace("]", "").trim();

        String[] cords = cordString.split(",");

        ArrayList<GeoPoint> mapPoints = new ArrayList<>();

        for (int j = 0; j < cords.length - 1; j += 2) {
            GeoPoint point = new GeoPoint(Double.parseDouble(cords[j + 1]),
                    Double.parseDouble(cords[j]));
            mapPoints.add(point);
            myPath.addPoint(point);
        }

        myPath.setColor(getColor(R.color.green_darker));
        Polygon polygon = new Polygon();
        polygon.setPoints(mapPoints);
        polygon.setFillColor(getColor(R.color.green_faded));
        mapMain.getOverlays().add(polygon);
        mapMain.getOverlays().add(myPath);
        mapMain.invalidate();
    }

    private GeoPoint computeCentroid(ArrayList<GeoPoint> points) {
        double latitude = 0;
        double longitude = 0;

        for (GeoPoint point : points) {
            latitude += point.getLatitude();
            longitude += point.getLongitude();
        }

        return new GeoPoint(latitude / points.size(), longitude / points.size());
    }

}