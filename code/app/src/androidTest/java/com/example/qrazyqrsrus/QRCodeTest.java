package com.example.qrazyqrsrus;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.google.zxing.WriterException;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

@RunWith(AndroidJUnit4.class)
public class QRCodeTest {
    QRCodeGenerator generator = new QRCodeGenerator();

    @Mock
    FirebaseDB mockFirebaseDB = mock(FirebaseDB.class);

    @Mock
    BarcodeEncoder mockBarcodeEncoder = mock(BarcodeEncoder.class);

    Bitmap testImage;

    @Before
    public void setUp(){
        //we set the FirebaseDB for the QRCodeGenerator to use to be the mock
        generator.setFirebaseDB(mockFirebaseDB);
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

    @Test
    public void generateBitmapTest_Success(){
//        testImage = BitmapFactory.decodeResource(InstrumentationRegistry.getInstrumentation().getContext().getResources(), R.drawable.profilep);
//        assertNotNull(testImage);
//        when(mockBarcodeEncoder.encodeBitmap(Mockito.any(), Mockito.any(), eq(400), eq(400))).thenReturn(testImage);

        Bitmap returnedBitmap = generator.generateBitmap("dwahdwah");
        assertNotNull(returnedBitmap);


        //we verify that bitmaps get generated as expected
        returnedBitmap = generator.generateBitmap("newevent_20242218_mynameisjohn");
        assertNotNull(returnedBitmap);
        //test null
        returnedBitmap = generator.generateBitmap(null);
        assertNull(returnedBitmap);
        //test too long
        returnedBitmap = generator.generateBitmap("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");
        assertNull(returnedBitmap);
        //test empty
        returnedBitmap = generator.generateBitmap("");
        assertNull(returnedBitmap);
    }

    @Test
    public void generateBitmapTest_Failure() throws WriterException {
        generator.setBarcodeEncoder(mockBarcodeEncoder);
        when(mockBarcodeEncoder.encode(Mockito.any(), Mockito.any(), eq(400), eq(400))).thenThrow(new WriterException());

        Bitmap returnedBitmap = generator.generateBitmap("dwahdwah");
        assertNull(returnedBitmap);

    }

    @Test
    public void checkUnique_Success(){
        //we tell checkUnique of firebase to call onResult (qr is unique)
        Mockito.doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                Object[] args = invocation.getArguments();
                FirebaseDB.UniqueCheckCallBack callBack = (FirebaseDB.UniqueCheckCallBack) args[2];
                callBack.onResult(true);
                return null;
            }
        }).when(mockFirebaseDB).checkUnique(Mockito.any(),eq(0),Mockito.any());

        QRCodeGenerator.UniqueQRCheckCallBack mockCallback = mock(QRCodeGenerator.UniqueQRCheckCallBack.class);

        generator.checkUnique("gwah gwah", 0, mockCallback);

        //we verify our callback is called the way we wanted
        verify(mockCallback).onUnique();
    }

    @Test
    public void checkUnique_Failure(){
        //we tell checkUnique of firebase to call onResult (qr is not unique)
        Mockito.doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                Object[] args = invocation.getArguments();
                FirebaseDB.UniqueCheckCallBack callBack = (FirebaseDB.UniqueCheckCallBack) args[2];
                callBack.onResult(false);
                return null;
            }
        }).when(mockFirebaseDB).checkUnique(Mockito.any(),eq(0),Mockito.any());

        QRCodeGenerator.UniqueQRCheckCallBack mockCallback = mock(QRCodeGenerator.UniqueQRCheckCallBack.class);

        generator.checkUnique("doop doop", 0, mockCallback);


        //we verify our callback is called the way we wanted
        verify(mockCallback).onNotUnique();
    }

}
