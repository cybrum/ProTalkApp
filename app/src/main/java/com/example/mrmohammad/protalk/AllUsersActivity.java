package com.example.mrmohammad.protalk;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

public class AllUsersActivity extends AppCompatActivity {

    Toolbar appBar;
    RecyclerView allUsersList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_users);

        appBar = findViewById(R.id.all_users_appbar);
        setSupportActionBar(appBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("All Users");

        allUsersList = findViewById(R.id.all_users_list);


    }
}
