package com.halim.flixflex.Series;

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
import com.halim.flixflex.R;
import com.halim.flixflex.Series.Detail.DetailSerieActivity;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;

public class SeriesRecyclerAdapter extends RecyclerView.Adapter<SeriesRecyclerAdapter.ViewHolder> {

    private ArrayList<SeriesItem> seriesItem;
    private Context mContext;

    public SeriesRecyclerAdapter(Context mContext, ArrayList<SeriesItem> seriesItem) {
        this.seriesItem = seriesItem;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public SeriesRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // inflating our layout file
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_movie, parent, false);
        return new SeriesRecyclerAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SeriesRecyclerAdapter.ViewHolder holder, int position) {

        holder.posterPicture.setImageResource(R.drawable.bg_white_gray);

        Glide.with(mContext).clear(holder.posterPicture);
        Glide.with(mContext)
                .asBitmap()
                .load(seriesItem.get(position).getPosterPicture())
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

        holder.ratingBar.setRating((float) ((seriesItem.get(position).getVoteAverage()) / 2));
        holder.voteAverage.setText(String.valueOf(seriesItem.get(position).getVoteAverage()));
        holder.voteCount.setText(String.valueOf(seriesItem.get(position).getVoteCount()));
        holder.title.setText(seriesItem.get(position).getTitleSerie());

        holder.showCard.setOnClickListener(view -> {
            //Pass id of serie to detail
            Intent intent = new Intent(mContext, DetailSerieActivity.class);
            intent.putExtra("idSerie", seriesItem.get(position).getIdSerie());
            mContext.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        // returning the size of our list.
        return seriesItem.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // creating a variable
        private MaterialCardView showCard;
        private RoundedImageView posterPicture;
        private RatingBar ratingBar;
        private TextView voteAverage;
        private TextView voteCount;
        private TextView title;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            // initializing our variables.
            showCard = itemView.findViewById(R.id.showCard);
            posterPicture = itemView.findViewById(R.id.posterPicture);
            ratingBar = itemView.findViewById(R.id.ratingBar);
            voteAverage = itemView.findViewById(R.id.voteAverage);
            voteCount = itemView.findViewById(R.id.voteCount);
            title = itemView.findViewById(R.id.title);
        }
    }

}
