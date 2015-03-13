package tk.c4se.fovet.restClient;

import retrofit.client.Response;
import retrofit.http.DELETE;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;
import retrofit.http.Path;
import tk.c4se.fovet.entity.User;

/**
 * Created by nesachirou on 15/03/06.
 */
public interface UsersClient {
    @FormUrlEncoded
    @POST("/users")
    User create(@Field("password") String password);

    @DELETE("/users/{id}")
    void destroy(@Path("id") int id) throws ForbiddenException;

    @FormUrlEncoded
    @POST("/users/login")
    User login(@Field("id") int id, @Field("password") String password) throws ForbiddenException;
}
