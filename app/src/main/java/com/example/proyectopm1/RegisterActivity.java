package com.example.proyectopm1;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import com.example.proyectopm1.models.AuthResponse;
import com.example.proyectopm1.models.User;
import com.example.proyectopm1.network.ApiClient;
import com.example.proyectopm1.network.ApiService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    private TextInputLayout tilName, tilEmail, tilPassword, tilConfirmPassword;
    private TextInputEditText etName, etEmail, etPassword, etConfirmPassword;
    private MaterialButton btnRegister;
    private ProgressBar progressBar;
    private TextView tvError, tvGoToLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initViews();
        setupClickListeners();
    }

    private void initViews() {
        tilName = findViewById(R.id.tilName);
        tilEmail = findViewById(R.id.tilEmail);
        tilPassword = findViewById(R.id.tilPassword);
        tilConfirmPassword = findViewById(R.id.tilConfirmPassword);

        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);

        btnRegister = findViewById(R.id.btnRegister);
        progressBar = findViewById(R.id.progressBar);
        tvError = findViewById(R.id.tvError);
        tvGoToLogin = findViewById(R.id.tvGoToLogin);
    }

    private void setupClickListeners() {
        btnRegister.setOnClickListener(v -> attemptRegister());

        tvGoToLogin.setOnClickListener(v -> {
            finish(); // Vuelve al LoginActivity
        });
    }

    private void attemptRegister() {
        // Limpiar errores previos
        tvError.setVisibility(View.GONE);
        tilName.setError(null);
        tilEmail.setError(null);
        tilPassword.setError(null);
        tilConfirmPassword.setError(null);

        String name = etName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String confirmPassword = etConfirmPassword.getText().toString().trim();

        // Validaciones básicas
        if (name.isEmpty()) {
            tilName.setError("El nombre es requerido");
            etName.requestFocus();
            return;
        }

        if (name.length() < 2) {
            tilName.setError("El nombre debe tener al menos 2 caracteres");
            etName.requestFocus();
            return;
        }

        if (email.isEmpty()) {
            tilEmail.setError("El email es requerido");
            etEmail.requestFocus();
            return;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            tilEmail.setError("Ingresa un email válido");
            etEmail.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            tilPassword.setError("La contraseña es requerida");
            etPassword.requestFocus();
            return;
        }

        if (password.length() < 6) {
            tilPassword.setError("La contraseña debe tener al menos 6 caracteres");
            etPassword.requestFocus();
            return;
        }

        if (confirmPassword.isEmpty()) {
            tilConfirmPassword.setError("Confirma tu contraseña");
            etConfirmPassword.requestFocus();
            return;
        }

        if (!password.equals(confirmPassword)) {
            tilConfirmPassword.setError("Las contraseñas no coinciden");
            etConfirmPassword.requestFocus();
            return;
        }

        // Realizar registro
        registerUser(name, email, password);
    }

    private void registerUser(String name, String email, String password) {
        // Mostrar loading
        progressBar.setVisibility(View.VISIBLE);
        btnRegister.setEnabled(false);
        btnRegister.setText("Registrando...");

        // Crear objeto User
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(password);

        ApiService apiService = ApiClient.getClient(this).create(ApiService.class);
        Call<AuthResponse> call = apiService.register(user);

        call.enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                progressBar.setVisibility(View.GONE);
                btnRegister.setEnabled(true);
                btnRegister.setText("Registrarse");

                if (response.isSuccessful() && response.body() != null) {
                    AuthResponse authResponse = response.body();

                    // Guardar token en SharedPreferences
                    String token = authResponse.getToken();
                    SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
                    prefs.edit().putString("jwt_token", token).apply();

                    Toast.makeText(RegisterActivity.this,
                            "¡Registro exitoso! Bienvenido " + authResponse.getUser().getName(),
                            Toast.LENGTH_LONG).show();

                    // Opción 1: Ir directo al MainActivity
                    navigateToMain();

                    // Opción 2: Regresar al Login (descomenta esta línea y comenta la anterior)
                    // finish();

                } else {
                    // Manejar errores del servidor
                    String errorMessage = "Error al registrar usuario";

                    if (response.code() == 400) {
                        errorMessage = "Datos inválidos. Verifica la información.";
                    } else if (response.code() == 409) {
                        errorMessage = "Este email ya está registrado.";
                    } else if (response.code() == 500) {
                        errorMessage = "Error del servidor. Intenta más tarde.";
                    }

                    tvError.setText(errorMessage);
                    tvError.setVisibility(View.VISIBLE);
                    Toast.makeText(RegisterActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                btnRegister.setEnabled(true);
                btnRegister.setText("Registrarse");

                String errorMessage = "Error de conexión: " + t.getMessage();
                tvError.setText(errorMessage);
                tvError.setVisibility(View.VISIBLE);
                Toast.makeText(RegisterActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void navigateToMain() {
        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}