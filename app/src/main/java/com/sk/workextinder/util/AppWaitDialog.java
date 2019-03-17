package com.sk.workextinder.util;

import android.app.ProgressDialog;
import android.content.Context;

/*Created by Sambhaji Karad on 12/03/2019.*/

public class AppWaitDialog extends ProgressDialog {

    /**
     * Instantiates a new oE dialog.
     *
     * @param context the context
     */
    public AppWaitDialog(Context context) {
        super(context);
        this.setMessage("Please wait...");
        this.setCancelable(false);
    }

    /**
     * Instantiates a new oE dialog.
     *
     * @param context      the context
     * @param isCancelable the is cancelable
     * @param message      the message
     */
    public AppWaitDialog(Context context, boolean isCancelable, String message) {
        super(context);
        this.setTitle("Please wait...");
        this.setCancelable(isCancelable);
        this.setMessage(message);
        this.setCancelable(false);
    }
}
