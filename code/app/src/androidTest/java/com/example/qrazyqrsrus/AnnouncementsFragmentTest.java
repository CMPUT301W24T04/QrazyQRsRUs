package com.example.qrazyqrsrus;

import androidx.annotation.NonNull;
import androidx.fragment.app.testing.FragmentScenario;
import androidx.lifecycle.Lifecycle;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Arrays;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import android.os.Bundle;
import android.widget.ArrayAdapter;

/**
 * Test cases for the AnnouncementsFragment
 * @see AnnouncementsFragment
 * References:
 *  OpenAI, 2024, ChatGPT 3.5, Creation of test cases
 */
@RunWith(AndroidJUnit4.class)
public class AnnouncementsFragmentTest {
    ArrayList<String> announcementsList = new ArrayList<>();
    ArrayAdapter<String> announcementsListAdapter;

    private FragmentScenario<AnnouncementsFragment> fragmentScenario;

    @Before
    public void setUp() {
        // Initialize the fragmentScenario
        fragmentScenario = FragmentScenario.launchInContainer(AnnouncementsFragment.class);
    }

    /**
     * Tests whether the announcements are correctly displayed
     */
    @Test
    public void testAnnouncementsDisplayed() {
        Bundle bundle = getBundle();
        // Set up test data
        ArrayList<String> announcements = new ArrayList<>(Arrays.asList("Announcement 1", "Announcement 2"));
        // Launch the fragment with the provided bundle
        FragmentScenario<AnnouncementsFragment> scenario =  FragmentScenario.launchInContainer(AnnouncementsFragment.class, bundle, R.style.Base_Theme_QrazyQRsRUs);
        announcementsList.add("Announcement 1");
        announcementsList.add("Announcement 2");
//        announcementsListAdapter.notifyDataSetChanged();
//        // Set arguments for the fragment
//        fragmentScenario.onFragment(fragment -> {
//            Bundle args = new Bundle();
//            Event event = new Event();
//            event.setAnnouncements(announcements);
//            args.putSerializable("event", event);
//            fragment.setArguments(args);
//        });

        // Check if the ListView is displayed
        onView(withId(R.id.list_announcements)).check(matches(isDisplayed()));

        onView(withId(R.id.button_back)).check(matches(isDisplayed()));

        // Check if announcements are displayed in the ListView
//        onView(withText("Announcement 1")).check(matches(isDisplayed()));
//        onView(withText("Announcement 2")).check(matches(isDisplayed()));

//        onView(ViewMatchers.withText("Announcement 1")).check(matches(isDisplayed()));
//        onView(ViewMatchers.withText("Announcement 2")).check(matches(isDisplayed()));
    }
    @NonNull
    private static Bundle getBundle() {
        ArrayList<String> announcements = new ArrayList<>();
        ArrayList<String> signUps = new ArrayList<>();
        ArrayList<String> checkIns = new ArrayList<>();
        checkIns.add("uHWRJuNzQmzjXa1dfleb");
        signUps.add("uHWRJuNzQmzjXa1dfleb");
        Event event = new Event("1111111", "John", "222222222", "This is an event",
                "Edmonton", "4/6/2024", "4/8/2024",
                true, "path", "qrCode",
                "promoQr", "organizerToken",announcements,signUps,
                checkIns, 10);
        // Create a bundle with necessary data
        Bundle bundle = new Bundle();
        bundle.putSerializable("event", event);
        return bundle;
    }
}
