package com.topline.hub;

/**
 * Created by Otieno Bryan on 26/08/22.
 */

public class Constants {

   // e64a4f78be2256a38de080744dd5b117

    //private static final String ROOT_URL = "http://geojakltd.co.ke/impulse/mobile/v1/"; // this for live server
    private static final String ROOT_URL = "https://dialadrinkltdco.ke/dial/mobile/customer_mobile/";
    //private static final String ROOT_URL = "http://192.168.1.251:8080/Topline_project/topline/mobile/v1/";

    //public static final String URL_CATIMAGE = "http://boschsafarini.geojakltd.co.ke/mobile/v1/categories/";
    //public static final String URL_SUBCATIMAGE = "http://boschsafarini.geojakltd.co.ke/mobile/v1/sub_categories/";
    public static final String URL_IMAGE_UPLOAD = "https://dialadrinkltdco.ke/dial/admin/mobile_images.php?user_id=";
    public static final String URL_IMAGE_UPLOAD_REPORT = "https://dialadrinkltdco.ke/dial/admin/report_images.php";
    //public static final String URL_IMAGE_POSM_REPORT = "http://curlycare.co.ke/topline/admin/report_posm_images.php";

    public static final String URL_GETAPPOINTMENT = ROOT_URL+"getAppointment.php?clickeditem=";
    public static final String URL_TASK = ROOT_URL+"Tasks.php?clickeditem=";
    public static final String URL_TERMINATE_TASK = ROOT_URL+"terminateTask.php";
    public static final String URL_OUTLETS = ROOT_URL+"Outlets.php?clickeditem=";
    public static final String URL_MENTAL = ROOT_URL+"Mental.php?clickeditem=";
    public static final String URL_NUTRITION = ROOT_URL+"Nutrition.php?clickeditem=";
    public static final String URL_LIFE = ROOT_URL+"Life.php?clickeditem=";
    public static final String URL_OUTLETS_VISITED = ROOT_URL+"outletsVisited.php?clickeditem=";
    public static final String URL_CHECKIN = ROOT_URL+"checkin.php";
    public static final String URL_CANCEL = ROOT_URL+"cancelAppoint.php";
    public static final String URL_CHECKOUT = ROOT_URL+"checkout.php";
    public static final String URL_IMAGE_VALIDATION = ROOT_URL+"imageValidation.php";
    public static final String URL_VISIT_VALIDATION = ROOT_URL+"visitValidation.php";

    public static final String URL_ABOUT = ROOT_URL+"About.php?clickeditem=";
    public static final String URL_TERMS = ROOT_URL+"Terms.php?clickeditem=";
    public static final String URL_PRIVACY = ROOT_URL+"Privacy.php?clickeditem=";
    public static final String URL_TESTIMONIALS = ROOT_URL+"Testimonials.php?clickeditem=";

    public static final String URL_NOTICE = ROOT_URL+"Notice.php?clickeditem=";
    public static final String URL_PAY = ROOT_URL+"Pay.php?clickeditem=";

    public static final String URL_RECURRING_UPDATE = ROOT_URL+"recurring_update.php";
    public static final String URL_ADD_APPOINTMENT = ROOT_URL+"appointment.php";


    public static final String URL_REGISTER = ROOT_URL+"registerUser.php";
    public static final String URL_LOGIN = ROOT_URL+"userLogin.php";


    public static final String URL_ACTIVATION = ROOT_URL+"userActivation.php";

    public static final String URL_NEW_DEALER = ROOT_URL+"newDealer.php";
    public static final String URL_NEW_USER = ROOT_URL+"newUser.php";

    public static final String URL_APPOINTMENT = ROOT_URL+"appointment.php";
    public static final String URL_LOCATION = ROOT_URL+"userLocation.php";

 //public static final String URL_REGISTER_DEVICE = "http://192.168.0.15/FcmExample/RegisterDevice.php";
 public static final String URL_SEND_SINGLE_PUSH = ROOT_URL+"sendSinglePush.php";
    public static final String URL_SEND_RANGER_PUSH = ROOT_URL+"sendRangerPush.php";
 public static final String URL_SEND_MULTIPLE_PUSH = ROOT_URL+"sendMultiplePush.php";
 public static final String URL_FETCH_DEVICES = ROOT_URL+"GetRegisteredDevices.php";

