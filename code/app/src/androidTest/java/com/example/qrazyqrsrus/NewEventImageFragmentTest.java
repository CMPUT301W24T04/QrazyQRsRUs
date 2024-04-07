package com.example.qrazyqrsrus;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.Intents.intending;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasType;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;

import androidx.fragment.app.testing.FragmentScenario;
import androidx.lifecycle.Lifecycle;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.intent.matcher.IntentMatchers;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static com.example.qrazyqrsrus.ImageViewHasDrawableMatcher.hasDrawable;
import static com.example.qrazyqrsrus.ImageViewHasDrawableMatcher.hasNoDrawable;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;


import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;

@RunWith(AndroidJUnit4.class)
public class NewEventImageFragmentTest {
    Bundle args = new Bundle();
    Uri imageUri;

    @Mock
    NavController mockNavController = Mockito.mock(NavController.class);


    @Before
    public void setUp(){
        Intents.init();

        //we create a builder to simulate the arguments passed by the previous new event fragments
        Event.EventBuilder mockBuilder = new Event.EventBuilder();
        mockBuilder.setName("Detail-less Event");
        mockBuilder.setLocation("somewhere");
        mockBuilder.setDetails("eventful");
        mockBuilder.setStartDate("some time");
        mockBuilder.setEndDate("some time later");

        args.putSerializable("builder", mockBuilder);

        //we provide a test image that the photopicker will return
        //this method of getting a Uri from a drawable resource is from https://stackoverflow.com/questions/6602417/get-the-uri-of-an-image-stored-in-drawable (accessed April 8th, 2024)
        //it was posted in the post https://stackoverflow.com/a/6606163 by the user Micheal (https://stackoverflow.com/users/170842/michael)
        imageUri = Uri.parse("android.resource://com.example.qrazyqrsrus/drawable/profilep");






//        Bitmap testImage = BitmapFactory.decodeResource(InstrumentationRegistry.getInstrumentation().getContext().getResources(), R.drawable.profilep);


    }

    @After
    public void tearDown(){
        Intents.release();
    }

    //this test was written with the help of ChatGPT
    //OpenAI, 2024, ChatGPT, Android UI tests for a class that launches a pick media activity using espresso

    /**
     * This function tests the functionality of the photo picker when the user selects an image
     */
    @Test
    public void testImageButtonClick(){
        // Set up an ActivityResultCallback to return a mock image URI
//        InstrumentationRegistry.getInstrumentation().addMonitor(
//                MediaStore.ACTION_PICK_IMAGES,
//                new Instrumentation.ActivityResult(Activity.RESULT_OK, resultData),
//                true
//        );
        //we tell the photopicker to return our result with an image
        Intent resultData = new Intent();
        resultData.setData(imageUri);
        Instrumentation.ActivityResult result = new Instrumentation.ActivityResult(Activity.RESULT_OK, resultData);
        intending(hasAction(MediaStore.ACTION_PICK_IMAGES)).respondWith(result);

        //we launch the fragment with our apps theme and the args from navigating from the previous fragments
        FragmentScenario<NewEventImageFragment> scenario = FragmentScenario.launchInContainer(NewEventImageFragment.class, args, R.style.Base_Theme_QrazyQRsRUs);
        scenario.onFragment(fragment ->
                Navigation.setViewNavController(fragment.requireView(), mockNavController)
        );

        onView(withId(R.id.new_event_image_button)).perform(click());
        //verify that we launch the image picker screen after clicking the upload image button
        intended(hasAction(MediaStore.ACTION_PICK_IMAGES));

        //and we verify that the image appears :)
        onView(withId(R.id.new_event_display_event_poster)).check(matches(hasDrawable()));

        //then we verify that when we move to the next fragment, the Builder has some imageUri and poster path
        onView(withId(R.id.image_screen_next_screen_button)).perform(click());
        //we create an ArgumentCaptor to capture the bundle that is passed when we navigate
        ArgumentCaptor<Bundle> captor = ArgumentCaptor.forClass(Bundle.class);
        //we verify that we navigate to the next screen
        verify(mockNavController).navigate(
                eq(R.id.action_newEventImageFragment_to_newEventQrFragment),
                captor.capture()
        );

        Bundle passedBundle = captor.getValue();
        Event.EventBuilder builder = (Event.EventBuilder) passedBundle.getSerializable("builder");
        assertNotNull(builder.getUri());
        assertNotNull(builder.getPosterPath());

    }

