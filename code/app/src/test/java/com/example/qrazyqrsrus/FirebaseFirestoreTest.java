package com.example.qrazyqrsrus;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockedConstruction;
import org.mockito.Mockito;

import java.util.ArrayList;

public class FirebaseFirestoreTest{
//    FirebaseFirestore mockFirestore = Mockito.mock(FirebaseFirestore.class);
    //first set up mock of all items in constructor


    //can't do this, since db is a static variable
    //need to refactor FirebaseDB
//    FirebaseDB firebaseDB = new FirebaseDB(mockFirestore);

    //OR:
    //mock entire firebaseDB, then
    //could do when(getInstance).thenreturn(mocked FirebaseDB)

//    MockedConstruction<FirebaseDB> mockFirebaseDB = Mockito.mockConstruction(FirebaseDB.class, ((mock, context) -> {
//        when(mock.getInstance()).thenReturn(new FirebaseDB())
//    }));
//    when(mockFirebaseDB

    @Mock
    FirebaseFirestore mockFirestore = Mockito.mock(FirebaseFirestore.class);







    @Test
    public void addUserTest(){

        //then return a mock collection
        Mockito.when(mockFirestore.collection("Users")).thenReturn()
//        FirebaseDB firebaseDBWithMocks = createTestInstance(mock)
        Attendee user = new Attendee("abcdefg", null, "johnny t", "google@gmail.com", null, false);
        ArrayList<Attendee> attendeeList = new ArrayList<Attendee>();
        mockFirebaseDB.addUser(user);
        mockFirebaseDB.getUsersCollection()
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for (DocumentSnapshot documentSnapshot: task.getResult()) {
                            Attendee attendee = documentSnapshot.toObject(Attendee.class);
                            //attendeeList.add(attendee);
                            assertEquals(attendee.getId(), user.getId());
                            assertEquals(attendee.getName(), user.getName());
                            assertEquals(attendee.getEmail(), user.getEmail());
                            assertEquals(attendee.getGeolocationOn(), user.getGeolocationOn());
                        }
                    }
                });
    }
}
