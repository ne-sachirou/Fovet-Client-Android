package tk.c4se.fovet;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import ollie.Ollie;
import tk.c4se.fovet.entity.User;


public class MainActivity extends ActionBarActivity {

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

    public void startShoot(View v) {
        startActivity(new Intent(this, ShootActivity.class));
    }
}
