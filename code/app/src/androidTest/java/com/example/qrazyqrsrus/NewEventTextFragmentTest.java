package com.example.qrazyqrsrus;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isChecked;
import static androidx.test.espresso.matcher.ViewMatchers.isNotChecked;
import static androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import android.os.Bundle;

import androidx.fragment.app.testing.FragmentScenario;
import androidx.lifecycle.Lifecycle;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

@RunWith(AndroidJUnit4.class)
public class NewEventTextFragmentTest {

    @Test
    public void testFragmentCreationWithArguments() {
        // Create a mock EventBuilder with pre-filled data
        Event.EventBuilder mockBuilder = new Event.EventBuilder();
        mockBuilder.setName("Test Event");
        mockBuilder.setLocation("Test Location");
        mockBuilder.setDetails("This is a detailed description of the test event.");

        Bundle args = new Bundle();
        args.putSerializable("builder", mockBuilder);

        FragmentScenario<NewEventTextFragment> scenario = FragmentScenario.launchInContainer(
                NewEventTextFragment.class, args);


        scenario.moveToState(Lifecycle.State.STARTED);

        // Verify that the input fields are populated with the mock data
        onView(withId(R.id.event_name_edit_text)).check(matches(ViewMatchers.withText("Test Event")));
        onView(withId(R.id.event_location_edit_text)).check(matches(ViewMatchers.withText("Test Location")));
        onView(withId(R.id.event_details_edit_text)).check(matches(ViewMatchers.withText("This is a detailed description of the test event.")));
    }

    @Test
    public void testToggleBehaviorAndAttendeeLimitation() {
        // Given
        Event.EventBuilder mockBuilder = new Event.EventBuilder();
        Attendee mockAttendee = new Attendee();
        mockAttendee.setDocumentId("dummyId");

        Bundle args = new Bundle();
        args.putSerializable("builder", mockBuilder);
        args.putSerializable("attendee", mockAttendee);

        FragmentScenario<NewEventTextFragment> scenario = FragmentScenario.launchInContainer(
                NewEventTextFragment.class, args);
        scenario.moveToState(Lifecycle.State.STARTED);

        // When the toggle is off, the attendee limit should be hidden.
        onView(withId(R.id.limit_attendees_toggle)).check(matches(isNotChecked()));
        onView(withId(R.id.max_attendees_edit_text)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)));

        // Toggle the switch on
        onView(withId(R.id.limit_attendees_toggle)).perform(click());

        // Then, the attendee limit should be displayed.
        onView(withId(R.id.limit_attendees_toggle)).check(matches(isChecked()));
        onView(withId(R.id.max_attendees_edit_text)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));

        // Toggle the switch off again
        onView(withId(R.id.limit_attendees_toggle)).perform(click());

        // Then, the attendee limit should be hidden again.
        onView(withId(R.id.limit_attendees_toggle)).check(matches(isNotChecked()));
        onView(withId(R.id.max_attendees_edit_text)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)));
    }

    @Mock
    NavController mockNavController;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockNavController = mock(NavController.class);
    }

    @Test
    public void testNavigationToNextFragment() {
        // Given
        Event.EventBuilder mockBuilder = new Event.EventBuilder();
        Attendee mockAttendee = new Attendee();
        mockAttendee.setDocumentId("dummyId");

        Bundle args = new Bundle();
        args.putSerializable("builder", mockBuilder);
        args.putSerializable("attendee", mockAttendee);

        FragmentScenario<NewEventTextFragment> scenario = FragmentScenario.launchInContainer(
                NewEventTextFragment.class, args);
        scenario.onFragment(fragment ->
                Navigation.setViewNavController(fragment.requireView(), mockNavController)
        );

        // When
        onView(withId(R.id.next_screen_button)).perform(click());

        verify(mockNavController).navigate(
                eq(R.id.action_newEventTextFragment_to_newEventImageFragment2),
                any(Bundle.class)
        );
    }

    @Test
    public void testCancellationFromFragment() {
        // Given
        FragmentScenario<NewEventTextFragment> scenario = FragmentScenario.launchInContainer(
                NewEventTextFragment.class, null);
        scenario.onFragment(fragment ->
                Navigation.setViewNavController(fragment.requireView(), mockNavController)
        );

        // When
        onView(withId(R.id.cancel_button)).perform(click());

        // Then
        verify(mockNavController).navigate(R.id.action_newEventTextFragment_to_newEventFragment);
    }


}