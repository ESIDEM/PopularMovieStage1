package ng.com.techdepo.popularmoviestage1;


import android.content.Context;
import android.graphics.Movie;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

import ng.com.techdepo.popularmoviestage1.parser.Movies;


/**
 * Created by ESIDEM jnr on 4/4/2017.
 */

public class MovieAdapter extends BaseAdapter{


    private final static String BASE_POSTER_URL = "https://image.tmdb.org/t/p/";
    private final static String IMAGE_SIZE = "w500";
    private final Context context;
    private final List<Movies> movies;

    MovieAdapter(Context context, List<Movies> movies) {
        this.context = context;
        this.movies = movies;
    }

    @Override
    public int getCount() {
        return movies.size();
    }

    @Override
    public Movies getItem(int position) {
        return movies.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressWarnings("StringBufferReplaceableByString")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Movies movie = getItem(position);
        ImageView imageView;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) parent.getContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            imageView = (ImageView) inflater.inflate(R.layout.movie_item, parent, false);
        } else {
            imageView = (ImageView) convertView;
        }

        String url = new StringBuilder()
                .append(BASE_POSTER_URL)
                .append(IMAGE_SIZE)
                .append(movie.getPosterPath().trim()).toString();

        Picasso.with(context)
                .load(url)
                .placeholder(R.mipmap.ic_launcher_round)
                .error(R.mipmap.ic_launcher)
                .into(imageView);
        return imageView;
    }

    void clear() {
        if (movies.size() > 0) {
            movies.clear();
        }
    }


}
