package com.refCustomerFamily.activities_fragments.stores.google_place_modul.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ethanhua.skeleton.Skeleton;
import com.ethanhua.skeleton.SkeletonScreen;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.SphericalUtil;
import com.refCustomerFamily.R;
import com.refCustomerFamily.activities_fragments.stores.google_place_modul.activity_fragments.activity_google_stores.GoogleStoresActivity;
import com.refCustomerFamily.databinding.MainCategoryDataRowBinding;
import com.refCustomerFamily.databinding.MainSliderRowBinding;
import com.refCustomerFamily.activities_fragments.stores.google_place_modul.models.CategoryDataModel;
import com.refCustomerFamily.activities_fragments.stores.google_place_modul.models.CategoryModel;
import com.refCustomerFamily.activities_fragments.stores.google_place_modul.models.MainItemData;
import com.refCustomerFamily.activities_fragments.stores.google_place_modul.models.NearbyModel;
import com.refCustomerFamily.remote.Api;
import com.refCustomerFamily.tags.Tags;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final int SLIDER = 1;
    private final int DATA = 2;

    private List<MainItemData> list;
    private Context context;
    private LayoutInflater inflater;
    private List<NearbyModel.Result> resultList;
    private boolean hasManyPages = false;
    private boolean isLoading = false;
    private String query = "food|restaurant|supermarket|bakery";
    private String next_page = "";
    private SkeletonScreen skeletonPopular, skeletonCategory;
    private double user_lat = 0.0, user_lng = 0.0;
    private NearbyAdapter2 nearbyAdapter;
    private PlaceCategoryAdapter categoryAdapter;
    private List<CategoryModel> categoryModelList;
    private GoogleStoresActivity activity;
    private String lang;
    private MainSliderRowBinding mainSliderRowBinding;
    private String currency;

    public MainAdapter(List<MainItemData> list, Context context, double user_lat, double user_lng, String currency) {
        this.list = list;
        this.context = context;
        inflater = LayoutInflater.from(context);
        resultList = new ArrayList<>();
        categoryModelList = new ArrayList<>();
        this.user_lat = user_lat;
        this.user_lng = user_lng;
        Paper.init(context);
        lang = Paper.book().read("lang", "ar");
        activity = (GoogleStoresActivity) context;
        this.currency = currency;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType == DATA) {
            MainCategoryDataRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.main_category_data_row, parent, false);
            return new MyHolder(binding);
        } else {
            MainSliderRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.main_slider_row, parent, false);
            return new SliderHolder(binding);
        }


    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof MyHolder) {
            MyHolder myHolder = (MyHolder) holder;
            myHolder.binding.recView.setLayoutManager(new GridLayoutManager(context, 2));


            skeletonCategory = Skeleton.bind(myHolder.binding.recView)
                    .frozen(false)
                    .duration(1000)
                    .shimmer(true)
                    .count(8)
                    .load(R.layout.category_row)
                    .show();

            getCategory(myHolder.binding);

        } else if (holder instanceof SliderHolder) {
            SliderHolder sliderHolder = (SliderHolder) holder;

            sliderHolder.binding.recViewPopular.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
            sliderHolder.binding.recViewPopular.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    if (dx > 0) {
                        int totalItem = nearbyAdapter.getItemCount();
                        LinearLayoutManager manager = (LinearLayoutManager) sliderHolder.binding.recViewPopular.getLayoutManager();
                        int pos = manager.findLastCompletelyVisibleItemPosition();
                        if (hasManyPages && totalItem >= 18 && (totalItem - pos == 2) && !isLoading) {
                            isLoading = true;
                            loadMore();

                        }
                    }
                }
            });


            skeletonPopular = Skeleton.bind(sliderHolder.binding.recViewPopular)
                    .frozen(false)
                    .duration(1000)
                    .shimmer(true)
                    .count(20)
                    .load(R.layout.shop_row)
                    .show();

            getNearByShops(sliderHolder.binding);


        }


    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public static class MyHolder extends RecyclerView.ViewHolder {
        public MainCategoryDataRowBinding binding;

        public MyHolder(@NonNull MainCategoryDataRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }

    public static class SliderHolder extends RecyclerView.ViewHolder {
        public MainSliderRowBinding binding;

        public SliderHolder(@NonNull MainSliderRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }


    @Override
    public int getItemViewType(int position) {
        MainItemData itemData = list.get(position);
        if (itemData.getType() == 0) {
            return SLIDER;
        } else {
            return DATA;
        }


    }

    private void getNearByShops(MainSliderRowBinding binding) {
        String loc = user_lat + "," + user_lng;
        Log.e("loc", loc);
        Api.getService("https://maps.googleapis.com/maps/api/")
                .nearbyPlaceRankBy(loc, query, "distance", lang, "", context.getString(R.string.map_api_key))
                .enqueue(new Callback<NearbyModel>() {
                    @Override
                    public void onResponse(Call<NearbyModel> call, Response<NearbyModel> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            if (response.body().getStatus().equals("OK")) {

                                if (response.body().getNext_page_token() != null) {
                                    hasManyPages = true;
                                    next_page = response.body().getNext_page_token();
                                } else {
                                    hasManyPages = false;
                                    next_page = "";
                                }

                                if (response.body().getResults().size() > 0) {
                                    calculateDistance(response.body().getResults(), binding);

                                } else {
                                    binding.llPopular.setVisibility(View.GONE);

                                }
                            } else {
                                binding.llPopular.setVisibility(View.GONE);

                            }

                        } else {

                            skeletonPopular.hide();
                            binding.llPopular.setVisibility(View.GONE);

                            try {
                                Log.e("error_code", response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }


                    }

                    @Override
                    public void onFailure(Call<NearbyModel> call, Throwable t) {
                        try {
                            Log.e("Error", t.getMessage());
                            skeletonPopular.hide();
                            binding.llPopular.setVisibility(View.GONE);


                            if (t.getMessage() != null) {
                                Log.e("error", t.getMessage());
                                if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                    Toast.makeText(context, context.getString(R.string.something), Toast.LENGTH_LONG).show();
                                } else if (t.getMessage().toLowerCase().contains("socket") || t.getMessage().toLowerCase().contains("canceled")) {
                                } else {
                                    Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }

                        } catch (Exception e) {

                        }
                    }
                });
    }

    private void loadMore() {

        resultList.add(null);
        nearbyAdapter.notifyItemInserted(resultList.size() - 1);


        String loc = user_lat + "," + user_lng;
        Log.e("loc", loc);
        Api.getService("https://maps.googleapis.com/maps/api/")
                .nearbyPlaceRankBy(loc, query, "distance", lang, next_page, context.getString(R.string.map_api_key))
                .enqueue(new Callback<NearbyModel>() {
                    @Override
                    public void onResponse(Call<NearbyModel> call, Response<NearbyModel> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            if (response.body().getStatus().equals("OK")) {

                                if (response.body().getNext_page_token() != null) {
                                    hasManyPages = true;
                                    next_page = response.body().getNext_page_token();
                                } else {
                                    hasManyPages = false;
                                    next_page = "";
                                }
                                if (response.body().getResults().size() > 0) {

                                    calculateDistanceLoadMore(response.body().getResults());
                                } else {
                                    isLoading = false;

                                    if (resultList.get(resultList.size() - 1) == null) {
                                        resultList.remove(resultList.size() - 1);
                                        nearbyAdapter.notifyItemRemoved(resultList.size() - 1);
                                    }
                                }
                            }

                        } else {

                            try {
                                Log.e("error_code", response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }


                    }

                    @Override
                    public void onFailure(Call<NearbyModel> call, Throwable t) {
                        try {

                            if (t.getMessage() != null) {
                                Log.e("error", t.getMessage());
                                if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                    Toast.makeText(context, context.getString(R.string.something), Toast.LENGTH_LONG).show();
                                } else if (t.getMessage().toLowerCase().contains("socket") || t.getMessage().toLowerCase().contains("canceled")) {
                                } else {
                                    Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }

                        } catch (Exception e) {

                        }
                    }
                });
    }

    private void calculateDistance(List<NearbyModel.Result> results, MainSliderRowBinding binding) {
        this.mainSliderRowBinding = binding;
        for (int i = 0; i < results.size(); i++) {
            NearbyModel.Result result = results.get(i);

            if (result != null) {

                LatLng user_location = new LatLng(user_lat, user_lng);
                LatLng place_location = new LatLng(result.getGeometry().getLocation().getLat(), result.getGeometry().getLocation().getLng());

                double distance = getDistance(user_location, place_location);

                result.setDistance(distance);
                resultList.add(result);
            }

        }


        skeletonPopular.hide();

        if (resultList.size() > 0) {
            binding.tv.setVisibility(View.VISIBLE);

            nearbyAdapter = new NearbyAdapter2(resultList, context, currency);
            mainSliderRowBinding.recViewPopular.setAdapter(nearbyAdapter);

        } else {
            binding.tv.setVisibility(View.GONE);
        }


    }

    private void calculateDistanceLoadMore(List<NearbyModel.Result> results) {

        List<NearbyModel.Result> resultListFiltered = new ArrayList<>();

        for (int i = 0; i < results.size(); i++) {
            NearbyModel.Result result = results.get(i);

            if (result != null) {


                result.setDistance(getDistance(new LatLng(user_lat, user_lng), new LatLng(result.getGeometry().getLocation().getLat(), result.getGeometry().getLocation().getLng())));
                resultListFiltered.add(result);

            }

        }


        isLoading = false;
        if (resultList.get(resultList.size() - 1) == null) {
            resultList.remove(resultList.size() - 1);
            nearbyAdapter.notifyItemRemoved(resultList.size() - 1);
        }
        int oldPos = resultList.size() - 1;

        resultList.addAll(results);
        int newPos = resultList.size();
        nearbyAdapter.notifyItemRangeChanged(oldPos, newPos);

    }

    private double getDistance(LatLng latLng1, LatLng latLng2) {
        return SphericalUtil.computeDistanceBetween(latLng1, latLng2) / 1000;
    }

    private void getCategory(MainCategoryDataRowBinding binding) {


        categoryModelList.clear();
        Api.getService(Tags.base_url)
                .getCategory()
                .enqueue(new Callback<CategoryDataModel>() {
                    @Override
                    public void onResponse(Call<CategoryDataModel> call, Response<CategoryDataModel> response) {
                        skeletonCategory.hide();
                        if (response.isSuccessful() && response.body() != null) {
                            Log.e("1","1");
                            categoryModelList.addAll(response.body().getData().getGoogle_categories());
                            categoryAdapter = new PlaceCategoryAdapter(categoryModelList, context);
                            binding.recView.setAdapter(categoryAdapter);

                        } else {
                            Log.e("2","2");

                            skeletonCategory.hide();

                            try {
                                Log.e("error_code", response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }


                    }

                    @Override
                    public void onFailure(Call<CategoryDataModel> call, Throwable t) {
                        try {
                            Log.e("3","3");

                            skeletonCategory.hide();
                            if (t.getMessage() != null) {
                                Log.e("error", t.getMessage());
                                if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                    Toast.makeText(context, context.getString(R.string.something), Toast.LENGTH_LONG).show();
                                } else if (t.getMessage().toLowerCase().contains("socket") || t.getMessage().toLowerCase().contains("canceled")) {
                                } else {
                                    Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        } catch (Exception e) {

                        }
                    }
                });


    }


}
