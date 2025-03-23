package com.example.example_android_pe.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.example_android_pe.R;
import com.example.example_android_pe.entity.Users;
import com.example.example_android_pe.fragments.MainFragment;
import com.example.example_android_pe.viewModel.UsersViewModel;

import java.util.ArrayList;
import java.util.List;

public class UsersListAdapter extends RecyclerView.Adapter<UsersListAdapter.UsersViewHolder> {
    private List<Users> usersList = new ArrayList<>();
    private Context context;
    private UsersViewModel viewModel;
    private MainFragment mainFragment; // Tham chiếu đến MainFragment

    public UsersListAdapter(Context context, UsersViewModel viewModel, MainFragment mainFragment) {
        this.context = context;
        this.viewModel = viewModel;
        this.mainFragment = mainFragment;
    }

    @NonNull
    @Override
    public UsersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.users_list_item, parent, false);
        return new UsersViewHolder(view, context, viewModel, mainFragment); // Truyền mainFragment vào constructor
    }

    @Override
    public void onBindViewHolder(@NonNull UsersViewHolder holder, int position) {
        Users user = usersList.get(position);
        holder.bind(user);
    }

    @Override
    public int getItemCount() {
        return usersList.size();
    }

    public void setUsers(List<Users> users) {
        this.usersList = users;
        notifyDataSetChanged();
    }

    public static class UsersViewHolder extends RecyclerView.ViewHolder {
        private TextView npUsersId, npUsersName, npUsersEmail, npUsersPhone;
        private Button btnDelete, btnEdit;
        private Context context;
        private UsersViewModel viewModel;
        private MainFragment mainFragment; // Thêm biến mainFragment trong ViewHolder

        public UsersViewHolder(@NonNull View itemView, Context context, UsersViewModel viewModel, MainFragment mainFragment) {
            super(itemView);
            this.context = context;
            this.viewModel = viewModel;
            this.mainFragment = mainFragment; // Nhận mainFragment từ constructor

            npUsersId = itemView.findViewById(R.id.eId);
            npUsersName = itemView.findViewById(R.id.eUsername);
            npUsersEmail = itemView.findViewById(R.id.eEmail);
            npUsersPhone = itemView.findViewById(R.id.ePhone);
            btnDelete = itemView.findViewById(R.id.btnDelete);
            btnEdit = itemView.findViewById(R.id.btnEdit);
        }

        public void bind(Users user) {
            npUsersId.setText(String.valueOf(user.getId()));
            npUsersName.setText(user.getUsername());
            npUsersEmail.setText(user.getEmail() != null ? user.getEmail() : "N/A");
            npUsersPhone.setText(user.getPhone() != null ? user.getPhone() : "N/A");

            btnDelete.setOnClickListener(v -> {
                viewModel.deleteUser(user);
            });

//            btnEdit.setOnClickListener(v -> {
//                if (mainFragment != null) {
//                    Log.d("UsersListAdapter", "Calling selectUserForUpdate with user: " + user.getUsername());
//                    mainFragment.selectUserForUpdate(user);
//                } else {
//                    Log.d("UsersListAdapter", "mainFragment is null");
//                }
//            });
        }
    }
}