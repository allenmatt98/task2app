package com.example.gallery2;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    ImageView image;
    Button add;
    private Uri imageUri;
    private File resizedImage;
    String filePath = "";
    private static final int REQUEST_CODE_ADD_IMAGE = 1000 ;
    Utility tr;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        add=(Button)findViewById(R.id.button);
        image=(ImageView)findViewById(R.id.imageView);


        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Image"), REQUEST_CODE_ADD_IMAGE);
            }
        });
    }

    @Override



    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        boolean b = tr.checkPermission(MainActivity.this);
        if (b == true) {
            if (requestCode == REQUEST_CODE_ADD_IMAGE) {
                if (resultCode == RESULT_OK) {

                    ImageView imageView = (ImageView) findViewById(R.id.imageView);
                    imageView.setVisibility(View.VISIBLE);
                    imageView.setImageURI(data.getData());
                    imageUri = data.getData();
                    String wholeID = DocumentsContract.getDocumentId(imageUri);

                    // Split at colon, use second item in the array
                    String id = wholeID.split(":")[1];

                    String[] column = {MediaStore.Images.Media.DATA};

                    // where id is equal to
                    String sel = MediaStore.Images.Media._ID + "=?";


                    Cursor cursor = getApplicationContext().getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                            column, sel, new String[]{id}, null);

                    int columnIndex = cursor.getColumnIndex(column[0]);

                    if (cursor.moveToFirst()) {
                        filePath = cursor.getString(columnIndex);


                    }
                    cursor.close();


                    resizedImage = new File(filePath);
                }
            }
        }
    }

}

