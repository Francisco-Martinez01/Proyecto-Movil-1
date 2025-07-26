package com.example.proyectopm1.viewmodels;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.proyectopm1.models.Favorite;
import com.example.proyectopm1.network.ApiClient;
import com.example.proyectopm1.network.ApiService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FavoriteViewModel extends ViewModel {
    private MutableLiveData<List<Favorite>> favorites = new MutableLiveData<>();
    private MutableLiveData<Favorite> favorite = new MutableLiveData<>();
    private MutableLiveData<String> error = new MutableLiveData<>();

    public LiveData<List<Favorite>> getFavorites() { return favorites; }
    public LiveData<Favorite> getFavorite() { return favorite; }
    public LiveData<String> getError() { return error; }

    public void fetchFavorites(Context context) {
        ApiService api = ApiClient.getClient(context).create(ApiService.class);
        api.getFavorites().enqueue(new Callback<List<Favorite>>() {
            @Override
            public void onResponse(Call<List<Favorite>> call, Response<List<Favorite>> response) {
                if (response.isSuccessful()) {
                    favorites.postValue(response.body());
                } else {
                    error.postValue("Error al obtener favoritos");
                }
            }
            @Override
            public void onFailure(Call<List<Favorite>> call, Throwable t) {
                error.postValue(t.getMessage());
            }
        });
    }

    public void addFavorite(Context context, Favorite newFavorite) {
        ApiService api = ApiClient.getClient(context).create(ApiService.class);
        api.addFavorite(newFavorite).enqueue(new Callback<Favorite>() {
            @Override
            public void onResponse(Call<Favorite> call, Response<Favorite> response) {
                if (response.isSuccessful()) {
                    favorite.postValue(response.body());
                } else {
                    error.postValue("Error al agregar favorito");
                }
            }
            @Override
            public void onFailure(Call<Favorite> call, Throwable t) {
                error.postValue(t.getMessage());
            }
        });
    }
    // Métodos para eliminar favoritos pueden agregarse igual
}
