//this fragment is the second fragment in the event creation sequence. it allows users to select a start time for their event.
package com.example.qrazyqrsrus;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Allows user to choose the start time of the event
 */
public class NewEventStartTimeFragment extends Fragment implements Toolbar.OnMenuItemClickListener {

    private androidx.appcompat.widget.Toolbar toolbar;

    /**
     * attaches dialog to screen
     * @param context
     */
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
        if (getArguments() != null) {

        }
    }

    /**
     * when view is created, show calendar to select the event
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
        View view = inflater.inflate(R.layout.new_event_start_time_fragment, container, false);
        FloatingActionButton fab = view.findViewById(R.id.next_screen_button);
        fab.setOnClickListener(v -> {
            //temporarily messily create a new event, put it in bundle to pass to next navigation destination
            Bundle args =  makeNewBundle(getArguments());
//            Event modifiedEvent = modifyEvent((Event) args.getSerializable("event"));
//            args.putSerializable("event", modifiedEvent);

            Navigation.findNavController(view).navigate(R.id.action_newEventStartTimeFragment_to_newEventEndTimeFragment, args);
        });
        Button dateButton = view.findViewById(R.id.show_date_picker_button);
        dateButton.setOnClickListener(v -> showDatePickerDialog());

        Button timeButton = view.findViewById(R.id.show_time_picker_button);
        timeButton.setOnClickListener(v -> showTimePickerDialog());
        Bundle args = getArguments();
        handleArguments(args, view);
        createToolbar(view);
        return view;
    }

    /**
     * Has the toolbar functionality to change views
     * @param view
     */
    private void createToolbar(View view){
        //once we have made the view, we create the toolbar and inflate it's menu, in order to set and onclicklistener from the fragment
        //the idea to access the toolbar by using the Fragment's host View was taken from https://stackoverflow.com/questions/29020935/using-toolbar-with-fragments on February 21st, 2024
        //it was posted by the user Faisal Naseer (https://stackoverflow.com/users/2641848/faisal-naseer) in the post https://stackoverflow.com/a/45653449
        toolbar = (androidx.appcompat.widget.Toolbar) view.findViewById(R.id.start_time_screen_toolbar);
        toolbar.inflateMenu(R.menu.menu_with_back_button);
        //the fragment implements the Toolbar.OnMenuItemClick interface, pass itself.
        toolbar.setOnMenuItemClickListener(this);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.back_button){
            Bundle args = getArguments();
            args = makeNewBundle(args);
            Navigation.findNavController(getView()).navigate(R.id.action_newEventStartTimeFragment_to_newEventTextFragment, args);
            return true;
        }  else if (id == R.id.cancel_button){
            //leave entire new event sequence
            Navigation.findNavController(getView()).navigate(R.id.action_newEventStartTimeFragment_to_newEventFragment);
            return true;
        }
        return false;
    }


    private LocalDateTime getLocalDateTime(DatePicker datePicker, TimePicker timePicker){
        return LocalDateTime.of(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth(), timePicker.getHour(), timePicker.getMinute());
    }


    private Bundle makeNewBundle(Bundle args){
        View view = getView();
        if (view == null || args == null) return args; // Safety checks

        Event.EventBuilder builder = (Event.EventBuilder) args.getSerializable("builder");
        if (builder != null) {
            Button dateButton = view.findViewById(R.id.show_date_picker_button);
            Button timeButton = view.findViewById(R.id.show_time_picker_button);

            // Assuming the date and time are set on the buttons in "yyyy-MM-dd" and "HH:mm" formats respectively
            String dateString = dateButton.getText().toString();
            String timeString = timeButton.getText().toString();

            // Combine the date and time strings
            String dateTimeString = dateString + " " + timeString;
            LocalDateTime startDate = LocalDateTime.parse(dateTimeString, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));

            // Update the builder with the new start date and time
            builder.setStartDate(startDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
            args.putSerializable("builder", builder);
        }

        return args;
    }


    private void handleArguments(Bundle args, View view) {
        Event.EventBuilder builder = (Event.EventBuilder) args.getSerializable("builder");
        if (builder != null && builder.getStartDate() != null) {
            String startDate = builder.getStartDate();
            LocalDateTime startDateTime = LocalDateTime.parse(startDate, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));

            Button dateButton = view.findViewById(R.id.show_date_picker_button);
            Button timeButton = view.findViewById(R.id.show_time_picker_button);

            // Format and set the date on the date button
            String formattedDate = startDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            dateButton.setText(formattedDate);

            // Format and set the time on the time button
            String formattedTime = startDateTime.format(DateTimeFormatter.ofPattern("HH:mm"));
            timeButton.setText(formattedTime);
        }
    }



    private void showDatePickerDialog() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                (view, year1, monthOfYear, dayOfMonth) -> {
                    // Handle the date chosen by the user
                    // Example: Update the button text
                    Button dateButton = getView().findViewById(R.id.show_date_picker_button);
                    dateButton.setText(String.format(Locale.getDefault(), "%d-%d-%d", year1, monthOfYear + 1, dayOfMonth));
                }, year, month, day);
        datePickerDialog.show();
    }

    private void showTimePickerDialog() {
        final Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(),
                (view, hourOfDay, minute1) -> {
                    // Handle the time chosen by the user
                    // Example: Update the button text
                    Button timeButton = getView().findViewById(R.id.show_time_picker_button);
                    timeButton.setText(String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minute1));
                }, hour, minute, true);
        timePickerDialog.show();
    }

}