    /**
     * This function tests the functionality of the photo picker when a user does not select and image
     */
    @Test
    public void testImageButtonClick_NoImage(){
        //we tell the photopicker to return our result with NO image (resultData will not have an image)
        Intent resultData = new Intent();
        Instrumentation.ActivityResult result = new Instrumentation.ActivityResult(Activity.RESULT_OK, resultData);
        intending(hasAction(MediaStore.ACTION_PICK_IMAGES)).respondWith(result);

        //we launch the fragment with our apps theme and the args from navigating from the previous fragments
        FragmentScenario<NewEventImageFragment> scenario = FragmentScenario.launchInContainer(NewEventImageFragment.class, args, R.style.Base_Theme_QrazyQRsRUs);
        scenario.onFragment(fragment ->
                Navigation.setViewNavController(fragment.requireView(), mockNavController)
        );

        onView(withId(R.id.new_event_image_button)).perform(click());
        //verify that we launch the image picker screen after clicking the upload image button
        intended(hasAction(MediaStore.ACTION_PICK_IMAGES));

        //and we verify that no image appears
        onView(withId(R.id.new_event_display_event_poster)).check(matches(hasNoDrawable()));

        //then we verify that when we move to the next fragment, the Builder has null for imageUri
        onView(withId(R.id.image_screen_next_screen_button)).perform(click());
        //we create an ArgumentCaptor to capture the bundle that is passed when we navigate
        ArgumentCaptor<Bundle> captor = ArgumentCaptor.forClass(Bundle.class);
        //we verify that we navigate to the next screen
        verify(mockNavController).navigate(
                eq(R.id.action_newEventImageFragment_to_newEventQrFragment),
                captor.capture()
        );

        Bundle passedBundle = captor.getValue();
        Event.EventBuilder builder = (Event.EventBuilder) passedBundle.getSerializable("builder");
        assertNull(builder.getUri());
        assertNull(builder.getPosterPath());
    }

    @Test
    public void testNavigationToNextFragment(){
        //we launch the fragment with our apps theme and the args from navigating from the previous fragments
        FragmentScenario<NewEventImageFragment> scenario = FragmentScenario.launchInContainer(NewEventImageFragment.class, args, R.style.Base_Theme_QrazyQRsRUs);
        scenario.onFragment(fragment ->
                Navigation.setViewNavController(fragment.requireView(), mockNavController)
        );


        onView(withId(R.id.image_screen_next_screen_button)).perform(click());
        //we verify that we navigate to the next screen
        verify(mockNavController).navigate(
                eq(R.id.action_newEventImageFragment_to_newEventQrFragment),
                Mockito.any(Bundle.class)
        );

    }

    @Test
    public void testNavigationToPreviousFragment(){
        //we launch the fragment with our apps theme and the args from navigating from the previous fragments
        FragmentScenario<NewEventImageFragment> scenario = FragmentScenario.launchInContainer(NewEventImageFragment.class, args, R.style.Base_Theme_QrazyQRsRUs);
        scenario.onFragment(fragment ->
                Navigation.setViewNavController(fragment.requireView(), mockNavController)
        );


        onView(withId(R.id.back_button)).perform(click());
        //we verify that we navigate to the next screen
        verify(mockNavController).navigate(
                eq(R.id.action_newEventImageFragment_to_newEventTextFragment),
                Mockito.any(Bundle.class)
        );

    }

    @Test
    public void testCancelNavigation(){
        //we launch the fragment with our apps theme and the args from navigating from the previous fragments
        FragmentScenario<NewEventImageFragment> scenario = FragmentScenario.launchInContainer(NewEventImageFragment.class, args, R.style.Base_Theme_QrazyQRsRUs);
        scenario.onFragment(fragment ->
                Navigation.setViewNavController(fragment.requireView(), mockNavController)
        );


        onView(withId(R.id.cancel_button)).perform(click());
        //we verify that we navigate to the next screen
        verify(mockNavController).navigate(
                eq(R.id.action_newEventImageFragment_to_newEventFragment)
        );

    }


}
