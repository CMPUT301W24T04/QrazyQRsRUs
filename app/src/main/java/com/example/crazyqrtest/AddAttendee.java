package com.example.crazyqrtest;

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

///**
// * A simple {@link Fragment} subclass.
// * Use the {@link AddAttendee#newInstance} factory method to
// * create an instance of this fragment.
// */
public class AddAttendee extends DialogFragment {
    /**
     * Adds info to the attendee to be added to the attendee list
     */

//    // TODO: Rename parameter arguments, choose names that match
//    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//    private static final String ARG_PARAM1 = "param1";
//    private static final String ARG_PARAM2 = "param2";
//
//    // TODO: Rename and change types of parameters
//    private String mParam1;
//    private String mParam2;
//
//    public AddAttendee() {
//        // Required empty public constructor
//    }
//
//    /**
//     * Use this factory method to create a new instance of
//     * this fragment using the provided parameters.
//     *
//     * @param param1 Parameter 1.
//     * @param param2 Parameter 2.
//     * @return A new instance of fragment AddAttendee.
//     */
//    // TODO: Rename and change types and number of parameters
//    public static AddAttendee newInstance(String param1, String param2) {
//        AddAttendee fragment = new AddAttendee();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
//        return fragment;
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
//    }

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
            throw new RuntimeException(context + "must implement AddAttendeeListener");
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

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        // make view content the fragment_add_attendee
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_add_attendee, null);
        EditText addName = view.findViewById(R.id.add_name);
        EditText addNumCheckins = view.findViewById(R.id.add_num_checkins);
        // get the attendee added as an argument on the bundle
        Bundle bundle = getArguments();
        Attendee newAttendee = (Attendee) bundle.getSerializable("book");
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        return builder
                .setView(view)
                .setTitle("Edit the Book(close keyboard before pressing OK)")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("OK", (dialog, which) -> {
                    addName.setText(newAttendee.getName());
                    addNumCheckins.setText(newAttendee.getNum_checkins());
                    listener.addAttendeeToList(new Attendee("Ryan", "4"));
                })
                .create();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_attendee, container, false);
    }
}