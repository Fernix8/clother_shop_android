package com.example.example_android_pe.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.example_android_pe.R;
import com.example.example_android_pe.entity.Users;
import com.example.example_android_pe.viewModel.UsersViewModel;

import java.util.ArrayList;
import java.util.List;

public class RegisterFragment extends Fragment {
    private EditText editUsername, editPassword, editBirthday, editPhone, editEmail;
    private Spinner roleSpinner;
    private UsersViewModel usersViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);

        editUsername = view.findViewById(R.id.eUsername);
        editPassword = view.findViewById(R.id.ePassword);
        editBirthday = view.findViewById(R.id.eBirthday);
        editPhone = view.findViewById(R.id.ePhone);
        editEmail = view.findViewById(R.id.eEmail);
        roleSpinner = view.findViewById(R.id.roleSpinner);

        // Set up role spinner
        List<String> roles = new ArrayList<>();
        roles.add("Customer");
        roles.add("Seller");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_spinner_item,
                roles
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        roleSpinner.setAdapter(adapter);

        usersViewModel = new ViewModelProvider(this).get(UsersViewModel.class);

        Button btnRegister = view.findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(v -> {
            String username = editUsername.getText().toString().trim();
            String password = editPassword.getText().toString().trim();
            String birthday = editBirthday.getText().toString().trim();
            String phone = editPhone.getText().toString().trim();
            String email = editEmail.getText().toString().trim();
            String role = roleSpinner.getSelectedItem().toString().toLowerCase();

            if (username.isEmpty() || password.isEmpty() || birthday.isEmpty() || phone.isEmpty() || email.isEmpty()) {
                Toast.makeText(requireContext(), "All fields are required", Toast.LENGTH_SHORT).show();
                return;
            }

            Users user = new Users(username, password, birthday, phone, email, role);
            usersViewModel.insertUser(user);

            Toast.makeText(requireContext(), "Registration successful", Toast.LENGTH_SHORT).show();
            Navigation.findNavController(v).navigate(R.id.action_register_to_login);
        });

        Button btnGoToLogin = view.findViewById(R.id.btnGoToLogin);
        btnGoToLogin.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.action_register_to_login);
        });

        return view;
    }
}