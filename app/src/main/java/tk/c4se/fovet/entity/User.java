package tk.c4se.fovet.entity;

import lombok.Data;
import tk.c4se.fovet.Settings;

/**
 * Created by nesachirou on 15/03/06.
 */
@Data
public class User {
    private int id;
    private String password;
    private String token;

    public User() {
        Settings settings = Settings.getInstance();
        id = settings.getUserId();
        password = settings.getPassword();
        token = settings.getToken();
    }

    public void save() {
        Settings settings = Settings.getInstance();
        settings.setUserId(id);
        settings.setPassword(password);
        settings.setToken(token);
    }
}
