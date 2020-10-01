package com.refCustomerFamily.services;


import com.refCustomerFamily.activities_fragments.stores.google_place_modul.models.CategoryDataModel;
import com.refCustomerFamily.activities_fragments.stores.google_place_modul.models.NearbyModel;
import com.refCustomerFamily.activities_fragments.stores.google_place_modul.models.PlaceDetailsModel;
import com.refCustomerFamily.activities_fragments.stores.google_place_modul.models.PlaceGeocodeData;
import com.refCustomerFamily.models.FamilyModel;
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


}