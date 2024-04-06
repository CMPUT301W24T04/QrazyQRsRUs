package com.example.qrazyqrsrus;

import android.provider.Settings;

public class CurrentUser {

    private static final CurrentUser instance = new CurrentUser();

    // So that no other class can call a new CurrentUser()
    private CurrentUser() {
    }
    public static CurrentUser getInstance() {
        return instance;
    }
    private Attendee currentUser;
    private String deviceId;

    /**
     * This method is to be called one, when MainActivity is created, to initialize our user
     */
    public void initializeUser(String deviceId) {
        this.deviceId = deviceId;
        FirebaseDB.getInstance().loginUser(deviceId, new FirebaseDB.GetAttendeeCallBack() {
            @Override
            public void onResult(Attendee attendee) {
                CurrentUser.this.currentUser = attendee;
            }

            @Override
            public void onNoResult() {
                CurrentUser.this.currentUser = null;
            }
        });
    }

    /**
     * Standard getter for currentUser
     */
    public Attendee getCurrentUser() {
        return currentUser;
    }

    /**
     * This sets the point of access (currentUser) to a new Attendee instance. This should be
     * used when currentUser is updated remotely and we need to update it here too
     */
    public void Update() {
        FirebaseDB.getInstance().loginUser(this.deviceId, new FirebaseDB.GetAttendeeCallBack() {
            @Override
            public void onResult(Attendee attendee) {
                CurrentUser.this.currentUser = attendee;
            }

            @Override
            public void onNoResult() {
                //do nothing, don't update attendee
            }
        });
    }
}
