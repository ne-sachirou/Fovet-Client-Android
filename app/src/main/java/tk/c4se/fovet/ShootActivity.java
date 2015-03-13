package tk.c4se.fovet;

import android.content.Context;
import android.hardware.Camera;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.FrameLayout;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import retrofit.RetrofitError;
import retrofit.mime.TypedFile;
import tk.c4se.fovet.entity.Movie;
import tk.c4se.fovet.restClient.MoviesClientBuilder;


public class ShootActivity extends ActionBarActivity {
    private CameraPreview preview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shoot);
        preview = new CameraPreview(this);
        ((FrameLayout) findViewById(R.id.cameraPreview)).addView(preview);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_shoot, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void shoot(View v) {
        preview.takePicture(new AsyncTask<byte[], Integer, Integer>() {
            @Override
            protected Integer doInBackground(byte[]... params) {
                final byte[] data = params[0];
                final String tmpFileName = "tmp.jpg";
                try {
                    FileOutputStream stream = null;
                    stream = openFileOutput(tmpFileName, MODE_PRIVATE);
                    stream.write(data, 0, data.length);
                    stream.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                    finish();
                    return null;
                }
                File file = new File(getFilesDir(), tmpFileName);
                Movie movie = null;
                try {
                    movie = new MoviesClientBuilder().getService().create(0, 0, new TypedFile("image/jpeg", file));
                } catch (RetrofitError ex) {
                    ex.printStackTrace();
                    file.delete();
                    finish();
                    return null;
                }
                movie.save();
                file.renameTo(new File(getFilesDir(), movie.uuid + ".jpg"));
                finish();
                return null;
            }
        });
    }

    public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {
        private Camera camera;

        public CameraPreview(Context context) {
            super(context);
            SurfaceHolder holder = getHolder();
            holder.addCallback(this);
            holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            camera = Camera.open();
            try {
                camera.setPreviewDisplay(holder);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            // Camera.Parameters params = camera.getParameters();
            // Camera.Size size = params.getPictureSize();
            camera.startPreview();
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            camera.release();
        }

        public void takePicture(final AsyncTask<byte[], Integer, Integer> pictureCallback) {
            Camera.ShutterCallback onShutter = new Camera.ShutterCallback() {
                @Override
                public void onShutter() {
                }
            };
            Camera.PictureCallback onPicture = new Camera.PictureCallback() {
                @Override
                public void onPictureTaken(byte[] data, Camera camera) {
                    pictureCallback.execute(data);
                }
            };
            camera.takePicture(onShutter, null, onPicture);
        }
    }
}
