package com.example.qrazyqrsrus;

import static org.junit.jupiter.api.Assertions.*;

import android.widget.Button;
import android.widget.ListView;

import com.google.firebase.Firebase;

import org.junit.Rule;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.Mockito;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
//import org.junit.jupiter.api.Test;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import org.junit.Test;  // ADD THIS TO STOP ERROR

import java.util.ArrayList;

public class EventListTest extends EventList{
    ArrayList<Event> MockEventDataList = new ArrayList<>();
    Event mockEvent1 = Mockito.mock(Event.class);

    @Test
    public void onCreateView() {

        MockEventDataList.add(mockEvent1);

//        assertNotNull(MockEventDataList);
    }
}