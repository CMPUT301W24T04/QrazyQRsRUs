package com.example.qrazyqrsrus;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.hamcrest.CoreMatchers.not;

import android.os.Bundle;

import androidx.fragment.app.testing.FragmentScenario;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class EventDetailsFragmentTest {

    private FragmentScenario<EventDetailsFragment> fragmentScenario;
    private Event event;
    private Attendee attendee;
    @Before
    public void setUp() {
        // Initialize the fragmentScenario
        fragmentScenario = FragmentScenario.launchInContainer(EventDetailsFragment.class);
        this.event = new Event(null, "Test Event", "OrganizerID", "Event Details", "Event Location",
                "testStartDate", "testEndDate", true, null, null, null, null, null, null, null, 2);;
        this.attendee = new Attendee("1", "doc123", "John Doe", "john@example.com", "path/to/profile.jpg", true);

    }


    @Test
    public void testFragmentDisplayed() {
        fragmentScenario.onFragment(fragment -> {
            Bundle args = new Bundle();
            args.putSerializable("event", this.event);
            args.putSerializable("attendee", this.attendee);
            args.putSerializable("isCheckIn", false);
            fragment.setArguments(args);
        });

        onView(withId(R.id.event_detail_name)).check(matches(isDisplayed()));
        onView(withId(R.id.event_detail_organizer)).check(matches(isDisplayed()));
        onView(withId(R.id.event_detail_details)).check(matches(isDisplayed()));
        onView(withId(R.id.event_detail_location)).check(matches(isDisplayed()));
        onView(withId(R.id.event_detail_start_date)).check(matches(isDisplayed()));
        onView(withId(R.id.event_detail_end_date)).check(matches(isDisplayed()));

        onView(withId(R.id.sign_up_button)).check(matches(isDisplayed()));
        onView(withId(R.id.check_in_share_button)).check(matches(not(isDisplayed())));

        onView(withId(R.id.event_detail_name)).check(matches(withText("Test Event")));
        onView(withId(R.id.event_detail_location)).check(matches(withText("Event Location")));


    }
}
