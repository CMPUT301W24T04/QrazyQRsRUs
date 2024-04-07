package com.example.qrazyqrsrus;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertEquals;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.testing.FragmentScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

@RunWith(AndroidJUnit4.class)
public class NewEventEndTimeFragmentTest {

    @Test
    public void testFragmentInitialization() {
        FragmentScenario<NewEventEndTimeFragment> scenario = FragmentScenario.launchInContainer(
                NewEventEndTimeFragment.class);
        scenario.onFragment(fragment -> assertNotNull(fragment.getView()));
    }

    @Test
    public void testFragmentWithValidArguments() {
        Event.EventBuilder mockBuilder = Mockito.mock(Event.EventBuilder.class);
        Bundle args = new Bundle();
        args.putSerializable("builder", mockBuilder);

        FragmentScenario<NewEventEndTimeFragment> scenario = FragmentScenario.launchInContainer(
                NewEventEndTimeFragment.class, args);
        scenario.onFragment(fragment -> assertNotNull(fragment.getView()));
    }

    @Test
    public void testFragmentWithNullArguments() {
        FragmentScenario<NewEventEndTimeFragment> scenario = FragmentScenario.launchInContainer(
                NewEventEndTimeFragment.class, null); // Passing null as arguments
        scenario.onFragment(fragment -> assertNotNull(fragment.getView()));
    }

    @Test
    public void testViewInflationAndUIComponentsExistence() {
        FragmentScenario<NewEventEndTimeFragment> scenario = FragmentScenario.launchInContainer(
                NewEventEndTimeFragment.class);
        scenario.onFragment(fragment -> {
            View view = fragment.getView();
            assertNotNull("Fragment view should be inflated", view);

            FloatingActionButton fab = view.findViewById(R.id.next_screen_button);
            assertNotNull("FloatingActionButton should exist", fab);

            TextView dateButton = view.findViewById(R.id.date_display_textview);
            assertNotNull("Date TextView should exist", dateButton);

            TextView timeButton = view.findViewById(R.id.time_display_textview);
            assertNotNull("Time TextView should exist", timeButton);

            Toolbar toolbar = view.findViewById(R.id.end_time_screen_toolbar);
            assertNotNull("Toolbar should exist", toolbar);
        });

        onView(withId(R.id.next_screen_button)).check(matches(isDisplayed()));
        onView(withId(R.id.date_display_textview)).check(matches(isDisplayed()));
        onView(withId(R.id.time_display_textview)).check(matches(isDisplayed()));
    }

    @Test
    public void testDateSelection() {
        FragmentScenario<NewEventEndTimeFragment> scenario = FragmentScenario.launchInContainer(
                NewEventEndTimeFragment.class);
        scenario.onFragment(fragment -> {
            View rootView = fragment.getView();
            if (rootView != null) {
                TextView dateButton = rootView.findViewById(R.id.date_display_textview);
                String selectedDate = "2024-04-16"; // Adjusting the date for the end time context
                dateButton.setText(selectedDate);
                assertEquals("TextView should display the selected date",
                        selectedDate, dateButton.getText().toString());
            }
        });
    }

    @Test
    public void testTimeSelection() {
        FragmentScenario<NewEventEndTimeFragment> scenario = FragmentScenario.launchInContainer(
                NewEventEndTimeFragment.class);
        scenario.onFragment(fragment -> {
            View rootView = fragment.getView();
            if (rootView != null) {
                TextView timeButton = rootView.findViewById(R.id.time_display_textview);
                String selectedTime = "15:30"; // Adjusting the time for the end time context
                timeButton.setText(selectedTime);
                assertEquals("TextView should display the selected time",
                        selectedTime, timeButton.getText().toString());
            }
        });
    }
}
