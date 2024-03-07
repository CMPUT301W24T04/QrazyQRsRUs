package com.example.qrazyqrsrus;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Ikjyot - icansingh
 * @see AnnouncementEditFragment
 * @version 1
 */
public class AnnouncementsFragment extends Fragment {

    private ListView announcementsListView;

    private FirebaseFirestore db;

    public AnnouncementsFragment() {
        // Constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.layout_announcements, container, false);

        announcementsListView = rootView.findViewById(R.id.list_announcements);

        db = FirebaseFirestore.getInstance();

        loadAnnouncements();

        return rootView;
    }

    /**
     * Loads the announcements from the database.
     * @throws ....
     */
    private void loadAnnouncements() {
        db.collection("Events") //This is the collection
                .document("Calgary") // Provide the event document ID here
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w("FirestoreListener", "Listen failed.", e);
                            return;
                        }

                        if (documentSnapshot != null && documentSnapshot.exists()) {
                            // Access announcements array within the event document
                            List<String> announcements = (List<String>) documentSnapshot.get("Announcements");

                            if (announcements != null && !announcements.isEmpty()) {
                                // Display announcements in a ListView
                                ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, announcements);
                                announcementsListView.setAdapter(adapter);
                            } else {
                                // Handle case when announcements array is empty
                            }
                        } else {
                            // Handle case when document doesn't exist
                        }
                    }
                });
    }


}

