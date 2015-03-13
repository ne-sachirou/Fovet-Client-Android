package tk.c4se.fovet.restClient;

import lombok.Getter;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import tk.c4se.fovet.Settings;

/**
 * Created by nesachirou on 15/03/06.
 */
public class UsersClientBuilder extends ClientBuilder {
    @Getter
    private UsersClient service;

    public UsersClientBuilder() {
        service = getAdapter().create(UsersClient.class);
    }
}
