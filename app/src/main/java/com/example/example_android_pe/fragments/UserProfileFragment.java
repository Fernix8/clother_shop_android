package com.example.example_android_pe.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.example_android_pe.R;
import com.example.example_android_pe.entity.UserProfile;
import com.example.example_android_pe.entity.Users;
import com.example.example_android_pe.utils.SessionManager;
import com.example.example_android_pe.viewModel.UserProfileViewModel;
import com.example.example_android_pe.viewModel.UsersViewModel;

public class UserProfileFragment extends Fragment {
    private SessionManager sessionManager;
    private UsersViewModel usersViewModel;
    private UserProfileViewModel userProfileViewModel;
    private TextView usernameTextView, emailTextView, roleTextView;
    private EditText fullNameEditText, addressEditText, cityEditText, stateEditText, zipEditText, countryEditText, bioEditText;
    private ImageView profileImageView;
    private Button saveButton, logoutButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_profile, container, false);

        // Initialize session manager
        sessionManager = new SessionManager(requireContext());

        // Initialize ViewModels
        usersViewModel = new ViewModelProvider(this).get(UsersViewModel.class);
        userProfileViewModel = new ViewModelProvider(this).get(UserProfileViewModel.class);

        // Set current user ID
        userProfileViewModel.setCurrentUserId(sessionManager.getUserId());

        // Initialize UI components
        usernameTextView = view.findViewById(R.id.usernameTextView);
        emailTextView = view.findViewById(R.id.emailTextView);
        roleTextView = view.findViewById(R.id.roleTextView);
        fullNameEditText = view.findViewById(R.id.fullNameEditText);
        addressEditText = view.findViewById(R.id.addressEditText);
        cityEditText = view.findViewById(R.id.cityEditText);
        stateEditText = view.findViewById(R.id.stateEditText);
        zipEditText = view.findViewById(R.id.zipEditText);
        countryEditText = view.findViewById(R.id.countryEditText);
        bioEditText = view.findViewById(R.id.bioEditText);
        profileImageView = view.findViewById(R.id.profileImageView);
        saveButton = view.findViewById(R.id.saveProfileButton);
        logoutButton = view.findViewById(R.id.logoutButton);

        // Load user data
        usersViewModel.getUserById(sessionManager.getUserId()).observe(getViewLifecycleOwner(), this::displayUserData);

        // Load profile data
        userProfileViewModel.getUserProfile().observe(getViewLifecycleOwner(), this::displayProfileData);

        // Set up save button
        saveButton.setOnClickListener(v -> saveProfile());

        // Set up logout button
        logoutButton.setOnClickListener(v -> {
            sessionManager.logout();
            Navigation.findNavController(v).navigate(R.id.action_userProfile_to_login);
        });

        return view;
    }

    private void displayUserData(Users user) {
        if (user != null) {
            usernameTextView.setText(user.getUsername());
            emailTextView.setText(user.getEmail());
            roleTextView.setText(user.getRole());

            // Set profile image if available
            if (user.getProfileImage() != null && !user.getProfileImage().isEmpty()) {
                // In a real app, you would use a library like Glide or Picasso to load the image
                // For simplicity, we'll just set a placeholder
                profileImageView.setImageResource(R.drawable.placeholder_image);
            } else {
                profileImageView.setImageResource(R.drawable.placeholder_image);
            }
        }
    }

    private void displayProfileData(UserProfile profile) {
        if (profile != null) {
            fullNameEditText.setText(profile.getFullName());
            addressEditText.setText(profile.getAddress());
            cityEditText.setText(profile.getCity());
            stateEditText.setText(profile.getState());
            zipEditText.setText(profile.getZipCode());
            countryEditText.setText(profile.getCountry());
            bioEditText.setText(profile.getBio());
        }
    }

    private void saveProfile() {
        String fullName = fullNameEditText.getText().toString().trim();
        String address = addressEditText.getText().toString().trim();
        String city = cityEditText.getText().toString().trim();
        String state = stateEditText.getText().toString().trim();
        String zip = zipEditText.getText().toString().trim();
        String country = countryEditText.getText().toString().trim();
        String bio = bioEditText.getText().toString().trim();

        UserProfile profile = new UserProfile(
                sessionManager.getUserId(),
                fullName,
                address,
                city,
                state,
                zip,
                country,
                bio
        );

        userProfileViewModel.saveUserProfile(profile);
        Toast.makeText(requireContext(), "Profile saved successfully", Toast.LENGTH_SHORT).show();
    }
}