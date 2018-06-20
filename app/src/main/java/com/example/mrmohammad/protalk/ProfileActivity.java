package com.example.mrmohammad.protalk;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity{

    Toolbar appBar;

    CircleImageView circleImageView;
    TextView displayName;
    TextView tvStatus;
    DatabaseReference getUserDataReference;
    FirebaseAuth mAuth;
    Button addImg, editStatus;
    public static final int IMAGE_PICK = 1;
    StorageReference storageProfileImageRef;

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





        getUserDataReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String name = dataSnapshot.child("user_name").getValue().toString().trim();
                String status = dataSnapshot.child("user_status").getValue().toString().trim();
                String image = dataSnapshot.child("image").getValue().toString().trim();
                String thumbnail =dataSnapshot.child("thumbnail").getValue().toString().trim();

                displayName.setText(name);
                tvStatus.setText(status);
                Picasso.get().load(image).into(circleImageView);




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

                Intent profileIntent = new Intent(ProfileActivity.this, EditStatusActivity.class);
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

                String userId = mAuth.getCurrentUser().getUid();
                Uri resultUri = result.getUri();
                StorageReference filePath = storageProfileImageRef.child(userId + ".jpg");
                filePath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(ProfileActivity.this, "Saving image to Database.", Toast.LENGTH_LONG).show();

                            String downloadUrl = task.getResult().getDownloadUrl().toString().trim();
                            getUserDataReference.child("image").setValue(downloadUrl)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            Toast.makeText(ProfileActivity.this, "Image Uploaded", Toast.LENGTH_SHORT).show();
                                        }
                                    });


                        }else{
                            Toast.makeText(ProfileActivity.this, "Error saving image....", Toast.LENGTH_LONG).show();
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

