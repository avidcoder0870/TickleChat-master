package com.techpro.chat.ticklechat.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.techpro.chat.ticklechat.R;
import com.techpro.chat.ticklechat.activity.home.TicklersAdapter;

import java.util.ArrayList;

public class SearchTicklerFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_tickle_friend,
                container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.my_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this.getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        ArrayList<String> myDataset =  new ArrayList<String>();
        myDataset.add("ahasfg");
        myDataset.add("aharsf");
        // specify an adapter (see also next example)
        mAdapter = new TicklersAdapter(myDataset);
        mRecyclerView.setAdapter(mAdapter);
        return view;
    }
}
