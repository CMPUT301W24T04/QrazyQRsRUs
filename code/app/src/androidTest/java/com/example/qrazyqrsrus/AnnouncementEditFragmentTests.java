package com.example.qrazyqrsrus;

import androidx.fragment.app.testing.FragmentScenario;
import androidx.test.espresso.action.ViewActions;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

/**
 * Test cases for the AnnouncementEditFragment
 * @see AnnouncementEditFragment
 * References:
 *  OpenAI, 2024, ChatGPT 3.5, Creation of test cases
 */
@RunWith(AndroidJUnit4.class)
public class AnnouncementEditFragmentTests {

    private FragmentScenario<AnnouncementEditFragment> fragmentScenario;

    @Before
    public void setUp() {
        // Initialize the fragmentScenario
        fragmentScenario = FragmentScenario.launchInContainer(AnnouncementEditFragment.class);
    }

    @Rule
    public ActivityScenarioRule<MainActivity> activityScenarioRule =
            new ActivityScenarioRule<>(MainActivity.class);

    /**
     * Tests whether the UI elements are displayed
     */
    @Test
    public void testUIElementsDisplayed() {
        // Check if UI elements are displayed
        onView(withId(R.id.edit_announcement)).check(matches(isDisplayed()));
        onView(withId(R.id.button_add)).check(matches(isDisplayed()));
        onView(withId(R.id.list_announcements)).check(matches(isDisplayed()));
    }


    /**
     * Tests whether announcements are properly added
     */
    @Test
    public void testAddAnnouncement() {
        // Type an announcement in the EditText and click the add button
        onView(withId(R.id.edit_announcement)).perform(ViewActions.typeText("New Announcement"));
        onView(withId(R.id.button_add)).perform(ViewActions.click());

        // Check if the announcement is added to the list
        onView(withText("New Announcement")).check(matches(isDisplayed()));
    }

    /**
     * Tests whether announcements are properly deleted.
     */
    @Test
    public void testDeleteAnnouncement() {
        // Assuming there is at least one announcement in the list
        onView(withId(R.id.list_announcements)).perform(ViewActions.longClick());

        // Check if the delete confirmation dialog is displayed
        onView(withText("Delete Announcement")).check(matches(isDisplayed()));

        // Click on the "Yes" button in the confirmation dialog
        onView(withText("Yes")).perform(ViewActions.click());

        // Check if the announcement is deleted from the list
        onView(withText("Deleted Announcement")).check(matches(isDisplayed()));
    }
}
