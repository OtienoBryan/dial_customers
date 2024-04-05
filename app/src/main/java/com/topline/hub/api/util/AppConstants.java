package com.topline.hub.api.util;

public class AppConstants {

    /**
     * Connection timeout duration
     */
    public static final int CONNECT_TIMEOUT = 60 * 1000;
    /**
     * Connection Read timeout duration
     */
    public static final int READ_TIMEOUT = 60 * 1000;
    /**
     * Connection write timeout duration
     */
    public static final int WRITE_TIMEOUT = 60 * 1000;
    /**
     * Base URL
     */
    public static final String BASE_URL = "https://api.safaricom.co.ke/";
    /**
     * global topic to receive app wide push notifications
     */
    public static final String TOPIC_GLOBAL = "global";

    // broadcast receiver intent filters
    public static final String REGISTRATION_COMPLETE = "registrationComplete";
    public static final String PUSH_NOTIFICATION = "pushNotification";

    // id to handle the notification in the notification tray
    public static final int NOTIFICATION_ID = 100;
    public static final int NOTIFICATION_ID_BIG_IMAGE = 101;
    public static final String SHARED_PREF = "ah_firebase";

    //STKPush Properties
    public static final String BUSINESS_SHORT_CODE = "7861733";
    public static final String PASSKEY = "bfb205c2a0b53eb1685038322a8d6ae95abc2d63245eba38e96cc5fe45c84065";
    public static final String TRANSACTION_TYPE = "CustomerBuyGoodsOnline";
    public static final String PARTYB = "5855961";
    public static final String CALLBACKURL = "https://dialadrinkltdco.ke/dial/mobile/customer_mobile/my_call/callback.php?title=stk_push&message=result&push_type=individual&regId=";

}
