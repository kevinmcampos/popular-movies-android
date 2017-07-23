package me.kevincampos.popularmovies.ui;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.kevincampos.popularmovies.R;
import me.kevincampos.popularmovies.data.Movie;
import me.kevincampos.popularmovies.data.api.DataLoadingCallbacks;
import me.kevincampos.popularmovies.data.api.MoviesDataManager;

public class MovieAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements DataLoadingCallbacks {

    public interface ItemClickListener {
        void onItemClick(Movie movie);
    }

    private Activity hostActivity;
    private final ItemClickListener itemClickListener;
    private final MoviesDataManager moviesDataManager;

    private List<Movie> movies = new ArrayList<>();

    public MovieAdapter(Activity hostActivity, MoviesDataManager moviesDataManager, ItemClickListener itemClickListener) {
        this.hostActivity = hostActivity;
        this.itemClickListener = itemClickListener;
        this.moviesDataManager = moviesDataManager;
        this.moviesDataManager.registerCallback(this);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(hostActivity.getBaseContext())
                .inflate(R.layout.list_item_movie, parent, false);

        return new MovieHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Movie movie = movies.get(position);
        ((MovieHolder) holder).bind(movie);
    }

    @Override
    public int getItemCount() {
        return movies == null ? 0 : movies.size();
    }

    public void addData(List<Movie> newMovies) {
        this.movies.addAll(newMovies);
        notifyDataSetChanged();
    }

    public void clear() {
        this.movies.clear();
        notifyDataSetChanged();
    }

    @Override
    public void dataStartedLoading() {

    }

    @Override
    public void dataFinishedLoading() {

    }

    class MovieHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.movie_container)
        View container;

        @BindView(R.id.movie_poster)
        ImageView poster;

        MovieHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bind(final Movie movie) {
            container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemClickListener.onItemClick(movie);
                }
            });
            Picasso.with(hostActivity.getBaseContext())
                    .load(movie.getPosterURL())
                    .placeholder(R.color.immersive_bars)
                    .into(poster);
            poster.setContentDescription(hostActivity.getString(R.string.movie_poster_content_description, movie.TITLE));
        }
    }
}
