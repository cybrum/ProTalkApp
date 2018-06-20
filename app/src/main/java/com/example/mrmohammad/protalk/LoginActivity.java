package com.example.mrmohammad.protalk;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.CircularProgressDrawable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class LoginActivity extends AppCompatActivity {

    Button loginBtn, registreBtn;
    Toolbar appbar;
    EditText loginEmail, loginPassword, loginRepeatPassword;
    FirebaseAuth mAuth;
    ProgressDialog loading;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        appbar = findViewById(R.id.login_appbar);
        setSupportActionBar(appbar);
        getSupportActionBar().setTitle("Login");
        mAuth = FirebaseAuth.getInstance();

        loginEmail = findViewById(R.id.login_email);
        loginPassword = findViewById(R.id.login_password);
        loginRepeatPassword = findViewById(R.id.login_repeat_password);


        loading = new ProgressDialog(this,R.style.AppCompatAlertDialogStyle);


        loginBtn = findViewById(R.id.loginBtn);
        registreBtn = findViewById(R.id.registerBtn);

        registreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = loginEmail.getText().toString().trim();
                String password = loginPassword.getText().toString().trim();

                loginUser(email, password);

            }
        });

    }

    private void loginUser(String email, String password) {

        if(TextUtils.isEmpty(email)){
            Toast.makeText(this, "Email field is empty", Toast.LENGTH_SHORT).show();
        }
        if(TextUtils.isEmpty(password)){
            Toast.makeText(this, "Password field is empty", Toast.LENGTH_SHORT).show();
        }
        String repeatPassword = loginRepeatPassword.getText().toString().trim();

        if(!password.equals(repeatPassword)){
            Toast.makeText(this, "Passwords must match...", Toast.LENGTH_SHORT).show();
        }
        else{
            loading.setTitle("Logging In");
            loading.setMessage("One moment, Please wait while we verify your details.");
            loading.show();
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if(task.isSuccessful()){
                                Intent loginIntent = new Intent(LoginActivity.this, MainActivity.class);
                                loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(loginIntent);
                                finish();
                            }else{
                                Toast.makeText(LoginActivity.this, "Please try again", Toast.LENGTH_SHORT).show();
                            }
                            loading.dismiss();
                        }
                    });
        }
    }
}
