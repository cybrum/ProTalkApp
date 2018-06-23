package com.example.mrmohammad.protalk;

import android.content.Intent;
import android.support.v4.widget.CircularProgressDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;


public class AllUsersActivity extends AppCompatActivity {

    Toolbar appBar;
    RecyclerView allUsersList;
    DatabaseReference dbReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_users);

        appBar = findViewById(R.id.all_users_appbar);
        setSupportActionBar(appBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("All Users");

        allUsersList = findViewById(R.id.all_users_list);
        allUsersList.setHasFixedSize(true);
        allUsersList.setLayoutManager(new LinearLayoutManager(this));

        dbReference = FirebaseDatabase.getInstance().getReference().child("User");

    }


    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<AllUsers, AllUsersViewHolder> firebaseRecyclerAdapter
                = new FirebaseRecyclerAdapter<AllUsers, AllUsersViewHolder>(
                AllUsers.class,
                R.layout.all_users_display_layout,
                AllUsersViewHolder.class,
                dbReference
        ) {
            @Override
            protected void populateViewHolder(AllUsersViewHolder viewHolder, AllUsers model, final int position) {

                viewHolder.setUser_name(model.getUser_name());
                viewHolder.setUser_status(model.getUser_status());
                viewHolder.setThumbnail(model.getThumbnail());

                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String profile_view_userId = getRef(position).getKey();
                        Intent profileViewIntent = new Intent(AllUsersActivity.this, ProfileViewActivity.class);
                        profileViewIntent.putExtra("profile_view_userId", profile_view_userId);
                        startActivity(profileViewIntent);
                    }
                });

            }
        };
        allUsersList.setAdapter(firebaseRecyclerAdapter);

    }


    public static class AllUsersViewHolder extends RecyclerView.ViewHolder {

        View mView;

        public AllUsersViewHolder(View itemView) {
            super(itemView);

            mView = itemView;
        }

        public void setUser_name(String user_name) {

            TextView name = mView.findViewById(R.id.all_users_user_name);
            name.setText(user_name);
        }

        public void setUser_status(String user_status) {
            TextView status = mView.findViewById(R.id.all_users_user_status);
            status.setText(user_status);
        }

        public void setThumbnail(String thumb_img) {
            CircleImageView profilePic = mView.findViewById(R.id.all_users_profile_img);
            Picasso.get().load(thumb_img).placeholder(R.drawable.defaultimg).into(profilePic);
        }




    }
}

