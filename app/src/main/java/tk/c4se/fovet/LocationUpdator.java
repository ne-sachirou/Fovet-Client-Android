package tk.c4se.fovet;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;

import java.util.List;

import ollie.query.Select;
import tk.c4se.fovet.entity.Movie;
import tk.c4se.fovet.restClient.ForbiddenException;
import tk.c4se.fovet.restClient.MoviesClientBuilder;

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
        Log.v("Location", location.toString());
        final double latitude = location.getLatitude();
        final double longitude = location.getLongitude();
        Settings settings = Settings.getInstance();
        settings.setLatitude(latitude);
        settings.setLongitude(longitude);
        List<Movie> movies;
        try {
            movies = new MoviesClientBuilder().getService().nearby((float) latitude, (float) longitude);
        } catch (ForbiddenException ex) {
            return;
        }
        for (Movie movie : movies) {
            if (null != Select.from(Movie.class).where("uuid = ?", movie.uuid).fetchSingle()) {
                movie.save();
            }
        }
    }
}
