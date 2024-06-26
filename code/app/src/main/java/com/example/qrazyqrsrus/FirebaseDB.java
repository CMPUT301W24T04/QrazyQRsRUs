package com.example.qrazyqrsrus;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * This is a utility class that does all the Firebase related actions.
 */
public class FirebaseDB {

    /**
     * Callback interface for checking uniqueness.
     */
    public interface UniqueCheckCallBack {
        /**
         * Called with the result of the uniqueness check.
         *
         * @param isUnique Indicates whether the item checked is unique.
         */
        void onResult(boolean isUnique);
    }

    /**
     * Callback interface for checking uniqueness of a check-in.
     */
    public interface UniqueCheckInCallBack {
        /**
         * Called with the result of the check-in uniqueness check.
         *
         * @param isUnique Indicates whether the check-in is unique.
         * @param checkIn  The check-in object being checked.
         */
        void onResult(boolean isUnique, CheckIn checkIn);
    }

    //- we change the callback interface for looking for a matching qr code to have an OnNoResult function that handles the case that no matching qr code exists for either promo or checkin
    //then in qr scanhandler we call findEventWithQR in OnNoResult for the promo qr search
    //then in qr scanhandler we throw error in onNoResult after both qr searches
    /**
     * Callback interface for finding an event with a matching QR code.
     */
    public interface MatchingQRCallBack {
        /**
         * Called when a matching event is found.
         *
         * @param matchingEvent The event matching the QR code.
         */
        void onResult(Event matchingEvent);

        /**
         * Called when no matching event is found.
         */
        void onNoResult();
    }

    /**
     * Callback interface for retrieving a String.
     */
    public interface GetStringCallBack{
        /**
         * Called with the result string.
         *
         * @param string The result string.
         */
        void onResult(String string);
    }

    /**
     * Callback interface for retrieving a Bitmap.
     */
    public interface GetBitmapCallBack{
        /**
         * Called with the result bitmap.
         *
         * @param bitmap The result bitmap.
         */
        void onResult(Bitmap bitmap);
    }

    /**
     * Callback interface for retrieving an Attendee.
     */
    public interface GetAttendeeCallBack {
        /**
         * Called with the result attendee.
         *
         * @param attendee The result attendee.
         */
        void onResult(Attendee attendee);

        void onNoResult();
    }

    /**
     * Callback interface for retrieving all events.
     */
    public interface GetAllEventsCallBack {
        /**
         * Called with the result list of events.
         *
         * @param events The list of retrieved events.
         */
        void onResult(ArrayList<Event> events);
    }

    /**
     * Callback interface for handling event retrieval operations.
     */
    public interface GetEventCallback {
        /**
         * Called on successful retrieval of an event.
         *
         * @param event The event successfully retrieved.
         * @return The retrieved event.
         */
        Event onSuccess(Event event);

        /**
         * Called when event retrieval fails.
         *
         * @param errorMessage The error message describing the failure.
         */
        void onFailure(String errorMessage);
    }


    /**
     * Callback interface indicating completion of an operation.
     */
    public interface OnFinishedCallback{
        /**
         * Called when the operation is finished.
         */
        void onFinished();
    }

    /**
     * Callback interface for attempting a login operation.
     */
    public interface AttemptLoginCallback {
        /**
         * Called when the login operation results in success.
         */
        void onResult();

        /**
         * Called when the login operation does not result in success.
         */
        void onNoResult();
    }

    /**
     * Callback interface for retrieving an array of strings.
     */
    public interface StringArrayCallback {
        /**
         * Called with the result array of strings.
         *
         * @param array The array of strings.
         */
        void onResult(ArrayList<String> array);
    }

    /**
     * Callback interface for retrieving a token.
     */
    public interface GetTokenCallback{
        /**
         * Called with the result token.
         *
         * @param token The token string.
         */
        void onResult(String token);
    }

    /**
     * Callback interface for retrieving map markers.
     */
    public interface GetMapMarkersCallback {
        /**
         * Called with the result of map markers.
         *
         * @param checks The list of check-ins.
         * @param names  The list of names corresponding to the check-ins.
         */
        void onResult(ArrayList<CheckIn> checks, ArrayList<String> names);
    }

    //singleton
    private static FirebaseDB instance = null;
    private FirebaseFirestore db;
    private FirebaseStorage storage;
    private FirebaseMessaging messaging;
    private CollectionReference usersCollection;
    private CollectionReference eventsCollection;
    private CollectionReference checkInsCollection;
    private CollectionReference adminLoginsCollection;

    /**
     * Gets the collection reference for user documents.
     *
     * @return The Firestore collection reference for users.
     */
    public CollectionReference getUsersCollection() {
        return usersCollection;
    }

    /**
     * Sets the collection reference for user documents.
     *
     * @param usersCollection The new Firestore collection reference for users.
     */
    public void setUsersCollection(CollectionReference usersCollection) {
        this.usersCollection = usersCollection;
    }

    /**
     * Gets the collection reference for event documents.
     *
     * @return The Firestore collection reference for events.
     */
    public CollectionReference getEventsCollection() {
        return eventsCollection;
    }

    /**
     * Sets the collection reference for event documents.
     *
     * @param eventsCollection The new Firestore collection reference for events.
     */
    public void setEventsCollection(CollectionReference eventsCollection) {
        this.eventsCollection = eventsCollection;
    }

    /**
     * Gets the collection reference for check-in documents.
     *
     * @return The Firestore collection reference for check-ins.
     */
    public CollectionReference getCheckInsCollection() {
        return checkInsCollection;
    }

    /**
     * Sets the collection reference for check-in documents.
     *
     * @param checkInsCollection The new Firestore collection reference for check-ins.
     */
    public void setCheckInsCollection(CollectionReference checkInsCollection) {
        this.checkInsCollection = checkInsCollection;
    }

    /**
     * Gets the collection reference for admin login documents.
     *
     * @return The Firestore collection reference for admin logins.
     */
    public CollectionReference getAdminLoginsCollection() {
        return adminLoginsCollection;
    }

