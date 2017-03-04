package com.techpro.chat.ticklechat.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.techpro.chat.ticklechat.R;
import com.techpro.chat.ticklechat.utils.AppUtils;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by vishalrandive on 20/04/16.
 */
public class TempFragment extends BaseFragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_temp, container, false);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        getActivity().setTitle(getString(R.string.user_not_verified));
    }

    @OnClick(R.id.btnBack)
    void onClickContactUs() {
        AppUtils.call(AppUtils.BABYCHAKRA_CONTACT, getActivity());
    }
}
