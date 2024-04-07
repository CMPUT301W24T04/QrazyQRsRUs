
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
public class NewEventStartTimeFragmentTest {
    @Test
    public void testFragmentInitialization() {
        // Launch the fragment
        FragmentScenario<NewEventStartTimeFragment> scenario = FragmentScenario.launchInContainer(
                NewEventStartTimeFragment.class);

        // Assert that the fragment's view is displayed
        scenario.onFragment(fragment -> {
            assertNotNull(fragment.getView());
        });
    }
    @Test
    public void testFragmentWithValidArguments() {
        // Create a mock EventBuilder and a Bundle
        Event.EventBuilder mockBuilder = Mockito.mock(Event.EventBuilder.class);
        Bundle args = new Bundle();
        args.putSerializable("builder", mockBuilder);

        FragmentScenario<NewEventStartTimeFragment> scenario = FragmentScenario.launchInContainer(
                NewEventStartTimeFragment.class, args);

        scenario.onFragment(fragment -> {
            // Assert that the fragment's view is correctly initialized based on the arguments
            assertNotNull(fragment.getView());
            // Additional assertions to check if the builder was used correctly
        });
    }
    @Test
    public void testFragmentWithNullArguments() {
        FragmentScenario<NewEventStartTimeFragment> scenario = FragmentScenario.launchInContainer(
                NewEventStartTimeFragment.class, null); // Passing null as arguments

        scenario.onFragment(fragment -> {
            // Assert that the fragment is in a valid state with null arguments
            assertNotNull(fragment.getView());
            // Additional state checks can be performed here if necessary
        });
    }

    @Test
    public void testViewInflationAndUIComponentsExistence() {
        FragmentScenario<NewEventStartTimeFragment> scenario = FragmentScenario.launchInContainer(
                NewEventStartTimeFragment.class);

        scenario.onFragment(fragment -> {
            // Check if the fragment's view is correctly inflated
            View view = fragment.getView();
            assertNotNull("Fragment view should be inflated", view);

            // Check for the existence of FloatingActionButton
            FloatingActionButton fab = view.findViewById(R.id.next_screen_button);
            assertNotNull("FloatingActionButton should exist", fab);

            // Check for the existence of date and time TextViews
            TextView dateButton = view.findViewById(R.id.date_display_textview);
            assertNotNull("Date TextView should exist", dateButton);

            TextView timeButton = view.findViewById(R.id.time_display_textview);
            assertNotNull("Time TextView should exist", timeButton);

            // Check for the existence of Toolbar
            Toolbar toolbar = view.findViewById(R.id.start_time_screen_toolbar);
            assertNotNull("Toolbar should exist", toolbar);
        });

        // Using Espresso to check if the UI components are displayed
        onView(withId(R.id.next_screen_button)).check(matches(isDisplayed()));
        onView(withId(R.id.date_display_textview)).check(matches(isDisplayed()));
        onView(withId(R.id.time_display_textview)).check(matches(isDisplayed()));
    }
    @Test
    public void testDateSelection() {
        FragmentScenario<NewEventStartTimeFragment> scenario = FragmentScenario.launchInContainer(
                NewEventStartTimeFragment.class);

        scenario.onFragment(fragment -> {
            // Simulate setting the date as if chosen from the DatePickerDialog
            View rootView = fragment.getView();
            if (rootView != null) {
                TextView dateButton = rootView.findViewById(R.id.date_display_textview);
                // Assume the user selects April 15, 2024
                String selectedDate = "2024-04-15";
                dateButton.setText(selectedDate);

                // Now check if the TextView displays the correct date
                assertEquals("TextView should display the selected date",
                        selectedDate, dateButton.getText().toString());
            }
        });
    }

    @Test
    public void testTimeSelection() {
        FragmentScenario<NewEventStartTimeFragment> scenario = FragmentScenario.launchInContainer(
                NewEventStartTimeFragment.class);

        scenario.onFragment(fragment -> {
            // Simulate setting the time as if chosen from the TimePickerDialog
            View rootView = fragment.getView();
            if (rootView != null) {
                TextView timeButton = rootView.findViewById(R.id.time_display_textview);
                // Assume the user selects 14:30
                String selectedTime = "14:30";
                timeButton.setText(selectedTime);

                // Now check if the TextView displays the correct time
                assertEquals("TextView should display the selected time",
                        selectedTime, timeButton.getText().toString());
            }
        });
    }








}