package tk.c4se.fovet;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import retrofit.RetrofitError;
import tk.c4se.fovet.entity.User;


public class LoginActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
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

    public void logInOrSignup(View v) {
        final String password = ((TextView) findViewById(R.id.editTextPassword)).getEditableText().toString();
        (new AsyncTask<Integer, Integer, Integer>() {
            @Override
            protected Integer doInBackground(Integer... params) {
                User user = new User();
                try {
                    if (0 == new User().getId()) {
                        new LoginProxy().createUser(password);
                    } else {
                        user.setPassword(password);
                        user.save();
                        new LoginProxy().refreshToken();
                    }
                } catch (RetrofitError ex) {
                    ex.printStackTrace();
                    Toast.makeText(LoginActivity.this, ex.getMessage(), Toast.LENGTH_SHORT).show();
                    return null;
                }
                finish();
                return null;
            }
        }).execute();
    }
}
