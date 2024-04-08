//this fragment is the third fragment in the event creation sequence. it allows users to select an end time for their event.

package com.example.qrazyqrsrus;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.SignStyle;
import java.time.temporal.ChronoField;
import java.util.Calendar;
import java.util.Locale;

/**
 * Allows user to select the end time
 */
public class NewEventEndTimeFragment extends Fragment implements Toolbar.OnMenuItemClickListener {

    private androidx.appcompat.widget.Toolbar toolbar;

    //temporarily set listener to be mainActivity. should eventually be adding events to firestore.
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    /**
     * create view
     * @param savedInstanceState If the fragment is being re-created from
     * a previous saved state, this is the state.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    /**
     * When view created save bundle
     * @param inflater The LayoutInflater object that can be used to inflate
     * any views in the fragment,
     * @param container If non-null, this is the parent view that the fragment's
     * UI should be attached to.  The fragment should not add the view itself,
     * but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     *
     * @return View
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.new_event_end_time_fragment, container, false);
        FloatingActionButton fab = view.findViewById(R.id.next_screen_button);
        fab.setOnClickListener(v -> {
            Bundle args = getArguments(); // args might be null, need to handle this
            if(args != null) {
                try {
                    makeNewBundle(args);
                    Navigation.findNavController(view).navigate(R.id.action_newEventEndTimeFragment_to_newEventImageFragment, args);
                } catch (Exception e) {
                    if(getActivity() != null) {
                        new ErrorDialog(R.string.no_e_date).show(getActivity().getSupportFragmentManager(), "Error Dialog");
                    }
                }
            }
        });
        TextView dateButton = view.findViewById(R.id.date_display_textview);
        dateButton.setOnClickListener(v -> showDatePickerDialog());

        TextView timeButton = view.findViewById(R.id.time_display_textview);
        timeButton.setOnClickListener(v -> showTimePickerDialog());
        Bundle args = getArguments();
        if(args != null) {
            handleArguments(args, view);
        }
        createToolbar(view);
        return view;
    }

    /**
     * Toolbar functionality
     * @param view
     */
    private void createToolbar(View view){
        //once we have made the view, we create the toolbar and inflate it's menu, in order to set and onclicklistener from the fragment
        //the idea to access the toolbar by using the Fragment's host View was taken from https://stackoverflow.com/questions/29020935/using-toolbar-with-fragments on February 21st, 2024
        //it was posted by the user Faisal Naseer (https://stackoverflow.com/users/2641848/faisal-naseer) in the post https://stackoverflow.com/a/45653449
        toolbar = (androidx.appcompat.widget.Toolbar) view.findViewById(R.id.end_time_screen_toolbar);
        toolbar.inflateMenu(R.menu.menu_with_back_button);
        //the fragment implements the Toolbar.OnMenuItemClick interface, pass itself.
        toolbar.setOnMenuItemClickListener((androidx.appcompat.widget.Toolbar.OnMenuItemClickListener) this);
    }

    /**
     * switch views when menu is clicked
     * @param item {@link MenuItem} that was clicked
     * @return
     */
    @Override
    public boolean onMenuItemClick(MenuItem item) {
        int id = item.getItemId();
        View currentView = getView(); // Store the view once to avoid multiple calls to getView
        if (currentView != null) { // Check if the current view is not null
            if (id == R.id.back_button) {
                Bundle args = getArguments();
                if (args == null) args = new Bundle(); // If args is null, create a new Bundle
                args = makeNewBundle(args); // Assign new value to args from makeNewBundle
                Navigation.findNavController(currentView).navigate(R.id.action_newEventEndTimeFragment_to_newEventStartTimeFragment, args);
                return true;
            } else if (id == R.id.cancel_button) {
                //leave the entire new event sequence
                Navigation.findNavController(currentView).navigate(R.id.action_newEventEndTimeFragment_to_newEventFragment);
                return true;
            }
        }
        return false;
    }



    /**
     * Save the local time
     * @param datePicker
     * @param timePicker
     * @return LocalDateTime
     */
    //we must convert the date that was picked by the user into an LocalDateTime (java.time.LocalDateTime)
    private LocalDateTime getLocalDateTime(DatePicker datePicker, TimePicker timePicker){
        return LocalDateTime.of(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth(), timePicker.getHour(), timePicker.getMinute());
    }

