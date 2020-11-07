package com.example.gd8_e_9590.LoginLogout;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.gd8_e_9590.API.ApiClient;
import com.example.gd8_e_9590.API.ApiInterface;
import com.example.gd8_e_9590.MODEL.UserDAO;
import com.example.gd8_e_9590.MODEL.UserResponse;
import com.example.gd8_e_9590.MainActivity;
import com.example.gd8_e_9590.R;
import com.google.android.material.button.MaterialButton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    private EditText etNim, etPassword;
    private MaterialButton btnLogin;
    private SharedPreferences preferences;
    private ProgressDialog progressDialog;
    private  Intent intent = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        preferences = getSharedPreferences("Login", MODE_PRIVATE);
        String sIdUser = preferences.getString("id", "");

        if(sIdUser.isEmpty() || sIdUser.equals("0")) {
            progressDialog = new ProgressDialog(this);

            btnLogin = findViewById(R.id.btnLogin);
            etNim = findViewById(R.id.etNim);
            etPassword = findViewById(R.id.etPassord);

            btnLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(etNim.getText().toString().isEmpty()) {
                        etNim.setError("Isikan dengan benar");
                        etNim.requestFocus();
                    }
                    else if(etPassword.getText().toString().isEmpty()) {
                        etPassword.setError("Isikan dengan benar");
                        etPassword.requestFocus();
                    }
                    else {
                        progressDialog.show();
                        loginUser();
                    }
                }
            });
        }
        else {
            intent = new Intent(LoginActivity.this, LogoutAndProfilActivity.class);
            intent.putExtra("id", sIdUser);
            finish();
            startActivity(intent);
        }
    }

    private void loginUser() {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<UserResponse> loginRequest = apiService.login(etNim.getText().toString(),
                etPassword.getText().toString());

        loginRequest.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                if(response.body().getMessage().equals("Berhasil login")) {
                    Toast.makeText(LoginActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                    UserDAO user = response.body().getUsers().get(0);
                    if(user.getNim().equalsIgnoreCase("admin")) {
                        intent = new Intent(LoginActivity.this, MainActivity.class);
                    }
                    else {
                        intent = new Intent(LoginActivity.this, LogoutAndProfilActivity.class);
                        intent.putExtra("id", user.getId());
                        finish();
                    }
                    startActivity(intent);
                    SharedPreferences.Editor editor = preferences.edit();

                    editor.putString("id", user.getId());
                    editor.apply();
                }
                else {
                    Toast.makeText(LoginActivity.this, "Username atau Password Salah", Toast.LENGTH_SHORT).show();
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "Kesalahan Jaringan", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }
}