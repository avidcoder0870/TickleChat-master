package com.techpro.chat.ticklechat.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import com.techpro.chat.ticklechat.R;
import com.techpro.chat.ticklechat.utils.AppUtils;

/**
 * Created by vishalrandive on 06/04/16.
 */

public class GenericTextView extends TextView {

    public GenericTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs);
    }

    public GenericTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);

    }

    public GenericTextView(Context context) {
        super(context);
        init(null);
    }

    private void init(AttributeSet attrs) {



        if (attrs != null) {
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.GenericTextView);
            String fontName = a.getString(R.styleable.GenericTextView_fontName);
//			AppUtil.showLog(" fontName fontName fontNamefontName "+ fontName);
            if (fontName != null) {
                Typeface myTypeface = Typeface.createFromAsset(getContext().getAssets(), "" + fontName);
                setTypeface(myTypeface);
            } else {
                //Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "OpenSans-Regular.ttf");
                setTypeface(AppUtils.getFontType(getContext(), AppUtils.FONT_OPENSANS_REGULAR), 1);
            }


            a.recycle();
        }
        //setTextColor(getResources().getColor(R.color.blue));
//		setGravity(Gravity.CENTER);
//		setPadding(getDpValue(5), getDpValue(5), getDpValue(5), getDpValue(5));

    }

    public int getDpValue(int pixel) {
        float scaleValue = getContext().getResources().getDisplayMetrics().density;
        return (int) (pixel * scaleValue + 0.5f);
    }



}