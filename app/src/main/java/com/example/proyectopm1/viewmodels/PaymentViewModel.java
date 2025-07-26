package com.example.proyectopm1.viewmodels;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.proyectopm1.models.PaymentIntentRequest;
import com.example.proyectopm1.models.PaymentIntentResponse;
import com.example.proyectopm1.network.ApiClient;
import com.example.proyectopm1.network.ApiService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaymentViewModel extends ViewModel {
    private MutableLiveData<PaymentIntentResponse> paymentIntent = new MutableLiveData<>();

    public void createPaymentIntent(Context context, double amount) {
        ApiService api = ApiClient.getClient(context).create(ApiService.class);
        PaymentIntentRequest req = new PaymentIntentRequest();
        req.setAmount(amount);
        api.createPaymentIntent(req).enqueue(new Callback<PaymentIntentResponse>() {
            @Override
            public void onResponse(Call<PaymentIntentResponse> call, Response<PaymentIntentResponse> response) {
                if (response.isSuccessful()) {
                    paymentIntent.postValue(response.body());
                }
            }
            @Override
            public void onFailure(Call<PaymentIntentResponse> call, Throwable t) { }
        });
    }
}
