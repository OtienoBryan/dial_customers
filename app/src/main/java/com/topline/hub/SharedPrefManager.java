package com.topline.hub;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Otieno Bryan.
 */

public class SharedPrefManager {

    private static SharedPrefManager mInstance;
    private static Context mCtx;

    private static final String SHARED_PREF_NAME = "mysharedpref12";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_USER_LASTNAME = "lastname";
    private static final String KEY_USER_EMAIL = "useremail";
    private static final String KEY_USER_ID = "userid";
    private static final String KEY_ADMIN_ID = "adminid";
    private static final String KEY_USER_TEAM = "userteam";
    private static final String KEY_USER_ROLE = "role";
    private static final String KEY_USER_TELEPHONE = "usertelephone";
    private static final String KEY_USER_UNIT = "userunit";
    private static final String KEY_USER_ACCOUNT = "admin_id";
    private static final String KEY_CHECK_MPA = "mpa";

    ///// Appointments Shared
   /* private static final String KEY_APPOINT_ADMIN_ID = "appoint_admin_id";
    private static final String KEY_APPOINT_MERCHANDISER_ID = "appoint_admin_id";
    private static final String KEY_APPOINT_MERCHANDISER_NAME = "appoint_admin_id";
    private static final String KEY_APPOINT_OUTLET_ID = "appoint_admin_id";
    private static final String KEY_APPOINT_OUTLET_NAME = "appoint_admin_id";
    private static final String KEY_APPOINT_ADMIN_ID = "appoint_admin_id";
    private static final String KEY_APPOINT_ADMIN_ID = "appoint_admin_id";
    private static final String KEY_APPOINT_ADMIN_ID = "appoint_admin_id";
    private static final String KEY_APPOINT_ADMIN_ID = "appoint_admin_id";
    private static final String KEY_APPOINT_ADMIN_ID = "appoint_admin_id";
    private static final String KEY_APPOINT_ADMIN_ID = "appoint_admin_id";
    private static final String KEY_APPOINT_ADMIN_ID = "appoint_admin_id";
    private static final String KEY_APPOINT_ADMIN_ID = "appoint_admin_id";
    private static final String KEY_APPOINT_ADMIN_ID = "appoint_admin_id";
    private static final String KEY_APPOINT_ADMIN_ID = "appoint_admin_id";
    private static final String KEY_APPOINT_ADMIN_ID = "appoint_admin_id";
    private static final String KEY_APPOINT_ADMIN_ID = "appoint_admin_id"; */





    ///// KEYS FOR WHEN THE USER CHECKIN TO OUTLET.............
    private static final String KEY_APPOINT_ID = "appointId";
    private static final String KEY_OUTLET_ID = "outletId";
    private static final String KEY_OUTLET_NAME = "outletName";

    ///// KEYS FOR WHEN THE USER CHECKIN TO OUTLET.............

    private static final String KEY_USER_DEVICE = "userdevice";
    //private static final String KEY_USER_ACTIVE = "activation";

    private static final String TAG_TOKEN = "tagtoken";

    //private static final String KEY_PRODDUCTS = "products";


    private SharedPrefManager(Context context) {
        mCtx = context;

    }

    public static synchronized SharedPrefManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new SharedPrefManager(context);
        }
        return mInstance;
    }

    public boolean userLogin(int id, String username, String email, String team, String telephone, String unit, String lastname, String account,String role ){

        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putInt(KEY_USER_ID, id);
        editor.putString(KEY_USER_EMAIL, email);
        editor.putString(KEY_USERNAME, username);
        editor.putString(KEY_USER_TELEPHONE, telephone);
        editor.putString(KEY_USER_UNIT, unit);
        editor.putString(KEY_USER_TEAM, team);
        editor.putString(KEY_USER_ROLE, role);
        editor.putString(KEY_USER_LASTNAME, lastname);
        editor.putString(KEY_USER_ACCOUNT, account);

        editor.apply();

        return true;
    }

    public boolean userCheckin(String appointId, int outletId, String outletname){

        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(KEY_APPOINT_ID, appointId);
        editor.putInt(KEY_OUTLET_ID, outletId);
        editor.putString(KEY_OUTLET_NAME, outletname);


        editor.apply();

        return true;
    }

    /*public boolean userProduct(String product){

        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(KEY_PRODDUCTS, product);

        editor.apply();

        return true;
    }

    public boolean isProductExist(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        if(sharedPreferences.getString(KEY_PRODDUCTS, null) != null){
            return true;
        }
        return false;
    }*/


    public boolean isLoggedIn(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        if(sharedPreferences.getString(KEY_USERNAME, null) != null){
            return true;
        }
        return false;
    }

    public boolean logout(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        return true; 
    }


    //this method will save the device token to shared preferences
    public boolean saveDeviceToken(String token){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(TAG_TOKEN, token);
        editor.apply();
        return true;
    }

    //this method will fetch the device token from shared preferences
    public String getDeviceToken(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return  sharedPreferences.getString(TAG_TOKEN, null);
    }



    public String getUsername(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_USERNAME, null);
    }

    public String getUserRole(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_USER_ROLE, null);
    }

    public String getUserEmail(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_USER_EMAIL, null);
    }

    public String getUserTelephone(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_USER_TELEPHONE, null);
    }


    public String getUserTeam(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_USER_TEAM,null);
    }

    public static Integer getUserId(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(KEY_USER_ID,0);
    }

    public String getUserUnit(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_USER_UNIT, null);
    }


    public String getUserLastname() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_USER_LASTNAME, null);
    }

    public String getUserAccount() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_USER_ACCOUNT, null);
    }




    public String getAppointId() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_APPOINT_ID, null);
    }

    public Integer getOutletId() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(KEY_OUTLET_ID, 0);

    }

    public String getOutletName() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_OUTLET_NAME, null);
    }
    public String getUserIdString() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return "" + sharedPreferences.getInt(KEY_USER_ID, 0);
    }


    public String getAdminIdString() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return "" + sharedPreferences.getInt(KEY_ADMIN_ID, 0);
    }

    public boolean removeMpa(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(KEY_CHECK_MPA, false);
        editor.apply();
        return true;
    }

    public boolean isMpa(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        if(sharedPreferences.getBoolean(KEY_CHECK_MPA, false) != false){
            return true;
        }
        return false;
    }
    public boolean doneMPA(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(KEY_CHECK_MPA, true);
        editor.apply();
        return true;
    }
}
