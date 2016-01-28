package com.example.nikhiljoshi.popularmovies;

import android.app.Activity;
import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by nikhiljoshi on 1/27/16.
 * Guided by code in android-custom-arrayadapter from udacity github
 */
public class MovieImageAdapter extends ArrayAdapter<Movie> {

    public MovieImageAdapter(Activity context, List<Movie> movieImagesUrls) {
        super(context, 0, movieImagesUrls);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        //Get the Image for the movie at that specific location
        final Movie currentMovie = getItem(position);
        String movieImageUrl = currentMovie.getPosterImageUrl();

        if (convertView == null) {
            // If a view isnt present, then obtain the LayoutInflater from the current context,
            // and then inflates the view hierarchy present in the xml resource
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.grid_item_movie, parent, false);
        }

        final ImageView imageView = (ImageView) convertView.findViewById(R.id.grid_movie_image);
        Picasso.with(getContext()).load(movieImageUrl).into(imageView);

        return convertView;
    }
}
