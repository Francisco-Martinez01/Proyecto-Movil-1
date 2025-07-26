package com.example.proyectopm1.viewmodels;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.proyectopm1.models.Review;
import com.example.proyectopm1.network.ApiClient;
import com.example.proyectopm1.network.ApiService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReviewViewModel extends ViewModel {
    private MutableLiveData<List<Review>> reviews = new MutableLiveData<>();
    private MutableLiveData<Review> review = new MutableLiveData<>();
    private MutableLiveData<String> error = new MutableLiveData<>();

    public LiveData<List<Review>> getReviews() { return reviews; }
    public LiveData<Review> getReview() { return review; }
    public LiveData<String> getError() { return error; }

    public void fetchReviewsByRestaurant(Context context, String restaurantId) {
        ApiService api = ApiClient.getClient(context).create(ApiService.class);
        api.getReviewsByRestaurant(restaurantId).enqueue(new Callback<List<Review>>() {
            @Override
            public void onResponse(Call<List<Review>> call, Response<List<Review>> response) {
                if (response.isSuccessful()) {
                    reviews.postValue(response.body());
                } else {
                    error.postValue("Error al obtener reseñas");
                }
            }
            @Override
            public void onFailure(Call<List<Review>> call, Throwable t) {
                error.postValue(t.getMessage());
            }
        });
    }

    public void createReview(Context context, Review newReview) {
        ApiService api = ApiClient.getClient(context).create(ApiService.class);
        api.createReview(newReview).enqueue(new Callback<Review>() {
            @Override
            public void onResponse(Call<Review> call, Response<Review> response) {
                if (response.isSuccessful()) {
                    review.postValue(response.body());
                } else {
                    error.postValue("Error al crear reseña");
                }
            }
            @Override
            public void onFailure(Call<Review> call, Throwable t) {
                error.postValue(t.getMessage());
            }
        });
    }
    // Métodos para actualizar y eliminar reseñas pueden agregarse igual
}
