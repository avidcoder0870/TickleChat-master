package com.techpro.chat.ticklechat.activity.registration;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.techpro.chat.ticklechat.R;
import com.techpro.chat.ticklechat.models.DataStorage;
import com.techpro.chat.ticklechat.models.user.UserDetailsModel;
import com.techpro.chat.ticklechat.models.user.UserModel;
import com.techpro.chat.ticklechat.rest.ApiClient;
import com.techpro.chat.ticklechat.rest.ApiInterface;
import com.techpro.chat.ticklechat.utils.AppUtils;
import com.techpro.chat.ticklechat.utils.UtilityImage;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by vishalrandive on 11/04/16.
 */
public class RegistrationActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView ivProfileIcon;
    private EditText password;
    private EditText confirm_password;
    private EditText profilename;
    private EditText profileemail;
    private EditText profilephone, countrycode;
    private EditText profile_date;
    private TextView tvBtnMale;
    private TextView tvBtnFemale;
    private TextView submit;
    private ProgressDialog dialog;
    private String gender = "m";
    private Bitmap selectedBitmap = null;
    Calendar myCalendar = Calendar.getInstance();
    DatePickerDialog.OnDateSetListener date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
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
                new DatePickerDialog(RegistrationActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    private void updateLabel() {

        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        profile_date.setText(sdf.format(myCalendar.getTime()));
    }

    private void initUi() {
        confirm_password = (EditText) findViewById(R.id.confirm_password);
        password = (EditText) findViewById(R.id.password);
        ivProfileIcon = (ImageView) findViewById(R.id.iv_profile_icon);
        profilename = (EditText) findViewById(R.id.profilename);
        profileemail = (EditText) findViewById(R.id.profileemail);
        profilephone = (EditText) findViewById(R.id.profilephone);
        profile_date = (EditText) findViewById(R.id.profile_date);
        countrycode = (EditText) findViewById(R.id.countrycode);
        tvBtnMale = (TextView) findViewById(R.id.tv_btn_male);
        tvBtnFemale = (TextView) findViewById(R.id.tv_btn_female);
        submit = (TextView) findViewById(R.id.submit);
        submit.setText("Submit");
        tvBtnMale.setSelected(true);
        tvBtnMale.setOnClickListener(this);
        tvBtnFemale.setOnClickListener(this);
        submit.setOnClickListener(this);
        ivProfileIcon.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.submit:
                if (profilename.getText().toString().equals("") || profile_date.getText().toString().equals("") ||
                        profilephone.getText().toString().equals("") || profileemail.getText().toString().equals("") ||
                        confirm_password.getText().toString().equals("") || password.getText().toString().equals("") || countrycode.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "Please enter complete details.", Toast.LENGTH_LONG).show();
                } else if (!confirm_password.getText().toString().equals(password.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "Invalid Password.", Toast.LENGTH_LONG).show();
                } else if (!AppUtils.isNetworkConnectionAvailable(getApplicationContext())) {
                    Toast.makeText(getApplicationContext(),
                            getString(R.string.internet_connection_error), Toast.LENGTH_SHORT).show();
                } else {
                    dialog = ProgressDialog.show(RegistrationActivity.this, "Loading", "Please wait...", true);
                    String profileImage = "";
                    if (selectedBitmap != null) {
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        selectedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                        profileImage = Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);
                    }

                    callUpdateUserDataService(profilename.getText().toString(),
                            gender, String.valueOf(myCalendar.getTimeInMillis()), profilephone.getText().toString(),
                            profileemail.getText().toString(), profileImage, countrycode.getText().toString(), SHA1(password.getText().toString()));
                }
                break;

            case R.id.iv_profile_icon:
                // TODO: 30/10/16

                selectImage();
                break;

            case R.id.tv_btn_male:
                // TODO: 30/10/16
                tvBtnFemale.setSelected(false);
                tvBtnMale.setSelected(true);
                gender = "m";
                break;

            case R.id.tv_btn_female:
                // TODO: 30/10/16
                tvBtnMale.setSelected(false);
                tvBtnFemale.setSelected(true);
                gender = "f";
                break;
        }
    }

    /*
* Get - User details by user chatUserList
* @param userId - user chatUserList
* */
    private synchronized void callUpdateUserDataService(String name, String gender, String dob, String phone, String email, String profile_image, String code, String pass) {
        //Getting webservice instance which we need to call
        Call<UserModel> callForUserDetailsFromID = (ApiClient.getClient()
                .create(ApiInterface.class)).registeruser(name, gender, dob, phone, email, profile_image, code, pass);
        //Calling Webservice by enqueue
        callForUserDetailsFromID.enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                if (response != null && response.body() != null) {
                    UserDetailsModel getUserDetails = response.body().getBody();
                    Toast.makeText(getApplicationContext(), "Registration Successful please login now.", Toast.LENGTH_LONG).show();
                    DataStorage.UserDetails = getUserDetails;
                    finish();
                    Log.e("profile", "Success  callLoginService : " + getUserDetails);
                    Log.e("profile", "Success  getUserDetails.getId() : " + getUserDetails.getId());

                } else {
                    if (response != null && response.body() != null && response.body().getMessage() != null) {
                        Log.e("profile", "response.body().getMessage() ==> " + response.body().toString());
                        Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_LONG).show();
                    } else if (response != null && response.message() != null) {
                        Toast.makeText(getApplicationContext(), response.message(), Toast.LENGTH_LONG).show();
                        Log.e("profile", "response.body().response() ==> " + response.toString());
                        Log.e("profile", "response.body().code() ==> " + response.code());
                        Log.e("profile", "response.body().raw() ==> " + response.raw().message());
                        Log.e("profile", "response.body().body() ==> " + response.raw().body());
                        Log.e("profile", "response.body().errorBody() ==> " + response.errorBody().toString());
                    } else {
                        Toast.makeText(getApplicationContext(), R.string.failmessage, Toast.LENGTH_LONG).show();
                        Log.e("profile", "Success callTickles_Service but null response");
                    }
                }
                dialog.dismiss();
            }

            @Override
            public void onFailure(Call<UserModel> call, Throwable t) {
                // Log error here since request failed
                Toast.makeText(getApplicationContext(), R.string.failmessage, Toast.LENGTH_LONG).show();
                Log.e("profile", t.toString());
                dialog.dismiss();
            }
        });
    }

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

        AlertDialog.Builder builder = new AlertDialog.Builder(RegistrationActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = UtilityImage.checkPermission(RegistrationActivity.this);

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
                bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        selectedBitmap = bm;
        ivProfileIcon.setImageBitmap(bm);
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