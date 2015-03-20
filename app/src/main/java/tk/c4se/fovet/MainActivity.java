package tk.c4se.fovet;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import ollie.Ollie;
import ollie.query.Select;
import tk.c4se.fovet.entity.Movie;
import tk.c4se.fovet.entity.User;


public class MainActivity extends ActionBarActivity implements MainItemFragment.OnFragmentInteractionListener {
    private List<MainItemFragment> itemFragments = new ArrayList<>();
    private Timer redrawTimer;
    private LocationManager locationManager;
    private LocationListener locationListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Settings.init(this);
        Ollie.with(this).setName("fovet.db").setVersion(1).setLogLevel(Ollie.LogLevel.FULL).setCacheSize(100).init();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (null == new User().getToken()) {
            startActivity(new Intent(this, LoginActivity.class));
            return;
        }
        (new AsyncTask<Integer, Integer, Integer>() {
            @Override
            protected Integer doInBackground(Integer... params) {
                new LoginProxy().login(MainActivity.this, 0);
                return null;
            }
        }).execute();
        redrawTimer = new Timer(true);
        redrawTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                redrawMovies();
            }
        }, 0, 10000);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationListener = new LocationUpdator();
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 31000, 100, locationListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        redrawTimer.cancel();
        locationManager.removeUpdates(locationListener);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void removeMainItemFragment(MainItemFragment fragment) {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.remove(fragment);
        itemFragments.remove(fragment);
        ft.commit();
    }

    public void startShoot(View v) {
        startActivity(new Intent(this, ShootActivity.class));
    }

    private void redrawMovies() {
        List<Movie> movies = Select.from(Movie.class).orderBy("created_at").fetch();
        Collections.reverse(movies);
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        List<MainItemFragment> newItemFragments = new ArrayList<>();
        for (Movie movie : movies) {
            boolean isAlraedyShown = false;
            for (MainItemFragment f : itemFragments) {
                if (f.getMovieId() == movie.uuid) {
                    itemFragments.remove(f);
                    newItemFragments.add(f);
                    isAlraedyShown = true;
                    break;
                }
            }
            if (isAlraedyShown) {
                break;
            }
            MainItemFragment fragment = MainItemFragment.newInstance(movie.uuid, movie.count);
            newItemFragments.add(fragment);
            ft.add(R.id.ItemsHolder, fragment);
        }
        for (MainItemFragment f : itemFragments) {
            ft.remove(f);
        }
        itemFragments = newItemFragments;
        ft.commit();
    }
}
