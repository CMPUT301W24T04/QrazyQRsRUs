package com.example.qrazyqrsrus;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static com.google.common.base.CharMatcher.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.core.AllOf.allOf;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.testing.FragmentScenario;
import androidx.lifecycle.Lifecycle;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.test.espresso.ViewAssertion;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;

import junit.framework.TestCase;

import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.Arrays;

@RunWith(AndroidJUnit4.class)
public class AnnouncementViewTests {

    NavController mockNavController;
    FragmentScenario<AnnouncementsFragment> scenario;

    @Rule
    public ActivityScenarioRule<MainActivity> rule =
            new ActivityScenarioRule<>(MainActivity.class);

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockNavController = Mockito.mock(NavController.class);

        Bundle bundle = getBundle();

        // Launch the fragment with the provided bundle
        scenario = FragmentScenario.launchInContainer(AnnouncementsFragment.class, bundle, R.style.Base_Theme_QrazyQRsRUs, Lifecycle.State.INITIALIZED);
        scenario.onFragment(announcementsFragment -> scenario.moveToState(Lifecycle.State.RESUMED));
        scenario.onFragment((FragmentScenario.FragmentAction<AnnouncementsFragment>) announcementsFragment -> Navigation.setViewNavController(announcementsFragment.requireView(), mockNavController));

        scenario.onFragment((FragmentScenario.FragmentAction<AnnouncementsFragment>) announcementsFragment -> Navigation.setViewNavController(announcementsFragment.requireView(), mockNavController));
    }

    public static Event getEvent(Integer count) {
        return new Event(null, "Event" + count, "OrganizerID", "Event Details", "Event Location",
                "testStartDate", "testEndDate", true, null, "DIFWDSADW", "DIFWDWDSSADW", null, new ArrayList<String>(Arrays.asList("Announcement 1", "Announcement 2")), new ArrayList<String>(), new ArrayList<String>(), 2);
    }

    @Test
    public void testAnnouncementsDisplayed() {
        // Check if the ListView is displayed
        onView(withId(R.id.list_announcements)).check(matches(isDisplayed()));

        onView(withId(R.id.button_back)).check(matches(isDisplayed()));

        onData(CoreMatchers.is(CoreMatchers.instanceOf(String.class))).inAdapterView(withId(R.id.list_announcements)).atPosition(0).check(matches((withText("Announcement 1"))));
        onData(CoreMatchers.is(CoreMatchers.instanceOf(String.class))).inAdapterView(withId(R.id.list_announcements)).atPosition(1).check(matches((withText("Announcement 2"))));
    }

    @Test
    public void testBackButtonClicked() {
        // Click on the back button
        onView(withId(R.id.button_back)).perform(click());

        // Verify that NavController navigates back
        verify(mockNavController).popBackStack();
    }

    @NonNull
    private static Bundle getBundle() {
        Event event = getEvent(1);
        // Create a bundle with necessary data
        Bundle bundle = new Bundle();
        bundle.putSerializable("event", event);
        bundle.putBoolean("isCheckedIn", false);
        return bundle;
    }
}
