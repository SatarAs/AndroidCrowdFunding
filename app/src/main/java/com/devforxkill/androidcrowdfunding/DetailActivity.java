package com.devforxkill.androidcrowdfunding;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.devforxkill.androidcrowdfunding.Config.ApiEndPoints;
import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.Date;

import com.devforxkill.androidcrowdfunding.Models.Project;
import com.devforxkill.androidcrowdfunding.Models.AmountDon;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Activity pour l'affichage d'un seul projet
 */
public class DetailActivity extends AppCompatActivity {
    Project editProject;
    TextView etTitle;
    TextView etAmount;
    TextView etDescription;
    TextView etTotal;
    TextView etEnd_Date;
    Button etButton;
    ImageView etImage;
    OkHttpClient client = new OkHttpClient.Builder()
            .addNetworkInterceptor(new StethoInterceptor())
            .build();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        etTitle = findViewById(R.id.title);
        etAmount = findViewById(R.id.amount);
        etDescription = findViewById(R.id.description);
        etButton =  findViewById(R.id.don);
        etImage = findViewById(R.id.project_pic);
        editProject = (Project) getIntent().getParcelableExtra("book");
        editProject.setPicture(getIntent().getStringExtra("ImgUrl"));
        etEnd_Date = findViewById(R.id.end_date_single);
        etTotal = findViewById(R.id.total);

        etTitle.setText(editProject.getTitle());
        etAmount.setText(editProject.getMontant());
        etDescription.setText(editProject.getDescription());
        Picasso.get().load(ApiEndPoints.BASE + editProject.getPicture()).into(etImage);
        etButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)  {
                update(editProject);
            }
        });
        getProject();
        Picasso.get().load(ApiEndPoints.BASE + editProject.getPicture()).into(etImage);
        getDons();
    }


    private void getProject(){
        String URL = "";

        URL = ApiEndPoints.DETAIL + editProject.getId();

        Request request = new Request.Builder()
                .url(URL)
                .get()
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                Log.d("Error","Error");

                DetailActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("Main Activity", e.getMessage());
                        Toast.makeText(DetailActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                Log.d("onResponse","In response : " +new Gson().toJson(response.body()));
                final Gson gson = new Gson();
                assert response.body() != null;
                final Project entity = gson.fromJson(response.body().string(), Project.class);
                Log.d("API Response", String.valueOf(entity.getClass()));

                if(response.isSuccessful()){
                    try{
                        DetailActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if(response.code() == 200){
                                    Log.d("Log entity", ": "+ entity);
                                    String title = entity.getTitle();
                                    String description = entity.getDescription();
                                    String montant = "Objectif: "+ entity.getMontant() + " €";
                                    String endDate = entity.getEndDate();
                                    etTitle.setText(title);
                                    etDescription.setText(description);
                                    etAmount.setText(montant);
                                    etEnd_Date.setText("Date de fin: 25/02/2021");
                                    Log.d("Montant", "Oui" + montant);
                                }else{
                                    Toast.makeText(DetailActivity.this, "Error: "+response.code(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    } catch (JsonSyntaxException e){

                    }
                }
            }
        });
    }
    private void getDons(){
        String URL = "";

        URL = ApiEndPoints.SUMDON + editProject.getId();


        Request request = new Request.Builder()
                .url(URL)
                .get()
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                Log.d("Error","Error");

                DetailActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("Main Activity", e.getMessage());
                        Toast.makeText(DetailActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                final Gson gson = new Gson();
                assert response.body() != null;
                final AmountDon entity = gson.fromJson(response.body().string(), AmountDon.class);

                if (response.isSuccessful()) {
                    try {
                        DetailActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if(response.code() == 200){
                                    Integer montantTotal = (int)((entity.getTOTAL_AMOUNT()));
                                    etTotal.setText("Récolté : " + montantTotal.toString()+ " €");
                                }else{

                                    Toast.makeText(DetailActivity.this, "Error: "+response.code(), Toast.LENGTH_SHORT).show();
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
                    DetailActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(DetailActivity.this, "Server error", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

    }

    private void update(Project project){
        Intent intent = new Intent(this, DonActivity.class);
        intent.putExtra("project", project);
        startActivity(intent);
    }
}