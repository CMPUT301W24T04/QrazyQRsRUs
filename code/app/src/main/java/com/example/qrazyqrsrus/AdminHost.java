package com.example.qrazyqrsrus;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AdminHost#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AdminHost extends Fragment {


    /**
     * Required empty public constructor for the {@code AdminHost} fragment.
     * <p>
     * It's recommended not to perform any initialization here that could lead to
     * incomplete fragment constructions or require context not available during construction.
     */
    public AdminHost() {
        // Required empty public constructor
    }


    /**
     * Called to do initial creation of the fragment.
     * <p>
     * This is where to perform all static set up: create views, bind data to lists, and so on.
     * This method also provides a {@link Bundle} if the fragment is being re-constructed from
     * a saved state. It's not the place for animations or data loading tasks that require context.
     *
     * @param savedInstanceState If the fragment is being re-created from a previous saved state, this
     *                           is the state. This value may be {@code null}.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    /**
     * Called to have the fragment instantiate its user interface view.
     * <p>
     * This is optional, and non-graphical fragments can return {@code null}. This will be called between
     * {@link #onCreate(Bundle)} and {@link #onActivityCreated(Bundle)}.
     * A default View can be returned by calling {@code inflater.inflate} with a layout resource.
     * This method returns the root of your fragment's layout. If you return a {@code View} from here,
     * you will later be called in {@link #onDestroyView} when the view is being released.
     *
     * @param inflater           The LayoutInflater object that can be used to inflate any views in the fragment.
     * @param container          If non-null, this is the parent view that the fragment's UI should be attached to.
     *                           The fragment should not add the view itself, but this can be used to generate
     *                           the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state
     *                           as given here.
     * @return Return the View for the fragment's UI, or {@code null}.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_host, container, false);
        // Inflate the layout for this fragment
        return view;
    }


    /**
     * Factory method to create a new instance of {@code AdminHost} fragment.
     * <p>
     * This method encapsulates the creation of a new {@code AdminHost} fragment, ensuring that
     * any required configuration or initialization is encapsulated here. It's recommended to
     * use this method for creating instances of {@code AdminHost} to ensure consistency and
     * potentially pass any arguments or initialization data.
     *
     * @return A new instance of fragment {@code AdminHost}.
     */
    public static AdminHost newInstance(){
        AdminHost fragment = new AdminHost();
        return fragment;
    }
}