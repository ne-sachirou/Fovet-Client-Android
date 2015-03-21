package tk.c4se.fovet.entity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;

import ollie.Model;
import ollie.annotation.Column;
import ollie.annotation.NotNull;
import ollie.annotation.Table;
import ollie.annotation.Unique;
import retrofit.RetrofitError;
import retrofit.client.Response;
import rx.functions.Action1;
import tk.c4se.fovet.restClient.ForbiddenException;
import tk.c4se.fovet.restClient.MoviesClientBuilder;
import tk.c4se.fovet.restClient.NotFoundException;

/**
 * Created by nesachirou on 15/03/06.
 */
@Table("movies")
public class Movie extends Model {
    public static File saveImageToTmpFile(Context context, byte[] data) throws IOException {
        final String tmpFileName = "tmp.jpg";
        FileOutputStream stream = context.openFileOutput(tmpFileName, Context.MODE_PRIVATE);
        stream.write(data, 0, data.length);
        stream.close();
        return new File(context.getFilesDir(), tmpFileName);
    }

    @Column("count")
    @NotNull
    public Integer count = 10;
    @Column("latitude")
    @NotNull
    public Float latitude;
    @Column("longitude")
    @NotNull
    public Float longitude;
    @Column("uuid")
    @Unique
    @NotNull
    public String uuid;
    @Column("created_at")
    public Date created_at;
    @Column("updated_at")
    public Date updated_at;

    public File getFile(Context context) {
        return new File(context.getFilesDir(), uuid + ".jpg");
    }

    public void attachImageToView(final Context context, final ImageView view) {
        final File file = getFile(context);
        if (file.exists()) {
            Bitmap image = BitmapFactory.decodeFile(file.getAbsolutePath());
            view.setImageBitmap(image);
        } else {
            fetchImageFile(context, file, new Runnable() {
                @Override
                public void run() {
                    if (file.exists()) {
                        Bitmap image = BitmapFactory.decodeFile(file.getAbsolutePath());
                        view.setImageBitmap(image);
                    }
                }
            });
        }
    }

    public void removeCache(Context context) {
        getFile(context).delete();
    }

    private void fetchImageFile(final Context context, final File file, final Runnable runnable) {
        (new AsyncTask<Integer, Integer, Integer>() {
            @Override
            protected Integer doInBackground(Integer... params) {
                try {
                    new MoviesClientBuilder().getService().file(uuid).subscribe(new Action1<Response>() {
                        @Override
                        public void call(Response response) {
                            InputStream in = null;
                            OutputStream out = null;
                            try {
                                in = response.getBody().in();
                                out = context.openFileOutput(file.getName(), Context.MODE_PRIVATE);
                                byte[] buffer = new byte[1024];
                                int length;
                                while ((length = in.read(buffer)) != -1) {
                                    out.write(buffer, 0, length);
                                }
                                runnable.run();
                            } catch (IOException ex) {
                                ex.printStackTrace();
                                file.delete();
                            } finally {
                                try {
                                    if (null != out) {
                                        out.close();
                                    }
                                } catch (IOException ex) {
                                }
                                try {
                                    if (null != in) {
                                        in.close();
                                    }
                                } catch (IOException ex) {
                                }
                            }
                        }
                    });
                } catch (ForbiddenException | NotFoundException | RetrofitError ex) {
                    ex.printStackTrace();
                }
                return null;
            }
        }).execute();
    }
}
