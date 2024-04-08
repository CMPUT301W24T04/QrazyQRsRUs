package com.example.qrazyqrsrus;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasChildCount;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.espresso.action.ViewActions.click;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.testing.FragmentScenario;
import androidx.lifecycle.Lifecycle;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;


public class AttendeeListUITest {

    FirebaseDB mockFirebaseDB = Mockito.mock(FirebaseDB.class);
    ArrayList<Attendee> MockAttendeeDataList = new ArrayList<>();
    AttendeeListAdapter MockAttendeeListAdapter;

    @Mock
    NavController mockNavController = Mockito.mock(NavController.class);

    private Attendee mockAttendee(){
        return new Attendee("1", "DocumentId", "John", "john@ualberta.ca", "111111111111111", true,8);
    }
    @Test
    public void canDisplay() {

        Bundle bundle = getBundle();

        Mockito.doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                Object[] args = invocation.getArguments();
                ArrayList<Attendee> attendeeDataList = (ArrayList<Attendee>) args[1];
                AttendeeListAdapter attendeeListAdapter = (AttendeeListAdapter) args[2];

                attendeeListAdapter.setAdapterFirebaseDB(mockFirebaseDB);
                attendeeListAdapter.notifyDataSetChanged();
                return null;
            }
        }).when(mockFirebaseDB).getEventCheckedInUsers(Mockito.any(),Mockito.any(),Mockito.any());
        Mockito.doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                Object[] args = invocation.getArguments();
                FirebaseDB.GetStringCallBack callBack = (FirebaseDB.GetStringCallBack) args[1];
                callBack.onResult("Name");
                return null;
            }
        }).when(mockFirebaseDB).getUserName(Mockito.any(),Mockito.any());


        // Launch the fragment with the provided bundle
        FragmentScenario<AttendeeList> scenario =  FragmentScenario.launchInContainer(AttendeeList.class, bundle, R.style.Base_Theme_QrazyQRsRUs, Lifecycle.State.INITIALIZED);

        scenario.onFragment(fragment ->
                fragment.setFirebaseDB(mockFirebaseDB)
        );
        scenario.onFragment(fragment ->
                scenario.moveToState(Lifecycle.State.RESUMED)
        );

//        Mockito.doNothing().when(mockFirebase).getEventCheckedInUsers(new Event(), MockAttendeeDataList, MockAttendeeListAdapter);
//        Mockito.doNothing().when(mockFirebaseDB).getEventCheckedInUsers(Mockito.any(),Mockito.any(),Mockito.any());
        mockAttendee().setCheckins(8);
        onView(withId(R.id.button_back_checkin)).check(matches(isDisplayed()));
        onView(withId(R.id.app_title)).check(matches(isDisplayed()));
        onView(withId(R.id.attendee_list_view)).check(matches(isDisplayed()));
        onView(withId(R.id.attendee_list_view)).check(matches(hasChildCount(0)));
    }
    @Test
    public void testFragmentDisplayedAddAttendee() {

        Bundle bundle = getBundle();

        Mockito.doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                Object[] args = invocation.getArguments();
                ArrayList<Attendee> attendeeDataList = (ArrayList<Attendee>) args[1];
                AttendeeListAdapter attendeeListAdapter = (AttendeeListAdapter) args[2];

                attendeeDataList.add(mockAttendee());
                attendeeListAdapter.setAdapterFirebaseDB(mockFirebaseDB);
                attendeeListAdapter.notifyDataSetChanged();
                return null;
            }
        }).when(mockFirebaseDB).getEventCheckedInUsers(Mockito.any(),Mockito.any(),Mockito.any());
        Mockito.doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                Object[] args = invocation.getArguments();
                FirebaseDB.GetStringCallBack callBack = (FirebaseDB.GetStringCallBack) args[1];
                callBack.onResult("Name");
                return null;
            }
        }).when(mockFirebaseDB).getUserName(Mockito.any(),Mockito.any());


        // Launch the fragment with the provided bundle
        FragmentScenario<AttendeeList> scenario =  FragmentScenario.launchInContainer(AttendeeList.class, bundle, R.style.Base_Theme_QrazyQRsRUs, Lifecycle.State.INITIALIZED);

        scenario.onFragment(fragment ->
                fragment.setFirebaseDB(mockFirebaseDB)
        );
        scenario.onFragment(fragment ->
                scenario.moveToState(Lifecycle.State.RESUMED)
        );

