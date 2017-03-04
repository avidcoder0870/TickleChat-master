package com.techpro.chat.ticklechat.activity.registration;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.techpro.chat.ticklechat.LoginDialog;
import com.techpro.chat.ticklechat.R;
import com.techpro.chat.ticklechat.activity.home.HomeActivity;
import com.techpro.chat.ticklechat.listeners.GenericListener;
import com.techpro.chat.ticklechat.models.DataStorage;
import com.techpro.chat.ticklechat.models.user.UserDetailsModel;
import com.techpro.chat.ticklechat.models.user.UserModel;
import com.techpro.chat.ticklechat.rest.ApiClient;
import com.techpro.chat.ticklechat.rest.ApiInterface;
import com.techpro.chat.ticklechat.utils.AppUtils;
import com.techpro.chat.ticklechat.utils.SharedPreferenceUtils;
import com.viewpagerindicator.CirclePageIndicator;

import java.security.MessageDigest;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by vishalrandive on 06/04/16.
 */
public class Login extends AppCompatActivity implements View.OnClickListener {

    private ProgressDialog dialog;
    private static final String TAG = Login.class.getSimpleName();
    private ApiInterface apiService;
    private LoginDialog mStatusUpdateDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager_signup);
        // Add code to print out the key hash
        CirclePageIndicator titlePageIndicator = (CirclePageIndicator) findViewById(R.id.pageIndicator);
        viewPager.setAdapter(new SignupPagerAdapter(getSupportFragmentManager()));
        titlePageIndicator.setViewPager(viewPager);

        apiService = ApiClient.getClient().create(ApiInterface.class);

        findViewById(R.id.btnLogin).setOnClickListener(this);
        findViewById(R.id.btnSignup).setOnClickListener(this);
