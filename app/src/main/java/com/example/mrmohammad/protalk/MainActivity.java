package com.example.mrmohammad.protalk;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {


    FirebaseAuth mAuth;
    Toolbar appBar;
    ViewPager viewPager;
    TabLayout tabLayout;
    TabsPagerAdapter mPagerAdapter;
    public static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        appBar = findViewById(R.id.main_appbar);
        setSupportActionBar(appBar);
        getSupportActionBar().setTitle("ProTalk");

        viewPager = findViewById(R.id.tabs_pager);
        mPagerAdapter = new TabsPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(mPagerAdapter);
        tabLayout = findViewById(R.id.main_tabs);
        tabLayout.setupWithViewPager(viewPager);





    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser == null){

            logOutUser();

        }


    }

    private void logOutUser() {

        Intent mainIntent = new Intent(MainActivity.this, Login.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        int id = item.getItemId();

        switch(id){
            case R.id.logout_btn:
                mAuth.signOut();
                logOutUser();
                break;

            case R.id.profile_btn:
                startActivity(new Intent(MainActivity.this, ProfileActivity.class));
                break;
            case R.id.settings_btn:
                startActivity(new Intent(MainActivity.this, SettingsActivity.class));
                break;
                default:
                    Log.e(TAG, "ERROR with MenuItem..");
        }

        return true;
    }
}
