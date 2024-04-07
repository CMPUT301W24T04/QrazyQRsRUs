package com.example.qrazyqrsrus;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasChildCount;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.testing.FragmentScenario;
import androidx.test.annotation.UiThreadTest;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.rule.UiThreadTestRule;


import org.junit.Rule;
import org.junit.Test;

import java.util.Objects;

public class HomeFragmentTest {
    @Rule
    public ActivityScenarioRule<MainActivity> rule =
            new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void testFragmentDisplayed() {

        Bundle bundle = getBundle();

        // Launch the fragment with the provided bundle
        FragmentScenario<HomeFragment> scenario =  FragmentScenario.launchInContainer(HomeFragment.class, bundle, R.style.Base_Theme_QrazyQRsRUs);

        scenario.onFragment(new FragmentScenario.FragmentAction<HomeFragment>() {
            @Override
            public void perform(@NonNull HomeFragment homeFragment) {
                onView(withId(R.id.browse_events_button)).check(matches(isDisplayed()));
                onView(withId(R.id.checked_in_events_listview)).check(matches(isDisplayed()));
                onView(withId(R.id.signed_up_events_listview)).check(matches(isDisplayed()));
                onView(withId(R.id.events_textview)).check(matches(isDisplayed()));
                onView(withId(R.id.welcome_message_view)).check(matches(isDisplayed()));
                onView(withId(R.id.checked_in_events_listview)).check(matches(hasChildCount(0)));
                onView(withId(R.id.signed_up_events_listview)).check(matches(hasChildCount(0)));

            }
        });



    }

    @NonNull
    private static Bundle getBundle() {
        Attendee attendee = new Attendee("1", "doc123", "John Doe", "john@example.com", null, true);
        // Create a bundle with necessary data
        Bundle bundle = new Bundle();
        bundle.putSerializable("user", attendee);
        return bundle;
    }

}