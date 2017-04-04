package ng.com.techdepo.popularmoviestage1;





import android.net.Uri;
import android.os.AsyncTask;

import org.jetbrains.annotations.Contract;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import ng.com.techdepo.popularmoviestage1.parser.Movies;

/**
 * Created by ESIDEM jnr on 4/4/2017.
 */

public class FetchMovieTask extends AsyncTask<String, Void, Movies[]> {

    private final MyCallback movieTaskCallback;

    FetchMovieTask(MyCallback movieTaskCallback) {
        this.movieTaskCallback = movieTaskCallback;
    }

    @Contract("null -> null")
    private Movies[] getMoviesFromJson(String movieJsonString) throws JSONException {
        final String ORIGINAL_TITLE = "original_title";
        final String POSTER_PATH = "poster_path";
        final String OVERVIEW = "overview";
        final String VOTE_AVERAGE = "vote_average";
        final String RELEASE_DATE = "release_date";

        if (movieJsonString == null || "".equals(movieJsonString)) {
            return null;
        }

        JSONObject jsonObjectMovie = new JSONObject(movieJsonString);
        JSONArray jsonArrayMovies = jsonObjectMovie.getJSONArray("results");

        Movies[] movies = new Movies[jsonArrayMovies.length()];

        for (int i = 0; i < jsonArrayMovies.length(); i++) {
            JSONObject object = jsonArrayMovies.getJSONObject(i);
            movies[i] = new Movies(object.getString(ORIGINAL_TITLE),
                    object.getString(POSTER_PATH),
                    object.getString(OVERVIEW),
                    object.getString(VOTE_AVERAGE),
                    object.getString(RELEASE_DATE));
        }
        return movies;

    }


    @Override
    protected Movies[] doInBackground(String... params) {

        if (params.length == 0) {
            return null;
        }

        final String BASE_URL = "https://api.themoviedb.org/3/movie/";
        final String API_KEY = "api_key";

        HttpURLConnection urlConnection = null;
        String movieJsonString = null;
        BufferedReader reader = null;

        Uri uri = Uri.parse(BASE_URL).buildUpon()
                .appendEncodedPath(params[0])
                .appendQueryParameter(API_KEY, BuildConfig.THE_MOVIEDB_API_KEY)
                .build();

        try {
            URL url = new URL(uri.toString());
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            StringBuilder builder = new StringBuilder();
            if (inputStream == null) {
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {

                builder.append(line + "\n");
            }

            if (builder.length() == 0) {
                return null;
            }

            movieJsonString = builder.toString();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }

            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        try {
            return getMoviesFromJson(movieJsonString);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }


    @Override
    protected void onPostExecute(Movies[] movies) {
        movieTaskCallback.updateAdapter(movies);
    }
}
