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

public class DeliveryViewModel extends ViewModel {
    private MutableLiveData<List<Order>> availableOrders = new MutableLiveData<>();

    public void fetchAvailableOrders(Context context) {
        ApiService api = ApiClient.getClient(context).create(ApiService.class);
        api.getAvailableOrders().enqueue(new Callback<List<Order>>() {
            @Override
            public void onResponse(Call<List<Order>> call, Response<List<Order>> response) {
                if (response.isSuccessful()) {
                    availableOrders.postValue(response.body());
                }
            }
            @Override
            public void onFailure(Call<List<Order>> call, Throwable t) { }
        });
    }
    // Métodos para aceptar, entregar, actualizar ubicación, etc.
}
