package com.example.example_android_pe.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.example_android_pe.R;
import com.example.example_android_pe.entity.Users;
import com.example.example_android_pe.utils.SessionManager;
import com.example.example_android_pe.viewModel.UsersViewModel;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LoginFragment extends Fragment {
    private EditText editUsername, editPassword;
    private UsersViewModel usersViewModel;
    private SessionManager sessionManager;
    private ExecutorService executorService;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        // Initialize session manager
        sessionManager = new SessionManager(requireContext());
        executorService = Executors.newSingleThreadExecutor();

        // Check if user is already logged in
        if (sessionManager.isLoggedIn()) {
            navigateBasedOnRole();
            return view;
        }

        editUsername = view.findViewById(R.id.eUsername);
        editPassword = view.findViewById(R.id.ePassword);
        usersViewModel = new ViewModelProvider(this).get(UsersViewModel.class);

        Button btnLogin = view.findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(v -> {
            String username = editUsername.getText().toString().trim();
            String password = editPassword.getText().toString().trim();

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(requireContext(), "Username and Password are required", Toast.LENGTH_SHORT).show();
                return;
            }

            // Check credentials from database
            executorService.execute(() -> {
                Users user = usersViewModel.authenticateUser(username, password);

                requireActivity().runOnUiThread(() -> {
                    if (user != null) {
                        if ("banned".equals(user.getStatus())) {
                            Toast.makeText(requireContext(), "Your account has been banned. Please contact support.", Toast.LENGTH_LONG).show();
                            return;
                        }

                        // Create login session
                        sessionManager.createLoginSession(user.getId(), username, user.getRole());

                        // Navigate based on role
                        navigateBasedOnRole();

                        clearFields();
                        Toast.makeText(requireContext(), "Login successful", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(requireContext(), "Invalid credentials", Toast.LENGTH_SHORT).show();
                    }
                });
            });
        });

        Button btnGoToRegister = view.findViewById(R.id.btnGoToRegister);
        btnGoToRegister.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.action_login_to_register);
        });

        return view;
    }

    private void navigateBasedOnRole() {
        if (sessionManager.isAdmin()) {
            Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
                    .navigate(R.id.action_login_to_adminDashboard);
        } else if (sessionManager.isSeller()) {
            Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
                    .navigate(R.id.action_login_to_sellerDashboard);
        } else {
            Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
                    .navigate(R.id.action_login_to_shop);
        }
    }

    private void clearFields() {
        editUsername.setText("");
        editPassword.setText("");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (executorService != null) {
            executorService.shutdown();
        }
    }
}