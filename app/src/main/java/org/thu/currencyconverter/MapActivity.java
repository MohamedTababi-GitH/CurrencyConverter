package org.thu.currencyconverter;

import android.content.Context;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

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
        /*ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });*/

        GeoPoints[] pointsDB = GeoPoints.getPointsDB();

        MapView         mMapView;
        MapController   mMapController;
        mMapView = (MapView) findViewById(R.id.osmmap);
        mMapView.setTileSource(TileSourceFactory.MAPNIK);
        mMapController = (MapController) mMapView.getController();


        mMapController.setZoom(17);


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

        // Add a marker
        Marker marker = new Marker(mMapView);
        marker.setPosition(gPt);
        marker.setTitle(capital);

        mMapView.getOverlays().add(marker);
    }
}