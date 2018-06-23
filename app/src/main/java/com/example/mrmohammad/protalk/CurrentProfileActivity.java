package com.example.mrmohammad.protalk;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;

public class CurrentProfileActivity extends AppCompatActivity{

    Toolbar appBar;

    CircleImageView circleImageView;
    TextView displayName;
    TextView tvStatus;
    DatabaseReference getUserDataReference;
    FirebaseAuth mAuth;
    Button addImg, editStatus;
    public static final int IMAGE_PICK = 1;
    StorageReference storageProfileImageRef;
    StorageReference thumbImgRef;
    ProgressDialog loading;

    Bitmap thumb_bitmap = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_prof);
        appBar = findViewById(R.id.profile_appbar);
        setSupportActionBar(appBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Profile");
        addImg = findViewById(R.id.add_photo);
        circleImageView = findViewById(R.id.circle_img);
        displayName = findViewById(R.id.tv_display_name);
        tvStatus = findViewById(R.id.tv_status);
        editStatus = findViewById(R.id.edit_status_btn);
        mAuth = FirebaseAuth.getInstance();
        String user_id= mAuth.getCurrentUser().getUid();
        getUserDataReference = FirebaseDatabase.getInstance().getReference().child("User").child(user_id);
        storageProfileImageRef = FirebaseStorage.getInstance().getReference().child("Profile_Images");
        thumbImgRef = FirebaseStorage.getInstance().getReference().child("Thumb_Images");

        loading = new ProgressDialog(this,R.style.AppCompatAlertDialogStyle);





        getUserDataReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String name = dataSnapshot.child("user_name").getValue().toString().trim();
                String status = dataSnapshot.child("user_status").getValue().toString().trim();
                String image = dataSnapshot.child("img").getValue().toString().trim();
                String thumbnail =dataSnapshot.child("thumbnail").getValue().toString().trim();

                displayName.setText(name);
                tvStatus.setText(status);
                if(!image.equals("default_profile_image")){

                    Picasso.get().load(image).placeholder(R.drawable.defaultimg).into(circleImageView);

                }








            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        addImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent imageIntent = new Intent();
                imageIntent.setAction(Intent.ACTION_GET_CONTENT);
                imageIntent.setType("image/*");
                startActivityForResult(imageIntent, IMAGE_PICK);

            }
        });

        editStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String prevStatus = tvStatus.getText().toString().trim();

                Intent profileIntent = new Intent(CurrentProfileActivity.this, EditStatusActivity.class);
                profileIntent.putExtra("status", prevStatus);
                startActivity(profileIntent);

            }
        });








    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == IMAGE_PICK && resultCode == RESULT_OK && data != null){
            Uri imageUri= data.getData();
            CropImage.activity(imageUri)
                    .setAspectRatio(1,1)
                    .start(this);


        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {

                loading.setTitle("Updating Profile Image");
                loading.setMessage("Please wait 1 moment, updating your Profile Image...");
                loading.show();

                Uri resultUri = result.getUri();
                File thumb_filePathUri = new File(resultUri.getPath());

                try{

                    thumb_bitmap = new Compressor(this)
                            .setMaxHeight(200)
                            .setMaxWidth(200)
                            .setQuality(50)
                            .compressToBitmap(thumb_filePathUri);

                }catch(IOException e){
                    e.printStackTrace();
                }

                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                thumb_bitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);

                final byte[] thumb_byte = byteArrayOutputStream.toByteArray();



                String userId = mAuth.getCurrentUser().getUid();

                StorageReference filePath = storageProfileImageRef.child(userId + ".jpg");
                final StorageReference thumb_filePath = thumbImgRef.child(userId + ".jpg");


                filePath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull final Task<UploadTask.TaskSnapshot> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(CurrentProfileActivity.this, "Saving image to Database.", Toast.LENGTH_LONG).show();

                            final String downloadUrl = task.getResult().getDownloadUrl().toString().trim();

                            UploadTask uploadTask = thumb_filePath.putBytes(thumb_byte);

                            uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> thumbTask) {
                                    String thumb_downloadUrl = thumbTask.getResult().getDownloadUrl().toString().trim();
                                    if(task.isSuccessful()){



                                        Map update_userData = new HashMap();
                                        update_userData.put("img", downloadUrl);
                                        update_userData.put("thumbnail", thumb_downloadUrl);

                                        getUserDataReference.updateChildren(update_userData)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        Toast.makeText(CurrentProfileActivity.this, "Image Uploaded", Toast.LENGTH_SHORT).show();


                                                        loading.dismiss();
                                                    }
                                                });


                                    }
                                }
                            });





                        }else{
                            Toast.makeText(CurrentProfileActivity.this, "Error saving image....", Toast.LENGTH_LONG).show();

                            loading.dismiss();
                        }
                    }
                });
            }
            else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {

                Exception error = result.getError();

                }
        }

        }

    }

