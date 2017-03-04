package com.techpro.chat.ticklechat.activity.registration;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.api.GoogleApiClient;
import com.techpro.chat.ticklechat.R;
import com.techpro.chat.ticklechat.listeners.GenericListener;
import com.techpro.chat.ticklechat.views.GenericTextView;


/**
 * Created by Vishal Randive on 20/18/2015.
 */
public class SignupPagerFragment extends Fragment {
    private String title;
    private int page;
    int position;
    ImageView signup_background_image;
    TextView tvMsg_Signup;
    TextView tvTitle_Msg_Signup;
    TextView tvBottom_Msg_Signup;
    int size = 0;


    GoogleApiClient mGoogleApiClient;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        page = getArguments().getInt("layout_id", 0);
        title = getArguments().getString("layout_title");
        position = getArguments().getInt("position");
        size = getArguments().getInt("size");

//        else position = -1;
    }
    GenericListener<Boolean> objGenericListener;
    public void addListener(GenericListener<Boolean> objGenericListener){
        this.objGenericListener = objGenericListener;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(page, container, false);
        signup_background_image = (ImageView) view.findViewById(R.id.ivAppLogo);
//        ivMomStar = (ImageView) view.findViewById(R.chatUserList.ivMomstarImg);
        tvBottom_Msg_Signup = (TextView) view.findViewById(R.id.tvBottom_Msg_Signup);
        tvTitle_Msg_Signup = (TextView) view.findViewById(R.id.tvTitle_Msg_Signup);
        String topMsg = "";
        String bottomMsg = "";
        int id = R.drawable.logo_circle;


            switch (position) {

                case 0:
                    tvTitle_Msg_Signup.setTextColor(getResources().getColor(R.color.white));
                    id = R.drawable.logo_circle;
                    topMsg = "";
                    bottomMsg = "Welcome to Tickle Chat";

                    break;
                case 1:
                    id = R.drawable.logo_circle;
                    topMsg = "Take a small tour of the \n App before you start";
                    bottomMsg = "Tickle-ing";

                    break;
                case 2:
                    id = R.drawable.logo_circle;
                    topMsg = "Start with clicking on \n Tickle Logo on left top corner for menu button";
                    bottomMsg = "";
                    break;
                case 3:
                    id = R.drawable.logo_circle;
                    topMsg = "Menu has lots to explore";
                    bottomMsg = "";
                    break;
                case 4:
                    id = R.drawable.logo_circle;
                    topMsg = "Tickle your friends already available on tickle. \nTap and Enjoy.";
                    bottomMsg = "";
                    break;
                case 5:
                    id = R.drawable.logo_circle;
                    topMsg = "Create Tickler Groups and enjoy the laughter riot.";
                    bottomMsg = "";
                    break;
                case 6:
                    id = R.drawable.logo_circle;
                    topMsg = "Get Crackling along Random Ticklers with Search Ticklers Feature";
                    bottomMsg = "";
                    break;
                case 7:
                    id = R.drawable.logo_circle;
                    topMsg = "The Name itself says, \nAdd Sentences to the Tickle List and Boast amongst your Friends.";
                    bottomMsg = "";
                    break;
            }

        tvTitle_Msg_Signup.setText(topMsg);
        tvTitle_Msg_Signup.setTextColor(getActivity().getResources().getColor(R.color.white));
        tvBottom_Msg_Signup.setText(bottomMsg);

        if(id!=0)
            signup_background_image.setImageResource(id);
        else
            signup_background_image.setVisibility(View.GONE);


        return view;
    }





}