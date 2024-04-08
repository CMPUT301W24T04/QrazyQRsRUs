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

@RunWith(AndroidJUnit4.class)
public class AnnouncementEditTests {


    FirebaseDB mockFirebaseDB = Mockito.mock(FirebaseDB.class);
    NavController mockNavController;
    FragmentScenario<AnnouncementEditFragment> scenario;

    @Rule
    public ActivityScenarioRule<MainActivity> rule =
            new ActivityScenarioRule<>(MainActivity.class);

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockNavController = Mockito.mock(NavController.class);
        Mockito.doAnswer(invocation -> null).when(mockFirebaseDB).updateEvent(any()); // added

        Bundle bundle = getBundle();

        // Launch the fragment with the provided bundle
        scenario = FragmentScenario.launchInContainer(AnnouncementEditFragment.class, bundle, R.style.Base_Theme_QrazyQRsRUs, Lifecycle.State.INITIALIZED);
//        scenario.onFragment(announcementEditFragment -> announcementEditFragment.setFirebaseDB(mockFirebaseDB));
        scenario.onFragment(announcementEditFragment -> scenario.moveToState(Lifecycle.State.RESUMED));
        scenario.onFragment((FragmentScenario.FragmentAction<AnnouncementEditFragment>) announcementEditFragment -> Navigation.setViewNavController(announcementEditFragment.requireView(), mockNavController));

        scenario.onFragment((FragmentScenario.FragmentAction<AnnouncementEditFragment>) announcementEditFragment -> Navigation.setViewNavController(announcementEditFragment.requireView(), mockNavController));
    }

    public Attendee mockAttendee() {
        return new Attendee("1", "OrganizerID", "John Doe", "john@example.com", null, true);
    }

    public static Event getEvent(Integer count) {

        return new Event(null, "Event" + count, "OrganizerID", "Event Details", "Event Location",
                "testStartDate", "testEndDate", true, null, "DIFWDSADW", "DIFWDWDSSADW", null, new ArrayList<String>(), new ArrayList<String>(), new ArrayList<String>(), 2);
    }

    @Test
    public void testAnnouncementAddedToList() {
        // Type text into the announcement edit text
        String typedText = "Test announcement";
        onView(withId(R.id.edit_announcement)).perform(clearText(), typeText(typedText));

        // Click the "Add" button
        onView(withId(R.id.button_add)).perform(click());

        // Check if the typed text is added to the list view
        //onView(withText(typedText)).check(matches(isDisplayed()));
        onData(CoreMatchers.is(CoreMatchers.instanceOf(String.class))).inAdapterView(withId(R.id.list_announcements)).atPosition(0).check(matches((withText(typedText))));

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
