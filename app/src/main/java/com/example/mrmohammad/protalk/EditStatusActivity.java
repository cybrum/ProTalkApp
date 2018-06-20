package com.example.mrmohammad.protalk;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EditStatusActivity extends AppCompatActivity {

    Toolbar appBar;
    EditText changeStatusET;
    Button saveChangesBtn;
    DatabaseReference statusChangedRef;
    FirebaseAuth mAuth;
    ProgressDialog loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_status);

        appBar = findViewById(R.id.edit_status_appbar);
        setSupportActionBar(appBar);
        getSupportActionBar().setTitle("Edit Status");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        loading = new ProgressDialog(this);

        String prevStatus = getIntent().getExtras().getString("status");

        changeStatusET = findViewById(R.id.change_status_et);
        changeStatusET.setText(prevStatus);
        saveChangesBtn = findViewById(R.id.save_changes_btn);
        mAuth = FirebaseAuth.getInstance();
        String user_id = mAuth.getCurrentUser().getUid();
        statusChangedRef = FirebaseDatabase.getInstance().getReference().child("User").child(user_id);

        saveChangesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String status = changeStatusET.getText().toString().trim();

                updateProfileStatus(status);
            }
        });

    }

    private void updateProfileStatus(String status) {

        if(TextUtils.isEmpty(status)){
            Toast.makeText(this, "Please update your status.....", Toast.LENGTH_SHORT).show();
        }
        else{

            loading.setTitle("Edit Status");
            loading.setMessage("Please wait while your new status is updated");
            loading.show();
            statusChangedRef.child("user_status").setValue(status)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){

                                loading.dismiss();
                                startActivity(new Intent(EditStatusActivity.this, ProfileActivity.class));
                                Toast.makeText(EditStatusActivity.this, "Status updated...", Toast.LENGTH_SHORT).show();

                            }else{
                                Toast.makeText(EditStatusActivity.this, "Error...", Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
        }


    }
}