 public static final String URL_FETCH_PRODUCT_CATEGORIES = ROOT_URL+"GetProductCategory.php?selecteditem=";
    public static final String URL_BRANDS = ROOT_URL+"GetProductBrands.php?selecteditem=";
    public static final String URL_SERVICE = ROOT_URL+"services.php?selecteditem=";
 public static final String URL_FETCH_PRODUCT_COMPETITOR = ROOT_URL+"GetProductCompetitor.php?selecteditem=";
    public static final String URL_FETCH_PROMOTIONS = ROOT_URL+"GetRegisteredPromotion.php";
    public static final String URL_COMPETITIVE_ACTIVITY = ROOT_URL+"competitiveActivity.php";
    public static final String URL_BIDCO_ACTIVITY = ROOT_URL+"bidcoActivity.php";
    public static final String URL_CLIENT_INQUIRY = ROOT_URL+"clientInquiry.php";
    public static final String URL_COMPE_ACTIVITY = ROOT_URL+"compeActivity.php";
    public static final String URL_POSM_REPORT = ROOT_URL+"posmReport.php";
    public static final String URL_POST_ORDER = ROOT_URL+"product_expiryReport.php";
    public static final String URL_PRICE_WATCH_REPORT = ROOT_URL+"price_watchReport.php";
    public static final String URL_QUALITY_REPORT = ROOT_URL+"qualityReport.php";
    public static final String URL_MARKET_REPORT = ROOT_URL+"marketReport.php";
    public static final String URL_PROMOTION_ACTIVITY = ROOT_URL+"promotionReport.php";
    public static final String URL_STOCK_ACTIVITY = ROOT_URL+"stockReport.php";
    public static final String URL_SOSNEW_REPORT = ROOT_URL+"newSosReport.php";
    public static final String URL_SOS_REPORT = ROOT_URL+"sosReport.php";
    public static final String URL_PRODUCT_EXPIRY_REPORT_UPDATE = ROOT_URL+"product_expiryReport_update.php";
    public static final String URL_NEW_EXPIRY_REPORT_UPDATE = ROOT_URL+"new_expiryReport_update.php";

////// FOR QUESTIONNAIRES
    public static final String URL_PRODUCT_CATEGORY = ROOT_URL+"productCategory.php?clickeditem=";
    public static final String URL_PRODUCTS = ROOT_URL+"Products.php?clickeditem=";
    public static final String URL_HOME_PRODUCTS = ROOT_URL+"home_products.php?clickeditem=";
    public static final String URL_ASSETS = ROOT_URL+"Assets.php?clickeditem=";
    public static final String URL_STOCKS = ROOT_URL+"getStockCount.php?clickeditem=";
    public static final String URL_GET_APPROVAL = ROOT_URL+"getApproval.php?team=";
    public static final String URL_POST_APPROVAL = ROOT_URL+"postApproval.php";

    public static final String URL_MY_CATEGORY = ROOT_URL+"category_products.php?clickeditem=";
    public static final String URL_CART = ROOT_URL+"cart.php?clickeditem=";
    public static final String URL_ORDER_ITEMS = ROOT_URL+"order_items.php?clickeditem=";
    public static final String URL_ORDERS = ROOT_URL+"my_orders.php?clickeditem=";
    public static final String URL_COMPLETE = ROOT_URL+"complete.php?clickeditem=";
    public static final String URL_POST_CHECKOUT = ROOT_URL+"checkoutReport.php";
    public static final String URL_POST_REORDER = ROOT_URL+"re_order.php";
    public static final String URL_COMPLETE_ORDER = ROOT_URL+"confirm_complete.php";

    //views
    public static final String URL_MY_OFFERS = ROOT_URL+"offers.php?clickeditem=";
    public static final String URL_ARRIVALS = ROOT_URL+"arrivals.php?clickeditem=";
    public static final String URL_POPULAR = ROOT_URL+"popular.php?clickeditem=";
    public static final String URL_GIFT = ROOT_URL+"gifts.php?clickeditem=";
    public static final String URL_RELATED = ROOT_URL+"related.php?clickeditem=";


    //APis by Bryan Otieno
    public static final String URL_ORDER = ROOT_URL+"postOrder.php";
    public static final String URL_DELIVERY = ROOT_URL+"postDelivery.php";
    public static final String URL_FOLLOWUP = ROOT_URL+"postFollowupAppointment.php";
    public static final String URL_OBJECTIVE = ROOT_URL+"postObjective.php";
    public static final String URL_COACHING= ROOT_URL+"postCoaching.php";
    public static final String URL_MPA_PRODUCTS = ROOT_URL+"getMpa.php";
    public static final String URL_MPA = ROOT_URL+"postMpa.php";
    public static final String URL_NPL = ROOT_URL+"postNPL.php";
    public static final String URL_AVAILABILITY = ROOT_URL+"postAvailability.php";
    public static final String URL_REPORT_ORDER = ROOT_URL+"postTeamleaderOrder.php";
    public static final String URL_REPORT_DELIVERY = ROOT_URL+"postTeamleaderDelivery.php";
    public static final String URL_REPORT_BA = ROOT_URL+"postBa.php";






}
