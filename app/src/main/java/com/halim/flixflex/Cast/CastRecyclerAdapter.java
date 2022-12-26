package com.halim.flixflex.Cast;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.halim.flixflex.R;

import java.util.ArrayList;

public class CastRecyclerAdapter extends RecyclerView.Adapter<CastRecyclerAdapter.ViewHolder> {

    private ArrayList<CastItem> castItems;
    private Context mContext;

    public CastRecyclerAdapter(Context mContext, ArrayList<CastItem> castItems) {
        this.castItems = castItems;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public CastRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // inflating our layout file
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_cast, parent, false);
        return new CastRecyclerAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CastRecyclerAdapter.ViewHolder holder, int position) {

        Glide.with(mContext).clear(holder.profileCast);
        Glide.with(mContext)
                .asBitmap()
                .load(castItems.get(position).getProfilePath())
                .apply(RequestOptions.placeholderOf(R.drawable.bg_white_gray))
                .apply(RequestOptions.circleCropTransform())
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        holder.profileCast.setImageBitmap(resource);
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {
                    }
                });

        holder.nameCast.setText(castItems.get(position).getName());

    }

    @Override
    public int getItemCount() {
        // returning the size of our list.
        return castItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // creating a variable
        private LinearLayout castLayout;
        private ImageView profileCast;
        private TextView nameCast;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            // initializing our variables.
            castLayout = itemView.findViewById(R.id.castLayout);
            profileCast = itemView.findViewById(R.id.profileCast);
            nameCast = itemView.findViewById(R.id.nameCast);
        }
    }
}
