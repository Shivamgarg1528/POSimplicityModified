package com.utils;

import android.view.View;

import com.posimplicity.R;

public class Keypad implements View.OnClickListener {

    private View mParentView;
    private KeypadClick mListener;
    private int[] mTypedArray;
    private StringBuilder mStringBuilder;

    public static final int[] keypadArray = {
            R.id.keypad_layout_0,
            R.id.keypad_layout_1,
            R.id.keypad_layout_2,
            R.id.keypad_layout_3,
            R.id.keypad_layout_4,
            R.id.keypad_layout_5,
            R.id.keypad_layout_6,
            R.id.keypad_layout_7,
            R.id.keypad_layout_8,
            R.id.keypad_layout_9,
            R.id.keypad_layout_del};

    public static final int[] keypadArrayExtra = {
            R.id.keypad_layout_0,
            R.id.keypad_layout_1,
            R.id.keypad_layout_2,
            R.id.keypad_layout_3,
            R.id.keypad_layout_4,
            R.id.keypad_layout_5,
            R.id.keypad_layout_6,
            R.id.keypad_layout_7,
            R.id.keypad_layout_8,
            R.id.keypad_layout_9,
            R.id.keypad_layout_del,
            R.id.keypad_layout_extra_key_5,
            R.id.keypad_layout_extra_key_10,
            R.id.keypad_layout_extra_key_20,
            R.id.keypad_layout_extra_key_25,
            R.id.keypad_layout_extra_key_50,
            R.id.keypad_layout_extra_key_100,
            R.id.keypad_layout_extra_key_125,
            R.id.keypad_layout_extra_key_250,
            R.id.keypad_layout_extra_key_500,
            R.id.keypad_layout_extra_key_625,
            R.id.keypad_layout_extra_key_1250,
            R.id.keypad_layout_extra_key_2500
    };


    public Keypad(View pParentView, KeypadClick pListener, int[] pTypedArray) {
        this.mParentView = pParentView;
        this.mListener = pListener;
        this.mTypedArray = pTypedArray;
    }

    public void onBind(float pValue) {
        onBind(pValue, true);
    }

    public void onBind(float pValue, boolean pLinking) {
        this.mStringBuilder = new StringBuilder(MyStringFormat.formatWith2DecimalPlaces(pValue));
        if (pLinking) {
            try {
                for (int i = 0; i < mTypedArray.length; i++) {
                    mParentView.findViewById(mTypedArray[i]).setOnClickListener(this);
                }
            } catch (Exception ignored) {
                ignored.printStackTrace();
            }
        }
    }

    public void onUnbind() {
        mParentView = null;
        mListener = null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.keypad_layout_0: {
                startCalculation(0);
                break;
            }
            case R.id.keypad_layout_1: {
                startCalculation(1);
                break;
            }
            case R.id.keypad_layout_2: {
                startCalculation(2);
                break;
            }
            case R.id.keypad_layout_3: {
                startCalculation(3);
                break;
            }
            case R.id.keypad_layout_4: {
                startCalculation(4);
                break;
            }
            case R.id.keypad_layout_5: {
                startCalculation(5);
                break;
            }
            case R.id.keypad_layout_6: {
                startCalculation(6);
                break;
            }
            case R.id.keypad_layout_7: {
                startCalculation(7);
                break;
            }
            case R.id.keypad_layout_8: {
                startCalculation(8);
                break;
            }
            case R.id.keypad_layout_9: {
                startCalculation(9);
                break;
            }
            case R.id.keypad_layout_del: {
                startCalculation(-1);
                break;
            }
            // adding 00 at end on belows number to convert it in float
            // for example 5 become 500 and output will be 5.00
            case R.id.keypad_layout_extra_key_5: {
                this.mStringBuilder = new StringBuilder();
                startCalculation(500);
                break;
            }
            case R.id.keypad_layout_extra_key_10: {
                this.mStringBuilder = new StringBuilder();
                startCalculation(1000);
                break;
            }
            case R.id.keypad_layout_extra_key_20: {
                this.mStringBuilder = new StringBuilder();
                startCalculation(2000);
                break;
            }
            case R.id.keypad_layout_extra_key_25: {
                this.mStringBuilder = new StringBuilder();
                startCalculation(2500);
                break;
            }
            case R.id.keypad_layout_extra_key_50: {
                this.mStringBuilder = new StringBuilder();
                startCalculation(5000);
                break;
            }
            case R.id.keypad_layout_extra_key_100: {
                this.mStringBuilder = new StringBuilder();
                startCalculation(10000);
                break;
            }
            case R.id.keypad_layout_extra_key_125: {
                this.mStringBuilder = new StringBuilder();
                startCalculation(12500);
                break;
            }
            case R.id.keypad_layout_extra_key_250: {
                this.mStringBuilder = new StringBuilder();
                startCalculation(25000);
                break;
            }
            case R.id.keypad_layout_extra_key_500: {
                this.mStringBuilder = new StringBuilder();
                startCalculation(50000);
                break;
            }
            case R.id.keypad_layout_extra_key_625: {
                this.mStringBuilder = new StringBuilder();
                startCalculation(62500);
                break;
            }
            case R.id.keypad_layout_extra_key_1250: {
                this.mStringBuilder = new StringBuilder();
                startCalculation(125000);
                break;
            }
            case R.id.keypad_layout_extra_key_2500: {
                this.mStringBuilder = new StringBuilder();
                startCalculation(250000);
                break;
            }
        }
    }

    public interface KeypadClick {
        void onKeypadClick(String pNewValue);
    }

    private void startCalculation(int pNumber) {
        if (pNumber < 0) { // Means we pressed back button
            mStringBuilder.deleteCharAt(mStringBuilder.length() - 1);
            if (mStringBuilder.length() < 4 || "0.00".equalsIgnoreCase(mStringBuilder.toString())) {
                mStringBuilder.insert(0, "0");
            }
        } else {
            for (int index = 0; index < mStringBuilder.length(); index++) {
                if (mStringBuilder.charAt(0) == '0') {
                    mStringBuilder.deleteCharAt(0);
                }
            }
            mStringBuilder.insert(mStringBuilder.length(), pNumber);
        }

        // deleting and inserting '.' from place to place....
        if (mStringBuilder.indexOf(".") >= 0) {
            mStringBuilder.deleteCharAt(mStringBuilder.indexOf("."));
        }
        mStringBuilder.insert(mStringBuilder.length() - 2, ".");
        mListener.onKeypadClick(mStringBuilder.toString());
    }
}
