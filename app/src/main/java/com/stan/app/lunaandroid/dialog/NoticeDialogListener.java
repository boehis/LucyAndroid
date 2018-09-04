package com.stan.app.lunaandroid.dialog;


import android.support.v4.app.DialogFragment;

public interface NoticeDialogListener {
    void onDialogPositiveClick(DialogFragment dialog, String tag);
    void onDialogNegativeClick(DialogFragment dialog, String tag);
    void onDialogDismissed(DialogFragment dialog, String tag);
}
