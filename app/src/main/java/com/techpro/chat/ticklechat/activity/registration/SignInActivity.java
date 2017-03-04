package com.techpro.chat.ticklechat.activity.registration;

import android.app.ProgressDialog;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.techpro.chat.ticklechat.LoginDialog;
import com.techpro.chat.ticklechat.R;
import com.techpro.chat.ticklechat.activity.home.HomeActivity;
import com.techpro.chat.ticklechat.databinding.ActivitySigninBinding;
import com.techpro.chat.ticklechat.listeners.GenericListener;
import com.techpro.chat.ticklechat.models.DataStorage;
import com.techpro.chat.ticklechat.models.user.UserDetailsModel;
import com.techpro.chat.ticklechat.models.user.UserModel;
import com.techpro.chat.ticklechat.rest.ApiClient;
import com.techpro.chat.ticklechat.rest.ApiInterface;
import com.techpro.chat.ticklechat.utils.AppUtils;
import com.techpro.chat.ticklechat.utils.SharedPreferenceUtils;

import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by vishalrandive on 09/11/16.
 */

public class SignInActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, View.OnClickListener {

    private static final String TAG = "SignInActivity";
    private static final int RC_SIGN_IN = 9001;

    private GoogleApiClient mGoogleApiClient;
    private ProgressDialog mProgressDialog;

    private ActivitySigninBinding mBinding;
    private LoginDialog mLoginDialog;

    UserDetailsModel user = new UserDetailsModel();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(SignInActivity.this, R.layout.activity_signin);
        // Button listeners
//        mBinding.fbLogin.setOnClickListener(this);
        mBinding.btnFbSignin.setOnClickListener(this);
        mBinding.signInButton.setOnClickListener(this);
        mBinding.btnSignin.setOnClickListener(this);
        mBinding.btnSignup.setOnClickListener(this);
        mBinding.btnGoogleSignin.setOnClickListener(this);

        // [START configure_signin]
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        // [END configure_signin]

        // [START build_client]
        // Build a GoogleApiClient with access to the Google Sign-In API and the
        // options specified by gso.
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        // [END build_client]

        // [START customize_button]
        // Customize sign-in button. The sign-in button can be displayed in
        // multiple sizes and color schemes. It can also be contextually
        // rendered based on the requested scopes. For example. a red button may
        // be displayed when Google+ scopes are requested, but a white button
        // may be displayed when only basic profile is requested. Try adding the
        // Scopes.PLUS_LOGIN scope to the GoogleSignInOptions to see the
        // difference.

