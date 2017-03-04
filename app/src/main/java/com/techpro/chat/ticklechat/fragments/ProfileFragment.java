package com.techpro.chat.ticklechat.fragments;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.techpro.chat.ticklechat.R;
import com.techpro.chat.ticklechat.models.DataStorage;
import com.techpro.chat.ticklechat.models.user.UserDetailsModel;
import com.techpro.chat.ticklechat.models.user.UserModel;
import com.techpro.chat.ticklechat.rest.ApiClient;
import com.techpro.chat.ticklechat.rest.ApiInterface;
import com.techpro.chat.ticklechat.utils.AppUtils;
import com.techpro.chat.ticklechat.utils.SharedPreferenceUtils;
import com.techpro.chat.ticklechat.utils.UtilityImage;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileFragment extends Fragment implements View.OnClickListener {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private ImageView ivProfileIcon;
    private EditText profilename;
    private EditText profileemail;
    private EditText profilephone;
    private EditText profile_date;
    private TextView tvBtnMale;
    private TextView tvBtnFemale;
    private TextView submit;
    private ProgressDialog dialog;
    private View view;
    private String gender = "m";
    Calendar myCalendar = Calendar.getInstance();
    DatePickerDialog.OnDateSetListener date;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.activity_profile, container, false);
        }
        initUi();

        date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };

        profile_date.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(ProfileFragment.this.getActivity(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        return view;
    }

    private void updateLabel() {

        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        profile_date.setText(sdf.format(myCalendar.getTime()));
    }

    private void initUi() {
        ivProfileIcon = (ImageView) view.findViewById(R.id.iv_profile_icon);
        profilename = (EditText) view.findViewById(R.id.profilename);
        profileemail = (EditText) view.findViewById(R.id.profileemail);
        profilephone = (EditText) view.findViewById(R.id.profilephone);
        profile_date = (EditText) view.findViewById(R.id.profile_date);
        tvBtnMale = (TextView) view.findViewById(R.id.tv_btn_male);
        tvBtnFemale = (TextView) view.findViewById(R.id.tv_btn_female);
        submit = (TextView) view.findViewById(R.id.submit);

        tvBtnMale.setSelected(true);
        tvBtnMale.setOnClickListener(this);
        tvBtnFemale.setOnClickListener(this);
        submit.setOnClickListener(this);
        ivProfileIcon.setOnClickListener(this);
        if (DataStorage.UserDetails.getProfile_image() != null) {
            byte[] decodedString = Base64.decode(DataStorage.UserDetails.getProfile_image().getBytes(), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            if (decodedByte != null)
                ivProfileIcon.setImageBitmap(decodedByte);
        }
        profilename.setText(DataStorage.UserDetails.getName());
        profileemail.setText(DataStorage.UserDetails.getEmail());
        profilephone.setText(DataStorage.UserDetails.getPhone());
        profile_date.setText(DataStorage.UserDetails.getBirthday());
        gender = DataStorage.UserDetails.getGender();
        try {
            if (DataStorage.UserDetails.getGender().equals("m")) {
                tvBtnFemale.setSelected(false);
                tvBtnMale.setSelected(true);
            } else {
                tvBtnMale.setSelected(false);
                tvBtnFemale.setSelected(true);
            }
        }catch (Exception e){
            e.printStackTrace();
            tvBtnFemale.setSelected(false);
            tvBtnMale.setSelected(true);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.submit:
                if (!AppUtils.isNetworkConnectionAvailable(ProfileFragment.this.getContext())) {
                    Toast.makeText(ProfileFragment.this.getContext(),
                            getString(R.string.internet_connection_error), Toast.LENGTH_SHORT).show();
                    return;
                }

                if (profilename.getText().toString().equals("") || profile_date.getText().toString().equals("") ||
                        profilephone.getText().toString().equals("") || profileemail.getText().toString().equals("")) {
                    Toast.makeText(ProfileFragment.this.getActivity(), "Please enter complete details.", Toast.LENGTH_LONG).show();
                } else {
                    dialog = ProgressDialog.show(ProfileFragment.this.getActivity(), "Loading", "Please wait...", true);
                    String profileImage = "";
                    if (selectedBitmap != null) {
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        selectedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                        profileImage = Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);
                    }
                    callUpdateUserDataService(Integer.parseInt(DataStorage.UserDetails.getId()), profilename.getText().toString(), gender, profile_date.getText().toString(),
                            profilephone.getText().toString(), profileemail.getText().toString(), profileImage);
                }
                break;

            case R.id.iv_profile_icon:
                selectImage();
                break;

            case R.id.tv_btn_male:
                // TODO: 30/10/16
                gender = "m";
                tvBtnFemale.setSelected(false);
                tvBtnMale.setSelected(true);
                break;

            case R.id.tv_btn_female:
                // TODO: 30/10/16
                gender = "f";
                tvBtnMale.setSelected(false);
                tvBtnFemale.setSelected(true);
                break;
        }
    }

    /*
* Get - User details by user chatUserList
* @param userId - user chatUserList
* */
    private synchronized void callUpdateUserDataService(int userid, String name, String gender, String dob, String phone, String email, String profile_image) {
        //Getting webservice instance which we need to call
        Call<UserModel> callForUserDetailsFromID = (ApiClient.createServiceWithAuth(DataStorage.UserDetails.getId())
                .create(ApiInterface.class)).callUpdateUserDataService(userid, name, gender, dob, phone, email, profile_image);
        //Calling Webservice by enqueue
        callForUserDetailsFromID.enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                if (response != null && response.body() != null) {
                    UserDetailsModel getUserDetails = response.body().getBody();
                    DataStorage.UserDetails = getUserDetails;
                    Gson gson = new Gson();
                    String json = gson.toJson(getUserDetails);
                    Log.e("ProfileFragment", "json ==> " + json);
                    SharedPreferenceUtils.setValue(getContext(), SharedPreferenceUtils.LoginuserDetailsPreference, json);
                    getActivity().getSupportFragmentManager().popBackStack();
                    Toast.makeText(ProfileFragment.this.getContext(), "Profile Updated Successfully.", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(ProfileFragment.this.getContext(), R.string.failmessage, Toast.LENGTH_LONG).show();
                    Log.e("profile", "Success callTickles_Service but null response");
                }
                dialog.dismiss();
            }

            @Override
            public void onFailure(Call<UserModel> call, Throwable t) {
                // Log error here since request failed
                Toast.makeText(ProfileFragment.this.getContext(), R.string.failmessage, Toast.LENGTH_LONG).show();
                Log.e("profile", t.toString());
                dialog.dismiss();
            }
        });

    }


    private Bitmap selectedBitmap = null;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private String userChoosenTask;

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case UtilityImage.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (userChoosenTask.equals("Take Photo"))
                        cameraIntent();
                    else if (userChoosenTask.equals("Choose from Library"))
                        galleryIntent();
                } else {
                    //code for deny
                }
                break;
        }
    }

    private void selectImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Library",
                "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(ProfileFragment.this.getActivity());
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = UtilityImage.checkPermission(ProfileFragment.this.getActivity());

                if (items[item].equals("Take Photo")) {
                    userChoosenTask = "Take Photo";
                    if (result)
                        cameraIntent();

                } else if (items[item].equals("Choose from Library")) {
                    userChoosenTask = "Choose from Library";
                    if (result)
                        galleryIntent();

                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
    }

    private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
        }
    }

    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 100, bytes);

        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");

        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        selectedBitmap = thumbnail;
        ivProfileIcon.setImageBitmap(thumbnail);
    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {

        Bitmap bm = null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        selectedBitmap = bm;
        ivProfileIcon.setImageBitmap(bm);
    }

}
