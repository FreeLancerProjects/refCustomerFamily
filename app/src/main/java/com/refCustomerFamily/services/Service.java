package com.refCustomerFamily.services;


import com.refCustomerFamily.activities_fragments.stores.google_place_modul.models.CategoryDataModel;
import com.refCustomerFamily.activities_fragments.stores.google_place_modul.models.NearbyModel;
import com.refCustomerFamily.activities_fragments.stores.google_place_modul.models.PlaceDetailsModel;
import com.refCustomerFamily.activities_fragments.stores.google_place_modul.models.PlaceGeocodeData;
import com.refCustomerFamily.models.FamilyModel;
import com.refCustomerFamily.models.SubCategoryFamilyModel;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
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


    @GET("api/categories-with-families")
    Call<SubCategoryFamilyModel> getCategoriesWithFamilies(
            @Query("pagination_status") String pagination_status,
            @Query("per_link_") int per_link_,
            @Query("page") int page);



    @GET("api/family-by-family-id")
    Call<FamilyModel> getFamilyByFamilyId(
            @Query("family_id") int family_id
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