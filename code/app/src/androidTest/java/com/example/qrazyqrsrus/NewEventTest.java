package com.example.qrazyqrsrus;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static
        androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.anything;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.action.ViewActions;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
public class NewEventTest {
    @Rule
    public ActivityScenarioRule<MainActivity> scenario = new
            ActivityScenarioRule<MainActivity>(MainActivity.class);

    @Test
    public void testChangeFragment(){
        onView(withId(R.id.my_events)).perform(click());
        onView(withId(R.id.new_event_acitivity_nav_host)).check(matches(isDisplayed()));
    }

    @Test
    public void testSelectNewEventFloatingActionButton(){
        onView(withId(R.id.my_events)).perform(click());
        onView(withId(R.id.new_event_button)).perform(click());
        onView(withId(R.id.new_event_text_fragment)).check(matches(isDisplayed()));
    }

    @Test
    public void testEventCreationWithInput(){
        onView(withId(R.id.my_events)).perform(click());
        onView(withId(R.id.new_event_button)).perform(click());
        onView(withId(R.id.event_name_edit_text)).perform(ViewActions.typeText("my new event"));
        onView(withId(R.id.event_location)).perform(ViewActions.typeText("treehouse"));
        onView(withId(R.id.event_details)).perform(ViewActions.typeText("it's a birthday party!"));
        onView(withId(R.id.next_screen_button)).perform(click());
        //we skip selecting a date and time - android's built in time and date picker
        onView(withId(R.id.next_screen_button)).perform(click());
        onView(withId(R.id.next_screen_button)).perform(click());
        //we do not upload a photo - android's built-in photo picker
        onView(withId(R.id.next_screen_button)).perform(click());
        onView(withId(R.id.new_event_generate_qr_button)).perform(click());
        onView(withId(R.id.next_screen_button)).perform(click());
        onView(withId(R.id.new_event_generate_qr_button)).perform(click());

        //check that we are brought back to our list of events screen
        onView(withId(R.id.event_list_view)).check(matches(isDisplayed()));
        //check that there is an entry with the new event we created
        onView(withText("my new event")).check(matches(isDisplayed()));
    }

}
