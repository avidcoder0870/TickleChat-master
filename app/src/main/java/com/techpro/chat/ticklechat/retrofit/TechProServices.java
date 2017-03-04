package com.techpro.chat.ticklechat.retrofit;

import com.squareup.okhttp.ResponseBody;

import java.util.Map;

import retrofit.Call;
import retrofit.http.Field;
import retrofit.http.FieldMap;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.Part;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by viveksingh on 18/11/15.
 * <p/>
 * this class contain the methods for getting the retrofit response
 */
public interface TechProServices {


    @FormUrlEncoded
    @POST("/register/gcm/")
    Call<ResponseBody> registerGCM(@Header("Authorization") String authorization, @Field("device_uid") String deviceid, @Field("token") String token);
    //void registerGCM(@Header("Authorization") String authorization, @Field("device_uid") String deviceid, @Field("token") String token, Callback<RequestResponse> authCallback);


//    @FormUrlEncoded
//    @POST("/register")
//    Call<SignUpBean> getSignUpResponse(@FieldMap Map<String, String> map);
//
//    @FormUrlEncoded
//    @POST("/login")
//    Call<ResponseBody> getLoginResponse(@FieldMap Map<String, String> map);
//
//    @GET("/find/services")
//    Call<ServiceBean> getSelectedServices(@Query("query") String query);
//
//    @GET("/subcategories/list")
//    Call<CategoryBean> getSelectedCategory(@Header("Authorization") String header);
//
//    @GET("/profile/details")
//    Call<ProfileBean> getProfileDetails(@Header("Authorization") String header);
//
//    @Multipart
//    @POST("/profile/images/upload/")
//    Call<ImageDataBean> uploadFile(@Header("Authorization") String token,
//                                   @Part("file\";filename=\"image.png\" ") RetrofitProgressRequestBody file);
//
//    @FormUrlEncoded
//    @POST("/profile/edit")
//    Call<ProfileBean> editProfile(@Header("Authorization") String token, @FieldMap Map<String, String> map);
//
//    @GET("/invoices")
//    Call<InvoiceBean> getInvoices(@Header("Authorization") String header);
//
//    @GET("/profile/images")
//    Call<ImagesBean> getImages(@Header("Authorization") String token);
//
//    @GET("/dashboard")
//    Call<DashboardBean> getDashboard(@Header("Authorization") String token);
//
//    @FormUrlEncoded
//    @POST("/profile/images/reorder")
//    Call<ImagesBean> reorderImages(@Header("Authorization") String token, @Field("ids_new_order") String newOrder);
//
//    @POST("/profile/images/{imageId}/delete")
//    Call<DeleteImage> deleteImage(@Header("Authorization") String token, @Path("imageId") String imageId);
//
//    @GET("/reviews")
//    Call<ReviewsBeans> getReviewsAll(@Header("Authorization") String token);
//
//    @GET("/reviews/pending")
//    Call<ReviewsBeans> getReviewsPending(@Header("Authorization") String token);
//
//    @GET("/calls")
//    Call<CallBean> getCallResponse(@Header("Authorization") String token);
//
//    @GET("/reviews/{reviewId}/replies")
//    Call<ReviewReplyBean> getReviewReplies(@Header("Authorization") String token,
//                                           @Path("reviewId") String reviewId);

    @FormUrlEncoded
    @POST("/reviews/{reviewId}/replies/post")
    Call<ResponseBody>
    postReviewUserReply(@Header("Authorization") String token, @Path("reviewId") String reviewId, @FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("/reviews/replies/{reviewId}/update")
    Call<ResponseBody>
    updateReviewUserReply(@Header("Authorization") String token, @Path("reviewId") String reviewId, @FieldMap Map<String, String> map);

//    @FormUrlEncoded
//    @POST("/register/gcm")
//    Call<GcmBean> registerGcm(@Header("Authorization") String token, @FieldMap Map<String, String> map);
//
//    @GET("/localities/list")
//    Call<LocationBean> getLocation(@Header("Authorization") String token, @Query("city_id") String city);
}