    private LocalDateTime parseDateTimeString(String dateTimeStr) {
        // Create a DateTimeFormatter with optional parts for day and month
        DateTimeFormatter formatter = new DateTimeFormatterBuilder()
                .appendPattern("yyyy")
                .appendLiteral('-')
                .appendValue(ChronoField.MONTH_OF_YEAR, 1, 2, SignStyle.NORMAL)
                .appendLiteral('-')
                .appendValue(ChronoField.DAY_OF_MONTH, 1, 2, SignStyle.NORMAL)
                .appendLiteral(' ')
                .appendValue(ChronoField.HOUR_OF_DAY, 1, 2, SignStyle.NORMAL)
                .appendLiteral(':')
                .appendValue(ChronoField.MINUTE_OF_HOUR, 2)
                .toFormatter(Locale.US);

        // Parse the date and time string
        return LocalDateTime.parse(dateTimeStr, formatter);
    }

    /**
     * Updates the provided Bundle with the end date and time selected by the user.
     * This method reads the end date and time from UI components, combines them into a single
     * {@link LocalDateTime} object, and stores it back into the provided Bundle.
     *
     * @param args The Bundle containing the {@link Event.EventBuilder} object to be updated.
     * @return The updated Bundle with the new end date and time set in the EventBuilder.
     * If either the view is not accessible or the initial arguments are null, the original
     * arguments are returned without modification.
     */
    private Bundle makeNewBundle(Bundle args) {
        View view = getView();
        if (view == null || args == null) return args; // Safety checks
        Event.EventBuilder builder = (Event.EventBuilder) args.getSerializable("builder");
        if (builder != null) {
            TextView dateButton = view.findViewById(R.id.date_display_textview);
            TextView timeButton = view.findViewById(R.id.time_display_textview);

            // Assuming the date and time are set on the buttons in "yyyy-MM-dd" and "HH:mm" formats respectively
            String dateString = dateButton.getText().toString();
            String timeString = timeButton.getText().toString();

            // Combine the date and time strings
            String dateTimeString = dateString + " " + timeString;
            LocalDateTime endDate = parseDateTimeString(dateTimeString);

            // Update the builder with the new start date and time
            builder.setEndDate(endDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
            args.putSerializable("builder", builder);
        }
        return args;
    }

    /**
     * Populates UI components with the end date and time from the provided arguments.
     * This method extracts the end date and time from the {@link Event.EventBuilder} within
     * the provided Bundle, formats them according to specified patterns, and sets them on
     * corresponding TextViews for display.
     *
     * @param args The Bundle containing the {@link Event.EventBuilder} from which to extract
     *             the end date and time.
     * @param view The view containing the TextViews to be updated with the date and time.
     */
    private void handleArguments (Bundle args, View view){
        Event.EventBuilder builder = (Event.EventBuilder) args.getSerializable("builder");
        if (builder != null && builder.getEndDate() != null) {
            String endDate = builder.getEndDate();

            // Use the custom parsing method
            LocalDateTime endDateTime = parseDateTimeString(endDate);

            TextView dateButton = view.findViewById(R.id.date_display_textview);
            TextView timeButton = view.findViewById(R.id.time_display_textview);

            // Format and set the date on the date button
            String formattedDate = endDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            dateButton.setText(formattedDate);

            // Format and set the time on the time button
            String formattedTime = endDateTime.format(DateTimeFormatter.ofPattern("HH:mm"));
            timeButton.setText(formattedTime);
        }
    }

    /**
     * Displays a date picker dialog to the user for selecting the event's end date.
     * The selected date is formatted and displayed in a TextView. The dialog is initialized
     * with today's date as the default selection.
     */
    private void showDatePickerDialog() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        Context context = getContext(); // Get the context and check if it is not null
        if (context != null) {
            DatePickerDialog datePickerDialog = new DatePickerDialog(context, (view, year1, monthOfYear, dayOfMonth) -> {
                View rootView = getView(); // Get the current view and check if it is not null
                if (rootView != null) {
                    TextView dateButton = rootView.findViewById(R.id.date_display_textview);
                    // Month is 0-based in Calendar, add 1 for display purposes
                    dateButton.setText(String.format(Locale.getDefault(), "%d-%02d-%02d", year1, monthOfYear + 1, dayOfMonth));
                }
            }, year, month, day);

            datePickerDialog.show();
        }
    }

    /**
     * Displays a time picker dialog to the user for selecting the event's end time.
     * The selected time is formatted in 24-hour format and displayed in a TextView. The dialog
     * is initialized with the current time as the default selection.
     */
    private void showTimePickerDialog() {
        final Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        Context context = getContext(); // Check for null context
        if (context != null) {
            TimePickerDialog timePickerDialog = new TimePickerDialog(context, (timePickerView, hourOfDay, minute1) -> {
                View rootView = getView(); // Check for null view
                if (rootView != null) {
                    TextView timeButton = rootView.findViewById(R.id.time_display_textview);
                    if (timeButton != null) { // Check if the TextView was found
                        timeButton.setText(String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minute1));
                    }
                }
            }, hour, minute, true);
            timePickerDialog.show();
        }

    }

}