    /**
     * Sets the collection reference for admin login documents.
     *
     * @param adminLoginsCollection The new Firestore collection reference for admin logins.
     */
    public void setAdminLoginsCollection(CollectionReference adminLoginsCollection) {
        this.adminLoginsCollection = adminLoginsCollection;
    }

    /**
     * Gets the instance of the Firestore database.
     *
     * @return The Firestore database instance.
     */
    public FirebaseFirestore getDb() {
        return db;
    }

    /**
     * Sets the instance of the Firestore database.
     *
     * @param db The new Firestore database instance.
     */
    public void setDb(FirebaseFirestore db) {
        this.db = db;
    }

    /**
     * Gets the instance of Firebase Storage.
     *
     * @return The Firebase Storage instance.
     */
    public FirebaseStorage getStorage() {
        return storage;
    }

    /**
     * Sets the instance of Firebase Storage.
     *
     * @param storage The new Firebase Storage instance.
     */
    public void setStorage(FirebaseStorage storage) {
        this.storage = storage;
    }

    /**
     * Gets the instance of Firebase Messaging.
     *
     * @return The Firebase Messaging instance.
     */
    public FirebaseMessaging getMessaging() {
        return messaging;
    }

    /**
     * Sets the instance of Firebase Messaging.
     *
     * @param messaging The new Firebase Messaging instance.
     */
    public void setMessaging(FirebaseMessaging messaging) {
        this.messaging = messaging;
    }

    final String usersTAG = "Users";
    final String eventsTAG = "Events";
    final String imagesTAG = "Images";

    final String checkInsTag = "CheckIns";



    public static FirebaseDB getInstance(){
        if (instance == null){
            FirebaseFirestore firestoreInstance = FirebaseFirestore.getInstance();
            instance = new FirebaseDB(firestoreInstance, FirebaseStorage.getInstance(), FirebaseMessaging.getInstance(), firestoreInstance.collection("Users"), firestoreInstance.collection("Events"),
                    firestoreInstance.collection("CheckIns"), firestoreInstance.collection("Logins"));
        }
        return instance;
    }

    /**
     * This method is used to get a test instance of FirebaseDB that can use Mock Firebase Firestore/Storage/Messaging instances, to perform tests without hitting the actual database.
     */
    public static FirebaseDB getInstance(FirebaseFirestore firestoreInstance, FirebaseStorage firebaseStorageInstance, FirebaseMessaging firebaseMessagingInstance){
        return new FirebaseDB(firestoreInstance, firebaseStorageInstance, firebaseMessagingInstance, firestoreInstance.collection("Users"), firestoreInstance.collection("Events"),
                firestoreInstance.collection("CheckIns"), firestoreInstance.collection("Logins"));
    }

    //dependency injection doesn't work, because db is a static variable
    //consider refactoring FirebaseDB into a singleton with dependency injection
    //we don't want to mock FirebaseDB, we want to mock FirebaseFirestore.getInstance()
    private FirebaseDB(FirebaseFirestore firestoreInstance, FirebaseStorage storageInstance, FirebaseMessaging messagingInstance, CollectionReference usersCollection, CollectionReference eventsCollection,
                       CollectionReference checkInsCollection, CollectionReference adminLoginsCollection){
        this.db = firestoreInstance;
        this.storage = storageInstance;
        this.messaging = messagingInstance;
        this.usersCollection = usersCollection;
        this.eventsCollection = eventsCollection;
        this.checkInsCollection = checkInsCollection;
        this.adminLoginsCollection = adminLoginsCollection;
    }

    // Change String to Attendee class when someone implements it.

    /**
     * Adds a document that represents a user in the database
     *
     * @param user The user we want to add
     */
    public void addUser(Attendee user) {
        usersCollection
                .add(user)
                .addOnSuccessListener(documentReference -> {
//                        Log.d(usersTAG, "User document snapshot written with ID:" + documentReference.getId());
                    user.setDocumentId(documentReference.getId());
                    updateUser(user);
                })
                .addOnFailureListener(e -> {
//                        Log.w(usersTAG, "Error while adding user document", e);
                });
    }

    /**
     * Logs the user in depending on their unique identifier. Will create a new user if this is
     * the first time they have opened the app.
     *
     * @param userId The unique identifier of the user that has opened the app
     */

