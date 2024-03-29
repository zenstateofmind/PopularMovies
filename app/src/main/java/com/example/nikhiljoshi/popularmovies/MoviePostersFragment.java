package com.example.nikhiljoshi.popularmovies;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * A placeholder fragment containing a simple view.
 */
public class MoviePostersFragment extends Fragment {

    private MovieImageAdapter movieImageAdapter;

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

        movieImageAdapter = new MovieImageAdapter(getActivity(), new ArrayList<Movie>());

        GridView movie_grid = (GridView) rootView.findViewById(R.id.movieimages_gridview);
        movie_grid.setAdapter(movieImageAdapter);
        movie_grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Movie movie = movieImageAdapter.getItem(position);
                Intent intent = new Intent(getActivity(), DetailActivity.class);
                intent.putExtra("movie", movie);
                startActivity(intent);
            }
        });

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        updateMovies();
    }

    private void updateMovies() {
        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        final String sortBy = sharedPreferences.getString(getString(R.string.image_sorting_key), getString(R.string.image_sorting_default));

        FetchMovieTask fetchMovies = new FetchMovieTask();
        fetchMovies.execute(sortBy);
    }

    public class FetchMovieTask extends AsyncTask<String, Void, Movie[]> {

        private final String LOG_TAG = FetchMovieTask.class.getSimpleName();

        @Override
        protected void onPostExecute(Movie[] movies) {
            if (movies != null) {
                movieImageAdapter.clear();
                for (Movie movie : movies) {
                    movieImageAdapter.add(movie);
                }
            }
        }

        @Override
        protected Movie[] doInBackground(String[] params) {

            // param[0] is going to be a preference whether to sort by
            // popularity of ratings
            String movieDbUrlStr = getUrl(params[0]);

            /**
             * Referred to code from Lesson 1-3 WeatherForecast lectures
             */
            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            String movieInfoJson = null;

            try {
                // Connect the URL for the MovieDB query
                final URL url = new URL(movieDbUrlStr);

                // Connect the request to MovieDb website, and open connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();

                if (inputStream == null) {
                    return null;
                }

                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    return null;
                }

                movieInfoJson = buffer.toString();

            } catch (IOException e) {
                Log.e(LOG_TAG, "Error while fetching information from the moviedb api", e);
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }

                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }

            Log.i(LOG_TAG, "movieDbJSON: " + movieInfoJson);

            // call the url and get all the information -- image urls
            try {
                final Movie[] movies = getMoviePosterUrlsFromJson(movieInfoJson);

                Log.i(LOG_TAG, "Images have been collected!: " + Arrays.toString(movies));

                return movies;
            } catch (JSONException e) {
                Log.e(LOG_TAG, "Failed in getting image urls", e);
                return null;
            }

        }

        private String convertToFullMovieImageUrl(String moviePath) {
            //http://image.tmdb.org/t/p/w185/nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg
            Log.i(LOG_TAG, "path: " + moviePath);
            Uri.Builder builder = new Uri.Builder();
            builder.scheme("http")
                    .authority("image.tmdb.org")
                    .appendPath("t")
                    .appendPath("p")
                    .appendPath("w185")
                    .appendPath(moviePath);
            return builder.build().toString();
        }

        private String getUrl(String sortByMetric) {
            //http://api.themoviedb.org/3/discover/movie?sort_by=popularity.desc&api_key=c050ce6e7ffdda633b23601366858a82
            Uri.Builder builder = new Uri.Builder();
            builder.scheme("http")
                    .authority("api.themoviedb.org")
                    .appendPath("3")
                    .appendPath("discover")
                    .appendPath("movie")
                    .appendQueryParameter("sorty_by", sortByMetric)
                    .appendQueryParameter("api_key", "c050ce6e7ffdda633b23601366858a82");

            return builder.build().toString();
        }

        /**
         *     original title
         *     movie poster image thumbnail
         *     A plot synopsis (called overview in the api)
         *     user rating (called vote_average in the api)
         *     release date
         */
        private Movie[] getMoviePosterUrlsFromJson(String movieInfoJson) throws JSONException {

            final String MOVIE_RESULT_LIST = "results";
            final String POSTER_PATH = "poster_path";
            final String ORIGINAL_TITLE = "original_title";
            final String OVERVIEW = "overview";
            final String USER_RATING = "vote_average";
            final String RELEASE_DATE = "release_date";

            JSONObject jsonObject = new JSONObject(movieInfoJson);
            JSONArray moviesInfoArray = jsonObject.getJSONArray(MOVIE_RESULT_LIST);

            Movie[] movies = new Movie[moviesInfoArray.length()];

            for (int i = 0; i < moviesInfoArray.length(); i++) {
                JSONObject movieInfo = moviesInfoArray.getJSONObject(i);

                final String moviePath = convertToFullMovieImageUrl(movieInfo.getString(POSTER_PATH).substring(1));
                final String originalTitle = movieInfo.getString(ORIGINAL_TITLE);
                final String overview = movieInfo.getString(OVERVIEW);
                final String userRating = movieInfo.getString(USER_RATING);
                final String releaseDate = movieInfo.getString(RELEASE_DATE);

                Movie movie = new Movie(originalTitle, moviePath, overview, userRating, releaseDate);

                movies[i] = movie;
            }

            return movies;
        }
    }
}
