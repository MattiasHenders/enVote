package com.envelopepushers.envote;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ProgressBar;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.CustomZoomButtonsController;
import org.osmdroid.views.MapController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.compass.CompassOverlay;

public class LocationSelectMap extends Activity implements LocationListener {

    private MapView mapMain;
    private IMapController mapController;
    private Button btnSubmitLocation;
    protected LocationManager locationManager;
    protected LocationListener locationListener;
    private boolean setLocation = false;

    private double userLat = 0, userLon = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_select_map);

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
        mapController.setZoom(17);

        //Burnaby lat/lon as default
        GeoPoint startPoint;
        if (userLon == 0 || userLat == 0) {
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
        System.out.println("Latitude:" + location.getLatitude() + ", Longitude:" + location.getLongitude());
        userLat = location.getLatitude();
        userLon = location.getLongitude();
        if (!setLocation) {
            GeoPoint startPoint = new GeoPoint(userLat, userLon);
            mapController.setCenter(startPoint);
            setLocation = true;
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


}