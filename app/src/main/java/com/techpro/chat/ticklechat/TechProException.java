package com.techpro.chat.ticklechat;

import android.app.Activity;
import android.content.Context;
/**
 * Created by vishalrandive on 06/04/16.
 */
public class TechProException extends Exception {
    String mMessage;

    public TechProException(Activity activity, String message) {
        super(message);

        mMessage = message;
        Alert.showSnackBar(activity, message);
    }

    public TechProException(Context context, String message) {
        super(message);

        mMessage = message;
        Alert.showSnackBar((Activity) context, message);
    }

    public TechProException(String message) {
        super(message);

        mMessage = message;
    }

    public String getmMessage() {
        return mMessage;
    }


}
