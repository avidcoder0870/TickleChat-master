package com.techpro.chat.ticklechat.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.techpro.chat.ticklechat.R;
import com.techpro.chat.ticklechat.adapters.SubmittedTicklesAdapter;
import com.techpro.chat.ticklechat.models.TickleFriend;

import java.util.ArrayList;
import java.util.List;

public class SubmittedTickles extends Fragment
{
    private View mView;
    private RecyclerView rvSubmittedTickles;
    private SubmittedTicklesAdapter mAdapter;
    private List<TickleFriend> mTempList = new ArrayList<>();
    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        if (mView == null)
        {
            mView = inflater.inflate(R.layout.fragment_submitted_tickles, container, false);
        }
        initUi();
        return mView;
    }

    private void initUi ()
    {
        rvSubmittedTickles = (RecyclerView) mView.findViewById(R.id.rv_submitted_tickles);
        rvSubmittedTickles.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvSubmittedTickles.setHasFixedSize(false);
        mTempList.add(new TickleFriend("Mad Max: Fury Road", "Action & Adventure", "2015", null));
        assignTempData();
        mAdapter = new SubmittedTicklesAdapter(mTempList, getContext());
        rvSubmittedTickles.setAdapter(mAdapter);
    }

    private void assignTempData()
    {
        mTempList.add(new TickleFriend("Mad Max: Fury Road", "Action & Adventure", "2015", null));
        mTempList.add(new TickleFriend("Mad Max: Fury Road", "Action & Adventure", "2015", null));
        mTempList.add(new TickleFriend("Mad Max: Fury Road", "Action & Adventure", "2015", null));
        mTempList.add(new TickleFriend("Mad Max: Fury Road", "Action & Adventure", "2015", null));
    }
}
