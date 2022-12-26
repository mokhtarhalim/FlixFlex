package com.halim.flixflex.Movies;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.material.card.MaterialCardView;
import com.halim.flixflex.Movies.Detail.DetailMovieActivity;
import com.halim.flixflex.R;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;

public class MoviesRecyclerAdapter extends RecyclerView.Adapter<MoviesRecyclerAdapter.ViewHolder> {

    private ArrayList<MoviesItem> moviesItem;
    private Context mContext;

    public MoviesRecyclerAdapter(Context mContext, ArrayList<MoviesItem> moviesItem) {
        this.moviesItem = moviesItem;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // inflating our layout file
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_movie, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.posterPicture.setImageResource(R.drawable.bg_white_gray);

        Glide.with(mContext).clear(holder.posterPicture);
        Glide.with(mContext)
                .asBitmap()
                .load(moviesItem.get(position).getPosterPicture())
                .apply(RequestOptions.placeholderOf(R.drawable.bg_white_gray))
                .apply(RequestOptions.centerCropTransform())
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        holder.posterPicture.setImageBitmap(resource);
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {
                    }
                });

        holder.ratingBar.setRating((float) ((moviesItem.get(position).getVoteAverage()) / 2));
        holder.voteAverage.setText(String.valueOf(moviesItem.get(position).getVoteAverage()));
        holder.voteCount.setText(String.valueOf(moviesItem.get(position).getVoteCount()));
        holder.title.setText(moviesItem.get(position).getTitleMovie());

        holder.showCard.setOnClickListener(view -> {
            //Pass id of movie to detail
            Intent intent = new Intent(mContext, DetailMovieActivity.class);
            intent.putExtra("idMovie", moviesItem.get(position).getIdMovie());
            mContext.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        // returning the size of our list.
        return moviesItem.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // creating a variable
        private MaterialCardView showCard;
        private RoundedImageView posterPicture;
        private LinearLayout ratingContainer;
        private RatingBar ratingBar;
        private TextView voteAverage;
        private TextView voteCount;
        private TextView title;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            // initializing our variables.
            showCard = itemView.findViewById(R.id.showCard);
            posterPicture = itemView.findViewById(R.id.posterPicture);
            ratingContainer = itemView.findViewById(R.id.ratingContainer);
            ratingBar = itemView.findViewById(R.id.ratingBar);
            voteAverage = itemView.findViewById(R.id.voteAverage);
            voteCount = itemView.findViewById(R.id.voteCount);
            title = itemView.findViewById(R.id.title);
        }
    }
}
