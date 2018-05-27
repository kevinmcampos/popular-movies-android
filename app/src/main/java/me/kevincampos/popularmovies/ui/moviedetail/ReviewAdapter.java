package me.kevincampos.popularmovies.ui.moviedetail;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import me.kevincampos.popularmovies.data.Review;
import me.kevincampos.popularmovies.databinding.ListItemReviewBinding;

public class ReviewAdapter extends RecyclerView.Adapter {

    private final Context context;

    private List<Review> reviews;

    public ReviewAdapter(Context context) {
        this.context = context;
        this.reviews = new ArrayList<>();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        ListItemReviewBinding listItemReviewBinding =
                ListItemReviewBinding.inflate(layoutInflater, parent, false);
        return new ReviewHolder(listItemReviewBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Review review = getReviewAtPosition(position);
        ((ReviewHolder) holder).bind(review);
    }

    private Review getReviewAtPosition(int position) {
        return reviews.get(position);
    }

    public void swapReviews(List<Review> reviews) {
        this.reviews = reviews;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }

    class ReviewHolder extends RecyclerView.ViewHolder {

        private ListItemReviewBinding binding;

        ReviewHolder(ListItemReviewBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(final Review review) {
            binding.reviewAuthor.setText(review.AUTHOR);
            binding.reviewContent.setText(review.CONTENT);
        }
    }

}
