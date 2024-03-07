package com.example.qrazyqrsrus;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Calendar;
import java.util.Date;


public class NewEventTextFragment extends Fragment implements Toolbar.OnMenuItemClickListener{
    //temporarily define a listener to add events to. eventually we should be adding events to firstore
    interface AddEventListener{
        void addEvent(Event event);
    }
    private Toolbar toolbar;
    private AddEventListener listener;
    public static NewEventTextFragment newInstance(String param1, String param2) {
        NewEventTextFragment fragment = new NewEventTextFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    //temporarily set listener to be mainActivity. should eventually be adding events to firestore.
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        listener = (AddEventListener) context;
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
            //temporarily messily create a new event, put it in bundle to pass to next navigation destination
            createNewEvent(view);
            Event newEvent = createNewEvent(view);
            listener.addEvent(newEvent);
            Bundle bundle = new Bundle();
            bundle.putSerializable("event", newEvent);
            Navigation.findNavController(view).navigate(R.id.action_newEventTextFragment_to_newEventImageFragment2, bundle);
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


    //create new event from the user input. messy, needs error checking
    private Event createNewEvent(View view){
        String eventName = ((EditText) view.findViewById(R.id.event_name_edit_text)).getText().toString();
        String eventLocation = ((EditText) view.findViewById(R.id.event_location_edit_text)).getText().toString();
        Date eventDate = getDate(((DatePicker) view.findViewById(R.id.event_date_picker)));
        String eventDetails = ((EditText) view.findViewById(R.id.event_details_edit_text)).getText().toString();

        //temporary add event, we should be storing the event into firestore
        return new Event(eventName, eventDetails, eventLocation, eventDate);
    }

    //we must convert the date that was picked by the user into an Date (java.util.Date)
    //this conversion from the android DatePicker to a java Data is from https://stackoverflow.com/questions/8409043/getdate-from-datepicker-android on February 21st, 2024
    //it was posted by user Andres Canavesi (https://stackoverflow.com/users/641238/andr%c3%a9s-canavesi) in the post (https://stackoverflow.com/a/14590523)
    private Date getDate(DatePicker datePicker){
        //we get the user input (picked date)
        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth();
        int year = datePicker.getYear();

        //we convert the picked date into a calendar entry
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);

        //we use calendar.getTime() to get the formatted Date of the event
        return calendar.getTime();
    }

}
