package com.example.qrazyqrsrus;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.espresso.matcher.ViewMatchers.isChecked;
import static org.hamcrest.Matchers.not;


import android.view.View;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;


@RunWith(AndroidJUnit4.class)
public class AdminViewAttendeesFragmentTest {

    private List<Attendee> allAttendees;

    @Rule
    public ActivityScenarioRule<MainActivity> activityRule = new ActivityScenarioRule<>(MainActivity.class);

    @Before
    public void setup() {
        allAttendees = new ArrayList<>();
        Attendee attendee1 = new Attendee("1", "doc1", "John Doe", "john.doe@example.com", "/path/to/profile1.jpg", true);
        Attendee attendee2 = new Attendee("2", "doc2", "Jane Smith", "jane.smith@example.com", "/path/to/profile2.jpg", false);
        Attendee attendee3 = new Attendee("3", "doc3", "Alex Johnson", "alex.johnson@example.com", "/path/to/profile3.jpg", true);
        allAttendees.add(attendee1);
        allAttendees.add(attendee2);
        allAttendees.add(attendee3);

        // Launch MainActivity
        ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class);

        // trigger the condition that leads to the display of AdminHomeFragment
        // also supply allAttendees to the fragment instance
    }

    @Test
    public void testNavigationBackButton() {
        // this is the FAB
        onView(withId(R.id.back_button)).perform(click());
        // add check for previous event display
    }

    @Test
    public void testAttendeeInformationDisplayed() {
        // Assume the first attendee's name and email to check.
        onView(withId(R.id.etFullName)).check(ViewAssertions.matches(withText("Expected Name")));
        onView(withId(R.id.etEmailAddress)).check(ViewAssertions.matches(withText("expected.email@example.com")));
    }

    @Test
    public void testDeleteConfirmationVisibilityToggle() {
        onView(withId(R.id.delete_button)).perform(click());
        onView(withId(R.id.cancel_button)).check(ViewAssertions.matches(isDisplayed()));
        onView(withId(R.id.confirm_button)).check(ViewAssertions.matches(isDisplayed()));
    }

    @Test
    public void testGeolocationSwitchReflectsAttendeeStatus() {
        // Assuming you have a way to set an attendee with geolocation ON.
        onView(withId(R.id.switchGeolocation)).check(matches(isChecked()));
    }

    @Test
    public void testNextPreviousButtonNavigation() {
        // Assume allAttendees list is populated with at least 2 mock attendees.

        // Record the initial attendee's name to compare after navigation.
        final String[] initialAttendeeName = new String[1];
        onView(withId(R.id.etFullName)).perform(new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return isDisplayed();
            }

            @Override
            public String getDescription() {
                return "Getting text from the nameView EditText";
            }

            @Override
            public void perform(UiController uiController, View view) {
                EditText nameView = (EditText) view;
                initialAttendeeName[0] = nameView.getText().toString();
            }
        });

        // Click on the next button to navigate to the next attendee.
        onView(withId(R.id.next_event_button)).perform(click());

        // Verify that the displayed name is not the same as the initial name, indicating navigation.
        onView(withId(R.id.etFullName)).check(matches(not(withText(initialAttendeeName[0]))));

        // Click on the previous button to navigate back to the first attendee.
        onView(withId(R.id.previous_event_button)).perform(click());

        // Verify that the displayed name is the same as the initial name, indicating successful navigation back.
        onView(withId(R.id.etFullName)).check(matches(withText(initialAttendeeName[0])));
    }


}
