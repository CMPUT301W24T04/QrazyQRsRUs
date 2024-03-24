//this fragment is the first fragment of the new event generation sequence. it allows users to name their event, set the location, details, and optionally limit attendees.
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

/**
 * shows the new event text
 */
public class NewEventTextFragment extends Fragment implements Toolbar.OnMenuItemClickListener{
    private Toolbar toolbar;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
//        listener = (AddEventListener) context;
    }

    /**
     * creates view
     * @param savedInstanceState If the fragment is being re-created from
     * a previous saved state, this is the state.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    /**
     * When view is created, show the event details as text
     * @param inflater The LayoutInflater object that can be used to inflate
     * any views in the fragment,
     * @param container If non-null, this is the parent view that the fragment's
     * UI should be attached to.  The fragment should not add the view itself,
     * but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     *
     * @return
     */
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

        limitAttendeesToggle.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                maxAttendeesEditText.setVisibility(View.VISIBLE);
            } else {
                maxAttendeesEditText.setVisibility(View.GONE);
                maxAttendeesEditText.setText("");
            }
        });

        Bundle args = getArguments();
        handleArguments(args, view);
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
        Event.EventBuilder builder = (Event.EventBuilder) bundle.getSerializable("builder");
        builder.setName(((EditText) view.findViewById(R.id.event_name_edit_text)).getText().toString());
        builder.setLocation(((EditText) view.findViewById(R.id.event_location_edit_text)).getText().toString());
        builder.setDetails(((EditText) view.findViewById(R.id.event_details_edit_text)).getText().toString());
        builder.setOrganizerId(((Attendee) bundle.getSerializable("attendee")).getDocumentId());

        String maxAttendeesString = ((EditText) view.findViewById(R.id.max_attendees_edit_text)).getText().toString();

        Integer maxAttendees = null;
        if (!maxAttendeesString.isEmpty()) {
            try {
                maxAttendees = Integer.valueOf(maxAttendeesString);
            } catch (NumberFormatException e) {
                throw new RuntimeException("cannot convert string to int");
            }
        }

        builder.setMaxAttendees(maxAttendees);
        //we put the updates builder back into the bundle
        bundle.putSerializable("builder", builder);

        return bundle;
    }

    private void handleArguments(Bundle args, View view){
        Event.EventBuilder builder = (Event.EventBuilder) args.getSerializable("builder");
        if (builder.getName() != null){
            //if builder.getName() != null, the user has already been to this screen, so we restore their input
            ((EditText) view.findViewById(R.id.event_name_edit_text)).setText(builder.getName());
            ((EditText) view.findViewById(R.id.event_location_edit_text)).setText(builder.getLocation());
            ((EditText) view.findViewById(R.id.event_details_edit_text)).setText(builder.getDetails());
            //we check if user set maxAttendees
            if (builder.getMaxAttendees() != null){
                ((SwitchCompat) view.findViewById(R.id.limit_attendees_toggle)).setChecked(true);
                ((EditText) view.findViewById(R.id.max_attendees_edit_text)).setText(builder.getMaxAttendees().toString());
            }
        }
    }

}
