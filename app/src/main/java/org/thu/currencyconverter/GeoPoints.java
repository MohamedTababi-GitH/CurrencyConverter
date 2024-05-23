package org.thu.currencyconverter;

import org.osmdroid.util.GeoPoint;

import java.util.ArrayList;

public class GeoPoints {
    String name;
    Double longitude;
    Double altitude;

    static GeoPoints[] pointsDB = {
            new GeoPoints("Bruxelles", 50.85500, 4.35123),
            new GeoPoints("Washington", 38.9072, -77.0369),
            new GeoPoints("Tokyo", 35.6895, 139.6917),
            new GeoPoints("Sofia", 42.6977, 23.3219),
            new GeoPoints("Prague", 50.0755, 14.4378),
            new GeoPoints("Copenhagen", 55.6761, 12.5683),
            new GeoPoints("London", 51.48933, -0.14406),
            new GeoPoints("Budapest", 47.4979, 19.0402),
            new GeoPoints("Warsaw", 52.2297, 21.0122),
            new GeoPoints("Bucharest", 44.4268, 26.1025),
            new GeoPoints("Stockholm", 59.3293, 18.0645),
            new GeoPoints("Bern", 46.9480, 7.4474),
            new GeoPoints("Reykjavik", 64.1466, -21.9426),
            new GeoPoints("Oslo", 59.9139, 10.7522),
            new GeoPoints("Zagreb", 45.8150, 15.9819),
            new GeoPoints("Ankara", 39.9334, 32.8641),
            new GeoPoints("Canberra", 48.8575, 2.3514),
            new GeoPoints("Brasilia", -15.8267, -47.8825),
            new GeoPoints("Ottawa", 45.4215, -75.6903),
            new GeoPoints("Beijing", 39.9042, 116.4074),
            new GeoPoints("Hong Kong", 22.3193, 114.1694),
            new GeoPoints("Jakarta", -6.2088, 106.8456),
            new GeoPoints("Jerusalem", 31.7683, 35.2130),
            new GeoPoints("New Delhi", 28.6139, 77.2090),
            new GeoPoints("Seoul", 37.5665, 126.9780),
            new GeoPoints("Mexico City", 19.4326, -99.1332),
            new GeoPoints("Kuala Lumpur", 3.1390, 101.6869),
            new GeoPoints("Wellington", -41.2865, 174.7762),
            new GeoPoints("Manila", 14.5995, 120.9842),
            new GeoPoints("Singapore", 1.3521, 103.8198),
            new GeoPoints("Bangkok", 13.7563, 100.5018),
            new GeoPoints("Cape Town", -33.9249, 18.4241)

    };
    public GeoPoints(String capital, Double altitude, Double longitude) {
        this.name = capital;
        this.altitude = altitude;
        this.longitude = longitude;
    }
    public GeoPoint getGeoPoint() {
        return new GeoPoint(this.altitude, this.longitude);
    }

    public static GeoPoints[] getPointsDB() {
        return pointsDB;
    }
}
