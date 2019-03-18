package com.sk.workextinder.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.sk.workextinder.base.MyApplicationClass;

/*
 * Created by Sambhaji Karad on 12-03-2019
 */

public class AppPreferenceStorage {

    private static final String mAppPref = "mAppPref";
    private static final String user_login_status = "user_login_user";
    private static final String user_details = "user_details";
    private static Boolean USER_LOGIN_STATUS = false;
    private static String USER_DETAILS = "";

    public static void saveUserLoginStatus(Boolean status) {
        SharedPreferences hxPrefs = MyApplicationClass.getAppContext().getSharedPreferences(mAppPref, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = hxPrefs.edit();
        editor.putBoolean(user_login_status, status);

        setUserActiveStatus(status);
        editor.apply();
    }

    public static void saveUserDetails(String userDetails) {
        SharedPreferences hxPrefs = MyApplicationClass.getAppContext().getSharedPreferences(mAppPref, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = hxPrefs.edit();
        editor.putString(user_details, userDetails);

        setUserDetails(userDetails);
        editor.apply();
    }

    public static void removePreference() {
        SharedPreferences hxPrefs = MyApplicationClass.getAppContext().getSharedPreferences(mAppPref, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = hxPrefs.edit();

        editor.clear();
        editor.apply();
    }

    public static Boolean getUserLoginStatus() {
        SharedPreferences hxPrefs = MyApplicationClass.getAppContext().getSharedPreferences(mAppPref, Context.MODE_PRIVATE);
        USER_LOGIN_STATUS = hxPrefs.getBoolean(user_login_status, false);
        return USER_LOGIN_STATUS;
    }

    private static void setUserActiveStatus(Boolean status) {
        USER_LOGIN_STATUS = status;
    }

    public static String getUserDetails() {
        SharedPreferences hxPrefs = MyApplicationClass.getAppContext().getSharedPreferences(mAppPref, Context.MODE_PRIVATE);
        USER_DETAILS = hxPrefs.getString(user_details, "");
        return USER_DETAILS;
    }

    private static void setUserDetails(String userDetails) {
        USER_DETAILS = userDetails;
    }
}