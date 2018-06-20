package com.example.mrmohammad.protalk;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity{
    Toolbar appBar;
    EditText regName, regEmail, regPassword;
    Button createAccountBtn;
    FirebaseAuth mAuth;
    ProgressDialog loading;
    DatabaseReference storeUserDataReference;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        appBar = findViewById(R.id.reg_appbar);
        setSupportActionBar(appBar);
        getSupportActionBar().setTitle("Register");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        regName = findViewById(R.id.reg_name);
        regEmail = findViewById(R.id.reg_email);
        regPassword = findViewById(R.id.reg_password);
        createAccountBtn = findViewById(R.id.create_account_btn);
        loading = new ProgressDialog(this,R.style.AppCompatAlertDialogStyle);

        createAccountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String name = regName.getText().toString().trim();
                String email = regEmail.getText().toString().trim();
                String password = regPassword.getText().toString().trim();

                registerDetails(name, email, password);


            }
        });

    }

    private void registerDetails(final String name, final String email, final String password) {

        if(TextUtils.isEmpty(name)){
            Toast.makeText(this, "The Name field cannot be left blank", Toast.LENGTH_SHORT).show();
        }
        if(TextUtils.isEmpty(email)){
            Toast.makeText(this, "The Email field cannot be left blank", Toast.LENGTH_SHORT).show();
        }

        if(TextUtils.isEmpty(password)){
            Toast.makeText(this, "The Password field cannot be left blank", Toast.LENGTH_SHORT).show();
        }

        if(password.length() < 6 || password.length() > 10){
            Toast.makeText(this, "Password must be between 6 - 10 digits.", Toast.LENGTH_SHORT).show();
        }
        else{

            loading.setTitle("Creating your Account");
            loading.setMessage("Checking your details please wait 1 moment.");
            loading.show();

            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){

                                String user_id = mAuth.getCurrentUser().getUid();
                                storeUserDataReference = FirebaseDatabase.getInstance().getReference().child("User").child(user_id);

                                storeUserDataReference.child("user_name").setValue(name);
                                storeUserDataReference.child("user_status").setValue("Hey, I'm using ProTalk");
                                storeUserDataReference.child("image").setValue("default_profile_image");
                                storeUserDataReference.child("thumbnail").setValue("default_thumbnail")
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if(task.isSuccessful()){
                                                    Intent registerIntent = new Intent(RegisterActivity.this, MainActivity.class);
                                                    registerIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                    startActivity(registerIntent);
                                                    finish();
                                                }
                                            }
                                        });


                            }else{
                                Toast.makeText(RegisterActivity.this, "Error creating account.", Toast.LENGTH_SHORT).show();
                            }

                            loading.dismiss();
                        }
                    });

        }


    }

}
