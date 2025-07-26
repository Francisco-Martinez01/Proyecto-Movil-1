package com.example.proyectopm1.viewmodels;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.proyectopm1.models.Order;
import com.example.proyectopm1.network.ApiClient;
import com.example.proyectopm1.network.ApiService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderViewModel extends ViewModel {
    private MutableLiveData<List<Order>> myOrders = new MutableLiveData<>();

    public void fetchMyOrders(Context context) {
        ApiService api = ApiClient.getClient(context).create(ApiService.class);
        api.getMyOrders().enqueue(new Callback<List<Order>>() {
            @Override
            public void onResponse(Call<List<Order>> call, Response<List<Order>> response) {
                if (response.isSuccessful()) {
                    myOrders.postValue(response.body());
                }
            }
            @Override
            public void onFailure(Call<List<Order>> call, Throwable t) { }
        });
    }
    // Métodos para crear, cancelar, actualizar estado, etc.
}
