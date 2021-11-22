package com.envelopepushers.envote;

import androidx.core.app.ActivityCompat;
import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Polygon;
import org.osmdroid.views.overlay.Polyline;


import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class LocationSelectMap extends Activity implements LocationListener {

    //Map variables
    private MapView mapMain;
    private IMapController mapController;
    Timer updateMapTimer;

    //Location submission
    private Button btnSubmitLocation;
    protected LocationManager locationManager;
    private boolean setLocation = false;

    //Calls to online APIs
    private JsonFromWeb returnObj;

    //Buttons for moving to next page
    public ExtendedFloatingActionButton titleView;

    //Users location
    private double userLat = 0, userLon = 0;

    //Local Reps
    JSONArray localReps;

    boolean airIssue = false;
    boolean emissionIssue = false;

    int aqi = 0;
    double emission = 0;

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
            //ActivityCompat.requestPermissions();
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            Toast.makeText(this, getString(R.string.map_error), Toast.LENGTH_SHORT).show();
            finish();
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
                openActivityIssue();
            }
        });
    }

    @Override
    public void onLocationChanged(Location location) {
        userLat = location.getLatitude();
        userLon = location.getLongitude();
        if (!setLocation) {
            System.out.println("Latitude:" + location.getLatitude() + ", Longitude:" + location.getLongitude());
            GeoPoint startPoint = new GeoPoint(userLat, userLon);
            mapController.setCenter(startPoint);
            setLocation = true;

            String callURL = "https://represent.opennorth.ca/boundaries/simple_shape?contains=" + userLat + "%2C" + userLon;
            returnObj = new JsonFromWeb(callURL);


            // Get the API data from the issue screen once the lat and lon are set
            getAirIssue();
            getEmissionsIssue();
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

        //Check the local reps
        String repString = "{}";
        if (localReps != null) {
            repString = localReps.toString();
        }

//        //Get the issues found in the area
//        String issues = "";
//        if (airIssue) {
//            issues += EcoIssues.AIR.getKey() + ", ";
//        }
//        if (waterIssue) {
//            issues += EcoIssues.WATER.getKey() + ", ";
//        }
//        if (trashIssue) {
//            issues += EcoIssues.TRASH.getKey() + ", ";
//        }
//
//        if (!issues.isEmpty()) {
//            issues = issues.substring(0, issues.length() - 2);
//        }

        //Start the next activity
        Intent intent = new Intent(this, IssueSelectActivity.class);
        intent.putExtra("reps", repString);
        intent.putExtra("lat", userLat);
        intent.putExtra("lon", userLon);
        intent.putExtra("aqi", aqi);
//        intent.putExtra("issues", issues);
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

        //Error handling for district not found
        if (array.length() == 0) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    titleView.setText(getString(R.string.map_no_location));
                    titleView.setBackgroundColor(getColor(R.color.red_bright));

                    btnSubmitLocation.setVisibility(View.VISIBLE);
                    btnSubmitLocation.setBackgroundColor(getColor(R.color.red_bright));
                    btnSubmitLocation.setText(getText(R.string.map_go_back));
                    btnSubmitLocation.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            finish();
                        }
                    });
                }
            });
        }

//        //Start getting other JSONs if all is good
//        getLocalReps();
//        getLocalIssues();

        //At this point the JSON is good
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

    private void getAirIssue() {

        String urlCall = "https://api.waqi.info/feed/geo:" +
                userLat + ";" +
                userLon +
                "/?token=bef2f7c377eda84ef9908eeac938ddae88989c5d";

        JsonFromWeb airQuality = new JsonFromWeb(urlCall);
        final JSONObject[] airQualityJSONHolder = {null};
        final int[] AQIScore = new int[1];
        //Use new timer to get response
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                while (airQuality.getJSONObject() == null) {
                    //Wait for response on separate thread
                }
                airQualityJSONHolder[0] = airQuality.getJSONObject();
                try {
                    if (airQualityJSONHolder[0]
                            .getJSONObject("data")
                            .getInt("aqi") > 80) {
                        airIssue = true;
                    }
                    aqi = airQualityJSONHolder[0].getJSONObject("data").getInt("aqi");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, 100);
    }

    private void getEmissionsIssue() {

        String urlCall = "https://api.v2.emissions-api.org/api/v2/carbonmonoxide/average.json?point=" + 49 + "&point=" + -123 + "&begin=2021-01-01&end=2021-11-21&limit=1&offset=0";

        JsonFromWeb emissionResult = new JsonFromWeb(urlCall);
        final JSONObject[] emissionJSONHolder = {null};
        final int[] emissionScore = new int[1];
        //Use new timer to get response
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                System.out.println("EMISSION RESULT: " + emissionResult.toString());
                while (emissionResult.toString().equals(null)) {
                    //Wait for response on separate thread
                }
                emissionJSONHolder[0] = emissionResult.getJSONObject();
//                try {
                    System.out.println(emissionJSONHolder);
//                    if (emissionJSONHolder[0]
//                            .getJSONObject("data")
//                            .getInt("aqi") > 80) {
//                        airIssue = true;
//                    }
//                    emission = emissionJSONHolder[0].getJSONObject("data").getInt("aqi");
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
            }
        }, 100);
    }

}