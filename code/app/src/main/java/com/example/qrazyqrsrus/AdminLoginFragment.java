//package com.example.qrazyqrsrus;
//
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.EditText;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.fragment.app.Fragment;
//import androidx.navigation.Navigation;
//
//public class AdminLoginFragment extends Fragment {
//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//    }
//
//    @Nullable
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        View rootView = inflater.inflate(R.layout.fragment_admin_login, container, false);
//
//        Button loginButton = rootView.findViewById(R.id.btnLogin);
//        EditText usernameEditText = rootView.findViewById(R.id.etUsername);
//        EditText passwordEditText = rootView.findViewById(R.id.etPassword);
//        loginButton.setOnClickListener(v ->{
//            FirebaseDB.getInstance().attemptAdminLogin(usernameEditText.getText().toString(), passwordEditText.getText().toString(), new FirebaseDB.getInstance().AttemptLoginCallback() {
//                //if the user has input a username and password that match some document's fields in the admin Logins collection, we navigate to the admin home screen
//                @Override
//                public void onResult() {
//                    Navigation.findNavController(rootView).navigate(R.id.action_adminLoginFragment_to_adminHomeFragment);
//                }
//
//                //if the user has input a username and password that do not match, we show a dialog to tell them.
//                @Override
//                public void onNoResult() {
//                    new ErrorDialog(R.string.wrong_admin_login_error).show(getActivity().getSupportFragmentManager(), "Error Dialog");
//                }
//            });
//        });
//
//        return rootView;
//    }
//}
