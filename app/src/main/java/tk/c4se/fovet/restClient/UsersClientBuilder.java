package tk.c4se.fovet.restClient;

import lombok.Getter;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import tk.c4se.fovet.Settings;

/**
 * Created by nesachirou on 15/03/06.
 */
public class UsersClientBuilder {
    @Getter
    private UsersClient service;

    public UsersClientBuilder() {
        RequestInterceptor interceptor = new RequestInterceptor() {
            @Override
            public void intercept(RequestFacade request) {
                final String token = Settings.getInstance().getToken();
                if (null != token) {
                    request.addPathParam("token", token);
                    request.addQueryParam("token", token);
                }
            }
        };
        RestAdapter adapter = new RestAdapter.Builder().
                setEndpoint("http://10.0.2.2:3000").
                setRequestInterceptor(interceptor).
                build();
        service = adapter.create(UsersClient.class);
    }
}
