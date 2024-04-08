package com.example.qrazyqrsrus;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import static org.hamcrest.Matchers.not;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.qrazyqrsrus.MainActivity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class AdminViewImagesFragmentTest {

    @Rule
    public ActivityScenarioRule<MainActivity> activityRule = new ActivityScenarioRule<>(MainActivity.class);

    @Before
    public void setup() {
        // navigate to the fragment
        // also not sure how to create sample images
    }

    @Test
    public void testImageIsDisplayed() {
        // This test assumes the fragment initially has at least one image to display.
        onView(withId(R.id.image_view)).check(matches(isDisplayed()));
    }

    @Test
    public void testNavigationBetweenImages() {
        // Navigate to the next image
        onView(withId(R.id.next_event_button)).perform(click());
        // Perform checks to verify the image has changed (this may require mock data or specific assertions to validate the change)

        // Navigate back to the previous image
        onView(withId(R.id.previous_event_button)).perform(click());
        // Perform checks similar to above to verify the image has changed back
    }

    @Test
    public void testDeleteConfirmationVisibilityToggle() {
        // Press the delete button
        onView(withId(R.id.delete_button)).perform(click());

        // Check if the confirm and cancel buttons are displayed
        onView(withId(R.id.confirm_button)).check(matches(isDisplayed()));
        onView(withId(R.id.cancel_button)).check(matches(isDisplayed()));

        // Press the cancel button to revert the UI to its original state
        onView(withId(R.id.cancel_button)).perform(click());

        // Verify that the confirm and cancel buttons are no longer displayed
        onView(withId(R.id.confirm_button)).check(matches(not(isDisplayed())));
        onView(withId(R.id.cancel_button)).check(matches(not(isDisplayed())));
    }
}
