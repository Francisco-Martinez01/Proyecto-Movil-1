package com.example.proyectopm1.viewmodels;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.proyectopm1.models.Address;
import com.example.proyectopm1.network.ApiClient;
import com.example.proyectopm1.network.ApiService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddressViewModel extends ViewModel {
    private MutableLiveData<List<Address>> addresses = new MutableLiveData<>();
    private MutableLiveData<Address> address = new MutableLiveData<>();
    private MutableLiveData<String> error = new MutableLiveData<>();

    public LiveData<List<Address>> getAddresses() { return addresses; }
    public LiveData<Address> getAddress() { return address; }
    public LiveData<String> getError() { return error; }

    public void fetchAddresses(Context context) {
        ApiService api = ApiClient.getClient(context).create(ApiService.class);
        api.getAddresses().enqueue(new Callback<List<Address>>() {
            @Override
            public void onResponse(Call<List<Address>> call, Response<List<Address>> response) {
                if (response.isSuccessful()) {
                    addresses.postValue(response.body());
                } else {
                    error.postValue("Error al obtener direcciones");
                }
            }
            @Override
            public void onFailure(Call<List<Address>> call, Throwable t) {
                error.postValue(t.getMessage());
            }
        });
    }

    public void addAddress(Context context, Address newAddress) {
        ApiService api = ApiClient.getClient(context).create(ApiService.class);
        api.addAddress(newAddress).enqueue(new Callback<Address>() {
            @Override
            public void onResponse(Call<Address> call, Response<Address> response) {
                if (response.isSuccessful()) {
                    address.postValue(response.body());
                } else {
                    error.postValue("Error al agregar dirección");
                }
            }
            @Override
            public void onFailure(Call<Address> call, Throwable t) {
                error.postValue(t.getMessage());
            }
        });
    }
    // Métodos para actualizar y eliminar direcciones pueden agregarse igual
}
