package com.example.nikhiljoshi.popularmovies;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

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

//        final ImageView imageView = (ImageView) rootView.findViewById(R.id.image_view);
//        Picasso.with(getContext()).load("http://i.imgur.com/DvpvklR.png").into(imageView);
        GridView gridView = (GridView) rootView.findViewById(R.id.gridview);
        gridView.setAdapter(new ImageAdapter(getContext()));

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getContext(), position + "", Toast.LENGTH_SHORT).show();
            }
        });

        return rootView;


    }
}
