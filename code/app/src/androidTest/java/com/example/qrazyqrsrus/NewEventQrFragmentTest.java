package com.example.qrazyqrsrus;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.Intents.intending;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.example.qrazyqrsrus.ImageViewHasDrawableMatcher.hasDrawable;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;

import androidx.fragment.app.testing.FragmentScenario;
import androidx.lifecycle.Lifecycle;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;

@RunWith(AndroidJUnit4.class)
public class NewEventQrFragmentTest {
    Bundle args = new Bundle();
    Uri imageUri;
    Bitmap testImage;

    @Mock
    QRCodeGenerator mockQRCodeGenerator = Mockito.mock(QRCodeGenerator.class);

    @Mock
    QRCodeScanHandler mockQRCodeScanHandler = Mockito.mock(QRCodeScanHandler.class);

    @Mock
    NavController mockNavController = Mockito.mock(NavController.class);


    @Before
    public void setUp(){
        Intents.init();

        //we create a builder to simulate the arguments passed by the previous new event fragments
        Event.EventBuilder mockBuilder = new Event.EventBuilder();
        mockBuilder.setName("blahblah");
        mockBuilder.setLocation("somewhere");
        mockBuilder.setDetails("eventful");
        mockBuilder.setStartDate("some time");
        mockBuilder.setEndDate("some time later");

        args.putSerializable("builder", mockBuilder);

        //we provide a test qr code for the generator to return
        QRCodeGenerator generator = new QRCodeGenerator();
        testImage = generator.generateBitmap("meow");

        //we provide a imageUri for the photo picker to return
        imageUri = Uri.parse("android.resource://com.example.qrazyqrsrus/drawable/profilep");

        Mockito.doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                Object[] args = invocation.getArguments();
                QRCodeGenerator.UniqueQRCheckCallBack callBack = (QRCodeGenerator.UniqueQRCheckCallBack) args[2];
                callBack.onUnique();
                return null;
            }
        }).when(mockQRCodeGenerator).checkUnique(Mockito.any(),eq(0), Mockito.any());
        Mockito.doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                Object[] args = invocation.getArguments();
                QRCodeGenerator.UniqueQRCheckCallBack callBack = (QRCodeGenerator.UniqueQRCheckCallBack) args[2];
                callBack.onUnique();
                return null;
            }
        }).when(mockQRCodeGenerator).checkUnique(Mockito.any(),eq(1), Mockito.any());

    }

    @After
    public void tearDown(){
        Intents.release();
    }

    @Test
    public void testGenerateButtonClick(){
        assertNotNull(testImage);
        when(mockQRCodeGenerator.generateBitmap(Mockito.any())).thenReturn(testImage);

        //we launch the fragment with our apps theme and the args from navigating from the previous fragments
        FragmentScenario<NewEventQrFragment> scenario = FragmentScenario.launchInContainer(NewEventQrFragment.class, args, R.style.Base_Theme_QrazyQRsRUs, Lifecycle.State.INITIALIZED);
        //before the fragment reaches onCreate, we give it a mock QRCodeGenerator
        scenario.onFragment(fragment ->
                fragment.setQrCodeGenerator(mockQRCodeGenerator)
        );
        //then we move ahead
        scenario.onFragment(fragment ->
                scenario.moveToState(Lifecycle.State.RESUMED)
        );
        //then we give it a mock nav controller
        scenario.onFragment(fragment ->
                Navigation.setViewNavController(fragment.requireView(), mockNavController)
        );

        onView(withId(R.id.new_event_generate_qr_button)).perform(click());

        //and we verify that the qr code appears :)
        onView(withId(R.id.new_event_display_qr_code)).check(matches(hasDrawable()));

        onView(withId(R.id.qr_screen_finish_button)).perform(click());
        //we create an ArgumentCaptor to capture the bundle that is passed when we navigate
        ArgumentCaptor<Bundle> captor = ArgumentCaptor.forClass(Bundle.class);
        //we verify that we navigate to the next screen
        verify(mockNavController).navigate(
                eq(R.id.action_newEventQrFragment_to_eventList2),
                captor.capture()
        );

        //we verify the bundle that is passed to the next screen has the qr code content
        Bundle passedBundle = captor.getValue();
        Event.EventBuilder builder = (Event.EventBuilder) passedBundle.getSerializable("builder");
        assertNotNull(builder.getQrCode());

    }

    @Test
    public void testUploadButtonClicked(){
        //we tell mockito to tell us it is unique, and to tell us our imageUri has the qr content meow
        when(mockQRCodeScanHandler.scanImage(Mockito.any(), Mockito.any())).thenReturn("meow");
        assertNotNull(testImage);
        when(mockQRCodeGenerator.generateBitmap(Mockito.any())).thenReturn(testImage);

        //we tell the photopicker to return our result with our qr code
        Intent resultData = new Intent();
        resultData.setData(imageUri);
        Instrumentation.ActivityResult result = new Instrumentation.ActivityResult(Activity.RESULT_OK, resultData);
        intending(hasAction(MediaStore.ACTION_PICK_IMAGES)).respondWith(result);

        //we launch the fragment with our apps theme and the args from navigating from the previous fragments
        FragmentScenario<NewEventQrFragment> scenario = FragmentScenario.launchInContainer(NewEventQrFragment.class, args, R.style.Base_Theme_QrazyQRsRUs, Lifecycle.State.INITIALIZED);
        //before the fragment reaches onCreate, we give it a mock QRCodeGenerator
        scenario.onFragment(fragment ->
                fragment.setQrCodeGenerator(mockQRCodeGenerator)
        );
        scenario.onFragment(fragment ->
                fragment.setQrCodeScanHandler(mockQRCodeScanHandler)
        );
        //then we move ahead
        scenario.onFragment(fragment ->
                scenario.moveToState(Lifecycle.State.RESUMED)
        );
        //then we give it a mock nav controller
        scenario.onFragment(fragment ->
                Navigation.setViewNavController(fragment.requireView(), mockNavController)
        );

        onView(withId(R.id.new_event_upload_qr_button)).perform(click());
        //verify that we launch the image picker screen after clicking the upload image button
        intended(hasAction(MediaStore.ACTION_PICK_IMAGES));

        //and we verify that the qr code appears :)
        onView(withId(R.id.new_event_display_qr_code)).check(matches(hasDrawable()));

        onView(withId(R.id.qr_screen_finish_button)).perform(click());
        //we create an ArgumentCaptor to capture the bundle that is passed when we navigate
        ArgumentCaptor<Bundle> captor = ArgumentCaptor.forClass(Bundle.class);
        //we verify that we navigate to the next screen
        verify(mockNavController).navigate(
                eq(R.id.action_newEventQrFragment_to_eventList2),
                captor.capture()
        );

        //we verify the bundle that is passed to the next screen has the qr code content
        Bundle passedBundle = captor.getValue();
        Event.EventBuilder builder = (Event.EventBuilder) passedBundle.getSerializable("builder");
        assertNotNull(builder.getQrCode());

    }

    @Test
    public void testErrorDialog(){
        //we tell mockito to tell us it is unique, and to fail to generate a qr code
        assertNotNull(testImage);
        when(mockQRCodeGenerator.generateBitmap(Mockito.any())).thenReturn(null);

        //we launch the fragment with our apps theme and the args from navigating from the previous fragments
        FragmentScenario<NewEventQrFragment> scenario = FragmentScenario.launchInContainer(NewEventQrFragment.class, args, R.style.Base_Theme_QrazyQRsRUs, Lifecycle.State.INITIALIZED);
        //before the fragment reaches onCreate, we give it a mock QRCodeGenerator
        scenario.onFragment(fragment ->
                fragment.setQrCodeGenerator(mockQRCodeGenerator)
        );
        //then we move ahead
        scenario.onFragment(fragment ->
                scenario.moveToState(Lifecycle.State.RESUMED)
        );


        onView(withId(R.id.new_event_generate_qr_button)).perform(click());
        //verify that the error dialog appears
        onView(withText("The QR generator failed to generate a QR code. Try again")).check(matches(isDisplayed()));
    }
}
