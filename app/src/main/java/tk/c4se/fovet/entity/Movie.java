package tk.c4se.fovet.entity;

import java.util.Date;

import ollie.Model;
import ollie.annotation.Column;
import ollie.annotation.Table;

/**
 * Created by nesachirou on 15/03/06.
 */
@Table("movies")
public class Movie extends Model {
    @Column("count")
    public Integer count = 10;
    @Column("latitude")
    public Float latitude;
    @Column("longitude")
    public Float longitude;
    @Column("uuid")
    public String uuid;
    @Column("created_at")
    public Date created_at;
    @Column("updated_at")
    public Date updated_at;
}
