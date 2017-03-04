package com.techpro.chat.ticklechat.rest;

import com.google.gson.JsonObject;
import com.techpro.chat.ticklechat.models.CustomModel;
import com.techpro.chat.ticklechat.models.GetGroupDetails;
import com.techpro.chat.ticklechat.models.message.AllMessages;
import com.techpro.chat.ticklechat.models.message.CreateGroup;
import com.techpro.chat.ticklechat.models.message.SendMessage;
import com.techpro.chat.ticklechat.models.message.Tickles;
import com.techpro.chat.ticklechat.models.user.GetUserDetails;
import com.techpro.chat.ticklechat.models.user.UserModel;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;


public interface ApiInterface {

    //Get response in GSON object
    @GET("users/{userid}")
    Call<GetUserDetails> getUserDetailsFromID(@Path("userid") int userId);
    //Get response in GSON object

    @GET("messages/all")
    Call<AllMessages> getAllMessageList();

    @POST("users/random")
    Call<GetUserDetails> getRandomUser();

    @GET("tickles")
    Call<Tickles> getTickles();

    @GET("bots")
    Call<CustomModel> getBot();

    @GET("bots/{botId}")
    Call<CustomModel> deleteBot(@Path("userid") int userid);

    @GET("bots")
    Call<CustomModel> createBot(@Field("name") String name, @Field("image") String image);

    @GET("tickles/{userid}/tickles")
    Call<Tickles> getTickles(@Path("userid") int userid);

    @GET("groups/{groupId}")
    Call<GetGroupDetails> getGroupDetials(@Path("groupId") int groupId);

    @FormUrlEncoded
    @PUT("users/{userid}")
    Call<UserModel> callUpdateUserDataService(@Path("userid") int userid, @Field("name") String name, @Field("gender") String gender,
                                              @Field("dob") String dob, @Field("phone") String phone, @Field("email") String email, @Field("profile_image") String profile_image);
    @FormUrlEncoded
    @POST("users")
    Call<UserModel> registeruser(@Field("name") String name, @Field("gender") String gender,@Field("dob") String dob, @Field("phone")
            String phone, @Field("email") String email, @Field("profile_image") String profile_image,
                                 @Field("country_code") String country_code,  @Field("password") String password);


    @FormUrlEncoded
    @POST("tickles")
    Call<JSONObject> callAddSentenceService(@Field("message") String message, @Field("requester") String requester);

    @FormUrlEncoded
    @PUT("users/{userid}/status")
    Call<JsonObject> callupdateStatusService(@Path("userid") int userid, @Field("status") String status);

    @FormUrlEncoded
    @POST("messages")
    Call<SendMessage> postChatMessage(@Field("tickleId") String tickleId, @Field("withId") String withId, @Field("message") String message);

    @FormUrlEncoded
    @POST("groups")
    Call<CreateGroup> postNewGroup(@Field("name") String name, @Field("image") String image, @Field("created_by") String created_by, @Field("admin") String admin);

    @FormUrlEncoded
    @POST("groups/{groupId}/addmembers")
    Call<JsonObject> postGroupMembers(@Path("groupId") int groupId, @Field("members") String members);

    @FormUrlEncoded
    @POST("users/login")
    Call<UserModel> loginUser(@Field("phone") String phone, @Field("password") String password);

    @FormUrlEncoded
    @PUT("users/{userid}/status")
    Call<UserModel> statusUpdate(@Path("userid") int userId, @Field("status") String status);

    @FormUrlEncoded
    @PUT("users/{userid}")
    Call<UserModel> updateUserDetails(@Path("userid") int userId, @Field("name") String name,
                                      @Field("gender") String gender, @Field("dob") String dob,
                                      @Field("phone") String phone, @Field("email") String email,
                                      @Field("profile_image") String profile_image);

    @DELETE("users/{userid}")
    Call<JSONObject> deleteUser(@Path("userid") int userId);

    @FormUrlEncoded
    @POST("users/updateDT")
    Call<JsonObject> UpdateDeviceTockan (@Field("devicetoken") String devicetoken);


    @FormUrlEncoded
    @POST("users/social_login")
    Call<JsonObject> signInUsingSocialSdk(@Field("name") String name, @Field("gender") String gender, @Field("dob") String dob,
                                          @Field("phone") String phone, @Field("email") String email, @Field("profile_image") String profileImage,
                                          @Field("country_code") String countryCode, @Field("password") String password, @Field("sourceType") String sourceType);

    /*//Get response in json string
    @GET("users/{userid}")
    Call<ResponseBody> getUserDetails(@Path("userid") int chatUserList);*/

    @FormUrlEncoded
    @POST("users/check_if_registered")
    Call<JsonObject> getRegisteredUser(@Field("list") String list);

}
