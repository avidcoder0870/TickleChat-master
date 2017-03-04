package com.techpro.chat.ticklechat.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mikhaellopez.circularimageview.CircularImageView;
import com.techpro.chat.ticklechat.R;
import com.techpro.chat.ticklechat.models.TickleFriend;

import java.util.List;

public class SubmittedTicklesAdapter extends RecyclerView.Adapter<SubmittedTicklesAdapter.MyViewHolder>{

    private List<TickleFriend> moviesList;
    private Context mContext = null;

    public SubmittedTicklesAdapter (List<TickleFriend> moviesList, Context context) {
        this.moviesList = moviesList;
        this.mContext = context;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_submitted_tickles_row, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        TickleFriend movie = moviesList.get(position);
        holder.mTvTickleText.setText(movie.getName());
    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView mTvTickleText;
        public CircularImageView mIvTickleImage;

        public MyViewHolder(View view) {
            super(view);
            mTvTickleText = (TextView) view.findViewById(R.id.tv_tickle_text);
            mIvTickleImage = (CircularImageView) view.findViewById(R.id.iv_tickle_image);
        }
    }
}
