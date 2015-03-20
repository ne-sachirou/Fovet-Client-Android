package tk.c4se.fovet.restClient;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.converter.GsonConverter;
import tk.c4se.fovet.Settings;
import tk.c4se.fovet.entity.User;

/**
 * Created by nesachirou on 15/03/13.
 */
public abstract class ClientBuilder {
    protected RestAdapter getAdapter() {
        Gson gson = new GsonBuilder().
                // setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").
                        registerTypeAdapter(Date.class, new DateDeserializer()).
                create();
        return new RestAdapter.Builder().
                setEndpoint(Settings.getInstance().getRestEndpoint()).
                setRequestInterceptor(new ClientRequestInterceptor()).
                setErrorHandler(new ClientErrorHandler()).
                setConverter(new GsonConverter(gson)).
                setLogLevel(RestAdapter.LogLevel.FULL).
                build();
    }

    private class DateDeserializer implements JsonDeserializer<Date> {
        @Override
        public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            String str = json.getAsJsonPrimitive().getAsString();
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            format.setTimeZone(TimeZone.getTimeZone("GMT"));
            try {
                return format.parse(str);
            } catch (ParseException ex) {
                ex.printStackTrace();
                return null;
            }
        }
    }

    private class ClientRequestInterceptor implements RequestInterceptor {
        @Override
        public void intercept(RequestFacade request) {
            String token = new User().getToken();
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
            if (null == res) {
                return cause;
            }
            if (403 == res.getStatus()) {
                return new ForbiddenException(cause);
            }
            if (404 == res.getStatus()) {
                return new NotFoundException(cause);
            }
            return cause;
        }
    }
}
