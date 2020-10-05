package com.refCustomerFamily.activities_fragments.stores.google_place_modul.activity_fragments.activity_shops;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ethanhua.skeleton.Skeleton;
import com.ethanhua.skeleton.SkeletonScreen;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.SphericalUtil;
import com.refCustomerFamily.R;
import com.refCustomerFamily.activities_fragments.stores.google_place_modul.activity_fragments.activity_filter.FilterActivity;
import com.refCustomerFamily.activities_fragments.stores.google_place_modul.activity_fragments.activity_map_search.MapSearchActivity;
import com.refCustomerFamily.activities_fragments.stores.google_place_modul.activity_fragments.activity_shop_map.ShopMapActivity;
import com.refCustomerFamily.activities_fragments.stores.google_place_modul.adapters.NearbyAdapter;
import com.refCustomerFamily.activities_fragments.stores.google_place_modul.adapters.ResentSearchAdapter;
import com.refCustomerFamily.activities_fragments.stores.google_place_modul.models.FavoriteLocationModel;
import com.refCustomerFamily.activities_fragments.stores.google_place_modul.models.FilterModel;
import com.refCustomerFamily.activities_fragments.stores.google_place_modul.models.HourModel;
import com.refCustomerFamily.activities_fragments.stores.google_place_modul.models.NearbyModel;
import com.refCustomerFamily.databinding.ActivityShopsBinding;
import com.refCustomerFamily.interfaces.Listeners;
import com.refCustomerFamily.language.Language_Helper;
import com.refCustomerFamily.models.DefaultSettings;
import com.refCustomerFamily.models.UserModel;
import com.refCustomerFamily.preferences.Preferences;
import com.refCustomerFamily.remote.Api;
import com.refCustomerFamily.share.Common;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShopsActivity extends AppCompatActivity implements Listeners.BackListener {
    private ActivityShopsBinding binding;
    private List<NearbyModel.Result> resultList;
    private NearbyAdapter adapter;
    private double user_lat;
    private double user_lng;
    private SkeletonScreen skeletonScreen;
    private String lang;
    private boolean hasManyPages = false;
    private boolean isLoading = false;
    private String query = "restaurant|food|supermarket|bakery";
    private String next_page = "";
    private double rate = 5.0;
    private int distance = 60000;
    private DefaultSettings defaultSettings;
    private ResentSearchAdapter resentSearchAdapter;
    private List<String> recentSearchList;
    private Preferences preferences;
    private boolean type = false;
    private boolean closeRecentSearch = false;
    private List<NearbyModel.Result> resultListFiltered;
    private UserModel userModel;
    private Call<NearbyModel> nearbyCall;
    private Call<NearbyModel> nearbyLoadMoreCall;
    private Call<NearbyModel> searchCall;
    private Call<NearbyModel> searchLoadMoreCall;



    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language_Helper.updateResources(newBase, Paper.book().read("lang", "ar")));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_shops);
        getDataFromIntent();
        initView();
    }


    private void initView() {
        preferences = Preferences.newInstance();
        userModel = preferences.getUserData(this);
        Paper.init(this);
        lang = Paper.book().read("lang", "ar");
        binding.setLang(lang);
        binding.setCount(0);
        binding.setQuery("");
        binding.setListener(this);
        recentSearchList = new ArrayList<>();
        resultList = new ArrayList<>();
        defaultSettings = preferences.getAppSetting(this);
        binding.recView.setLayoutManager(new LinearLayoutManager(this));

        String currency = getString(R.string.sar);

        adapter = new NearbyAdapter(resultList, this, user_lat, user_lng, currency);
        binding.recView.setAdapter(adapter);


        if (defaultSettings != null) {
            recentSearchList.clear();
            recentSearchList.addAll(defaultSettings.getRecentSearchList());

        }
        resentSearchAdapter = new ResentSearchAdapter(recentSearchList, this);
        binding.recViewRecentSearch.setLayoutManager(new LinearLayoutManager(this));
        binding.recViewRecentSearch.setAdapter(resentSearchAdapter);

        skeletonScreen = Skeleton.bind(binding.recView)
                .adapter(adapter)
                .count(5)
                .frozen(false)
                .shimmer(true)
                .show();

        binding.recView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) {
                    int totalItem = adapter.getItemCount();
                    LinearLayoutManager manager = (LinearLayoutManager) binding.recView.getLayoutManager();
                    int pos = manager.findLastCompletelyVisibleItemPosition();
                    if (hasManyPages && totalItem >= 20 && (totalItem - pos == 2) && !isLoading) {
                        isLoading = true;
                        if (query.equals("restaurant|food|supermarket|bakery") && rate == 5.0 && distance == 60000) {
                            loadMore();

                        } else {
                            loadMoreSearch();
                        }
                    }
                }
            }
        });

        binding.edtSearch.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                query = binding.edtSearch.getText().toString().trim();
                if (!query.isEmpty()) {
                    addQuery(query);
                    search(query, distance, rate);
                }
            }
            return false;
        });


        binding.edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().isEmpty()) {
                    if (binding.expandLayout.isExpanded()) {
                        binding.expandLayout.collapse(true);

                    }

                    clear();

                } else {


                    Log.e("close", closeRecentSearch + "__");
                    if (!closeRecentSearch) {
                        if (recentSearchList.size() > 0) {

                            binding.expandLayout.expand(true);
                        }
                        binding.tvCancel.setVisibility(View.VISIBLE);
                    } else {
                        search(s.toString().trim(), distance, rate);
                    }


                }


            }
        });

        binding.tvCancel.setOnClickListener(v -> {
            binding.edtSearch.setText(null);
        });

        binding.imageFilter.setOnClickListener(v -> {
            Intent intent = new Intent(this, FilterActivity.class);
            startActivityForResult(intent, 100);
        });

        binding.llLocation.setOnClickListener(v -> {
            Intent intent = new Intent(this, MapSearchActivity.class);
            intent.putExtra("type", 1);
            startActivityForResult(intent, 200);
        });

        binding.tvDelete.setOnClickListener(v -> {
            clearQuery();
        });


        getShops(query);
    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        user_lat = intent.getDoubleExtra("lat", 0.0);
        user_lng = intent.getDoubleExtra("lng", 0.0);
        type = intent.getBooleanExtra("type", false);
    }

    private void clear() {
        closeRecentSearch = false;
        rate = 5.0;
        distance = 60000;
        next_page = "";
        binding.tvCancel.setVisibility(View.GONE);
        query = "restaurant|food|supermarket|bakery";
        binding.setCount(0);
        binding.setQuery("");
        getShops(query);
    }

    private void getShops(String query) {
        binding.setCount(0);
        resultList.clear();
        adapter.notifyDataSetChanged();
        binding.tvNoData.setVisibility(View.GONE);
        skeletonScreen.show();

        if (searchCall != null) {
            searchCall.cancel();
        }
        if (searchLoadMoreCall != null) {
            searchLoadMoreCall.cancel();
        }

        String loc = user_lat + "," + user_lng;
        Log.e("loc",loc);
        nearbyCall = Api.getService("https://maps.googleapis.com/maps/api/").nearbyPlaceRankBy(loc, query, "distance", lang, "", getString(R.string.map_api_key));
        nearbyCall.enqueue(new Callback<NearbyModel>() {
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
                            calculateDistance(response.body().getResults(), rate);
                            binding.tvNoData.setVisibility(View.GONE);

                        } else {
                            binding.tvNoData.setVisibility(View.VISIBLE);
                            binding.setCount(0);

                        }
                    } else {
                        binding.tvNoData.setVisibility(View.VISIBLE);
                        binding.setCount(0);

                    }

                } else {

                    skeletonScreen.hide();

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


                    binding.setCount(0);
                    skeletonScreen.hide();

                    if (t.getMessage() != null) {
                        Log.e("error", t.getMessage());
                        if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                            Toast.makeText(ShopsActivity.this, getString(R.string.something), Toast.LENGTH_LONG).show();
                        }
                        else if (t.getMessage().toLowerCase().contains("socket")||t.getMessage().toLowerCase().contains("canceled")){ }

                        else {
                            Toast.makeText(ShopsActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }

                } catch (Exception e) {

                }
            }
        });
    }

    private void loadMore() {

        resultList.add(null);
        adapter.notifyItemInserted(resultList.size() - 1);


        String loc = user_lat + "," + user_lng;

        nearbyLoadMoreCall = Api.getService("https://maps.googleapis.com/maps/api/").nearbyPlaceRankBy(loc, query, "distance", lang, next_page, getString(R.string.map_api_key));
        nearbyLoadMoreCall.enqueue(new Callback<NearbyModel>() {
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

                            calculateDistanceLoadMore(response.body().getResults(), rate);
                        }
                    }

                } else {
                    isLoading = false;


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
                    isLoading = false;
                    if (resultList.get(resultList.size() - 1) == null) {
                        resultList.remove(resultList.size() - 1);
                        adapter.notifyItemRemoved(resultList.size() - 1);
                    }

                    if (t.getMessage() != null) {
                        Log.e("error", t.getMessage());
                        if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                            Toast.makeText(ShopsActivity.this, getString(R.string.something), Toast.LENGTH_LONG).show();
                        }
                        else if (t.getMessage().toLowerCase().contains("socket")||t.getMessage().toLowerCase().contains("canceled")){ }

                        else {
                            Toast.makeText(ShopsActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }

                } catch (Exception e) {

                }
            }
        });
    }

    private void search(String query, int distance, double rate) {

        Common.CloseKeyBoard(this, binding.edtSearch);
        binding.setCount(0);
        binding.expandLayout.collapse(true);
        binding.tvNoData.setVisibility(View.GONE);
        resultList.clear();
        adapter.notifyDataSetChanged();
        skeletonScreen.show();
        closeRecentSearch = false;
        if (query.equals("restaurant|food|supermarket|bakery")) {
            binding.setQuery("");

        } else {

            binding.setQuery(query);

        }
        if (nearbyCall != null) {
            nearbyCall.cancel();
        }
        if (nearbyLoadMoreCall != null) {
            nearbyLoadMoreCall.cancel();
        }


        String loc = user_lat + "," + user_lng;
        searchCall = Api.getService("https://maps.googleapis.com/maps/api/").nearbyPlaceInDistance(loc, query, distance, lang, "", getString(R.string.map_api_key));
        searchCall.enqueue(new Callback<NearbyModel>() {
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


                            calculateDistance(response.body().getResults(), rate);

                        } else {
                            binding.tvNoData.setVisibility(View.VISIBLE);
                            binding.setCount(0);

                        }
                    } else {
                        binding.setCount(0);

                        binding.tvNoData.setVisibility(View.VISIBLE);

                    }

                } else {

                    skeletonScreen.hide();

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
                    binding.setCount(0);
                    skeletonScreen.hide();

                    if (t.getMessage() != null) {
                        Log.e("error", t.getMessage());
                        if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                            Toast.makeText(ShopsActivity.this, getString(R.string.something), Toast.LENGTH_LONG).show();
                        }
                        else if (t.getMessage().toLowerCase().contains("socket")||t.getMessage().toLowerCase().contains("canceled")){ }
                        else {
                            Toast.makeText(ShopsActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }


                } catch (Exception e) {

                }
            }
        });
    }

    private void loadMoreSearch() {

        resultList.add(null);
        adapter.notifyItemInserted(resultList.size() - 1);
        String loc = user_lat + "," + user_lng;
        searchLoadMoreCall = Api.getService("https://maps.googleapis.com/maps/api/").nearbyPlaceInDistance(loc, query, distance, lang, next_page, getString(R.string.map_api_key));
        searchLoadMoreCall.enqueue(new Callback<NearbyModel>() {
            @Override
            public void onResponse(Call<NearbyModel> call, Response<NearbyModel> response) {
                skeletonScreen.hide();
                resultList.remove(resultList.size() - 1);
                adapter.notifyItemRemoved(resultList.size() - 1);
                isLoading = false;

                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().getStatus().equals("OK")) {

                        if (response.body().getNext_page_token() != null) {
                            hasManyPages = true;
                            next_page = response.body().getNext_page_token();
                            Log.e("mmm", "mmm");

                        } else {
                            hasManyPages = false;
                            next_page = "";
                        }

                        if (response.body().getResults().size() > 0) {


                            calculateDistanceLoadMore(response.body().getResults(), rate);
                            binding.tvNoData.setVisibility(View.GONE);

                        } else {
                            binding.tvNoData.setVisibility(View.VISIBLE);

                        }
                    } else {
                        binding.tvNoData.setVisibility(View.VISIBLE);

                    }

                } else {

                    if (resultList.get(resultList.size() - 1) == null) {
                        resultList.remove(resultList.size() - 1);
                        adapter.notifyItemRemoved(resultList.size() - 1);
                    }
                    skeletonScreen.hide();

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
                    isLoading = false;
                    if (resultList.get(resultList.size() - 1) == null) {
                        resultList.remove(resultList.size() - 1);
                        adapter.notifyItemRemoved(resultList.size() - 1);
                    }
                    skeletonScreen.hide();

                    if (t.getMessage() != null) {
                        Log.e("error", t.getMessage());
                        if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                            Toast.makeText(ShopsActivity.this, getString(R.string.something), Toast.LENGTH_LONG).show();
                        }
                        else if (t.getMessage().toLowerCase().contains("socket")||t.getMessage().toLowerCase().contains("canceled")){ }
                        else {
                            Toast.makeText(ShopsActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }

                } catch (Exception e) {

                }
            }
        });
    }


    private void calculateDistance(List<NearbyModel.Result> results, double rate) {
        resultListFiltered = new ArrayList<>();

        for (int i = 0; i < results.size(); i++) {
            NearbyModel.Result result = results.get(i);

            if (result != null) {


                if (result.getRating() <= rate) {
                    result.setDistance(getDistance(new LatLng(user_lat, user_lng), new LatLng(result.getGeometry().getLocation().getLat(), result.getGeometry().getLocation().getLng())) );
                    resultListFiltered.add(result);
                }
            }

        }

        binding.setCount(resultListFiltered.size());

        if (resultListFiltered.size() > 0) {

            skeletonScreen.hide();
            resultList.clear();
            resultList.addAll(resultListFiltered);
            adapter.notifyDataSetChanged();

        } else {
            binding.tvNoData.setVisibility(View.VISIBLE);

        }
    }

    private void calculateDistanceLoadMore(List<NearbyModel.Result> results, double rate) {

        List<NearbyModel.Result> resultListFiltered = new ArrayList<>();

        for (int i = 0; i < results.size(); i++) {
            NearbyModel.Result result = results.get(i);

            if (result != null) {


                if (result.getRating() <= rate) {
                    result.setDistance(getDistance(new LatLng(user_lat, user_lng), new LatLng(result.getGeometry().getLocation().getLat(), result.getGeometry().getLocation().getLng())));
                    resultListFiltered.add(result);
                }
            }

        }


        if (resultList.get(resultList.size() - 1) == null) {
            resultList.remove(resultList.size() - 1);
            adapter.notifyItemRemoved(resultList.size() - 1);
        }


        isLoading = false;
        int oldPos = resultList.size();
        resultList.addAll(results);

        int newPos = resultList.size();
        binding.setCount(newPos);
        adapter.notifyItemRangeChanged(oldPos, newPos);


    }


    private double getDistance(LatLng latLng1, LatLng latLng2) {
        return SphericalUtil.computeDistanceBetween(latLng1, latLng2) / 1000;
    }



    public void setShopData(NearbyModel.Result placeModel) {
        if (type) {
            //from main fragment
            Intent intent = new Intent(this, ShopMapActivity.class);
            intent.putExtra("data",placeModel);
            startActivity(intent);




        } else {
            Intent intent = getIntent();
            intent.putExtra("data", placeModel);
            setResult(RESULT_OK, intent);
            finish();
        }

    }

    private List<HourModel> getHours(NearbyModel.Result placeModel)
    {
        List<HourModel> list = new ArrayList<>();

        if (placeModel!=null&&placeModel.getWork_hours()!=null&&placeModel.getWork_hours().getWeekday_text()!=null&&placeModel.getWork_hours().getWeekday_text().size()>0){
            for (String time: placeModel.getWork_hours().getWeekday_text()){

                String day = time.split(":", 2)[0].trim();
                String t = time.split(":",2)[1].trim();
                HourModel hourModel = new HourModel(day,t);
                list.add(hourModel);




            }
        }


        return list;
    }

    public void setRecentSearchItem(String query) {
        binding.expandLayout.collapse(true);
        closeRecentSearch = true;
        binding.edtSearch.setText(query);
    }

    private void addQuery(String query) {
        if (defaultSettings == null) {
            defaultSettings = new DefaultSettings();
        }


        if (recentSearchList.size() > 0) {
            for (String q : recentSearchList) {
                if (!q.equals(query)) {
                    recentSearchList.add(query);
                    defaultSettings.setRecentSearchList(recentSearchList);
                    preferences.createUpdateAppSetting(this, defaultSettings);
                    resentSearchAdapter.notifyItemInserted(this.recentSearchList.size() - 1);
                }
            }
        } else {
            recentSearchList.add(query);
            defaultSettings.setRecentSearchList(recentSearchList);
            preferences.createUpdateAppSetting(this, defaultSettings);
            resentSearchAdapter.notifyItemInserted(this.recentSearchList.size() - 1);
        }


    }

    private void clearQuery() {
        if (defaultSettings == null) {
            defaultSettings = new DefaultSettings();
        }
        recentSearchList.clear();
        defaultSettings.setRecentSearchList(recentSearchList);
        resentSearchAdapter.notifyDataSetChanged();
        binding.expandLayout.collapse(true);


    }

    private boolean isRestaurant(NearbyModel.Result result) {

        for (String type : result.getTypes()) {
            if (type.equals("restaurant")) {
                return true;
            }
        }

        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {
            FilterModel filterModel = (FilterModel) data.getSerializableExtra("data");
            rate = filterModel.getRate();
            distance = filterModel.getDistance() * 1000;
            next_page = "";
            isLoading = false;
            hasManyPages = false;
            closeRecentSearch = true;
            if (!filterModel.getKeyword().isEmpty()) {
                query = filterModel.getKeyword();
                binding.tvRecentSearch.setText(query);
            } else {
                query = "restaurant|food|supermarket|bakery";
                binding.tvRecentSearch.setText(null);

            }

            search(query, distance, rate);

        } else if (requestCode == 200 && resultCode == RESULT_OK && data != null) {

            FavoriteLocationModel model = (FavoriteLocationModel) data.getSerializableExtra("data");
            user_lat = model.getLat();
            user_lng = model.getLng();
            binding.tvLocation.setText(model.getAddress());
            closeRecentSearch = true;

            if (binding.edtSearch.getText().toString().trim().isEmpty()) {
                query = "restaurant|food|supermarket|bakery";

            } else {
                query = binding.edtSearch.getText().toString().trim();

            }
            next_page = "";
            hasManyPages = false;
            /*rate = 5.0;
            distance = 60000;*/
            binding.setCount(0);
            getShops(query);
        }
    }

    @Override
    public void back() {
        super.onBackPressed();
    }


}