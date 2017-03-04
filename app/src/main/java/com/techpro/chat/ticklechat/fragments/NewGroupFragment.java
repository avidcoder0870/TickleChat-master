package com.techpro.chat.ticklechat.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.techpro.chat.ticklechat.R;
import com.techpro.chat.ticklechat.activity.home.HomeActivity;
import com.techpro.chat.ticklechat.adapters.AddGroupMembersAdapter;
import com.techpro.chat.ticklechat.models.DataStorage;
import com.techpro.chat.ticklechat.models.Group;
import com.techpro.chat.ticklechat.models.TickleFriend;
import com.techpro.chat.ticklechat.models.message.AllMessages;
import com.techpro.chat.ticklechat.models.message.CreateGroup;
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
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewGroupFragment extends Fragment {
    private Button createGroup;
    private Activity mActivity = null;
    private AddGroupMembersAdapter mAdapter;
    private ImageView groupIcon;
    private List<TickleFriend> movieList = new ArrayList<>();
    private EditText groupname;
    private RecyclerView mRecyclerView;
    private Group grp = null;
    private ProgressDialog dialog;
    public static List<Integer> addedUser = new ArrayList<Integer>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        grp = new Group();
        View view = inflater.inflate(R.layout.activity_create_group,
                container, false);
        createGroup = (Button) view.findViewById(R.id.creategroup);
        groupname = (EditText) view.findViewById(R.id.groupname);
        groupIcon = (ImageView) view.findViewById(R.id.groupIcon);
        mActivity = getActivity();
        groupIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });

        createGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!AppUtils.isNetworkConnectionAvailable(NewGroupFragment.this.getContext())) {
                    Toast.makeText(NewGroupFragment.this.getContext(),
                            getString(R.string.internet_connection_error), Toast.LENGTH_SHORT).show();
                    return;
                }
                if (groupname.getText().toString().equals("")) {
                    Toast.makeText(NewGroupFragment.this.getContext(),
                            "Please enter Group Name", Toast.LENGTH_SHORT).show();
                } else {
                    dialog = ProgressDialog.show(NewGroupFragment.this.getActivity(), "Loading", "Please wait...", true);
                    String profileImage = "";
                    if (selectedBitmap != null) {
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        selectedBitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
                        profileImage = Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);
                    }
                    callCreateGroupService(groupname.getText().toString(), profileImage, DataStorage.UserDetails.getId(), DataStorage.UserDetails.getId());
                }
            }
        });
        return view;
    }

    /*
* Get - User details by user chatUserList
* @param userId - user chatUserList
* */
    private void callCreateGroupService(String name, String image, String created_by, String admin) {
        //Getting webservice instance which we need to call
        Call<CreateGroup> callForUserDetailsFromID = ApiClient.createServiceWithAuth(DataStorage.UserDetails.getId()).
                create(ApiInterface.class).postNewGroup(name, image, created_by, admin);
        //Calling Webservice by enqueue
        callForUserDetailsFromID.enqueue(new Callback<CreateGroup>() {
            @Override
            public void onResponse(Call<CreateGroup> call, Response<CreateGroup> response) {
                if (response != null && response.body() != null && response.body().getStatus().equals("success")) {
                    grp.setId(response.body().getBody().getId());
                    grp.setAdmin(response.body().getBody().getId());
                    grp.setCreated_at(response.body().getBody().getCreated_at());
                    grp.setCreated_by(response.body().getBody().getCreated_by());
                    grp.setGroup_image(response.body().getBody().getGroup_image());
                    grp.setImage(response.body().getBody().getImage());
                    grp.setName(response.body().getBody().getName());
                    grp.setUpdated_at(response.body().getBody().getUpdated_at());
                    dialog.dismiss();

                    final Dialog dialog = new Dialog(mActivity);
                    // Include dialog.xml file
                    dialog.setContentView(R.layout.dialog_add_group_members);
                    // Set dialog title
                    dialog.setTitle("Select Group Members");

                    // set values for custom dialog components - text, image and button
                    mRecyclerView = (RecyclerView) dialog.findViewById(R.id.my_recycler_view);
                    (dialog.findViewById(R.id.buttonok)).
                            setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Log.e("Create group", "ssssssssssssss == " + addedUser);
                                    if (!AppUtils.isNetworkConnectionAvailable(NewGroupFragment.this.getContext())) {
                                        Toast.makeText(NewGroupFragment.this.getContext(),
                                                getString(R.string.internet_connection_error), Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                    dialog.dismiss();
                                    NewGroupFragment.this.dialog = ProgressDialog.show(NewGroupFragment.this.getActivity(), "Loading", "Please wait...", true);
                                    callAddMembersService(addedUser, Integer.parseInt(grp.getId()));

                                }
                            });
                    NewGroupFragment.this.dialog.dismiss();
                    (dialog.findViewById(R.id.buttoncancle)).
                            setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    dialog.dismiss();
                                }
                            });
                    addedUser.clear();
                    mAdapter = new AddGroupMembersAdapter(DataStorage.chatUserList, mActivity.getApplicationContext(), true, true);
                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
                    mRecyclerView.setLayoutManager(mLayoutManager);
                    mRecyclerView.setItemAnimator(new DefaultItemAnimator());
                    mRecyclerView.setAdapter(mAdapter);
                    dialog.show();

                } else {
                    Toast.makeText(NewGroupFragment.this.getContext(), R.string.failmessage, Toast.LENGTH_LONG).show();
                    Log.e("SendMessage", "Success callMessage_ALL_Service but null response");
                }
            }

            @Override
            public void onFailure(Call<CreateGroup> call, Throwable t) {
                // Log error here since request failed
                Toast.makeText(NewGroupFragment.this.getContext(), R.string.failmessage, Toast.LENGTH_LONG).show();
                Log.e("SendMessage", t.toString());
                dialog.dismiss();
            }
        });

    }

