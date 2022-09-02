package com.topline.hub;

import android.content.Context;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiInterface {

    @GET("getAppointments.php")
    Call< List<Appointments> > getAppointments(@Query("key") String keyword, @Query("userid") String userId, @Query("adminid") String adminId);

    @GET("getPosm.php")
    Call< List<Posm_model> > getPosm(@Query("key") String keyword, @Query("userid") String userId);

    @GET("getApproval.php")
    Call< List<Appointments> > getApprovalAppointments(@Query("team") String team);

    @GET("getAppointments.php")
    Call< List<Appointments> > getOutletAppointments(@Query("outlet") String outlet, @Query("userid") String userId);

    @GET("getAppointment_old.php")
    Call< List<Appointments> > getOldOutletAppointments(@Query("outlet") String outlet, @Query("userid") String userId);

    @GET("getShareVisibility.php")
    Call< List<ShareVisibility> > getShareVisibility(@Query("admin") String adminId);

    @GET("getProducts.php")
    Call<JsonArray> getProducts(@Query("admin") String adminId);

    @GET("getExpiry_report.php")
    Call< List<ExpiryView_model> > getExpiryReports(@Query("key") String keyword, @Query("userid") String userId);

    @GET("getPrice_report.php")
    Call< List<PriceView_model> > getPriceReports(@Query("key") String keyword, @Query("userid") String userId);

    @GET("getQuality_report.php")
    Call< List<QualityView_model> > getQualityReports(@Query("key") String keyword, @Query("userid") String userId);

    @GET("getMarket_report.php")
    Call< List<MarketView_model> > getMarketReports(@Query("key") String keyword, @Query("userid") String userId);


    @GET("getActivity_report.php")
    Call< List<ActivityTracker_model> > getActivityReports(@Query("key") String keyword, @Query("userid") String userId);

    @GET("getMySos.php")
    Call< List<MySosView_model> > getSos(@Query("key") String keyword, @Query("userid") String userId);

    @GET("getExpiry_product.php")
    Call< List<ExpiryProduct_model> > getExpiryProducts(@Query("key") String keyword, @Query("adminid") String adminId);

    @GET("getSos_product.php")
    Call< List<MySosProduct_model> > getSosProducts(@Query("key") String keyword, @Query("adminid") String adminId);

    @GET("getExpiry_product.php")
    Call< List<PriceProduct_model> > getPriceProducts(@Query("key") String keyword, @Query("adminid") String adminId);

    @GET("getExpiry_product.php")
    Call< List<QualityProduct_model> > getQualityProducts(@Query("key") String keyword, @Query("adminid") String adminId);

    @GET("getCompetitor_product.php")
    Call< List<PriceProductCompetitor_model> > getCompetitorProducts(@Query("competId") String competId);

    @GET("getMainProduct_category.php")
    Call< List<SosProductCategory_model> > getMainProductCategories(@Query("key") String keyword, @Query("adminid") String adminId);

    @GET("getCompetitorProduct_category.php")
    Call< List<SosCategoryCompetitor_model> > getCompetitorProductCategories(@Query("catId") String catId);

    @GET("getAccounts.php")
    Call< List<account_model> > getAccounts();

    @GET("getOutlet.php")
    Call< List<OutletModel> > getOutlets(@Query("userid") String userId);


    //Post Data

    @FormUrlEncoded
    @POST("userActivation.php")
    Call<JsonObject> postActivation(@Field("device_id") String device_id,
                                    @Field("telephone") String telephone,
                                    @Field("device_name") String device_name,
                                    @Field("token") String token);

    @FormUrlEncoded
    @POST("issueTracker.php")
    Call<JsonObject> postIssue(@Field("user_id") String user_id,
                               @Field("name") String name,
                               @Field("phone") String phone,
                               @Field("account") String account,
                               @Field("comment") String comment,
                               @Field("recipient") String recipient,
                               @Field("email") String email,
                               @Field("issue") String issue);
}
