package com.envelopepushers.envote;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapController;
import org.osmdroid.views.MapView;

public class LocationSelectMap extends AppCompatActivity {

    private MapView mapMain;
    private MapController mapController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_select_map);

        mapMain = (MapView) findViewById(R.id.map_main);
        mapMain.setTileSource(TileSourceFactory.DEFAULT_TILE_SOURCE);
        mapMain.setBuiltInZoomControls(true);
        mapController = (MapController) mapMain.getController();
        mapController.setZoom(13);
        GeoPoint gPt = new GeoPoint(51500000, -150000);
        mapController.setCenter(gPt);
    }
}