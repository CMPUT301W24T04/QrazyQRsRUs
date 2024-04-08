package com.example.qrazyqrsrus;


import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import androidx.fragment.app.testing.FragmentScenario;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.MediumTest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
@MediumTest
public class MyEventsListFragmentTest {
    private NavController mockNavController;

    @Before
    public void setUp() {
        mockNavController = mock(NavController.class);

    }

    @Test
    public void testFragmentInitialization() {
        // Launching the MyEventsListFragment
        FragmentScenario<MyEventsListFragment> scenario = FragmentScenario.launchInContainer(MyEventsListFragment.class, null, R.style.Base_Theme_QrazyQRsRUs);
        //if binary file error comes up add null , R.style.Base_Theme_QrazyQr laquchin container
        scenario.onFragment(fragment -> {
            // Verify the ListView is not null
            assertNotNull("ListView is null", fragment.getView().findViewById(R.id.event_list_view));

            // Verify the FloatingActionButton is not null
            assertNotNull("FloatingActionButton is null", fragment.getView().findViewById(R.id.new_event_button));
        });
    }
    @Test
    public void testListViewItemClickNavigation() {
        // Launch the fragment scenario
        FragmentScenario<MyEventsListFragment> scenario = FragmentScenario.launchInContainer(
                MyEventsListFragment.class, null, R.style.Base_Theme_QrazyQRsRUs
        );
        scenario.onFragment(fragment -> {
            // Set the mock NavController on the fragment
            Navigation.setViewNavController(fragment.requireView(), mockNavController);

            // Perform click on the ListView item
            ListView listView = fragment.getView().findViewById(R.id.event_list_view);



            // Perform the click on the first visible item
            if (listView.getCount() > 0) {
                View itemView = listView.getChildAt(0);
                assertNotNull("Item View is null. No items in the list.", itemView);

                // Perform the click
                itemView.isClickable();

                // Verify that the NavController has been called with the correct action and bundle
                verify(mockNavController).navigate(
                        eq(R.id.action_myEventsListFragment_to_eventDetailsFragment),
                        any(Bundle.class)
                );
            }
        });
    }



}
