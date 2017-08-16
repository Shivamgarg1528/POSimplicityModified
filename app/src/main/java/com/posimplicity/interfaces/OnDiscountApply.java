package com.posimplicity.interfaces;

import android.support.v7.app.AlertDialog;

public interface OnDiscountApply {
  void onDiscountApply(int pDiscountType, float pValue, AlertDialog pAlertDialog);
}
