package com.example.qrazyqrsrus;

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

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Map;

public class FirebaseDB {

    static FirebaseFirestore db = FirebaseFirestore.getInstance();
    static FirebaseStorage storage = FirebaseStorage.getInstance();
    static CollectionReference usersCollection = db.collection("Users");
    static CollectionReference eventsCollection = db.collection("Events");

    final static String usersTAG = "Users";
    final static String eventsTAG = "Events";
    final static String imagesTAG = "Images";

    // Change String to Attendee class when someone implements it.
    /**
     * Adds a document that represents a user in the database
     * @param user The user we want to add
     * */
    public static void addUser(String user) {
        usersCollection
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(usersTAG, "User document snapshot written with ID:" + documentReference.getId());
                        // user.setDocumentId(documentReference.getId())
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
     * @param userId The unique identifier of the user that has opened the app
     * */
    public static void loginUser(String userId) {
        // Attendee user;
        usersCollection
                .whereEqualTo("id", userId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            int i = 0;
                            for (DocumentSnapshot documentSnapshot: task.getResult()) {
                                // user = documentSnapshot.toObject(Attendee.class);
                                i += 1;
                            }
                            if (i == 0) {
                                // This means that Attendee needs a constructor where it only accepts userId and sets the rest to default
                                // user = new Attendee(userId);
                                // addUser(user);
                            }
                        }
                        else {
                            Log.e("MainActivity", "Error trying to login");
                        }
                    }
                });
        // return user
    }

    /**
     * Adds a document that represents an event in the database
     * @param event The event we want to add
     * */
    public static void addEvent(Event event) {
        eventsCollection
                .add(event)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(eventsTAG, "event document snapshot written with ID:" + documentReference.getId());
                        // event.setId(documentReference.getId())
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
     * @param user The user that needs their document updated.
     * */
    public static void updateUser(String user) {
        usersCollection
                .document("user.getDocumentId()")
                .update("name", "Attendee.getName()",
                        "email", "Attendee.getEmail()",
                        "geolocationOn", "Attendee.getGeolocationOn()",
                        "profilePicturePath", "Attendee.getProfilePicturePath()")
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
     * @param event The event that needs its document updated.
     * */
    public static void updateEvent(Event event) {
        eventsCollection
                .document("event.getId()")
                .update("announcements", "event.getAnnouncements()",
                        "checkIns", "event.getCheckIns()", "signUps",
                        "event.getSignUps()", "posterPath", "event.getPosterPath()",
                        "qrCodePath", "event.qrCodePath()")
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
     * @param file The file in the local system
     * @param pathName This is the path of the image. In the form {folder}/{image} where
     *                 folder is either profiles, posters, qrcodes, and image is just the name of
     *                 the image
     * */
    public static void uploadImage(Uri file, String pathName) {
        StorageReference storageRef = storage.getReference();
        StorageReference storageReference = storageRef.child(pathName+".jpg");

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
     * @param user This is the user we want to retrieve their profile picture
     * */
    public void retrieveImage(String user) { // Change this to Attendee class when implemented
//        try {
//            StorageReference storageRef = storage.getReference(user.getProfilePicturePath()+".jpg");
//            File localFile = File.createTempFile(user.getProfilePicturePath().split("/")[1], "jpg");
//            storageRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
//                @Override
//                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
//                    user.setProfilePicture(localFile);
//                    Log.d(imagesTAG, "Successfully retrieved image");
//                }
//            }).addOnFailureListener(new OnFailureListener() {
//                @Override
//                public void onFailure(@NonNull Exception exception) {
//                    Log.w(imagesTAG, "Failed to retrieve image: " + exception);
//                }
//            });
//        }
//        catch (IOException exception) {
//            Log.e(imagesTAG, "Error trying to retrieve image: " + exception);
//        }
    }
    /**
     * Retrieves an image from the database
     * @param event This is the event we're trying to get its image.
     * @param imageType This string clarifies what type of File we're retrieving, it could be
     *                  a poster, a QR code, or a promotion QR code
     * */
    public void retrieveImage(Event event, String imageType) {
//        try {
//            StorageReference storageRef = storage.getReference(event.getProfilePicturePath()+".jpg");
//            File localFile = File.createTempFile(event.getProfilePicturePath().split("/")[1], "jpg");
//            storageRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
//                @Override
//                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
//                    if (imageType.equals("poster")){
//                        event.setPosterImage(localFile);
//                    } else if (imageType.equals("qrCode")) {
//                        event.setQrCode(localFile);
//                    }
//                    else {
//                        event.setPromoQrCode(localFile);
//                    }
//                    Log.d(imagesTAG, "Successfully retrieved image");
//                }
//            }).addOnFailureListener(new OnFailureListener() {
//                @Override
//                public void onFailure(@NonNull Exception exception) {
//                    Log.w(imagesTAG, "Failed to retrieve image: " + exception);
//                }
//            });
//        }
//        catch (IOException exception) {
//            Log.e(imagesTAG, "Error trying to retrieve image: " + exception);
//        }

    }
    /**
     * Retrieves all events in the events collection
     * @param eventList The list we're going to hold the events in.
     * @param eventArrayAdapter The ArrayAdapter of eventList.
     * */
    public void getAllEvents(ArrayList<Event> eventList, ArrayAdapter<Event> eventArrayAdapter) {
        eventsCollection
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            Log.d(eventsTAG, "Retrieved all events");
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String id = document.getId();
                                String eventName = (String) document.getData().get("name");
                                String organizerId = (String) document.getData().get("organizerId");
                                String details = (String) document.getData().get("details");
                                String location = (String) document.getData().get("location");
                                LocalDateTime startDate = (LocalDateTime) document.getData().get("startDate");
                                LocalDateTime endDate = (LocalDateTime) document.getData().get("endDate");
                                Boolean geolocationOn = (Boolean) document.getData().get("geolocationOn");
                                String posterPath = (String) document.getData().get("posterPath");
                                String qrCodePath = (String) document.getData().get("qrCodePath");
                                String qrCodePromoPath = (String) document.getData().get("qrCodePromoPath");
                                ArrayList<String> announcements = (ArrayList<String>) document.getData().get("announcements");
                                ArrayList<String> signUps = (ArrayList<String>) document.getData().get("signUps");
                                ArrayList<Map<String, Object>> checkIns = (ArrayList<Map<String, Object>>) document.getData().get("checkIns");

                                // Event event = new Event(id, eventName, organizerId, details, location, startDate, endDate, geolocationOn, posterPath, qrCodePath, qrCodePromoPath, announcements, signUps, checkIns);
                                // eventList.add(event);
                                // eventArrayAdapter.notifyDataSetChanged;
                            }
                        } else {
                            Log.d(eventsTAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

    }

    /**
     * Retrieves all users in the users collection
     * @param attendeeList The list we're going to hold the users in.
     * @param attendeeArrayAdapter The ArrayAdapter of attendeeList.
     * */
    public void getAllUsers(ArrayList<String> attendeeList, ArrayAdapter<String> attendeeArrayAdapter) {
        eventsCollection
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


                                // Attendee attendee = new Attendee(id, documentId, name, email, profilePicturePath, geolocationOn);
                                // attendeeList.add(attendee);
                                // attendeeArrayAdapter.notifyDataSetChanged;
                            }
                        } else {
                            Log.d(usersTAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

    }


}
