package com.example.faceless.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.faceless.R;

import java.io.ByteArrayOutputStream;

/**
 * Created by praveen goel on 10/17/2015.
 */
public class CreatePostActivity extends BaseActivity implements View.OnClickListener {

    String encodedString;
    String fileName;
    private static int RESULT_LOAD_IMG = 1;
    ProgressDialog prgDialog;

    Button uploadImage;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.f_create_post_activity_layout);

        uploadImage = (Button) findViewById(R.id.uploadimage);
        uploadImage.setOnClickListener(this);
        imageView = (ImageView) findViewById(R.id.creaeimagevi);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.uploadimage) {
            Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(galleryIntent, RESULT_LOAD_IMG);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMG && resultCode == RESULT_OK && null != data) {
            try {
                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String picturePath = cursor.getString(columnIndex);
                cursor.close();

                String fileNameSegments[] = picturePath.split("/");
                fileName = fileNameSegments[fileNameSegments.length - 1];

                Bitmap myImg = BitmapFactory.decodeFile(picturePath);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                myImg.compress(Bitmap.CompressFormat.PNG, 50, stream);
                byte[] byte_arr = stream.toByteArray();
                encodedString = Base64.encodeToString(byte_arr, 0);

                imageView.setImageBitmap(myImg);
            } catch (Exception e) {
                makeToast(e.getMessage());
            }
        }
    }
}
