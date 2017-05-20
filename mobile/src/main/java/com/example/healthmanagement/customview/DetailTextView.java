package com.example.healthmanagement.customview;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.widget.TextView;

import com.example.healthmanagement.R;

/**
 * Created by zyx10 on 2017/5/20 0020.
 */

public class DetailTextView extends AppCompatTextView {
    public DetailTextView(Context context) {
        super(context);
    }

    public DetailTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DetailTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    /**
     *
     * @param value 要显示的数据
     * @param isAfterMeal 是否餐后血糖值
     */
    public void setText(float value, boolean isAfterMeal) {
        String text = String.valueOf(value);
        BufferType type = BufferType.NORMAL;
        super.setText(text, type);
        try {
            float num = Float.valueOf(text);
            if (num == 0) {
                this.setText("");
                this.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.background));
            } else {
                if (isAfterMeal) {
                    if (num < 2.8) {
                        this.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.low));
                    } else if (num < 7.8) {
                        this.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.background));
                    } else if (num < 11.1) {
                        this.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.one));
                    } else {
                        this.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.two));
                    }
                } else {
                    if (num < 2.8) {
                        this.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.low));
                    } else if (num < 6.1) {
                        this.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.background));
                    } else if (num <7.0) {
                        this.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.one));
                    } else {
                        this.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.two));
                    }
                }
            }


        } catch (NumberFormatException e) {

        }
    }
}
