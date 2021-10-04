package com.envelopepushers.envote;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
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

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.CustomZoomButtonsController;
import org.osmdroid.views.MapController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.compass.CompassOverlay;

public class LocationSelectMap extends AppCompatActivity {

    private MapView mapMain;
    private IMapController mapController;
    private Button btnSubmitLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_select_map);

        //Check permissions
        checkPermissions();

        //load/initialize the osmdroid configuration, this can be done
        Context ctx = getApplicationContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));

        //Set the map objects
        mapMain = findViewById(R.id.map_main);
        mapMain.setTileSource(TileSourceFactory.MAPNIK);

        mapController = mapMain.getController();
        mapController.setZoom(17);

        //Burnaby lat/lon as default
        GeoPoint startPoint = new GeoPoint(49.2488, -122.9805);
        mapController.setCenter(startPoint);

        btnSubmitLocation = findViewById(R.id.btn_location_submit);

        //OnClickListeners Set
//        btnSubmitLocation.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                finish();
//            }
//        });
        btnSubmitLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openActivityIssue();
            }
        });
    }

    public void openActivityIssue() {
        Intent intent = new Intent(this, activity_issue_select.class);
        startActivity(intent);
        finish();
    }
    public void onResume(){
        super.onResume();
        mapMain.onResume();
    }

    public void onPause(){
        super.onPause();
        mapMain.onPause();
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