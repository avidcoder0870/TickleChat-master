package com.techpro.chat.ticklechat.utils;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;


import com.techpro.chat.ticklechat.R;
import com.techpro.chat.ticklechat.TechProException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Locale;

public class BitmapUtils {
    private static final int MAX_IMAGE_SIZE_IN_MB = 8;
    private static final int MAX_IMAGE_SIZE = MAX_IMAGE_SIZE_IN_MB * 1024 * 1024 * 8;

    public static Bitmap decodeSampledBitmapFromFile(String imagePath, int reqWidth, int reqHeight) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;

        BitmapFactory.decodeFile(imagePath, options);
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(imagePath, options);
    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    public static File writeBitmapToFile(Context context, Bitmap bitmap, String fileName) throws TechProException {
        String file_path = Environment.getExternalStorageDirectory().getAbsolutePath() +
                File.separator + context.getString(R.string.app_name);
        File dir = new File(file_path);

        boolean isDirectoryCreated = dir.exists() || dir.mkdirs();
        if (isDirectoryCreated) {
            File file = new File(dir, fileName + ".jpeg");
            FileOutputStream fileOutputStream;
            try {
                fileOutputStream = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 80, fileOutputStream);

                fileOutputStream.flush();
                fileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (file.length() > MAX_IMAGE_SIZE) {
                String message = String.format(Locale.US
                        , context.getString(R.string.file_size_error), MAX_IMAGE_SIZE_IN_MB);
                throw new TechProException(context, message);
            }
            return file;
        } else {
            throw new TechProException(context, context.getString(R.string.could_not_create_directory));
        }
    }

    public static String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();

            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }
}
