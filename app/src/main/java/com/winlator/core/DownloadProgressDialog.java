package com.winlator.core;

import android.app.Activity;
import android.app.Dialog;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.winlator.R;

public class DownloadProgressDialog {
    private final Activity activity;
    private Dialog dialog;

    public DownloadProgressDialog(Activity activity) {
        this.activity = activity;
    }

    private void create() {
        if (dialog != null) return;
        dialog = new Dialog(activity, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(R.layout.download_progress_dialog);

        Window window = dialog.getWindow();
        if (window != null) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
        }
    }

    public void show(final Runnable onCancelCallback) {
        if (isShowing()) return;
        close();
        if (dialog == null) create();
        setProgress(0);
        dialog.findViewById(R.id.BTCancel).setOnClickListener((v) -> onCancelCallback.run());
        dialog.show();
    }

    public void setProgress(int progress) {
        if (dialog == null) return;
        ((CircularProgressIndicator)dialog.findViewById(R.id.CircularProgressIndicator)).setProgress(progress);
        ((TextView)dialog.findViewById(R.id.TVProgress)).setText(progress+"%");
    }

    public void close() {
        try {
            if (dialog != null) {
                dialog.dismiss();
            }
        }
        catch (Exception e) {}
    }

    public boolean isShowing() {
        return dialog != null && dialog.isShowing();
    }
}
