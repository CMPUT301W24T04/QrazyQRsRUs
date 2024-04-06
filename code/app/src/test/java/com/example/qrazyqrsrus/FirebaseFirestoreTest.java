package com.example.qrazyqrsrus;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.common.base.Verify;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.FirebaseStorage;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedConstruction;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class FirebaseFirestoreTest{
    //our test instance to spy on
    FirebaseDB firebaseDBSpy;
    //mocking our dependencies

    @Mock
    FirebaseFirestore mockFirestore = Mockito.mock(FirebaseFirestore.class);

    @Mock
    FirebaseStorage mockFirebaseStorage = Mockito.mock(FirebaseStorage.class);

    @Mock
    FirebaseMessaging mockFirebaseMessaging = Mockito.mock(FirebaseMessaging.class);

    @Mock
    CollectionReference mockUserCollection = Mockito.mock(CollectionReference.class);

    @Mock
    DocumentReference mockDocumentReference = Mockito.mock(DocumentReference.class);

    @Mock
    Query mockQuery = Mockito.mock(Query.class);

    @Mock
    Iterator<QueryDocumentSnapshot> mockIterator = Mockito.mock(Iterator.class);

    @Mock
    QuerySnapshot mockQuerySnapshot = Mockito.mock(QuerySnapshot.class);

    @Mock
    DocumentSnapshot mockDocumentSnapshot = Mockito.mock(DocumentSnapshot.class);
    @Mock
    DocumentSnapshot mockDocumentSnapshot2 = Mockito.mock(DocumentSnapshot.class);
    @Mock
    DocumentSnapshot mockDocumentSnapshot3 = Mockito.mock(DocumentSnapshot.class);

    @Mock
    QueryDocumentSnapshot mockQueryDocumentSnapshot = Mockito.mock(QueryDocumentSnapshot.class);

    @Mock
    Task<DocumentReference> mockTask = (Task<DocumentReference>) Mockito.mock(Task.class);

    @Mock
    Task<Void> mockTaskVoid = (Task<Void>) Mockito.mock(Task.class);

    @Mock
    Task<QuerySnapshot> mockTaskQuery = (Task<QuerySnapshot>) Mockito.mock(Task.class);

    @Mock
    Void mockVoid = Mockito.mock(Void.class);

//    @InjectMocks
//    FirebaseDB firebaseDB;

    List<DocumentSnapshot> mockList = new ArrayList<DocumentSnapshot>();

    @Before
    public void setUp() {
        when(mockTaskQuery.getResult()).thenReturn(mockQuerySnapshot);
        when(mockQuerySnapshot.getDocuments()).thenReturn(mockList);
        when(mockQuerySnapshot.iterator()).thenReturn(mockIterator);

        when(mockFirestore.collection("Users")).thenReturn(mockUserCollection);
        // Set mocked FirebaseFirestore instance in FirebaseDB for testing
        firebaseDBSpy = Mockito.spy(FirebaseDB.getInstance(mockFirestore, mockFirebaseStorage, mockFirebaseMessaging));


        when(mockUserCollection.add(Mockito.any())).thenReturn(mockTask);
        when(mockDocumentReference.update("name", "john t",
                "email", "testEmail",
                "geolocationOn", false,
                "profilePicturePath", "testPath", "documentId", "testDocumentId")).thenReturn(mockTaskVoid);
//        when(mockTask.addOnSuccessListener(Mockito.any())).thenReturn(mockTask);

        when(mockTask.addOnSuccessListener(Mockito.any())).thenAnswer(invocation -> {
            // Get the onSuccess listener
            OnSuccessListener<DocumentReference> listener = invocation.getArgument(0);
            // Call the onSuccess method with a mock DocumentReference
            listener.onSuccess(mockDocumentReference);
            return mockTask;
        });

        when(mockTaskVoid.addOnSuccessListener(Mockito.any())).thenAnswer(invocation -> {
            // Get the onSuccess listener
            OnSuccessListener<Void> listener = invocation.getArgument(0);
            // Call the onSuccess method with a mock DocumentReference
            listener.onSuccess(mockVoid);
            return mockTaskVoid;
        });

        when(mockQuery.get()).thenReturn(mockTaskQuery);

        when(mockTaskQuery.addOnCompleteListener(Mockito.any())).thenAnswer(invocation -> {
            // Get the onSuccess listener
            OnCompleteListener<QuerySnapshot> listener = invocation.getArgument(0);
            // Call the onSuccess method with a mock DocumentReference
            listener.onComplete(mockTaskQuery);
            return mockTask;
        });

//        doNothing().when(mockTaskVoid).addOnFailureListener(Mockito.any());
    }

    //this test was written with the help of ChatGPT
    //OpenAI, 2024, ChatGPT, Unit Tests for Singleton classes using Mockito
    @Test
    public void testAddUser_Success() {
        // Mock Firestore behavior
//        when(mockFirestore.collection("Users")).thenReturn(mockUserCollection);

        // Create sample Attendee object
        Attendee user = new Attendee("testId", null, "john t", "testEmail", "testPath", false);

        when(mockDocumentReference.getId()).thenReturn("testDocumentId");
        when(mockUserCollection.document("testDocumentId")).thenReturn(mockDocumentReference);
        // Call method to be tested
        firebaseDBSpy.addUser(user);

        //we verify that the functions should be called upon a successful addition of a user to firestore are called
        verify(mockFirestore, Mockito.times(1)).collection("Users");
        verify(mockUserCollection, Mockito.times(1)).add(user);
        verify(mockUserCollection, Mockito.times(1)).document("testDocumentId");
        verify(mockDocumentReference, Mockito.times(1)).update("name", "john t",
                "email", "testEmail",
                "geolocationOn", false,
                "profilePicturePath", "testPath", "documentId", "testDocumentId");
    }



    @Test
    public void testLoginUser_AlreadyExists(){
        Attendee user = new Attendee("coolId", "1234", "john again", "testEmail", "testPath", false);

        mockList.add(mockDocumentSnapshot);
        //our mock assumes there is one document in the database with a matching id
        when(mockUserCollection.whereEqualTo("id", "coolId")).thenReturn(mockQuery);
        when(mockTaskQuery.isSuccessful()).thenReturn(true);
        //when user already exists, task.getResult should return a document snapshot

        when(mockQuerySnapshot.isEmpty()).thenReturn(false);

        //we create a mock list of documentSnapshots for our query to return


        when(mockQueryDocumentSnapshot.toObject(Attendee.class)).thenReturn(user);
        when(mockQueryDocumentSnapshot.getId()).thenReturn("1234");

        //we configure the mockIterator to have only one element, and return our single user
        when(mockIterator.hasNext()).thenReturn(true, false);
        when(mockIterator.next()).thenReturn(mockQueryDocumentSnapshot);

        //we provide a callback that we will capture the arguments of, to make sure the right attendee is being retrieved
        ArgumentCaptor<Attendee> attendeeCaptor = ArgumentCaptor.forClass(Attendee.class);
        FirebaseDB.GetAttendeeCallBack mockCallback = Mockito.mock(FirebaseDB.GetAttendeeCallBack.class);


        doNothing().when(firebaseDBSpy).addUser(Mockito.any());

        firebaseDBSpy.loginUser("coolId", mockCallback);

        verify(mockCallback).onResult(attendeeCaptor.capture());
        assertEquals(user, attendeeCaptor.getValue());
    }
}
