package com.example.gd8_e_9590.LoginLogout;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gd8_e_9590.API.ApiClient;
import com.example.gd8_e_9590.API.ApiInterface;
import com.example.gd8_e_9590.CRUD.EditUserActivity;
import com.example.gd8_e_9590.MODEL.UserResponse;
import com.example.gd8_e_9590.R;
import com.google.android.material.button.MaterialButton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LogoutAndProfilActivity extends AppCompatActivity {

    private TextView twNama, twNim, twFakultas, twProdi, twJenisKelamin;
    private String sIdUser, sNama, sNim, sFakultas, sProdi, sJenisKelamin;
    private MaterialButton btnLogout, btnEdit;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logout_and_profil);

        sIdUser = getIntent().getStringExtra("id");
        loadUserById(sIdUser);

        progressDialog = new ProgressDialog(this);
        progressDialog.show();

        twNama = findViewById(R.id.twNama);
        twNim = findViewById(R.id.twNim);
        twFakultas = findViewById(R.id.twFakultas);
        twProdi = findViewById(R.id.twProdi);
        twJenisKelamin = findViewById(R.id.twJenisKelamin);
        btnEdit = findViewById(R.id.btnEdit);
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LogoutAndProfilActivity.this, EditUserActivity.class);
                intent.putExtra("id", sIdUser);
                startActivityForResult(intent, 0);
            }
        });

        btnLogout = findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences preferences = getSharedPreferences("Login", MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();

                editor.putString("id", "");
                editor.apply();

                Intent intent = new Intent(LogoutAndProfilActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                finish();
                startActivity(getIntent());
            }
        }
    }

    private void loadUserById(String id) {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<UserResponse> add = apiService.getUserById(id, "data");

        add.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                sNama = response.body().getUsers().get(0).getNama();
                sNim = response.body().getUsers().get(0).getNim();
                sFakultas = response.body().getUsers().get(0).getFakultas();
                sProdi = response.body().getUsers().get(0).getProdi();
                sJenisKelamin = response.body().getUsers().get(0).getJenis_kelamin();

                twNama.setText(sNama);
                twNim.setText(sNim);
                twFakultas.setText(sFakultas);
                twProdi.setText(sProdi);
                twJenisKelamin.setText(sJenisKelamin);
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                Toast.makeText(LogoutAndProfilActivity.this, "Kesalahan Jaringan", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }
}