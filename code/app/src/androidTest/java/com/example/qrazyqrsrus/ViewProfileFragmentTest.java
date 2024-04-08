package com.example.qrazyqrsrus;

import androidx.fragment.app.testing.FragmentScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class ViewProfileFragmentTest {
    @Before
    public void setup() {
        // Initialize anything required for the tests here
    }
    @Test
    public void testFragmentInitialization_NoAttendee() {

    }
    @Test
    public void testFragmentInitialization_WithAttendee() {

    }
    @Test
    public void testEnterEditMode() {
        FragmentScenario<ViewProfileFragment> scenario = FragmentScenario.launchInContainer(ViewProfileFragment.class);
        // Simulate button click to enter edit mode

        // Verify that components are enabled and visible
    }

    @Test
    public void testUpdateProfile() {
        // This test may need to mock FirebaseDB or Firestore interactions
    }

    @Test
    public void testImageSelection() {
        // Use a mocked ActivityResult to simulate image selection from the gallery
        //ask john for help
    }

    @Test
    public void testImageDeletion() {
        FragmentScenario<ViewProfileFragment> scenario = FragmentScenario.launchInContainer(ViewProfileFragment.class);
        // Simulate deletion action

        // Verify the image is replaced with the default/initials image
    }









}
