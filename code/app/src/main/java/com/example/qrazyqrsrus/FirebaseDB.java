package com.example.qrazyqrsrus;

import static android.graphics.ImageDecoder.createSource;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.checkerframework.checker.units.qual.A;
import org.checkerframework.checker.units.qual.C;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;
/**
 * This is a utility class that does all the Firebase related actions.
 */
public class FirebaseDB {

    public interface UniqueCheckCallBack {
        void onResult(boolean isUnique);
    }

    public interface MatchingQRCallBack {
        void onResult(Event matchingEvent);
    }

    public interface GetStringCallBack{
        void onResult(String string);
    }

    public interface GetBitmapCallBack{
        void onResult(Bitmap bitmap);
    }

    public interface GetAttendeeCallBack {
        void onResult(Attendee attendee);
    }

    static FirebaseFirestore db = FirebaseFirestore.getInstance();
    final static FirebaseStorage storage = FirebaseStorage.getInstance();
    final static CollectionReference usersCollection = db.collection("Users");
    final static CollectionReference eventsCollection = db.collection("Events");
    final static CollectionReference checkInsCollection = db.collection("CheckIns");

    final static String usersTAG = "Users";
    final static String eventsTAG = "Events";
    final static String imagesTAG = "Images";

    public FirebaseDB(FirebaseFirestore firestoreInstance){
        db = firestoreInstance;
    }

    // Change String to Attendee class when someone implements it.

