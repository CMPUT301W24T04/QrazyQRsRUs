package com.example.qrazyqrsrus;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasType;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.testing.FragmentScenario;
import androidx.lifecycle.Lifecycle;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.intent.matcher.IntentMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;


import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

@RunWith(AndroidJUnit4.class)
public class NewEventImageFragmentTest {
//    FragmentScenario<NewEventImageFragment> scenario;
    Bundle args;
    Uri mockImageUri = Mockito.mock(Uri.class);
    Intent resultData = new Intent();

    @Before
    public void setUp(){
        Event.EventBuilder mockBuilder = new Event.EventBuilder();
        mockBuilder.setName("Detail-less Event");
        mockBuilder.setLocation("somewhere");
        mockBuilder.setDetails("eventful");
        mockBuilder.setStartDate("some time");
        mockBuilder.setEndDate("some time later");

        Bundle args = new Bundle();
        args.putSerializable("builder", mockBuilder);

//        FragmentScenario<NewEventImageFragment> scenario = FragmentScenario.launchInContainer(
//                NewEventImageFragment.class, args);

        //maybe goes in each test?
//        scenario.moveToState(Lifecycle.State.STARTED);
        resultData.setData(mockImageUri);

        Intents.init();
    }

    @After
    public void tearDown(){
        Intents.release();
    }

    //this test was written with the help of ChatGPT
    //OpenAI, 2024, ChatGPT, Android UI tests for a class that launches a pick media activity using espresso
    @Test
    public void testImageButtonClick(){
        // Set up an ActivityResultCallback to return a mock image URI
        InstrumentationRegistry.getInstrumentation().addMonitor(
                Intent.ACTION_GET_CONTENT,
                new Instrumentation.ActivityResult(Activity.RESULT_OK, resultData),
                true
        );

        // Launch the fragment
        FragmentScenario<NewEventImageFragment> scenario = FragmentScenario.launchInContainer(NewEventImageFragment.class);
        scenario.moveToState(Lifecycle.State.STARTED);

        // Simulate a click on the image button
        onView(withId(R.id.new_event_image_button))
                .perform(ViewActions.click());

        // Verify that the image picker screen is opened
        intended(IntentMatchers.hasAction(Intent.ACTION_GET_CONTENT));

        // Verify that the returned intent has the expected data
        intended(IntentMatchers.hasData(mockImageUri));

    }

}
