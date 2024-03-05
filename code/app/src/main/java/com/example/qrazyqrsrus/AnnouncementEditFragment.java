package com.example.qrazyqrsrus;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Ikjyot - icansingh
 * @see AnnouncementsFragment
 * @version 1
 */
public class AnnouncementEditFragment extends Fragment {

    private EditText announcementEditText;
    private Button addButton;
    private ListView announcementListView;
    private FirebaseFirestore db;
    private ArrayAdapter<String> adapter;
    private List<String> announcements;

    public AnnouncementEditFragment() {
        // Constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_announcement_edit, container, false);

        announcementEditText = rootView.findViewById(R.id.edit_announcement);
        addButton = rootView.findViewById(R.id.button_add);
        announcementListView = rootView.findViewById(R.id.list_announcements);

        db = FirebaseFirestore.getInstance();
        announcements = new ArrayList<>();
        adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, announcements);
        announcementListView.setAdapter(adapter);

        loadAnnouncements();

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addAnnouncement();
            }
        });

        announcementListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                showDeleteConfirmationDialog(position);
                return true; // Consume the long-click event
            }
        });

        announcementListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                editAnnouncement(position);
            }
        });

        return rootView;
    }

    private void loadAnnouncements() {
        db.collection("Events")
                .document("Calgary") // Add event ID here
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            announcements.clear();
                            List<String> existingAnnouncements = (List<String>) documentSnapshot.get("Announcements");
                            if (existingAnnouncements != null) {
                                announcements.addAll(existingAnnouncements);
                                adapter.notifyDataSetChanged();
                            }
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Handle failure
                    }
                });
    }

    private void addAnnouncement() {
        String announcement = announcementEditText.getText().toString().trim();
        if (!announcement.isEmpty()) {
            db.collection("cities")
                    .document("Calgary")
                    .update("Announcements", FieldValue.arrayUnion(announcement))
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            loadAnnouncements(); // Reload announcements after addition
                            announcementEditText.setText("");
                            showToast("Announcement Added");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Handle failure
                        }
                    });
        }
    }

    private void showDeleteConfirmationDialog(final int position) {
        new AlertDialog.Builder(requireContext())
                .setTitle("Delete Announcement")
                .setMessage("Are you sure you want to delete this announcement?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteAnnouncement(position);
                    }
                })
                .setNegativeButton(android.R.string.no, null)
                .show();
    }

    private void deleteAnnouncement(final int position) {
        db.collection("cities")
                .document("Calgary")
                .update("Announcements", FieldValue.arrayRemove(announcements.get(position)))
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        announcements.remove(position);
                        adapter.notifyDataSetChanged();
                        showToast("Announcement Deleted");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Handle failure
                    }
                });
    }

    private void editAnnouncement(final int position) {
        final String oldAnnouncement = announcements.get(position);
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Edit Announcement");

        // Set up the input
        final EditText input = new EditText(requireContext());
        input.setText(oldAnnouncement);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String newAnnouncement = input.getText().toString().trim();
                if (!newAnnouncement.isEmpty() && !newAnnouncement.equals(oldAnnouncement)) {
                    updateAnnouncement(position, newAnnouncement);
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    private void updateAnnouncement(final int position, final String newAnnouncement) {
        db.collection("cities")
                .document("Calgary")
                .update("Announcements", FieldValue.arrayRemove(announcements.get(position)))
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        announcements.set(position, newAnnouncement);
                        adapter.notifyDataSetChanged();
                        db.collection("cities")
                                .document("Calgary")
                                .update("Announcements", FieldValue.arrayUnion(newAnnouncement))
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        showToast("Announcement Updated");
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        // Handle failure
                                    }
                                });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Handle failure
                    }
                });
    }

    private void showToast(String message) {
        Toast toast = new Toast(getContext());
        View toastView = getLayoutInflater().inflate(R.layout.toast_layout, null);
        TextView textView = toastView.findViewById(R.id.text_toast);
        textView.setText(message);
        toast.setView(toastView);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.show();
    }
}

