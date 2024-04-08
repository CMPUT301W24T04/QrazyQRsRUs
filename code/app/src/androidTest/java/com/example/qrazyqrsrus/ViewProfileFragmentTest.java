package com.example.qrazyqrsrus;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isEnabled;
import static androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import android.net.Uri;
import android.os.Bundle;
import android.widget.Switch;

import androidx.fragment.app.testing.FragmentScenario;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

@RunWith(AndroidJUnit4.class)
public class ViewProfileFragmentTest {

    @Mock
    FirebaseStorage mockFirebaseStorage; // Declare mock for FirebaseStorage
    @Mock
    FirebaseFirestore mockFirebaseFirestore; // Declare mock for FirebaseFirestore

    private Uri mockImageUri;
    private FragmentScenario<ViewProfileFragment> scenario;
    @Before
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockImageUri = Uri.parse("android.resource://com.example.qrazyqrsrus/drawable/test_image");

        scenario = FragmentScenario.launchInContainer(ViewProfileFragment.class, null, R.style.Base_Theme_QrazyQRsRUs);
        scenario.onFragment(fragment -> {
//            fragment.setFirebaseStorage(mockFirebaseStorage);
//            fragment.setFirebaseFirestore(mockFirebaseFirestore);
        });

    }
    @Test
    public void testFragmentInitialization_WithAttendee() {
        // Create a mock Attendee object
        Attendee mockAttendee = Mockito.mock(Attendee.class);
        when(mockAttendee.getName()).thenReturn("John Doe");
        when(mockAttendee.getEmail()).thenReturn("johndoe@example.com");
        when(mockAttendee.getGeolocationOn()).thenReturn(true);
        // Add more mocked methods if needed

        // Create a bundle and add the mockAttendee
        Bundle bundle = new Bundle();
        bundle.putSerializable("attendee", mockAttendee);

        // Launch the fragment with the mockAttendee as its argument
        FragmentScenario<ViewProfileFragment> scenario = FragmentScenario.launchInContainer(ViewProfileFragment.class, bundle, R.style.Base_Theme_QrazyQRsRUs);

        // Assert that the EditTexts are populated with the Attendee's data
        onView(withId(R.id.etFullName)).check(matches(withText("John Doe")));
        onView(withId(R.id.etEmailAddress)).check(matches(withText("johndoe@example.com")));

        // If your logic in onViewCreated or onCreateView depends on the Attendee's properties, add more checks here.
        // For example, to check the switch state:
        onView(withId(R.id.switchGeolocation)).check((view, noViewFoundException) -> {
            Switch geolocationSwitch = (Switch) view;
            assertTrue(geolocationSwitch.isChecked());
        });
    }



    @Test
    public void testEnterEditMode() {
        // Launch the fragment
        FragmentScenario.launchInContainer(ViewProfileFragment.class, null, R.style.Base_Theme_QrazyQRsRUs);

        // Simulate "Update Profile" button click to enter edit mode
        onView(withId(R.id.btnUpdateProfile)).perform(click());

        // Verify EditTexts are enabled
        onView(withId(R.id.etFullName)).check(matches(isEnabled()));
        onView(withId(R.id.etEmailAddress)).check(matches(isEnabled()));

        // Verify "Done" and "Cancel" buttons are visible
        onView(withId(R.id.btnDone)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
        onView(withId(R.id.btnCancel)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));

        // Verify "Update Profile" button is hidden
        onView(withId(R.id.btnUpdateProfile)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)));
    }



    @Test
    public void testUpdateProfile() {
            }


    @Test
    public void testImageSelection() {
        // Use a mocked ActivityResult to simulate image selection from the gallery
        //ask john for help
    }

    @Test
    public void testImageDeletion() {

        // Simulate deletion action

        // Verify the image is replaced with the default/initials image
    }









}
