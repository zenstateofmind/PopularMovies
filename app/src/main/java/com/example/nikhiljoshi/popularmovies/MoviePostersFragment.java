package com.example.nikhiljoshi.popularmovies;

import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;

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

        // figure out a way to grab all the image urls from movie db


        // see if I need to come up with an ImageView for a sample image and create an array
        // adapter for that, similar to the way its done in ArrayAdapter

        // Once that is done, see if you can serve images through the GridView


        GridView gridView = (GridView) rootView.findViewById(R.id.gridview);
        gridView.setAdapter(new ImageAdapter(getContext()));

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getContext(), position + "", Toast.LENGTH_SHORT).show();
            }
        });

        FetchMovieTask task = new FetchMovieTask();
        task.execute("popularity.desc");

        return rootView;
    }

    public class FetchMovieTask extends AsyncTask<String, Void, String[]> {

        private final String LOG_TAG = FetchMovieTask.class.getSimpleName();

        @Override
        protected void onPostExecute(String[] moviesImagePaths) {
            if (moviesImagePaths != null) {
                // put the images in right here
            }
        }

        @Override
        protected String[] doInBackground(String[] params) {

            // param[0] is going to be a preference whether to sort by
            // popularity of ratings

            // get url
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
                final String[] posterPathsFromJson = getPosterPathsFromJson(movieInfoJson);
                Log.i(LOG_TAG, "Images have been collected!: " + Arrays.toString(posterPathsFromJson));
                return posterPathsFromJson;
            } catch (JSONException e) {
                Log.e(LOG_TAG, "Failed in getting image urls", e);
                return null;
            }

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

        private String[] getPosterPathsFromJson(String movieInfoJson) throws JSONException {

            final String MOVIE_RESULT_LIST = "results";
            final String POSTER_PATH = "poster_path";

            JSONObject jsonObject = new JSONObject(movieInfoJson);
            JSONArray moviesInfoArray = jsonObject.getJSONArray(MOVIE_RESULT_LIST);

            String[] posterPathResults = new String[moviesInfoArray.length()];

            for (int i = 0; i < moviesInfoArray.length(); i++) {
                JSONObject movieInfo = moviesInfoArray.getJSONObject(i);
                posterPathResults[i] = movieInfo.getString(POSTER_PATH);
            }

            return posterPathResults;
        }
    }
}
