package me.kevincampos.popularmovies.ui.home;

import android.app.Activity;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import me.kevincampos.popularmovies.R;
import me.kevincampos.popularmovies.data.Movie;
import me.kevincampos.popularmovies.databinding.ListItemMovieBinding;

public class MovieAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public interface ItemClickListener {
        void onItemClick(Movie movie);
    }

    private Activity hostActivity;
    private final ItemClickListener itemClickListener;

    private Cursor cursor;
    private List<Movie> movies = new ArrayList<>();

    public MovieAdapter(Activity hostActivity, ItemClickListener itemClickListener) {
        this.hostActivity = hostActivity;
        this.itemClickListener = itemClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(hostActivity.getBaseContext());
        ListItemMovieBinding listItemMovieBinding =
                ListItemMovieBinding.inflate(layoutInflater, parent, false);
        return new MovieHolder(listItemMovieBinding);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Movie movie = getMovieAtPosition(position);
        ((MovieHolder) holder).bind(movie);
    }

    @Override
    public int getItemCount() {
        if (cursor == null) {
            return movies == null ? 0 : movies.size();
        } else {
            return cursor.getCount();
        }
    }

    private Movie getMovieAtPosition(int position) {
        if (cursor == null) {
            return movies.get(position);
        } else {
            cursor.moveToPosition(position);
            return new Movie(cursor);
        }
    }

    class MovieHolder extends RecyclerView.ViewHolder {

        private ListItemMovieBinding binding;

        MovieHolder(ListItemMovieBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(final Movie movie) {
            binding.movieContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemClickListener.onItemClick(movie);
                }
            });
            Picasso.with(hostActivity.getBaseContext())
                    .load(movie.getPosterURL())
                    .placeholder(R.color.immersive_bars)
                    .into(binding.moviePoster);
            binding.moviePoster.setContentDescription(hostActivity.getString(R.string.movie_poster_content_description, movie.TITLE));
        }
    }

    /* Array methods */

    public void addData(List<Movie> newMovies) {
        this.movies.addAll(newMovies);
        notifyDataSetChanged();
    }

    public void clear() {
        this.movies.clear();
        notifyDataSetChanged();
    }

    /* Cursor methods */

    public void swapCursor(Cursor cursor) {
        if (this.cursor != null) {
            this.cursor.close();
        }
        this.cursor = cursor;
        notifyDataSetChanged();
    }

}
