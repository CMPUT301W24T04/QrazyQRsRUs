//this fragment is the first fragment of the new event generation sequence. it allows users to name their event, set the location, details, and optionally limit attendees.
package com.example.qrazyqrsrus;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Locale;
import java.util.Objects;

/**
 * shows the new event text
 */
public class NewEventTextFragment extends Fragment implements Toolbar.OnMenuItemClickListener{

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

    }

    /**
     * creates view
     * @param savedInstanceState If the fragment is being re-created from
     * a previous saved state, this is the state.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
     *
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.new_event_text_fragment, container, false);

        FloatingActionButton fab = view.findViewById(R.id.next_screen_button);
        fab.setOnClickListener(v -> FirebaseDB.getInstance().getToken(token -> {
            Log.d("newEventTextFragment", token);
            Bundle bundle = makeNewBundle(token);
            if (bundle != null) {
                Navigation.findNavController(view).navigate(R.id.action_newEventTextFragment_to_newEventImageFragment2, bundle);
            }
        }));

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
        if (args != null) {
            handleArguments(args, view);
        }
        createToolbar(view);
        return view;
    }
    private void createToolbar(View view){
        //once we have made the view, we create the toolbar and inflate it's menu, in order to set and onclick listener from the fragment
        //the idea to access the toolbar by using the Fragment's host View was taken from https://stackoverflow.com/questions/29020935/using-toolbar-with-fragments on February 21st, 2024
        //it was posted by the user Faisal Naseer (https://stackoverflow.com/users/2641848/faisal-naseer) in the post https://stackoverflow.com/a/45653449
        Toolbar toolbar = view.findViewById(R.id.text_screen_toolbar);
        toolbar.inflateMenu(R.menu.menu_no_back_button);
        //the fragment implements the Toolbar.OnMenuItemClick interface, pass itself.
        toolbar.setOnMenuItemClickListener(this);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        if (item.getItemId() == R.id.cancel_button) {
            View view = getView();
            if (view != null) {
                Navigation.findNavController(view).navigate(R.id.action_newEventTextFragment_to_newEventFragment);
            }
            return true;
        }
        return false;
    }

    /**
     * Return a new Bundle with the user input this screen takes as serializable.
     *
     * @return  a Bundle containing all of the user input, and the userID of the organizer
     */
    private Bundle makeNewBundle(String organizerToken) {
        View view = getView();
        if (view == null) return null;

        Bundle bundle = getArguments();
        if (bundle == null) return null;

        Event.EventBuilder builder = (Event.EventBuilder) bundle.getSerializable("builder");
        Attendee attendee = (Attendee) bundle.getSerializable("attendee");

        if (builder == null || attendee == null) return null;

        builder.setName(((EditText) view.findViewById(R.id.event_name_edit_text)).getText().toString());
        builder.setLocation(((EditText) view.findViewById(R.id.event_location_edit_text)).getText().toString());
        builder.setDetails(((EditText) view.findViewById(R.id.event_details_edit_text)).getText().toString());
        builder.setOrganizerId(attendee.getDocumentId());

        builder.setOrganizerId(((Attendee) Objects.requireNonNull(bundle.getSerializable("attendee"))).getDocumentId());
        builder.setGeolocationOn(((SwitchCompat) view.findViewById(R.id.geolocation_toggle)).isChecked());
        builder.setOrganizerToken(organizerToken);

        String maxAttendeesString = ((EditText) view.findViewById(R.id.max_attendees_edit_text)).getText().toString();
        if (!maxAttendeesString.isEmpty()) {
            try {
                Integer maxAttendees = Integer.valueOf(maxAttendeesString);
                builder.setMaxAttendees(maxAttendees);
            } catch (NumberFormatException e) {
                Log.e("NewEventTextFragment", "Number format exception", e);
            }
        }

        SwitchCompat geolocationToggle = view.findViewById(R.id.geolocation_toggle);
        if (geolocationToggle.isChecked()) {
            builder.setGeolocationOn(true);
        }

        bundle.putSerializable("builder", builder);
        return bundle;
    }

    private void handleArguments(Bundle args, View view){
        Event.EventBuilder builder = (Event.EventBuilder) args.getSerializable("builder");
        if (builder != null && builder.getName() != null){
            //if builder.getName() != null, the user has already been to this screen, so we restore their input
            ((EditText) view.findViewById(R.id.event_name_edit_text)).setText(builder.getName());
            ((EditText) view.findViewById(R.id.event_location_edit_text)).setText(builder.getLocation());
            ((EditText) view.findViewById(R.id.event_details_edit_text)).setText(builder.getDetails());
            //we check if user set maxAttendees
            if (builder.getMaxAttendees() != null){
                ((SwitchCompat) view.findViewById(R.id.limit_attendees_toggle)).setChecked(true);
                String maxAttendees = String.format(Locale.getDefault(), "%d", builder.getMaxAttendees());
                ((EditText) view.findViewById(R.id.max_attendees_edit_text)).setText(maxAttendees);
            }
        }
    }

}
