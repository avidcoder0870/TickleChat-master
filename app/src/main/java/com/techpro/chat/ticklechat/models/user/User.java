package com.techpro.chat.ticklechat.models.user;

import java.io.Serializable;

/**
 * Created by aryans05 on 04/08/16.
 */

public class User extends UserGroupBotModel implements Serializable {
    private String birthday;

    private String phone;

    private String status;

    private String isbot;

    private String profile_image;

    private String registered_at;

    private String user_status;

    private String lastseen_at;

    private String password;

    private String country_code;

    private String id;

    private String device_os;

    private String device_model;

    private String updated_at;

    private String email;

    private String device_token;

    private String name;

    private String dob;

    private String created_at;

    private String gender;

    private String mobile;

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getIsbot() {
        return isbot;
    }

    public void setIsbot(String isbot) {
        this.isbot = isbot;
    }

    public String getProfile_image() {
        return profile_image;
    }

    public void setProfile_image(String profile_image) {
        this.profile_image = profile_image;
    }

    public String getRegistered_at() {
        return registered_at;
    }

    public void setRegistered_at(String registered_at) {
        this.registered_at = registered_at;
    }

    public String getUser_status() {
        return user_status;
    }

    public void setUser_status(String user_status) {
        this.user_status = user_status;
    }

    public String getLastseen_at() {
        return lastseen_at;
    }

    public void setLastseen_at(String lastseen_at) {
        this.lastseen_at = lastseen_at;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCountry_code() {
        return country_code;
    }

    public void setCountry_code(String country_code) {
        this.country_code = country_code;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDevice_os() {
        return device_os;
    }

    public void setDevice_os(String device_os) {
        this.device_os = device_os;
    }

    public String getDevice_model() {
        return device_model;
    }

    public void setDevice_model(String device_model) {
        this.device_model = device_model;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDevice_token() {
        return device_token;
    }

    public void setDevice_token(String device_token) {
        this.device_token = device_token;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    @Override
    public String toString() {
        return "UserDetails [birthday = " + birthday + ", phone = " + phone + ", status = " + status + ", isbot = " + isbot + ", profile_image = " + profile_image + ", registered_at = " + registered_at + ", user_status = " + user_status + ", lastseen_at = " + lastseen_at + ", password = " + password + ", country_code = " + country_code + ", chatUserList = " + id + ", device_os = " + device_os + ", device_model = " + device_model + ", updated_at = " + updated_at + ", email = " + email + ", device_token = " + device_token + ", name = " + name + ", dob = " + dob + ", created_at = " + created_at + ", gender = " + gender + ", mobile = " + mobile + "]";
    }
}

