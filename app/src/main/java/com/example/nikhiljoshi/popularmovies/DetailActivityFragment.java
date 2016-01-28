package com.example.nikhiljoshi.popularmovies;

import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment {

    public DetailActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

        final Movie movie = getActivity().getIntent().getParcelableExtra("movie");

        TextView originalTitle = (TextView)rootView.findViewById(R.id.original_title);
        originalTitle.setText(movie.getOriginalTitle());

        TextView overview = (TextView) rootView.findViewById(R.id.overview);
        overview.setText(movie.getPlotSynopsis());

        TextView userRating = (TextView) rootView.findViewById(R.id.user_rating);
        userRating.setText(movie.getUserRating());

        TextView releaseDate = (TextView) rootView.findViewById(R.id.release_date);
        releaseDate.setText(movie.getReleaseDate());

        return rootView;
    }
}