    public void loginUser(String userId, GetAttendeeCallBack callBack) {
        usersCollection
                .whereEqualTo("id", userId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        if (task.getResult() == null || task.getResult().isEmpty()) {
                            Attendee attendee = new Attendee(userId);
                            addUser(attendee);
                            callBack.onResult(attendee);
                        } else {
                            for (DocumentSnapshot documentSnapshot: task.getResult()) {
                                Attendee attendee = documentSnapshot.toObject(Attendee.class);
                                assert attendee != null;
                                attendee.setDocumentId(documentSnapshot.getId());
                                callBack.onResult(attendee);
                            }
                        }
                    } else {
                        Log.e("MainActivity", "Error trying to login");
                    }
                });
    }

    /**
     * Adds a document that represents an event in the database
     *
     * @param event The event we want to add
     */
    public void addEvent(Event event) {
        eventsCollection
                .add(event)
                .addOnSuccessListener(documentReference -> {
//                        Log.d(eventsTAG, "event document snapshot written with ID:" + documentReference.getId());
                    event.setDocumentId(documentReference.getId());
                    updateEvent(event);
                })
                .addOnFailureListener(e -> {
//                        Log.w(eventsTAG, "Error while adding event document", e);
                });
    }

    /**
     * Updates the document that represents the user in the database.
     *
     * @param user The user that needs their document updated.
     */
    public void updateUser(Attendee user) {
        usersCollection
                .document(user.getDocumentId())
                .update("name", user.getName(),
                        "email", user.getEmail(),
                        "geolocationOn", user.getGeolocationOn(),
                        "profilePicturePath", user.getProfilePicturePath(), "documentId", user.getDocumentId())
                .addOnSuccessListener(unused -> {
//                        Log.d(usersTAG, "User document updated successfully");
                })
                .addOnFailureListener(e -> {
//                        Log.w(usersTAG, "Error while updating user document", e);
                });
    }

    /**
     * Updates the document that represents the event in the database.
     *
     * @param event The event that needs its document updated.
     */
    public void updateEvent(Event event) {
        eventsCollection
                .document(event.getDocumentId())
                .update("announcements", event.getAnnouncements(),
                        "checkIns", event.getCheckIns(), "signUps",
                        event.getSignUps(), "posterPath", event.getPosterPath(),
                        "qrCode", event.getQrCode(), "documentId", event.getDocumentId(),
                        "organizerToken", event.getOrganizerToken())
                .addOnSuccessListener(unused -> {
//                        Log.d(eventsTAG, "Event document updated successfully");
                })
                .addOnFailureListener(e -> {
//                        Log.w(eventsTAG, "Error while updating event document", e);
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
    public void uploadImage(Uri file, String pathName) {
        StorageReference storageRef = storage.getReference();
        StorageReference storageReference = storageRef.child(pathName + ".jpg");

        storageReference.putFile(file)
                .addOnSuccessListener(taskSnapshot -> {
//                        Log.d(imagesTAG, "Successful image upload");
                })
                .addOnFailureListener(e -> {
//                        Log.d(imagesTAG, "Failed to upload image");
                });

    }

    /**
     * Retrieves an image from the database
     *
     * @param user This is the user we want to retrieve their profile picture
     * @param callBack This callBack will be used to get back bitmap
     */
    public void retrieveImage(Attendee user, GetBitmapCallBack callBack) {
        //ArrayList<Bitmap> localBitMap = new ArrayList<Bitmap>();
        try {
            StorageReference storageRef = storage.getReference(user.getProfilePicturePath() + ".jpg");
            File localFile = File.createTempFile(user.getProfilePicturePath().split("/")[1], "jpg");
            storageRef.getFile(localFile).addOnSuccessListener(taskSnapshot -> {
                callBack.onResult(BitmapFactory.decodeFile(localFile.getAbsolutePath()));
//                    Log.d(imagesTAG, "Successfully retrieved image");
            }).addOnFailureListener(exception -> {
//                    Log.w(imagesTAG, "Failed to retrieve image: " + exception);
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
    public void retrieveImage(Event event, GetBitmapCallBack callBack) {
        try {
            StorageReference storageRef = storage.getReference(event.getPosterPath() + ".jpg");
            File localFile = File.createTempFile(event.getPosterPath().split("/")[1], "jpg");
            storageRef.getFile(localFile).addOnSuccessListener(taskSnapshot -> {
                callBack.onResult(BitmapFactory.decodeFile(localFile.getAbsolutePath()));
                Log.d(imagesTAG, "Successfully retrieved image");
            }).addOnFailureListener(exception -> Log.w(imagesTAG, "Failed to retrieve image: " + exception));
        } catch (IOException exception) {
            Log.e(imagesTAG, "Error trying to retrieve image: " + exception);
        }
    }

    /**
     * Retrieves an image from the database
     *
     * @param event This is the event we're trying to get its poster.
     * @param callBack This callBack will be used to get back bitmap
     */
    public void retrieveImage(String path, GetBitmapCallBack callBack) {
        try {
            StorageReference storageRef = storage.getReference(path + ".jpg");
            File localFile = File.createTempFile(path.split("/")[1], "jpg");
            storageRef.getFile(localFile).addOnSuccessListener(taskSnapshot -> {
                callBack.onResult(BitmapFactory.decodeFile(localFile.getAbsolutePath()));
                Log.d(imagesTAG, "Successfully retrieved image");
            }).addOnFailureListener(exception -> Log.w(imagesTAG, "Failed to retrieve image: " + exception));
        } catch (IOException exception) {
            Log.e(imagesTAG, "Error trying to retrieve image: " + exception);
        }
    }

    /**
     * Deletes an image from the database
     *
     * @param pathName the pathname where we can find the file in the database storage
     */
    public void deleteImage(String pathName){
        StorageReference storageRef = storage.getReference();
        StorageReference storageReference = storageRef.child(pathName + ".jpg");

        storageReference.delete().addOnSuccessListener(unused -> {

        }).addOnFailureListener(e -> {
            //TODO: handle bad attempts to delete
        });
    }

    /**
     * Retrieves all events in the events collection
     *
     * @param eventList         The list we're going to hold the events in.
     * @param eventArrayAdapter The ArrayAdapter of eventList.
     */
    public void getAllEvents(ArrayList<Event> eventList, ArrayAdapter<Event> eventArrayAdapter) {
        eventsCollection
                .get()
                .addOnCompleteListener(task -> {
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
                            String organizerToken = (String) document.getData().get("organizerToken");
                            Long maxAttendeesLong = (Long) document.getData().get("maxAttendees");
                            Integer maxAttendees = maxAttendeesLong != null ? Math.toIntExact(maxAttendeesLong) : null;
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

                            Event event = new Event(id, name, organizerId, details, location, startDate, endDate, geolocationOn, posterPath, qrCode, qrCodePromo, organizerToken, announcements, signUps, checkIns, maxAttendees);
                            eventList.add(event);
                        }
                        eventArrayAdapter.notifyDataSetChanged();
                    } else {
                        Log.d(eventsTAG, "Error getting documents: ", task.getException());
                    }
                });

    }

    /**
     * Retrieves all events from the Firestore database and constructs a list of {@link Event} objects.
     * Each event document is transformed into an {@link Event} object which is then added to a list. Once all documents
     * have been processed, this list is passed to the provided {@link GetAllEventsCallBack} callback.
     * <p>
     * If the operation is successful, {@link GetAllEventsCallBack#onResult(ArrayList)} is called with the list of events.
     * In case of failure, an error log is generated and no further action is taken. The callback is not called with a failure indicator,
     * which means error handling should be managed externally based on application needs.
     *
     * @param callBack The {@link GetAllEventsCallBack} callback to handle the result. This callback gets a list of {@link Event}
     *                 objects on successful retrieval, allowing further processing such as updating UI or handling event data.
     */
    public void getAllEvents(GetAllEventsCallBack callBack) {
        ArrayList<Event> eventList = new ArrayList<>();
        eventsCollection
                .get()
                .addOnCompleteListener(task -> {
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
                            String organizerToken = (String) document.getData().get("organizerToken");
                            Long maxAttendeesLong = (Long) document.getData().get("maxAttendees");
                            Integer maxAttendees = maxAttendeesLong != null ? Math.toIntExact(maxAttendeesLong) : null;
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

                            Event event = new Event(id, name, organizerId, details, location, startDate, endDate, geolocationOn, posterPath, qrCode, qrCodePromo, organizerToken, announcements, signUps, checkIns, maxAttendees);
                            eventList.add(event);
                        }
                        callBack.onResult(eventList);
                    } else {
                        Log.d(eventsTAG, "Error getting documents: ", task.getException());
                    }
                });

    }

    /**
     * Retrieves all users in the users collection
     *
     * @param attendeeList         The list we're going to hold the users in.
     * @param attendeeArrayAdapter The ArrayAdapter of attendeeList.
     */
    public void getAllUsers(ArrayList<Attendee> attendeeList, ArrayAdapter<Attendee> attendeeArrayAdapter) {
        usersCollection
                .get()
                .addOnCompleteListener(task -> {
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
                });

    }

    /**
     * Retrieves all users in the users collection, but does not store them into an Array Adapter.
     *
     * @param attendeeList         The list we're going to hold the users in.
     * @param callback The OnFinishedCallback that we will invoke once firebase is done it's operation
     */
    public void getAllUsers(ArrayList<Attendee> attendeeList, OnFinishedCallback callback) {
        usersCollection
                .get()
                .addOnCompleteListener(task -> {
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
                        }
                        callback.onFinished();
                    } else {
                        Log.d(usersTAG, "Error getting documents: ", task.getException());
                    }
                });

    }

    /**
     * Retrieves all the events an user has signed up to
     *
     * @param user the user who as signed up to events
     * @param eventArrayList the list you want to add the events to
     */
    public void getAttendeeSignedUpEvents(Attendee user, ArrayList<Event> eventArrayList, HomeSignedUpListAdapter adapter) {
        eventsCollection
                .whereArrayContains("signUps", user.getDocumentId())
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        Event event = documentSnapshot.toObject(Event.class);
                        assert event != null;
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
                })
                .addOnFailureListener(e -> Log.w(eventsTAG, "Failed to retrieve events of user"));
    }

    /**
     * Retrieves all the events an user has checked in to
     *
     * @param user the user who as checked in to events
     * @param eventList the list passed in to get the events
     * @param adapter the adapter used to update the ListView
     */
    public void getEventsCheckedIn(Attendee user, ArrayList<Event> eventList, HomeCheckedInListAdapter adapter) {
        ArrayList<String> myCheckIns = new ArrayList<>();
        checkInsCollection
                .whereEqualTo("attendeeDocId", user.getDocumentId())
                .get()
                .addOnCompleteListener(task -> {
                    for (DocumentSnapshot documentSnapshot : task.getResult()) {
                        myCheckIns.add((String) documentSnapshot.get("eventDocId"));
                    }
                    if (!myCheckIns.isEmpty()) {
                        eventsCollection.whereIn("documentId", myCheckIns).get()
                                .addOnCompleteListener(task1 -> {
                                    if (task1.isSuccessful()) {
                                        for (DocumentSnapshot document : task1.getResult()) {
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
                                            String organizerToken = (String) document.getData().get("organizerToken");
                                            Long maxAttendeesLong = (Long) document.getData().get("maxAttendees");
                                            Integer maxAttendees = maxAttendeesLong != null ? Math.toIntExact(maxAttendeesLong) : null;
                                            ArrayList<String> announcements = (ArrayList<String>) document.getData().get("announcements");
                                            if (announcements == null) {
                                                announcements = new ArrayList<String>();
                                            }
                                            ArrayList<String> signUps = (ArrayList<String>) document.getData().get("signUps");
                                            if (signUps == null) {
                                                signUps = new ArrayList<String>();
                                            }
                                            ArrayList<String> checkIns = (ArrayList<String>) document.getData().get("checkIns");
                                            if (checkIns == null) {
                                                checkIns = new ArrayList<String>();
                                            }

                                            Event event = new Event(id, name, organizerId, details, location, startDate, endDate, geolocationOn, posterPath, qrCode, qrCodePromo, organizerToken, announcements, signUps, checkIns, maxAttendees);
                                            eventList.add(event);
                                        }
                                        adapter.notifyDataSetChanged();
                                    } else {
                                        Log.d("Firestore", "Error getting documents: ", task1.getException());
                                    }
                                });
                    }
                });

    }

    /**
     * Retrieves all the attendees that have signed up an event
     *
     * @param event the event we're getting the attendees
     * @param attendeeArrayList the list passed in to get the attendees
     */
    public void getEventSignedUp(Event event, ArrayList<Attendee> attendeeArrayList) {
        for (String signUps : event.getSignUps()) {
            usersCollection
                    .document(signUps)
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        Attendee attendee = documentSnapshot.toObject(Attendee.class);
                        assert attendee != null;
                        attendee.setDocumentId(documentSnapshot.getId());
                        attendeeArrayList.add(attendee);
                    })
                    .addOnFailureListener(e -> Log.w(usersTAG, "Failed " + e));
        }
    }

    /**
     * Retrieves all the attendees that have checked in an event
     *
     * @param event the event we're getting the attendees
     * @param attendeeArrayList the list passed in to get the events
     */
    public void getEventCheckedIn(Event event, ArrayList<Attendee> attendeeArrayList,  ArrayAdapter<Attendee> attendeeArrayAdapter) {
        checkInsCollection
                .whereEqualTo("eventDocId", event.getDocumentId())
                .get()
                .addOnCompleteListener(task -> {
                    for (DocumentSnapshot documentSnapshot : task.getResult()) {
                        usersCollection
                                .document((String) Objects.requireNonNull(documentSnapshot.get("attendeeDocId")))
                                .get()
                                .addOnSuccessListener(documentSnapshot1 -> {
                                    Attendee attendee = documentSnapshot1.toObject(Attendee.class);
                                    assert attendee != null;
                                    attendee.setDocumentId(documentSnapshot1.getId());
                                    attendeeArrayList.add(attendee);
                                })
                                .addOnFailureListener(e -> Log.w(usersTAG, "Failed " + e));

                    }
                    attendeeArrayAdapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> Log.w(usersTAG, "Failed " + e));

    }

    /** This returns the name of the user with that document id
     * @param userDocumentId This the document id of the user
     * @param callBack This callBack will be used to get back the name of user
     */
    public void getUserName(String userDocumentId, GetStringCallBack callBack) {
        usersCollection.document(userDocumentId).get()
                .addOnSuccessListener(documentSnapshot -> {
                    Log.d(usersTAG, "Success");
                    callBack.onResult((String) documentSnapshot.get("name"));
                })
                .addOnFailureListener(e -> Log.w(usersTAG, "Failed "+e));
    }

    /**
     * \
     * Check whether or not the content of a QR code is already in use by an event created in the app.
     *
     * @param qrContent a string that represents the content field of the QR code we are checking
     * @param mode an integer: 0 if we are checking promotional qr codes, 1 if we are checking checkin qr codes
     * @param callBack a class that implements the UniqueCheckCallBack that will allow you to handle the boolean result if there is an already existing event with the qr code.
     */
    public void checkUnique(String qrContent, int mode, UniqueCheckCallBack callBack) {
        String field;
        if (mode == 0) {
            field = "qrCodePromo";
        } else {
            field = "qrCode";
        }
        eventsCollection
                .whereEqualTo(field, qrContent)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        callBack.onResult(task.getResult() == null || task.getResult().isEmpty());

                    } else {
                        Log.e("MainActivity", "Error checking existing Event QR codes");
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
    public void findEventWithQR(String qrContent, int mode, MatchingQRCallBack callBack) {
        String field;
        if (mode == 0) {
            field = "qrCodePromo";
        } else {
            field = "qrCode";
        }
        eventsCollection
                .whereEqualTo(field, qrContent)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        if (task.getResult() == null || task.getResult().isEmpty()) {
                            //if no event with a matching qr code was found, tell the callback
                            callBack.onNoResult();
                        } else{
                            for (DocumentSnapshot documentSnapshot : task.getResult()) {
                                Event event = documentSnapshot.toObject((Event.class));
                                assert event != null;
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
    public void getEventsMadeByUser(Attendee user, ArrayList<Event> eventArrayList, EventListAdapter adapter) {
        eventsCollection
                .whereEqualTo("organizerId", user.getDocumentId())
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        Event event = documentSnapshot.toObject(Event.class);
                        assert event != null;
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
                })
                .addOnFailureListener(e -> Log.w(eventsTAG, "Failed to retrieve events of user"));
    }

    /**
     * Adds a new {@link CheckIn} object to the Firestore database within the `checkInsCollection`.
     * Upon successful addition, the method assigns the Firestore-generated document ID back to the {@link CheckIn} object
     * and calls {@code updateCheckIn} to update any necessary information within the database based on the newly added check-in.
     *
     * @param checkIn The {@link CheckIn} object to be added to the Firestore database. This object contains all necessary
     *                information about the check-in, such as attendee ID, event ID, and geolocation coordinates.
     */
    public void addCheckIn(CheckIn checkIn) {
        checkInsCollection
                .add(checkIn)
                .addOnSuccessListener(documentReference -> {
                    checkIn.setDocumentId(documentReference.getId());
                    updateCheckIn(checkIn);
                });
    }

    /**
     * Updates the specified check-in document in the Firestore database with new information from the provided {@link CheckIn} object.
     * This method updates the check-in document with the latest data, such as attendee document ID, event document ID,
     * geolocation coordinates, and the number of check-ins.
     *
     * @param checkIn The {@link CheckIn} object containing updated information to be stored in the database.
     */
    public void updateCheckIn(CheckIn checkIn) {
        checkInsCollection
                .document(checkIn.getDocumentId())
                .update("attendeeDocId", checkIn.getAttendeeDocId(),
                        "documentId", checkIn.getDocumentId(),
                        "eventDocId", checkIn.getEventDocId(), "longitude", checkIn.getLongitude(),
                        "latitude",checkIn.getLatitude() ,"numberOfCheckIns", checkIn.getNumberOfCheckIns())
                .addOnSuccessListener(unused -> Log.d(usersTAG, "CheckIn document updated successfully"))
                .addOnFailureListener(e -> Log.w(usersTAG, "Error while updating check in document", e));
    }

    /**
     * Checks if a check-in already exists for a given attendee and event combination in the Firestore database.
     * This method queries the `checkInsCollection` to find any existing check-in documents that match the specified
     * attendee and event document IDs. The result of this query is passed to the provided {@link UniqueCheckInCallBack}.
     *
     * @param eventDocId  The document ID of the event to check for an existing check-in.
     * @param attendeeDocId The document ID of the attendee to check for an existing check-in.
     * @param callBack    The {@link UniqueCheckInCallBack} callback to handle the result of the check. The callback
     *                    will be invoked with a boolean indicating whether the check-in is unique (i.e., does not exist)
     *                    and with the {@link CheckIn} object if an existing check-in is found.
     */
    public void checkInAlreadyExists(String eventDocId, String attendeeDocId, UniqueCheckInCallBack callBack) {
        checkInsCollection
                .whereEqualTo("attendeeDocId", attendeeDocId)
                .whereEqualTo("eventDocId", eventDocId)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    Log.d("checkInAlreadyExists", String.valueOf(queryDocumentSnapshots.isEmpty()));
                    CheckIn checkInToReturn = null;
                    if (!(queryDocumentSnapshots.isEmpty())){
                        if (queryDocumentSnapshots.getDocuments().size() == 1){
                            for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()){
                                checkInToReturn = documentSnapshot.toObject(CheckIn.class);
                            }
                        } else{
                            //TODO: handle this bad error, where there are duplicate checkins in firebase
                        }
                    }
                    //can return null if no checkin alredy exists, or if we get a bad checkin (duplicates exist)
                    callBack.onResult(queryDocumentSnapshots.isEmpty(), checkInToReturn);
                });
    }

    //i suggest this function be used primarily for new checkIns, because we need to also add it to the event

    /**
     * This functions creates a new checkIn for the user that is joining the event, and adds the documentID of the checkIn to the event's field in firebase
     * @param checkIn the object representing the checkIn. this holds the document ID of the event, and attendee that is checking in
     * @param event the event we are changing
     */
    public void addCheckInToEvent(CheckIn checkIn, Event event) {
        checkInsCollection
                .add(checkIn)
                .addOnSuccessListener(documentReference -> {
                    checkIn.setDocumentId(documentReference.getId());
                    //update checkIn to get the document ID set in the field for future accesses
                    updateCheckIn(checkIn);
                    //we delete the signup from the event's field
                    event.deleteSignUp(checkIn.getAttendeeDocId());
                    //we add the checkIn and update our event :)
                    event.addCheckIn(checkIn.getDocumentId());
                    updateEvent(event);
                });

    }


    /**
     * Retrieves the paths of all posters
     *
     * @param postersPaths The list to be populated with poster paths
     * @param callback the OnFinishedCallback to invoke when firebase has completed it's operation
     */
    public void getPostersPaths(ArrayList<String> postersPaths, OnFinishedCallback callback) {
        storage.getReference().child("poster")
                .listAll()
                .addOnSuccessListener(listResult -> {
                    for (StorageReference poster : listResult.getItems()) {
                        postersPaths.add(poster.getPath());
                    }
                    callback.onFinished();
                })
                .addOnFailureListener(e -> Log.w(imagesTAG, "Error trying to get posters' paths" + e));
    }

    /**
     * Retrieves the paths of all profile pictures
     *
     * @param profilesPaths The list to be populated with profile picture paths
     * @param callback the OnFinishedCallback to invoke when firebase has completed it's operation
     */
    public void getProfilePicturesPaths(ArrayList<String> profilesPaths, OnFinishedCallback callback) {
        storage.getReference().child("profile")
                .listAll()
                .addOnSuccessListener(listResult -> {
                    for (StorageReference profile : listResult.getItems()) {
                        profilesPaths.add(profile.getPath());
                    }
                    callback.onFinished();
                })
                .addOnFailureListener(e -> Log.w(imagesTAG, "Error trying to get profile pictures' paths" + e));
    }

    /**
     * This function gets the paths of all poster images, and profile picture images
     * @param imagePaths The ArrayList to store all of the paths
     * @param callback The callback to invoke once firebase has finished this operation
     */
    public void getAllPicturesPaths(ArrayList<String> imagePaths, OnFinishedCallback callback){
        getPostersPaths(imagePaths, () -> getProfilePicturesPaths(imagePaths, () -> callback.onFinished()));
    }

    /**
     * Retrieves an image from the database (administrator version)
     *
     * @param imagePath This is the path of the image we're trying to get (has the file extension)
     * @param callBack This callBack will be used to get back bitmap
     */
    public void adminRetrieveImage(String imagePath, GetBitmapCallBack callBack) {
        try {
            StorageReference storageRef = storage.getReference(imagePath);
            String path = imagePath.substring(0, imagePath.lastIndexOf("."));
            File localFile = File.createTempFile(path.split("/")[1], "jpg");
            storageRef.getFile(localFile).addOnSuccessListener(taskSnapshot -> {
                callBack.onResult(BitmapFactory.decodeFile(localFile.getAbsolutePath()));
                Log.d(imagesTAG, "Successfully retrieved image");
            }).addOnFailureListener(exception -> Log.w(imagesTAG, "Failed to retrieve image: " + exception));
        } catch (IOException exception) {
            Log.e(imagesTAG, "Error trying to retrieve image: " + exception);
        }

    }

    /**
     * Deletes the document that represents the event and all check ins related to the event
     *
     * @param event the event to be deleted
     */
    public void deleteEvent(Event event) {
        eventsCollection
                .document(event.getDocumentId())
                .delete()
                .addOnSuccessListener(unused -> Log.d(eventsTAG, "Successfully deleted the event"))
                .addOnFailureListener(e -> Log.w(eventsTAG, "Failed to deleted event" + e));

        checkInsCollection
                .whereEqualTo("eventDocId", event.getDocumentId())
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (DocumentSnapshot documentSnapshot : task.getResult()) {
                            checkInsCollection.document(documentSnapshot.getId()).delete();
                        }
                    }
                })
                .addOnFailureListener(e -> Log.w(eventsTAG, "Error trying to delete check Ins: " + e));
    }

    /**
     * Deletes an image in the storage (administrator version) and clears the image path field of whatever user/event had that image
     *
     * @param imagePath This is the path of the image we're trying to get (has the file extension)
     */
    public void deleteImageAdmin(String imagePath, OnFinishedCallback callback) {
        storage.getReference()
                .child(imagePath)
                .delete()
                .addOnSuccessListener(unused -> {
                    Log.d(imagesTAG, "Deleted image (admin) successfully");
                    //we look for the document (attendee or event) that has this imagePath in their posterPath field or profilePicturePathField
                    Log.d("test", imagePath.substring(1, imagePath.length() - 4));
                    if (imagePath.substring(1, 7).equals("poster")){
                        eventsCollection
                                .whereEqualTo("posterPath", imagePath.substring(1, imagePath.length() - 4))
                                .get()
                                .addOnCompleteListener(task -> {
                                    if (task.isSuccessful()) {
                                        if (task.getResult() == null || task.getResult().isEmpty()) {

                                        } else {
                                            for (DocumentSnapshot documentSnapshot: task.getResult()) {
                                                Event event = documentSnapshot.toObject(Event.class);
                                                assert event != null;
                                                event.setPosterPath(null);
                                                updateEvent(event);

                                            }
                                        }
                                        callback.onFinished();
                                    } else {
                                        Log.e("deleteImageAdmin", "Error trying to find the event that had this image.");
                                        callback.onFinished();
                                    }
                                });
                    } else{
                        usersCollection
                                .whereEqualTo("profilePicturePath", imagePath.substring(1, imagePath.length() - 4))
                                .get()
                                .addOnCompleteListener(task -> {
                                    if (task.isSuccessful()) {
                                        if (task.getResult() == null || task.getResult().isEmpty()) {

                                        } else {
                                            for (DocumentSnapshot documentSnapshot: task.getResult()) {
                                                Attendee attendee = documentSnapshot.toObject(Attendee.class);
                                                assert attendee != null;
                                                attendee.setProfilePicturePath(null);
                                                updateUser(attendee);
                                            }

                                        }
                                        callback.onFinished();
                                    } else {
                                        Log.e("deleteImageAdmin", "Error trying to find the attendee that had this image.");
                                        callback.onFinished();
                                    }
                                });
                    }
                })
                .addOnFailureListener(e -> Log.w(imagesTAG, "Failed to delete image: " + e));
    }

    /**
     * Removes the profile of a user
     * @param attendee The user whose profile we want to remove
     */
    public void deleteProfile(Attendee attendee) {
        attendee.setEmail(null);
        attendee.setName("Guest24");
        if (attendee.getProfilePicturePath() != null){
            deleteImage(attendee.getProfilePicturePath());
        }
        attendee.setProfilePicturePath(null);

        updateUser(attendee);
    }

    /**
     * Gets users from the list of users field in each event
     * Have to pass along the event class from EventDetailsFragment to AttendeeList so that it knows which event to get the checked-in users from
     */
    public void getEventCheckedInUsers(Event event, ArrayList<Attendee> attendeeDataList, ArrayAdapter<Attendee> attendeeListAdapter) {
        for(int i = 0; i < event.getCheckIns().size(); i++) {
            checkInsCollection
                    .whereEqualTo("eventDocId", event.getDocumentId()) //Finds document with the QR code of event clicked on
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                // check if the checked in user has a name that exists

                                String documentId = document.getId();
                                String id = (String) document.getData().get("attendeeDocId");
                                String name = (String) document.getData().get("name");
                                String email = (String) document.getData().get("email");
                                String profilePicturePath = (String) document.getData().get("profilePicturePath");
                                Boolean geolocationOn = (Boolean) document.getData().get("geolocationOn");
                                long checkins = (long) document.getData().get("numberOfCheckIns"); // changed to type long
                                Attendee attendee = new Attendee(id, documentId, name, email, profilePicturePath, geolocationOn, checkins);
                                attendeeDataList.add(attendee);

                            }
                            attendeeListAdapter.notifyDataSetChanged();
                        }
                    })
                    .addOnFailureListener(e -> Log.w(eventsTAG, "Error trying to get the checked-in users: " + e));
        }
    }
    /**
     * Gets users from the list of signed up users field in each event
     * Have to pass along the event class from EventDetailsFragment to AttendeeList so that it knows which event to get the checked-in users from
     */
    public void getEventSignedUpUsers(Event event, ArrayList<Attendee> attendeeDataList, ArrayAdapter<Attendee> attendeeListAdapter) {
        for(int i = 0; i < event.getSignUps().size(); i++){
            usersCollection
                    .whereEqualTo("documentId", event.getSignUps().get(i))
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String documentId = document.getId();
                                String id = (String) document.getData().get("id");
                                String name = (String) document.getData().get("name");
                                String email = (String) document.getData().get("email");
                                String profilePicturePath = (String) document.getData().get("profilePicturePath");
                                Boolean geolocationOn = (Boolean) document.getData().get("geolocationOn");

//                                Attendee attendee = document.toObject(Attendee.class);
                                Attendee attendee = new Attendee(id, documentId, name, email, profilePicturePath, geolocationOn);
                                attendeeDataList.add(attendee);
                            }
                            attendeeListAdapter.notifyDataSetChanged();
                        }
                    })
                    .addOnFailureListener(e -> Log.w(eventsTAG, "Error trying to get the checked-in users: " + e));

        }
    }
  
     /**
     * This function looks for a document with matching admin login details to the user's input
     * @param username The username input by the user
     * @param password The password input by the user
     * @param callback The callback we invoked to tell if the user input valid or invalid admin login credentials
     */
    public void attemptAdminLogin(String username, String password, AttemptLoginCallback callback){
        adminLoginsCollection
                .whereEqualTo("user", username)
                .whereEqualTo("pass", password)

                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        //if there is no matching adminLogin document, we do not log the user in
                        if (task.getResult() == null || task.getResult().isEmpty()) {
                            callback.onNoResult();
                        } else {
                            //if there is a matching adminLogin document, we log the user in to the admin screen
                            callback.onResult();
                        }
                    } else {
                        Log.e("MainActivity", "Error trying to login");
                    }
                });

    }

    /**
     * Checks if a specific user has checked into a specific event.
     * Queries the check-ins collection to find any check-ins that match both the attendee's document ID
     * and the event's document ID. The result of the query (whether any check-ins were found) is passed
     * to the provided {@link UniqueCheckCallBack}.
     *
     * @param user      The {@link Attendee} who is being checked.
     * @param event     The {@link Event} to check the attendee against.
     * @param callBack  The {@link UniqueCheckCallBack} to handle whether the attendee has checked into the event.
     */
    public void userCheckedIntoEvent(Attendee user, Event event, UniqueCheckCallBack callBack) {
        checkInsCollection
                .whereEqualTo("attendeeDocId", user.getDocumentId())
                .whereEqualTo("eventDocId", event.getDocumentId())
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> callBack.onResult(!queryDocumentSnapshots.isEmpty()));
    }

    /**
     * Retrieves the geolocation data for users who have checked into a specific event.
     * This method queries the check-ins collection for all check-ins associated with the given event's document ID,
     * extracting the latitude and longitude for each check-in. These coordinates are added to the provided
     * latitude and longitude lists. Additional attendee data, such as document IDs, are added to the attendeeDataList.
     *
     * @param event              The {@link Event} for which to retrieve checked-in users' geolocations.
     * @param attendeeDataList   An {@link ArrayList} to hold data of the attendees who have checked in.
     * @param latitudeList       An {@link ArrayList} to hold the latitude values of the checked-in locations.
     * @param longitudeList      An {@link ArrayList} to hold the longitude values of the checked-in locations.
     */
    public void getEventCheckedInUsersGeoLocation(Event event, ArrayList<String> attendeeDataList, ArrayList latitudeList, ArrayList longitudeList) {
        checkInsCollection
                .whereEqualTo("eventDocId", event.getDocumentId()) //Finds document with the QR code of event clicked on
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            // check if the checked in user has a name that exists
                            String documentId = document.getId();
                            double latitude = (double) document.getData().get("latitude");
                            double longitude = (double) document.getData().get("longitude");
                            attendeeDataList.add(documentId);
                            latitudeList.add(latitude);
                            longitudeList.add(longitude);

                        }
                    }
                })
                .addOnFailureListener(e -> Log.w(eventsTAG, "Error trying to get the checked-in users: " + e));
    }

    /**
     * Retrieves the current instance's Firebase Cloud Messaging (FCM) token.
     * This token is used to uniquely identify the app instance for push notifications. The retrieved token
     * is passed to the provided {@link GetTokenCallback}.
     *
     * @param callback The {@link GetTokenCallback} to handle the retrieved FCM token.
     */
    public void getToken(GetTokenCallback callback){
        messaging
                .getToken()
                .addOnCompleteListener(task -> {
                    callback.onResult(task.getResult());
                    Log.d("token", task.getResult());
                })
                .addOnFailureListener(e -> Log.d("token", "failed to get token"));
    }

    /**
     * This function subscribes a user to the topic that FCM will send new announcements to as push notifications
     * @param topicName The name of the topic in FCM that the user is subscribing to
     */
    public void subscribeAttendeeToEventTopic(String topicName){
        messaging
                .subscribeToTopic(topicName)
                .addOnCompleteListener((OnCompleteListener) task -> Log.d("topic", "successfully subscribed user to event topic"))
                .addOnFailureListener(e -> Log.d("topic", "failed to subscribe user to event topic"));
    }

    /**
     * Retrieves an event by its unique identifier from the Firestore database.
     * This method queries the Firestore database for an event document using the specified event ID. If the
     * document is successfully retrieved and exists, it constructs an {@link Event} object from the document's
     * data and invokes the {@link GetEventCallback#onSuccess(Event)} method of the provided callback with the
     * event object. If the document does not exist or an error occurs during the query, the appropriate failure
     * method of the callback is invoked.
     *
     * @param eventId  The unique identifier of the event to retrieve.
     * @param callback The {@link GetEventCallback} implementation to handle the response. The onSuccess method is
     *                 called with the event object if the event is found. The onFailure method is called with an
     *                 error message if the event is not found or if an error occurs during the database query.
     */
    public void getEventById(String eventId, GetEventCallback callback) {
        eventsCollection.document(eventId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            String id = document.getId();
                            String name = document.getString("name");
                            String organizerId = document.getString("organizerId");
                            String details = document.getString("details");
                            String location = document.getString("location");
                            String startDate = document.getString("startDate");
                            String endDate = document.getString("endDate");
                            Boolean geolocationOn = document.getBoolean("geolocationOn");
                            String posterPath = document.getString("posterPath");
                            String qrCode = document.getString("qrCode");
                            String qrCodePromo = document.getString("qrCodePromo");
                            String organizerToken = document.getString("organizerToken");
                            Long maxAttendeesLong = (Long) document.getData().get("maxAttendees");
                            Integer maxAttendees = maxAttendeesLong != null ? Math.toIntExact(maxAttendeesLong) : null;
                            ArrayList<String> announcements = (ArrayList<String>) document.get("announcements");
                            ArrayList<String> signUps = (ArrayList<String>) document.get("signUps");
                            ArrayList<String> checkIns = (ArrayList<String>) document.get("checkIns");

                            Event event = new Event(id, name, organizerId, details, location, startDate, endDate, geolocationOn, posterPath, qrCode, qrCodePromo, organizerToken, announcements, signUps, checkIns, maxAttendees);
                            callback.onSuccess(event);
                        } else {
                            Log.d(eventsTAG, "No such event exists");
                            callback.onFailure("No such event exists");
                        }
                    } else {
                        Log.d(eventsTAG, "Error getting event document: ", task.getException());
                        callback.onFailure("Error getting event document: " + task.getException().getMessage());
                    }
                });
    }


    /**
     * Gets two lists, one for the CheckIn instances and one for the names of the attendees
     *
     * @param event the event we want to get the check in locations
     * @param callback The callback we invoked to return the two lists
     * */
    public void getGeolocations(Event event, GetMapMarkersCallback callback) {
        ArrayList<CheckIn> checkIns = new ArrayList<>();
        ArrayList<String> names = new ArrayList<>();
        AtomicInteger tasksCount = new AtomicInteger(event.getCheckIns().size());
        for (String checkIn : event.getCheckIns()) {
            checkInsCollection.document(checkIn).get()
                    .addOnSuccessListener(documentSnapshot1 -> {
                        String docId = (String) documentSnapshot1.get("attendeeDocId");
                        assert docId != null;
                        usersCollection.document(docId)
                                .get()
                                .addOnSuccessListener(documentSnapshot2 -> {
                                    Boolean geolocationOn = (Boolean) documentSnapshot2.get("geolocationOn");
                                    if (Boolean.TRUE.equals(geolocationOn)) {
                                        checkIns.add(documentSnapshot1.toObject(CheckIn.class));
                                        names.add((String) documentSnapshot2.get("name"));
                                    }
                                    if (tasksCount.decrementAndGet() == 0) {
                                        callback.onResult(checkIns, names);
                                    }
                                })
                                .addOnFailureListener(e -> Log.w(checkInsTag, "Something went wrong" + e));
                    })
                    .addOnFailureListener(e -> Log.w(checkInsTag, "Something went wrong" + e));
        }
    }
}