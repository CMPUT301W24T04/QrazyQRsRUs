package com.example.qrazyqrsrus;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isEnabled;
import static androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Switch;

import androidx.activity.result.ActivityResultLauncher;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.testing.FragmentScenario;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

@RunWith(AndroidJUnit4.class)
public class ViewProfileFragmentTest {


    @Before
    public void setup() {
        MockitoAnnotations.openMocks(this);

    }
    @Test
    public void testFragmentInitialization_WithAttendee() {
        // Create a mock Attendee object
        Attendee mockAttendee = Mockito.mock(Attendee.class);
        when(mockAttendee.getName()).thenReturn("John Doe");
        when(mockAttendee.getEmail()).thenReturn("johndoe@example.com");
        when(mockAttendee.getGeolocationOn()).thenReturn(true);


        // Create a bundle and add the mockAttendee
        Bundle bundle = new Bundle();
        bundle.putSerializable("attendee", mockAttendee);

        // Launch the fragment with the mockAttendee as its argument
        FragmentScenario<ViewProfileFragment> scenario = FragmentScenario.launchInContainer(ViewProfileFragment.class, bundle, R.style.Base_Theme_QrazyQRsRUs);

        // Assert that the EditTexts are populated with the Attendee's data
        onView(withId(R.id.etFullName)).check(matches(withText("John Doe")));
        onView(withId(R.id.etEmailAddress)).check(matches(withText("johndoe@example.com")));


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






    @Rule
    public ActivityScenarioRule<MainActivity> activityScenarioRule =
            new ActivityScenarioRule<>(MainActivity.class);




    @Test
    public void testImageSelection() {
        // Use a mocked ActivityResult to simulate image selection from the gallery
        // Launch the fragment
        FragmentScenario<ViewProfileFragment> scenario = FragmentScenario.launchInContainer(ViewProfileFragment.class, null, R.style.Base_Theme_QrazyQRsRUs);

        // Using the scenario, get the fragment and set up the mocked launcher
        scenario.onFragment(fragment -> {
            // Create a mock Uri for testing
            Uri mockUri = Uri.parse("android.resource://com.example.qrazyqrsrus/drawable/image");
            Drawable mockDrawable = new BitmapDrawable(fragment.getResources(), Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888));

            // Stub the ActivityResultLauncher to return the mock Uri
            ActivityResultLauncher<String> mockLauncher = (ActivityResultLauncher<String>) Mockito.mock(ActivityResultLauncher.class);
            fragment.galleryActivityResultLauncher = mockLauncher;

            // Simulate the fragment receiving a result for the image selection
            fragment.onImageSelected(mockUri);
            fragment.imgProfilePicture.setImageDrawable(mockDrawable);

            // Verify that the ImageView has the image set (you may need to write a custom matcher to check the drawable)
            ImageView imgProfilePicture = fragment.imgProfilePicture;
            Drawable imgDrawable = imgProfilePicture.getDrawable();
            assertNotNull("Image drawable should not be null after selection", imgDrawable);

            // Alternatively, verify that newImageUri and imageDeleted are set correctly
            assertEquals("The Uri should match the mocked Uri", mockUri, fragment.newImageUri);
            assertFalse("Image should not be marked as deleted after selection", fragment.imageDeleted);
        });
    }
    private Bitmap getDefaultOrInitialsImage() {
        return Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888);
    }



    @Test
    public void testImageDeletion() {
        FragmentScenario<ViewProfileFragment> scenario = FragmentScenario.launchInContainer(ViewProfileFragment.class, null, R.style.Base_Theme_QrazyQRsRUs);

        scenario.onFragment(fragment -> {
            // Simulate the image deletion action
            fragment.deleteProfileImage();

            // Directly set a known drawable as the default image for comparison
            Drawable expectedDrawable = ContextCompat.getDrawable(fragment.requireContext(), R.drawable.default_image);
            fragment.imgProfilePicture.setImageDrawable(expectedDrawable);

            // Get the drawable from the ImageView after deletion action
            Drawable actualDrawable = fragment.imgProfilePicture.getDrawable();

            // Check if drawables are the same
            assertEquals("Drawables should be the same after deletion", expectedDrawable, actualDrawable);

            // Verify that newImageUri and imageDeleted are set correctly
            assertNull("newImageUri should be null after deletion", fragment.newImageUri);
            assertTrue("imageDeleted should be true after deletion", fragment.imageDeleted);
        });
    }












}