    /**
     * Adds a document that represents a user in the database
     *
     * @param user The user we want to add
     */
    public static void addUser(Attendee user) {
        usersCollection
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(usersTAG, "User document snapshot written with ID:" + documentReference.getId());
                        user.setDocumentId(documentReference.getId());
                        updateUser(user);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(usersTAG, "Error while adding user document", e);
                    }
                });
    }

    /**
     * Logs the user in depending on their unique identifier. Will create a new user if this is
     * the first time they have opened the app.
     *
     * @param userId The unique identifier of the user that has opened the app
     */

    public static void loginUser(String userId, GetAttendeeCallBack callBack) {
        usersCollection
                .whereEqualTo("id", userId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if (task.getResult() == null || task.getResult().isEmpty()) {
                                Attendee attendee = new Attendee(userId);
                                addUser(attendee);
                                callBack.onResult(attendee);
                            } else {
                                for (DocumentSnapshot documentSnapshot: task.getResult()) {
                                    Attendee attendee = documentSnapshot.toObject(Attendee.class);
                                    attendee.setDocumentId(documentSnapshot.getId());
                                    callBack.onResult(attendee);
                                }
                            }
                        } else {
                            Log.e("MainActivity", "Error trying to login");
                        }
                    }
                });
    }

    /**
     * Adds a document that represents an event in the database
     *
     * @param event The event we want to add
     */
    public static void addEvent(Event event) {
        eventsCollection
                .add(event)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(eventsTAG, "event document snapshot written with ID:" + documentReference.getId());

                        event.setDocumentId(documentReference.getId());
                        updateEvent(event);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(eventsTAG, "Error while adding event document", e);
                    }
                });
    }

    /**
     * Updates the document that represents the user in the database.
     *
     * @param user The user that needs their document updated.
     */
    public static void updateUser(Attendee user) {
        usersCollection
                .document(user.getDocumentId())
                .update("name", user.getName(),
                        "email", user.getEmail(),
                        "geolocationOn", user.getGeolocationOn(),
                        "profilePicturePath", user.getProfilePicturePath(), "documentId", user.getDocumentId())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d(usersTAG, "User document updated successfully");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(usersTAG, "Error while updating user document", e);
                    }
                });
    }

    /**
     * Updates the document that represents the event in the database.
     *
     * @param event The event that needs its document updated.
     */
    public static void updateEvent(Event event) {
        eventsCollection
                .document(event.getDocumentId())
                .update("announcements", event.getAnnouncements(),
                        "checkIns", event.getCheckIns(), "signUps",
                        event.getSignUps(), "posterPath", event.getPosterPath(),
                        "qrCode", event.getQrCode(), "documentId", event.getDocumentId())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d(eventsTAG, "Event document updated successfully");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(eventsTAG, "Error while updating event document", e);
                    }
                });
    }

    /**
     * Retrieves an image from the database
     *
     * @param file     The file in the local system
     * @param pathName This is the path of the image. In the form {folder}/{image} where
     *                 folder is either profiles, posters, qrcodes, and image is just the name of
     *                 the image
     */
    public static void uploadImage(Uri file, String pathName) {
        StorageReference storageRef = storage.getReference();
        StorageReference storageReference = storageRef.child(pathName + ".jpg");

        storageReference.putFile(file)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Log.d(imagesTAG, "Successful image upload");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(imagesTAG, "Failed to upload image");
                    }
                });

    }

    /**
     * Retrieves an image from the database
     *
     * @param user This is the user we want to retrieve their profile picture
     * @param callBack This callBack will be used to get back bitmap
     */
    public static void retrieveImage(Attendee user, GetBitmapCallBack callBack) {
        //ArrayList<Bitmap> localBitMap = new ArrayList<Bitmap>();
        try {
            StorageReference storageRef = storage.getReference(user.getProfilePicturePath() + ".jpg");
            File localFile = File.createTempFile(user.getProfilePicturePath().split("/")[1], "jpg");
            storageRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    callBack.onResult(BitmapFactory.decodeFile(localFile.getAbsolutePath()));
                    Log.d(imagesTAG, "Successfully retrieved image");
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    Log.w(imagesTAG, "Failed to retrieve image: " + exception);
                }
            });
        } catch (IOException exception) {
            Log.e(imagesTAG, "Error trying to retrieve image: " + exception);
        }
        //return localBitMap.get(0);
    }

    /**
     * Retrieves an image from the database
     *
     * @param event This is the event we're trying to get its poster.
     * @param callBack This callBack will be used to get back bitmap
     */
    public static void retrieveImage(Event event, GetBitmapCallBack callBack) {
        try {
            StorageReference storageRef = storage.getReference(event.getPosterPath() + ".jpg");
            File localFile = File.createTempFile(event.getPosterPath().split("/")[1], "jpg");
            storageRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    callBack.onResult(BitmapFactory.decodeFile(localFile.getAbsolutePath()));
                    Log.d(imagesTAG, "Successfully retrieved image");
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    Log.w(imagesTAG, "Failed to retrieve image: " + exception);
                }
            });
        } catch (IOException exception) {
            Log.e(imagesTAG, "Error trying to retrieve image: " + exception);
        }
    }

    /**
     * Retrieves all events in the events collection
     *
     * @param eventList         The list we're going to hold the events in.
     * @param eventArrayAdapter The ArrayAdapter of eventList.
     */
    public static void getAllEvents(ArrayList<Event> eventList, ArrayAdapter<Event> eventArrayAdapter) {
        eventsCollection
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            Log.d(eventsTAG, "Retrieved all events");
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String id = document.getId();
                                String name = (String) document.getData().get("name");
                                String organizerId = (String) document.getData().get("organizerId");
                                String details = (String) document.getData().get("details");
                                String location = (String) document.getData().get("location");
                                String startDate = (String) document.getData().get("startDate");
                                String endDate = (String) document.getData().get("endDate");
                                Boolean geolocationOn = (Boolean) document.getData().get("geolocationOn");
                                String posterPath = (String) document.getData().get("posterPath");
                                String qrCode = (String) document.getData().get("qrCode");
                                String qrCodePromo = (String) document.getData().get("qrCodePromo");
                                ArrayList<String> announcements = (ArrayList<String>) document.getData().get("announcements");
                                if (announcements == null){
                                    announcements = new ArrayList<String>();
                                }
                                ArrayList<String> signUps = (ArrayList<String>) document.getData().get("signUps");
                                if (signUps == null){
                                    signUps = new ArrayList<String>();
                                }
                                ArrayList<String> checkIns = (ArrayList<String>) document.getData().get("checkIns");
                                if (checkIns == null){
                                    checkIns = new ArrayList<String>();
                                }

                                Event event = new Event(id, name, organizerId, details, location, startDate, endDate, geolocationOn, posterPath, qrCode, qrCodePromo, announcements, signUps, checkIns);
                                eventList.add(event);
                            }
                            eventArrayAdapter.notifyDataSetChanged();
                        } else {
                            Log.d(eventsTAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

    }

    /**
     * Retrieves all users in the users collection
     *
     * @param attendeeList         The list we're going to hold the users in.
     * @param attendeeArrayAdapter The ArrayAdapter of attendeeList.
     */
    public static void getAllUsers(ArrayList<Attendee> attendeeList, ArrayAdapter<Attendee> attendeeArrayAdapter) {
        usersCollection
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            Log.d(usersTAG, "Retrieved all users");
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String documentId = document.getId();
                                String id = (String) document.getData().get("id");
                                String name = (String) document.getData().get("name");
                                String email = (String) document.getData().get("email");
                                String profilePicturePath = (String) document.getData().get("profilePicturePath");
                                Boolean geolocationOn = (Boolean) document.getData().get("geolocationOn");


                                Attendee attendee = new Attendee(id, documentId, name, email, profilePicturePath, geolocationOn);
                                attendeeList.add(attendee);
                                attendeeArrayAdapter.notifyDataSetChanged();
                            }
                        } else {
                            Log.d(usersTAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

    }

    /**
     * Retrieves all the events an user has signed up to
     *
     * @param user the user who as signed up to events
     * @param eventArrayList the list you want to add the events to
     */
    public static void getAttendeeSignedUpEvents(Attendee user, ArrayList<Event> eventArrayList, HomeSignedUpListAdapter adapter) {
        eventsCollection
                .whereArrayContains("signUps", user.getDocumentId())
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            Event event = documentSnapshot.toObject(Event.class);
                            if (event.getAnnouncements() == null){
                                event.setAnnouncements(new ArrayList<String>());
                            }
                            if (event.getSignUps() == null){
                                event.setSignUps(new ArrayList<String>());
                            }
                            if (event.getCheckIns() == null){
                                event.setCheckIns(new ArrayList<String>());
                            }
                            event.setDocumentId(documentSnapshot.getId());
                            eventArrayList.add(event);
                        }
                        adapter.notifyDataSetChanged();
                        Log.d(eventsTAG, "Events successfully retrieved");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(eventsTAG, "Failed to retrieve events of user");
                    }
                });
    }

    /**
     * Retrieves all the events an user has checked in to
     *
     * @param user the user who as checked in to events
     * @param eventArrayList the list passed in to get the events
     */
    public static void getAttendeeCheckedInEvents(Attendee user, ArrayList<Event> eventArrayList, HomeCheckedInListAdapter adapter) {
        ArrayList<String> attendeeCheckIns = new ArrayList<>();
        checkInsCollection
                .whereEqualTo("attendeeDocId", user.getDocumentId())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for (DocumentSnapshot documentSnapshot : task.getResult()) {
                            attendeeCheckIns.add((String) documentSnapshot.get("eventDocId"));
                        }
                    }
                });
        for (String id : attendeeCheckIns) {
            eventsCollection
                    .document(id)
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            Event event = documentSnapshot.toObject(Event.class);

                            if (event.getAnnouncements() == null){
                                event.setAnnouncements(new ArrayList<String>());
                            }
                            if (event.getSignUps() == null){
                                event.setSignUps(new ArrayList<String>());
                            }
                            if (event.getCheckIns() == null){
                                event.setCheckIns(new ArrayList<String>());
                            }
                            eventArrayList.add(event);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w(eventsTAG, "Didn't find it" + e);
                        }
                    });
        }
        adapter.notifyDataSetChanged();
    }

    /**
     * Retrieves all the attendees that have signed up an event
     *
     * @param event the event we're getting the attendees
     * @param attendeeArrayList the list passed in to get the attendees
     */
    public static void getEventSignedUp(Event event, ArrayList<Attendee> attendeeArrayList) {
        for (String signUps : event.getSignUps()) {
            usersCollection
                    .document(signUps)
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            Attendee attendee = documentSnapshot.toObject(Attendee.class);
                            attendee.setDocumentId(documentSnapshot.getId());
                            attendeeArrayList.add(attendee);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w(usersTAG, "Failed " + e);
                        }
                    });
        }
    }

    /**
     * Retrieves all the attendees that have checked in an event
     *
     * @param event the event we're getting the attendees
     * @param attendeeArrayList the list passed in to get the events
     */
    public static void getEventCheckedIn(Event event, ArrayList<Attendee> attendeeArrayList) {
        checkInsCollection
                .whereEqualTo("eventDocId", event.getDocumentId())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for (DocumentSnapshot documentSnapshot : task.getResult()) {
                            usersCollection
                                    .document((String) Objects.requireNonNull(documentSnapshot.get("attendeeDocId")))
                                    .get()
                                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                        @Override
                                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                                            Attendee attendee = documentSnapshot.toObject(Attendee.class);
                                            attendee.setDocumentId(documentSnapshot.getId());
                                            attendeeArrayList.add(attendee);
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.w(usersTAG, "Failed " + e);
                                        }
                                    });

                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(usersTAG, "Failed " + e);
                    }
                });

    }

    /** This returns the name of the user with that document id
     * @param userDocumentId This the document id of the user
     * @param callBack This callBack will be used to get back the name of user
     */
    public static void getUserName(String userDocumentId, GetStringCallBack callBack) {
        usersCollection.document(userDocumentId).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Log.d(usersTAG, "Success");
                        callBack.onResult((String) documentSnapshot.get("id"));
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(usersTAG, "Failed "+e);
                    }
                });
    }

    /**
     * \
     * Check whether or not the content of a QR code is already in use by an event created in the app.
     *
     * @param qrContent a string that represents the content field of the QR code we are checking
     * @param mode an integer: 0 if we are checking promotional qr codes, 1 if we are checking checkin qr codes
     * @param callBack a class that implements the UniqueCheckCallBack that will allow you to handle the boolean result if there is an already existing event with the qr code.
     */
    public static void checkUnique(String qrContent, int mode, UniqueCheckCallBack callBack) {
        String field;
        if (mode == 0) {
            field = "qrCodePromo";
        } else {
            field = "qrCode";
        }
        eventsCollection
                .whereEqualTo(field, qrContent)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            callBack.onResult(task.getResult() == null || task.getResult().isEmpty());

                        } else {
                            Log.e("MainActivity", "Error checking existing Event QR codes");
                        }
                    }
                });
    }

    /**
     * \
     * Check if there exists an event with the matching promotional or checkin qr code
     *
     * @param qrContent a string that represents the content field of the QR code we are checking
     * @param mode an integer: 0 if we are checking promotional qr codes, 1 if we are checking checkin qr codes
     * @param callBack a class that implements the MatchingQRCallBack that will allow you to handle the event with the matching qr code
     */
    public static void findEventWithQR(String qrContent, int mode, MatchingQRCallBack callBack) {
        String field;
        if (mode == 0) {
            field = "qrCodePromo";
        } else {
            field = "qrCode";
        }
        eventsCollection
                .whereEqualTo(field, qrContent)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot documentSnapshot : task.getResult()) {
                                Event event = documentSnapshot.toObject((Event.class));
                                if (event.getAnnouncements() == null){
                                    event.setAnnouncements(new ArrayList<String>());
                                }
                                if (event.getSignUps() == null){
                                    event.setSignUps(new ArrayList<String>());
                                }
                                if (event.getCheckIns() == null){
                                    event.setCheckIns(new ArrayList<String>());
                                }
                                event.setDocumentId(documentSnapshot.getId());

                                callBack.onResult(event);
                            }
                        }
                    }
                });

    }
    /**
     * Retrieves all the events an user has signed up to
     *
     * @param user the user who as signed up to events
     * @param eventArrayList the list you want to add the events to
     */
    public static void getEventsMadeByUser(Attendee user, ArrayList<Event> eventArrayList, EventListAdapter adapter) {
        eventsCollection
                .whereEqualTo("organizerId", user.getDocumentId())
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            Event event = documentSnapshot.toObject(Event.class);
                            if (event.getAnnouncements() == null){
                                event.setAnnouncements(new ArrayList<String>());
                            }
                            if (event.getSignUps() == null){
                                event.setSignUps(new ArrayList<String>());
                            }
                            if (event.getCheckIns() == null){
                                event.setCheckIns(new ArrayList<String>());
                            }
                            event.setDocumentId(documentSnapshot.getId());
                            eventArrayList.add(event);
                        }
                        adapter.notifyDataSetChanged();
                        Log.d(eventsTAG, "Events successfully retrieved");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(eventsTAG, "Failed to retrieve events of user");
                    }
                });
    }

    public static void addCheckIn(CheckIn checkIn) {
        checkInsCollection
                .add(checkIn)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        checkIn.setDocumentId(documentReference.getId());
                        updateCheckIn(checkIn);
                    }
                });
    }

    public static void updateCheckIn(CheckIn checkIn) {
        checkInsCollection
                .document(checkIn.getDocumentId())
                .update("attendeeDocId", checkIn.getAttendeeDocId(),
                        "documentId", checkIn.getDocumentId(),
                        "eventDocId", checkIn.getEventDocId(), "location", checkIn.getLocation(),
                        "numberOfCheckIns", checkIn.getNumberOfCheckIns())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d(usersTAG, "CheckIn document updated successfully");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(usersTAG, "Error while updating check in document", e);
                    }
                });
    }

    public static void checkInAlreadyExists(String eventDocId, String attendeeDocId, UniqueCheckCallBack callBack) {
        checkInsCollection
                .whereEqualTo("attendeeDocId", attendeeDocId)
                .whereEqualTo("eventDocId", eventDocId)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        callBack.onResult(queryDocumentSnapshots.isEmpty());
                    }
                });
    }
}