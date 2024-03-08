package com.example.qrazyqrsrus;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

/**
 * Displays error when QR code is wront
 */
public class ErrorDialog extends DialogFragment {
    @StringRes int resID;

    /**
     * displays the error on a dialog
     * @param resID
     */
    public ErrorDialog(@StringRes int resID){
        super();
        this.resID = resID;
    }

    /**
     * attaches the dialog to the view
     * @param context
     */
    @Override
    public void onAttach(@NonNull Context context){
        super.onAttach(context);
    }

    /**
     * when vies is created, generate the dialog
     * @param savedInstanceState The last saved instance state of the Fragment,
     * or null if this is a freshly created Fragment.
     *
     * @return Dialog
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view =
                LayoutInflater.from(getContext()).inflate(R.layout.error_dialog_fragment, null);
        TextView errorMessageTextView = view.findViewById(R.id.error_message);
        errorMessageTextView.setText(resID);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        return builder
                .setView(view)
                .setTitle("Error")
                .setPositiveButton("OK", null)
                .create();

    }
}
