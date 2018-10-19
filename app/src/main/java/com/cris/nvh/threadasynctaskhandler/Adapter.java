package com.cris.nvh.threadasynctaskhandler;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {
	private ArrayList<String> mPaths;

	public Adapter(ArrayList<String> paths) {
		mPaths = paths;
	}

	@NonNull
	@Override
	public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		View view = LayoutInflater
				.from(parent.getContext())
				.inflate(R.layout.viewholder, parent, false);
		return new ViewHolder(view);
	}

	@Override
	public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
		holder.setImage(mPaths.get(position));
	}

	@Override
	public int getItemCount() {
		return mPaths != null ? mPaths.size() : 0;
	}

	public class ViewHolder extends RecyclerView.ViewHolder {
		public ImageView mImageView;

		public ViewHolder(View v) {
			super(v);
			mImageView = (ImageView) v.findViewById(R.id.image_view);
		}

		public void setImage(String path) {
			RequestOptions requestOptions = new RequestOptions()
					// place a gif while loading image
					.placeholder(R.drawable.loading)
					// is displayed when error
					.error(R.drawable.ic_launcher_background);
			Glide.with(itemView.getContext())
					.load(path)
					.apply(requestOptions)
					.into(mImageView);
		}
	}
}
