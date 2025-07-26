package com.example.proyectopm1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.proyectopm1.R;
import com.example.proyectopm1.viewmodels.AuthViewModel;

public class LoginActivity extends AppCompatActivity {

    private TextInputLayout tilEmail, tilPassword;
    private TextInputEditText etEmail, etPassword;
    private MaterialButton btnLogin;
    private ProgressBar progressBar;
    private TextView tvError, tvGoToRegister;
    private AuthViewModel authViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Verificar si ya está logueado
        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
        if (authViewModel.isLoggedIn(this)) {
            navigateToMain();
            return;
        }

        initViews();
        setupObservers();
        setupClickListeners();
    }

    private void initViews() {
        tilEmail = findViewById(R.id.tilEmail);
        tilPassword = findViewById(R.id.tilPassword);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        progressBar = findViewById(R.id.progressBar);
        tvError = findViewById(R.id.tvError);
        tvGoToRegister = findViewById(R.id.tvGoToRegister);
    }

    private void setupObservers() {
        // Observar el estado de carga
        authViewModel.getIsLoading().observe(this, isLoading -> {
            if (isLoading) {
                btnLogin.setText("");
                btnLogin.setEnabled(false);
                progressBar.setVisibility(View.VISIBLE);
            } else {
                btnLogin.setText("Iniciar Sesión");
                btnLogin.setEnabled(true);
                progressBar.setVisibility(View.GONE);
            }
        });

        // Observar errores
        authViewModel.getError().observe(this, error -> {
            if (error != null && !error.isEmpty()) {
                tvError.setText(error);
                tvError.setVisibility(View.VISIBLE);
                Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
            } else {
                tvError.setVisibility(View.GONE);
            }
        });

        // Observar login exitoso
        authViewModel.getUser().observe(this, user -> {
            if (user != null) {
                Toast.makeText(this, "¡Bienvenido " + user.getName() + "!", Toast.LENGTH_SHORT).show();
                navigateToMain();
            }
        });
    }

    private void setupClickListeners() {
        btnLogin.setOnClickListener(v -> attemptLogin());

        tvGoToRegister.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }

    private void attemptLogin() {
        // Limpiar errores previos
        tvError.setVisibility(View.GONE);
        tilEmail.setError(null);
        tilPassword.setError(null);

        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        // Validaciones básicas
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

        // Realizar login
        authViewModel.login(this, email, password);
    }

    private void navigateToMain() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