        mBinding.signInButton.setSize(SignInButton.SIZE_STANDARD);
        mBinding.signInButton.setScopes(gso.getScopeArray());
        // [END customize_button]
        initFBLogin();
    }

    CallbackManager callbackManager;

    void initFBLogin() {
        mBinding.fbLogin.setReadPermissions("email");
        // If using in a fragmehnt
//        mBinding.loginButton.setFragment(this);
        // Other app specific specialization
        callbackManager = CallbackManager.Factory.create();

    }

    void clickedOnFbSignIn() {
        // Callback registration
        mBinding.fbLogin.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
                showSignIn();
            }

            @Override
            public void onCancel() {
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
            }
        });
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("email", "public_profile", "user_birthday", "user_friends"));
    }

    void clickedOnGoogleSignin() {

        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
        if (opr.isDone()) {
            // If the user's cached credentials are valid, the OptionalPendingResult will be "done"
            // and the GoogleSignInResult will be available instantly.
            Log.d(TAG, "Got cached sign-in");
            GoogleSignInResult result = opr.get();
            handleSignInResult(result);
        } else {
            // If the user has not previously signed in on this device or the sign-in has expired,
            // this asynchronous branch will attempt to sign in the user silently.  Cross-device
            // single sign-on will occur in this branch.
            showProgressDialog();
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(GoogleSignInResult googleSignInResult) {
                    hideProgressDialog();
                    handleSignInResult(googleSignInResult);
                }
            });
        }

    }


    @Override
    public void onStart() {
        super.onStart();
    }

    // [START onActivityResult]
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
            showSignIn();
        } else {
            callbackManager.onActivityResult(requestCode, resultCode, data);
            showSignIn();
        }
    }
    // [END onActivityResult]

    // [START handleSignInResult]
    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            user.setName(acct.getDisplayName());
            user.setEmail(acct.getEmail());
            user.setProfile_image((acct.getPhotoUrl() != null) ? acct.getPhotoUrl().getPath() : "");

            //mStatusTextView.setText(getString(R.string.signed_in_fmt, acct.getDisplayName()));
            updateUI(true);
        } else {
            // Signed out, show unauthenticated UI.
            updateUI(false);
        }
    }
    // [END handleSignInResult]

    // [START signIn]
    private void clickedOnGoogleSignIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    // [END signIn]

    // [START signOut]
    private void signOut() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        // [START_EXCLUDE]
                        updateUI(false);
                        // [END_EXCLUDE]
                    }
                });
    }
    // [END signOut]

    // [START revokeAccess]
    private void revokeAccess() {
        Auth.GoogleSignInApi.revokeAccess(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        // [START_EXCLUDE]
                        updateUI(false);
                        // [END_EXCLUDE]
                    }
                });
    }
    // [END revokeAccess]

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
    }

    private void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(getString(R.string.loading));
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.hide();
        }
    }

    private void updateUI(boolean signedIn) {

        // TODO: 09/11/16 RESULT SUCCESS - boolean true Start with home activity

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_in_button:
                clickedOnGoogleSignIn();
                break;
            case R.id.btn_signin:
                showSignIn();
                break;
            case R.id.btn_signup:
                Intent mainIntent = new Intent(SignInActivity.this, RegistrationActivity.class);
                startActivity(mainIntent);
                break;
            case R.id.btn_fb_signin:
                clickedOnFbSignIn();
                break;
            case R.id.btn_google_signin:
                clickedOnGoogleSignin();
                break;
        }
    }


    void showSignIn() {
        mLoginDialog = new LoginDialog(SignInActivity.this, new GenericListener<String>() {
            @Override
            public void onResponse(int callerID, String messages) {

                switch (callerID) {
                    case R.id.tvPositive:
                        if (AppUtils.isNetworkConnectionAvailable(getApplicationContext())) {
                            Log.e(TAG, "login:==> " + messages);
                            if (messages != null && !messages.equals("") && messages.contains("~") && !messages.trim().equals("~")
                                    && Login.SHA1(messages.split("~")[1]) != null) {
                                mLoginDialog.cancel();
                                mProgressDialog = ProgressDialog.show(SignInActivity.this, "Loading", "Please wait...", true);
//                                callLoginService("8652355351", "2233c15a7f3371fc6e6a8afeb5089b5411db19a1");
                                AppUtils.showLog("2233c15a7f3371fc6e6a8afeb5089b5411db19a1");
                                AppUtils.showLog("" + Login.SHA1(messages.split("~")[1]));
                                callLoginService(messages.split("~")[0], Login.SHA1(messages.split("~")[1]));
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
                        mLoginDialog.cancel();
                        break;
                }
            }
        });

        mLoginDialog.setTitle("Enter your login details.");
        mLoginDialog.setPositiveButtonText("OK");
        mLoginDialog.setNegativeButtonText("CANCEL");
        mLoginDialog.setCancelable(false);
        mLoginDialog.show();
    }

    private void callLoginService(String username, String pass) {
        //Getting webservice instance which we need to call
        Call<UserModel> callForUserDetailsFromID = ApiClient.getClient().create(ApiInterface.class).loginUser(username, pass);
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
                    startActivity(new Intent(SignInActivity.this, HomeActivity.class));
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
                mProgressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<UserModel> call, Throwable t) {
                // Log error here since request failed
                Toast.makeText(getApplicationContext(), R.string.failmessage, Toast.LENGTH_LONG).show();
                Log.e(TAG, t.toString());
                mProgressDialog.dismiss();
            }
        });
    }


    // TODO: 14/11/16 SAGAR PLZ add
    private void callSignupService(String username, String pass) {
        //Getting webservice instance which we need to call
        Call<JsonObject> callForUserDetailsFromID = ApiClient.getClient().create(ApiInterface.class).signInUsingSocialSdk(user.getName(), user.getGender(), user.getDob(), user.getPhone(), user.getEmail(), user.getProfile_image(), user.getCountry_code(), user.getPassword(), "usertype");
        //Calling Webservice by enqueue
        callForUserDetailsFromID.enqueue(new Callback<JsonObject>() {

            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response != null && response.body() != null && response.body() != null) {

                    JsonObject jsonObject = response.body();
                    Log.e(TAG, "json ==> " + jsonObject);

                    Gson gson = new Gson();
                    UserDetailsModel model = gson.fromJson(jsonObject, UserDetailsModel.class);

                    DataStorage.UserDetails = model;
                    SharedPreferenceUtils.setValue(getApplicationContext(), SharedPreferenceUtils.LoginuserDetailsPreference, jsonObject.toString());
                    startActivity(new Intent(SignInActivity.this, HomeActivity.class));
                    finish();
                } else {
//                    if (response != null && response.body() != null  && response.body().getMessage() != null){
//                        Log.e(TAG, "response.body().getMessage() ==> "+response.body().toString());
//                        Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_LONG).show();
//                    } else {
//                        Toast.makeText(getApplicationContext(), R.string.failmessage, Toast.LENGTH_LONG).show();
                    Log.e(TAG, "Success but null response");
//                    }
                }
                mProgressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                // Log error here since request failed
                Toast.makeText(getApplicationContext(), R.string.failmessage, Toast.LENGTH_LONG).show();
                Log.e(TAG, t.toString());
                mProgressDialog.dismiss();
            }
        });
    }
}