//this fragment is the second fragment in the event creation sequence. it allows users to select a start time for their event.
package com.example.qrazyqrsrus;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

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

/**
 * Allows user to choose the start time of the event
 */
public class NewEventStartTimeFragment extends Fragment implements Toolbar.OnMenuItemClickListener {

    private androidx.appcompat.widget.Toolbar toolbar;

//    public static NewEventStartTimeFragment newInstance(String param1, String param2) {
//        NewEventStartTimeFragment fragment = new NewEventStartTimeFragment();
//        Bundle args = new Bundle();
//        fragment.setArguments(args);
//        return fragment;
//    }

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

    //create new event from the user input. messy, needs error checking


//    we must convert the date that was picked by the user into an LocalDateTime (java.time.LocalDateTime)
    private LocalDateTime getLocalDateTime(DatePicker datePicker, TimePicker timePicker){
        return LocalDateTime.of(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth(), timePicker.getHour(), timePicker.getMinute());
    }
//    private String getDateString(DatePicker datePicker){
//        int year = datePicker.getYear();
//        int month = datePicker.getMonth();
//        int day = datePicker.getDayOfMonth();
//
//        String yearString = Integer.toString(year);
//        String monthString = Integer.toString(month);
//        String dayString = Integer.toString(day);
//
//        String date = dayString + "/" + monthString + "/" + yearString;
//        return date;
//    }

    private Bundle makeNewBundle(Bundle args){
        View view = getView();
        Event.EventBuilder builder = (Event.EventBuilder) args.getSerializable("builder");

        DatePicker datePicker = getView().findViewById(R.id.event_date_picker);
        TimePicker timePicker = getView().findViewById(R.id.event_time_picker);
        LocalDateTime startDate = getLocalDateTime(datePicker, timePicker);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        builder.setStartDate(startDate.format(formatter));



        //we put the updates builder back into the bundle
        args.putSerializable("builder", builder);

        return args;
    }
//    private Bundle makeNewBundle(Bundle bundle){
//        DatePicker datePicker = getView().findViewById(R.id.event_date_picker);
//        TimePicker timePicker = getView().findViewById(R.id.event_time_picker);
//        LocalDateTime startDate = getLocalDateTime(datePicker, timePicker);
//        bundle.putSerializable("startDate", startDate);
//        return bundle;
//    }

    private void handleArguments(Bundle args, View view){
        Event.EventBuilder builder = (Event.EventBuilder) args.getSerializable("builder");
        if (builder.getStartDate() != null){
            String startDate = builder.getStartDate();
            //if builder.getStartDate() != null, the user has already been to this screen, so we restore their input
            DatePicker datePicker = view.findViewById(R.id.event_date_picker);
            TimePicker timePicker = view.findViewById(R.id.event_time_picker);
            //MAKE SURE STRING SLICING IS RIGHT
            String yearString = startDate.substring(0,4);
            String monthString = startDate.substring(5,7);
            String dayString = startDate.substring(8,10);

            Log.d("yearString", yearString);
            Log.d("monthString", monthString);
            Log.d("dayString", dayString);

            datePicker.updateDate(Integer.valueOf(yearString), Integer.valueOf(monthString), Integer.valueOf(dayString));

            String hourString = startDate.substring(11,13);
            String minuteString = startDate.substring(14,16);

            Log.d("hourString", hourString);
            Log.d("minuteString", minuteString);

            timePicker.setHour(Integer.valueOf(hourString));
            timePicker.setMinute(Integer.valueOf(minuteString));
        }
    }
}
