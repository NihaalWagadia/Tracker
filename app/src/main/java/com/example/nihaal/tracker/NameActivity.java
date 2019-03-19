package com.example.nihaal.tracker;

import android.content.Intent;
import android.net.Uri;
import androidx.annotation.Nullable;

//import android.support.annotation.Nullable;
//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

import androidx.appcompat.app.AppCompatActivity;
import de.hdodenhof.circleimageview.CircleImageView;

public class NameActivity extends AppCompatActivity {
    String email, password;
    EditText e5_name;
    CircleImageView circleImageView;
    Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_name);
        e5_name = findViewById(R.id.editText_name);
        circleImageView = findViewById(R.id.circularImageImage);

        Intent myIntent = getIntent();
        if (myIntent!= null){
            email = myIntent.getStringExtra("email");
            password = myIntent.getStringExtra("password");

        }
    }

    public void generateCode(View v){

        Date myDate = new Date();
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a", Locale.getDefault());
        String date = format1.format(myDate);
        Random random = new Random();

        int n  = 100000 + random.nextInt(900000);
        String code = String.valueOf(n);

        if(uri != null ){
            Intent intent = new Intent(this, InviteCodeActivity.class);
            intent.putExtra("name",e5_name.getText().toString());
            intent.putExtra("email", email);
            intent.putExtra("password", password);
            intent.putExtra("date",date);
            intent.putExtra("isSharing", "false");
            intent.putExtra("code", code);
            intent.putExtra("imageUri", uri);
            startActivity(intent);
            finish();
        }
        else
        {
            Toast.makeText(getApplicationContext(),"Please choose an image", Toast.LENGTH_SHORT).show();

        }

    }

    public void selectImage(View v){
        Intent i = new Intent();
        i.setAction(Intent.ACTION_GET_CONTENT);
        i.setType("image/*");
        startActivityForResult(i,12);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 12 && resultCode == RESULT_OK && data != null){
            CropImage.activity()
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1,1)
                    .start(this);
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                circleImageView.setImageURI(uri);
                uri = result.getUri();
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }
}