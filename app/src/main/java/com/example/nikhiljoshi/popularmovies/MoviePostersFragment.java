package com.example.nikhiljoshi.popularmovies;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

/**
 * A placeholder fragment containing a simple view.
 */
public class MoviePostersFragment extends Fragment {

    public MoviePostersFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        final ImageView imageView = (ImageView) rootView.findViewById(R.id.image_view);
        Picasso.with(getContext()).load("http://i.imgur.com/DvpvklR.png").into(imageView);

        return rootView;


    }
}
