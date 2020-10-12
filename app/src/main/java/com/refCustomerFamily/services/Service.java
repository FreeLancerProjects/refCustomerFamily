package com.refCustomerFamily.services;


import com.refCustomerFamily.activities_fragments.stores.google_place_modul.models.CategoryDataModel;
import com.refCustomerFamily.activities_fragments.stores.google_place_modul.models.NearbyModel;
import com.refCustomerFamily.activities_fragments.stores.google_place_modul.models.PlaceDetailsModel;
import com.refCustomerFamily.activities_fragments.stores.google_place_modul.models.PlaceGeocodeData;
import com.refCustomerFamily.models.FamilyCategoryProductDataModel;
import com.refCustomerFamily.models.FamilyModel;
import com.refCustomerFamily.models.NotificationModel;
import com.refCustomerFamily.models.SettingModel;
import com.refCustomerFamily.models.SingleOrderDataModel;
import com.refCustomerFamily.models.SliderModel;
import com.refCustomerFamily.models.SubCategoryFamilyModel;
import com.refCustomerFamily.models.UserModel;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface Service {

    @GET("place/nearbysearch/json")
    Call<NearbyModel> nearbyPlaceRankBy(@Query(value = "location") String location,
                                        @Query(value = "keyword") String keyword,
                                        @Query(value = "rankby") String rankby,
                                        @Query(value = "language") String language,
                                        @Query(value = "pagetoken") String pagetoken,
                                        @Query(value = "key") String key
    );


    @FormUrlEncoded
    @POST("api/login")
    Call<UserModel> login(@Field("phone_code") String phone_code,
                          @Field("phone") String phone

    );


    @FormUrlEncoded
    @POST("api/register-client")
    Call<UserModel> signUpWithoutImage(@Field("name") String name,
                                       @Field("email") String email,
                                       @Field("phone_code") String phone_code,
                                       @Field("phone") String phone,
                                       @Field("software_type") String software_type


    );

    @Multipart
    @POST("api/register-client")
    Call<UserModel> signUpWithImage(@Part("name") RequestBody name,
                                    @Part("email") RequestBody email,
                                    @Part("phone_code") RequestBody phone_code,
                                    @Part("phone") RequestBody phone,
                                    @Part MultipartBody.Part logo,
                                    @Part("software_type") RequestBody software_type


    );


    @GET("api/categories-with-families")
    Call<SubCategoryFamilyModel> getCategoriesWithFamilies(
            @Query("pagination_status") String pagination_status,
            @Query("per_link_") int per_link_,
            @Query("page") int page);


    @GET("api/family-by-family-id")
    Call<FamilyModel> getFamilyByFamilyId(
            @Query("family_id") int family_id
    );


    @GET("api/famlies-by-category-id")
    Call<FamilyModel> getFamilyByCategoryId(
            @Query("category_id") int category_id
    );


    @GET("place/nearbysearch/json")
    Call<NearbyModel> nearbyPlaceInDistance(@Query(value = "location") String location,
                                            @Query(value = "keyword") String keyword,
                                            @Query(value = "radius") int radius,
                                            @Query(value = "language") String language,
                                            @Query(value = "pagetoken") String pagetoken,
                                            @Query(value = "key") String key
    );


    @GET("geocode/json")
    Call<PlaceGeocodeData> getGeoData(@Query(value = "latlng") String latlng,
                                      @Query(value = "language") String language,
                                      @Query(value = "key") String key);

    @GET("place/details/json")
    Call<PlaceDetailsModel> getPlaceDetails(@Query(value = "placeid") String placeid,
                                            @Query(value = "fields") String fields,
                                            @Query(value = "language") String language,
                                            @Query(value = "key") String key
    );

    @GET("api/google-categories")
    Call<CategoryDataModel> getCategory();


    @GET
    Call<ResponseBody> getFullUrl(@Url String url);


    @GET("api/slider")
    Call<SliderModel> getSliders();

    @FormUrlEncoded
    @POST("api/create-order")
    Call<SingleOrderDataModel> sendTextOrder(@Header("Authorization") String user_token,
                                             @Field("user_id") int user_id,
                                             @Field("family_id") int family_id,
                                             @Field("order_type") String order_type,
                                             @Field("google_place_id") String google_place_id,
                                             @Field("bill_cost") String bill_cost,
                                             @Field("to_address") String to_address,
                                             @Field("to_latitude") double to_latitude,
                                             @Field("to_longitude") double to_longitude,
                                             @Field("from_name") String from_name,
                                             @Field("from_address") String from_address,
                                             @Field("from_latitude") double from_latitude,
                                             @Field("from_longitude") double from_longitude,
                                             @Field("end_shipping_time") String end_shipping_time,
                                             @Field("coupon_id") String coupon_id,
                                             @Field("order_description") String order_description,
                                             @Field("order_nots") String order_nots,
                                             @Field("payment_method") String payment_method,
                                             @Field("hour_arrival_time") String hour_arrival_time


    );

    @Multipart
    @POST("api/create-order")
    Call<SingleOrderDataModel> sendTextOrderWithImage(@Header("Authorization") String user_token,
                                                      @Part("user_id") RequestBody user_id,
                                                      @Part("family_id") RequestBody family_id,
                                                      @Part("order_type") RequestBody order_type,
                                                      @Part("google_place_id") RequestBody google_place_id,
                                                      @Part("bill_cost") RequestBody bill_cost,
                                                      @Part("to_address") RequestBody to_address,
                                                      @Part("to_latitude") RequestBody to_latitude,
                                                      @Part("to_longitude") RequestBody to_longitude,
                                                      @Part("from_name") RequestBody from_name,
                                                      @Part("from_address") RequestBody from_address,
                                                      @Part("from_latitude") RequestBody from_latitude,
                                                      @Part("from_longitude") RequestBody from_longitude,
                                                      @Part("end_shipping_time") RequestBody end_shipping_time,
                                                      @Part("coupon_id") RequestBody coupon_id,
                                                      @Part("order_description") RequestBody order_description,
                                                      @Part("payment_method") RequestBody payment_method,
                                                      @Part("order_nots") RequestBody order_nots,
                                                      @Part("hour_arrival_time") RequestBody hour_arrival_time,
                                                      @Part List<MultipartBody.Part> images

    );

    @GET("api/categories-with-families-by-family-id")
    Call<FamilyCategoryProductDataModel> getFamilyCategory_Products(@Query("family_id") int family_id);

    @GET("api/sttings")
    Call<SettingModel> getSetting();
    @GET("api/Get-my-notifications")
    Call<NotificationModel> getNotification(@Header("Authorization") String user_token,
                                            @Query("user_id") int user_id);

}