//        findViewById(R.chatUserID.btnGoogle).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btnLogin:
                mStatusUpdateDialog = new LoginDialog(Login.this, new GenericListener<String>() {
                    @Override
                    public void onResponse(int callerID, String messages) {

                        switch (callerID) {
                            case R.id.tvPositive:
                                if (AppUtils.isNetworkConnectionAvailable(getApplicationContext())) {
                                    Log.e(TAG, "login:==> " + messages);
                                    if (messages != null && !messages.equals("") && messages.contains("~") && !messages.trim().equals("~")
                                            && Login.SHA1(messages.split("~")[1]) != null) {
                                        mStatusUpdateDialog.cancel();
                                        dialog = ProgressDialog.show(Login.this, "Loading", "Please wait...", true);
//                                        callLoginService("8652355351", "2233c15a7f3371fc6e6a8afeb5089b5411db19a1");
                                        callLoginService(messages.split("~")[0], SHA1(messages.split("~")[1]));
                                    } else {
                                        Log.e(TAG, "login:==> " + messages);
                                        Toast.makeText(getApplicationContext(), "Please enter complete details.", Toast.LENGTH_LONG).show();
                                    }
                                } else {
                                    Log.e(TAG, "login:==> " + messages);
                                    Toast.makeText(getApplicationContext(),
                                            getString(R.string.internet_connection_error), Toast.LENGTH_SHORT).show();
                                }
                                break;

                            case R.id.tvNegative:
                                mStatusUpdateDialog.cancel();
                                break;
                        }
                    }
                });

                mStatusUpdateDialog.setTitle("Enter your login details.");
                mStatusUpdateDialog.setPositiveButtonText("OK");
                mStatusUpdateDialog.setNegativeButtonText("CANCEL");
                mStatusUpdateDialog.setCancelable(false);
                mStatusUpdateDialog.show();
                break;
            case R.id.btnSignup:
                Intent mainIntent = new Intent(Login.this, RegistrationActivity.class);
                Login.this.startActivity(mainIntent);
                break;

        }
    }

    class SignupPagerAdapter extends FragmentPagerAdapter {

        private int ITEMS = 8;

        public SignupPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        // Returns the fragment to display for that page
        @Override
        public Fragment getItem(int position) {
            return getFragment(R.layout.fragment_layout_signup, "Page1", position, ITEMS);
        }

        // Returns the page title for the top indicator
        @Override
        public CharSequence getPageTitle(int position) {
            return "Page " + position;
        }

        @Override
        public int getCount() {

            return ITEMS;
        }
    }

    public SignupPagerFragment getFragment(int page, String title, final int position, final int size) {

        SignupPagerFragment fragment = new SignupPagerFragment();
        fragment.addListener(new GenericListener<Boolean>() {
            @Override
            public void onResponse(int callerID, Boolean messages) {
                if (messages != null) {
                    if (messages) {
                        // setSelection(1);
                    }
                }
            }
        });
        Bundle args = new Bundle();
        args.putInt("layout_id", page);
        args.putString("layout_title", title);
        args.putInt("position", position);
        args.putInt("size", size);

        fragment.setArguments(args);

        return fragment;
    }


    private void callLoginService(String username, String pass) {
        //Getting webservice instance which we need to call
        Call<UserModel> callForUserDetailsFromID = apiService.loginUser(username, pass);
        //Calling Webservice by enqueue
        callForUserDetailsFromID.enqueue(new Callback<UserModel>() {

            @Override
            public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                if (response != null && response.body() != null && response.body().getBody() != null && response.body().getMessage().equals("")) {
                    UserDetailsModel getUserDetails = response.body().getBody();
                    DataStorage.UserDetails = getUserDetails;
//                    if (DataStorage.UserDetails.getProfile_image()!=null) {
//                        byte[] decodedString = Base64.decode(DataStorage.UserDetails.getProfile_image(), Base64.DEFAULT);
//                        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
//                        if (decodedByte != null) {
//                            DataStorage.UserDetails.setProfile_image_bitmap(decodedByte);
//                        }
//                    }
                    Gson gson = new Gson();
                    String json = gson.toJson(getUserDetails);
                    Log.e(TAG, "json ==> " + json);
                    DataStorage.myAllUserlist = null;
                    DataStorage.chatUserList = null;
                    DataStorage.mygrouplist = null;

                    SharedPreferenceUtils.setValue(getApplicationContext(), SharedPreferenceUtils.LoginuserDetailsPreference, json);
                    SharedPreferenceUtils.setColleactionObject(getApplicationContext(), SharedPreferenceUtils.myuserlist,
                            DataStorage.myAllUserlist);
                    SharedPreferenceUtils.setColleactionObject(getApplicationContext(), SharedPreferenceUtils.mygrouplist,
                            DataStorage.mygrouplist);
                    SharedPreferenceUtils.setColleactionObject(getApplicationContext(), SharedPreferenceUtils.chatUserID,
                            DataStorage.chatUserList);
                    startActivity(new Intent(Login.this, HomeActivity.class));
                    finish();
                } else {
                    if (response != null && response.body() != null && response.body().getMessage() != null) {
                        Log.e(TAG, "response.body().getMessage() ==> " + response.body().toString());
                        Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_LONG).show();
                    } else if (response != null && response.message() != null) {
                        Log.e(TAG, "response.body().getMessage() ==> " + response.message());
                        Toast.makeText(getApplicationContext(), response.message(), Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(), R.string.failmessage, Toast.LENGTH_LONG).show();
                        Log.e(TAG, "Success but null response");
                    }
                }
                dialog.dismiss();
            }

            @Override
            public void onFailure(Call<UserModel> call, Throwable t) {
                // Log error here since request failed
                Toast.makeText(getApplicationContext(), R.string.failmessage, Toast.LENGTH_LONG).show();
                Log.e(TAG, t.toString());
                dialog.dismiss();
            }
        });
    }


    // For Password
    public static String SHA1(String text) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            md.update(text.getBytes("iso-8859-1"), 0, text.length());
            byte[] sha1hash = md.digest();
            StringBuilder buf = new StringBuilder();
            for (byte b : sha1hash) {
                int halfbyte = (b >>> 4) & 0x0F;
                int two_halfs = 0;
                do {
                    buf.append((0 <= halfbyte) && (halfbyte <= 9) ? (char) ('0' + halfbyte) : (char) ('a' + (halfbyte - 10)));
                    halfbyte = b & 0x0F;
                } while (two_halfs++ < 1);
            }
            return buf.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
