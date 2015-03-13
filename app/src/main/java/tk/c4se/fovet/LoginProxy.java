package tk.c4se.fovet;

import android.app.Activity;
import android.content.Intent;

import retrofit.RetrofitError;
import tk.c4se.fovet.entity.User;
import tk.c4se.fovet.restClient.ForbiddenException;
import tk.c4se.fovet.restClient.UsersClient;
import tk.c4se.fovet.restClient.UsersClientBuilder;

/**
 * Created by nesachirou on 15/03/14.
 */
public class LoginProxy {
    public User refreshToken() throws RetrofitError {
        UsersClient client = new UsersClientBuilder().getService();
        User user = new User();
        try {
            user = client.login(user.getId(), user.getPassword());
        } catch (ForbiddenException ex) {
            return createUser(user.getPassword());
        }
        user.save();
        return user;
    }

    public User createUser(final String password) throws RetrofitError {
        UsersClient client = new UsersClientBuilder().getService();
        User user = client.create(password);
        user.save();
        return user;
    }

    /**
     * Do not call from LoginActivity.
     *
     * @param activity
     * @param requestCode
     */
    public void login(Activity activity, int requestCode) {
        User user = new User();
        if (null == user.getToken() || null == user.getPassword()) {
            startLoginActivity(activity, requestCode);
            return;
        }
        try {
            refreshToken();
        } catch (RetrofitError ex) {
            startLoginActivity(activity, requestCode);
        }
    }

    private void startLoginActivity(Activity activity, int requestCode) {
        activity.startActivityForResult(new Intent(activity, LoginActivity.class), requestCode);
    }
}
