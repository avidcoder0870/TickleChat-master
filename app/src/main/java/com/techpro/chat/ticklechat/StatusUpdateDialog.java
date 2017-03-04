package com.techpro.chat.ticklechat;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.techpro.chat.ticklechat.listeners.GenericListener;

/**
 * Created by vishalrandive on 30/10/16.
 */

public class StatusUpdateDialog extends Dialog implements View.OnClickListener {

    private EditText mEtStatus;
    private TextView tvNegative;
    private TextView tvPositive;
    private GenericListener<String> objGenericListener;

    public StatusUpdateDialog (Context context, GenericListener<String> objGenericListener)
    {
        super(context);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        setContentView(R.layout.dialog_status_update);
        setCanceledOnTouchOutside(false);

        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        this.objGenericListener = objGenericListener;
        mEtStatus = (EditText) findViewById(R.id.et_status);
        tvNegative = (TextView) findViewById(R.id.tvNegative);
        tvPositive = (TextView) findViewById(R.id.tvPositive);

        tvNegative.setOnClickListener(this);
        tvPositive.setOnClickListener(this);
    }

    public void setMessage(String message) {
        mEtStatus.setText(message);
    }

    public void setPositiveButtonText(String positiveBtnTitle) {
        if (tvPositive != null)
            tvPositive.setText(positiveBtnTitle);
    }

    public void setNegativeButtonText(String negativeBtnTitle) {
        if (tvNegative != null)
            tvNegative.setText(negativeBtnTitle);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.tvNegative:
                objGenericListener.onResponse(R.id.tvNegative, null);
                break;

            case R.id.tvPositive:
                objGenericListener.onResponse(R.id.tvPositive, mEtStatus.getText().toString());
                break;
        }
    }
}
