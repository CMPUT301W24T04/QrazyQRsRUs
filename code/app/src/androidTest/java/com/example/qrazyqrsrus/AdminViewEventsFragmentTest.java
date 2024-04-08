package com.example.qrazyqrsrus;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.core.IsNot.not;

import androidx.test.ext.junit.rules.ActivityScenarioRule;


import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class AdminViewEventsFragmentTest {

    @Rule
    public ActivityScenarioRule<MainActivity> activityRule = new ActivityScenarioRule<>(MainActivity.class);

    private List<Event> mockEvents;

    @Before
    public void setup() {
        mockEvents = new ArrayList<>();
        // Example dates
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime plusOneHour = now.plusHours(1);

        // Initialize some mock events using the correct constructor
        mockEvents.add(new Event("Event 1", "org1", "Detail 1", "Location 1", now, plusOneHour, 100));
        mockEvents.add(new Event("Event 2", "org2", "Detail 2", "Location 2", now, plusOneHour, 200));
        mockEvents.add(new Event("Event 3", "org3", "Detail 3", "Location 3", now, plusOneHour, 300));

        // navigate to the fragment
    }

    @Test
    public void testEventInformationDisplayed() {
        // Assume the fragment is navigated to and the first event is displayed
        onView(withId(R.id.event_detail_name)).check(matches(withText(containsString("Event 1"))));
        onView(withId(R.id.event_detail_location)).check(matches(withText(containsString("Location 1"))));
        onView(withId(R.id.event_detail_details)).check(matches(withText(containsString("Detail 1"))));
        onView(withId(R.id.event_detail_start_date)).check(matches(withText(containsString("Start Date 1"))));
        onView(withId(R.id.event_detail_end_date)).check(matches(withText(containsString("End Date 1"))));

        // Additional checks for organizer name and poster view might require mocking FirebaseDB responses
    }

    @Test
    public void testNavigationBetweenEvents() {
        //assuming the first event is displayed
        // Check the initial event name to ensure we start from a known state
        onView(withId(R.id.event_detail_name)).check(matches(withText(containsString("Event 1"))));

        // Navigate to the next event
        onView(withId(R.id.next_event_button)).perform(click());

        // Verify the details of the second event are displayed
        onView(withId(R.id.event_detail_name)).check(matches(withText(containsString("Event 2"))));

        // Navigate back to the first event
        onView(withId(R.id.previous_event_button)).perform(click());

        // Verify the details of the first event are displayed again
        onView(withId(R.id.event_detail_name)).check(matches(withText(containsString("Event 1"))));
    }

    @Test
    public void testDeleteConfirmationVisibilityToggle() {

        // Press the delete button
        onView(withId(R.id.delete_button)).perform(click());

        // Check if the confirm and cancel buttons are displayed
        onView(withId(R.id.confirm_button)).check(matches(isDisplayed()));
        onView(withId(R.id.cancel_button)).check(matches(isDisplayed()));

        // Optionally, check if the confirm text view is also displayed
        onView(withId(R.id.confirm_text_view)).check(matches(isDisplayed()));

        // Press the cancel button to revert the UI to its original state
        onView(withId(R.id.cancel_button)).perform(click());

        onView(withId(R.id.confirm_button)).check(matches(not(isDisplayed())));
        onView(withId(R.id.cancel_button)).check(matches(not(isDisplayed())));
        // The delete button should be visible again
        onView(withId(R.id.delete_button)).check(matches(isDisplayed()));
    }


}