//        Mockito.doNothing().when(mockFirebase).getEventCheckedInUsers(new Event(), MockAttendeeDataList, MockAttendeeListAdapter);
//        Mockito.doNothing().when(mockFirebaseDB).getEventCheckedInUsers(Mockito.any(),Mockito.any(),Mockito.any());
        mockAttendee().setCheckins(8);
        onView(withId(R.id.button_back_checkin)).check(matches(isDisplayed()));
        onView(withId(R.id.app_title)).check(matches(isDisplayed()));
        onView(withId(R.id.attendee_list_view)).check(matches(isDisplayed()));
        onView(withText("Name")).check(matches(isDisplayed()));
        onView(withText("# Check Ins: 8")).check(matches(isDisplayed()));
        onView(withId(R.id.attendee_list_view)).check(matches(hasChildCount(1)));
    }
    @Test
    public void TestSwapViewBackButton(){
        Bundle bundle = getBundle();
        Mockito.doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                Object[] args = invocation.getArguments();
                ArrayList<Attendee> attendeeDataList = (ArrayList<Attendee>) args[1];
                AttendeeListAdapter attendeeListAdapter = (AttendeeListAdapter) args[2];

                attendeeDataList.add(mockAttendee());
                attendeeListAdapter.setAdapterFirebaseDB(mockFirebaseDB);
                attendeeListAdapter.notifyDataSetChanged();
                return null;
            }
        }).when(mockFirebaseDB).getEventCheckedInUsers(Mockito.any(),Mockito.any(),Mockito.any());
        Mockito.doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                Object[] args = invocation.getArguments();
                FirebaseDB.GetStringCallBack callBack = (FirebaseDB.GetStringCallBack) args[1];
                callBack.onResult("Name");
                return null;
            }
        }).when(mockFirebaseDB).getUserName(Mockito.any(),Mockito.any());
        FragmentScenario<AttendeeList> scenario =  FragmentScenario.launchInContainer(AttendeeList.class, bundle, R.style.Base_Theme_QrazyQRsRUs, Lifecycle.State.INITIALIZED);
        scenario.onFragment(fragment ->
                fragment.setFirebaseDB(mockFirebaseDB)
        );
        scenario.onFragment(fragment ->
                scenario.moveToState(Lifecycle.State.RESUMED)
        );

        scenario.onFragment(fragment ->
                Navigation.setViewNavController(fragment.requireView(), mockNavController)
        );
        onView(withId(R.id.button_back_checkin)).perform(click());
        verify(mockNavController).navigate(
                eq(R.id.action_attendeeList2_to_eventDetailsFragment),
                Mockito.any(Bundle.class)
        );
//        onView(withId(R.id.eventDetailsFragment)).check(matches(isDisplayed()));
    }
    @Test
    public void TestSwapViewSignupsButton(){
        Bundle bundle = getBundle();
        Mockito.doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                Object[] args = invocation.getArguments();
                ArrayList<Attendee> attendeeDataList = (ArrayList<Attendee>) args[1];
                AttendeeListAdapter attendeeListAdapter = (AttendeeListAdapter) args[2];

                attendeeDataList.add(mockAttendee());
                attendeeListAdapter.setAdapterFirebaseDB(mockFirebaseDB);
                attendeeListAdapter.notifyDataSetChanged();
                return null;
            }
        }).when(mockFirebaseDB).getEventCheckedInUsers(Mockito.any(),Mockito.any(),Mockito.any());
        Mockito.doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                Object[] args = invocation.getArguments();
                FirebaseDB.GetStringCallBack callBack = (FirebaseDB.GetStringCallBack) args[1];
                callBack.onResult("Name");
                return null;
            }
        }).when(mockFirebaseDB).getUserName(Mockito.any(),Mockito.any());
        FragmentScenario<AttendeeList> scenario =  FragmentScenario.launchInContainer(AttendeeList.class, bundle, R.style.Base_Theme_QrazyQRsRUs, Lifecycle.State.INITIALIZED);
        scenario.onFragment(fragment ->
                fragment.setFirebaseDB(mockFirebaseDB)
        );
        scenario.onFragment(fragment ->
                scenario.moveToState(Lifecycle.State.RESUMED)
        );

        scenario.onFragment(fragment ->
                Navigation.setViewNavController(fragment.requireView(), mockNavController)
        );
        onView(withId(R.id.button_view_signups)).perform(click());
        verify(mockNavController).navigate(
                eq(R.id.action_attendeeList2_to_attendeeSignupsList),
                Mockito.any(Bundle.class)
        );
