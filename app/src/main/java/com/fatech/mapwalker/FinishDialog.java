package com.fatech.mapwalker;

import android.app.Dialog;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

public class FinishDialog extends DialogFragment {

    static String errorDialog = null;

    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        int KKal = (int) getArguments().get("KKal");
        String message = "Вы дошли до конца и прошли все километры. Вы сожгли " + KKal + " ККал. Столько же калорий содержится в:";
        return builder
                .setIcon(R.drawable.man_finish_dialog)
                .setTitle("Поздравляем!")
                .setMessage(message)
                .setView(R.layout.finish_dialog).create();
    }

}
