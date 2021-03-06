package tk.c4se.fovet;

import android.location.Location;
import android.location.LocationListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import java.util.List;

import ollie.query.Select;
import retrofit.RetrofitError;
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
        float latitude = (float) location.getLatitude();
        float longitude = (float) location.getLongitude();
        Settings settings = Settings.getInstance();
        settings.setLatitude(latitude);
        settings.setLongitude(longitude);
        fetchAndSaveNearby(latitude, longitude);
    }

    private void fetchAndSaveNearby(float latitude, float longitude) {
        (new AsyncTask<Float, Integer, Integer>() {
            @Override
            protected Integer doInBackground(Float... params) {
                float latitude = params[0];
                float longitude = params[1];
                List<Movie> movies;
                try {
                    movies = new MoviesClientBuilder().getService().nearby(latitude, longitude);
                } catch (ForbiddenException ex) {
                    try {
                        new LoginProxy().refreshToken();
                    } catch (RetrofitError ex2) {
                        ex2.printStackTrace();
                        return null;
                    }
                    try {
                        movies = new MoviesClientBuilder().getService().nearby(latitude, longitude);
                    } catch (ForbiddenException | RetrofitError ex2) {
                        ex2.printStackTrace();
                        return null;
                    }
                } catch (RetrofitError ex) {
                    ex.printStackTrace();
                    return null;
                }
                for (Movie movie : movies) {
                    if (0 == Select.columns("COUNT(*)").from(Movie.class).where("uuid = ?", movie.uuid).fetchValue(Integer.class)) {
                        movie.save();
                    }
                }
                return null;
            }
        }).execute(latitude, longitude);
    }
}
