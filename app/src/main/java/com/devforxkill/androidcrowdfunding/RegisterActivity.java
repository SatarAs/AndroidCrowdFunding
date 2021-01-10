package com.devforxkill.androidcrowdfunding;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.devforxkill.androidcrowdfunding.Models.User;
import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import org.apache.commons.lang3.StringUtils;

import java.io.IOException;

import com.devforxkill.androidcrowdfunding.Config.ApiEndPoints;
import com.devforxkill.androidcrowdfunding.Models.APIResponse;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Activity pour l'inscription d'un nouvel utilisateur
 */
public class RegisterActivity extends AppCompatActivity {
    EditText etEmail;
    EditText etPseudo;
    EditText etPassword;
    EditText etBirthdate;
    Button btnAddCover;

    OkHttpClient client = new OkHttpClient.Builder()
            .addNetworkInterceptor(new StethoInterceptor())
            .build();

    Gson gson = new Gson();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        etPseudo = (EditText) findViewById(R.id.pseudo);
        etEmail = (EditText) findViewById(R.id.email);
        etPassword = (EditText) findViewById(R.id.password);
        etBirthdate = (EditText) findViewById(R.id.birthdate);
        btnAddCover = (Button) findViewById(R.id.login);

        btnAddCover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("MARCHE","MARCHE");
                register();
            }
        });

    }

    private void register(){
        String email = etEmail.getText().toString();
        String pseudo = etPseudo.getText().toString();
        String password = etPassword.getText().toString();
        String birthdate = etBirthdate.getText().toString();

        Log.d("TESTE", email + pseudo + password + birthdate);

        if(StringUtils.isEmpty(email)) return;
        if(StringUtils.isEmpty(pseudo)) return;
        if(StringUtils.isEmpty(password)) return;
        if(StringUtils.isEmpty(birthdate)) return;

        RequestBody requestBody = null;
        String URL = "";


            requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("email", email)
                    .addFormDataPart("pseudo", pseudo)
                    .addFormDataPart("password", password)
                    .addFormDataPart("birthdate", birthdate)
                    .build();
            URL = ApiEndPoints.REGISTER;


        Request request = new Request.Builder()
                .url(URL)
                .post(requestBody)
                .build();

        Intent i = new Intent(this,LoginActivity.class);
        startActivity(i);

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                Log.d("Error","Error");
                RegisterActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("Main Activity", e.getMessage());
                        Toast.makeText(RegisterActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                Log.d("Good","Good");

                if (response.isSuccessful()) {
                    String result= response.body().string();
                    Log.d("Good", result);
                }
            }
        });
    }
}