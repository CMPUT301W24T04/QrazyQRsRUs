package com.example.qrazyqrsrus;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class NewEventImageFragment extends Fragment implements Toolbar.OnMenuItemClickListener {
    private Toolbar toolbar;
    public static NewEventImageFragment newInstance(String param1, String param2) {
        NewEventImageFragment fragment = new NewEventImageFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
        //alternative method, giving toolbar to activity rather than fragment
//        toolbar = (Toolbar) getActivity().findViewById(R.id.image_screen_toolbar);
        //we must cast the fragment's activity to an AppCompatActivity in order to invoke setSupportActionBar
        //this idea was found from https://stackoverflow.com/questions/29020935/using-toolbar-with-fragments on February 21st, 2024
        //it was posted by user vinitius (https://stackoverflow.com/users/3112331/vinitius) in the reply https://stackoverflow.com/a/29021287
//        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.new_event_image_fragment, container, false);
        createToolbar(view);
        //temporarily display event name here
        ((TextView) view.findViewById(R.id.new_event_image_text)).setText(((Event) getArguments().getSerializable("event")).getEventName());
        FloatingActionButton fab = view.findViewById(R.id.image_screen_next_screen_button);
        //pass bundle ahead to qr code
        fab.setOnClickListener(v -> {
            Navigation.findNavController(view).navigate(R.id.action_newEventImageFragment_to_newEventQrFragment, getArguments());
        });
        return view;
    }

    private void createToolbar(View view){
        //once we have made the view, we create the toolbar and inflate it's menu, in order to set and onclicklistener from the fragment
        //the idea to access the toolbar by using the Fragment's host View was taken from https://stackoverflow.com/questions/29020935/using-toolbar-with-fragments on February 21st, 2024
        //it was posted by the user Faisal Naseer (https://stackoverflow.com/users/2641848/faisal-naseer) in the post https://stackoverflow.com/a/45653449
        toolbar = (Toolbar) view.findViewById(R.id.image_screen_toolbar);
        toolbar.inflateMenu(R.menu.menu_with_back_button);
        //the fragment implements the Toolbar.OnMenuItemClick interface, pass itself.
        toolbar.setOnMenuItemClickListener(this);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        int id = item.getItemId();
        //we check which item id was clicked on, navigating accordingly
        if (id == R.id.back_button){
            //go to previous screen
            Navigation.findNavController(getView()).navigate(R.id.action_newEventImageFragment_to_newEventTextFragment);
            return true;
        }
        else if (id == R.id.cancel_button){
            //leave entire new event sequence
            Navigation.findNavController(getView()).navigate(R.id.action_newEventImageFragment_to_newEventFragment);
            return true;
        }
        return false;
    }
}
