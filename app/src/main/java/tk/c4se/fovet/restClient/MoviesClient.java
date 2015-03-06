package tk.c4se.fovet.restClient;

import java.util.List;

import retrofit.client.Response;
import retrofit.http.DELETE;
import retrofit.http.Field;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;
import rx.Observable;
import tk.c4se.fovet.entity.Movie;

/**
 * Created by nesachirou on 15/03/06.
 */
public interface MoviesClient {
    @GET("/movies")
    List<Movie> index();

    @GET("/movies/{uuid}")
    Movie show(@Path("uuid") String uuid);

    @POST("/movies")
    Movie create();

    @DELETE("/movies/{uuid}")
    void destroy(@Path("uuid") String uuid);

    @GET("/movies/nearby?latitude={lalitude}&longitude={longitude}")
    List<Movie> nearby(@Path("latitude") double latitude, @Path("longitude") double longitude);

    @GET("/movies/{uuid}/file")
    Observable<Response> file(@Path("uuid") String uuid);

    @POST("/movies/{uuid}/thumbup")
    Movie thumbup(@Path("uuid") String uuid);
}
