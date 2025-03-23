package com.example.example_android_pe.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.example_android_pe.R;
import com.example.example_android_pe.entity.Users;
import com.example.example_android_pe.viewModel.UsersViewModel;

import java.util.ArrayList;
import java.util.List;

public class AdminUserAdapter extends RecyclerView.Adapter<AdminUserAdapter.UserViewHolder> {
    private List<Users> users = new ArrayList<>();
    private Context context;
    private UsersViewModel usersViewModel;

    public AdminUserAdapter(Context context, UsersViewModel usersViewModel) {
        this.context = context;
        this.usersViewModel = usersViewModel;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.admin_user_list_item, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        Users user = users.get(position);
        holder.bind(user, usersViewModel);
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public void setUsers(List<Users> users) {
        this.users = users;
        notifyDataSetChanged();
    }

    static class UserViewHolder extends RecyclerView.ViewHolder {
        private TextView usernameTextView, emailTextView, roleTextView, statusTextView;
        private Button banButton, activateButton;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            usernameTextView = itemView.findViewById(R.id.adminUserUsernameTextView);
            emailTextView = itemView.findViewById(R.id.adminUserEmailTextView);
            roleTextView = itemView.findViewById(R.id.adminUserRoleTextView);
            statusTextView = itemView.findViewById(R.id.adminUserStatusTextView);
            banButton = itemView.findViewById(R.id.banUserButton);
            activateButton = itemView.findViewById(R.id.activateUserButton);
        }

        public void bind(Users user, UsersViewModel usersViewModel) {
            usernameTextView.setText(user.getUsername());
            emailTextView.setText(user.getEmail());
            roleTextView.setText("Role: " + user.getRole());
            statusTextView.setText("Status: " + user.getStatus());
            
            // Show/hide buttons based on current status
            if ("active".equals(user.getStatus())) {
                banButton.setVisibility(View.VISIBLE);
                activateButton.setVisibility(View.GONE);
            } else {
                banButton.setVisibility(View.GONE);
                activateButton.setVisibility(View.VISIBLE);
            }
            
            // Set up ban button
            banButton.setOnClickListener(v -> {
                usersViewModel.banUser(user.getId());
                Toast.makeText(v.getContext(), "User banned: " + user.getUsername(), Toast.LENGTH_SHORT).show();
            });
            
            // Set up activate button
            activateButton.setOnClickListener(v -> {
                usersViewModel.activateUser(user.getId());
                Toast.makeText(v.getContext(), "User activated: " + user.getUsername(), Toast.LENGTH_SHORT).show();
            });
        }
    }
}