// TODO: 06/11/16 API NOT WORKING


    /*
* Get - User details by user chatUserList
* @param userId - user chatUserList
* */
    private void callAddMembersService(final List<Integer> members, int groupId) {
        //Getting webservice instance which we need to call
        String idList = members.toString();
        final String csv = idList.substring(1, idList.length() - 1).replace(", ", ",");
        Log.e("===============>", "groupId ==> " + groupId);
        Log.e("===============>", "csv.toString() ==> " + csv);
//        csv = "{\"members\"}:\""+csv+"\"";
        Call<JsonObject> callForUserDetailsFromID = ApiClient.createServiceWithAuth(DataStorage.UserDetails.getId()).
                create(ApiInterface.class).postGroupMembers(groupId, csv);
        //Calling Webservice by enqueue
        callForUserDetailsFromID.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response != null && response.body() != null) {
                    Log.e("SendMessage", "response.message(: " + response.message());
                    Log.e("SendMessage", "response.code(: " + response.code());
                    Log.e("SendMessage", "response.code(: " + response.body());
                    grp.setMembers(csv);
                    DataStorage.mygrouplist.add(grp);
                    SharedPreferenceUtils.setColleactionObject(getContext(), grp.getId(), new ArrayList<AllMessages.MessageList.ChatMessagesList>());
                    SharedPreferenceUtils.setColleactionObject(getContext(), SharedPreferenceUtils.mygrouplist,
                            DataStorage.mygrouplist);
                    getActivity().getSupportFragmentManager().popBackStack();
                    Toast.makeText(NewGroupFragment.this.getContext(), "Group created Succesfully.", Toast.LENGTH_LONG).show();
//                    if (response.body() != null && response.body().getStatus().equals("success")) {
//                        grp.setMembers(members.toString().replace("[","").replace("]",""));
//                        DataStorage.mygrouplist.add(grp);
//                        SharedPreferenceUtils.setColleactionObject(mActivity.getApplicationContext(),
//                                SharedPreferenceUtils.mygrouplist,DataStorage.mygrouplist);
//                    }
                } else {
                    Toast.makeText(NewGroupFragment.this.getContext(), R.string.failmessage, Toast.LENGTH_LONG).show();
                    Log.e("SendMessage", "Success callMessage_ALL_Service but null response");
                }
                NewGroupFragment.this.dialog.dismiss();
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                // Log error here since request failed
                Toast.makeText(NewGroupFragment.this.getContext(), R.string.failmessage, Toast.LENGTH_LONG).show();
                Log.e("SendMessage", t.toString());
                NewGroupFragment.this.dialog.dismiss();
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

        AlertDialog.Builder builder = new AlertDialog.Builder(NewGroupFragment.this.getActivity());
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = UtilityImage.checkPermission(NewGroupFragment.this.getActivity());

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
        groupIcon.setImageBitmap(thumbnail);
    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {

        Bitmap bm = null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(NewGroupFragment.this.getContext().getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        selectedBitmap = bm;
        groupIcon.setImageBitmap(bm);
    }

}
