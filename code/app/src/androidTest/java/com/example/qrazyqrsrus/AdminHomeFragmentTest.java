package com.example.qrazyqrsrus;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class AdminHomeFragmentTest {

    // Use ActivityScenarioRule to launch the container activity for your fragment
    @Rule
    public ActivityScenarioRule<MainActivity> activityRule = new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void testNavigationToAdminViewEventsFragment() {
        // Perform click on the "Events" button
        onView(withId(R.id.admin_events_button)).perform(click());

        // Check if the AdminViewEventsFragment's UI is displayed
        onView(withId(R.id.adminViewEventsFragment)).check(matches(isDisplayed()));
    }

    @Test
    public void testNavigationToAdminViewAttendeesFragment() {
        // Perform click on the "Profiles" button
        onView(withId(R.id.admin_profiles_button)).perform(click());

        // Check if the AdminViewAttendeesFragment's UI is displayed
        onView(withId(R.id.adminViewAttendeesFragment)).check(matches(isDisplayed()));
    }

    @Test
    public void testNavigationToAdminViewImagesFragment() {
        // Perform click on the "Profiles" button
        onView(withId(R.id.admin_images_button)).perform(click());

        // Check if the AdminViewAttendeesFragment's UI is displayed
        onView(withId(R.id.adminViewImagesFragment)).check(matches(isDisplayed()));
    }
}
