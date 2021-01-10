package com.devforxkill.androidcrowdfunding;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.google.gson.Gson;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.kbeanie.multipicker.api.CameraImagePicker;
import com.kbeanie.multipicker.api.ImagePicker;
import com.kbeanie.multipicker.api.Picker;
import com.kbeanie.multipicker.api.callbacks.ImagePickerCallback;
import com.kbeanie.multipicker.api.entity.ChosenImage;
import com.squareup.picasso.Picasso;

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.devforxkill.androidcrowdfunding.Config.ApiEndPoints;
import com.devforxkill.androidcrowdfunding.Models.Project;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Activity untuk menambahkan dan mengupdate data buku
 */
public class AddProject extends AppCompatActivity {

    String imagePath;
    String imageFileName;
    private static int EDIT_MODE = 0;
    private static int ADD_MODE = 1;
    private static final String PREFS_ID = "PREFS_ID";
    int MODE = 1;

    Project editProject;
    SharedPreferences sharedPreferences;
    String id ;

    ImagePicker imagePicker;
    CameraImagePicker cameraImagePicker;

    String TAG = getClass().getName().toString();
    private static final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/jpeg");

    @BindView(R.id.button) Button btnAddCover;
    @BindView(R.id.imageView2) ImageView imageView;
    @BindView(R.id.etISBN)
    TextInputEditText etTitle;
    @BindView(R.id.etName)
    TextInputEditText etMontant;
    @BindView(R.id.etYear)
    TextInputEditText etEnd_Date;
    @BindView(R.id.etDescription)
    TextInputEditText etDescription;
    @BindView(R.id.save) Button save;

    OkHttpClient client = new OkHttpClient.Builder()
            .addNetworkInterceptor(new StethoInterceptor())
            .build();

    Gson gson = new Gson();

    ImagePickerCallback callback = new ImagePickerCallback(){
        @Override
        public void onImagesChosen(List<ChosenImage> images) {
            // get image path
            if(images.size() > 0){
                imagePath = images.get(0).getOriginalPath();
                imageFileName = images.get(0).getDisplayName();
                Picasso.get().load(new File(imagePath)).into(imageView);
            }
        }

        @Override
        public void onError(String message) {
            // Do error handling
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_project);
        ButterKnife.bind(this); //Bind ButterKnife


        imagePicker = new ImagePicker(AddProject.this);
        cameraImagePicker = new CameraImagePicker(AddProject.this);


        imagePicker.setImagePickerCallback(callback);
        cameraImagePicker.setImagePickerCallback(callback);


        btnAddCover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickImageChoice();
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addProject();
            }
        });


        if(getIntent().getParcelableExtra("book") != null){
            editProject = getIntent().getParcelableExtra("book");
            MODE = EDIT_MODE;
            etTitle.setText(editProject.getTitle());
            etMontant.setText(editProject.getEndDate());
            etEnd_Date.setText(editProject.getMontant());
            Picasso.get().load(ApiEndPoints.BASE + editProject.getPicture()).into(imageView);

        }
    }

    private void addProject(){
        Log.d(TAG, "addProject: TEST ADD");
        String title = etTitle.getText().toString();
        String montant = etMontant.getText().toString();
        String end_date = etEnd_Date.getText().toString();
        String description = etDescription.getText().toString();
        //String picture = imageView.toString();

        if(StringUtils.isEmpty(title)) return;
        if(StringUtils.isEmpty(montant)) return;
        if(StringUtils.isEmpty(end_date)) return;

        RequestBody requestBody = null;
        String URL = "";
        sharedPreferences = getBaseContext().getSharedPreferences("PREFS", MODE_PRIVATE);

        if (sharedPreferences.contains(PREFS_ID)) {

            id = sharedPreferences.getString(PREFS_ID, null);
            Log.d("UserID", id);

        }
        requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("idUser", id )
                .addFormDataPart("title", title)
                .addFormDataPart("montant", montant)
                .addFormDataPart("end_date", end_date)
                .addFormDataPart("description", description)
                .addFormDataPart("image", imageFileName,
                        RequestBody.create(MEDIA_TYPE_PNG, new File(imagePath)))
                .build();
        URL = ApiEndPoints.ADD_PROJECT;


        Request request = new Request.Builder()
                .url(URL)
                .post(requestBody)
                .build();

        Intent i = new Intent(this,MainActivity.class);
        startActivity(i);

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                Log.d("Error","Error");
                AddProject.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("Main Activity", e.getMessage());
                        Toast.makeText(AddProject.this, e.getMessage(), Toast.LENGTH_SHORT).show();
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

    private void pickImageChoice(){

        Dexter.withActivity(this)
            .withPermissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            .withListener(new MultiplePermissionsListener() {
                @Override
                public void onPermissionsChecked(MultiplePermissionsReport report) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(AddProject.this);
                    builder.setTitle("Pick image from?");

                    String[] menus = {"Gallery", "Camera"};
                    builder.setItems(menus, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case 0:
                                    imagePicker.pickImage();
                                    break;
                                case 1:
                                    imagePath = cameraImagePicker.pickImage();
                                    break;
                            }
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }

                @Override
                public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {

                }
            }).check();
    }

    /**
     * onActivityResult untuk menghandle data yang diambil dari camera atau gallery
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK) {
            if(requestCode == Picker.PICK_IMAGE_DEVICE) {
                if(imagePicker == null) {
                    imagePicker = new ImagePicker(AddProject.this);
                    imagePicker.setImagePickerCallback(callback);
                }
                imagePicker.submit(data);
            }
            if(requestCode == Picker.PICK_IMAGE_CAMERA) {
                if(cameraImagePicker == null) {
                    cameraImagePicker = new CameraImagePicker(AddProject.this);
                    cameraImagePicker.reinitialize(imagePath);
                    cameraImagePicker.setImagePickerCallback(callback);
                }
                cameraImagePicker.submit(data);
            }
        }
    }

    /**
     * Jangan lupa handle reference path gambar agar tidak hilang saat activity restart
     * @param outState
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString("picker_path", imagePath);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey("picker_path")) {
                imagePath = savedInstanceState.getString("picker_path");
            }
        }
        super.onRestoreInstanceState(savedInstanceState);
    }

    /**
     * Buat menu SIMPAN di pojok kanan atas
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_add, menu);
        return true;
    }

    /**
     * Handle menu, ketika diklik, panggil method save()
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.save:
                addProject();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
