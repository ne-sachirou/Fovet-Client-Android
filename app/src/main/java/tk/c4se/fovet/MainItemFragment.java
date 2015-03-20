package tk.c4se.fovet;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import lombok.Getter;
import retrofit.client.Response;
import rx.functions.Action1;
import tk.c4se.fovet.restClient.ForbiddenException;
import tk.c4se.fovet.restClient.MoviesClientBuilder;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MainItemFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MainItemFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainItemFragment extends Fragment {
    private static final String ARG_MOVIE_ID = "movieId";
    private static final String ARG_COUNT = "count";

    @Getter
    private String movieId;
    private int count;

    private OnFragmentInteractionListener mListener;

    public MainItemFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param movieId Parameter 1.
     * @param count   Parameter 2.
     * @return A new instance of fragment MainItemFragment.
     */
    public static MainItemFragment newInstance(String movieId, int count) {
        MainItemFragment fragment = new MainItemFragment();
        Bundle args = new Bundle();
        args.putString(ARG_MOVIE_ID, movieId);
        args.putInt(ARG_COUNT, count);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            movieId = getArguments().getString(ARG_MOVIE_ID);
            count = getArguments().getInt(ARG_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_main_item, container, false);
        ((TextView) v.findViewById(R.id.textViewCount)).setText("" + count);
        final ImageView imageView = (ImageView) v.findViewById(R.id.imageView);
        final File file = new File(getActivity().getFilesDir(), movieId + ".jpg");
        if (file.exists()) {
            Bitmap image = BitmapFactory.decodeFile(file.getAbsolutePath());
            imageView.setImageBitmap(image);
        } else {
            (new AsyncTask<Integer, Integer, Integer>() {
                @Override
                protected Integer doInBackground(Integer... params) {
                    try {
                        new MoviesClientBuilder().getService().file(movieId).subscribe(new Action1<Response>() {
                            @Override
                            public void call(Response response) {
                                try {
                                    OutputStream out = getActivity().openFileOutput(file.getName(), Context.MODE_PRIVATE);
                                    InputStream in = response.getBody().in();
                                    byte[] buffer = new byte[8];
                                    int length;
                                    while ((length = in.read(buffer)) != -1) {
                                        out.write(buffer, 0, length);
                                    }
                                    in.close();
                                    out.close();
                                    if (file.exists()) {
                                        Bitmap image = BitmapFactory.decodeFile(file.getAbsolutePath());
                                        imageView.setImageBitmap(image);
                                    }
                                } catch (IOException ex) {
                                    ex.printStackTrace();
                                }
                            }
                        });
                    } catch (ForbiddenException ex) {
                    }
                    return null;
                }
            }).execute();
        }
        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}
