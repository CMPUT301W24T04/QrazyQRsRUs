package com.example.qrazyqrsrus;

import android.content.Context;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Calendar;
import java.util.Date;


public class NewEventTextFragment extends Fragment implements Toolbar.OnMenuItemClickListener{
    private Toolbar toolbar;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
//        listener = (AddEventListener) context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.new_event_text_fragment, container, false);

        FloatingActionButton fab = view.findViewById(R.id.next_screen_button);
        fab.setOnClickListener(v -> {
            Bundle bundle = makeNewBundle();
            Navigation.findNavController(view).navigate(R.id.action_newEventTextFragment_to_newEventImageFragment2, bundle);
        });

        SwitchCompat limitAttendeesToggle = view.findViewById(R.id.limit_attendees_toggle);
        EditText maxAttendeesEditText = view.findViewById(R.id.max_attendees_edit_text);
        Button confirmMaxAttendeesButton = view.findViewById(R.id.confirm_max_attendees_button);

        limitAttendeesToggle.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                maxAttendeesEditText.setVisibility(View.VISIBLE);
                confirmMaxAttendeesButton.setVisibility(View.VISIBLE);
            } else {
                maxAttendeesEditText.setVisibility(View.GONE);
                confirmMaxAttendeesButton.setVisibility(View.GONE);
                maxAttendeesEditText.setText("");
            }
        });

        confirmMaxAttendeesButton.setOnClickListener(v -> {
            // after part 3
        });

        createToolbar(view);

        return view;
    }
    private void createToolbar(View view){
        //once we have made the view, we create the toolbar and inflate it's menu, in order to set and onclicklistener from the fragment
        //the idea to access the toolbar by using the Fragment's host View was taken from https://stackoverflow.com/questions/29020935/using-toolbar-with-fragments on February 21st, 2024
        //it was posted by the user Faisal Naseer (https://stackoverflow.com/users/2641848/faisal-naseer) in the post https://stackoverflow.com/a/45653449
        toolbar = (Toolbar) view.findViewById(R.id.text_screen_toolbar);
        toolbar.inflateMenu(R.menu.menu_no_back_button);
        //the fragment implements the Toolbar.OnMenuItemClick interface, pass itself.
        toolbar.setOnMenuItemClickListener(this);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.cancel_button){
            Navigation.findNavController(getView()).navigate(R.id.action_newEventTextFragment_to_newEventFragment);
            return true;
        }
        return false;
    }

    /**
     * Return a new Bundle with the user input this screen takes as serializables.
     *
     * @return  a Bundle containing all of the user input, and the userID of the organizer
     */
    private Bundle makeNewBundle(){
        Bundle bundle = getArguments();
        View view = getView();
        bundle.putSerializable("name", ((EditText) view.findViewById(R.id.event_name_edit_text)).getText().toString());
        bundle.putSerializable("organizerId", ( (Attendee) bundle.getSerializable("attendee")).getDocumentId());
        bundle.putSerializable("location", ((EditText) view.findViewById(R.id.event_location_edit_text)).getText().toString());
        bundle.putSerializable("details", ((EditText) view.findViewById(R.id.event_details_edit_text)).getText().toString());
        return bundle;
    }

}
