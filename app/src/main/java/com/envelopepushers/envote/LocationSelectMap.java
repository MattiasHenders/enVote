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

    /**
     * The map view.
     */
    private MapView mapMain;

    /**
     * Controls the map view.
     */
    private IMapController mapController;

    /**
     * A timer for updating the map.
     */
    Timer updateMapTimer;

    /**
     * The submit button.
     */
    private Button btnSubmitLocation;

    /**
     * Manages user location.
     */
    protected LocationManager locationManager;

    /**
     * Determines if location should be set.
     */
    private boolean setLocation = false;

    /**
     * Response from call to online APIs.
     */
    private JsonFromWeb returnObj;

    /**
     * Buttons for moving to next page.
     */
    public ExtendedFloatingActionButton titleView;

    /**
     * The user's location.
     */
    private double userLat = 0, userLon = 0;

    /**
     * The default zoom of the map.
     */
    private int defaultZoom = 14;

    /**
     * JSON array of all local reps card view.
     */
    JSONArray localReps;

    /**
     * The air issue.
     */
    boolean airIssue = false;

    /**
     * The emission issue.
     */
    boolean emissionIssue = false;

    /**
     * The AQI intent value.
     */
    int aqi = 0;

    /**
     * The emission intent value.
     */
    double emission = 0;

    /**
     * Called on activity creation.
     * @param savedInstanceState
     */
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
        mapController.setZoom(defaultZoom);

        GeoPoint startPoint;
        if (userLon == 0 || userLat == 0) {
            //Burnaby lat/lon as default
            startPoint = new GeoPoint(0.0, 0.0);
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

    /**
     * Opens the next activity screen
     */
    public void openActivityIssue() {

        //Check the local reps
        String repString = "{}";
        if (localReps != null) {
            repString = localReps.toString();
        }

        //Start the next activity
        Intent intent = new Intent(this, IssueSelectActivity.class);
        intent.putExtra("reps", repString);
        intent.putExtra("lat", userLat);
        intent.putExtra("lon", userLon);
        intent.putExtra("aqi", aqi);
        intent.putExtra("emission", emission);
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

    /**
     * On a separate timer schedules the map to update
     */
    private void scheduleMapUpdate() {

        updateMapTimer = new Timer();
        //Timers
        int timerPeriod = 100;
        int timerDelay = 50;
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
        }, timerDelay, timerPeriod);
    }

    /**
     * Adds boundaries to the map
     * @throws JSONException
     */
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

    /**
     * Gets the air issue value and places it in the GUI.
     */
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

    /**
     * Gets the emission issue value and sends it to the GUI.
     */
    private void getEmissionsIssue() {
        String urlCall = "https://api.v2.emissions-api.org/api/v2/carbonmonoxide/average.json?point=" + 49 + "&point=" + -123 + "&begin=2021-01-01&end=2021-11-21&limit=1&offset=0";
        JsonFromWeb emissionResult = new JsonFromWeb(urlCall);
        final JSONObject[] emissionJSONHolder = {null};
        final int[] emissionScore = new int[1];
        //Use new timer to get response
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                while (emissionResult.getJSONObject() == null) {
                    //Wait for response on separate thread
                }
                emissionJSONHolder[0] = emissionResult.getJSONObject();
                try {
                    emission = emissionJSONHolder[0].getJSONArray("data").getJSONObject(0).getDouble("average");

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, 100);
    }
}
