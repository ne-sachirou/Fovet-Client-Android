package tk.c4se.fovet.restClient;

import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import tk.c4se.fovet.Settings;

/**
 * Created by nesachirou on 15/03/13.
 */
public abstract class ClientBuilder {
    protected RestAdapter getAdapter() {
        return new RestAdapter.Builder().
                setEndpoint(Settings.getInstance().getRestEndpoint()).
                setRequestInterceptor(new ClientRequestInterceptor()).
                setErrorHandler(new ClientErrorHandler()).
                setLogLevel(RestAdapter.LogLevel.FULL).
                build();
    }

    private class ClientRequestInterceptor implements RequestInterceptor {
        @Override
        public void intercept(RequestFacade request) {
            String token = Settings.getInstance().getToken();
            if (null != token) {
                request.addPathParam("token", token);
                request.addQueryParam("token", token);
            }
            request.addHeader("User-Agent", "Fovet-Client-Android");
        }
    }

    private class ClientErrorHandler implements retrofit.ErrorHandler {
        @Override
        public Throwable handleError(RetrofitError cause) {
            Response res = cause.getResponse();
            return cause;
        }
    }
}
