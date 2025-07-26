package com.example.proyectopm1.viewmodels;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.proyectopm1.models.Restaurant;
import com.example.proyectopm1.network.ApiClient;
import com.example.proyectopm1.network.ApiService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RestaurantViewModel extends ViewModel {
    private MutableLiveData<List<Restaurant>> restaurants = new MutableLiveData<>();

    public void fetchRestaurants(Context context) {
        ApiService api = ApiClient.getClient(context).create(ApiService.class);
        api.getRestaurants().enqueue(new Callback<List<Restaurant>>() {
            @Override
            public void onResponse(Call<List<Restaurant>> call, Response<List<Restaurant>> response) {
                if (response.isSuccessful()) {
                    restaurants.postValue(response.body());
                }
            }
            @Override
            public void onFailure(Call<List<Restaurant>> call, Throwable t) { }
        });
    }
    // Métodos similares para productos
}
