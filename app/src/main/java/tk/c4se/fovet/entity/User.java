package tk.c4se.fovet.entity;

import lombok.Data;

/**
 * Created by nesachirou on 15/03/06.
 */
@Data
public class User {
    private int id;
    private String password;
    private String token;
}
