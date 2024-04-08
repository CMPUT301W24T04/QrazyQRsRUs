package com.example.qrazyqrsrus;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.google.zxing.MultiFormatReader;

import org.junit.Before;
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
    MultiFormatReader mockMultiFormatReader = mock(MultiFormatReader.class);





    @Before
    public void setUp(){
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
}
