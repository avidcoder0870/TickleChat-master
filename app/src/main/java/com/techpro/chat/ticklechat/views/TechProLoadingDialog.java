package com.techpro.chat.ticklechat.views;

import android.app.ProgressDialog;
import android.content.Context;

import com.techpro.chat.ticklechat.R;

/**
 * Created by vishalrandive on 20/04/16.
 */
public class TechProLoadingDialog  extends ProgressDialog {

    public TechProLoadingDialog(Context context) {
        super(context);

        this.setMessage(context.getString(R.string.loading));
    }
}