//        onView(withId(R.id.eventDetailsFragment)).check(matches(isDisplayed()));
    }

//    @Test
//    public void TestSwapViewProfileFragment(){
//        Bundle bundle = getBundle();
//        Mockito.doAnswer(new Answer() {
//            @Override
//            public Object answer(InvocationOnMock invocation) throws Throwable {
//                Object[] args = invocation.getArguments();
//                ArrayList<Attendee> attendeeDataList = (ArrayList<Attendee>) args[1];
//                AttendeeListAdapter attendeeListAdapter = (AttendeeListAdapter) args[2];
//
//                attendeeDataList.add(mockAttendee());
//                attendeeListAdapter.setAdapterFirebaseDB(mockFirebaseDB);
//                attendeeListAdapter.notifyDataSetChanged();
//                return null;
//            }
//        }).when(mockFirebaseDB).getEventCheckedInUsers(Mockito.any(),Mockito.any(),Mockito.any());
//        Mockito.doAnswer(new Answer() {
//            @Override
//            public Object answer(InvocationOnMock invocation) throws Throwable {
//                Object[] args = invocation.getArguments();
//                FirebaseDB.GetStringCallBack callBack = (FirebaseDB.GetStringCallBack) args[1];
//                callBack.onResult("Name");
//                return null;
//            }
//        }).when(mockFirebaseDB).getUserName(Mockito.any(),Mockito.any());
//        FragmentScenario<AttendeeList> scenario =  FragmentScenario.launchInContainer(AttendeeList.class, bundle, R.style.Base_Theme_QrazyQRsRUs, Lifecycle.State.INITIALIZED);
//        scenario.onFragment(fragment ->
//                fragment.setFirebaseDB(mockFirebaseDB)
//        );
//        scenario.onFragment(fragment ->
//                scenario.moveToState(Lifecycle.State.RESUMED)
//        );
//
//        scenario.onFragment(fragment ->
//                Navigation.setViewNavController(fragment.requireView(), mockNavController)
//        );
//        onView(withId(R.id.button_view_signups)).perform(click());
//        verify(mockNavController).navigate(
//                eq(R.id.action_attendeeList2_to_viewProfileFragment),
//                Mockito.any(Bundle.class)
//        );
////        onView(withId(R.id.eventDetailsFragment)).check(matches(isDisplayed()));
//    }

    @NonNull
    private static Bundle getBundle() {
        ArrayList<String> announcements = new ArrayList<>();
        ArrayList<String> signUps = new ArrayList<>();
        ArrayList<String> checkIns = new ArrayList<>();
        checkIns.add("uHWRJuNzQmzjXa1dfleb");
        Event event = new Event("1111111", "John", "222222222", "This is an event",
                "Edmonton", "4/6/2024", "4/8/2024",
                true, "path", "qrCode",
                "promoQr", "organizerToken",announcements,signUps,
                checkIns, 10);
        // Create a bundle with necessary data
        Bundle bundle = new Bundle();
        bundle.putSerializable("event", event);
        return bundle;
    }
    @NonNull
    private static Bundle getAttendeeBundle() {
        Attendee attendee = new Attendee("1", "DocumentId", "John", "john@ualberta.ca", "111111111111111", true,8);
        // Create a bundle with necessary data
        Bundle bundle = new Bundle();
        bundle.putSerializable("attendee", attendee);
        return bundle;
    }

}
