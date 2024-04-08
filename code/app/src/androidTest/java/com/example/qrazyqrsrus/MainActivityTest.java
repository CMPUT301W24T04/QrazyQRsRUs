package com.example.qrazyqrsrus;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.espresso.action.ViewActions;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Rule
    public ActivityScenarioRule<MainActivity> activityScenarioRule = new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void navigateToMyEventsFragment() {
        onView(withId(R.id.my_events)).perform(ViewActions.click());
        onView(withId(R.id.new_event_acitivity_nav_host)).check(matches(isDisplayed()));
        //Check that My Events Fragment was opened.
    }

    @Test
    public void navigateToProfileFragment() {
        onView(withId(R.id.profile)).perform(ViewActions.click());
        onView(withId(R.id.update_profile)).check(matches(isDisplayed()));
        // // Check that Profile Fragment was opened.
    }
}
