package tk.c4se.fovet.restClient;

import lombok.Getter;
import retrofit.RetrofitError;

/**
 * Created by nesachirou on 15/03/20.
 */
public class NotFoundException extends Exception {
    @Getter
    private RetrofitError originalError;

    public NotFoundException(RetrofitError ex) {
        originalError = ex;
    }
}
