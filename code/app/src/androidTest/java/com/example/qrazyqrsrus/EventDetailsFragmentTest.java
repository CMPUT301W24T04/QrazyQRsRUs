package com.example.qrazyqrsrus;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.hamcrest.CoreMatchers.not;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.testing.FragmentScenario;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class EventDetailsFragmentTest{

    @Rule
    public ActivityScenarioRule<MainActivity> rule =
           new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void testFragmentDisplayed() {

        Bundle bundle = getBundle();

        // Launch the fragment with the provided bundle
        FragmentScenario<EventDetailsFragment> scenario =  FragmentScenario.launchInContainer(EventDetailsFragment.class, bundle);

        scenario.onFragment(new FragmentScenario.FragmentAction<EventDetailsFragment>() {
            @Override
            public void perform(@NonNull EventDetailsFragment eventDetailsFragment) {
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
        });



    }

    @NonNull
    private static Bundle getBundle() {
        Event event = new Event(null, "Test Event", "OrganizerID", "Event Details", "Event Location",
                "testStartDate", "testEndDate", true, null, "Latest DJ set by Johnny T!!_20240404_160546_checkin", "Latest DJ set by Johnny T!!_20240404_160543_checkin", "ef4Mg7oXT7CtkAdRDOywWx:APA91bG0XrsWT2P6nigSrEhVzkVkrYTERHEtOa54Ay_ghGgG3yOpT6DtjZAqFHSVCvGwtW9M8oFdLddo0wlYYSt4Vhbhn8hIATCEBzrm-SykGojtdX50O_BmtFnP0Kd7ApVBFAnDkGB1", null, null, null, 2);
        ;
        Attendee attendee = new Attendee("1", "doc123", "John Doe", "john@example.com", null, true);
        // Create a bundle with necessary data
        Bundle bundle = new Bundle();
        bundle.putSerializable("event", event);
        bundle.putSerializable("attendee", attendee);
        bundle.putBoolean("isCheckedIn", false);
        return bundle;
    }
}
