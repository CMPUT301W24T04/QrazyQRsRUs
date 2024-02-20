//I DONT THINK I NEED ANY OF THIS
//WE DON'T NEED A CUSTOM NAV HOST FRAGMENT
//package com.example.qrazyqrsrus;
//
//import android.content.Context;
//import android.os.Bundle;
//import android.util.AttributeSet;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.fragment.app.Fragment;
//import androidx.navigation.fragment.NavHostFragment;
//
//public class NewEventNavHostFragment extends NavHostFragment {
//
//    interface AddEventListener{
//        void setNavController();
//    }
//
//    private AddEventListener listener;
//    public static NewEventNavHostFragment newInstance() {
//        NewEventNavHostFragment fragment = new NewEventNavHostFragment();
//        Bundle args = new Bundle();
//        fragment.setArguments(args);
//        return fragment;
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//
//        }
//    }
//
//    @Override
//    public void onAttach(@NonNull Context context) {
//        super.onAttach(context);
//        if (context instanceof AddEventListener) {
//            listener = (AddEventListener) context;
//            listener.setNavController();
//        } else {
//            throw new RuntimeException(context + "must implement AddEventListener");
//        }
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.new_event_nav_host, container, false);
//    }
//
//    @Override
//    public void onInflate(@NonNull Context context, @NonNull AttributeSet attrs, @Nullable Bundle savedInstanceState) {
//
//        super.onInflate(context, attrs, savedInstanceState);
//    }
//}
