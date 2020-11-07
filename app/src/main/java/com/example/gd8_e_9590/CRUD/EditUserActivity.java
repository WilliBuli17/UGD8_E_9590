package com.example.gd8_e_9590.CRUD;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.gd8_e_9590.API.ApiClient;
import com.example.gd8_e_9590.API.ApiInterface;
import com.example.gd8_e_9590.MODEL.UserResponse;
import com.example.gd8_e_9590.R;
import com.google.android.material.button.MaterialButton;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditUserActivity extends AppCompatActivity {
    private ImageButton ibBack;
    private EditText etNama, etNim, etPassword;
    private AutoCompleteTextView exposedDropdownFakultas, exposedDropdownProdi;
    private RadioGroup rgJenisKelamin;
    private RadioButton rbMale, rbFemale;
    private MaterialButton btnCancle, btnUpdate;
    private String sIdUser, sNama, sNim, sProdi = "", sFakultas = "", sJenisKelamin;
    private String[] saProdi = new String[] {"Informatika", "Manajemen", "Ilmu Komunikasi", "Ilmu Hukum"};
    private String[] saFakultas = new String[] {"FTI", "FBE" , "FISIP", "FH"};
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user);

        sIdUser = getIntent().getStringExtra("id");
        loadUserById(sIdUser);

        progressDialog = new ProgressDialog(this);

        ibBack = findViewById(R.id.ibBack);
        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        etNama = findViewById(R.id.etNama);
        etNim = findViewById(R.id.etNim);
        exposedDropdownProdi = findViewById(R.id.edProdi);
        exposedDropdownFakultas = findViewById(R.id.edFakultas);
        rgJenisKelamin = findViewById(R.id.rgJenisKelamin);
        rbMale = findViewById(R.id.rbLakiLaki);
        rbFemale = findViewById(R.id.rbPerempuan);
        etPassword = findViewById(R.id.etPassord);
        btnCancle = findViewById(R.id.btnCancel);
        btnUpdate = findViewById(R.id.btnUpdate);

        ArrayAdapter<String> adapterProdi = new ArrayAdapter<>(Objects.requireNonNull(this),
                R.layout.list_item, R.id.item_list, saProdi);
        exposedDropdownProdi.setAdapter(adapterProdi);
        exposedDropdownProdi.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                sProdi = saProdi[position];
            }
        });

        ArrayAdapter<String> adapterFakultas = new ArrayAdapter<>(Objects.requireNonNull(this),
                R.layout.list_item, R.id.item_list, saFakultas);
        exposedDropdownFakultas.setAdapter(adapterFakultas);
        exposedDropdownFakultas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                sFakultas = saFakultas[position];
            }
        });

        btnCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        rgJenisKelamin.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.rbLakiLaki:
                        sJenisKelamin = "Laki-Laki";
                        break;
                    case R.id.rbPerempuan:
                        sJenisKelamin = "Perempuan";
                        break;
                }
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etNama.getText().toString().isEmpty()){
                    etNama.setError("Isikan Dengan Benar");
                    etNama.requestFocus();
                }
                else if(etNim.getText().toString().isEmpty()){
                    etNim.setError("Isikan Dengan Benar");
                    etNim.requestFocus();
                }
                else if(sProdi.isEmpty()){
                    exposedDropdownProdi.setError("Isikan Dengan Benar", null);
                    exposedDropdownProdi.requestFocus();
                }
                else if(sFakultas.isEmpty()){
                    exposedDropdownFakultas.setError("Isikan Dengan Benar", null);
                    exposedDropdownFakultas.requestFocus();
                }
                else if(etPassword.getText().toString().isEmpty()){
                    etPassword.setError("Isikan Dengan Benar");
                    etPassword.requestFocus();
                }
                else{
                    progressDialog.show();
                    updateUser(sIdUser);
                }
            }
        });
    }

    private void loadUserById(String id) {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<UserResponse> add = apiService.getUserById(id,"data");

        add.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                sNama = response.body().getUsers().get(0).getNama();
                sNim = response.body().getUsers().get(0).getNim();
                sFakultas = response.body().getUsers().get(0).getFakultas();
                sProdi = response.body().getUsers().get(0).getProdi();
                sJenisKelamin = response.body().getUsers().get(0).getJenis_kelamin();

                etNama.setText(sNama);
                etNim.setText(sNim);
                exposedDropdownFakultas.setText(sFakultas);
                exposedDropdownProdi.setText(sProdi);
                if(sJenisKelamin.equalsIgnoreCase("Perempuan")) {
                    rbFemale.setChecked(true);
                    rbMale.setChecked(false);
                } else {
                    rbFemale.setChecked(false);
                    rbMale.setChecked(true);
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                Toast.makeText(EditUserActivity.this, "Kesalahan Jaringan", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }

    private void updateUser(String id) {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<UserResponse> add = apiService.updateUser(id, "data", etNama.getText().toString(),
                etNim.getText().toString(), sProdi, sFakultas, sJenisKelamin, etPassword.getText().toString());

        add.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                Toast.makeText(EditUserActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(EditUserActivity.this, ShowListUserActivity.class);
                startActivity(intent);
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                Toast.makeText(EditUserActivity.this, "Kesalahan Jaringan", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }
}