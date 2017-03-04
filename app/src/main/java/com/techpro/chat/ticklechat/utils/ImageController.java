package com.techpro.chat.ticklechat.utils;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;


import com.techpro.chat.ticklechat.listeners.GenericListener;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.lang.ref.WeakReference;


/**
 * Created by Administrator on 1/12/2016.
 */


public class ImageController {

//  public static final int PICK_IMAGE_REQUEST = 1;
    public static final int PICK_IMAGE = 1;
    public static final int CAMERA_CAPTURE_IMAGE = 2;
    private WeakReference<Activity> objWeakReference;
    private GenericListener<Object> objGenericListener;

    private int opponentId;
    private int userId;


    public ImageController(WeakReference<Activity> objWeakReference, GenericListener<Object> objGenericListener, int opponentId, int userId) {
        this.objWeakReference = objWeakReference;
        this.objGenericListener = objGenericListener;
        this.userId = userId;
        this.opponentId = opponentId;
    }

    public void initController(int requestCode, Intent intent, Uri uri) {
        try {
            switch (requestCode) {

                case PICK_IMAGE:

                    Uri originalUri = (uri==null)?intent.getData():uri;


                    if(originalUri!=null)
                    {
                        String selectedImagePath = getFilePathFromContentUri(originalUri);

                        if (TextUtils.isEmpty(selectedImagePath)) {

                            String id = originalUri.getLastPathSegment().split(":")[1];
                            final String[] imageColumns = {MediaStore.Images.Media.DATA};
                            final String imageOrderBy = null;

                            Uri newuri = getUri();
                            selectedImagePath = "path";

                            Cursor imageCursor = objWeakReference.get().managedQuery(newuri, imageColumns,
                                    MediaStore.Images.Media._ID + "=" + id, null, imageOrderBy);

                            if (imageCursor != null && imageCursor.moveToFirst()) {
                                selectedImagePath = imageCursor.getString(imageCursor.getColumnIndex(MediaStore.Images.Media.DATA));
//                            imageCursor.close();
                            }
                            Log.e("path", selectedImagePath); // use selectedImagePath
                        }
                        processImage(selectedImagePath);

                    }

                    break;

                default:
                    break;

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // By using this method get the Uri of Internal/External Storage for Media
    private Uri getUri() {
        String state = Environment.getExternalStorageState();
        if (!state.equalsIgnoreCase(Environment.MEDIA_MOUNTED))
            return MediaStore.Images.Media.INTERNAL_CONTENT_URI;

        return MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
    }


    private void postImage(int callerId, Object object) {

        objGenericListener.onResponse(callerId, object);
    }

    public void compressImage(String sourcefilePath, String DestinationPath) {

        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inSampleSize = 5;// reduces 1/4 th the original size

        Bitmap bitmap = BitmapFactory.decodeFile(sourcefilePath, bmOptions);

        File dest = new File(DestinationPath);

        try {
            FileOutputStream out = new FileOutputStream(dest);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void compressImage(Bitmap bitmap, String DestinationPath) {

        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inSampleSize = 5;// reduces 1/4 th the original size
        // Bitmap bitmap = BitmapFactory.decodeFile(sourcefilePath, bmOptions);

        File dest = new File(DestinationPath);

        try {
            FileOutputStream out = new FileOutputStream(dest);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Bitmap rotateImage(String sourcepath) {
       /* Bitmap retVal;

        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        retVal = Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);

        return retVal;*/


        int rotate = 0;

        float angle=0;

        File imageFile = new File(sourcepath);
        try {


            ExifInterface exif = new ExifInterface(
                    imageFile.getAbsolutePath());
            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);

            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_270:
                    angle = 270;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    angle = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_90:
                    angle = 90;
                    break;
            }
            Log.v("Image controller ", "Exif orientation: " + orientation);
        } catch (Exception e) {
            e.printStackTrace();
        }

        /****** Image rotation ****/
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        Bitmap bmp =  null;

        Bitmap cropped = null;
        try {
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            bmOptions.inSampleSize = 5;
            bmp = BitmapFactory.decodeStream(new FileInputStream(imageFile), null, bmOptions);
            cropped = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix, true);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


        return cropped;
    }





    private String getFilePathFromContentUri(Uri selectedFileUri) {
        String filePath = null;
        try{

            String[] filePathColumn = {MediaStore.MediaColumns.DATA};

            Cursor cursor = objWeakReference.get().getContentResolver().query(selectedFileUri, filePathColumn, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                filePath = cursor.getString(columnIndex);
                cursor.close();
            } else return null;

        }catch (Exception e)
        {
            e.printStackTrace();
        }

        return filePath;
    }


    public void processImage(String imagepath) {


        try {

            if (TextUtils.isEmpty(imagepath))
                return;

            String destfilename = Environment.getExternalStorageDirectory() + "/BabyChakraSPapp/IMG_" +
                    userId + "_" + opponentId + "_" + AppUtils.getCurrentDate() + ".jpg";
            File folder = new File(Environment.getExternalStorageDirectory() + "/BabyChakraSPapp");

            try {
                if (!folder.exists()) {
                    folder.mkdir();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            //compressImage(imagepath, destfilename);

            // get image pas for rotaion
            Bitmap rbitmap = rotateImage(imagepath);

             //pass to compress

            compressImage(rbitmap, destfilename);


            postImage(ImageController.PICK_IMAGE, destfilename);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }




    /************ Convert Image Uri path to physical path **************/

    public static String convertImageUriToFile ( Uri imageUri, Activity activity )  {

        Cursor cursor = null;
        int imageID = 0;

        try {

            /*********** Which columns values want to get *******/
            String [] proj={
                    MediaStore.Images.Media.DATA,
                    MediaStore.Images.Media._ID,
                    MediaStore.Images.Thumbnails._ID,
                    MediaStore.Images.ImageColumns.ORIENTATION
            };

            cursor = activity.managedQuery(

                    imageUri,         //  Get data for specific image URI
                    proj,             //  Which columns to return
                    null,             //  WHERE clause; which rows to return (all rows)
                    null,             //  WHERE clause selection arguments (none)
                    null              //  Order-by clause (ascending by name)

            );

            //  Get Query Data

            int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID);
            int columnIndexThumb = cursor.getColumnIndexOrThrow(MediaStore.Images.Thumbnails._ID);
            int file_ColumnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

            //int orientation_ColumnIndex = cursor.
            //    getColumnIndexOrThrow(MediaStore.Images.ImageColumns.ORIENTATION);

            int size = cursor.getCount();

            /*******  If size is 0, there are no images on the SD Card. *****/

            if (size == 0) {


                AppUtils.showLog("No Image");
            }
            else
            {

                int thumbID = 0;
                if (cursor.moveToFirst()) {

                    /**************** Captured image details ************/

                    /*****  Used to show image on view in LoadImagesFromSDCard class ******/
                    imageID     = cursor.getInt(columnIndex);

                    thumbID     = cursor.getInt(columnIndexThumb);

                    String Path = cursor.getString(file_ColumnIndex);

                    //String orientation =  cursor.getString(orientation_ColumnIndex);

                    String CapturedImageDetails = " CapturedImageDetails : \n\n"
                            +" ImageID :"+imageID+"\n"
                            +" ThumbID :"+thumbID+"\n"
                            +" Path :"+Path+"\n";

                    // Show Captured Image detail on activity
                    AppUtils.showLog("No CapturedImageDetails");

                }
            }
        } finally {
//            if (cursor != null) {
//                cursor.close();
//            }
        }

        // Return Captured Image ImageID ( By this ImageID Image will load from sdcard )

        return ""+imageID;
    }


}

