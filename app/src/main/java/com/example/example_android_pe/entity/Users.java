package com.example.example_android_pe.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "users")
public class Users {
  @PrimaryKey(autoGenerate = true)
  @ColumnInfo(name = "id")
  private int id;

  @ColumnInfo(name = "username")
  private String username;

  @ColumnInfo(name = "password")
  private String password;

  @ColumnInfo(name = "birthday")
  private String birthday;

  @ColumnInfo(name = "phone")
  private String phone;

  @ColumnInfo(name = "email")
  private String email;

  @ColumnInfo(name = "role")
  private String role; // "customer", "seller", "admin"

  @ColumnInfo(name = "status")
  private String status; // "active", "banned"

  @ColumnInfo(name = "profile_image")
  private String profileImage;

  @ColumnInfo(name = "created_at")
  private long createdAt;

  public Users() {
  }

  public Users(String username, String password, String birthday, String phone, String email) {
    this.username = username;
    this.password = password;
    this.birthday = birthday;
    this.phone = phone;
    this.email = email;
    this.role = "customer"; // Default role
    this.status = "active"; // Default status
    this.createdAt = System.currentTimeMillis();
  }

  public Users(String username, String password, String birthday, String phone, String email, String role) {
    this.username = username;
    this.password = password;
    this.birthday = birthday;
    this.phone = phone;
    this.email = email;
    this.role = role;
    this.status = "active"; // Default status
    this.createdAt = System.currentTimeMillis();
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getBirthday() {
    return birthday;
  }

  public void setBirthday(String birthday) {
    this.birthday = birthday;
  }

  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getRole() {
    return role;
  }

  public void setRole(String role) {
    this.role = role;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getProfileImage() {
    return profileImage;
  }

  public void setProfileImage(String profileImage) {
    this.profileImage = profileImage;
  }

  public long getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(long createdAt) {
    this.createdAt = createdAt;
  }

  public boolean isAdmin() {
    return "admin".equals(role);
  }

  public boolean isSeller() {
    return "seller".equals(role);
  }

  public boolean isCustomer() {
    return "customer".equals(role);
  }

  public boolean isActive() {
    return "active".equals(status);
  }
}