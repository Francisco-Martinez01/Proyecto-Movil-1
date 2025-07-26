package com.example.proyectopm1.viewmodels;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.proyectopm1.models.AuthResponse;
import com.example.proyectopm1.models.User;
import com.example.proyectopm1.network.ApiClient;
import com.example.proyectopm1.network.ApiService;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthViewModel extends ViewModel {
    private MutableLiveData<User> user = new MutableLiveData<>();
    private MutableLiveData<String> error = new MutableLiveData<>();
    private MutableLiveData<Boolean> isLoading = new MutableLiveData<>();

    public LiveData<User> getUser() { return user; }
    public LiveData<String> getError() { return error; }
    public LiveData<Boolean> getIsLoading() { return isLoading; }

    public void login(Context context, String email, String password) {
        isLoading.postValue(true);

        ApiService api = ApiClient.getClient(context).create(ApiService.class);
        Map<String, String> body = new HashMap<>();
        body.put("email", email);
        body.put("password", password);

        api.login(body).enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                isLoading.postValue(false);

                if (response.isSuccessful() && response.body() != null) {
                    // ✅ AQUÍ VA LA LÍNEA PARA GUARDAR EL TOKEN
                    SharedPreferences prefs = context.getSharedPreferences("prefs", Context.MODE_PRIVATE);
                    prefs.edit().putString("jwt_token", response.body().getToken()).apply();

                    // Notificar a la UI que el login fue exitoso
                    user.postValue(response.body().getUser());
                } else {
                    error.postValue("Credenciales incorrectas");
                }
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                isLoading.postValue(false);
                error.postValue("Error de conexión: " + t.getMessage());
            }
        });
    }

    public void register(Context context, String name, String email, String password) {
        isLoading.postValue(true);

        ApiService api = ApiClient.getClient(context).create(ApiService.class);
        User newUser = new User();
        newUser.setName(name);
        newUser.setEmail(email);
        newUser.setPassword(password); // Asegúrate de tener este campo en tu modelo User

        api.register(newUser).enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                isLoading.postValue(false);

                if (response.isSuccessful() && response.body() != null) {
                    // ✅ AQUÍ TAMBIÉN VA LA LÍNEA PARA GUARDAR EL TOKEN
                    SharedPreferences prefs = context.getSharedPreferences("prefs", Context.MODE_PRIVATE);
                    prefs.edit().putString("jwt_token", response.body().getToken()).apply();

                    // Notificar a la UI que el registro fue exitoso
                    user.postValue(response.body().getUser());
                } else {
                    error.postValue("Error en el registro");
                }
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                isLoading.postValue(false);
                error.postValue("Error de conexión: " + t.getMessage());
            }
        });
    }

    // Método para cerrar sesión
    public void logout(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("prefs", Context.MODE_PRIVATE);
        prefs.edit().remove("jwt_token").apply();
        user.postValue(null);
    }

    // Método para verificar si hay una sesión activa
    public boolean isLoggedIn(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("prefs", Context.MODE_PRIVATE);
        String token = prefs.getString("jwt_token", null);
        return token != null && !token.isEmpty();
    }
}
