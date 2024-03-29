package com.example.qrazyqrsrus;

import androidx.fragment.app.testing.FragmentScenario;
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

import android.os.Bundle;

/**
 * Test cases for the AnnouncementsFragment
 * @see AnnouncementsFragment
 * References:
 *  OpenAI, 2024, ChatGPT 3.5, Creation of test cases
 */
@RunWith(AndroidJUnit4.class)
public class AnnouncementsFragmentTest {

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
        // Set up test data
        ArrayList<String> announcements = new ArrayList<>(Arrays.asList("Announcement 1", "Announcement 2"));

        // Set arguments for the fragment
        fragmentScenario.onFragment(fragment -> {
            Bundle args = new Bundle();
            Event event = new Event();
            event.setAnnouncements(announcements);
            args.putSerializable("event", event);
            fragment.setArguments(args);
        });

        // Check if the ListView is displayed
        onView(withId(R.id.list_announcements)).check(matches(isDisplayed()));

        // Check if announcements are displayed in the ListView
        onView(ViewMatchers.withText("Announcement 1")).check(matches(isDisplayed()));
        onView(ViewMatchers.withText("Announcement 2")).check(matches(isDisplayed()));
    }
}
