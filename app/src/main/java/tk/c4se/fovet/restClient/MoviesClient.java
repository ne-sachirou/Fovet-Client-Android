package tk.c4se.fovet.restClient;

import java.util.List;

import retrofit.client.Response;
import retrofit.http.DELETE;
import retrofit.http.GET;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.Part;
import retrofit.http.Path;
import retrofit.mime.TypedFile;
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

    @Multipart
    @POST("/movies")
    Movie create(@Part("latitude") double latitude, @Part("longitude") double longitude, @Part("file") TypedFile file);

    @DELETE("/movies/{uuid}")
    void destroy(@Path("uuid") String uuid);

    @GET("/movies/nearby?latitude={lalitude}&longitude={longitude}")
    List<Movie> nearby(@Path("latitude") double latitude, @Path("longitude") double longitude);

    @GET("/movies/{uuid}/file")
    Observable<Response> file(@Path("uuid") String uuid);

    @POST("/movies/{uuid}/thumbup")
    Movie thumbup(@Path("uuid") String uuid);
}
