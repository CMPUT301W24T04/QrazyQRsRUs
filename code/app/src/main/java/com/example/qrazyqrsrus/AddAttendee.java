package com.example.qrazyqrsrus;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.qrazyqrsrus.Attendee;

///**
// * A simple {@link Fragment} subclass.
// * Use the {@link AddAttendee#newInstance} factory method to
// * create an instance of this fragment.
// */
public class AddAttendee extends DialogFragment {
    /**
     * Adds info to the attendee to be added to the attendee list
     */

    interface AddAttendeeDialogListener{
        void addAttendeeToList(Attendee attendee);
    }

    private AddAttendeeDialogListener listener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof AddAttendeeDialogListener) {
            listener = (AddAttendeeDialogListener) context;
        } else {
            throw new RuntimeException(context + " must implement AddAttendeeDialogListener");
        }
    }

    static AddAttendee newInstance(Attendee attendee){
        /**
         * Allows the attendee to be saved as an instance using a bundle
         */
        Bundle args = new Bundle();
        args.putSerializable("attendee", attendee);
        AddAttendee fragment = new AddAttendee();
        fragment.setArguments(args);
        return fragment;
    }

//    @NonNull
//    @Override
//    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
//        // make view content the fragment_add_attendee
//        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_add_attendee, null);
//        EditText addName = view.findViewById(R.id.add_name);
//        EditText addNumCheckins = view.findViewById(R.id.add_num_checkins);
//        // get the attendee added as an argument on the bundle
////        Bundle bundle = getArguments();
////        Attendee newAttendee = (Attendee) bundle.getSerializable("attendee");
//
//        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
//        return builder
//                .setView(view)
//                .setTitle("Add an attendee")
//                .setNegativeButton("Cancel", null)
//                .setPositiveButton("OK", (dialog, which) -> {
//                    String Name = addName.getText().toString();
//                    String numCheckins = addNumCheckins.getText().toString();
////                    addName.setText(newAttendee.getName());
////                    addNumCheckins.setText(newAttendee.getNum_checkins());
//                    listener.addAttendeeToList(new Attendee(Name, numCheckins));
//                })
//                .create();
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_attendee, container, false);
    }
}