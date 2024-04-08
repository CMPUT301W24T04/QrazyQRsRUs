package com.example.qrazyqrsrus;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.intent.Intents.intending;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasCategories;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.provider.MediaStore;

import androidx.appcompat.app.AppCompatActivity;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import com.google.zxing.MultiFormatReader;
import com.journeyapps.barcodescanner.ScanIntentResult;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;


@RunWith(AndroidJUnit4.class)
public class QrCodeScanHandlerTest {
    @Mock
    FirebaseDB mockFirebaseDB = mock(FirebaseDB.class);

    @Mock
    AppCompatActivity mockActivity = mock(AppCompatActivity.class);

    @Mock
    QRCodeScanHandler.ScanCompleteCallback mockCallback = mock(QRCodeScanHandler.ScanCompleteCallback.class);


    @Rule
    public ActivityTestRule<MainActivity> scenario = new
            ActivityTestRule<MainActivity>(MainActivity.class, true, false);






    @Before
    public void setUp(){
        Intents.init();
        //we set the FirebaseDB for the QRCodeGenerator to use to be the mock
//        generator.setFirebaseDB(mockFirebaseDB);
        //we define what to do when functions are called on our mockFirebaseDB

        Mockito.doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                Object[] args = invocation.getArguments();
                FirebaseDB.UniqueCheckCallBack callBack = (FirebaseDB.UniqueCheckCallBack) args[2];
                callBack.onResult(true);
                return null;
            }
        }).when(mockFirebaseDB).checkUnique(Mockito.any(),eq(1),Mockito.any());


    }

    @After
    public void tearDown(){
        Intents.release();
    }

    @Test
    public void testSpecialResult(){
        //we give the scanner an intent to return with the special qr code in it's extra
        Intent resultData = new Intent();
        resultData.putExtra(com.google.zxing.client.android.Intents.Scan.RESULT, "https://www.youtube.com/watch?v=dQw4w9WgXcQ&ab_channel=RickAstley");
        Instrumentation.ActivityResult result = new Instrumentation.ActivityResult(Activity.RESULT_OK, resultData);
        intending(hasAction(com.google.zxing.client.android.Intents.Scan.ACTION)).respondWith(result);

        //we make a new QRCodeScanHandler object with a mock callback that we can monitor

        scenario.getActivity().setQrHandler(new QRCodeScanHandler(mockFirebaseDB, scenario.getActivity(), "johnathan", mockCallback));

//        scenario.onActivity(activity ->{
//            QRCodeScanHandler scanHandler = new QRCodeScanHandler(mockFirebaseDB, activity, "johnathan", mockCallback);
//
//            Attendee user = new Attendee("johnathan", "document", "dj gnome boy", "5", "idk", false);
//            //we launch the scanner
//            scanHandler.launch(user);
//        });

        scenario.launchActivity(null);

        onView(withId(R.id.scan)).perform(click());

        //we verify that our scanner invoked the special result callback, that would navigate to the admin screen
        verify(mockCallback).onSpecialResult();

    }
}
