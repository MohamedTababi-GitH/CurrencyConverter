package org.thu.currencyconverter;

import android.content.Context;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import android.preference.PreferenceManager;
import android.util.Log;

import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

public class MapActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        Context context = this.getApplicationContext();
        Configuration.getInstance().load(context, PreferenceManager.getDefaultSharedPreferences(context));
        setContentView(R.layout.activity_map);

        GeoPoints[] pointsDB = GeoPoints.getPointsDB();

        // I used open street map library "osmdroid" to implement the map activity
        MapView         mMapView;
        MapController   mMapController;
        mMapView = findViewById(R.id.osmmap);
        mMapView.setTileSource(TileSourceFactory.MAPNIK);

        // tap the screen to reveal the map controls
        mMapController = (MapController) mMapView.getController();

        // setting the map zoom level
        mMapController.setZoom(17);

        // retrieving the capital from the previous activity intent
        String capital = (String)getIntent().getSerializableExtra("capital");

        double longitude = 0;
        double altitude = 0;
        for (GeoPoints pt : pointsDB) {
            if (pt.name.equals(capital)){
                longitude = pt.longitude;
                altitude = pt.altitude;
                Log.d("MyApp", "Longitude: " + longitude + ", Altitude: " + altitude);
            }
        }
        GeoPoint gPt = new GeoPoint(altitude, longitude);
        mMapController.setCenter(gPt);

        // Adding a marker
        Marker marker = new Marker(mMapView);
        marker.setPosition(gPt);
        marker.setTitle(capital);

        mMapView.getOverlays().add(marker);
    }
}