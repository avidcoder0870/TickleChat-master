package com.techpro.chat.ticklechat.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.techpro.chat.ticklechat.R;
import com.techpro.chat.ticklechat.models.TickleFriend;

import java.util.List;

public class AddSentenseAdapter extends RecyclerView.Adapter<AddSentenseAdapter.MyViewHolder> {

    private List<TickleFriend> moviesList;
    private Context mContext = null;
    private AddSentenseAdapter.DataUpdated dataupdate = null;

    public AddSentenseAdapter(List<TickleFriend> moviesList, Context context) {
        this.moviesList = moviesList;
        this.mContext = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_add_sentence_row, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        TickleFriend movie = moviesList.get(position);
        holder.mTvTickleText.setText(movie.getName());
        if (movie.getNumber().equals("0"))
            holder.tv_tickle_status.setText("Pending");


        if(position == getItemCount()-1)
            holder.viewSeperator.setVisibility(View.GONE);
        else holder.viewSeperator.setVisibility(View.VISIBLE);
    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView mTvTickleText;
        public TextView tv_tickle_status;
        public View viewSeperator;

        public MyViewHolder(View view) {
            super(view);
            mTvTickleText = (TextView) view.findViewById(R.id.tv_tickle_text);
            tv_tickle_status = (TextView) view.findViewById(R.id.tv_tickle_status);
            viewSeperator= (View) view.findViewById(R.id.view_seperator);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("RecyclerView", "getPosition：" + getPosition());
                    if (mContext != null) {
                        Log.d("RecyclerView", "user.getId()：" + moviesList.get(getPosition()));
                        dataupdate.dataUpdated(moviesList.get(getPosition()).getName());
                    }
                }
            });
        }
    }

    public interface DataUpdated {
        void dataUpdated(String id);
    }

    public void setDataUpdateListener(AddSentenseAdapter.DataUpdated dataupdate) {
        this.dataupdate = dataupdate;
    }
}
