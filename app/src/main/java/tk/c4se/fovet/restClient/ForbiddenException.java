package tk.c4se.fovet.restClient;

import lombok.Getter;
import retrofit.RetrofitError;

/**
 * Created by nesachirou on 15/03/13.
 */
public class ForbiddenException extends Exception {
    @Getter
    RetrofitError originalError;

    public ForbiddenException(RetrofitError ex) {
        originalError = ex;
    }
}
