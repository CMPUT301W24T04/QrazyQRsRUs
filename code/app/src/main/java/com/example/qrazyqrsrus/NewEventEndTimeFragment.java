//this fragment is the third fragment in the event creation sequence. it allows users to select an end time for their event.

package com.example.qrazyqrsrus;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TimePicker;
import androidx.appcompat.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.time.LocalDateTime;

/**
 * Allows user to select the end time
 */
public class NewEventEndTimeFragment extends Fragment implements Toolbar.OnMenuItemClickListener {

    private androidx.appcompat.widget.Toolbar toolbar;

    /**
     * Saves the end dats in a bundle
     * @param param1
     * @param param2
     * @return fragment
     */
    public static NewEventEndTimeFragment newInstance(String param1, String param2) {
        NewEventEndTimeFragment fragment = new NewEventEndTimeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

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
            //temporarily messily create a new event, put it in bundle to pass to next navigation destination
            Bundle args =  makeNewBundle(getArguments());
//            Event modifiedEvent = modifyEvent((Event) args.getSerializable("event"));
//            args.putSerializable("event", modifiedEvent);

            Navigation.findNavController(view).navigate(R.id.action_newEventEndTimeFragment_to_newEventImageFragment, args);
        });
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
        if (id == R.id.back_button){
            Navigation.findNavController(getView()).navigate(R.id.action_newEventEndTimeFragment_to_newEventStartTimeFragment);
            return true;
        }  else if (id == R.id.cancel_button){
            //leave entire new event sequence
            Navigation.findNavController(getView()).navigate(R.id.action_newEventEndTimeFragment_to_newEventFragment);
            return true;
        }
        return false;
    }

    //create new event from the user input. messy, needs error checking

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

//    private String formatDateAndTime(DatePicker datePicker, TimePicker timePicker){
//
//    }
//    private Event modifyEvent(Event event){
//        //event.setEndDate(getLocalDateTime(getView().findViewById(R.id.event_date_picker), getView().findViewById(R.id.event_time_picker)));
//        return event;
//    }

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

    /**
     * hold date as bundle
     * @param bundle
     * @return Bundle
     */
    private Bundle makeNewBundle(Bundle bundle){
        DatePicker datePicker = getView().findViewById(R.id.event_date_picker);
        TimePicker timePicker = getView().findViewById(R.id.event_time_picker);
        LocalDateTime endDate = getLocalDateTime(datePicker, timePicker);
        bundle.putSerializable("endDate", endDate);
        return bundle;
    }
}
