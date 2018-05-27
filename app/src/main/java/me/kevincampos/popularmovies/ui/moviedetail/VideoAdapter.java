package me.kevincampos.popularmovies.ui.moviedetail;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import me.kevincampos.popularmovies.R;
import me.kevincampos.popularmovies.data.YoutubeVideo;
import me.kevincampos.popularmovies.databinding.ListItemVideoBinding;

public class VideoAdapter extends RecyclerView.Adapter {

    private final Context context;
    private final ItemClickListener itemClickListener;

    private List<YoutubeVideo> videos;

    public interface ItemClickListener {
        void onItemClick(YoutubeVideo youtubeVideo);
    }

    public VideoAdapter(Context context, ItemClickListener itemClickListener) {
        this.context = context;
        this.itemClickListener = itemClickListener;
        this.videos = new ArrayList<>();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        ListItemVideoBinding listItemMovieBinding =
                ListItemVideoBinding.inflate(layoutInflater, parent, false);
        return new VideoHolder(listItemMovieBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        YoutubeVideo video = getVideoAtPosition(position);
        ((VideoHolder) holder).bind(video);
    }

    private YoutubeVideo getVideoAtPosition(int position) {
        return videos.get(position);
    }

    public void swapTrailers(List<YoutubeVideo> videos) {
        this.videos = videos;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return videos.size();
    }

    class VideoHolder extends RecyclerView.ViewHolder {

        private ListItemVideoBinding binding;

        VideoHolder(ListItemVideoBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(final YoutubeVideo video) {
            binding.videoContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemClickListener.onItemClick(video);
                }
            });

            binding.videoTitle.setText(video.NAME);

            Picasso.with(context)
                    .load(video.getThumbnailURL())
                    .placeholder(R.color.immersive_bars)
                    .into(binding.videoThumbnail);
            binding.videoThumbnail.setContentDescription(context.getString(R.string.movie_trailer_thumbnail_content_description, video.NAME));
        }
    }

}
