package com.devforxkill.androidcrowdfunding;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.devforxkill.androidcrowdfunding.Models.User;
import com.devforxkill.androidcrowdfunding.data.LoginRepository;
import com.devforxkill.androidcrowdfunding.data.model.LoggedInUser;
import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import org.apache.commons.lang3.StringUtils;

import java.io.IOException;


import com.devforxkill.androidcrowdfunding.Adapter.*;
import com.devforxkill.androidcrowdfunding.Config.ApiEndPoints;
import com.devforxkill.androidcrowdfunding.Models.APIResponse;
import com.devforxkill.androidcrowdfunding.Models.Project;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Activity pour la connexion d'un utilisateur
 */
public class LoginActivity extends AppCompatActivity {
    EditText etEmail;
    EditText etPassword;
    Button btnAddCover;
    SharedPreferences sharedPreferences;

    private static final String PREFS_AGE = "PREFS_AGE";
    private static final String PREFS_ID = "PREFS_ID";

    OkHttpClient client = new OkHttpClient.Builder()
            .addNetworkInterceptor(new StethoInterceptor())
            .build();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        etEmail = (EditText) findViewById(R.id.email2);
        etPassword = (EditText) findViewById(R.id.password2);
        btnAddCover = (Button) findViewById(R.id.login2);

        btnAddCover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)  {
                Log.d("loginClick","Ok");
                login(view);

            }
        });
    }



    private void login(final View view){
        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();

        if(StringUtils.isEmpty(email)) return;
        if(StringUtils.isEmpty(password)) return;

        RequestBody requestBody = null;
        String URL = "";


        requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("email", email)
                .addFormDataPart("password", password)
                .build();
        URL = ApiEndPoints.LOGIN;


        Request request = new Request.Builder()
                .url(URL)
                .post(requestBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                LoginActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("Main Activity", e.getMessage());
                        Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                Log.d("responseLogin","Login OK" +new Gson().toJson(response.body()));
                final Gson gson = new Gson();
                final APIResponse entity = gson.fromJson(response.body().string(), APIResponse.class);

                if (response.isSuccessful()) {
                    try {
                        LoginActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if(response.code() == 200){
                                    Toast.makeText(LoginActivity.this, "Connexion ok ! ", Toast.LENGTH_SHORT).show();
                                    sharedPreferences = getBaseContext().getSharedPreferences("PREFS", MODE_PRIVATE);


                                    sharedPreferences
                                            .edit()
                                            .putBoolean(PREFS_AGE, true)
                                            .putString(PREFS_ID, entity.getUser().getId())
                                            .apply();
                                    Intent show = new Intent(view.getContext(), MainActivity.class);

                                    startActivity(show);
                                    finish();
                                }else{

                                    Toast.makeText(LoginActivity.this, "Error: "+response.code(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    } catch (JsonSyntaxException e) {
                        e.printStackTrace();

                        Log.e("MainActivity", "JSON Errors:"+e.getMessage());
                    } finally {
                        response.body().close();
                    }

                } else {
                    LoginActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(LoginActivity.this, "Server error", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

    }
}