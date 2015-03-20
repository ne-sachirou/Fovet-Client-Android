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
    List<Movie> index() throws ForbiddenException;

    @GET("/movies/{uuid}")
    Movie show(@Path("uuid") String uuid) throws ForbiddenException, NotFoundException;

    @Multipart
    @POST("/movies")
    Movie create(@Part("latitude") float latitude, @Part("longitude") float longitude, @Part("file") TypedFile file) throws ForbiddenException;

    @DELETE("/movies/{uuid}")
    void destroy(@Path("uuid") String uuid) throws ForbiddenException, NotFoundException;

    @GET("/movies/nearby?latitude={lalitude}&longitude={longitude}")
    List<Movie> nearby(@Path("latitude") float latitude, @Path("longitude") float longitude) throws ForbiddenException;

    @GET("/movies/{uuid}/file")
    Observable<Response> file(@Path("uuid") String uuid) throws ForbiddenException, NotFoundException;

    @POST("/movies/{uuid}/thumbup")
    Movie thumbup(@Path("uuid") String uuid) throws ForbiddenException, NotFoundException;
}
