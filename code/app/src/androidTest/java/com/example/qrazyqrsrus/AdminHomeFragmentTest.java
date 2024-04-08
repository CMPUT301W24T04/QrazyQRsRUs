package com.example.qrazyqrsrus;

import androidx.fragment.app.testing.FragmentScenario;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

import android.os.Bundle;

@RunWith(AndroidJUnit4.class)
public class AdminHomeFragmentTest {

    // Use ActivityScenarioRule to launch the container activity for your fragment
//    @Rule
//    public ActivityScenarioRule<MainActivity> activityRule = new ActivityScenarioRule<>(MainActivity.class);

    @Mock
    NavController mockNavController = Mockito.mock(NavController.class);

    @Before
    public void setup() {
        // Launch MainActivity


        // trigger the condition that leads to the display of AdminHomeFragment
    }

    @Test
    public void testNavigationToAdminViewEventsFragment() {
        FragmentScenario<AdminHomeFragment> scenario = FragmentScenario.launchInContainer(AdminHomeFragment.class);
        scenario.onFragment(fragment ->
                Navigation.setViewNavController(fragment.requireView(), mockNavController)
        );

        // Perform click on the "Events" button
        onView(withId(R.id.admin_events_button)).perform(click());

        // Check if the AdminViewEventsFragment's UI is displayed
//        onView(withId(R.id.adminViewEventsFragment)).check(matches(isDisplayed()));

        ArgumentCaptor<Bundle> captor = ArgumentCaptor.forClass(Bundle.class);
        //we verify that we navigate to the next screen
        verify(mockNavController).navigate(
                eq(R.id.action_adminHomeFragment_to_adminViewEventsFragment)
        );
    }

    @Test
    public void testNavigationToAdminViewAttendeesFragment() {
        FragmentScenario<AdminHomeFragment> scenario = FragmentScenario.launchInContainer(AdminHomeFragment.class);
        scenario.onFragment(fragment ->
                Navigation.setViewNavController(fragment.requireView(), mockNavController)
        );

        // Perform click on the "Profiles" button
        onView(withId(R.id.admin_profiles_button)).perform(click());

        // Check if the AdminViewAttendeesFragment's UI is displayed
//        onView(withId(R.id.adminViewAttendeesFragment)).check(matches(isDisplayed()));
        ArgumentCaptor<Bundle> captor = ArgumentCaptor.forClass(Bundle.class);
        //we verify that we navigate to the next screen
        verify(mockNavController).navigate(
                eq(R.id.action_adminHomeFragment_to_adminViewAttendeesFragment)
        );
    }

    @Test
    public void testNavigationToAdminViewImagesFragment() {
        FragmentScenario<AdminHomeFragment> scenario = FragmentScenario.launchInContainer(AdminHomeFragment.class);
        scenario.onFragment(fragment ->
                Navigation.setViewNavController(fragment.requireView(), mockNavController)
        );

        // Perform click on the "Profiles" button
        onView(withId(R.id.admin_images_button)).perform(click());

        // Check if the AdminViewAttendeesFragment's UI is displayed
//        onView(withId(R.id.adminViewImagesFragment)).check(matches(isDisplayed()));
        ArgumentCaptor<Bundle> captor = ArgumentCaptor.forClass(Bundle.class);
        //we verify that we navigate to the next screen
        verify(mockNavController).navigate(
                eq(R.id.action_adminHomeFragment_to_adminViewAttendeesFragment)
        );
    }
}
