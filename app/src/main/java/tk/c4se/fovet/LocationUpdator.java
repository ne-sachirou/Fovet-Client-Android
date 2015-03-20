package tk.c4se.fovet;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by nesachirou on 15/03/20.
 */
public class LocationUpdator implements LocationListener {
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onLocationChanged(Location location) {
        Settings settings = Settings.getInstance();
        settings.setLatitude(location.getLatitude());
        settings.setLongitude(location.getLongitude());
        Log.v("Location", location.toString());
    }
}
