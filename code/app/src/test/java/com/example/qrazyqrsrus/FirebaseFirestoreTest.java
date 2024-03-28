package com.example.qrazyqrsrus;

import static org.junit.Assert.assertEquals;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;

public class FirebaseFirestoreTest{
    FirebaseFirestore mockFirestore = Mockito.mock(FirebaseFirestore.class);
    //can't do this, since db is a static variable
    //need to refactor FirebaseDB
    FirebaseDB firebaseDB = new FirebaseDB(mockFirestore);

    @Test
    public void addUserTest(){
        Attendee user = new Attendee("abcdefg", null, "johnny t", "google@gmail.com", null, false);
        ArrayList<Attendee> attendeeList = new ArrayList<Attendee>();
        firebaseDB.addUser(user);
        mockFirestore.collection("Users")
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
